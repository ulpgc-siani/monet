package org.monet.docservice.upgrades.impl;

import org.monet.docservice.Application;
import org.monet.docservice.core.helper.VersionHelper;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.core.sql.BufferedQuery;
import org.monet.docservice.docprocessor.configuration.Configuration;
import org.monet.docservice.docprocessor.data.DiskManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class UpgradeScript {
	private String version;
	protected final Application application;
	protected final Logger logger;
	protected final Configuration configuration;
	protected final DiskManager diskManager;

	private static final String QUERY_ESCAPED_SEMICOLON = "::SEMICOLON::";

	public UpgradeScript(String version, Application application, Logger logger, Configuration configuration, DiskManager diskManager) {
		this.version = version;
		this.application = application;
		this.logger = logger;
		this.configuration = configuration;
		this.diskManager = diskManager;
	}

	public boolean canExecute(String appVersion, String databaseVersion) {
		int versionNumber = VersionHelper.versionToNumber(version);
		int appVersionNumber = VersionHelper.versionToNumber(appVersion);
		int databaseVersionNumber = VersionHelper.versionToNumber(databaseVersion);

		if (versionNumber > appVersionNumber)
			return false;

		if (versionNumber <= databaseVersionNumber)
			return false;

		return true;
	}

	public boolean execute(Connection connection) {

		logger.info(String.format("Script %s found. Executing BEFORE upgrade database script...", version));

		if (!executeBeforeUpgradeDatabase(connection)) {
			logger.error("BEFORE Execution failed. Check logs!");
			return false;
		}

		logger.info(String.format("Before upgrade database script executed. Upgrading database...", version));

		if (!upgradeDatabase(connection)) {
			logger.error("Database upgrade failed. Check logs!");
			return false;
		}

		logger.info(String.format("Database upgraded. Executing AFTER upgrade database script...", version));
		if (!executeAfterUpgradeDatabase(connection)) {
			logger.error("AFTER Execution failed. Check logs!");
			return false;
		}

		logger.info(String.format("Script %s executed successfully!", version));

		return true;
	}

	protected abstract boolean executeBeforeUpgradeDatabase(Connection connection);

	protected abstract boolean executeAfterUpgradeDatabase(Connection connection);

	protected boolean upgradeDatabase(Connection connection) {
		File scriptFile = getDatabaseScriptFile();

		try {

			if (executeUpgradeTransaction(connection, new BufferedQuery(new FileReader(scriptFile)))) {
				application.updateDatabaseVersion(version);
				logger.info("Database version updated to " + version);
			}
			else {
				logger.error("Could not upgrade database to version " + version + ". Check script file: " + scriptFile.getAbsolutePath());
				return false;
			}

		} catch (FileNotFoundException exception) {
			logger.error("Could not upgrade database to version " + version + ". Check script file: " + scriptFile.getAbsolutePath(), exception);
			return false;
		}

		return true;
	}

	private boolean executeUpgradeTransaction(Connection connection, BufferedQuery bufferedQuery) {
		boolean autoCommit;
		String query;
		Statement statement = null;

		try {
			autoCommit = connection.getAutoCommit();
			connection.setAutoCommit(false);
			statement = connection.createStatement();

			while ((query = bufferedQuery.readQuery()) != null) {
				query = query.replace(QUERY_ESCAPED_SEMICOLON, ";");
				statement.execute(query);
			}

			if (autoCommit) {
				connection.commit();
				connection.setAutoCommit(true);
			}
		} catch (Exception exception) {
			logger.error("Failed executing transaction", exception);
			try {
				connection.rollback();
			} catch (SQLException oRollbackException) {
			}
			return false;
		} finally {
			try {
				if (statement != null) statement.close();
			} catch (SQLException e) {
			}
		}

		return true;
	}

	private File getDatabaseScriptFile() {
		int versionNumber = VersionHelper.versionToNumber(this.version);
		String upgradesDir = configuration.getUpgradesDir();
		String databaseType = configuration.getString(Configuration.JDBC_DATABASE);
		return new File(upgradesDir + "/" + "v" + String.valueOf(versionNumber) + "/" + databaseType.toLowerCase() + ".sql");
	}

}
