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
package org.jsmtpd.plugins.filters.builtin;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsmtpd.core.common.PluginInitException;
import org.jsmtpd.core.common.PluginStore;
import org.jsmtpd.core.common.acl.IACL;
import org.jsmtpd.core.common.filter.FilterTreeFailureException;
import org.jsmtpd.core.common.filter.FilterTreeSuccesException;
import org.jsmtpd.core.common.filter.IFilter;
import org.jsmtpd.core.mail.Email;
import org.jsmtpd.core.mail.EmailAddress;
import org.jsmtpd.core.mail.Rcpt;

/**
 * @author Jean-Francois POUX
 */
public class RcptRewrite implements IFilter {

    private Map<EmailAddress,EmailAddress> rewriteMap = new HashMap<EmailAddress,EmailAddress>();
    private Map<EmailAddress,EmailAddress> catchAllMap = new HashMap<EmailAddress,EmailAddress>();
    private IACL acl = null;

    private Log log = LogFactory.getLog(RcptRewrite.class);

    public boolean doFilter(Email input) throws FilterTreeFailureException, FilterTreeSuccesException {
        List<Rcpt> rcpts = input.getRcpt();
        Vector<Rcpt> pendingRemove = new Vector<Rcpt>();

        for (Iterator<Rcpt> iter = rcpts.iterator(); iter.hasNext();) {
            Rcpt element = (Rcpt) iter.next();
            EmailAddress rcpt = element.getEmailAddress();

            // Check if rcpt is in rw map, then apply rw
            if (rewriteMap.containsKey(rcpt)) {
                EmailAddress newRcpt = (EmailAddress) rewriteMap.get(rcpt);
                if (newRcpt != null) {
                    log.info("rcpt " + rcpt.toString() + " is now " + newRcpt + " in mail " + input.getDiskName());
                    element.setEmailAddress(newRcpt);
                } else {
                    log.info("Removed rcpt " + rcpt.toString() + " in mail " + input.getDiskName());
                    pendingRemove.add(element);
                }

            } else //might be a catchall 
            {
                // Apply catch all if the catchall is defined AND if the user is not excplicitly defined
                if (catchAllMap.keySet().contains(rcpt) && !acl.isValidAddressStandardUser(rcpt)) {
                    EmailAddress newRcpt = (EmailAddress) catchAllMap.get(rcpt);
                    if (newRcpt != null) {
                        log.info("rcpt " + rcpt.toString() + " is now at catch all " + newRcpt + " in mail " + input.getDiskName());
                        element.setEmailAddress(newRcpt);
                    } else {
                        log.info("Removed rcpt by catchall " + rcpt.toString() + " in mail " + input.getDiskName());
                        pendingRemove.add(element);
                    }
                }//else leave alone
            }
        }//end for loop over rcpt

        // Apply pendings
        for (Iterator<Rcpt> iter = pendingRemove.iterator(); iter.hasNext();) {
            Rcpt element = (Rcpt) iter.next();
            rcpts.remove(element);
        }

        return true;
    }

    public String getPluginName() {
        return "Recipient Rewriter for Jsmtpd";
    }

    public void initPlugin() throws PluginInitException {
        acl = PluginStore.getInstance().getAcl();
        if (acl == null)
            throw new PluginInitException();
    }

    public void shutdownPlugin() {

    }

    public void setRewrite(String in) throws Exception {
        if (!in.contains(","))
            throw new Exception("Property set must be user@domain,user2@domain2");
        String[] args = in.split(",");

        EmailAddress from = EmailAddress.parseAddress(args[0]);
        EmailAddress to;
        if (args[1].equals("remove"))
            to = null;
        else
            to = EmailAddress.parseAddress(args[1]);

        if (rewriteMap.containsKey(from))
            throw new Exception("Alias already defined");

        rewriteMap.put(from, to);
    }

    public void setCatchAll(String in) throws Exception {
        if (!in.contains(","))
            throw new Exception("Property set must be *@domain,user2@domain2");
        String[] args = in.split(",");

        if (!args[0].contains("*"))
            throw new Exception("Property set must be *@domain,user2@domain2");
        if (args[1].contains("*"))
            throw new Exception("Property set must be *@domain,user2@domain2");

        EmailAddress to;
        EmailAddress from = EmailAddress.parseAddress(args[0]);

        if (args[1].equals("remove"))
            to = null;
        else
            to = EmailAddress.parseAddress(args[1]);

        if (catchAllMap.containsKey(from))
            throw new Exception("Catch All already defined");

        catchAllMap.put(from, to);
    }
}