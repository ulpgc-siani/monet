package org.monet.docservice;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.monet.docservice.core.helper.VersionHelper;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.core.log.impl.DatabaseAppender;
import org.monet.docservice.docprocessor.configuration.Configuration;
import org.monet.docservice.docprocessor.data.impl.DatabaseRepository;
import org.monet.docservice.docprocessor.worker.DocumentPreviewsCleaner;
import org.monet.docservice.docprocessor.worker.WorkQueueManager;
import org.monet.docservice.guice.InjectorFactory;
import org.monet.docservice.upgrades.UpgradeManager;

import java.lang.ref.PhantomReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

@Singleton
public class Application {
	private final Logger logger;
	private String name = null;
	private boolean running = false;
	private DatabaseRepository repository;
	private Configuration configuration;

	@Inject
	public Application(Logger logger) {
		this.logger = logger;
	}

	@Inject
	public void injectDatabaseRepository(DatabaseRepository repository) {
		this.repository = repository;
	}

	@Inject
	public void injectConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public void run() {
		if (running)
			return;

		Injector injector = InjectorFactory.get();

		checkConfiguration();
		upgrade(injector);
		initWorkQueueManager(injector);
		initDocumentPreviewCleaner(injector);

		running = true;
		logger.info(String.format("%s running!", this.getName()));
	}

	public void stop() {
		Thread[] threads = getThreads();

		String[] guicePrefixes = {
				"DatabaseLogAppender",
				"WorkQueueManager",
				"com.google.inject.internal.",          // google guice 2.0
				"com.google.inject.internal.util.$"     // google guice 3.0

		};

		DatabaseAppender.destroy();

		for (Thread thread : threads) {
			for (String guicePrefix : guicePrefixes) {
				if (thread == null) continue;

				// Try to shutdown the Finalizer Thread
				try {
					if (thread.getName().equals("WorkQueueManager")) {
						WorkQueueManager workQueue = InjectorFactory.get().getInstance(WorkQueueManager.class);
						workQueue.destroy();
					}


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
				if (driver != null)
					System.out.println(String.format("Error deregistering driver %s   ", driver) + e.getMessage());
			}
		}
	}

	private void checkConfiguration() {
		configuration.check();
	}

	private void upgrade(Injector injector) {
		UpgradeManager upgradeManager = injector.getInstance(UpgradeManager.class);
		upgradeManager.upgrade();
	}

	private static void initWorkQueueManager(Injector injector) {
		WorkQueueManager manager = injector.getInstance(WorkQueueManager.class);
		manager.init();
	}

	private void initDocumentPreviewCleaner(Injector injector) {
		DocumentPreviewsCleaner cleaner = injector.getInstance(DocumentPreviewsCleaner.class);
		cleaner.init();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return "3.2.2";
	}

	public String getDatabaseVersion() {
		String version = this.repository.getVersion();

		if (version == null)
			version = VersionHelper.getMajorVersion(this.getVersion()) + ".0";

		return version;
	}

	public void updateDatabaseVersion(String version) {
		this.repository.updateVersion(version);
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
