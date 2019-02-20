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

/**
 * Any classes implementing this interface can be pooleed by GenericThreadPool
 * @see org.jsmtpd.generic.threadpool.GenericThreadPool
 * @see org.jsmtpd.generic.threadpool.DummyThread implementation exemple
 * @author Jean-Francois POUX
 */
public interface IThreadedClass {

    /**
     * the threaded method. will be invoked by the pool when the thread is to be runned
     *
     */
    public void doJob();

    /**
     * invoking this method should terminate immediatly doJob();
     *
     */
    public void forceShutdown();

    /**
     * invoking this method should gracefully terminate doJob();
     *
     */
    public void gracefullShutdown();

    /**
     * A parameter passed by the poll upon thread assignation
     * @param o the object to assign
     */
    public void setParam(Object o);
}