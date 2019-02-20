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
package org.jsmtpd.generic.threadpool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Handles a job
 * @author Jean-Francois POUX
 * Jsmtp
 */
public class ThreadWorker extends Thread {

    private boolean free = false;
    private boolean running = true;
    private IThreadedClass wrk;
    private Log log = LogFactory.getLog(ThreadWorker.class);
    
    public void run() {
        while (true) {
            goSleep();
            if (!running) {
                log.debug("Worker "+getName()+" stopping");
                return;
            }
            try {
                wrk.doJob();
            } catch (RuntimeException e) {
               log.error("Generic Thread pool worker #" + Thread.currentThread().getId() + " Caught a runtime exception : ",e);
               log.error("Thread job lost, recycling thread");
            }
        }    
    }

    public void forceShutdown() {
        running = false;
        free = false;
        wrk.forceShutdown();
        wake();
    }

    public void gracefullShutdown() {
        running = false;
        free = false;
        wrk.gracefullShutdown();
        wake();
    }

    public void setParam(Object o) {
        wrk.setParam(o);
    }

    public void setWorker(IThreadedClass cl) {
        wrk = cl;
    }

    public boolean isFree() {
        return free;
    }

    public synchronized void wake() {
        free = false;
        notify();
    }

    private synchronized void goSleep() {
        free = true;
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}