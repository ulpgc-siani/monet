/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2009  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
*/

package org.monet.space.kernel.listeners;

import com.codeforces.log4j.databaseappender.DatabaseAppender;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.gisc.libs.io.watcherservice.event.FileSystemEvent;
import org.gisc.libs.io.watcherservice.event.FileSystemEventType;
import org.gisc.libs.io.watcherservice.event.IFileSystemEventListener;
import org.gisc.libs.io.watcherservice.watcher.IPoolingFileSystemWatcher;
import org.gisc.libs.io.watcherservice.watcher.PoolingFileSystemWatcher;
import org.monet.space.kernel.Kernel;
import org.monet.space.kernel.agents.AgentSession;
import org.monet.space.kernel.agents.AgentWorkQueue;
import org.monet.space.kernel.components.*;
import org.monet.space.kernel.configuration.ConfigurationLoader;
import org.monet.space.kernel.configuration.ConfigurationMap;
import org.monet.space.kernel.model.Context;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.File;
import java.lang.ref.PhantomReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.Security;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

public final class ListenerContext implements ServletContextListener {
	private IPoolingFileSystemWatcher watcher;
	private IFileSystemEventListener listener;

	public ListenerContext() {
	}

	private void initializeFilesystemWatcher() {
		File oFile = new File(ConfigurationLoader.userHomeConfigurationFile());

		this.listener = new IFileSystemEventListener() {
			public void onChange(FileSystemEvent event) {
				ConfigurationLoader.load(ConfigurationMap.fromUserHome());
			}
		};

		this.watcher = new PoolingFileSystemWatcher();
		this.watcher.setDelay(1000);
		this.watcher.setPeriod(1000 * 15 * 1);
		this.watcher.watchFile(oFile, FileSystemEventType.FILE_MODIFIED);
		this.watcher.addIFileSystemEventListener(this.listener);
		this.watcher.run();

		Security.addProvider(new BouncyCastleProvider());
	}

	public void contextInitialized(ServletContextEvent oEvent) {
		Context oContext = Context.getInstance();
		oContext.setFrameworkName(oEvent.getServletContext().getServletContextName());
		oContext.setFrameworkDir(oEvent.getServletContext().getRealPath("/"));
		this.initializeFilesystemWatcher();
		Kernel.getInstance().run(ConfigurationMap.fromUserHome());
	}

	@SuppressWarnings("rawtypes")
	public void contextDestroyed(ServletContextEvent event) {

        if (ComponentDatawareHouse.started())
    		ComponentDatawareHouse.getInstance().destroy();

        if (ComponentDocuments.started())
		    ComponentDocuments.getInstance().destroy();

        if (ComponentFederation.started())
		    ComponentFederation.getInstance().destroy();

        if (ComponentPersistence.started())
		    ComponentPersistence.getInstance().destroy();

        if (ComponentSecurity.started())
		    ComponentSecurity.getInstance().destroy();

        if (AgentSession.started())
		    AgentSession.getInstance().clear();

        if (this.watcher != null) {
            this.watcher.removeIFileSystemEventListener(this.listener);
            this.watcher.stop();
        }

		DatabaseAppender.destroy();

        if (AgentWorkQueue.started())
		    AgentWorkQueue.getInstance().destroy();

        Thread[] threads = getThreads();
        String[] guicePrefixes = {
                "com.google.inject.internal.",          // google guice 2.0
                "com.google.inject.internal.util.$"     // google guice 3.0
        };
		for (String guicePrefix : guicePrefixes) {
			for (Thread thread : threads) {
				if (thread == null) continue;

				// Try to shutdown the Finalizer Thread
				try {
					if (thread.getClass().getName().equals(guicePrefix + "Finalizer")) {
						Class mapMakerClass = Class.forName(guicePrefix + "MapMaker");
						Class[] classes = mapMakerClass.getDeclaredClasses();
						for (Class clazz : classes) {
							if (clazz.getName().equals(guicePrefix + "MapMaker$QueueHolder")) {
								Object finalizableReferenceQueue = getFieldInstance(null, clazz, "queue");
								Object referenceQueue = getFieldInstance(finalizableReferenceQueue, finalizableReferenceQueue.getClass(), "queue");
								Object finalizerQueue = getFieldInstance(thread, thread.getClass(), "queue");

								// Check that the thread is our instance
								if (referenceQueue == finalizerQueue) {
									PhantomReference frqReference = (PhantomReference) getFieldInstance(thread, thread.getClass(), "frqReference");

									// Notify the finalizer it can stop
									Method enqueue = finalizerQueue.getClass().getDeclaredMethod("enqueue", new Class[]{java.lang.ref.Reference.class});
									enqueue.setAccessible(true);
									enqueue.invoke(finalizerQueue, new Object[]{frqReference});
								}
							}
						}
					}
				} catch (Exception e) {
				}
			}
		}

		// This manually deregisters JDBC driver, which prevents Tomcat 7 from complaining about memory leaks wrto this class
		Enumeration<Driver> drivers = DriverManager.getDrivers();
		while (drivers.hasMoreElements()) {
			Driver driver = drivers.nextElement();
			try {
				DriverManager.deregisterDriver(driver);
				System.out.println(String.format("deregistering jdbc driver: %s", driver));
			} catch (Exception e) {
				System.out.println(String.format("Error deregistering driver %s   ", driver) + e.getMessage());
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private Object getFieldInstance(Object instance, Class clazz, String name) {
		try {
			Field field = clazz.getDeclaredField(name);
			field.setAccessible(true);
			return field.get(instance);
		} catch (Exception e) {
			return null;
		}
	}

	/* Code from apache tomcat 6.0.32 */
	private Thread[] getThreads() {
		// Get the current thread group
		ThreadGroup tg = Thread.currentThread().getThreadGroup();
		// Find the root thread group
		while (tg.getParent() != null) {
			tg = tg.getParent();
		}

		int threadCountGuess = tg.activeCount() + 50;
		Thread[] threads = new Thread[threadCountGuess];
		int threadCountActual = tg.enumerate(threads);
		// Make sure we don't miss any threads
		while (threadCountActual == threadCountGuess) {
			threadCountGuess *= 2;
			threads = new Thread[threadCountGuess];
			// Note tg.enumerate(Thread[]) silently ignores any threads that
			// can't fit into the array
			threadCountActual = tg.enumerate(threads);
		}

		return threads;
	}

}