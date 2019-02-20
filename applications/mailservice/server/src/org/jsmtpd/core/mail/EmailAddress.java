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
package org.jsmtpd.core.mail;

import java.io.Serializable;

/**
 * Helper class to store an email address
 * @author Jean-Francois POUX
 * 
 */
public class EmailAddress implements Serializable {

    private String user = "";
    private String host = "";
    private String authContext = "";

    /**
     * Creates an instance by parsing a string user@domain, or <>
     * @param input the string
     * @return instance
     * @throws InvalidAddress when parsing error
     */
    public static EmailAddress parseAddress(String input) throws InvalidAddress {
        if ((input==null) || input.equals(""))
            throw new InvalidAddress();
         
        if (input.equals("<>")) {
            EmailAddress e = new EmailAddress();
            e.setUser("<>");
            return e;
        }
        EmailAddress e = new EmailAddress();
        String mailPart;
        String tmp = input.toLowerCase().trim();
        if (tmp.indexOf(" auth=")!=-1) {
            String[] res = input.trim().split(" ");
            e.setAuthContext(res[1].trim());
            mailPart=res[0].trim();
        } else
            mailPart=input.trim();     
        
        if (!mailPart.matches("^[_A-Za-z0-9-+=*#]+(\\.[_A-Za-z0-9-+=*#]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*$"))
        	throw new InvalidAddress();
        
        String[] res = mailPart.split("@");
        
        if ((res[0].length() > 512) || (res[1].length() > 512))
            throw new InvalidAddress();

        e.setUser(res[0]);
        e.setHost(res[1].toLowerCase());
        return e;
    }

    public String toString() {
        if (user.equals("<>"))
            return user;
        return (user + "@" + host);
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public boolean isEqual(EmailAddress in) {
        if (in == this)
            return true;
        if (in.getHost().equals(this.host) && in.getUser().equals(this.user))
            return true;
        if (in.getHost().equals(this.host) && in.getUser().equals("*"))
            return true;
        return false;
    }
     
    public int hashCode() {
        String tmp=host;
        return tmp.hashCode();
    }
    public boolean equals(Object obj) {
        if (! (obj instanceof EmailAddress))
            return false;
        return isEqual((EmailAddress)obj);
    }
    
    public EmailAddress () {}
    
    public EmailAddress (EmailAddress original) {
    	setHost(new String (original.getHost()));
    	setUser(new String (original.getUser()));
    }

    public String getAuthContext() {
        return authContext;
    }

    public void setAuthContext(String authContext) {
        this.authContext = authContext;
    }
}