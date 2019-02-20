package org.monet.bpi.java;

import org.monet.bpi.Datastore;
import org.monet.bpi.DatastoreBuilder;
import org.monet.bpi.Node;
import org.monet.space.kernel.bpi.java.locator.BPIClassLocator;
import org.monet.space.kernel.components.ComponentDatawareHouse;
import org.monet.space.kernel.components.layers.DatastoreLayer;

public class DatastoreBuilderImpl implements DatastoreBuilder {

	BPIClassLocator bpiClassLocator = BPIClassLocator.getInstance();

	protected Datastore loadDatastore(Class<? extends Datastore> datastoreClass, String name) {
		DatastoreLayer datastoreLayer = ComponentDatawareHouse.getInstance().getDatastoreLayer();
		org.monet.space.kernel.model.Datastore datastore = datastoreLayer.load(name);

		DatastoreImpl bpiDatastore = this.bpiClassLocator.instantiateBehaviour(datastoreClass);
		bpiDatastore.injectDatastore(datastore);

		return bpiDatastore;
	}

	@Override
	public void onBuild(Node node) {
		// TODO Auto-generated method stub
	}

}
