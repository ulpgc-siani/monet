/*
 * 
 * Jsmtpd, Java SMTP daemon
 * Copyright (C) 2006  Jean-Francois POUX, jf.poux@laposte.net
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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This needs testing.
 * 
 * 
 * A growable thread pool.
 * It's like the generic thread pool, but :
 * Starts at 10% of initial threads provided (or whatever you set with growthRatio)
 * When the pool is exhausted and a new thread is required, it will start 
 * numThreads*growRatio new threads, and place them in the pool, if the pool 
 * has still lower than numThreads
 * 
 * Each 5000 ms, (or whatever you set), a "control thread" will look for
 * unused threads and decrease the size of the pool as needed. There must have been no 
 * creation since 4* cycleTime.
 * 
 * @author Jean-Francois POUX
 * @see org.jsmtpd.generic.threadpool.IThreadedClass
 */
public class GrowingThreadPool extends Thread implements ThreadPool {
    private Log log = LogFactory.getLog(GrowingThreadPool.class);
    private List<ThreadWorker> threads = new LinkedList<ThreadWorker>();
    private int maxSize;
    private int minimumThreads;
    private int growth;
    private String displayThreadName;
    private String threadClassName;
    private boolean running=true;
    private long lastUp;
    private float growthRatio=0.2f;
    private float minRatio=0.1f;
    private int cycleTime=5000;
    
    /**
     * 
     * @param numThreads number of threads to be spawned
     * @param threadClassName name of the class to be threaded, must impletement IThreadedClass
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     */
    public GrowingThreadPool(int numThreads, String threadClassName,String displayThreadName) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        ThreadWorker tmp;
        IThreadedClass cls;
        
        this.displayThreadName=displayThreadName;
        this.threadClassName=threadClassName;
        maxSize=numThreads;
        // Start at ratio of max size
        minimumThreads=(int)Math.round(numThreads*minRatio); 
        if (minimumThreads==0)
            minimumThreads=1;
        
        // Grow or lower by growthRatio
        growth = (int) Math.round(numThreads*growthRatio);
        if (growth==0)
            growth=1;
        
        log.info("Starting initial pool of "+minimumThreads+" threads");
        for (int i = 0; i < minimumThreads; i++) {
            tmp = new ThreadWorker();
            cls = (IThreadedClass) Class.forName(threadClassName).newInstance();           
            tmp.setWorker(cls);
            tmp.setName(displayThreadName+"-"+tmp.getId());
            tmp.start();
            while (!tmp.isFree()) {
                //wait for thread to be up if not
                Thread.yield();
            }
            log.debug("Thread "+tmp.getName()+" started");
            threads.add(tmp);
        }
        lastUp=System.currentTimeMillis();
        start();
        
    }
    
    /**
     * Will gracefully shutdown each running thread
     *
     */
    public synchronized void gracefullShutdown() {
        running=false;
        this.interrupt();
        log.debug("Gracefull shutdown ...");
        ThreadWorker tmp;
        for (int i = 0; i < threads.size(); i++) {
            tmp = (ThreadWorker) threads.get(i);
            tmp.gracefullShutdown();
        }
    }

    /**
     * Will force each thread to shutdown
     *
     */
    public synchronized void forceShutdown() {
        running=false;
        this.interrupt();
        log.debug("Forcing shutdown ...");
        ThreadWorker tmp;
        for (int i = 0; i < threads.size(); i++) {
            tmp = (ThreadWorker) threads.get(i);
            tmp.forceShutdown();
        }
    }

    /**
     * 
     * @return true if any free thread
     */
    public synchronized boolean hasFreeThread() {
    	for (ThreadWorker element : threads) {
            if (element.isFree())
                return true;
        }
        if ((threads.size()+growth)<=maxSize)
            return true;
        return false;
    }

    /**
     * 
     */
    public synchronized int countFreeThread() {
        int count = 0;
        for (ThreadWorker element : threads) {
            if (element.isFree())
                count++;
        }
        return count;
    }

    /**
     * passes the obj parameter to the thread instance, and runs its doJob mehtod
     * @param obj the object to pass
     * @throws BusyThreadPoolException when the pool is exhausted
     */
    public synchronized void assignFreeThread(Object obj) throws BusyThreadPoolException {
        if (countFreeThread()==0) {
            growPool ();
        }
//        int i = 0;
        for (ThreadWorker worker : threads) {
            if (worker.isFree()) {
                log.debug("Worker "+worker.getName()+" is free, assigning job");
                worker.setParam(obj);
                worker.wake();
                return;
            }
//            i++;
        }
        log.warn("Thread pool exhausted !");
        throw new BusyThreadPoolException();
    }
    
    public synchronized void growPool () {
        log.debug("Trying to grow thread pool");
        ThreadWorker tmp;
        IThreadedClass cls;
      
        if ((threads.size()+growth)<=maxSize) {
            try {
                lastUp=System.currentTimeMillis();
                log.info("Increasing number of threads by "+growth+", starting from "+threads.size());
                for (int i=0;i<growth;i++) {
                    tmp = new ThreadWorker();
                    cls = (IThreadedClass) Class.forName(threadClassName).newInstance();
                    tmp.setWorker(cls);
                    tmp.setName(displayThreadName+"#"+tmp.getId());
                    tmp.start();
                    while (!tmp.isFree()) {
                        //wait for thread to be up
                        Thread.yield();
                    }
                    threads.add(tmp);
                    log.debug("Thread "+tmp.getName()+" started");
                }
            } catch (Exception e) {
                log.error("Could not increase pool",e);
            }
        } else {
            log.error("Thread pool too low to assign a thread");
        }
    }
    
    public synchronized void lowerPool() {
        long diff = System.currentTimeMillis()-lastUp;
        if ((diff>(cycleTime*4)) && (threads.size()>minimumThreads) && (countFreeThread()>growth)) {
            // remove growth size
            Set<ThreadWorker> toRemove=new HashSet<ThreadWorker>();
            for (int i=threads.size()-1;i>0;i--) { // loop backward to avoid destroy first threads
                ThreadWorker worker = threads.get(i);
                if (worker.isFree())
                    toRemove.add(worker);
                if (toRemove.size()==growth)
                    break;
            }
            log.info("Reducing number of threads by "+growth+", starting from "+threads.size());
            for (ThreadWorker worker : toRemove) {
                log.debug("Shutting down thread "+worker.getName());
                worker.gracefullShutdown();
                threads.remove(worker);
            }
        }
    }
    
    public void run () {
        setName("C-"+getId());
        log.info("Control thread started");
        while (running) {
            try {
                Thread.sleep(cycleTime);
            } catch (InterruptedException e) {
            }
            log.debug("Current size "+threads.size()+"/"+maxSize+", "+countFreeThread()+" free threads");
            lowerPool();
        }
        log.info("Control thread ended");
    }

    /**
     * Time in ms of wait time of control thread
     */
    public void setCycleTime(int cycleTime) {
        this.cycleTime = cycleTime;
    }

    /**
     * Ratio between 0.1 and 0.9 of grow rate of the pool
     */
    public void setGrowthRatio(float growthRatio) {
        this.growthRatio = growthRatio;
    }

    /**
     * Minimal ratio of pool (between 0.1 and 1)
     */
    public void setMinRatio(float minRatio) {
        this.minRatio = minRatio;
    }
    
}