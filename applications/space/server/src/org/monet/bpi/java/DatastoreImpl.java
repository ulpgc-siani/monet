package org.monet.bpi.java;

import org.monet.bpi.Cube;
import org.monet.bpi.Datastore;
import org.monet.bpi.Dimension;
import org.monet.space.kernel.bpi.java.locator.BPIClassLocator;

public class DatastoreImpl implements Datastore {
	private org.monet.space.kernel.model.Datastore datastore;

	BPIClassLocator bpiClassLocator = BPIClassLocator.getInstance();

	public void injectDatastore(org.monet.space.kernel.model.Datastore datastore) {
		this.datastore = datastore;
	}

	protected Dimension getDimension(Class<? extends Dimension> dimensionClass, String key) {

		org.monet.space.kernel.model.Dimension dimension = this.datastore.getDimension(key);
		DimensionImpl bpiDimension = this.bpiClassLocator.instantiateBehaviour(dimensionClass);
		bpiDimension.injectDatastore(this.datastore);
		bpiDimension.injectDimension(dimension);

		return bpiDimension;
	}

	protected Cube getCube(Class<? extends Cube> cubeClass, String key) {

		org.monet.space.kernel.model.Cube cube = this.datastore.getCube(key);
		CubeImpl bpiCube = this.bpiClassLocator.instantiateBehaviour(cubeClass);
		bpiCube.injectDatastore(this.datastore);
		bpiCube.injectCube(cube);

		return bpiCube;
	}

}
