package org.monet.bpi;

import org.monet.bpi.types.Number;
import org.monet.bpi.types.Term;

import java.util.ArrayList;

public interface DimensionComponent {

	Object getFeatureValue(String key);

	ArrayList<Object> getFeatureValues(String key);

	void addFeature(String key, Number value);

	void addFeature(String key, Boolean value);

	void addFeature(String key, String value);

	void addFeature(String key, Term value, ArrayList<Term> ancestors);

	void save();

}
