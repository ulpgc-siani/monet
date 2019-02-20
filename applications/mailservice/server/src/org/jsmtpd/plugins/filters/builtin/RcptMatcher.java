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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsmtpd.core.common.PluginInitException;
import org.jsmtpd.core.common.filter.FilterTreeFailureException;
import org.jsmtpd.core.common.filter.FilterTreeSuccesException;
import org.jsmtpd.core.common.filter.IFilter;
import org.jsmtpd.core.mail.Email;
import org.jsmtpd.core.mail.EmailAddress;
import org.jsmtpd.core.mail.InvalidAddress;
import org.jsmtpd.core.mail.Rcpt;

/**
 * This filter returns true if the RCPTS specified are present.
 * If multiple RCPTS are present, you can configure the logical operator between the matches
 * by default all rcpt are required to validate the filter. set the properet orOperator to true
 * will return true at the first rcpt found.
 * 
 * Additionnally, you can set failOnError to true if you wish to break the tree when rcpt are not found.
 * You can also validate the whole tree is the filter is passed successfully by setting validateOnSucces to true.
 * 
 * @author Jean-Francois POUX
 */
public class RcptMatcher implements IFilter {

    private boolean orOperator = false;

    private List<EmailAddress> rcptToMatch = new LinkedList<EmailAddress>();
    private Log log = LogFactory.getLog(RcptMatcher.class);
    private boolean failOnError = false;
    private boolean validateOnSucces = false;

    /* (non-Javadoc)
     * @see org.jsmtpd.core.common.filter.IFilter#doFilter(org.jsmtpd.core.mail.Email)
     */
    public boolean doFilter(Email input) throws FilterTreeFailureException, FilterTreeSuccesException {
        
    	boolean res = doFilterInternal(input); 
        
        if (failOnError && (!res)) throw new FilterTreeFailureException();

        if (validateOnSucces && res) throw new FilterTreeSuccesException();

        return res;
    }

    private boolean doFilterInternal (Email input) {
    	List<EmailAddress> addressesInMail = new ArrayList<EmailAddress>();
    	for (Rcpt rcpt : input.getRcpt()) {
			addressesInMail.add(rcpt.getEmailAddress());
		}
    	
		if (orOperator) {
			for (EmailAddress emailAddress : addressesInMail) {
				if (rcptToMatch.contains(emailAddress))
					return true;
			}
			return false;
		} else {
			for (EmailAddress emailAddress : rcptToMatch) {
				if (! addressesInMail.contains(emailAddress))
					return false;
			}
			return true;
		}
    }
    
    /* (non-Javadoc)
     * @see org.jsmtpd.core.common.IGenericPlugin#getPluginName()
     */
    public String getPluginName() {
        return "Recipient filter matcher for Jsmtp";
    }

    /* (non-Javadoc)
     * @see org.jsmtpd.core.common.IGenericPlugin#initPlugin()
     */
    public void initPlugin() throws PluginInitException {

    }

    /* (non-Javadoc)
     * @see org.jsmtpd.core.common.IGenericPlugin#shutdownPlugin()
     */
    public void shutdownPlugin() {

    }

    public void setRcpt(String rcpt) {
        try {
            EmailAddress rcptToAdd = EmailAddress.parseAddress(rcpt);
            rcptToMatch.add(rcptToAdd);
        } catch (InvalidAddress e) {
            log.error("Plugin: " + getPluginName() + " can't add rcpt to matching list, " + rcpt + " is not a valid email adress");
        }
    }

    public void setOrOperator(boolean cond) {
        orOperator = cond;
    }

    public void setFailOnError(boolean op) {
        failOnError = op;
    }

    public void setValidateOnSucces(boolean op) {
        validateOnSucces = op;
    }
}