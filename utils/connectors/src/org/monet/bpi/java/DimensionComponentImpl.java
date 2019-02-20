package org.monet.bpi.java;

import org.monet.api.space.backservice.BackserviceApi;
import org.monet.bpi.DimensionComponent;
import org.monet.metamodel.Dictionary;
import org.monet.v3.BPIClassLocator;

import java.util.ArrayList;

public class DimensionComponentImpl implements DimensionComponent {
	org.monet.api.space.backservice.impl.model.DimensionComponent component;
	org.monet.api.space.backservice.impl.model.Datastore datastore;
	org.monet.api.space.backservice.impl.model.Dimension dimension;
	BackserviceApi api;
	BPIClassLocator bpiClassLocator;
	Dictionary dictionary;

	void injectComponent(org.monet.api.space.backservice.impl.model.DimensionComponent component) {
		this.component = component;
	}

	void injectDatastore(org.monet.api.space.backservice.impl.model.Datastore datastore) {
		this.datastore = datastore;
	}

	void injectDimension(org.monet.api.space.backservice.impl.model.Dimension dimension) {
		this.dimension = dimension;
	}

	public void injectApi(BackserviceApi api) {
		this.api = api;
	}

	public void injectBPIClassLocator(BPIClassLocator bpiClassLocator) {
		this.bpiClassLocator = bpiClassLocator;
	}

	public void injectDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public Object getFeatureValue(String key) {
		return this.component.getFeatureValue(key);
	}

	public ArrayList<Object> getFeatureValues(String key) {
		return this.component.getFeatureValues(key);
	}

	@Override
	public void addFeature(String key, org.monet.bpi.types.Number value) {
		this.component.addFeature(key, value.doubleValue());
	}

	@Override
	public void addFeature(String key, Boolean value) {
		this.component.addFeature(key, value);
	}

	@Override
	public void addFeature(String key, String value) {
		this.component.addFeature(key, value);
	}

	@Override
	public void addFeature(String key, org.monet.bpi.types.Term value, ArrayList<org.monet.bpi.types.Term> ancestors) {
		org.monet.bpi.types.Term term = (org.monet.bpi.types.Term) value;

		org.monet.api.space.backservice.impl.model.Term monetTerm = new org.monet.api.space.backservice.impl.model.Term(term.getKey(), term.getLabel());
		ArrayList<org.monet.api.space.backservice.impl.model.Term> monetAncestors = new ArrayList<org.monet.api.space.backservice.impl.model.Term>();

		for (org.monet.bpi.types.Term ancestor : ancestors) {
			org.monet.api.space.backservice.impl.model.Term monetAncestor = new org.monet.api.space.backservice.impl.model.Term(ancestor.getKey(), ancestor.getLabel());
			monetAncestors.add(monetAncestor);
		}

		this.component.addFeature(key, monetTerm, monetAncestors);
	}

	public void save() {
		this.api.addDatastoreDimensionComponent(this.datastore.getCode(), this.dimension.getCode(), this.component);
	}

}
