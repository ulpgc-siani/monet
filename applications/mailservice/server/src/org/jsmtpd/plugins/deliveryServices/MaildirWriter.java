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

package org.jsmtpd.plugins.deliveryServices;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsmtpd.config.ReadConfig;
import org.jsmtpd.core.common.PluginInitException;
import org.jsmtpd.core.common.delivery.FatalDeliveryException;
import org.jsmtpd.core.common.delivery.IDeliveryService;
import org.jsmtpd.core.common.delivery.TemporaryDeliveryException;
import org.jsmtpd.core.mail.Email;
import org.jsmtpd.core.mail.Rcpt;
import org.jsmtpd.tools.ByteArrayTool;
import org.jsmtpd.tools.rights.IChown;
import org.jsmtpd.tools.rights.RightException;
import org.jsmtpd.tools.rights.UnixChown;
/**
 * This has been tested with solid-pop3d, under linux.
 * There is nothing in java to change the owner of a file (as far as I know ;), 
 * but some (many) pop/imap daemons working with mdir requires permissions to be set.
 * The current impl to change right invokes a system command (fork process ...), it's not
 * really beautifull, you have to run Jsmtpd as root, but it works.
 * 
 * Don't use in production/stressed environment, behavior should not be good.
 * 
 * @author Jean-Francois POUX
 *
 */
public class MaildirWriter implements IDeliveryService {
	private String basePath="/var/mail";
	private String postPath="";
	private String host = ReadConfig.getInstance().getProperty("localHost");
	private Log log = LogFactory.getLog(MaildirWriter.class);
	private IChown chown = new UnixChown();
	private boolean tryChown=false;
	private boolean crlf=false;
	
	public void setCrlf(boolean crlf) {
		this.crlf = crlf;
	}

	public void setChown(IChown chown) {
		this.chown = chown;
	}

	public void doDelivery(Email in, List<Rcpt> rcpts) {
		log.debug("Starting batch");
		for (Rcpt rcpt : rcpts) {
			try {
                deliver(in,rcpt);                
                rcpt.setDelivered(Rcpt.STATUS_DELIVERED);
                log.debug("mail delivered to "+rcpt.getEmailAddress().toString());
            } catch (FatalDeliveryException e) {
                rcpt.setDelivered(Rcpt.STATUS_ERROR_FATAL);
                log.debug("fatal delivery error for "+rcpt.getEmailAddress().toString());
            } catch (TemporaryDeliveryException e) {
                rcpt.setDelivered(Rcpt.STATUS_ERROR_NOT_FATAL);
                log.debug("temporary delivery error for "+rcpt.getEmailAddress().toString());
            } catch (RightException e) {
            	log.debug("Fatal delivery error for rcpt (right error)"+rcpt.getEmailAddress().toString());
            	rcpt.setDelivered(Rcpt.STATUS_ERROR_FATAL);
            }
		}
		log.debug("ending batch");
	}

	public String getPluginName() {
		return "Maildir Writer for Jsmtpd";
	}

	public void initPlugin() throws PluginInitException {
		File fl = new File(basePath);
		fl.mkdir();
	}

	public void shutdownPlugin() {
	}
	
	private void deliver (Email in, Rcpt rcpt) throws TemporaryDeliveryException,FatalDeliveryException, RightException {
		String user = rcpt.getEmailAddress().getUser().toLowerCase(); // This assumes that a rcpt rewriter plugin exists for handling aliases, and rewrites to system uids.
		
		//	Make sure dirs exists
		String mpath = basePath+"/"+rcpt.getEmailAddress().getUser()+"/"+postPath+"/";
		File fl = new File (mpath);
		// if no exists mkdirs + rchown
		if (!fl.exists()) {
			fl.mkdirs();
			if (tryChown)
				chown.recursiveChown(fl.toString(),user);
		}
		File tmp = new File (mpath+"/tmp/");
		if (!tmp.exists()) {
			tmp.mkdirs();
			if (tryChown)
				chown.recursiveChown(tmp.toString(),user);
		}
		File cur = new File (mpath+"/cur/");
		if (!cur.exists()) {
			cur.mkdirs();
			if (tryChown)
				chown.recursiveChown(cur.toString(),user);
		}
		File newF = new File(mpath+"/new/");
		if (!newF.exists()) {
			newF.mkdirs();
			if (tryChown)
				chown.chown(newF.toString(),user);
		}
		
		// Write to tmp directory
		String fileName = makeUid();
		File target = new File (tmp.getAbsolutePath()+"/"+fileName);
		RandomAccessFile raf;
			try {
				raf = new RandomAccessFile (target,"rw");
				raf.seek(0);
			    //DataStreamParser dsp = new DataStreamParser(512, 512);
                //dsp.appendString("From " + in.getFrom().toString() + " " + DateUtil.currentMailboxDate());
                //raf.write(ByteArrayTool.replaceBytes(dsp.getData(), ByteArrayTool.CRLF, ByteArrayTool.LF));
			    if (crlf) {
			    	raf.write(ByteArrayTool.replaceBytes(in.getDataAsByte(),ByteArrayTool.LF,ByteArrayTool.CRLF));
			    	byte[] lf = new byte[2];
			        lf[0] = 13;
			        lf[1] = 10;
			        raf.write(lf);
			    }
			    else {
			    	raf.write(ByteArrayTool.replaceBytes(in.getDataAsByte(),ByteArrayTool.CRLF,ByteArrayTool.LF));
			    	byte[] lf = new byte[1];
			        lf[0] = 10;
			        raf.write(lf);
			    }
				
				raf.close();
			} catch (FileNotFoundException e) {
				log.error("File not found: "+e);
				throw new TemporaryDeliveryException(e);
			} catch (IOException e) {
				log.error("Io error ",e);
				throw new TemporaryDeliveryException (e);
			} 

		if (tryChown)
			chown.chown(target.toString(),user);
		
		// Commit = mv
		File commit = new File (cur.getAbsolutePath()+"/"+fileName);
		target.renameTo(commit);
	}
	
	/**
	 * See spec of D. J. Bernstein and what we can do in Java
	 * @return
	 */
	private String makeUid () {
		return ""+System.currentTimeMillis()+"."+Thread.currentThread().getId()+"."+host;
	}

	/**
	 * The path is made of basePath/user/postPath/
	 * @param basePath maildir storage prefix
	 */
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
	
	/**
	 * The path is made of basePath/user/postPath/
	 * @param postPath path suffix.
	 */
	public void setPostPath(String postPath) {
		this.postPath = postPath;
	}

	public void setTryChown(boolean tryChown) {
		this.tryChown = tryChown;
	}
	
}
