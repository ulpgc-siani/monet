package org.monet.bpi.java;

import org.monet.api.space.backservice.BackserviceApi;
import org.monet.api.space.backservice.impl.model.Datastore;
import org.monet.bpi.Dimension;
import org.monet.bpi.DimensionComponent;
import org.monet.metamodel.Dictionary;
import org.monet.v3.BPIClassLocator;

import java.util.HashMap;

public class DimensionImpl implements Dimension {
	private org.monet.api.space.backservice.impl.model.Datastore datastore;
	private org.monet.api.space.backservice.impl.model.Dimension dimension;
	private HashMap<String, DimensionComponent> componentsToInsert = new HashMap<String, DimensionComponent>();
	private BPIClassLocator bpiClassLocator;
	private Dictionary dictionary;
	private BackserviceApi api;

	void injectDatastore(Datastore datastore) {
		this.datastore = datastore;
	}

	void injectDimension(org.monet.api.space.backservice.impl.model.Dimension dimension) {
		this.dimension = dimension;
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

	protected DimensionComponent insertComponentImpl(Class<? extends DimensionComponent> dimensionComponentClass, String id) {

		if (this.componentsToInsert.containsKey(id))
			return this.componentsToInsert.get(id);

		org.monet.api.space.backservice.impl.model.DimensionComponent dimensionComponent = new org.monet.api.space.backservice.impl.model.DimensionComponent(this.dimension.getCode());
		dimensionComponent.setId(id);

		DimensionComponentImpl bpiDimensionComponent = this.bpiClassLocator.instantiateBehaviour(dimensionComponentClass);
		bpiDimensionComponent.injectApi(this.api);
		bpiDimensionComponent.injectBPIClassLocator(this.bpiClassLocator);
		bpiDimensionComponent.injectDictionary(this.dictionary);
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
			this.api.addDatastoreDimensionComponent(this.datastore.getCode(), this.dimension.getCode(), componentImpl.component);
		}
	}

}
