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
package org.jsmtpd.plugins.acls;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsmtpd.core.common.PluginInitException;
import org.jsmtpd.core.common.acl.ExtendedInet4Address;
import org.jsmtpd.core.common.acl.IACL;
import org.jsmtpd.core.mail.EmailAddress;
/**
 * 1/04/06 : Change to query primary mail by fixed attribute, not by uid (for multiple domains and outgoing)
 * @author jf poux
 *
 */
public class LdapACL implements IACL {

	private String adminBindDn; // ex cn=administrator,dc=jsmtpd,dc=org
	private String adminBindPassword;
	private String ldapUrl; // ex ldap://172.16.0.200/
	
	// How to query the user db
	private String ldapUserProvider; // Branch containing user objects, like ou=people,dc=jsmtpd,dc=org
	private String ldapUserAliasAttribute="rfc822MailAlias"; // Attribute of mail aliases
	private String ldapUserPrimaryMail="mail"; // Attribute of primary mail address.

	
	private String ldapNetworkProvider; // A branch containing network objects (class ipNetwork by example) : ou=networks,dc=jsmtpd,dc=org
	private String ldapNetworkClass="ipNetwork"; 
	private String ldapNetworkAddressAttribute="ipNetworkNumber"; // Atribute of network ip
	private String ldapNetworkMaskAttribute="ipNetmaskNumber"; // attribut of network mask
    
	private Hashtable<String,String> environnement;
	private static Log log = LogFactory.getLog(LdapACL.class);
	
	private List<String> localDomains=new ArrayList<String>();
	
	public void initPlugin() throws PluginInitException {
		environnement = new Hashtable<String,String>();
		environnement.put(Context.SECURITY_PRINCIPAL,adminBindDn);
		environnement.put(Context.SECURITY_CREDENTIALS,adminBindPassword);
	}

	public boolean isValidAddress(EmailAddress e) {
        if (isValidAddressWildCard(e))
            return true;
        else
            return isValidAddressStandardUser(e);
	}

	public boolean isValidAddressStandardUser(EmailAddress e) {
		InitialContext initialContext=null;
		NamingEnumeration<SearchResult> namingEnumeration=null;
		DirContext ctx=null;
		try {
			initialContext = new InitialContext(environnement);
			ctx = (DirContext) initialContext.lookup(ldapUrl);
			SearchControls searchControl = new SearchControls();
			
            // Will look for primary mail field.
			String ldapQuery ="("+ldapUserPrimaryMail+"="+e.toString()+")";
			namingEnumeration = ctx.search(ldapUserProvider,ldapQuery,searchControl);
			boolean found = namingEnumeration.hasMore();
			namingEnumeration.close();
			if (found) {
				log.debug("User "+e.toString()+" found in directory");
				return true;
			} 
						
			// Lookup alias
			ldapQuery="("+ldapUserAliasAttribute+"="+e.toString()+")";
			namingEnumeration = ctx.search(ldapUserProvider,ldapQuery,searchControl);
			found = namingEnumeration.hasMore();
			namingEnumeration.close();
			if (found) {
				log.debug("Alias "+e.toString()+" found in directory");
				return true;
			}
			
		} catch (NamingException e1) {
			log.error("Failed to query server",e1);
		} finally {
			try {
				if (namingEnumeration!=null)
					namingEnumeration.close();
				if (ctx!=null)
					ctx.close();
				if (initialContext!=null)
					initialContext.close();
			} catch (NamingException e1) {
				log.error(e1);
			}
		}
		return false;
	}

	public boolean isValidAddressWildCard(EmailAddress e) {
		InitialContext initialContext=null;
		DirContext ctx=null;
		NamingEnumeration<SearchResult> namingEnumeration=null;
		try {
			initialContext = new InitialContext(environnement);
			ctx = (DirContext) initialContext.lookup(ldapUrl);
			SearchControls searchControl = new SearchControls();
			
			// see if there are users with *@domain.com wildcard aliases
			String query = "("+ldapUserAliasAttribute+"=@"+e.getHost()+")";
			namingEnumeration = ctx.search(ldapUserProvider,query,searchControl);
			boolean found = namingEnumeration.hasMore();
			namingEnumeration.close();
			if (found) {
				log.debug("Wildcard alias "+e.toString()+" found in directory");
				return true;
			}
		
		} catch (NamingException e1) {
			log.error("Failed to query server",e1);
		} finally {
			try {
				if (namingEnumeration!=null)
					namingEnumeration.close();
				if (ctx!=null)
					ctx.close();
				if (initialContext!=null)
					ctx.close();
			} catch (NamingException e1) {
				log.error(e1);
			}
		}
 		return false;
	}

	public void shutdownPlugin() {
		
	}


	public boolean isValidRelay(String hostIP) {
		
		InitialContext initialContext=null;
		DirContext ctx=null;
		NamingEnumeration<SearchResult> namingEnumeration=null;
		// This set is rebuilt for each query. Can be cached with expiraton time
		Set<ExtendedInet4Address> ldapNetworks=new HashSet<ExtendedInet4Address>();
		Inet4Address ag;
		try {
			initialContext = new InitialContext(environnement);
			ctx = (DirContext) initialContext.lookup(ldapUrl);
			SearchControls searchControl = new SearchControls();
			namingEnumeration = ctx.search(ldapNetworkProvider,"(objectclass="+ldapNetworkClass+")",searchControl);
			while (namingEnumeration.hasMore()) {
				SearchResult result = namingEnumeration.next();
				Attributes attributes = result.getAttributes();
				Attribute ip = attributes.get(ldapNetworkAddressAttribute);
				Attribute mask = attributes.get(ldapNetworkMaskAttribute);
				
				if ((ip==null)||(mask==null)) 
					throw new NamingException("Can't fing ip or netmask");
				
				Inet4Address ipInet = (Inet4Address) InetAddress.getByName( (String)ip.get());
				Inet4Address maskInet = (Inet4Address) InetAddress.getByName( (String)mask.get());
				ExtendedInet4Address ad = new ExtendedInet4Address (ipInet,maskInet);
				ldapNetworks.add(ad);
			}
			ag = (Inet4Address) InetAddress.getByName(hostIP);
			for (ExtendedInet4Address address : ldapNetworks) {
				if (address.isEqualorInMask(ag))
					return true;
			}
		} catch (NamingException e) {
			log.error("Can't query server for relays",e);
		} catch (UnknownHostException e) {
			log.error("Conversion of ip failed ",e);
		} finally {
			try {
				if (namingEnumeration!=null)
					namingEnumeration.close();
				if (ctx!=null)
					ctx.close();
				if (initialContext!=null)
					ctx.close();
			} catch (NamingException e1) {
				log.error(e1);
			}
		}
		return false;
	}

	public String getPluginName() {
		return "Simple LDAP ACL provider for jsmtpd";
	}

    
    public void setAdminBindDn(String adminBindDn) {
        this.adminBindDn = adminBindDn;
    }

    
    public void setAdminBindPassword(String adminBindPassword) {
        this.adminBindPassword = adminBindPassword;
    }

    
    public void setLdapNetworkAddressAttribute(String ldapNetworkAddressAttribute) {
        this.ldapNetworkAddressAttribute = ldapNetworkAddressAttribute;
    }

    
    public void setLdapNetworkClass(String ldapNetworkClass) {
        this.ldapNetworkClass = ldapNetworkClass;
    }

    
    public void setLdapNetworkMaskAttribute(String ldapNetworkMaskAttribute) {
        this.ldapNetworkMaskAttribute = ldapNetworkMaskAttribute;
    }

    
    public void setLdapNetworkProvider(String ldapNetworkProvider) {
        this.ldapNetworkProvider = ldapNetworkProvider;
    }

    
    public void setLdapUrl(String ldapUrl) {
        this.ldapUrl = ldapUrl;
    }

    
    public void setLdapUserAliasAttribute(String ldapUserAliasAttribute) {
        this.ldapUserAliasAttribute = ldapUserAliasAttribute;
    }
        
    public void setLdapUserPrimaryMail(String ldapUserPrimaryMail) {
        this.ldapUserPrimaryMail = ldapUserPrimaryMail;
    }

    public void setLdapUserProvider(String ldapUserProvider) {
        this.ldapUserProvider = ldapUserProvider;
    }

	public boolean isLocalDomain(String domain) {
		return localDomains.contains(domain);
	}

	public void setLocalDomain (String domain) {
		localDomains.add(domain);
	}

}
