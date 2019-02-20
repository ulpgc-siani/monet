package upgrades.v322;

import org.monet.docservice.Application;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.docprocessor.configuration.Configuration;
import org.monet.docservice.docprocessor.data.DiskManager;
import org.monet.docservice.upgrades.impl.UpgradeScript;

import java.sql.Connection;

public class Main extends UpgradeScript {
	private static final String VERSION = "3.2.2";

	public Main(Application application, Logger logger, Configuration configuration, DiskManager diskManager) {
		super(VERSION, application, logger, configuration, diskManager);
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
