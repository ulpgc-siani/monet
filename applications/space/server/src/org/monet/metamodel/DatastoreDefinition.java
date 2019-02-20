package org.monet.metamodel;

import org.monet.metamodel.DatastoreDefinitionBase.CubeProperty.MetricProperty;
import org.monet.metamodel.interfaces.IsInitiable;
import org.monet.space.kernel.model.DefinitionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatastoreDefinition extends DatastoreDefinitionBase implements IsInitiable {
	HashMap<String, ArrayList<DimensionProperty>> dimensionsWithOntologyMap = new HashMap<String, ArrayList<DimensionProperty>>();
	HashMap<String, CubeProperty> cubeMap = new HashMap<String, CubeProperty>();
	HashMap<String, DimensionProperty> dimensionMap = new HashMap<String, DimensionProperty>();
	HashMap<String, DimensionProperty.FeatureProperty> featureMap = new HashMap<String, DimensionProperty.FeatureProperty>();
	HashMap<String, MetricProperty> metricMap = new HashMap<String, MetricProperty>();

	@Override
	public DefinitionType getType() {
		return DefinitionType.datastore;
	}

	public static class FeatureProperty extends DimensionProperty.FeatureProperty {
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

			for (DimensionProperty.FeatureProperty featureDefinition : dimensionDefinition.getFeatureList()) {
				String name = featureDefinition.getName();
				String code = featureDefinition.getCode();

				this.featureMap.put(name, featureDefinition);
				this.featureMap.put(code, featureDefinition);
			}
		}

		for (CubeProperty cubeDefinition : this.getCubeList()) {

			this.cubeMap.put(cubeDefinition.getName(), cubeDefinition);
			this.cubeMap.put(cubeDefinition.getCode(), cubeDefinition);

			for (MetricProperty metricDefinition : cubeDefinition.getMetricList()) {
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

	public DimensionProperty.FeatureProperty getFeature(String key) {
		return this.featureMap.get(key);
	}

	public CubeProperty getCube(String key) {
		return this.cubeMap.get(key);
	}

	public MetricProperty getMetric(String key) {
		return this.metricMap.get(key);
	}

}

