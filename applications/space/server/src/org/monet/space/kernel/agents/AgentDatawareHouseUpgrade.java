package org.monet.space.kernel.agents;

import org.monet.metamodel.DatastoreDefinition;
import org.monet.space.kernel.components.ComponentDatawareHouse;
import org.monet.space.kernel.components.layers.DatastoreLayer;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.model.Context;
import org.monet.space.kernel.model.Dictionary;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class AgentDatawareHouseUpgrade extends TimerTask {
	private Configuration configuration;
	private Timer timer;
	private static final String THREAD_NAME = "Monet-DatawareHouse-Upgrade-Agent-%s";

	private static AgentDatawareHouseUpgrade instance;

	private AgentDatawareHouseUpgrade() {
		this.configuration = Configuration.getInstance();
	}

	public static AgentDatawareHouseUpgrade getInstance() {
		if (instance == null)
			instance = new AgentDatawareHouseUpgrade();
		return instance;
	}

	public synchronized void init() {
		this.timer = new Timer(String.format(THREAD_NAME, Context.getInstance().getFrameworkName()), true);

		if (configuration.launchDatawareHouseUpgradeOnStart())
			this.upgrade();

		this.timer.schedule(this, 30000, 60 * 30 * 1000);
	}

	@Override
	public synchronized void run() {
		if (!this.isUpgradeHour())
			return;
		this.upgrade();
	}

	public synchronized void destroy() {
		if (this.timer != null) {
			this.timer.cancel();
			this.timer.purge();
		}
	}

	private void upgrade() {
		DatastoreLayer datastoreLayer = ComponentDatawareHouse.getInstance().getDatastoreLayer();
		Dictionary dictionary = Dictionary.getInstance();

		for (DatastoreDefinition datastoreDefinition : dictionary.getDatastoreDefinitionList()) {
			String datastoreCode = datastoreDefinition.getCode();

			if (datastoreLayer.exists(datastoreCode))
				datastoreLayer.mount(datastoreLayer.load(datastoreCode));
		}
	}

	private boolean isUpgradeHour() {
		if (configuration.isDatawareHouseUpgradeDisabled()) return false;
		Calendar calendar = Calendar.getInstance();
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		return hour == configuration.getDatawareHouseUpgradeHour();
	}

}
