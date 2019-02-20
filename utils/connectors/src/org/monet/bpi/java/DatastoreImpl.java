package org.monet.bpi.java;


import org.monet.api.space.backservice.BackserviceApi;
import org.monet.bpi.Cube;
import org.monet.bpi.Datastore;
import org.monet.bpi.Dimension;
import org.monet.v3.BPIClassLocator;
import org.monet.v3.model.Dictionary;

public class DatastoreImpl implements Datastore {
	private org.monet.api.space.backservice.impl.model.Datastore datastore;
	private BPIClassLocator bpiClassLocator;
	private Dictionary dictionary;
	private BackserviceApi api;

	public void injectDatastore(org.monet.api.space.backservice.impl.model.Datastore datastore) {
		this.datastore = datastore;
	}

	public void injectBpiClassLocator(BPIClassLocator bpiClassLocator) {
		this.bpiClassLocator = bpiClassLocator;
	}

	public void injectDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public void injectApi(BackserviceApi api) {
		this.api = api;
	}

	protected Dimension getDimension(Class<? extends Dimension> dimensionClass, String key) {
		String code = this.dictionary.getDimensionDefinition(key).getCode();

		org.monet.api.space.backservice.impl.model.Dimension dimension = this.datastore.getDimension(code);
		DimensionImpl bpiDimension = this.bpiClassLocator.instantiateBehaviour(dimensionClass);
		bpiDimension.injectApi(this.api);
		bpiDimension.injectBPIClassLocator(this.bpiClassLocator);
		bpiDimension.injectDictionary(this.dictionary);
		bpiDimension.injectDatastore(this.datastore);
		bpiDimension.injectDimension(dimension);

		return bpiDimension;
	}

	protected Cube getCube(Class<? extends Cube> cubeClass, String key) {
		String code = this.dictionary.getCubeDefinition(key).getCode();

		org.monet.api.space.backservice.impl.model.Cube cube = this.datastore.getCube(code);
		CubeImpl bpiCube = this.bpiClassLocator.instantiateBehaviour(cubeClass);
		bpiCube.injectApi(this.api);
		bpiCube.injectBPIClassLocator(this.bpiClassLocator);
		bpiCube.injectDictionary(this.dictionary);
		bpiCube.injectDatastore(this.datastore);
		bpiCube.injectCube(cube);

		return bpiCube;
	}

}
