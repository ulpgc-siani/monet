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

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.jsmtpd.core.common.io.InvalidStreamParserInitialisation;
import org.jsmtpd.core.common.io.dataStream.DataStreamParser;
import org.jsmtpd.tools.DateUtil;

/**
 * Info class for pending E-Mail<br>
 * Any received mail is put in an instance of this class.<br>

 * 7/03/2005<br>
 * changed linkedlist of strings to a byte array (no errors of charset, efficient to store)
 * @author Jean-Francois POUX
 */
public class Email implements Serializable {
	/**
	 * If there was someone identified by the server while emting the mail.
	 */
	private String authContext = null;
    /**
     * Set by the receiver upon reception
     */
    private Date arrival;
    /**
     * Number of delivery attemps
     */
    private int attemps=0;

    /**
     * Recipients of the message
     */
    private List<Rcpt> rcpt = new LinkedList<Rcpt>();
    /**
     * Senders adress
     */
    private EmailAddress from;
    /**
     * Sender ip
     */
    private String receivedFrom;

    /**
     * Instance name on disk upon serialisation/writing
     */
    private String diskName = "default.mls";
    /**
     * Internal mails should not be filtered, and an already filtered mail should not be filtered again.
     */
    private boolean filtered = false;
    /**
     * Holding the DATA sent by the client, after eventual cleanings.
     */
    private byte[] dataBuffer = null;

    public Email() {
        generateDiskName();	
    }

    public void addRcpt(EmailAddress in) {
        rcpt.add(new Rcpt(in));
    }

    public Date getArrival() {
        return arrival;
    }

    public void setArrival(Date arrival) {
        this.arrival = arrival;
    }

    public int getAttemps() {
        return attemps;
    }

    public void setAttemps(int attemps) {
        this.attemps = attemps;
    }

    public void increaseAttempts () {
    	attemps++;
    }
    
    public String getReceivedFrom() {
        return receivedFrom;
    }

    public void setReceivedFrom(String receivedFrom) {
        this.receivedFrom = receivedFrom;
    }

    public List<Rcpt> getRcpt() {
        return rcpt;
    }
    
    public String getRcptAsString() {
        String rcp = "";
        for (Rcpt elm : rcpt) {
        	rcp += elm.getEmailAddress().toString() + ";";
		}
        return rcp;
    }

    public String toString() {
        String out = "Message Dump\n";
        out += "Received on " + arrival + " From " + receivedFrom + "\n";
        out += "Attempts :" + attemps + "\n";
        out += "FROM :" + from + "\n";
        Rcpt re;
        for (int i = 0; i < rcpt.size(); i++) {
            re = (Rcpt) rcpt.get(i);
            out += "RCPT: " + re.getEmailAddress().toString() + "\n";
        }
        return out;
    }

    public EmailAddress getFrom() {
        return from;
    }

    public void setFrom(EmailAddress from) {
        this.from = from;
    }

    public long getSize() {
        if (dataBuffer == null)
            return 0;
        return dataBuffer.length;
    }

    public static void save(String fileName, Email e) throws IOException {
        ObjectOutput out = new ObjectOutputStream(new FileOutputStream(fileName));
        out.writeObject(e);
        out.close();
    }

    public static Email load(String fileName) throws IOException {
        RandomAccessFile fp = new RandomAccessFile(fileName, "r");
        byte[] data = new byte[(int) fp.length()];
        fp.read(data);
        fp.close();
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(data));
        Email e = null;
        try {
            e = (Email) in.readObject();
        } catch (ClassNotFoundException cnfe) {}
        in.close();
        return e;
    }

    /**
     * If all recipients got the mail or permanent error, this should return true
     * @return true if the mail is delivered
     */
    public boolean isDelivered() {
    	for (Rcpt elm : rcpt) {
			if (!elm.isDelivered())
				return false;
		}
    	return true;    }

    public byte[] getDataAsByte() {
        return dataBuffer;
    }

    public static Email createInternalMail(EmailAddress to, String subject, LinkedList<String> mailMessages, Email attach) {
        Email e = new Email();
        e.generateDiskName();
        EmailAddress from = new EmailAddress();
        e.setFiltered(true);
        from.setUser("<>");
        e.setFrom(from);
        e.addRcpt(to);
        e.setArrival(new Date());
        e.setReceivedFrom("jsmtpd mailer daemon");

        try {
            DataStreamParser dsp = new DataStreamParser(512, 1024 * 1024 * 10);
            dsp.appendString("Message-ID: <" + e.getDiskName() + ">");
            dsp.appendString("From: \"Jsmtpd Mailer-Daemon\" <>");
            dsp.appendString("To: <" + to.toString() + ">");
            dsp.appendString("Subject: " + subject);
            dsp.appendString("Date: " + DateUtil.currentRFCDate());
            dsp.appendString("Content-Type: text/plain; charset=\"ISO-8859-1\"");
            dsp.appendString("Content-Transfer-Encoding: 7bit");
            dsp.appendString("");
            for (String string : mailMessages) {
            	dsp.appendString(string);
			}
            e.setDataBuffer(dsp.getData());
        } catch (InvalidStreamParserInitialisation e1) {
        }

        return e;
    }

    public String generateDiskName() {
        return diskName = Thread.currentThread().getId() + "-" + System.currentTimeMillis() + ".eml";
    }

    public String getDiskName() {
        return diskName;
    }

    public void setDiskName(String diskName) {
        this.diskName = diskName;
    }

    public boolean isFiltered() {
        return filtered;
    }

    public void setFiltered(boolean filtered) {
        this.filtered = filtered;
    }

    public void setDataBuffer(byte[] dataBuffer) {
        this.dataBuffer = dataBuffer;
    }

	public String getAuthContext() {
		return authContext;
	}

	public void setAuthContext(String authContext) {
		this.authContext = authContext;
	}
	
	public boolean hasRecipient (String in) {
		for (Rcpt rc : rcpt) {
			if (rc.getEmailAddress().toString().equals(in))
				return true;
		}
		return false;
	}
	
	public boolean contains (String in) throws Exception {
		if (dataBuffer==null)
			return false;
		MimeMessage msg = new MimeMessage(null,new ByteArrayInputStream(getDataAsByte()));
		if (msg.isMimeType("text/plain"))
			return msg.getContent().toString().contains(in) || msg.getContent().equals("");
		if (msg.isMimeType("text/html"))
			return msg.getContent().toString().contains(in)|| msg.getContent().equals("");
		if (msg.isMimeType("multipart/alternative")) {
			 MimeMultipart mpart = (MimeMultipart) msg.getContent();
			 String all = "";
	            for (int i= 0; i < mpart.getCount(); i++) {
	                MimeBodyPart bodyPart = (MimeBodyPart) mpart.getBodyPart(i);
	                if (bodyPart.isMimeType("text/html") || bodyPart.isMimeType("text/plain"))
	                	all+=bodyPart.getContent().toString();
	            }
	            return all.contains(in)|| all.equals("");
		}
		return false;
	}
	
	public Email(Email original) {
		setFrom(new EmailAddress(original.getFrom()));
		byte[] newData = new byte[original.getDataAsByte().length];
		System.arraycopy(original.dataBuffer,0,newData,0,newData.length);
		setDataBuffer(newData);
		generateDiskName();
		if (original.authContext!=null)
			setAuthContext(new String (original.authContext));
		setFiltered(false);
		if (original.getReceivedFrom()!=null)
			setReceivedFrom(new String (original.receivedFrom));
		List<Rcpt> rcpts = original.getRcpt();
		for (Rcpt rcpt : rcpts) {
			addRcpt(rcpt.getEmailAddress());
		}
	}
	public Email copy () {
		return new Email(this);
	}
}