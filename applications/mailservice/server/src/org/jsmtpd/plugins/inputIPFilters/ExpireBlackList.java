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
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * BlackList + temporary volatile blacklist.
 * @author jfp
 *
 */
public class ExpireBlackList extends BlackList {
	private Log log = LogFactory.getLog(ExpireBlackList.class);
	private Map<InetAddress,Date> timedBlackList = new HashMap<InetAddress,Date>();
	private int maxTable = 2000;
	
	
	@Override
	public boolean checkIP(InetAddress input) {
		boolean fixed = super.checkIP(input);
		if (!fixed)
			return false;
		
		return true;
	}
	
	public void setTimedBlackList (long time, String ip) {
		expire ();
		if (timedBlackList.size()>maxTable)
			return;
		long target = new Date().getTime()+time;
		try {
			log.info("Blacklisting "+ip+" for "+time+" seconds");
			timedBlackList.put(InetAddress.getByName(ip),new Date(target));
		} catch (UnknownHostException e) {
			log.error("Blacklisting "+ip+" for "+time+" seconds failed",e);
		}
	}
	
	private void expire () {
		Date now = new Date();
		for (InetAddress addr : timedBlackList.keySet()) {
			Date cmp = timedBlackList.get(addr);
			if (cmp.getTime()<now.getTime())
				timedBlackList.remove(addr);
		}
	}
}
