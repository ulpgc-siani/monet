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
 * A simple test of implementation
 * @author Jean-Francois POUX
 */
public class DummyThread implements IThreadedClass {

    private String prm = "nada";

    public void doJob() {
        System.out.println("DummyTh working " + Thread.currentThread().getId() + " with param " + prm);

        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
        //int cpt = 0;

//        try {
//            while (true) {
//                if (false)
//                    break;
//
//                cpt++;
//                System.out.println(cpt);
//            }
//        } catch (Exception ie) {
//            ie.printStackTrace();
//        }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        System.out.println("DummyTh end" + Thread.currentThread().getId());
    }

    /* (non-Javadoc)
     * @see smtpd.common.ThreadPool.ThreadedClass#forceShutdown()
     */
    public void forceShutdown() {

    }

    /* (non-Javadoc)
     * @see smtpd.common.ThreadPool.ThreadedClass#gracefullShutdown()
     */
    public void gracefullShutdown() {
        
    }

    /* (non-Javadoc)
     * @see smtpd.common.ThreadPool.ThreadedClass#setParam(java.lang.Object)
     */
    public void setParam(Object o) {
        prm = (String) o;

    }

}