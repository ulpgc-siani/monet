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

import org.jsmtpd.config.ReadConfig;
import org.jsmtpd.core.mail.Email;
import org.jsmtpd.generic.threadpool.BusyThreadPoolException;
import org.jsmtpd.generic.threadpool.GrowingThreadPool;
import org.jsmtpd.generic.threadpool.ThreadPool;

/**
 * checks for mail on mqueue to be delivered, then passes them to a delivery thread
 * @author Jean-Francois POUX
 */
public class DeliveryPicker extends Thread {

    private ThreadPool pool = null;
    private boolean running = true;
    private QueueService queueService;

    public DeliveryPicker() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        ReadConfig readConfig = ReadConfig.getInstance();
        queueService= QueueService.getInstance();
        int numThreads = readConfig.getIntegerProperty("dMaxInstances");
        pool = new GrowingThreadPool(numThreads, "org.jsmtpd.core.send.DeliveryWorker","S");
        this.start();
    }

    public void run() {
        while (running) {
            if (pool.hasFreeThread())
            {
                Email e = null;
                e = queueService.getEmail();
                if (e != null) {
                    try {
                        pool.assignFreeThread(e);
                    } catch (BusyThreadPoolException e1) {
                        queueService.requeueMail(e);
                    }
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e2) {
                    }
                }
            } else {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public void shutdown() {
        running = false;
        wake();
        try {
            this.join();
        } catch (InterruptedException e) {
        }
        pool.forceShutdown();
        queueService.shutdownService();
    }

    public void wake() {
        this.interrupt();
    }
}