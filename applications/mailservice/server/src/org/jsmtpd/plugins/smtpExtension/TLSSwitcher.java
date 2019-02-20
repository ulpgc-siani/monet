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
import java.net.Socket;
import java.net.URL;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsmtpd.core.common.PluginInitException;
import org.jsmtpd.core.common.io.BareLFException;
import org.jsmtpd.core.common.io.InputSizeToBig;
import org.jsmtpd.core.common.smtpExtension.IProtocolHandler;
import org.jsmtpd.core.common.smtpExtension.ISmtpExtension;
import org.jsmtpd.core.common.smtpExtension.SmtpExtensionException;

/**
 * @author Jean-Francois POUX
 */
public class TLSSwitcher implements ISmtpExtension {

    private Log log = LogFactory.getLog(TLSSwitcher.class);
    private SSLSocketFactory sfact;

    private String keystoreName;
    private transient String keystorePassword;

    public boolean smtpTrigger(String command, IProtocolHandler protocol) throws SmtpExtensionException, IOException {
        if ((command == null) || (command.length() < 8)) {
            return false;
        }
        String tmp = command.substring(0, 8).toUpperCase();
        if (!"STARTTLS".equals(tmp))
            return false;

        try {
            log.debug("Trying to switch to TLS Mode");
            handleTLSRequest(protocol);
            log.debug("Switched to TLS Mode");
        } catch (IOException e) {
            log.error("IO Error while switching to TLS Mode");
            throw e;
        }
        return true;
    }

    private void handleTLSRequest(IProtocolHandler protocol) throws IOException {
        try {
            Socket old = protocol.getSock();
            SSLSocket securedSocket = (SSLSocket) sfact.createSocket(old, old.getInetAddress().getHostName(), old.getPort(), true);
            securedSocket.setEnabledCipherSuites(securedSocket.getSupportedCipherSuites());
            securedSocket.setUseClientMode(false);
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(old.getOutputStream()));
            wr.write("220 Go ahead\r\n");
            wr.flush();
            securedSocket.startHandshake();
            protocol.setSock(securedSocket);
            protocol.setSecured(true);
        } catch (IOException e) {
            log.error("error while switching to secured mode : ", e);
            throw e;
        }
    }

    public String getWelcome() {
        return "STARTTLS";
    }

    public String getPluginName() {
        return "TLS channel switcher for Jsmtpd";
    }

    public void initPlugin() throws PluginInitException {
        // place the keystore on system var
        URL url = this.getClass().getClassLoader().getResource(keystoreName);
        if (url != null) {
            String ks = url.getFile();
            System.setProperty("javax.net.ssl.keyStore", ks);
            System.setProperty("javax.net.ssl.keyStorePassword", keystorePassword);
            //Todo: try to open keystore
        } else {
            log.info("keystore file not found, SSL not available");
            throw new PluginInitException();
        }
        sfact = (SSLSocketFactory) SSLSocketFactory.getDefault();
    }

    public void shutdownPlugin() {
        // nothing
    }


    public void setKeystoreName(String keystoreName) {
        this.keystoreName = keystoreName;
    }

    public void setKeystorePassword(String keystorePassword) {
        this.keystorePassword = keystorePassword;
    }

    public boolean smtpPreTrigger(String command, IProtocolHandler protocol) throws SmtpExtensionException, IOException, InputSizeToBig, IOException, BareLFException {
        return false;
    }
}