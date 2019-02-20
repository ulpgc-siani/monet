/*
 * 
 * Jsmtpd, Java SMTP daemon
 * Copyright (C) 2005  Jean-Francois POUX, jf.poux@laposte.net
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */
package org.jsmtpd.plugins.deliveryServices;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsmtpd.core.common.delivery.FatalDeliveryException;
import org.jsmtpd.core.common.delivery.TemporaryDeliveryException;
import org.jsmtpd.core.common.io.BareLFException;
import org.jsmtpd.core.common.io.InputSizeToBig;
import org.jsmtpd.core.common.io.commandStream.MultiLineCommandStreamParser;
import org.jsmtpd.core.mail.Email;
import org.jsmtpd.core.mail.Rcpt;
import org.jsmtpd.tools.Base64Helper;

/**
 * This class chats with a remote smtp server to send mail for the delivery service
 * @author Jean-Francois POUX
 * <br><br>
 * 7/03/2005<br>
 * changed Email type, adapted sending it
 *
 */
public class SmtpSender {

    /**
     * hostname of our server
     */
    private String smtpHost;
    /**
     * Email to process
     */
    private Email e;
    /**
     * rcpts of the mail
     */
    @SuppressWarnings("rawtypes")
	private List rcpts;
    /**
     * remote smtp server
     */
    private Inet4Address server;

    private Log log=LogFactory.getLog(SmtpSender.class);
    /**
     * socket to connect to the remote server
     */
    private Socket sock = null;

    /**
     * remote smtp server port
     */
    private int serverPort;
    
    /**
     * writer of the socket
     */
    private BufferedWriter wr = null;

    private MultiLineCommandStreamParser csp;
    
    private int connectionTimeout;
    
    private String login;
    private String password;
    private String authMethod="none";
    private String helloCommand="HELO";
    /**
     * Use this constructor for anonymous sending
     * @param smtpHost
     * @param in
     * @param server
     * @param rcpts
     * @param connectionTimeout
     */
    @SuppressWarnings("rawtypes")
	public SmtpSender(String smtpHost, Email in, Inet4Address server, int serverPort, List rcpts, int connectionTimeout) {
        this.smtpHost = smtpHost;
        this.e = in;
        this.server = server;
        this.rcpts = rcpts;
        this.connectionTimeout=connectionTimeout;
        this.serverPort=serverPort;
    }
    /**
     * Use this constructor to try to auth before sending
     * @param smtpHost
     * @param in
     * @param server
     * @param rcpts
     * @param connectionTimeout
     * @param authMethod
     * @param login
     * @param password
     */
    @SuppressWarnings("rawtypes")
	public SmtpSender(String smtpHost, Email in, Inet4Address server, int serverPort, List rcpts, int connectionTimeout,String authMethod,String login, String password) {
        this.smtpHost = smtpHost;
        this.e = in;
        this.server = server;
        this.rcpts = rcpts;
        this.connectionTimeout=connectionTimeout;
        this.login=login;
        this.password=password;
        this.authMethod=authMethod;
        this.serverPort=serverPort;
    }
    
    private void performAuth () throws TemporaryDeliveryException, FatalDeliveryException {
    	if ((authMethod==null)||(authMethod.equals("none")))
    		return;
    	
    	if (authMethod.equals("plain")) {
    		log.debug("Performing authentication ...");
    		ByteArrayOutputStream bos = new ByteArrayOutputStream();
    		try {
				bos.write(login.getBytes());
				bos.write('\0');				
				bos.write(password.getBytes());
			} catch (IOException e) {
			}
    		
    		String lpEncoded = Base64Helper.encode(bos.toByteArray());
    		send("AUTH PLAIN "+lpEncoded);
    		String response = receive();
    		log.debug("Auth replied: "+response);
    		if ((response!=null)&&(response.startsWith("235"))) {
    			log.debug("Authenticated as "+login);
    			return;
    		} else {
    			log.warn("Remote server rejected authentication: "+response);
    			throw new FatalDeliveryException("Remote host rejected authentication");
    		}
    	}
    	
    }
    // Hack for non rfc compliant servers
    private List<String> multilineReceive () throws TemporaryDeliveryException, FatalDeliveryException {
        List<String> response = new ArrayList<String>();
        while (true) {
            String buffer = receive();
            if ((buffer==null)||buffer.equals(""))
                    throw new TemporaryDeliveryException("Remote server issued a null response");
             
            response.add(buffer);
            if (buffer.charAt(3)==' ') // esmtp end of response
                break;
            if (buffer.charAt(3)!='-') // if third char is not a space or -, there's a problem
                throw new TemporaryDeliveryException("Server issued somthing I don't understaind.");
        }
        return response;
    }
    /**
     * Chats with the server to deliver the mail
     * @throws TemporaryDeliveryException
     * @throws FatalDeliveryException
     */
    @SuppressWarnings("rawtypes")
	public void doDelivery() throws TemporaryDeliveryException, FatalDeliveryException {
        Stack<Rcpt> successfullRcpt=new Stack<Rcpt>();
        int respCode;
        String respString;
        try {
            init();
        } catch (IOException ioe) {
            log.warn("Error connecting to " + server.toString());
            closeConnection();
            throw new TemporaryDeliveryException("Error connecting to " + server.toString(),ioe);
        }

        try {
            // This should skip any extended (multiline) responses...
            List<String> responses = multilineReceive();
            if (responses.size()==0) {
                log.warn("Error while chatting with server. No response(s)");
                throw new TemporaryDeliveryException("Error chatting with " + server.toString()+", I could not read a response to connection");
            }
            if (parseResponse(responses.get(responses.size()-1)) != RESP_HELO_FIRST) {
                log.warn("Error while chatting with server");
                throw new TemporaryDeliveryException("Error chatting with " + server.toString()+", I could not find a valid welcome response from server");
            }

            
            
            /*
             * Comments are excepted behavior described in rfc 821
             * S: Succes
             * E: Error
             * F: fault (retry)
             * 
             */

            /* 	Todo, but usefull ?
             *  S: 250
             *  E: 500, 501, 504, 421
             *  500, syntax error, command unkown
             * 	501, syntax error in params 
             *  504, not implemented
             *  421, domain not available (remote server is shutting down) (RETRY)
             * 
             * 	=> Retry on any failure
             * 
             */

            send(helloCommand+" " + smtpHost);         
            /**
             * We issue a HELO command, the server should respond with a single response code (rfc 821). 
             * Some do send extended smtp responses anyway.
             */
            
            responses = multilineReceive();
            if (responses.size()==0) {
                log.warn("Error while chatting with server. No response(s)");
                throw new TemporaryDeliveryException("Error chatting with " + server.toString()+", server did not respond to my HELO command (no response)");
            }
            if ((parseResponse(responses.get(responses.size()-1))!=RESP_OK)) {
                    log.warn("Temporary Error while chatting with server (" + server.getHostAddress() + ") for delivering mail " + e.getDiskName()
                            + ", server said: " + ((responses.size()>0) ? responses.get(responses.size()):"") );
                    throw new TemporaryDeliveryException("Error chatting with " + server.toString()+", Error in HELO command (server did not sent me a ok response for helo command)");
            }
            
            /*
            while (true) {
                respString = receive();
                if ((respString==null))
                    throw new TemporaryDeliveryException("Error chatting with " + server.toString()+", Error in HELO command, cmd=null");
                if (respString.equals("250"))
                    break;
                if ((respString.length()>=5) &&(respString.charAt(3)!='-'))
                    break;
            }
            if (parseResponse(respString) != RESP_OK) {
                log.log(Level.WARN, "Temporary Error while chatting with server (" + server.getHostAddress() + ") for delivering mail " + e.getDiskName()
                        + ", server said: " + respString);
                throw new TemporaryDeliveryException("Error chatting with " + server.toString()+", Error in HELO command");
            }
            */
            
            performAuth();
            
            /*	Done
             *  S: 250
             *  F: 552, 451, 452
             *  E: 500, 501, 421
             * 
             * 	552, remote server disk full (Don't RETRY, see rfc 1893)
             *  451, process error (RETRY ?)
             *  452, system too loaded (RETRY)
             * 
             * 	E see 
             */
            if (e.getFrom().toString().equals("<>"))
                send("MAIL FROM:" + e.getFrom() + ""); // Bounce
            else
                send("MAIL FROM:<" + e.getFrom() + ">");

            respString = receive();
            respCode = parseResponse(respString);

            if (respCode != RESP_OK) {
                if ((respCode == 451) || (respCode == 452)) {
                    log.warn("Temporary Error while chatting with server (" + server.getHostAddress() + ") for delivering mail " + e.getDiskName()
                            + ", command=MAIL FROM, server said: " + respString);
                    throw new TemporaryDeliveryException("Error chatting with " + server.toString()+", could not issue mail from command");
                } else {
                    log.warn("Fatal Error while chatting with server (" + server.getHostAddress() + ") for delivering mail " + e.getDiskName()
                            + ", command=MAIL FROM, server said: " + respString);
                    for (Iterator iter = rcpts.iterator(); iter.hasNext();) {
						Rcpt element = (Rcpt) iter.next();
						element.setLastError(respString);
					}
                    throw new FatalDeliveryException("Error while sending FROM to server "+server.toString()+", server replied "+respString);
                }
            }

            /*	Done
             *  S: 250, 251
             *  F: 550, 551, 552, 553, 450, 451, 452
             *  E: 500, 501, 503, 421
             * 
             *  251, user not local (remote SMTP will forward)
             *  
             *  500, syntax error, command unkown
             *  501, syntax error in params 
             *  552  remote server disk full (RETRY)
             *  553  Error in mailbox syntax (don't retry rfc 1893)
             *  450  Mailbox not available (RETRY)
             *  452 system too loaded (RETRY)
             * 
             *  500, syntax error, command unkown
             * 	501, syntax error in params 
             *  503, syntax incorrect (not good order in params)
             *  421, domain not available (remote server is shutting down) (RETRY)
             * 
             */
            Set<Rcpt> tempFailure = new HashSet<Rcpt>();
            for (Iterator iter = rcpts.iterator(); iter.hasNext();) {
                Rcpt oneRcpt = (Rcpt) iter.next();
                send("RCPT TO:<" + oneRcpt.getEmailAddress().toString() + ">");
                respString = receive();
                respCode = parseResponse(respString);
                if (respCode != RESP_OK) {
                    // Removed (respCode == 550) || (respCode == 551) || (respCode == 552), 5xx errors are fatal.
                    if ( (respCode == 450) || (respCode == 451) || (respCode == 452)) {
                        log.warn("RSMPT> Temporary Error while chatting with server (" + server.getHostAddress() + ") for delivering mail " + e.getDiskName()
                                + ", command=RCPT " + oneRcpt.getEmailAddress().toString() + ", server said: " + respString);
                        oneRcpt.setDelivered(Rcpt.STATUS_ERROR_NOT_FATAL);
                        oneRcpt.setLastError("Recipient rejected: "+respString);
                        tempFailure.add(oneRcpt);
                    } else {
                        log.warn("Fatal Error while chatting with server (" + server.getHostAddress() + ") for delivering mail " + e.getDiskName()
                                + ", command=RCPT " + oneRcpt.getEmailAddress().toString() + ", server said: " + respString);
                        oneRcpt.setDelivered(Rcpt.STATUS_ERROR_FATAL);
                        oneRcpt.setLastError("Remote server permanently rejected recipient : "+respString);
                    }
                } else {
                    successfullRcpt.push(oneRcpt); // If delivery is commited, we will update theses later.
                }
            }
            
            if ((successfullRcpt.size()==0) && (tempFailure.size()==0) ){
                log.warn("all recipient(s) where rejected");
                throw new FatalDeliveryException("All recipient where rejected, and none is in temporary error.");
            }
            
            if ((successfullRcpt.size()==0) && (tempFailure.size()>0) ){ // no rcpt is valid to send any data.
                log.warn("There is no valid recipient this time. Abort chat");
                return;
            }
            
            /*
             * Still todo here.
             * I: 354 -> data -> S: 250
             *					 F: 552, 554, 451, 452
             *  F: 451, 554
             *  E: 500, 501, 503, 421
             * 
             * 
             *  552, remote server disk full (RETRY)
             *  554, transaction failed (RETRY)
             *  451, process error (RETRY ?)
             *  452, system too loaded (RETRY)
             *  
             * 
             * 
             */

            send("DATA");
            respString=receive();
            if (parseResponse(respString) != RESP_DATA_OK) {
            	if (respString.startsWith("5")) {
            		log.warn("Fatal error while chatting with server during DATA");
                    for (Iterator iter = rcpts.iterator(); iter.hasNext();) {
						Rcpt element = (Rcpt) iter.next();
						element.setLastError(respString);
					}
                    throw new FatalDeliveryException("Could not send data command to server, received a permanent error while sending data : "+respString);
            	} else {
            		log.warn("Temporary error while chatting with server during DATA");
                    throw new TemporaryDeliveryException("Temporary error while sending mail data command : "+respString);
            	}
                
            }
            try {
                sock.getOutputStream().write(e.getDataAsByte());
                send("\r\n."); // will send <CRLF>.<CRLF>
            } catch (IOException e2) {
                log.warn("RSMPT> Error while chatting with server during DATA");
                throw new TemporaryDeliveryException("Error while transmitting data, IO Error: "+e2.getMessage());
            }

            respString = receive();
            if (parseResponse(respString) != RESP_OK) {
            	if (respString.startsWith("5")) { // 5xx responses mean failure, rfc 1893
            		log.warn("Fatal error while chatting with server while ending data, response = "+respString);
                    for (Iterator iter = rcpts.iterator(); iter.hasNext();) {
						Rcpt element = (Rcpt) iter.next();
						element.setLastError(respString);
					}
            		throw new FatalDeliveryException("Fatal Error send mail data: "+respString);
            	} else {
            		log.warn("Temporary error while chatting with server while ending data, response = "+respString);
            		throw new TemporaryDeliveryException("Temporary error sending mail data : "+respString);
            	}
            } else {
                while (!successfullRcpt.empty()){
                    Rcpt tmp = (Rcpt)successfullRcpt.pop();
                    tmp.setDelivered(Rcpt.STATUS_DELIVERED);
                }
            }

            try {
                send("QUIT");
                respString=receive();
                if (parseResponse(respString) != RESP_END) {
                    log.warn("Error while chatting with server during end connection, last response = "+respString);
                    //throw new TemporaryDeliveryException(); // Throw => requeue mail, but it is accepted at this point.
                }
            } catch (TemporaryDeliveryException e1) {
                log.error("Looks like remote server ended connection !(mail was accepted anyway)",e1);
            }
        } catch (TemporaryDeliveryException e) {
            closeConnection();
            throw new TemporaryDeliveryException(e);
        } catch (FatalDeliveryException e) {
            closeConnection();
            throw new FatalDeliveryException(e);
        }
        closeConnection();
    }

    /**
     * Clean up the object
     *
     */
    private void closeConnection() {
        try {
            if (wr != null)
                wr.close();
            if (sock != null)
                sock.close();
        } catch (IOException e) {}
    }

    /**
     * Connects to the remote smtp server
     * @throws IOException
     */
    public void init() throws IOException {
        sock = new Socket();
        sock.setSoTimeout(connectionTimeout*1000);
        SocketAddress sockaddr = new InetSocketAddress(server, serverPort);
        sock.connect(sockaddr);
        csp = new MultiLineCommandStreamParser(sock.getInputStream(), 512, false);
        wr = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
    }

    /**
     * Sends a command
     * @param msg the command
     * @throws TemporaryDeliveryException
     */
    private void send(String msg) throws TemporaryDeliveryException {
        /**
         * Strict RFC
         * if we find a lone LF, replace it with CRLF
         */
        String res = msg.replaceAll("[^\r]\n", "\r\n");
        try {
            wr.write(res + "\r\n");
            wr.flush();
            log.debug("Sent: " + res.replaceAll("\r", "<CR>").replaceAll("\n", "<LF>") + "<CR><LF>");
        } catch (IOException e) {
            log.error("I/O error while trying to send "+msg,e);
            throw new TemporaryDeliveryException(e);
        }
    }

    /**
     * Gets a command string from the stream
     * @return
     * @throws TemporaryDeliveryException
     */
    private String receive() throws TemporaryDeliveryException, FatalDeliveryException {
        String rec;
        try {
            rec = csp.readLine();
            log.debug("Received: " + rec);
            return rec;
        } catch (InputSizeToBig e) {
            log.error("RemoteSender connected to " + server.toString() + " received a response > 512 bytes.");
            throw new FatalDeliveryException();
        } catch (IOException e) {
            throw new TemporaryDeliveryException();
        } catch (BareLFException e) {
            throw new TemporaryDeliveryException();
        }
    }

    private int parseResponse(String cmd) {
        
        if (cmd==null)
            return RESP_UNKW;
        
        if (cmd.startsWith("220"))
            return RESP_HELO_FIRST;

        if (cmd.startsWith("250"))
            return RESP_OK;

        if (cmd.startsWith("354"))
            return RESP_DATA_OK;

        if (cmd.startsWith("221"))
            return RESP_END;

        if (cmd.length() > 4)
            return Integer.parseInt(cmd.substring(0, 3));

        return RESP_UNKW;
    }

    private static final int RESP_HELO_FIRST = 0; // received inital 220 welcome
    private static final int RESP_OK = 1;
    private static final int RESP_DATA_OK = 2;
    private static final int RESP_END = 3;
    private static final int RESP_UNKW = -1;

	public void setLogin(String login) {
		this.login = login;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAuthMethod(String authMethod) {
		this.authMethod = authMethod;
	}
	
	public void setHelloCommand(String helloCommand) {
		this.helloCommand = helloCommand;
	}

	
}