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
package org.jsmtpd.plugins.filters.SA;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsmtpd.core.common.PluginInitException;
import org.jsmtpd.core.common.filter.IFilter;
import org.jsmtpd.core.mail.Email;

/**
 * SpamAssassin Filter for Jsmtpd
 * <br>
 * 
 * The SA filter will always return true, as long
 * as it only modifies the Email message (the body
 * of the mail).
 * 
 * @author Jean-Francois POUX
 */
public class SAFilter implements IFilter {

    private String spamdHost;
    private int spamdPort;
    private Log log = LogFactory.getLog(SAFilter.class);
    private int socketTimeout = 0;
    private int skipIfSizeMore = -1;

    /* (non-Javadoc)
     * @see jsmtpd.common.filter.IFilter#doFilter(jsmtpd.mail.Email)
     */
    public boolean doFilter(Email input) {

        //most spam are small, if a mail exceed this size, it will pass throught directly as true response
        if (skipIfSizeMore != -1) {
            if (input.getSize() > skipIfSizeMore) {
                log.info("Mail " + input.getDiskName() + " skipped filter, it's size exceds the treshold set");
                return true;
            }
        }
        SAChat chat = new SAChat(spamdHost, spamdPort, socketTimeout);
        return (chat.checkMail(input));

    }

    /* (non-Javadoc)
     * @see jsmtpd.common.IGenericPlugin#getPluginName()
     */
    public String getPluginName() {
        return "Jsmtpd-SA SpamAssassin filter";
    }

    /* (non-Javadoc)
     * @see jsmtpd.common.IGenericPlugin#initPlugin()
     */
    public void initPlugin() throws PluginInitException {
        log.debug(getPluginName() + " initialized");
    }

    /* (non-Javadoc)
     * @see jsmtpd.common.IGenericPlugin#shutdownPlugin()
     */
    public void shutdownPlugin() {
    }

    //Methods for plugin loader

    public void setSpamdHost(String hostname) {
        spamdHost = hostname;
    }

    public void setSpamdPort(int port) {
        spamdPort = port;
    }

    public void setSocketTimeout(int time) {
        socketTimeout = time;
    }

    public void setSkipIfSizeMore(int skipIfSizeMore) {
        this.skipIfSizeMore = skipIfSizeMore;
    }
}