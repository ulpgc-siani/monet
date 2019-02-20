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
package org.jsmtpd.plugins.smtpExtension;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsmtpd.core.common.PluginInitException;
import org.jsmtpd.core.common.io.BareLFException;
import org.jsmtpd.core.common.io.InputSizeToBig;
import org.jsmtpd.core.common.io.commandStream.CommandStreamParser;
import org.jsmtpd.core.common.smtpExtension.IProtocolHandler;
import org.jsmtpd.core.common.smtpExtension.ISmtpExtension;
import org.jsmtpd.core.common.smtpExtension.SmtpExtensionException;
import org.jsmtpd.tools.Base64Helper;
import org.jsmtpd.tools.ByteArrayTool;

/**
 * Provides a base class to extend to provide SMTP authentication
 * Not fully RFC compliant at the momment
 * TODO: Add support for cancel authentication
 * TODO: Correctly reset states.
 * TODO: When authenticated, if client issues a RSET, the state is cleared. is it the correct behavior ?
 * TODO: auth plain doest not works with 3 params as it should
 * RFCS: http://www.fehcom.de/rfc/rfc2595.txt
 * @author Jean-Francois POUX
 */
public abstract class SmtpAuthenticator implements ISmtpExtension {

    private boolean requireSecuredChannel = false;
    private Log log = LogFactory.getLog(SmtpAuthenticator.class);

    protected abstract boolean performAuth(String login, byte[] password);

    public boolean smtpTrigger(String command, IProtocolHandler protocol) throws SmtpExtensionException, InputSizeToBig, IOException, BareLFException {

        BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(protocol.getSock().getOutputStream()));
        CommandStreamParser csp = new CommandStreamParser(protocol.getSock().getInputStream(), 512, false);

        if ((command == null) || (command.length() < 8)) {
            return false;
        }

        String tmp = command.substring(0, 4).toUpperCase();
        if (!tmp.equals("AUTH"))
            return false; // cmd not for this plugin

        if (requireSecuredChannel && (!protocol.isSecured())) {
            send(wr, MSG_REQUIRE_SEC_CHANNEL);
            log.error("client wants to auth, but is not on secured channel.");
            throw new SmtpExtensionException();
        }

        String[] args = command.split(" ");
        if ((args == null) || (args.length < 1)) {
            send(wr, MSG_INVALID_CMD);
            log.error("ESMTP:AUHT> Invalid or empty command");
            throw new SmtpExtensionException();
        }

        if ("PLAIN".equals(args[1].toUpperCase())) {
            if ((args.length < 2) || (args[2] == null)) {
                send(wr, MSG_INVALID_CMD);
                log.error("ESMTP:AUHT> Invalid or empty PLAIN command");
                throw new SmtpExtensionException();
            }

            byte[] lpData = Base64Helper.decode(args[2]);

            String[] lp = parseAuthData(lpData);
            if ((lp.length < 2) || (args[0] == null) || (args[1] == null)) {
                send(wr, MSG_INVALID_CMD);
                log.error("ESMTP:AUHT> Invalid or empty PLAIN command");
                throw new SmtpExtensionException();
            }

            // 2 kind of responses (2 or 3 params).
            boolean authRes;
            if (lp.length==2)
            	authRes=performAuth(lp[0], lp[1].getBytes());
            else
            	authRes=performAuth(lp[1], lp[2].getBytes());
            
            if (authRes) {
                protocol.setRelayed(true);
                log.info("Remote host is now relayed (PLAIN authentication successfull)");
                protocol.setAuthContext(lp[1]); // who sent the authenticaded mail
                send(wr, MSG_AUTH_OK);
                return true;
            } else {
                log.info("Remote host authentication failed");
                send(wr, MSG_AUTH_FAILED);
                throw new SmtpExtensionException();
            }
        }

        if ("LOGIN".equals((args[1]))) {
            send(wr, MSG_AUTH_GO_ON + " VXNlcm5hbWU6");
            String login = csp.readLine();
            if (login == null) {
                log.info("LOGIN Remote host issued a null login");
                throttle(protocol);
                send(wr, MSG_INVALID_CMD);
                throw new SmtpExtensionException();
            }
            login = new String(Base64Helper.decode(login));
            send(wr, MSG_AUTH_GO_ON + " UGFzc3dvcmQ6");
            String pass = csp.readLine();
            if (pass == null) {
                log.info("LOGIN Remote host issued a null password");
                throttle(protocol);
                send(wr, MSG_INVALID_CMD);
                throw new SmtpExtensionException();
            }
            byte[] pw = Base64Helper.decode(pass);
            //pw = md5.digest(pw); // removed: let the underlying class decide what to do with it.
            if (performAuth(login, pw)) {
                protocol.setRelayed(true);
                log.info("Remote host is now relayed (LOGIN authentication successfull)");
                protocol.setAuthContext(login);
                send(wr, MSG_AUTH_OK);
                return true;
            } else {
                log.info("Remote host authentication failed");
                throttle(protocol);
                send(wr, MSG_AUTH_FAILED);
                throw new SmtpExtensionException();
            }
        }

        throw new SmtpExtensionException();
    }

    public String getWelcome() {
        return "AUTH PLAIN LOGIN";
    }

    public String getPluginName() {
        return "PLAIN/LOGIN authentication SMTP Extension for Jsmtpd";
    }

    public void initPlugin() throws PluginInitException {
    }

    public void shutdownPlugin() {

    }

    private void send(BufferedWriter wr, String message) throws IOException {
        wr.write(message + "\r\n");
        wr.flush();
        log.debug("sent " + message + "<CR><LF>");
    }

    private static final String MSG_INVALID_CMD = "500 Invalid auth command";
    private static final String MSG_REQUIRE_SEC_CHANNEL = "538 Encryption required for requested authentication mechanism";
    private static final String MSG_AUTH_OK = "235 OK Authenticated";
    private static final String MSG_AUTH_FAILED = "535 authentication failed";
    private static final String MSG_AUTH_GO_ON = "334";

    // Autoconf
    public void setRequireSecuredChannel(boolean requireSecuredChannel) {
        this.requireSecuredChannel = requireSecuredChannel;
    }

    private void throttle(IProtocolHandler handler) {
    	long waitTime =  Math.round(Math.exp(handler.getErrorCount()));
        log.info("Waiting "+waitTime+" secs before retry");
        Object o = new Object();
        synchronized (o) {
            try {
                o.wait(waitTime*1000);
            } catch (InterruptedException e) {
            }
        }
    }

    private String[] parseAuthData(byte[] input) {
        List<byte[]> res = ByteArrayTool.split(input, ByteArrayTool.NULL);
        String[] ret = new String[res.size()];
        int i = 0;
        for (Iterator<byte[]> iter = res.iterator(); iter.hasNext();) {
            byte[] element = (byte[]) iter.next();
            ret[i] = new String(element);
            i++;
        }
        return ret;
    }

    public boolean smtpPreTrigger(String command, IProtocolHandler protocol) throws SmtpExtensionException, IOException, InputSizeToBig, IOException, BareLFException {
        return false;
    }
}