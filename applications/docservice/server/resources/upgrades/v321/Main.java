package upgrades.v321;

import org.monet.docservice.Application;
import org.monet.docservice.core.Key;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.docprocessor.configuration.Configuration;
import org.monet.docservice.docprocessor.data.DiskManager;
import org.monet.docservice.upgrades.impl.UpgradeScript;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Main extends UpgradeScript {
	private static final String VERSION = "3.2.1";

	public Main(Application application, Logger logger, Configuration configuration, DiskManager diskManager) {
		super(VERSION, application, logger, configuration, diskManager);
	}

	@Override
	protected boolean executeBeforeUpgradeDatabase(Connection connection) {
		return moveDocumentsFromDatabaseToFile(connection);
	}

	@Override
	protected boolean executeAfterUpgradeDatabase(Connection connection) {
		return saveDocumentsLocations(connection);
	}

	private boolean moveDocumentsFromDatabaseToFile(Connection connection) {
		boolean autoCommit;
		Statement statement = null;
		ResultSet resultSet;

		Statement statementData = null;
		ResultSet resultSetData = null;

		List<Key> migrationState;
		FileWriter migrationStateWriter = null;
		Map<Key, String> migrationLocations;
		FileWriter migrationLocationsWriter = null;

		try {
			migrationState = readMigrationState();
			migrationStateWriter = new FileWriter(getMigrationStateFile(), true);

			migrationLocations = readMigrationLocations();
			migrationLocationsWriter = new FileWriter(getMigrationLocationsFile(), true);

			autoCommit = connection.getAutoCommit();
			connection.setAutoCommit(false);

			statement = connection.createStatement();
//			resultSet = statement.executeQuery("SELECT id_document, `data` FROM ds$documents_data");
			resultSet = statement.executeQuery("SELECT id_document FROM ds$documents_data");

			while (resultSet.next()) {
				Key documentKey = Key.from(resultSet.getString("id_document"));

				if (migrationState.contains(documentKey))
					continue;

				InputStream documentStream;
				try {
					statementData = connection.createStatement();
					resultSetData = statementData.executeQuery("SELECT `data` FROM ds$documents_data WHERE id_document = '" + documentKey + "'");
                    resultSetData.next();
					documentStream = resultSetData.getBlob("data").getBinaryStream();
				} finally {
					if (resultSetData != null) resultSetData.close();
					if (statementData != null) statementData.close();
				}

				String location = diskManager.addDocument(documentKey, documentStream);

				migrationState.add(documentKey);
				migrationStateWriter.write(documentKey + "\n");
				migrationStateWriter.flush();

				migrationLocations.put(documentKey, location);
				migrationLocationsWriter.write(documentKey + ":" + location + "\n");
				migrationLocationsWriter.flush();
			}

			if (autoCommit) {
				connection.commit();
				connection.setAutoCommit(true);
			}

		} catch (SQLException | IOException exception) {
			this.logger.error(exception.getMessage());
			try {
				connection.rollback();
			} catch (SQLException ignored) {
			}
			return false;
		} finally {
			try {
				if (statement != null) statement.close();
			} catch (SQLException ignored) {
			}
			try {
				if (migrationStateWriter != null)
					migrationStateWriter.close();
				if (migrationLocationsWriter != null)
					migrationLocationsWriter.close();
			} catch (IOException ignored) {
			}
		}

		return true;
	}

	private boolean saveDocumentsLocations(Connection connection) {
		boolean autoCommit;
		Statement statement = null;
		Map<Key, String> migrationLocations;

		try {
			migrationLocations = readMigrationLocations();

			autoCommit = connection.getAutoCommit();
			connection.setAutoCommit(false);
			statement = connection.createStatement();

			for (Key documentKey : migrationLocations.keySet()) {
				String location = migrationLocations.get(documentKey);
				statement.executeUpdate(String.format("UPDATE ds$documents_data SET location='%s' WHERE id_document='%s'", location, documentKey));
			}

			if (autoCommit) {
				connection.commit();
				connection.setAutoCommit(true);
			}

		} catch (SQLException | IOException exception) {
			this.logger.error(exception.getMessage());
			try {
				connection.rollback();
			} catch (SQLException ignored) {
			}
			return false;
		} finally {
			try {
				if (statement != null) statement.close();
			} catch (SQLException ignored) {
			}
		}

		return true;
	}

	private File getMigrationStateFile() {
		return new File(configuration.getUserDataDir() + "/upgrade." + VERSION + ".out");
	}

	private File getMigrationLocationsFile() {
		return new File(configuration.getUserDataDir() + "/upgrade." + VERSION + ".locations");
	}

	private List<Key> readMigrationState() throws IOException {
		List<Key> result = new ArrayList<>();
		File source = getMigrationStateFile();

		if (!source.exists())
			source.createNewFile();

		try (Scanner fileScanner = new Scanner(source)) {
			while (fileScanner.hasNextLine()) {
				String documentId = fileScanner.nextLine();
				if (documentId.isEmpty()) continue;
				result.add(Key.from(documentId));
			}
		}

		return result;
	}

	private Map<Key, String> readMigrationLocations() throws IOException {
		Map<Key, String> result = new HashMap<>();
		File source = getMigrationLocationsFile();

		if (!source.exists())
			source.createNewFile();

		try (Scanner fileScanner = new Scanner(source)) {
			while (fileScanner.hasNextLine()) {
				String line = fileScanner.nextLine();
				if (line.isEmpty()) continue;
				String[] document = line.split(":");
				result.put(Key.from(document[0]), document[1]);
			}
		}

		return result;
	}

}
