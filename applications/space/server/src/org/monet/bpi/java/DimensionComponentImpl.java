package org.monet.bpi.java;

import org.monet.bpi.DimensionComponent;
import org.monet.bpi.types.Number;
import org.monet.bpi.types.Term;
import org.monet.space.kernel.components.ComponentDatawareHouse;
import org.monet.space.kernel.components.layers.DatastoreLayer;

import java.util.ArrayList;

public class DimensionComponentImpl implements DimensionComponent {
	org.monet.space.kernel.model.DimensionComponent component;
	org.monet.space.kernel.model.Datastore datastore;
	org.monet.space.kernel.model.Dimension dimension;

	final DatastoreLayer datastoreLayer = ComponentDatawareHouse.getInstance().getDatastoreLayer();

	void injectComponent(org.monet.space.kernel.model.DimensionComponent component) {
		this.component = component;
	}

	void injectDatastore(org.monet.space.kernel.model.Datastore datastore) {
		this.datastore = datastore;
	}

	void injectDimension(org.monet.space.kernel.model.Dimension dimension) {
		this.dimension = dimension;
	}

	public Object getFeatureValue(String key) {
		return this.component.getFeatureValue(key);
	}

	public ArrayList<Object> getFeatureValues(String key) {
		return this.component.getFeatureValues(key);
	}

	public void addFeature(String key, Number value) {
		this.component.addFeature(key, value.doubleValue());
	}

	public void addFeature(String key, Boolean value) {
		this.component.addFeature(key, value);
	}

	public void addFeature(String key, String value) {
		this.component.addFeature(key, value);
	}

	public void addFeature(String key, Term value, ArrayList<Term> ancestors) {
		Term term = (Term) value;

		org.monet.space.kernel.model.Term monetTerm = new org.monet.space.kernel.model.Term(term.getKey(), term.getLabel());
		ArrayList<org.monet.space.kernel.model.Term> monetAncestors = new ArrayList<org.monet.space.kernel.model.Term>();

		for (Term ancestor : ancestors) {
			org.monet.space.kernel.model.Term monetAncestor = new org.monet.space.kernel.model.Term(ancestor.getKey(), ancestor.getLabel());
			monetAncestors.add(monetAncestor);
		}

		this.component.addFeature(key, monetTerm, monetAncestors);
	}

	public void save() {
		this.datastoreLayer.insertComponent(this.datastore, this.dimension, this.component);
	}

}
