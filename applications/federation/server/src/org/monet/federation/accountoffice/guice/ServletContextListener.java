package org.monet.federation.accountoffice.guice;

import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import org.gisc.libs.io.watcherservice.event.FileSystemEvent;
import org.gisc.libs.io.watcherservice.event.FileSystemEventType;
import org.gisc.libs.io.watcherservice.event.IFileSystemEventListener;
import org.gisc.libs.io.watcherservice.watcher.IPoolingFileSystemWatcher;
import org.gisc.libs.io.watcherservice.watcher.PoolingFileSystemWatcher;
import org.monet.federation.accountoffice.control.sockets.SocketListener;
import org.monet.federation.accountoffice.core.agents.AgentSession;
import org.monet.federation.accountoffice.core.configuration.Configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.io.File;
import java.lang.ref.PhantomReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

public class ServletContextListener extends GuiceServletContextListener {
    private IPoolingFileSystemWatcher watcher;
    private IFileSystemEventListener listener;
    private ServletContext servletContext;

    @Override
    protected Injector getInjector() {
        return InjectorFactory.get(this.servletContext);
    }

    private void initializeFilesystemWatcher() {
        Injector injector = InjectorFactory.get();
        Configuration configuration = injector.getInstance(Configuration.class);
        File file = new File(configuration.getConfigurationFile());

        this.listener = new IFileSystemEventListener() {
            public void onChange(FileSystemEvent event) {
                Injector injector = InjectorFactory.get();
                Configuration configuration = injector.getInstance(Configuration.class);
                configuration.reload();
            }
        };

        this.watcher = new PoolingFileSystemWatcher();
        this.watcher.setDelay(1000);
        this.watcher.setPeriod(1000 * 15 * 1);
        this.watcher.watchFile(file, FileSystemEventType.FILE_MODIFIED);
        this.watcher.addIFileSystemEventListener(this.listener);
        this.watcher.run();
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        this.servletContext = servletContextEvent.getServletContext();
        super.contextInitialized(servletContextEvent);

        this.initializeFilesystemWatcher();

        AgentSession agentSession = this.getInjector().getInstance(AgentSession.class);
        agentSession.init();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        super.contextDestroyed(servletContextEvent);
        SocketListener sckL = InjectorFactory.get().getInstance(SocketListener.class);
        sckL.close();

        this.watcher.removeIFileSystemEventListener(this.listener);
        this.watcher.stop();

        AgentSession agentSession = this.getInjector().getInstance(AgentSession.class);
        agentSession.stop();

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
