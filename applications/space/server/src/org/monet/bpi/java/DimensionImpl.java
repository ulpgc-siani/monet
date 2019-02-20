package org.monet.bpi.java;

import org.monet.bpi.Dimension;
import org.monet.bpi.DimensionComponent;
import org.monet.space.kernel.bpi.java.locator.BPIClassLocator;
import org.monet.space.kernel.components.ComponentDatawareHouse;
import org.monet.space.kernel.components.layers.DatastoreLayer;
import org.monet.space.kernel.model.Datastore;

import java.util.HashMap;

public class DimensionImpl implements Dimension {
	private org.monet.space.kernel.model.Datastore datastore;
	private org.monet.space.kernel.model.Dimension dimension;
	private DatastoreLayer datastoreLayer = ComponentDatawareHouse.getInstance().getDatastoreLayer();
	private HashMap<String, DimensionComponent> componentsToInsert = new HashMap<String, DimensionComponent>();

	BPIClassLocator bpiClassLocator = BPIClassLocator.getInstance();

	void injectDatastore(Datastore datastore) {
		this.datastore = datastore;
	}

	void injectDimension(org.monet.space.kernel.model.Dimension dimension) {
		this.dimension = dimension;
	}

	protected DimensionComponent insertComponentImpl(Class<? extends DimensionComponent> dimensionComponentClass, String id) {

		if (this.componentsToInsert.containsKey(id))
			return this.componentsToInsert.get(id);

		org.monet.space.kernel.model.DimensionComponent dimensionComponent = new org.monet.space.kernel.model.DimensionComponent(this.dimension.getCode());
		dimensionComponent.setId(id);

		DimensionComponentImpl bpiDimensionComponent = this.bpiClassLocator.instantiateBehaviour(dimensionComponentClass);
		bpiDimensionComponent.injectComponent(dimensionComponent);
		bpiDimensionComponent.injectDatastore(datastore);
		bpiDimensionComponent.injectDimension(dimension);

		this.componentsToInsert.put(id, bpiDimensionComponent);

		return bpiDimensionComponent;
	}

	public void save() {
		this.insertComponents();
	}

	private void insertComponents() {
		for (DimensionComponent component : this.componentsToInsert.values()) {
			DimensionComponentImpl componentImpl = (DimensionComponentImpl) component;
			this.datastoreLayer.insertComponent(this.datastore, this.dimension, componentImpl.component);
		}
	}

}
