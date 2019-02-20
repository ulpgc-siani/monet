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

import org.jsmtpd.core.common.PluginInitException;
import org.jsmtpd.core.common.filter.FilterTreeFailureException;
import org.jsmtpd.core.common.filter.FilterTreeSuccesException;
import org.jsmtpd.core.common.filter.IFilter;
import org.jsmtpd.core.mail.Email;

/**
 * Jsmtpd
 * @author Jean-Francois POUX
 */
public class FalseFilter implements IFilter {

    /* (non-Javadoc)
     * @see org.jsmtpd.core.common.filter.IFilter#doFilter(org.jsmtpd.core.mail.Email)
     */
    public boolean doFilter(Email input) throws FilterTreeFailureException, FilterTreeSuccesException {
        return false;
    }

    /* (non-Javadoc)
     * @see org.jsmtpd.core.common.IGenericPlugin#getPluginName()
     */
    public String getPluginName() {
        return "FalseFilter: Always false debug filter plugin for jsmtpd";
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

}