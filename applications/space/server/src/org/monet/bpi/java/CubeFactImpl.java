package org.monet.bpi.java;

import org.monet.bpi.CubeFact;
import org.monet.space.kernel.bpi.java.locator.BPIClassLocator;
import org.monet.space.kernel.components.ComponentDatawareHouse;
import org.monet.space.kernel.components.layers.DatastoreLayer;

public class CubeFactImpl implements CubeFact {
	org.monet.space.kernel.model.CubeFact fact;
	org.monet.space.kernel.model.Datastore datastore;
	org.monet.space.kernel.model.Cube cube;

	final BPIClassLocator bpiClassLocator = BPIClassLocator.getInstance();
	final DatastoreLayer datastoreLayer = ComponentDatawareHouse.getInstance().getDatastoreLayer();

	void injectFact(org.monet.space.kernel.model.CubeFact fact) {
		this.fact = fact;
	}

	public void injectDatastore(org.monet.space.kernel.model.Datastore datastore) {
		this.datastore = datastore;
	}

	public void injectCube(org.monet.space.kernel.model.Cube cube) {
		this.cube = cube;
	}

	public double getMeasure(String key) {
		return this.fact.getMeasure(key);
	}

	public void setMeasure(String key, double value) {
		this.fact.setMeasure(key, value);
	}

	public String getComponentId(String key) {
		return this.fact.getComponent(key);
	}

	public void setComponent(String key, String componentId) {
		this.fact.setComponent(key, componentId);
	}

	public void save() {
		this.datastoreLayer.insertFact(datastore, cube, this.fact);
	}

}
