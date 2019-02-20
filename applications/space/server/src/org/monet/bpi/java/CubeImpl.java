package org.monet.bpi.java;

import org.monet.bpi.Cube;
import org.monet.bpi.CubeFact;
import org.monet.bpi.types.Date;
import org.monet.space.kernel.bpi.java.locator.BPIClassLocator;
import org.monet.space.kernel.components.ComponentDatawareHouse;
import org.monet.space.kernel.components.layers.DatastoreLayer;
import org.monet.space.kernel.model.CubeFactList;
import org.monet.space.kernel.model.Datastore;

import java.util.ArrayList;
import java.util.List;

public class CubeImpl implements Cube {
	private org.monet.space.kernel.model.Datastore datastore;
	private org.monet.space.kernel.model.Cube cube;
	private ArrayList<CubeFact> facts = new ArrayList<CubeFact>();
	private DatastoreLayer datastoreLayer = ComponentDatawareHouse.getInstance().getDatastoreLayer();

	BPIClassLocator bpiClassLocator = BPIClassLocator.getInstance();

	public void injectDatastore(Datastore datastore) {
		this.datastore = datastore;
	}

	void injectCube(org.monet.space.kernel.model.Cube cube) {
		this.cube = cube;
	}

	protected CubeFact insertFactImpl(Class<? extends CubeFact> cubeFactClass, Date date) {
		org.monet.space.kernel.model.CubeFact fact = new org.monet.space.kernel.model.CubeFact(date.getValue());

		CubeFactImpl bpiCubeFact = this.bpiClassLocator.instantiateBehaviour(cubeFactClass);
		bpiCubeFact.injectFact(fact);
		bpiCubeFact.injectDatastore(datastore);
		bpiCubeFact.injectCube(cube);

		this.facts.add(bpiCubeFact);

		return bpiCubeFact;
	}

	List<CubeFact> getFacts() {
		return this.facts;
	}

	public void save() {
		CubeFactList cubeFactList = new CubeFactList();

		for (CubeFact fact : this.facts) {
			CubeFactImpl factImpl = (CubeFactImpl) fact;
			cubeFactList.add(factImpl.fact);
		}

		this.datastoreLayer.insertFacts(datastore, cube, cubeFactList);
	}

	public void save(List<CubeFact> facts) {
		CubeFactList cubeFactList = new CubeFactList();

		for (CubeFact fact : facts) {
			CubeFactImpl factImpl = (CubeFactImpl) fact;
			cubeFactList.add(factImpl.fact);
		}

		this.datastoreLayer.insertFacts(datastore, cube, cubeFactList);
	}

}
