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

import java.io.ByteArrayInputStream;
import java.util.LinkedList;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsmtpd.config.ModuleNotFoundException;
import org.jsmtpd.core.common.IGenericPlugin;
import org.jsmtpd.core.common.PluginInitException;
import org.jsmtpd.core.common.PluginStore;
import org.jsmtpd.core.common.filter.FilterTreeFailureException;
import org.jsmtpd.core.common.filter.FilterTreeSuccesException;
import org.jsmtpd.core.common.filter.IFilter;
import org.jsmtpd.core.mail.Email;
import org.jsmtpd.core.mail.EmailAddress;
import org.jsmtpd.core.mail.InvalidAddress;
import org.jsmtpd.core.mail.Rcpt;
import org.jsmtpd.core.send.QueueService;
import org.jsmtpd.plugins.inputIPFilters.BlackList;

/**
 * @author Jean-Francois POUX
 */
public class SimpleBlackListManager implements IFilter {

    String blackListPluginName = "";
    BlackList controlled = null;

    String allowedSource = "";
    String managerMail = "";
    EmailAddress manMail = null;
    QueueService dsvc;

    private Log log = LogFactory.getLog(SimpleBlackListManager.class);

    public boolean doFilter(Email input) throws FilterTreeFailureException, FilterTreeSuccesException {
        // if mail is not from relayed domains, return true (continue process)
        // if mail is not for jsmtpd-blacklistmanager return true
        // If mail is not from admin ip adresses, return true
        // else : query the plugin manager for the plugin name
        // Then analyse the mail for command an execute it over the ip
        // send confirm message
        // return false (break of chain, don't deliver the mail)

        if (!input.getReceivedFrom().equals(allowedSource))
            return true; // Skip

        if (!(input.getRcpt().size() != 1))
            return true; // Skip

        Rcpt tmp = (Rcpt) input.getRcpt().get(0);
        if (!tmp.getEmailAddress().equals(manMail))
            return true;

        try {
            MimeMessage msg = new MimeMessage(null, new ByteArrayInputStream(input.getDataAsByte()));
            String command = msg.getSubject();

            if ((command == null) || (command.length() < 7))
                return true;

            if (command.equals("add")) {
                String ip = command.substring(4);
                //controlled.addPermanentBL(ip, new Date(0));
                log.info("IP address " + ip + " added to BlackList");

                LinkedList<String> mesgs = new LinkedList<String>();
                mesgs.add("Sucessfully added IP " + ip + " to blacklist manager");
                mesgs.add("");
                mesgs.add("This host can't connect now to Jsmtpd");
                mesgs.add("");

                Email e = Email.createInternalMail(input.getFrom(), "BlackList admin - added address", mesgs, null);
                dsvc.queueMail(e);
            }

            if (command.equals("remove")) {
                String ip = command.substring(4);
                //controlled.removePermanentBL(ip);
                log.info("IP address " + ip + " removed from BlackList");

            }

            return false; // Break of chain.

        } catch (MessagingException e) {
            log.error("Can't parse message " + input.getDiskName() + " ", e);
            return false;
        }

        // transform to MimeMsg
        // Parse command
        //Create new Email with mmsg
        // Apply & confirm

    }

    public String getPluginName() {
        return "Email controlled BlackList manager";
    }

    public void initPlugin() throws PluginInitException {
        PluginStore pluginStore = PluginStore.getInstance();
        IGenericPlugin query;
        try {
            query = pluginStore.getPluginByLogicalName(blackListPluginName);
        } catch (ModuleNotFoundException e) {
            throw new PluginInitException(e);
        }

        if (!(query instanceof BlackList))
            throw new PluginInitException("The plugin name " + blackListPluginName + " is not controlable");

        dsvc = QueueService.getInstance();
        controlled = (BlackList) query;

        try {
            manMail = EmailAddress.parseAddress(managerMail);
        } catch (InvalidAddress e1) {
            throw new PluginInitException(e1);
        }

    }

    public void shutdownPlugin() {

    }

    public String getBlackListPluginName() {
        return blackListPluginName;
    }

    public void setBlackListPluginName(String blackListPluginName) {
        this.blackListPluginName = blackListPluginName;
    }

    public String getAllowedSource() {
        return allowedSource;
    }

    public void setAllowedSource(String allowedSource) {
        this.allowedSource = allowedSource;
    }

    public String getManagerMail() {
        return managerMail;
    }

    public void setManagerMail(String managerMail) {
        this.managerMail = managerMail;
    }
}