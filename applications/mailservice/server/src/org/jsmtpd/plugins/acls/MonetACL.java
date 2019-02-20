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

public class MonetACL implements IACL {
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
    	return true;
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