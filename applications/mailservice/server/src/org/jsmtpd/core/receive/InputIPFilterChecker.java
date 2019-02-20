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
package org.jsmtpd.core.receive;

import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsmtpd.core.common.PluginStore;
import org.jsmtpd.core.common.inputIPFilter.IFilterIP;


public class InputIPFilterChecker {
    private List<IFilterIP> ipFilters=new LinkedList<IFilterIP>();
    private Log log = LogFactory.getLog(InputIPFilterChecker.class);
    
    public InputIPFilterChecker (){
        ipFilters=PluginStore.getInstance().getInputIPFilters();
    }
    
    public boolean checkIPAgainstFilters (InetAddress toCheck) {
    	for (IFilterIP filter : ipFilters) {
    		if (!filter.checkIP(toCheck)) {
                log.warn("Client ip " + toCheck.getHostAddress() + " is blacklisted by " + filter.getPluginName() + ", closing connection");
                return false;
            }	
		}
        return true;
    }
}
