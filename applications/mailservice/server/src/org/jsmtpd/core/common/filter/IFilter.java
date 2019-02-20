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
package org.jsmtpd.core.common.filter;

import org.jsmtpd.core.common.IGenericPlugin;
import org.jsmtpd.core.mail.Email;

/**
 * Return true if the filter is passed successfully<br>
 * Return false if the filter is not passed<br>
 * <br>
 * Chain will go on until its end, or if FilterTreeFailureException or
 * FilterTreeSuccesException are thrown. Theses exceptions causes the 
 * immediate succes or failure of the chain
 * <br>
 * A filter that causes a failure of chain should excplitly create a new mail
 * to inform the sender of the failure.
 * @author Jean-Francois POUX
 * 
 */
public interface IFilter extends IGenericPlugin {

    /**
     * Processes the mail throught the filter
     * @param input the mail to process
     * @return true if filter pass, false otherwise
     */
    public boolean doFilter(Email input) throws FilterTreeFailureException, FilterTreeSuccesException;

}