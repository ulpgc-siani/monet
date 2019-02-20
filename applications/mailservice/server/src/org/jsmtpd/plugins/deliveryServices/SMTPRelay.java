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

import java.net.Inet4Address;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsmtpd.config.ReadConfig;
import org.jsmtpd.core.common.PluginInitException;
import org.jsmtpd.core.common.delivery.FatalDeliveryException;
import org.jsmtpd.core.common.delivery.IDeliveryService;
import org.jsmtpd.core.common.delivery.TemporaryDeliveryException;
import org.jsmtpd.core.mail.Email;
import org.jsmtpd.core.mail.Rcpt;


/**
 * Not tested yet
 * @author Jean-Francois POUX
 */
public class SMTPRelay implements IDeliveryService {
	
    private Log log = LogFactory.getLog(SMTPRelay.class);
    private List<Inet4Address> smtpServers = new ArrayList<Inet4Address>();
    private String smtpHostName;
    private int connectionTimeout=30;
    private String login;
    private String password;
    private String authMethod;
    private int smtpPort = 25;
    private boolean lmtp;
    
    public void doDelivery(Email in, List<Rcpt> rcpts) {
        Rcpt first = (Rcpt) rcpts.get(0);
        String domain = first.getEmailAddress().getHost();
        
        log.info("delivering mail from " + in.getFrom().toString() + " of batch " + domain);
        
        // Query DNS, for each DNS try send message.
        
        if (smtpServers.size() == 0) {
            log.warn("Can't deliver to domain" + domain + ", no SMTP relay set");
            // Mark all rcpt as error fatal
            for (Iterator<Rcpt> iterator = rcpts.iterator(); iterator.hasNext();) {
                Rcpt oneRcpt = (Rcpt) iterator.next();
                oneRcpt.setDelivered(Rcpt.STATUS_ERROR_FATAL);
                oneRcpt.setLastError("No smtp relay servers set");
            }
            return;
        }
        log.info("using " + smtpServers.size() + " smtp relays");
        for (Iterator<Inet4Address> iter = smtpServers.iterator(); iter.hasNext();) {
            InetAddress element = (InetAddress) iter.next();
            log.debug("trying " + element.toString());
            SmtpSender sender;
            if (authMethod!=null)
            	sender = new SmtpSender(smtpHostName, in, (Inet4Address) element,smtpPort, rcpts,connectionTimeout,authMethod,login,password);
            else
            	sender = new SmtpSender(smtpHostName, in, (Inet4Address) element, smtpPort, rcpts,connectionTimeout);
           if (lmtp) {
        	   sender.setHelloCommand("LHLO");
           }
            try {
                sender.doDelivery();
                break; // if delivery is ok, do not resend !
            } catch (TemporaryDeliveryException e) {
                log.debug( "Temporary delivery error with " + element.toString());
                // Mark all rcpt as error not fatal
                for (Iterator<Rcpt> iterator = rcpts.iterator(); iterator.hasNext();) {
                    Rcpt oneRcpt = (Rcpt) iterator.next();
                    oneRcpt.setDelivered(Rcpt.STATUS_ERROR_NOT_FATAL);
                    oneRcpt.setLastError("Temporary delivery error while delivering to domain: "+domain+" using relay "+element.toString());
                }
            } catch (FatalDeliveryException e) {
                log.debug("Permanent delivery error with " + element.toString());
                // Mark all rcpt as fatal
                for (Iterator<Rcpt> iterator = rcpts.iterator(); iterator.hasNext();) {
                    Rcpt oneRcpt = (Rcpt) iterator.next();
                    oneRcpt.setDelivered(Rcpt.STATUS_ERROR_FATAL);
                    oneRcpt.setLastError("Fatal delivery error while delivering to domain: "+ domain);
                }
            }
        }
    }


    public String getPluginName() {
        return "SMTP Relay delivery plugin";
    }


    public void initPlugin() throws PluginInitException {
        smtpHostName = ReadConfig.getInstance().getProperty("localHost");
    }


    public void shutdownPlugin() {

    }

    public void setRelay (String relay) throws Exception{
        smtpServers.add((Inet4Address)Inet4Address.getByName(relay));
    }


	public void setAuthMethod(String authMethod) {
		this.authMethod = authMethod;
	}


	public void setLogin(String login) {
		this.login = login;
	}


	public void setPassword(String password) {
		this.password = password;
	}

	public void setSmtpPort(int smtpPort) {
		this.smtpPort = smtpPort;
	}


	public void setLmtp(boolean lmtp) {
		this.lmtp = lmtp;
	}

    
}
