package org.monet.bpi.java;

import org.monet.api.space.backservice.BackserviceApi;
import org.monet.bpi.CubeFact;
import org.monet.v3.BPIClassLocator;
import org.monet.v3.model.Dictionary;

public class CubeFactImpl implements CubeFact {
	org.monet.api.space.backservice.impl.model.CubeFact fact;
	org.monet.api.space.backservice.impl.model.Datastore datastore;
	org.monet.api.space.backservice.impl.model.Cube cube;

	private BPIClassLocator bpiClassLocator;
	private Dictionary dictionary;
	private BackserviceApi api;

	void injectFact(org.monet.api.space.backservice.impl.model.CubeFact fact) {
		this.fact = fact;
	}

	public void injectBPIClassLocator(BPIClassLocator bpiClassLocator) {
		this.bpiClassLocator = bpiClassLocator;
	}

	public void injectDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public void injectApi(BackserviceApi api) {
		this.api = api;
	}

	public void injectDatastore(org.monet.api.space.backservice.impl.model.Datastore datastore) {
		this.datastore = datastore;
	}

	public void injectCube(org.monet.api.space.backservice.impl.model.Cube cube) {
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
		this.api.addDatastoreCubeFact(datastore.getName(), cube.getName(), this.fact);
	}

}
