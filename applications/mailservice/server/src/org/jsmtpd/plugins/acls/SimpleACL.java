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
package org.jsmtpd.plugins.acls;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsmtpd.core.common.PluginInitException;
import org.jsmtpd.core.common.acl.ExtendedInet4Address;
import org.jsmtpd.core.common.acl.IACL;
import org.jsmtpd.core.mail.EmailAddress;
import org.jsmtpd.core.mail.InvalidAddress;

/**
 * @author Jean-Francois POUX
 * Jsmtp
 * 
 * Basic and simpliest ACL.
 * 
 * Loads one local domain  and a list of users for this domain
 * Loads a list of relayed ip/networks
 * 
 * 
 * 
 */
public class SimpleACL implements IACL {
	private static Log log = LogFactory.getLog(SimpleACL.class);
    /**
     * valid relayed hosts (open relay only for theses hosts)
     * 
     */
    protected List<ExtendedInet4Address> validRelays = new ArrayList<ExtendedInet4Address>();
    /**
     * Local domain adresses
     */
    protected List<EmailAddress> validAdresses = new ArrayList<EmailAddress>();

    protected List<EmailAddress> wildcardUsers = new ArrayList<EmailAddress>();

    protected List<String> localDomains=new ArrayList<String>();
    
    /**
     * @param hostIP the ip acessing to smtp service
     * @return true if the ip is relayed
     */
    public boolean isValidRelay(String hostIP) {
    	
        Inet4Address ag;
        try {
            ag = (Inet4Address) InetAddress.getByName(hostIP);
            for (Iterator<ExtendedInet4Address> iter = validRelays.iterator(); iter.hasNext();) {
                ExtendedInet4Address element = iter.next();
                if (element.isEqualorInMask(ag))
                    return true;
            }
        } catch (UnknownHostException e) {
            log.error(e);
        }

        return false;                
    }

    public String getPluginName() {
        return "Simple Acces Control List plugin for jsmtp";
    }

    /**
     * @return true if the adress is for local delivery, false otherwise
     */
    public boolean isValidAddress(EmailAddress e) {
        if (isValidAddressWildCard(e))
            return true;
        else
            return isValidAddressStandardUser(e);
    }

    public boolean isValidAddressStandardUser(EmailAddress e) {
        for (Iterator<EmailAddress> iter = validAdresses.iterator(); iter.hasNext();) {
            EmailAddress element = iter.next();
            if (e.isEqual(element))
                return true;
        }
        return false;
    }

    public boolean isValidAddressWildCard(EmailAddress e) {
        for (Iterator<EmailAddress> iter = wildcardUsers.iterator(); iter.hasNext();) {
            EmailAddress element =  iter.next();
            if (e.isEqual(element))
                return true;
        }
        return false;
    }

    public void initPlugin() throws PluginInitException {

    }

    // Autoconf meth
    public void setRelayedHosts(String cfg) {
        String[] hosts = null;
        if (cfg.contains(",")) {
            hosts = cfg.split(",");
        } else {
            hosts = new String[1];
            hosts[0] = cfg;
        }

        for (int i = 0; i < hosts.length; i++) {
            if (hosts[i].contains("/")) {
                String[] sp = hosts[i].split("/");
                try {
                    Inet4Address host = (Inet4Address) InetAddress.getByName(sp[0]);
                    Inet4Address mask = (Inet4Address) InetAddress.getByName(sp[1]);
                    ExtendedInet4Address tmp = new ExtendedInet4Address(host, mask);
                    validRelays.add(tmp);
                } catch (UnknownHostException e) {
                	log.error(e);
                }

            } else {
                try {
                    ExtendedInet4Address tmp = new ExtendedInet4Address((Inet4Address) InetAddress.getByName(hosts[i]));
                    validRelays.add(tmp);
                } catch (UnknownHostException e) {
                	log.error(e);
                }
            }

        }

    }

    //  Autoconf meth
    public void setValidUsers(String cfg) {
    	
    	if ((cfg==null) ||(cfg.equals("")))
    		return;
    	
        String[] ret = null;
        if (cfg.contains(","))
            ret = cfg.split(",");
        else {
            ret = new String[1];
            ret[0] = cfg;
        }

        for (int i = 0; i < ret.length; i++) {
            String string = ret[i];
            addUser(string);
        }
    }

    /**
     * adds a new local user to the accepted list
     * @param user the user's name
     */
    private void addUser(String user) {
        if (user.contains("*")) {

            try {
                EmailAddress in = EmailAddress.parseAddress(user);
                wildcardUsers.add(in);
            } catch (InvalidAddress e) {
            	log.error(e);
            }
            return;
        }
        try {
            EmailAddress in = EmailAddress.parseAddress(user);
            validAdresses.add(in);
        } catch (InvalidAddress e) {
        	log.error(e);
        }

    }

    public void shutdownPlugin() {

    }

	public boolean isLocalDomain(String domain) {
		return localDomains.contains(domain);
	}

	public void setLocalDomain (String domain) {
		localDomains.add(domain);
	}
}