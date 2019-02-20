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
 * @author Jean-Francois POUX
 * Jsmtpd
 */
public class ThreadPoolTest {

    public static void main(String[] args) throws InterruptedException {
        ThreadPool pool = null;
        try {
            pool = new GenericThreadPool(20, "org.jsmtpd.generic.threadpool.DummyThread","Dummy");
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {

            e.printStackTrace();
        }

//        for (int i=0;i<20;i++) {
//            try {
//                pool.assignFreeThread("Test !");
//            } catch (BusyThreadPoolException e) {
//                e.printStackTrace();
//            }
//        }
//        Thread.sleep(30000);
        int cp=0;
        while (cp<100) {
            for (int i=0;i<20;i++) {
                try {
                    pool.assignFreeThread("Test !");
                } catch (BusyThreadPoolException e) {
                }
            }
            Thread.sleep(3000);
            cp++;
        }
        Thread.sleep(30000);
        /*
        try {
            System.out.println("Assigning 1, pool can handle " + pool.countFreeThread());
            pool.assignFreeThread("Test !");
            System.out.println("Assigned 1");
        } catch (BusyThreadPoolException e2) {

            e2.printStackTrace();
        }
        */
        /*
         System.out.println("main Go sleep");
         try {
         Thread.sleep(300);
         } catch (InterruptedException e1) {
         e1.printStackTrace();
         }
         System.out.println("main wake");*/
        pool.forceShutdown();
        System.err.println("pool down");
        /*
         try {
         System.out.println("Assigning 2, pool can handle "+pool.countFreeThread());
         pool.assignFreeThread("Test2 !");
         System.out.println("Assigned 2");
         } catch (BusyThreadPoolException e2) {
         e2.printStackTrace();
         }
         */
        System.out.println("main Go sleep");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        System.out.println("main wake");
        pool.forceShutdown();
        System.out.println("pool clean");

    }
}