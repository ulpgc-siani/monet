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
package org.jsmtpd.core.common.dnsService;

import java.net.InetAddress;
import java.util.List;

import org.jsmtpd.core.common.IGenericPlugin;

/**
 * DNS resolving service<br>
 * handles MX requests
 * @see org.jsmtpd.plugins.dnsServices.DNSJavaResolver
 * @author Jean-Francois POUX
 */
public interface IDNSResolver extends IGenericPlugin {

    /**
     * 
     * @param name domain name of rcpt
     * @return a list containing the ip addresse of th MX servers, firts => top priority, last => lowest priority
     * 
     */
    public List<InetAddress> doMXLookup(String name);

    /**
     * Checks if a domain name exists
     * @param name the domain name to check
     * @return true if exists, false if not
     */

    public boolean exists(String name);

    /**
     * Resolve to ip address
     * @param name the domain name to check
     * @return the ip address of the domain name, null if it does not exists
     */
    public InetAddress doLookupToIp(String name);

    /**
     * Reverse lookup of an ip addr to domain name
     * @param in ip adress
     * @return the domain name
     */
    public String doReverseLookup(InetAddress in);

}