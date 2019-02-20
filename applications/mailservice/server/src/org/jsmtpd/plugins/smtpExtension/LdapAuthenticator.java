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

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsmtpd.core.common.PluginInitException;
/**
 * 1/04/06 : Change to query primary mail by fixed attribute, not by uid (for multiple domains and outgoing)
 * @author jf poux
 *
 */
public class LdapAuthenticator extends SmtpAuthenticator {
	private static Log log = LogFactory.getLog(LdapAuthenticator.class);
	
    private String adminBindDn; // ex cn=administrator,dc=jsmtpd,dc=org
    private String adminBindPassword;
	private String ldapUrl;
	
	// How to query the user db
	private String ldapUserProvider;
	
    /**
     * By default, will use uid attribute (posix/shadow account schema), in this case login = uid
     * If you want vdom, set to primary mail attribute (should be unique in the directory).
     * Users will then provide their primary mail as login.
     */
    private String ldapUserLogin="uid";
	
	private String searchUserDn (String login){
		Hashtable<String,String> environnement;
		environnement = new Hashtable<String,String>();
		environnement.put(Context.SECURITY_PRINCIPAL,adminBindDn);
		environnement.put(Context.SECURITY_CREDENTIALS,adminBindPassword);
		
		InitialContext initialContext;
		try {
			initialContext = new InitialContext(environnement);
			DirContext ctx = (DirContext) initialContext.lookup(ldapUrl);
			SearchControls searchControl = new SearchControls();
			NamingEnumeration<SearchResult> namingEnumeration = ctx.search(ldapUserProvider,"("+ldapUserLogin+"="+login+")",searchControl);
			while (namingEnumeration.hasMore()) {
				SearchResult result = namingEnumeration.next();
				return (result.getNameInNamespace());
			}
		} catch (NamingException e) {
			log.error("Can't query server for user dn: "+login,e);
		}
		return null;
	}
	
	private boolean bindAs (String dn, byte[] password) {
		Hashtable<String,Object> environnement;
		environnement = new Hashtable<String,Object>();
		environnement.put(Context.SECURITY_PRINCIPAL,dn);
		environnement.put(Context.SECURITY_CREDENTIALS,password);
		InitialContext initialContext;
		try {
			initialContext = new InitialContext(environnement);
			DirContext ctx = (DirContext) initialContext.lookup(ldapUrl);
			ctx.close();
			return true;
		} catch (NamingException e) {
			log.error("Unable to bind user "+dn,e);
			return false;
		}
	}
	
	protected boolean performAuth(String login, byte[] password) {
		String userDn = searchUserDn(login);
		return bindAs (userDn, password);
	}

	public String getPluginName() {
		return "Ldap Authenticator for Jsmtpd";
	}

	public void initPlugin() throws PluginInitException {}

	public void shutdownPlugin() {}

	public void setLdapUrl(String ldapUrl) {
		this.ldapUrl = ldapUrl;
	}

	public void setLdapUserLogin(String ldapUserLogin) {
		this.ldapUserLogin = ldapUserLogin;
	}

	public void setLdapUserProvider(String ldapUserProvider) {
		this.ldapUserProvider = ldapUserProvider;
	}
    
    public void setAdminBindDn(String adminBindDn) {
        this.adminBindDn = adminBindDn;
    }
    
    public void setAdminBindPassword(String adminBindPassword) {
        this.adminBindPassword = adminBindPassword;
    }

}
