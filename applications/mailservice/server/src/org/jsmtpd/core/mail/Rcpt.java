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
 * Holds the delivery status of a mail RCPT
 * @author Jean-Francois POUX
 */
public class Rcpt implements Serializable {
    private EmailAddress emailAddress = null;
    private int deliveryAttempts = STATUS_PENDING;
    private int status;
    private String lastError="";
    
    public Rcpt(EmailAddress e) {
        emailAddress = e;
    }
    
    public Rcpt (Rcpt original) {
    	setEmailAddress(new EmailAddress(original.getEmailAddress()));
    	deliveryAttempts = original.getDeliveryAttempts();
    	status = original.getStatus();
    	lastError=new String (original.getLastError());
    }
    
    public EmailAddress getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(EmailAddress e) {
        emailAddress = e;
    }

    public int getDeliveryAttempts() {
        return deliveryAttempts;
    }

    /**
     * @return the RCPT is consired delivered when :
     * 		Successufully delivered
     * 		Fatal delivery error (in that case the server will genere a new mail to inform of the error)
     */
    public boolean isDelivered() {
        if (status == STATUS_DELIVERED || status == STATUS_ERROR_FATAL)
            return true;
        return false;
    }

    public void setDelivered(int status) {
        this.status = status;
        deliveryAttempts++;
    }

    public String getLastError() {
        return lastError;
    }
    public void setLastError(String lastError) {
        this.lastError = lastError;
    }

    public int getStatus() {
        return status;
    }

	public String toString() {
		if (emailAddress==null)
			return "null adress";
		return this.emailAddress.toString();
	}
    
    public static final int STATUS_PENDING = 0;
    public static final int STATUS_ERROR_NOT_FATAL = 1;
    public static final int STATUS_ERROR_FATAL = 2;
    public static final int STATUS_DELIVERED = 3;
}