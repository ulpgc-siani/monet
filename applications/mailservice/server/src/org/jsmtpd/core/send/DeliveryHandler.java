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
package org.jsmtpd.core.send;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsmtpd.config.ReadConfig;
import org.jsmtpd.core.common.PluginStore;
import org.jsmtpd.core.common.acl.IACL;
import org.jsmtpd.core.common.delivery.IDeliveryService;
import org.jsmtpd.core.common.filter.FilterTreeFailureException;
import org.jsmtpd.core.common.filter.FilterTreeNode;
import org.jsmtpd.core.common.filter.FilterTreeSuccesException;
import org.jsmtpd.core.mail.Email;
import org.jsmtpd.core.mail.Rcpt;
import org.jsmtpd.tools.ByteArrayTool;

/**
 * Handles the delivery process of a message:<br>
 * -Filtering it throught the filter tree<br>
 * -Determining if it is local or not<br>
 * -Delivering it for each rcpt using the right plugin.
 * @author Jean-Francois POUX
 */
public class DeliveryHandler {

    private Email e = null;
    private Log log=LogFactory.getLog(DeliveryHandler.class);
    private IACL acl;
    private IDeliveryService local;
    private IDeliveryService remote;
    private QueueService dsvc;
    private String runningHost;
    private int maxRetries;
    private int retryDelay;
    
    public DeliveryHandler() {
        ReadConfig readConfig = ReadConfig.getInstance();
        acl = PluginStore.getInstance().getAcl();
        local = PluginStore.getInstance().getLocalDeliveryService();
        remote = PluginStore.getInstance().getRemoteDeliveryService();
        dsvc = QueueService.getInstance();
        runningHost=readConfig.getProperty("localHost");
        maxRetries = readConfig.getIntegerProperty("dNumRetry");
        retryDelay = readConfig.getIntegerProperty("dDelayRetry");
    }

    public void clearMail() {
        e = null;
    }

    /**
     * trys to send a mail
     * @param in mail to send
     */
    public void processMessage(Email in) {
        this.e = in;
        String message = e.getDiskName();
        log.debug("Begin process " + e.getDiskName());

        // apply filter
        if (!e.isFiltered()) {
            if (!applyFilterChain()) {
                log.info("Message " + message + " was dropped during filtering chain");
                return;
            }
            e.setFiltered(true);
        }
        log.info("Message " + message + " has passed filtering tree");

        List<Rcpt> rcpt = e.getRcpt();
        Map<String,List<Rcpt>> batch = prepareBatch(in);

        for (String domain : batch.keySet()) {
            List<Rcpt> domainBatch = batch.get(domain);
            //Get the first recipient and use it to determine if it's local or remote domain
            Rcpt tmp = (Rcpt) domainBatch.get(0);
            if (acl.isValidAddress(tmp.getEmailAddress())) {
                try {
                    local.doDelivery(e, domainBatch);
                } catch (RuntimeException runtimeException) {
                    log.fatal("DeliveryHandler has detected a problem in the loaded local delivery plugin: ",runtimeException);
                }
            } else {
                try {
                    remote.doDelivery(e, domainBatch);
                } catch (RuntimeException runtimeException) {
                    log.fatal("DeliveryHandler has detected a problem in the loaded remote delivery plugin: ",runtimeException);
                }
            }
        }
        // the delivery plugins will modify the status of each recipient (success, temporary failure, fatal failure).
        // We need to check if retries does not exceed a threshold.
        for (Rcpt element : rcpt) {;
            if (element.getDeliveryAttempts() > maxRetries) {
                element.setDelivered(Rcpt.STATUS_ERROR_FATAL); //change to fatal
                element.setLastError("Exceeding delivery attemps ("+maxRetries+"+)");
                log.warn("Maxmimum retry for RCPT " + element.getEmailAddress().toString() + " in mail " + e.getDiskName());
            }
        }

        // are all rcpt delivered ?
        if (e.isDelivered()) {
            log.info("Message " + message + " has been fully delivered");
            sendErrorMail();
            e = null;

        } else {
            if (!dsvc.requeueMail(e))
                log.error("Message " + message + " can't be requeued for delivery, message is lost");
            else {
                log.info("Message " + message + " is requeued for delivery");
                if (e.getAttemps()==1)
                	sendNotification(); // This is the first time mail is sucessfully requeued. So it has non faltal errors. Send bounce mail to inform
            }
        }
        e = null; // don't keep a reference to the mail, so if requeued by delivery svc it can be gc'ed
        log.debug("End process " + message);
    }

    /**
     * Prepares a map of grouped domains, each entry is a list of recipient to deliver to the domain (key of the map)
     * @param in the mail to process
     * @return the batch map
     */
    private Map<String,List<Rcpt>> prepareBatch(Email in) {
        Map<String,List<Rcpt>> batch = new HashMap<String,List<Rcpt>>();
        List<Rcpt> rawRcpt = in.getRcpt();

        for (Rcpt rcpt : rawRcpt) {
            if (!rcpt.isDelivered()) { // skip already delivered
                log.debug("Delivery handler adding "+rcpt.getEmailAddress().toString()+" to batch (num retry="+rcpt.getDeliveryAttempts()+")");
                String domain = rcpt.getEmailAddress().getHost();
                if (!batch.containsKey(domain)) {
                    List<Rcpt> domainBatch = new ArrayList<Rcpt>();
                    domainBatch.add(rcpt);
                    batch.put(domain, domainBatch);
                } else {
                    List<Rcpt> domainBatch =  batch.get(domain);
                    domainBatch.add(rcpt);
                }
            }
        }
        return batch;
    }

    /**
     * Applies the filter tree to a mail
     * @return true if mail can be sent, false if it is to be dropped
     */
    private boolean applyFilterChain() {
        FilterTreeNode rootFilter = PluginStore.getInstance().getRootFilter();
        if (rootFilter == null)//no filter, auto succes
            return true;
        try {
            rootFilter.doFilter(e);
        } catch (FilterTreeFailureException e) {
            return false;
        } catch (FilterTreeSuccesException e) {
            return true;
        } catch (Throwable e) {
            log.fatal("A plugin in the filter tree throwed an uncaught exception: ", e);
            return true;
        }
        return true;

    }
    private void sendErrorMail(){
        // If there is a fatal error, notify sender.
        boolean bounce=false;
        for (Rcpt element : e.getRcpt()) {
            if (element.getStatus()==Rcpt.STATUS_ERROR_FATAL) {
                bounce=true;
                break;
            }
        }
        //notice send of failure for each RCPT failed, except if bounce mail
        if ( (!e.getFrom().toString().equals("<>")) && bounce) {
            LinkedList<String> messages = new LinkedList<String>();
            messages.add("");
            messages.add("Hello, this is the msmtpd mailer daemon, running on system "+runningHost);
            messages.add("");
            messages.add("I'm afraid I can't deliver your email to : ");

        	for (Rcpt element : e.getRcpt()) {
                if (element.getStatus() == Rcpt.STATUS_ERROR_FATAL)
                    messages.add("\t Recipent: " + element.getEmailAddress().toString() + ", fatal error: "+element.getLastError());
            }
            messages.add("");
            messages.add("************************************");
            messages.add("* This is a fatal error, giving up *");
            messages.add("************************************");
            Email error = Email.createInternalMail(e.getFrom(), "Mailer-daemon, fatal error while processing mail", messages, e);
            attachMail(error,this.e);
            log.warn("Mail "+e.getDiskName()+" has fatal delivery errors, sending errors back to sender");
            dsvc.queueMail(error);
        }
    }
    
    private void sendNotification() {
        boolean bounce=false;
        for (Rcpt element : e.getRcpt()) {
            if (element.getStatus()==Rcpt.STATUS_ERROR_NOT_FATAL) {
                bounce=true;
                break;
            }
        }
        
        if ( (!e.getFrom().toString().equals("<>")) && bounce) {
            LinkedList<String> messages = new LinkedList<String>();
            messages.add("");
            messages.add("Hello, this is the msmtpd mailer daemon, running on system "+runningHost);
            messages.add("");
            messages.add("You wanted me to deliver your mail, but I could not do immediatly for : ");
            for (Rcpt element : e.getRcpt()) {
                if (element.getStatus() == Rcpt.STATUS_ERROR_FATAL)
                    messages.add("\t Recipent: " + element.getEmailAddress().toString() + ", fatal error (I give up) : "+element.getLastError());
                if (element.getStatus() == Rcpt.STATUS_ERROR_NOT_FATAL) {
                    messages.add("\t Recipent: " + element.getEmailAddress().toString() + ", temporary error (I'll retry): "+element.getLastError());
                }
            }
            messages.add("");
            messages.add("I'll try to send your mail to temporary unavailable recipients, up to "+maxRetries+" times, trying each "+retryDelay+" minute(s)");
            Email error = Email.createInternalMail(e.getFrom(), "Mailer-daemon, mail delivery delayed", messages, e);
            attachMail(error,this.e);
            log.warn("Mail "+e.getDiskName()+" has temporary delivery errors, notifying sender");
            dsvc.queueMail(error);
        }
    }
    

    private void attachMail (Email target, Email join) {
    	try {
    		log.debug("Attaching original mail to error report mail");
			MimeMessage targetMime = new MimeMessage(null,new ByteArrayInputStream(target.getDataAsByte()));
			MimeMessage joinMime = new MimeMessage (null,new ByteArrayInputStream(join.getDataAsByte()));
			
			MimeBodyPart targetPart = new MimeBodyPart();
			targetPart.setDataHandler(targetMime.getDataHandler());
			
			MimeBodyPart joinedPart = new MimeBodyPart();
			joinedPart.setDataHandler(joinMime.getDataHandler());
			joinedPart.setFileName("Original mail");
			
			Multipart multipart = new MimeMultipart ();
			multipart.addBodyPart(targetPart);
			multipart.addBodyPart(joinedPart);
			
			targetMime.setContent(multipart);
			targetMime.saveChanges();
			
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			targetMime.writeTo(bos);
			
			target.setDataBuffer(ByteArrayTool.crlfFix(bos.toByteArray()));
			log.debug("New error mail has original mail attached");
		} catch (Exception e) {
			log.error("Unable to attach original mail: ",e);
		} 
    }
}