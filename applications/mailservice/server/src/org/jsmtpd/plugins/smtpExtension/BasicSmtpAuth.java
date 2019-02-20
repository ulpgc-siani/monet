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
package org.jsmtpd.plugins.smtpExtension;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.jsmtpd.core.common.PluginInitException;
import org.jsmtpd.tools.Base64Helper;
import org.jsmtpd.tools.ByteArrayTool;

/**
 * @author Jean-Francois POUX
 */
public class BasicSmtpAuth extends SmtpAuthenticator {

    private Map<String,byte[]> users = new HashMap<String,byte[]>();
    private MessageDigest md;
    protected boolean performAuth(String login, byte[] password) {
        if (!users.containsKey(login))
            return false;

        byte[] hash = (byte[]) users.get(login);
        
        return ByteArrayTool.compare(hash, md.digest(password));
    }

    private void addPlainUser(String user, String password) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        users.put(user, md5.digest(password.getBytes()));
    }

    private void addMD5User(String user, String md5base64password) {
        users.put(user, Base64Helper.decode(md5base64password));
    }

    public void setPlainUser(String in) throws Exception {
        String[] tmp = in.split(",");
        addPlainUser(tmp[0], tmp[1]);
    }

    public void setMD5User(String in) {
        String[] tmp = in.split(",");
        addMD5User(tmp[0], tmp[1]);
    }

	@Override
	public void initPlugin() throws PluginInitException {
		try {
			md=MessageDigest.getInstance("md5");
		} catch (NoSuchAlgorithmException e) {
			throw new PluginInitException("md5 not available");
		}
		super.initPlugin();
	}
}