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
package org.jsmtpd.core.receive;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsmtpd.config.ReadConfig;
import org.jsmtpd.core.common.PluginStore;
import org.jsmtpd.core.common.acl.IACL;
import org.jsmtpd.core.common.io.BareLFException;
import org.jsmtpd.core.common.io.InputSizeToBig;
import org.jsmtpd.core.common.io.InvalidStreamParserInitialisation;
import org.jsmtpd.core.common.io.commandStream.CommandStreamParser;
import org.jsmtpd.core.common.io.dataStream.DataStreamParser;
import org.jsmtpd.core.common.smtpExtension.IProtocolHandler;
import org.jsmtpd.core.common.smtpExtension.ISmtpExtension;
import org.jsmtpd.core.common.smtpExtension.SmtpExtensionException;
import org.jsmtpd.core.mail.Email;
import org.jsmtpd.core.mail.EmailAddress;
import org.jsmtpd.core.mail.InvalidAddress;
import org.jsmtpd.core.mail.Rcpt;
import org.jsmtpd.core.send.QueueService;
import org.jsmtpd.tools.DateUtil;

/**
 * @author Jean-Francois POUX
 * Jsmtp<BR>
 * 02/04/2005<br>
 * Problems with some mailing list managers, when trying to suscribe, so :<br>
 * Increased limit of user size to 512o<br>
 * Increased limit of domain size to 512o<br>
 * 
 * 
 * 16/03/2005 <br>
 * Fixed a bugg that could cause the server to crash by consuming all its memory
 * while parsing random ascii injection. (removed buffered reader) <br>
 * Check for MAX RCPT and adresses sizes.
 * 
 * 
 * Chat with smtp sender
 * Checks acl
 * Write message to delivery service
 * 
 * <br>
 * RFC 821 :
 * 	user : 		64 byte => add to emailaddress parser  [done]
 *  domain : 	64 byte => add to emailaddress parser  [done]
 *  route :		256 byte, route NA in this implem => err 501 
 *  command:	512 byte max => err 500 [done]
 *  response:	512 byte max OK
 * 	dataline:	1000 max no problem
 * 	max rcpt:	100 => err 552s [done]
 * <br><br>
 * 7/3/2005<br>
 * Changed Email class, so changed the way to handle DATA cmd<br>
 * raw byte reading<br>
 * 
 */
public class ProtocolHandler implements IProtocolHandler {

    /**
     * The tcp socket where the exchange takes place
     */
    private Socket sock = null;

    /**
     * Writer, from the socket
     */
    private BufferedWriter wr = null;
    /**
     * Email instance to store data, rcpt & from mainly
     */
    private Email mail = new Email();

    /**
     * Maximum message size, in ko.
     */
    private int maxMessageSize = ReadConfig.getInstance().getIntegerProperty("maxMessageSize");
    /**
     * Connection will fail after this timeout
     */
    private int timeout = ReadConfig.getInstance().getIntegerProperty("connectionTimeout");
    /**
     * ACL pluggin to use to check if this mail is to be accepted or not
     */
    private IACL acl = PluginStore.getInstance().getAcl();

    private Log log = LogFactory.getLog(ProtocolHandler.class);
    /**
     * Remote ip of the client connected to the smtp session
     */
    private String remote = "";
    /**
     * Our hostname
     */
    private String localHost = ReadConfig.getInstance().getProperty("localHost");

    /**
     * Once a mail is received and validated, it is placed in this service
     * that will deliver it
     */
    private QueueService dsvc = QueueService.getInstance();

    /**
     * relay remote host ?
     */
    private boolean relayed=false;
    
    /**
     * is communication layer secured by any means ?
     */
    private boolean secured=false;
    
    private CommandStreamParser csp;
    
    private InputIPFilterChecker checker = new InputIPFilterChecker();
    
    private List<ISmtpExtension> smtpExtensions=PluginStore.getInstance().getSmtpExtensions();
    
    private List<String> commandHistory;
    /**
     * Listening thread queries the thread pool to receive an instance of this class, 
     * then passes it the socket resulting from the accept method
     * This methods invoques the chat protocol with the client.
     * @param sock the socket to chat with the connected client
     */
    
    private String authContext = null;
    
    private int maxRcpt =ReadConfig.getInstance().getIntegerProperty("maxRcpt");
    
    private int errorCount=0;
    
    public int getErrorCount() {
		return errorCount;
	}

	public void init(Socket sock,boolean secured) {
        commandHistory=new LinkedList<String>();
        remote = ((InetSocketAddress) sock.getRemoteSocketAddress()).getAddress().getHostAddress(); // get client hostname

        //Ensure any previous state is cleared
        closeConnection();
        reset();
        this.sock = sock;
        this.secured=secured;
        try {
            sock.setSoTimeout(timeout * 1000); // Timeout comes from the config file
            wr = new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
            // Run the chat with the client
            runDialog();
        } catch (IOException e) {
            log.error("Network error for " + remote);
        } catch (EndofProtocol e) {
            log.info("End service for " + remote);
        } finally {
            closeConnection();
            reset();
        }
    }

    /**
     * Chats with the client
     * @throws IOException Network error
     * @throws EndofProtocol Client is disconnecting
     */
    private void runDialog() throws IOException, EndofProtocol {
        // Count errors, if it exceeds a threshold, the connection is droped because someone
        // is probably trying to do something not allowed
        errorCount = 0;
        // The last command issued
        int lastCommand = -1;
        // Current command
        int command;

        if (!checker.checkIPAgainstFilters(sock.getInetAddress())) {
            send(MSG_BLACKLISTED);
            return;
        }

        log.info( "Service for " + remote + " running");
        send(MSG_HELLO_CODE + localHost + MSG_HELLO_MESG); // Send the : HELO serverHostname.com Welcome to jsmtpd
        mail.setReceivedFrom(remote); // Email type records where the connection came from (for filtering later)
        
        csp = new CommandStreamParser(sock.getInputStream(), 4096, true);
        String commandString;
        try {
            while ((commandString = csp.readLine()) != null)
            {
            	if (errorCount > 10) {
            		log.error("Max err count reached, ciao");
                    return;
            	}
                command = getCommand(commandString);
                log.debug("Command: " + commandString);
                try {
                    if (executePreExtensions(commandString,this))
                        continue;
                } catch (SmtpExtensionException e1) {
                   log.error("Failed to execute smtp extension");
                   break;
                } 
                switch (command) {
                case CMD_EHLO:
                    if (smtpExtensions.size()<=0) {
                        send("250 Command ok");
                    } else {
                        send("250-ok");
                        for (ISmtpExtension ext : smtpExtensions) {
                            if ( (ext.getWelcome()!=null) && (!ext.getWelcome().equals("")))
                                send ("250-"+ext.getWelcome());
                        }
                   		send ("250 HELP");
                    }
                    lastCommand = CMD_HELLO;
                    break;
                case CMD_HELLO:
                    send("250 Command ok");
                    lastCommand = CMD_HELLO;
                    break;
                case CMD_QUIT:
                    lastCommand = CMD_RESET;
                    throw new EndofProtocol();
                case CMD_NOOP:
                    send(MSG_OK);
                    break;
                case CMD_RESET:
                    mail = new Email();
                    mail.setReceivedFrom(remote);
                    mail.setAuthContext(authContext); // If we have a auth context, transmit it;
                    send(MSG_OK);
                    lastCommand = CMD_RESET;
                    break;
                case CMD_MAIL_FROM:
                    if (lastCommand == CMD_RESET || lastCommand == CMD_HELLO) {
                        if (decodeFrom(commandString))
                            lastCommand = CMD_MAIL_FROM;
                    } else
                        send(MSG_CMD_NOT_ALLOWED);
                    break;

                case CMD_RCPT:
                    if (lastCommand == CMD_MAIL_FROM || lastCommand == CMD_RCPT) {
                        if (decodeRcpt(commandString))
                            lastCommand = CMD_RCPT;
                    } else {
                        send(MSG_CMD_NOT_ALLOWED);
                    }
                    break;
                    
                case CMD_HELP:
                        send ("214 See rfc 2821");
                        lastCommand=CMD_HELP;
                    break;
                    
                case CMD_DATA:
                    if (lastCommand == CMD_RCPT) {
                        parseData();
                        mail = new Email();
                        mail.setReceivedFrom(remote);
                        mail.setAuthContext(authContext); // transmit auth ctx
                        lastCommand = CMD_RESET;
                    } else {
                        send(MSG_CMD_NOT_ALLOWED);
                    }
                    break;
                    
                default:
                    try {
                            if (!executeExtensions(commandString,this)) {
                                send(MSG_INVALID_CMD);
                                log.error("Invalid command: " + commandString + " from " + remote);
                                errorCount++;
                                break;
                            }
                        } catch (IOException e) {
                            throw new EndofProtocol();
                        } catch (SmtpExtensionException e) {
                            log.error("Error executing SMTP Extensions");
                            errorCount++;
                        } 
                }
            }

        } catch (InputSizeToBig e) {
            send(MSG_CMD_TO_BIG);
            return;
        } catch (BareLFException e) {
            send(MSG_ERROR_LF);
            return;
        }
    }

    private boolean executeExtensions (String cmd, IProtocolHandler protocol) throws SmtpExtensionException, IOException, InputSizeToBig, BareLFException {
    	for (ISmtpExtension extension : smtpExtensions) {
    		if (extension.smtpTrigger(cmd,protocol))
                return true;
		}
        return false;
    }
    
    private boolean executePreExtensions (String cmd, IProtocolHandler protocol) throws SmtpExtensionException, IOException, InputSizeToBig, BareLFException {
        boolean over=false;
        for (ISmtpExtension extension : smtpExtensions) {
            if (extension.smtpPreTrigger(cmd,protocol))
                over=true;
        }
        return over;
    }
    
    private void parseData() throws IOException {
        send(MSG_DATA_BEGIN);
        mail.setArrival(new Date());

        try {
            DataStreamParser dsp = new DataStreamParser(1024 * 512, 1024 * 1024 * maxMessageSize);
            List<Rcpt> rcpts = mail.getRcpt();
            String rc = "";
            for (Rcpt rcpt : rcpts) {
                 rc += "<" + rcpt.getEmailAddress().toString() + ">;";
			}
            dsp.appendString("Received: from " + remote + " by " + localHost + " (Jsmtpd) for " + rc + " " + DateUtil.currentRFCDate());

            try {
                dsp.parseInputStream(sock.getInputStream());
                mail.setDataBuffer(dsp.getData());
                if (!dsvc.queueMail(mail))
                    send(MSG_NO_SPACE_LEFT);
                else
                    send(MSG_OK);
            } catch (InputSizeToBig e1) {
                send(MSG_ERROR_SIZE);
            } catch (IOException e1) {
                throw e1;
            } catch (BareLFException e1) {
                send(MSG_ERROR_LF);
            }

        } catch (InvalidStreamParserInitialisation e) {
            send(MSG_SAVE_ERROR);
        }

    }

    private boolean decodeRcpt(String rcpt) throws IOException {
        if ((rcpt == null) || (rcpt.length() < 10))
            return false;

        String rc = rcpt.substring(9).trim().replace("<", "").replace(">", "");
        EmailAddress e = null;
        try {
            e = EmailAddress.parseAddress(rc);

        } catch (InvalidAddress e1) {
            send(MSG_USER_INVALID);
            return false;
        }
        if (isAcceptable(e)) {
            // Todo : check user ?
            if (mail.getRcpt().size() >= maxRcpt) {
                send(MSG_TO_MANY_RCPT);
                return false;
            } else {
                mail.addRcpt(e);
                send(MSG_OK);
                return true;
            }
        } else {
            send(MSG_USER_UNKOWN); // you are not for local domains and connection is not to be relayed.
            return false;
        }

    }


    
    private boolean decodeFrom(String from) throws IOException {
        if ((from == null) || (from.length() < 11))
            return false;
        String fr = from.substring(10).trim();
        if (fr.contains("<>")) {
            EmailAddress e = new EmailAddress();
            e.setUser("<>");
            mail.setFrom(e);
            send(MSG_OK);
            return true;
        }

        fr = fr.replace("<", "").replace(">", "");
        EmailAddress e = null;
        try {
            e = EmailAddress.parseAddress(fr);
            mail.setFrom(e);
            send(MSG_OK);
            return true;

        } catch (InvalidAddress ia) {
            send(MSG_USER_INVALID);
            return false;
        }

    }

    private boolean isAcceptable(EmailAddress addr) {
    	
    	boolean isValidAddress = acl.isValidAddress(addr);    	
    	
    	if (acl.isLocalDomain(addr.getHost())){
    		if (isValidAddress){
    			log.debug("RCPT: " + addr + " is valid: local domain & valid address");
                return true;
    		} else {
    			log.debug("RCPT: " + addr + " is not valid: local domain but not valid address");
                return false;
    		}	
    	} else {
    		 // Mail is sent from relayed hosts
    		if (acl.isValidRelay(mail.getReceivedFrom())) {
                log.debug("RCPT: " + addr + " is valid for relayed host :" + mail.getReceivedFrom());
                return true;
            }
    		// user is authentified by external mecanism
            if (relayed) {
                log.debug("RCPT: " + addr + " is valid for relayed host :" + mail.getReceivedFrom()+" allowed by SMTP extension(s)");
                return true;      
            }
            if (addr.getHost().equals("localhost") || addr.getHost().equals("127.0.0.1")) {
                log.debug("RCPT is not valid for " + mail.getReceivedFrom() + ", this is for localhost but sender is not to be relayed");
                return false;
            }
    	}
    	
        log.debug("RCPT is not valid for " + mail.getReceivedFrom() + ", this is not from relayed host and not for local domain");
        return false;        
    }

    private void closeConnection() {
        if (wr != null) {
            try {
                wr.write("221 Closing channel. Good Bye " + remote + "\r\n");
                wr.flush();
                log.debug("Closing connection ");
            } catch (IOException e) {
                //error closing channel (sending 221)
            }
        }
    }

    private void reset() {
        relayed=false;
        secured=false;
        mail = new Email();
        commandHistory=new LinkedList<String>();
        authContext=null;

        if (wr != null) {
            try {
                wr.close();
                wr = null;
            } catch (IOException e) {
                log.error(e);
            }
        }
        if (sock != null) {
            try {
                sock.close();
                sock = null;
            } catch (IOException e) {
            	log.error(e);
            }
        }
    }

    private void send(String msg) throws IOException {
        wr.write(msg + "\r\n");
        wr.flush();
        log.debug("Sent: " + msg);
    }

    private int getCommand(String input) {
        if ((input == null) || (input.length() < 4))
            return CMD_UNKW;
        String tmp = input.substring(0, 4).toUpperCase();

        if ("HELO".equals(tmp))
            return CMD_HELLO;

        if ("EHLO".equals(tmp))
            return CMD_EHLO;
        
        if ("QUIT".equals(tmp))
            return CMD_QUIT;

        if ("MAIL".equals(tmp))
            return CMD_MAIL_FROM;

        if ("RCPT".equals(tmp))
            return CMD_RCPT;

        if ("DATA".equals(tmp))
            return CMD_DATA;

        if ("RSET".equals(tmp))
            return CMD_RESET;

        if ("NOOP".equals(tmp))
            return CMD_NOOP;
        
        if ("HELP".equals(tmp))
            return CMD_NOOP;
        
        return CMD_UNKW;
    }
    
    public void setRelayed (boolean value) {
        this.relayed=value;
    }
    public Socket getSock() {
        return sock;
    }
    public void setSock(Socket sock) throws IOException {
        this.sock = sock;
        //When setting a socket (by a SMTP extension), rewire reader/writers objects
        csp = new CommandStreamParser(sock.getInputStream(), 4096, true);
        wr=new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()));
    }
    public boolean isSecured() {
        return secured;
    }
    public void setSecured(boolean secured) {
        this.secured = secured;
    }


    public List<String> getCommandHistory() {
        return commandHistory;
    }

    public void addCommandHistory(String command) {
        if (commandHistory.size()>50)
            commandHistory.remove(commandHistory.size());
        commandHistory.add(0,command);
    }
    
	public void setAuthContext(String context) {
		this.authContext=context;
		mail.setAuthContext(context);
	}
    
    private static final String MSG_HELLO_CODE = "220 ";
    private static final String MSG_HELLO_MESG = " ESMTP";
    private static final String MSG_OK = "250 Command OK";
    private static final String MSG_CMD_NOT_ALLOWED = "503 Command not allowed";
    private static final String MSG_USER_UNKOWN = "550 User does not exists, and you are not relayed";
    private static final String MSG_USER_INVALID = "501 Address is not valid"; // was 451
    private static final String MSG_DATA_BEGIN = "354 Listening for data input";
    private static final String MSG_SAVE_ERROR = "500 Error processing message";
    private static final String MSG_INVALID_CMD = "500 Invalid command";
    private static final String MSG_ERROR_SIZE = "552 Message size is to big";
    private static final String MSG_ERROR_LF = "551 Bare LF in data";
    private static final String MSG_CMD_TO_BIG = "500 Line size is to big";
    private static final String MSG_TO_MANY_RCPT = "552 To many RCPT";
    private static final String MSG_BLACKLISTED = "421 Your IP is blacklisted"; //RFC responses expects S: 250 or E: 421. 5xx ?
    private static final String MSG_NO_SPACE_LEFT = "552 No space left on storage";
    
    private static final int CMD_HELLO = 0;
    private static final int CMD_EHLO = 10;
    private static final int CMD_QUIT = 1;
    private static final int CMD_MAIL_FROM = 2;
    private static final int CMD_RCPT = 3;
    private static final int CMD_DATA = 4;
    private static final int CMD_RESET = 6;
    private static final int CMD_NOOP = 7;
    private static final int CMD_UNKW = -1;
    private static final int CMD_HELP = 8;

    public boolean isRelayed() {
        return relayed;
    }

    
    public Email getMail() {
        return mail;
    }
    

    
}