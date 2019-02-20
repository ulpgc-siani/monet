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
package org.jsmtpd.plugins.filters.SA;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsmtpd.core.mail.Email;
import org.jsmtpd.tools.ByteArrayTool;

/**
 * Spamassassin plugin. <br>
 * Connects to a spamd, passes it the message, then read the response and replace the content with it.
 * @author Jean-Francois POUX
 *
 */
public class SAChat {

    private Socket sock=null;
    private InputStream is=null;
    private OutputStream os=null;
    private int spamdPort;
    private String spamdHost;
    private Log log = LogFactory.getLog(SAChat.class);
    private int timeout;

    public SAChat(String host, int port,  int timeout) {
        this.spamdHost = host;
        this.spamdPort = port;
        this.timeout = timeout;
    }

    public boolean checkMail(Email in) {
        boolean statusOK = true;
        sock = new Socket();
        
        SocketAddress sockaddr = new InetSocketAddress(spamdHost, spamdPort);
        try {
            sock.setSoTimeout(timeout * 1000);
            sock.connect(sockaddr);
            is = sock.getInputStream();
            os = sock.getOutputStream();
            //Send the command + message
            String s = "PROCESS SPAMC/1.0\r\n";
            os.write(s.getBytes());
            os.write(in.getDataAsByte());

            sock.shutdownOutput();

            //Read response
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[512];
            int rec = 0;
            while (true) {
                rec = is.read(buffer);
                if (rec <= 0)
                    break;
                bos.write(buffer, 0, rec);
            }
            
            is.close();
            is=null;
            os.close();
            os=null;
            sock.close();
            sock=null;
            
            //Parse response
            if (bos.size() > 18) {
                byte[] responseReaded = bos.toByteArray();
                log.debug("Buffer is " + bos.size());
                int ncopy = 17;
                byte[] ok = new byte[ncopy];
                System.arraycopy(responseReaded, 0, ok, 0, ncopy);
                String response = new String(ok);
                log.debug("Response=" + response);
                if (response.contains("SPAMD/1.0 0 EX_OK")) {
                    byte[] newData = new byte[responseReaded.length - 19];
                    System.arraycopy(responseReaded, 19, newData, 0, responseReaded.length - 19);
                    
                    // spamassassin sends messages with mixed CRLF and LF, so we have to clean up...
                    newData = ByteArrayTool.crlfFix(newData);
                    in.setDataBuffer(newData);
                    log.debug("SAFilter Success - content of " + in.getDiskName() + " is modified");

                    if (newData.length > 512) {
                        byte[] detect = new byte[512];
                        System.arraycopy(newData, 0, detect, 0, 512);
                        String dt = new String(detect);
                        if (dt.contains("X-Spam-Flag: YES"))
                            statusOK = false;
                    } else
                        log.debug("Error, in " + in.getDiskName() + " response to short"+ in.getDiskName() + " is left unmodified");
                } else
                    log.debug("Error, response not understood - content of " + in.getDiskName() + " is left unmodified");
            } else {
                log.debug("Error, response too short - content of " + in.getDiskName() + " is left unmodified");
            }
        } catch (IOException e) {
            log.debug( "Error, Network IO Error - content of " + in.getDiskName() + " is left unmodified");
        } finally {
            try {
                if (is!=null)
                is.close();
            } catch (IOException e1) {}
            try {
                if (os!=null)
                os.close();
            } catch (IOException e2) {}
            try {
                if (sock!=null)
                sock.close();
            } catch (IOException e3) {}
        }

        return statusOK;

    }

}