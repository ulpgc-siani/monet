package org.monet.bpi.java;

import org.apache.commons.lang.NotImplementedException;
import org.monet.bpi.Datastore;
import org.monet.bpi.DatastoreBuilder;
import org.monet.bpi.Node;

public class DatastoreBuilderImpl implements DatastoreBuilder {

	protected Datastore loadDatastore(Class<? extends Datastore> datastoreClass, String name) {
		throw new NotImplementedException();
	}

	@Override
	public void onBuild(Node node) {
		// TODO Auto-generated method stub
	}

}
