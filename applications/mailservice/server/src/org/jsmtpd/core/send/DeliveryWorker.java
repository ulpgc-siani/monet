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
package org.jsmtpd.core.send;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsmtpd.core.mail.Email;
import org.jsmtpd.generic.threadpool.IThreadedClass;

/**
 * implements the interface to be runable by the generic thread pooler
 * @author Jean-Francois POUX
 * @see org.jsmtpd.generic.threadpool.GenericThreadPool
 */
public class DeliveryWorker implements IThreadedClass {
	private static Log log = LogFactory.getLog(DeliveryWorker.class);
    private DeliveryHandler handler = new DeliveryHandler();
    private Email curentMail = null;

    public void doJob() {
        try {
            handler.processMessage(curentMail);

        } catch (RuntimeException e) {
            //=> requeue mail
            QueueService.getInstance().requeueMail(curentMail);
            log.error(e);
        }
        curentMail = null;
    }

    public void forceShutdown() {
        // Not really clean ... at all
        // or wait for curentMail to be null
        handler.clearMail(); //this will generate a null pointer exception excplicitly. Catched in doJob for requeing
        //wait for curmail to be null
        //Wait for curentMail to be null
        while (true) {
            if (curentMail == null)
                break;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                log.error (e);
            }
        }
    }
    
    public void gracefullShutdown() {}

    public void setParam(Object o) {
        curentMail = (Email) o;
    }
}