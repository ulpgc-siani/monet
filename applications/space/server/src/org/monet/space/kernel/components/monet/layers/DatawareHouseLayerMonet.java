package org.monet.space.kernel.components.monet.layers;

import org.monet.space.kernel.components.monet.ComponentPersistenceMonet;
import org.monet.space.kernel.guice.InjectorFactory;
import org.monet.space.kernel.machines.bim.Engine;
import org.monet.space.kernel.model.Context;
import org.monet.space.kernel.producers.ProducersFactory;

public class DatawareHouseLayerMonet extends LayerMonet {
	protected final Context context = Context.getInstance();
	protected final ProducersFactory producersFactory = ProducersFactory.getInstance();
	private Engine engine;

	public DatawareHouseLayerMonet() {
	}

	protected boolean isStarted() {
		return ComponentPersistenceMonet.started();
	}

	protected Engine getEngine() {

		if (this.engine == null)
			this.engine = InjectorFactory.getInstance().getInstance(Engine.class);

		return this.engine;
	}

}
