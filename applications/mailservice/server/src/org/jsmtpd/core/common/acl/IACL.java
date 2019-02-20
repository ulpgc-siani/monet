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
package org.jsmtpd.core.common.acl;

import org.jsmtpd.core.common.IGenericPlugin;
import org.jsmtpd.core.mail.EmailAddress;

/**
 * <b>Pluggin interface for input filtering</b><br><br>
 * Implement this interface to provide your custom acces control list.<br>
 * Then, change AclPluggin to match the name of your class,
 * it will be automaticly loaded upon server startup.<br>
 * <br>
 * <b>Warning</b> : One instance of your ACL will be created. It will be 
 * accessed for multiple threads. Think it thread safe.<br>
 * Methods are not marked synchronized, as basic implementations may
 * not need to care about thread concurency.<br>
 * <br>
 * All host names are FQDN, the server will resolve to ip adresses when needed.
 * 
 * @author Jean-Francois POUX
 *
 */
public interface IACL extends IGenericPlugin {


    /** Is it a valid email adress to relay ? eg user and domain*/
    public boolean isValidAddress(EmailAddress address);

    /** Is the email in wildcard, eg for a given domain, *@domain.com*/
    public boolean isValidAddressWildCard(EmailAddress e);

    /** Is the email in our domains, without wildcard on domain */
    public boolean isValidAddressStandardUser(EmailAddress e);

    /** Is this domain fully relayed ?*/
    //public boolean isValidDomain(String domain);
    /** Relayed host, open relay for theses hosts */
    public boolean isValidRelay(String hostIP);
    
    public boolean isLocalDomain (String domain);
    
}