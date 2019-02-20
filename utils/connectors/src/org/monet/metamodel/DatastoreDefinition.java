package org.monet.metamodel;

import org.monet.metamodel.interfaces.IsInitiable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatastoreDefinition extends DatastoreDefinitionBase implements IsInitiable {
	HashMap<String, ArrayList<DimensionProperty>> dimensionsWithOntologyMap = new HashMap<String, ArrayList<DimensionProperty>>();
	HashMap<String, CubeProperty> cubeMap = new HashMap<String, CubeProperty>();
	HashMap<String, DimensionProperty> dimensionMap = new HashMap<String, DimensionProperty>();
	HashMap<String, DatastoreDefinitionBase.DimensionProperty.FeatureProperty> featureMap = new HashMap<String, DatastoreDefinitionBase.DimensionProperty.FeatureProperty>();
	HashMap<String, CubeProperty.MetricProperty> metricMap = new HashMap<String, CubeProperty.MetricProperty>();

	public static class FeatureProperty extends DatastoreDefinitionBase.DimensionProperty.FeatureProperty {
		public static final String EXTRA_FEATURE_SUFFIX = "_extra";
	}

	@Override
	public void init() {

		for (DimensionProperty dimensionDefinition : this.getDimensionList()) {
			String ontology = dimensionDefinition.getOntology();

			if (!this.dimensionsWithOntologyMap.containsKey(ontology))
				this.dimensionsWithOntologyMap.put(ontology, new ArrayList<DimensionProperty>());

			ArrayList<DimensionProperty> dimensionList = this.dimensionsWithOntologyMap.get(ontology);
			dimensionList.add(dimensionDefinition);

			this.dimensionMap.put(dimensionDefinition.getName(), dimensionDefinition);
			this.dimensionMap.put(dimensionDefinition.getCode(), dimensionDefinition);

			for (DatastoreDefinitionBase.DimensionProperty.FeatureProperty featureDefinition : dimensionDefinition.getFeatureList()) {
				String name = featureDefinition.getName();
				String code = featureDefinition.getCode();

				this.featureMap.put(name, featureDefinition);
				this.featureMap.put(code, featureDefinition);
			}
		}

		for (CubeProperty cubeDefinition : this.getCubeList()) {

			this.cubeMap.put(cubeDefinition.getName(), cubeDefinition);
			this.cubeMap.put(cubeDefinition.getCode(), cubeDefinition);

			for (DatastoreDefinitionBase.CubeProperty.MetricProperty metricDefinition : cubeDefinition.getMetricList()) {
				String name = metricDefinition.getName();
				String code = metricDefinition.getCode();

				this.metricMap.put(name, metricDefinition);
				this.metricMap.put(code, metricDefinition);
			}
		}

	}

	public DimensionProperty getDimension(String key) {
		return this.dimensionMap.get(key);
	}

	public List<DimensionProperty> getDimensionsWithOntology(String ontology) {
		if (!this.dimensionsWithOntologyMap.containsKey(ontology)) return new ArrayList<DimensionProperty>();
		return this.dimensionsWithOntologyMap.get(ontology);
	}

	public DatastoreDefinitionBase.DimensionProperty.FeatureProperty getFeature(String key) {
		return this.featureMap.get(key);
	}

	public CubeProperty getCube(String key) {
		return this.cubeMap.get(key);
	}

	public CubeProperty.MetricProperty getMetric(String key) {
		return this.metricMap.get(key);
	}

}

