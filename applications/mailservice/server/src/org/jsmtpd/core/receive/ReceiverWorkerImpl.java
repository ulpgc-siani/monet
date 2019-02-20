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

import java.io.IOException;
import java.net.Socket;

import org.jsmtpd.generic.threadpool.IThreadedClass;

/**
 * Implements the threaded interface of generic pool
 * holds instance of the smtp protocol receiving handler
 * @author Jean-Francois POUX
 */
public class ReceiverWorkerImpl implements IThreadedClass {
    private ProtocolHandler proto = new ProtocolHandler();
    private Socket rec = null;

    public void doJob() {
    	if (rec.getClass().getName().toLowerCase().contains("ssl"))
    		proto.init(rec,true);
        proto.init(rec,false);
    }

    public void forceShutdown() {
        try {
            if (rec != null)
                rec.close();
        } catch (IOException e) {

        }
    }

    public void gracefullShutdown() {
        try {
            if (rec != null)
                rec.close();
        } catch (IOException e) {

        }
    }

    public void setParam(Object o) {
        rec = (Socket) o;
    }

}