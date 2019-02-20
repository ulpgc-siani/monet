package kernel.database.upgrades.v3211;

import org.monet.metamodel.TaskDefinition;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.agents.upgrade.UpgradeScript;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.library.LibraryArray;
import org.monet.space.kernel.model.Dictionary;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Main extends UpgradeScript {
	private static final String VERSION = "3.2.11";

	public Main(AgentLogger logger, Configuration configuration) {
		super(VERSION, logger, configuration);
	}

	@Override
	protected boolean executeBeforeUpgradeDatabase(Connection connection) {
		return true;
	}

	@Override
	protected boolean executeAfterUpgradeDatabase(Connection connection) {
		List<String> definitionCodes = getBackgroundDefinitionCodes();
		executeBackgroundQuery(connection, definitionCodes);
		return true;
	}

	private List<String> getBackgroundDefinitionCodes() {
		Dictionary dictionary = Dictionary.getInstance();
		List<String> result = new ArrayList();

		for (TaskDefinition taskDefinition : dictionary.getTaskDefinitionList()) {
			if (taskDefinition.isBackground())
				result.add(taskDefinition.getCode());
		}

		return result;
	}

	private void executeBackgroundQuery(Connection connection, List<String> definitionCodes) {
		String query = "UPDATE ts$tasks SET background=1 WHERE id IN (%s)";
		String definitionCodesValue = LibraryArray.implode(definitionCodes.toArray(new String[0]), "','");
		Statement statement = null;

		try {
			statement = connection.createStatement();
			statement.executeUpdate(String.format(query, "'" + definitionCodesValue + "'"));
		}
		catch (SQLException exception) {
			this.logger.error(exception.getMessage());
			try {
				connection.rollback();
			} catch (SQLException oRollbackException) {
			}
		}
		finally {
			try {
				if (statement != null) statement.close();
			} catch (SQLException e) {
			}
		}

	}
}
