package upgrades.v321;

import org.monet.docservice.Application;
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

		List<String> migrationState;
		FileWriter migrationStateWriter = null;
		Map<String, String> migrationLocations;
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
				String documentId = resultSet.getString("id_document");

				if (migrationState.contains(documentId))
					continue;

				InputStream documentStream = null;
				try {
					statementData = connection.createStatement();
					resultSetData = statementData.executeQuery("SELECT `data` FROM ds$documents_data WHERE id_document = '" + documentId + "'");
                    resultSetData.next();
					documentStream = resultSetData.getBlob("data").getBinaryStream();
				} finally {
					if (resultSetData != null) resultSetData.close();
					if (statementData != null) statementData.close();
				}

				String location = diskManager.addDocument(documentId, documentStream);

				migrationState.add(documentId);
				migrationStateWriter.write(documentId + "\n");
				migrationStateWriter.flush();

				migrationLocations.put(documentId, location);
				migrationLocationsWriter.write(documentId + ":" + location + "\n");
				migrationLocationsWriter.flush();
			}

			if (autoCommit) {
				connection.commit();
				connection.setAutoCommit(true);
			}

		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			try {
				connection.rollback();
			} catch (SQLException oRollbackException) {
			}
			return false;
		} catch (IOException exception) {
			this.logger.error(exception.getMessage());
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
			try {
				if (migrationStateWriter != null)
					migrationStateWriter.close();
				if (migrationLocationsWriter != null)
					migrationLocationsWriter.close();
			} catch (IOException e) {
			}
		}

		return true;
	}

	private boolean saveDocumentsLocations(Connection connection) {
		boolean autoCommit;
		Statement statement = null;
		Map<String, String> migrationLocations;

		try {
			migrationLocations = readMigrationLocations();

			autoCommit = connection.getAutoCommit();
			connection.setAutoCommit(false);
			statement = connection.createStatement();

			for (String documentId : migrationLocations.keySet()) {
				String location = migrationLocations.get(documentId);
				statement.executeUpdate(String.format("UPDATE ds$documents_data SET location='%s' WHERE id_document='%s'", location, documentId));
			}

			if (autoCommit) {
				connection.commit();
				connection.setAutoCommit(true);
			}

		} catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			try {
				connection.rollback();
			} catch (SQLException oRollbackException) {
			}
			return false;
		} catch (IOException exception) {
			this.logger.error(exception.getMessage());
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

	private File getMigrationStateFile() {
		return new File(configuration.getUserDataDir() + "/upgrade." + VERSION + ".out");
	}

	private File getMigrationLocationsFile() {
		return new File(configuration.getUserDataDir() + "/upgrade." + VERSION + ".locations");
	}

	private ArrayList<String> readMigrationState() throws IOException {
		ArrayList<String> result = new ArrayList<>();
		File source = getMigrationStateFile();
		Scanner fileScanner = null;

		if (!source.exists())
			source.createNewFile();

		try {
			fileScanner = new Scanner(source);
			while (fileScanner.hasNextLine()) {
				String documentId = fileScanner.nextLine();
				if (documentId.isEmpty()) continue;
				result.add(documentId);
			}
		}
		finally {
			if (fileScanner != null)
				fileScanner.close();
		}

		return result;
	}

	private Map<String, String> readMigrationLocations() throws IOException {
		Map<String, String> result = new HashMap<>();
		File source = getMigrationLocationsFile();
		Scanner fileScanner = null;

		if (!source.exists())
			source.createNewFile();

		try {
			fileScanner = new Scanner(source);
			while (fileScanner.hasNextLine()) {
				String line = fileScanner.nextLine();
				if (line.isEmpty()) continue;
				String[] document = line.split(":");
				result.put(document[0], document[1]);
			}
		}
		finally {
			if (fileScanner != null)
				fileScanner.close();
		}

		return result;
	}

}
