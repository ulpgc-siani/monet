/*
 * 
 * Jsmtpd, Java SMTP daemon
 * Copyright (C) 2005-2006  Jean-Francois POUX, jf.poux@laposte.net
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
package org.jsmtpd.tools.rights;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * It tries to change owner of a file invoking a system command
 * (fork + exec), so it's slow, relies on system.
 * Anyway, it should work on unixes
 * 
 * Another solution would be a JNI call on a so, but it would be less 
 * portable, require a C compiler a build time, etc...
 * 
 * @author Jean-Francois POUX
 */
public class UnixChown implements IChown {
	private Log log = LogFactory.getLog(UnixChown.class);
	
	public void chown(String file, String user) throws RightException {
		String[] args = new String [3];
		args[0] = "chown";
		args[1] = user;
		args[2] = file;
		if (file.contains(".."))
			throw new RightException("There is a .. in the file path.");
		try {
			Process child = Runtime.getRuntime().exec(args);
			child.waitFor();
			int ret = child.exitValue();
			if (ret!=0)
				throw new RightException ("Could not perform chown "+user+" "+file);
		} catch (Exception e) {
			throw new RightException (e);
		}
		log.debug("Performed chown "+user+" "+file);
	}

	public void recursiveChown(String file, String user) throws RightException {
		String[] args = new String [4];
		args[0] = "chown";
		args[1] ="-R";
		args[2] = user;
		args[3] = file;
		if (file.contains(".."))
			throw new RightException("There is a .. in the file path.");
		try {
			Process child = Runtime.getRuntime().exec(args);
			child.waitFor();
			int ret = child.exitValue();
			if (ret!=0)
				throw new RightException ("Could not perform chown "+user+" "+file);
		} catch (Exception e) {
			throw new RightException (e);
		}
		log.debug("Performed chown -R "+user+" "+file);
	}

}
