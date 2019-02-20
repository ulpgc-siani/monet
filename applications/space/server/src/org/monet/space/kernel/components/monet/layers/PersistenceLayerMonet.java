package org.monet.space.kernel.components.monet.layers;

import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.monet.ComponentPersistenceMonet;
import org.monet.space.kernel.model.Account;
import org.monet.space.kernel.model.Context;
import org.monet.space.kernel.model.Session;
import org.monet.space.kernel.producers.ProducersFactory;

public class PersistenceLayerMonet extends LayerMonet {

	protected final ComponentPersistence componentPersistence;
	protected final ProducersFactory producersFactory = ProducersFactory.getInstance();
	protected final Context oContext = Context.getInstance();

	public PersistenceLayerMonet(ComponentPersistence componentPersistence) {
		this.componentPersistence = componentPersistence;
	}

	protected boolean isStarted() {
		return ComponentPersistenceMonet.started();
	}

	protected Account getAccount() {
		Session oSession = this.oContext.getCurrentSession();
		if (oSession == null) return null;
		return oSession.getAccount();
	}

}
