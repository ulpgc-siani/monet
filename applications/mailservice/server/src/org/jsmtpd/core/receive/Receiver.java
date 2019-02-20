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
package org.jsmtpd.core.receive;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsmtpd.generic.threadpool.BusyThreadPoolException;
import org.jsmtpd.generic.threadpool.GrowingThreadPool;
import org.jsmtpd.generic.threadpool.ThreadPool;

/**
 * Listening to a given port<br>
 * When a connection occurs, picks a waiting thread from ThreadPool
 * to handle dialog via an instance of ProtoclHandler
 * @author Jean-Francois POUX
 */
public class Receiver extends Thread {

    protected ServerSocket sock = null;
    protected ThreadPool p = null;
    public boolean runing = true;
    private Log log = LogFactory.getLog(Receiver.class);
    protected Socket inc = null;

    public Receiver () {}
    
    public Receiver(int port, int maxInst) throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        p = new GrowingThreadPool(maxInst, "org.jsmtpd.core.receive.ReceiverWorkerImpl","R");
        sock = new ServerSocket(port);
        log.info("Listening for connections on port " + port);
        this.start();
    }

    public void run() {
        try {
            while ((inc = sock.accept()) != null) {
                try {
                    p.assignFreeThread(inc);
                } catch (BusyThreadPoolException e1) {
                    handleBusy(inc);
                }
            }
            sock.close();
        } catch (IOException e) {
        }
        log.info("Server is no longer listening for incoming connections");
    }

    private void handleBusy(Socket inc) {
        String remote = ((InetSocketAddress) inc.getRemoteSocketAddress()).getAddress().getHostAddress();
        BufferedWriter wr = null;
        try {
            log.error("Unable to serve " + remote + ", replied 421 Service unavaible due to heavy load");
            wr = new BufferedWriter(new OutputStreamWriter(inc.getOutputStream()));
            wr.write("421 Service unavaible due to heavy load");
            wr.flush();

        } catch (IOException e) {
            log.error(e);
        }
        try {
            wr.close();
        } catch (IOException e1) {
            log.error(e1);
        }
    }

    public void shutdown() {
        try {
            sock.close();
        } catch (IOException e) {
        }
        p.forceShutdown();
    }
}