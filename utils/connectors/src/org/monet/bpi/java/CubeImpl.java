package org.monet.bpi.java;

import org.monet.api.space.backservice.BackserviceApi;
import org.monet.api.space.backservice.impl.model.CubeFactList;
import org.monet.api.space.backservice.impl.model.Datastore;
import org.monet.bpi.Cube;
import org.monet.bpi.CubeFact;
import org.monet.bpi.types.Date;
import org.monet.v3.BPIClassLocator;
import org.monet.v3.model.Dictionary;

import java.util.ArrayList;
import java.util.List;

public class CubeImpl implements Cube {
	private org.monet.api.space.backservice.impl.model.Datastore datastore;
	private org.monet.api.space.backservice.impl.model.Cube cube;
	private ArrayList<CubeFact> facts = new ArrayList<CubeFact>();
	private BackserviceApi api;
	private Dictionary dictionary;
	private BPIClassLocator bpiClassLocator;

	public void injectDatastore(Datastore datastore) {
		this.datastore = datastore;
	}

	void injectCube(org.monet.api.space.backservice.impl.model.Cube cube) {
		this.cube = cube;
	}

	public void injectApi(BackserviceApi api) {
		this.api = api;
	}

	public void injectDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public void injectBPIClassLocator(BPIClassLocator bpiClassLocator) {
		this.bpiClassLocator = bpiClassLocator;
	}

	protected CubeFact insertFactImpl(Class<? extends CubeFact> cubeFactClass, Date date) {
		org.monet.api.space.backservice.impl.model.CubeFact fact = new org.monet.api.space.backservice.impl.model.CubeFact(date.getValue());

		CubeFactImpl bpiCubeFact = this.bpiClassLocator.instantiateBehaviour(cubeFactClass);
		bpiCubeFact.injectFact(fact);
		bpiCubeFact.injectDatastore(datastore);
		bpiCubeFact.injectCube(cube);
		bpiCubeFact.injectDictionary(this.dictionary);
		bpiCubeFact.injectBPIClassLocator(this.bpiClassLocator);
		bpiCubeFact.injectApi(this.api);

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
			factImpl.injectDictionary(this.dictionary);
			factImpl.injectBPIClassLocator(this.bpiClassLocator);
			factImpl.injectApi(this.api);
			cubeFactList.add(factImpl.fact);
		}

		this.api.addDatastoreCubeFacts(datastore.getCode(), cube.getCode(), cubeFactList);
	}

	public void save(List<CubeFact> facts) {
		CubeFactList cubeFactList = new CubeFactList();

		for (CubeFact fact : facts) {
			CubeFactImpl factImpl = (CubeFactImpl) fact;
			factImpl.injectDictionary(this.dictionary);
			factImpl.injectBPIClassLocator(this.bpiClassLocator);
			factImpl.injectApi(this.api);
			cubeFactList.add(factImpl.fact);
		}

		this.api.addDatastoreCubeFacts(datastore.getCode(), cube.getCode(), cubeFactList);
	}

}
