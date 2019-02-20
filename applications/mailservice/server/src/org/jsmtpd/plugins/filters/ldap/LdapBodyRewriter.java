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

package org.jsmtpd.plugins.filters.ldap;

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
import org.jsmtpd.core.common.filter.FilterTreeFailureException;
import org.jsmtpd.core.common.filter.FilterTreeSuccesException;
import org.jsmtpd.core.common.filter.IFilter;
import org.jsmtpd.core.mail.Email;
import org.jsmtpd.core.mail.EmailAddress;
import org.jsmtpd.core.mail.InvalidAddress;
import org.jsmtpd.core.mail.Rcpt;
/**
 * 1/04/06 : Change to query primary mail by fixed attribute, not by uid (for multiple domains and outgoing)
 * @author jf poux
 *
 */
public class LdapBodyRewriter implements IFilter {
    private String adminBindDn; // ex cn=administrator,dc=jsmtpd,dc=org
    private String adminBindPassword;
    private String ldapUrl; // ex ldap://172.16.0.200/
	
	// How to query the user db
    private String ldapUserProvider; // Branch containing user objects, like ou=people,dc=jsmtpd,dc=org
    private String ldapUserAliasAttribute="rfc822MailAlias"; // Attribute of mail aliases
    private String ldapUserPrimaryMail="mail"; // Attribute of primary mail address.
	private boolean replacePrimaryMail=true;
    
    private Hashtable<String,String> environnement;
	private static Log log = LogFactory.getLog(LdapBodyRewriter.class);
	
	public boolean doFilter(Email input) throws FilterTreeFailureException, FilterTreeSuccesException {
		// First, see if it is an existing uid. ok = return
		// The, see if it is an alias. ok = find uid, rewrite to uid, return
		// See if it has catchAll. Appli catches
		Set<Rcpt> toRemove = new HashSet<Rcpt>();
		Set<Rcpt> toAdd = new HashSet<Rcpt>();
		List<Rcpt> recipients=input.getRcpt();
		for (Rcpt rcpt : recipients) {
			if (!isPrimaryMail(rcpt)) {
				Set<Rcpt> aliases = computeAliases(rcpt);
				if (aliases!=null) {
					toRemove.add(rcpt);
					for (Rcpt alias : aliases) {
						log.debug("user "+rcpt.toString()+" is aliased to "+alias.toString()+" (recipient changed)");
						toAdd.add(alias);
					}
				} else {
					Set<Rcpt> catchAlls = computeCatchAll(rcpt);
					if (catchAlls!=null) {
						toRemove.add(rcpt);
						for (Rcpt catchAll : catchAlls) {
							log.debug("user "+rcpt.toString()+" is catch all to "+catchAll.toString()+" (recipient changed)");
							toAdd.add(catchAll);
						}
					} else {
						log.debug(" user "+rcpt.toString()+" is neither uid, alias, or catchall");
					}
				}
			} else {
				log.debug(" user "+rcpt.toString()+" is a real uid");
			}
		}
		// Commit changes
		recipients.removeAll(toRemove);
		recipients.addAll(toAdd);
		return true;
	}

	private boolean isPrimaryMail (Rcpt recipient) {
		InitialContext initialContext;
		try {
			initialContext = new InitialContext(environnement);
			DirContext ctx = (DirContext) initialContext.lookup(ldapUrl);
			SearchControls searchControl = new SearchControls();
			NamingEnumeration<SearchResult> namingEnumeration;
			String ldapQuery="("+ldapUserPrimaryMail+"="+recipient.getEmailAddress().toString()+")";
			namingEnumeration = ctx.search(ldapUserProvider,ldapQuery,searchControl);
			if (namingEnumeration.hasMore()) {
				return true;
			}
		} catch (NamingException e1) {
			log.error(" Failed to query server",e1);
		}
		return false;
	}
	
	// Returns null is no alias
	// Returns a set of real Rcpt if aliases are set
	private Set<Rcpt> computeAliases (Rcpt recipient) {
		Set<Rcpt> newRecipients = new HashSet<Rcpt>();
		InitialContext initialContext;
		try {
			initialContext = new InitialContext(environnement);
			DirContext ctx = (DirContext) initialContext.lookup(ldapUrl);
			SearchControls searchControl = new SearchControls();
			NamingEnumeration<SearchResult> namingEnumeration = ctx.search(ldapUserProvider,"("+ldapUserAliasAttribute+"="+
					recipient.getEmailAddress().toString()+")",searchControl);
			while (namingEnumeration.hasMore()) {
				SearchResult result = namingEnumeration.next();
				Attributes attributes = result.getAttributes();
				Attribute uid = attributes.get(ldapUserPrimaryMail);
				EmailAddress ad = EmailAddress.parseAddress((String)uid.get());
				if (!replacePrimaryMail)
				    ad.setHost(recipient.getEmailAddress().getHost());    
				newRecipients.add(new Rcpt(ad));
			}
		} catch (NamingException e) {
			log.error(" Can't query server for aliases",e);
		} catch (InvalidAddress e) {
            log.error(" invalid addres in alias of "+recipient.getEmailAddress().toString(),e);
        }
		
		if (newRecipients.size()>0)
			return newRecipients;
		else
			return null;
	}
	// Returns null is no alliases are defined
	// Returns a set of real uid rcpt if positive.
	private Set<Rcpt> computeCatchAll (Rcpt recipient) {
		Set<Rcpt> newRecipients = new HashSet<Rcpt>();
		InitialContext initialContext;
		try {
			initialContext = new InitialContext(environnement);
			DirContext ctx = (DirContext) initialContext.lookup(ldapUrl);
			SearchControls searchControl = new SearchControls();
			NamingEnumeration<SearchResult> namingEnumeration = ctx.search(ldapUserProvider,"("+ldapUserAliasAttribute+"=@"+
					recipient.getEmailAddress().getHost()+")",searchControl);
			while (namingEnumeration.hasMore()) {
				SearchResult result = namingEnumeration.next();
				Attributes attributes = result.getAttributes();
				Attribute uid = attributes.get(ldapUserPrimaryMail);
				EmailAddress ad = EmailAddress.parseAddress((String)uid.get());
				newRecipients.add(new Rcpt(ad));
			}
		} catch (NamingException e) {
			log.error(" Can't query server for catchAll",e);
		} catch (InvalidAddress e) {
            log.error(" invalid addres in alias(catchall) of "+recipient.getEmailAddress().toString(),e);
        }
		
		if (newRecipients.size()>0)
			return newRecipients;
		else
			return null;
	}
	
	public String getPluginName() {
		return "Ldap body rewriter for Jsmtpd";
	}

	public void initPlugin() throws PluginInitException {
		environnement = new Hashtable<String,String>();
		environnement.put(Context.SECURITY_PRINCIPAL,adminBindDn);
		environnement.put(Context.SECURITY_CREDENTIALS,adminBindPassword);
	}

	public void shutdownPlugin() {
		
	}

    public void setAdminBindDn(String adminBindDn) {
        this.adminBindDn = adminBindDn;
    }

    public void setAdminBindPassword(String adminBindPassword) {
        this.adminBindPassword = adminBindPassword;
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
    
    public void setReplacePrimaryMail(boolean replacePrimaryMail) {
        this.replacePrimaryMail = replacePrimaryMail;
    }
}
