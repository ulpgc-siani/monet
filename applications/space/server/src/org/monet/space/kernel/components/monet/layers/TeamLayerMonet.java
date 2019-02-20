package org.monet.space.kernel.components.monet.layers;

import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.TeamLayer;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.Producers;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.Team;
import org.monet.space.kernel.producers.ProducerTeam;

public class TeamLayerMonet extends PersistenceLayerMonet implements TeamLayer {

	public TeamLayerMonet(ComponentPersistence componentPersistence) {
		super(componentPersistence);
	}

	@Override
	public Team loadTeam() {
		ProducerTeam producerTeam;
		Team team;

		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}

		producerTeam = (ProducerTeam) this.producersFactory.get(Producers.TEAM);
		team = producerTeam.load();

		return team;
	}

}

