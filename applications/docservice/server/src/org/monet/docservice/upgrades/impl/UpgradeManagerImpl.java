package org.monet.docservice.upgrades.impl;

import com.google.inject.Inject;
import org.monet.docservice.Application;
import org.monet.docservice.core.agent.AgentFilesystem;
import org.monet.docservice.core.library.LibraryFile;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.docprocessor.configuration.Configuration;
import org.monet.docservice.docprocessor.data.DataSourceProvider;
import org.monet.docservice.docprocessor.data.DiskManager;
import org.monet.docservice.upgrades.UpgradeManager;
import org.monet.docservice.upgrades.UpgradeScriptClassLoader;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;

public class UpgradeManagerImpl implements UpgradeManager {
	private Application application;
	private DataSource dataSource;
	private Logger logger;
	private AgentFilesystem agentFilesystem;
	private Configuration configuration;
	private LibraryFile libraryFile;
	private DiskManager diskManager;

	private static final String QUERY_ESCAPED_SEMICOLON = "::SEMICOLON::";

	@Inject
	public void injectApplication(Application application) throws NamingException {
		this.application = application;
	}

	@Inject
	public void injectDataSourceProvider(DataSourceProvider dataSourceProvider) throws NamingException {
		this.dataSource = dataSourceProvider.get();
	}

	@Inject
	public void injectLogger(Logger logger) {
		this.logger = logger;
	}

	@Inject
	public void injectAgentFilesystem(AgentFilesystem agentFilesystem) {
		this.agentFilesystem = agentFilesystem;
	}

	@Inject
	public void injectConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	@Inject
	public void injectLibraryFile(LibraryFile libraryFile) {
		this.libraryFile = libraryFile;
	}

	@Inject
	public void injectDiskManager(DiskManager diskManager) {
		this.diskManager = diskManager;
	}

	@Override
	public void upgrade() {
		String appVersion = application.getVersion();
		String databaseVersion = application.getDatabaseVersion();

		logger.info(String.format("Checking if exists upgrades to launch for %s...", application.getName()));

		if (appVersion.equals(databaseVersion)) {
			logger.info("Versions OK!");
			return;
		}

		logger.info(String.format("%s VERSION (%s) IS DISTINCT FROM DATABASE VERSION (%s)! Finding for scripts to upgrade...", application.getName(), appVersion, databaseVersion));
		executeUpgradeScripts(databaseVersion, appVersion);
		logger.info("UPGRADE FINISHED!");
	}

	private boolean executeUpgradeScripts(String databaseVersion, String appVersion) {
		String upgradeDir = configuration.getUpgradesDir();
		String[] files = agentFilesystem.listDir(upgradeDir);
		Connection connection = null;

		Arrays.sort(files, new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				if (o1.length() < o2.length()) return -1;
				if (o1.length() > o2.length()) return 1;
				return o1.compareTo(o2);
			}
		});

		try {

			UpgradeScriptClassLoader classLoader = new UpgradeScriptClassLoader(configuration, logger);
			connection = this.dataSource.getConnection();
			boolean autoCommit = connection.getAutoCommit();
			connection.setAutoCommit(false);

			for (int i=0; i<files.length; i++) {
				String scriptFilename = files[i];
				String scriptVersion = libraryFile.getFilename(scriptFilename);

				Class<?> clazz = Class.forName("upgrades." + scriptVersion + ".Main", true, classLoader);
				Constructor constructor = clazz.getConstructor(Application.class, Logger.class, Configuration.class, DiskManager.class);
				UpgradeScript script = (UpgradeScript) constructor.newInstance(application, logger, configuration, diskManager);

				if (!script.canExecute(appVersion, databaseVersion))
					continue;

				if (!script.execute(connection))
					return false;
			}

			if (autoCommit) {
				connection.commit();
				connection.setAutoCommit(true);
			}

		} catch (SQLException exception) {
			logger.error("Error getting database autoCommit variable", exception);
			return false;
		} catch (ClassNotFoundException exception) {
			logger.error("Error getting database autoCommit variable", exception);
			return false;
		} catch (InvocationTargetException exception) {
			logger.error("Could not load script class", exception);
			return false;
		} catch (InstantiationException exception) {
			logger.error("Could not load script class", exception);
			return false;
		} catch (IllegalAccessException exception) {
			logger.error("Could not load script class", exception);
			return false;
		} catch (NoSuchMethodException exception) {
			logger.error("Could not load script class", exception);
			return false;
		} catch (Exception exception) {
			logger.error("Could not load script class", exception);
			return false;
		} finally {
			try {
				if (connection != null)
					connection.close();
			} catch (SQLException e) {
			}
		}

		return true;
	}

}
