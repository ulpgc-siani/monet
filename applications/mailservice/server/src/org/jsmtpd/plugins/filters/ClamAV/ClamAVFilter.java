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
package org.jsmtpd.plugins.filters.ClamAV;

import java.io.ByteArrayInputStream;
import java.util.LinkedList;
import org.jsmtpd.clamav.ClamAVScanner;
import org.jsmtpd.clamav.ClamAVScannerFactory;
import org.jsmtpd.clamav.ScannerException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsmtpd.core.common.PluginInitException;
import org.jsmtpd.core.common.filter.FilterTreeFailureException;
import org.jsmtpd.core.common.filter.IFilter;
import org.jsmtpd.core.mail.Email;
import org.jsmtpd.core.send.QueueService;

/**
 * 
 * This plugin is a client to the ClamAV antivirus
 * daemon.<br>
 * 
 * It uses libclamav, available at http://dev.taldius.net/libclamav/
 * 
 * @author Jean-Francois POUX
 */
public class ClamAVFilter implements IFilter {

    private Log log = LogFactory.getLog(ClamAVFilter.class);
    /**
     * Will cause a virus detection to automaticly break the chain
     * and generate an error message to the sender
     */
    private boolean failOnError = false;


    public boolean doFilter(Email input) throws FilterTreeFailureException {
    	log.debug("Starting ClamAV Scan of "+input.getDiskName());
    	long time = System.currentTimeMillis();
    	
    	ClamAVScanner scanner = ClamAVScannerFactory.getScanner();
    	ByteArrayInputStream is = new ByteArrayInputStream (input.getDataAsByte());
    	
    	boolean response;
		try {
			response = scanner.performScan(is);
			
			log.debug("Scanned "+input.getDiskName()+ ", "+input.getSize()+" octs in "+(System.currentTimeMillis()-time)+" ms");
	        if ( response == true) {
	            return true;
	        }

	        if (failOnError) {
	            if (!input.getFrom().toString().equals("<>")) // is it an internal mail already ?
	            {
	                LinkedList<String> messages = new LinkedList<String>();
	                messages.add("");
	                messages.add("Hello, this is the Jsmtpd mailer daemon");
	                messages.add("");
	                messages.add("I'm affraid I can't deliver your email to " + input.getRcptAsString());
	                messages.add("");
	                messages.add(getPluginName() + " has detected the virus : " + scanner.getMessage());
	                messages.add("");
	                messages.add("This is a fatal error, giving up");
	                Email error = Email.createInternalMail(input.getFrom(), "Mailer-daemon error, virus found", messages, input);
	                QueueService.getInstance().queueMail(error);
	            }
	            throw new FilterTreeFailureException();
	        }
			
		} catch (ScannerException e) {
			log.error("Failed to scan "+input.getDiskName(),e);
			return failOnError;
		}
        return false;
    }

    public String getPluginName() {
        return "Jsmtpd-ClamAV antivirus filter";
    }

    public void initPlugin() throws PluginInitException {
        log.debug(getPluginName() + " initialized");
    }

    public void shutdownPlugin() {

    }

    public void setClamdHost(String host) {
       ClamAVScannerFactory.setClamdHost(host);
    }

    public void setClamdPort(int port) {
        ClamAVScannerFactory.setClamdPort(port);
    }

    public void setFailOnError(boolean pa) {
        failOnError = pa;
    }
    
    public void setConnectionTimeout(int connectionTimeout) {
    	ClamAVScannerFactory.setConnectionTimeout(connectionTimeout*1000);
    }
}