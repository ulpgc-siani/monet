package org.monet.docservice;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.monet.docservice.core.helper.VersionHelper;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.docprocessor.configuration.Configuration;
import org.monet.docservice.docprocessor.data.impl.DatabaseRepository;
import org.monet.docservice.docprocessor.worker.DocumentPreviewsCleaner;
import org.monet.docservice.docprocessor.worker.WorkQueueManager;
import org.monet.docservice.guice.InjectorFactory;
import org.monet.docservice.upgrades.UpgradeManager;

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
}
