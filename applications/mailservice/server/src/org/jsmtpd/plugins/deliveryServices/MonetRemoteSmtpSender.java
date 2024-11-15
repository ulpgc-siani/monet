package org.jsmtpd.plugins.deliveryServices;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsmtpd.config.ReadConfig;
import org.jsmtpd.core.common.PluginInitException;
import org.jsmtpd.core.common.delivery.IDeliveryService;
import org.jsmtpd.core.mail.Email;
import org.jsmtpd.core.mail.Rcpt;

public class MonetRemoteSmtpSender implements IDeliveryService {

    /**
     * The server log4j instance
     */
    private Log log = LogFactory.getLog(RemoteSmtpSender.class);
    /**
     * hostname of our server, from config file jsmtpd.ini
     */
    private String smtpHostName;

//    private int connectionTimeout=30;
    private List<Inet4Address> bogusDNS = new ArrayList<Inet4Address> ();
    /**
     * Trys to deliver mail
     * nb : only works for ipv4 hosts, code to add to get it working with ipv6
     * @param in the email to deliver
     * @param rcpts the rcpt to process
     */
    public void doDelivery(Email in, List<Rcpt> rcpts) {

        Rcpt tmp = (Rcpt) rcpts.get(0);    	
      	log.fatal("Remote Delivery is not allowed, email: " + tmp.getEmailAddress());
/*    	
        Rcpt first = (Rcpt) rcpts.get(0);
        String domain = first.getEmailAddress().getHost().toLowerCase();
        
        log.info("delivering mail from " + in.getFrom().toString() + " to domain " + domain);
        
        
        IDNSResolver resolver = PluginStore.getInstance().getResolver();
        List smtpServers = resolver.doMXLookup(domain);
        
        for (Inet4Address addr : bogusDNS) {
            if (smtpServers.contains(addr)) {
                log.warn("Bogus dns removed: ("+addr.toString()+")");
                smtpServers.remove(addr);
            }     
        }
        
        if (smtpServers.size() == 0) {
            log.warn("Can't deliver to domain" + domain + ", dns query reported 0 valid results.");
            // Mark all rcpt as error fatal
            for (Iterator iterator = rcpts.iterator(); iterator.hasNext();) {
                Rcpt oneRcpt = (Rcpt) iterator.next();
                oneRcpt.setDelivered(Rcpt.STATUS_ERROR_FATAL);
                oneRcpt.setLastError("No DNS Mail exchanger entries for domain "+domain+", can't deliver mail.");
            }
            return;
        }
        log.debug("found " + smtpServers.size() + " mail exchangers");
        for (Iterator iter = smtpServers.iterator(); iter.hasNext();) {
            InetAddress element = (InetAddress) iter.next();
            
            log.debug("trying " + element.toString());
            SmtpSender sender = new SmtpSender(smtpHostName, in, (Inet4Address) element, 25, rcpts,connectionTimeout);
            try {
                sender.doDelivery();
                break; // if delivery is ok, do not resend !
            } catch (TemporaryDeliveryException e) {
                log.debug( "Temporary delivery error with " + element.toString());
                // Mark all rcpt as error not fatal
                for (Iterator iterator = rcpts.iterator(); iterator.hasNext();) {
                    Rcpt oneRcpt = (Rcpt) iterator.next();
                    oneRcpt.setDelivered(Rcpt.STATUS_ERROR_NOT_FATAL);
                    oneRcpt.setLastError("Temporary delivery error while delivering to domain: "+domain+", Cause = "+e.getMessage());
                }
            } catch (FatalDeliveryException e) {
                log.debug("Permanent delivery error with " + element.toString());
                // Mark all rcpt as fatal
                for (Iterator iterator = rcpts.iterator(); iterator.hasNext();) {
                    Rcpt oneRcpt = (Rcpt) iterator.next();
                    oneRcpt.setDelivered(Rcpt.STATUS_ERROR_FATAL);
                    oneRcpt.setLastError("Fatal delivery error while delivering to domain: "+ domain+", Cause = "+e.getMessage());
                }
            }
        }
*/        
    }

    public String getPluginName() {
        return "MonetRemoteSmtpSender plugin fort jsmptd";
    }

    /* (non-Javadoc)
     * @see smtpd.common.IGenericPlugin#initPlugin()
     */
    public void initPlugin() throws PluginInitException {
        smtpHostName = ReadConfig.getInstance().getProperty("localHost");
        try {
            Inet4Address localhost = (Inet4Address) InetAddress.getByName("127.0.0.1");
            bogusDNS.add(localhost);
            log.debug("Bogus DNS="+localhost.toString());
        } catch (UnknownHostException e) {
          throw new PluginInitException(e);
        }
        try {
            Inet4Address me = (Inet4Address)InetAddress.getByName(smtpHostName);
            bogusDNS.add(me);
            log.debug("Bogus DNS="+me.toString());
        } catch (UnknownHostException e) {
            throw new PluginInitException(e);
        }
    }

    public void setBogusDns (String dns){
    	try {
            Inet4Address host = (Inet4Address) InetAddress.getByName(dns);
            bogusDNS.add(host);
            log.debug("Bogus DNS="+host.toString());
        } catch (UnknownHostException e) {
        	log.error(e);
        }
    }
    
    /* (non-Javadoc)
     * @see jsmtpd.common.IGenericPlugin#shutdownPlugin()
     */
    public void shutdownPlugin() {

    }

    public void setConnectionTimeout(int connectionTimeout) {
//        this.connectionTimeout = connectionTimeout;
    }
}