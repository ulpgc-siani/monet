package org.monet.bpi;

public interface CubeFact {

	double getMeasure(String key);

	void setMeasure(String key, double value);

	String getComponentId(String key);

	void setComponent(String key, String componentId);

	void save();

}
