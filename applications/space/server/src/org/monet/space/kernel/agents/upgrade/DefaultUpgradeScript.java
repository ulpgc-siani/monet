package org.monet.space.kernel.agents.upgrade;

import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.configuration.Configuration;

import java.sql.Connection;

public class DefaultUpgradeScript extends UpgradeScript {

	public DefaultUpgradeScript(String version, AgentLogger logger, Configuration configuration) {
		super(version, logger, configuration);
	}

	@Override
	protected boolean executeBeforeUpgradeDatabase(Connection connection) {
		return true;
	}

	@Override
	protected boolean executeAfterUpgradeDatabase(Connection connection) {
		return true;
	}
}
