package org.monet.bpi.java;

import org.monet.bpi.DatastoreService;
import org.monet.space.kernel.bpi.java.locator.BPIClassLocator;
import org.monet.space.kernel.components.ComponentDatawareHouse;
import org.monet.space.kernel.components.layers.DatastoreLayer;
import org.monet.space.kernel.model.Datastore;

public class DatastoreServiceImpl extends DatastoreService {

	BPIClassLocator bpiClassLocator = BPIClassLocator.getInstance();

	public static void init() {
		instance = new DatastoreServiceImpl();
	}

	@Override
	public DatastoreImpl getImpl(String name) {
		DatastoreLayer datastoreLayer = ComponentDatawareHouse.getInstance().getDatastoreLayer();
		Datastore datastore = datastoreLayer.load(name);

		DatastoreImpl bpiDatastore = this.bpiClassLocator.instantiateBehaviour(datastore.getDefinition());
		bpiDatastore.injectDatastore(datastore);

		return bpiDatastore;
	}

}
