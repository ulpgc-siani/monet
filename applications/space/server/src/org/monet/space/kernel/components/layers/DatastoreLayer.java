package org.monet.space.kernel.components.layers;

import org.monet.metamodel.DatastoreDefinition;
import org.monet.space.kernel.model.*;

import java.util.List;

public interface DatastoreLayer extends Layer {

	boolean exists(String key);
	Datastore load(String key);
	Datastore create(String key);
	Datastore update(DatastoreDefinition oldDefinition, DatastoreDefinition definition);
	void remove(String key);
	List<DatastoreTransaction> loadPendingTransactions(Datastore datastore);
	void removePendingTransactions(Datastore datastore, List<DatastoreTransaction> transactions);
	void insertComponent(Datastore datastore, Dimension dimension, DimensionComponent component);
	void insertFact(Datastore datastore, Cube cube, CubeFact fact);
	void insertFacts(Datastore datastore, Cube cube, CubeFactList factList);
	void mount(Datastore datastore);
}
