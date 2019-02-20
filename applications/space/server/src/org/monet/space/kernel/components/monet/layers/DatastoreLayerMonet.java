package org.monet.space.kernel.components.monet.layers;

import org.monet.metamodel.DatastoreDefinition;
import org.monet.space.kernel.components.layers.DatastoreLayer;
import org.monet.space.kernel.constants.Producers;
import org.monet.space.kernel.exceptions.CreateDatastoreSessionException;
import org.monet.space.kernel.exceptions.MountDatastoreException;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.producers.ProducerDatastore;

import java.util.List;

public class DatastoreLayerMonet extends DatawareHouseLayerMonet implements DatastoreLayer {

	public DatastoreLayerMonet() {
		super();
	}

	@Override
	public boolean exists(String key) {
		String code = Dictionary.getInstance().getDefinitionCode(key);
		return this.getEngine().existsDatastore(code);
	}

	@Override
	public Datastore load(String key) {
		String code = Dictionary.getInstance().getDefinitionCode(key);
		return this.getEngine().getDatastore(code);
	}

	@Override
	public Datastore create(String key) {
		String code = Dictionary.getInstance().getDefinitionCode(key);
		return this.getEngine().createDatastore(code);
	}

	@Override
	public Datastore update(DatastoreDefinition oldDefinition, DatastoreDefinition definition) {
		return this.getEngine().updateDatastore(oldDefinition, definition);
	}

	@Override
	public void remove(String key) {
		String code = Dictionary.getInstance().getDefinitionCode(key);
		this.getEngine().removeDatastore(code);
	}

	@Override
	public List<DatastoreTransaction> loadPendingTransactions(Datastore datastore) {
		ProducerDatastore producerDatastore = producersFactory.get(Producers.DATASTORE);
		return producerDatastore.loadPendingTransactions(datastore);
	}

	@Override
	public void removePendingTransactions(Datastore datastore, List<DatastoreTransaction> transactions) {
		ProducerDatastore producerDatastore = producersFactory.get(Producers.DATASTORE);
		producerDatastore.removePendingTransactions(datastore, transactions);
	}

	@Override
	public void insertComponent(Datastore datastore, Dimension dimension, DimensionComponent component) {
		ProducerDatastore producerDatastore = producersFactory.get(Producers.DATASTORE);
		producerDatastore.queue(datastore, dimension, component);
	}

	@Override
	public void insertFact(Datastore datastore, Cube cube, CubeFact fact) {
		ProducerDatastore producerDatastore = producersFactory.get(Producers.DATASTORE);
		producerDatastore.queue(datastore, cube, fact);
	}

	@Override
	public void insertFacts(Datastore datastore, Cube cube, CubeFactList factList) {
		ProducerDatastore producerDatastore = producersFactory.get(Producers.DATASTORE);

		for (CubeFact fact : factList)
			producerDatastore.queue(datastore, cube, fact);
	}

	@Override
	public void mount(Datastore datastore) {
		ProducerDatastore producerDatastore = producersFactory.get(Producers.DATASTORE);
		List<DatastoreTransaction> transactions = producerDatastore.loadPendingTransactions(datastore);
		boolean sessionCreated = true;

		try {
			this.getEngine().mountDatastore(datastore.getCode(), transactions);
		}
		catch (CreateDatastoreSessionException exception) {
			sessionCreated = false;
		}
		catch (MountDatastoreException exception) {
		}
		finally {
			if (sessionCreated)
				producerDatastore.removePendingTransactions(datastore, transactions);
		}
	}

	@Override
	public void create() {
		this.getEngine().create();
	}

	@Override
	public void destroy() {
		this.getEngine().destroy();
	}

}