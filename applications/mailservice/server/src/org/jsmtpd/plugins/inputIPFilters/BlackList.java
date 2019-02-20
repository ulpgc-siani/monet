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
package org.jsmtpd.plugins.inputIPFilters;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsmtpd.core.common.PluginInitException;
import org.jsmtpd.core.common.PluginStore;
import org.jsmtpd.core.common.acl.IACL;
import org.jsmtpd.core.common.inputIPFilter.IFilterIP;

/**
 * @author Jean-Francois POUX
 */
public class BlackList implements IFilterIP {
    private List<String> blacklist = new ArrayList<String>();
    private IACL acl = null;
    private boolean bypassLocal = false;

    public boolean checkIP(InetAddress input) {
        String check = input.getHostAddress();
        if (bypassLocal && acl.isValidRelay(check))
            return true;
        for (Iterator<String> iter = blacklist.iterator(); iter.hasNext();) {
            String element = iter.next();
            if (check.equals(element))
                return false;
        }
        return true;
    }

    public String getPluginName() {
        return "Simple IP blacklist for Jsmtpd";
    }

    public void initPlugin() throws PluginInitException {
        acl = PluginStore.getInstance().getAcl();
        if (acl == null)
            throw new PluginInitException();
    }

    public void shutdownPlugin() {
    }

    public void setBypassLocal(boolean b) {
        bypassLocal = b;
    }

    public void setBlacklistedIP(String ip) {
        blacklist.add(ip);
    }
}