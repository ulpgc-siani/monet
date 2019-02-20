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

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsmtpd.config.ReadConfig;
import org.jsmtpd.core.mail.Email;

/**
 * queueMail holds a number of pending mails loaded (for perfs reasons)
 * others are stored on disk<br>
 * 
 * when a mail has to be requeued, it is directly writen to disk
 * 
 * @author Jean-Francois POUX
 */
public class QueueService {

    /**
     * mail here will be kept loaded
     */
    private List<Email> directService = Collections.synchronizedList(new LinkedList<Email>());
    private static QueueService instance = null;
    private String tempPath;
    private File retryDir;
    private File pendingDir;
    private Log log = LogFactory.getLog(QueueService.class);
    private boolean running = true;
    private long retryDelay;
    private long currentDiskSize = 0;
    private long maxDiskSize = 0;
    // Optim

    private int fastServ;

    private boolean safeMode = false;
    private String safeModePath = "";
    private FileFilter filter = new EmailFileFilter();

    public static synchronized QueueService getInstance() {
        if (instance == null)
            instance = new QueueService();
        return instance;
    }

    /**
     * Adds a mail on the queue, in ram if the server can afford it, or written on disk
     * @param input the mail to queue for delivery
     * @return false if it can't be queued
     */
    public synchronized boolean queueMail(Email input) {
        if (running == false)
            return false;
        log.debug("Queuing new mail "+input.getDiskName()+", Disk usage "+getStorageStats());
        // if safe mode, write down the mail to specified location.
        if (safeMode) {
            try {
                Email.save(safeModePath + "/" + input.getDiskName(), input);
                log.info("SafeMode on : written mail to " + safeModePath + "/" + input.getDiskName());
            } catch (IOException e) {
                log.error("SafeMode on : can't write mail to " + safeModePath + "/" + input.getDiskName(), e);
            }
        }
        // We keep big mails on disk, whereas small are kept in ram
        if ((directService.size() < 20) && (input.getSize() < 512000)) {
            directService.add(input);
            return true;
        } else {
            if ((directService.size() < 5) && (input.getSize() > 512000)) { // If we have a low number of mail on queue, keep big ones anyway
                directService.add(input);
                return true;
            } else {
                if ((currentDiskSize + input.getSize()) > maxDiskSize) {
                    log.warn("Can't store anymore incomming mails, storage size exceeded");
                    return false;
                }
                try {
                    Email.save(tempPath + "mqueue/pending/" + input.getDiskName(), input);
                    File tmp = new File(tempPath + "mqueue/pending/" + input.getDiskName());
                    currentDiskSize += tmp.length();
                    return true;
                } catch (IOException e) {
                }
            }

            return false;
        }
    }

    /**
     * If a mail temporary fails, this method will store it for later delivery
     * @param input the mail to requeue
     * @return false if can't be queued
     */
    public synchronized boolean requeueMail(Email input) {
        if (running == false)
            return false;
        log.debug("DSVC> Re-queuing new mail "+input.getDiskName()+", Disk usage "+getStorageStats());
        try {
        	input.increaseAttempts();
            Email.save(tempPath + "mqueue/retry/" + input.getDiskName(), input);
            File tmp = new File(tempPath + "mqueue/retry/" + input.getDiskName());
            currentDiskSize += tmp.length();
            return true;
        } catch (IOException e) {

        }
        return false;
    }

    /**
     * Gets a mail for delivering<br>
     * Mail is picked on retried mails, then from the RAM linked list, and from the pending queue on disk
     * @return the email instance picked from the queues
     */
    public synchronized Email getEmail() {
        /**
         * pickRetry();
         * pickDirect();
         * pickPending();
         */
        if (running == false)
            return null;
        Email tmp = null;
        tmp = pickRetry();
        if (tmp != null)
            return tmp;

        if (directService.size() > 0) {
            tmp = (Email) directService.remove(0);
            return tmp;
        }
        tmp = pickPending();
        if (tmp != null)
            return tmp;

        return null;
    }

    /**
     * picks a mail in the pending mqueue directory
     * @return instance of a mail, or null if queue empty
     */
    private Email pickPending() {

        File[] pendingMails = pendingDir.listFiles(filter);

        if ((pendingMails != null) && pendingMails.length != 0) {
            try {
                Email ret = Email.load(pendingMails[0].toString());
                currentDiskSize -= pendingMails[0].length();
                pendingMails[0].delete();
                return ret;
            } catch (IOException e) {
                File tmp = new File(tempPath + "/" + pendingMails[0].getName() + "-Bogus");
                pendingMails[0].renameTo(tmp);
                log.error("Cant load mail " + pendingMails[0].toString() + ", error: " + e);
                log.error("Mail moved to bogus");
            }
        }
        return null;
    }

    /**
     * pick a mail in the retry mqueue folder
     * @return the instance or null if queue is empty
     */
    private Email pickRetry() {
        File[] pendingMails = retryDir.listFiles(filter);

        if (pendingMails != null) {
            for (int i = 0; i < pendingMails.length; i++) {
                long timeOffset = pendingMails[i].lastModified() + retryDelay;
                if (timeOffset < System.currentTimeMillis()) {
                    try {
                        Email ret = Email.load(pendingMails[0].toString());
                        currentDiskSize -= pendingMails[0].length();
                        pendingMails[0].delete();
                        return ret;
                    } catch (IOException e) {
                        File tmp = new File(tempPath + "/" + pendingMails[0].getName() + "-Bogus");
                        pendingMails[0].renameTo(tmp);
                        log.error("Cant load mail " + pendingMails[0].toString() + ", error: " + e);
                        log.error("Mail moved to bogus");
                    }
                }
            }

        }
        return null;
    }

    private QueueService() {
        ReadConfig readConfig = ReadConfig.getInstance();
        tempPath = readConfig.getProperty("temporaryFolder");
        retryDelay = readConfig.getIntegerProperty("dDelayRetry") * 60 * 1000;
        retryDir = new File(tempPath + "/mqueue/retry");
        pendingDir = new File(tempPath + "/mqueue/pending");
        fastServ = readConfig.getIntegerProperty("dMaxInstances") * 2;
        log.debug("Buffer set to " + fastServ + " messages max for immediate processing");
        maxDiskSize = readConfig.getIntegerProperty("maxTemporarySize") * 1048576;
        initDiskCount(new File(tempPath));
        log.debug("Storage usage "+getStorageStats());
        safeMode = readConfig.getBooleanProperty("safeMode");
        if (safeMode)
            log.info("Safe Mode on");
        safeModePath = readConfig.getProperty("safeModeFolder");
    }
    private String getStorageStats () {
    	String out = Math.round((float) currentDiskSize / 1048576) + "/"
        + Math.round((float) maxDiskSize / 1048576) + " (in Mo)";
    	return out;
    }
    private void initDiskCount(File input) {
        if (input.isFile())
            currentDiskSize += input.length();
        else {
            File[] sub = input.listFiles();
            for (int i = 0; i < sub.length; i++) {
                File file = sub[i];
                initDiskCount(file);
            }
        }
    }

    public void shutdownService() {
        running = false;
        for (Email element : directService) {
            try {
                Email.save(tempPath + "mqueue/retry/" + element.getDiskName(), element);
                log.debug("Shutdown : " + element.getDiskName() + " written to retry");
            } catch (IOException e) {
                log.debug("Cant save mail on shutdown, mail " + element.getDiskName() + " is lost due to: " + e.getCause());
            }
        }
    }

}