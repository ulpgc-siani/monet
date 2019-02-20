package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
DatastoreDefinition
Representa el panel de control del sistema de información. En él se podrá tener una visión del estado actual de la organización

*/

public  class DatastoreDefinitionBase extends EntityDefinition {

	
	public static class IsExternalFed  {protected void copy(IsExternalFed instance) {}protected void merge(IsExternalFed child) {}}protected IsExternalFed _isExternalFed;public boolean isExternalFed() { return (_isExternalFed != null); }public IsExternalFed getIsExternalFed() { return _isExternalFed; }public void setIsExternalFed(boolean value) { if(value) _isExternalFed = new IsExternalFed(); else {_isExternalFed = null;}}public static class DimensionProperty extends org.monet.metamodel.ReferenceableProperty {protected Object _label;public Object getLabel() { return _label; }public void setLabel(Object value) { _label = value; }protected String _ontology;public String getOntology() { return _ontology; }public void setOntology(String value) { _ontology = value; }public static class FeatureProperty extends org.monet.metamodel.ReferenceableProperty {protected Object _label;public Object getLabel() { return _label; }public void setLabel(Object value) { _label = value; }public enum TypeEnumeration { STRING,TERM,BOOLEAN,INTEGER,REAL }protected TypeEnumeration _type;public TypeEnumeration getType() { return _type; }public void setType(TypeEnumeration value) { _type = value; }protected org.monet.metamodel.internal.Ref _source;public org.monet.metamodel.internal.Ref getSource() { return _source; }public void setSource(org.monet.metamodel.internal.Ref value) { _source = value; }protected void copy(FeatureProperty instance) {this._label = instance._label;
this._type = instance._type;
this._source = instance._source;
this._code = instance._code;
this._name = instance._name;
}protected void merge(FeatureProperty child) {super.merge(child);if(child._label != null)this._label = child._label;
if(child._type != null)this._type = child._type;
if(child._source != null)this._source = child._source;
}}protected LinkedHashMap<String, FeatureProperty> _featurePropertyMap = new LinkedHashMap<String, FeatureProperty>();public void addFeature(FeatureProperty value) {String key = value.getName() != null ? value.getName() : value.getCode();FeatureProperty current = _featurePropertyMap.get(key);if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {FeatureProperty instance = value.getClass().newInstance();instance.copy(current);instance.setCode(value.getCode());instance.setName(value.getName());instance.merge(value);_featurePropertyMap.put(key, instance);}catch (Exception exception) {}}else current.merge(value);} else {_featurePropertyMap.put(key, value);} }public java.util.Map<String,FeatureProperty> getFeatureMap() { return _featurePropertyMap; }public java.util.Collection<FeatureProperty> getFeatureList() { return _featurePropertyMap.values(); }protected void copy(DimensionProperty instance) {this._label = instance._label;
this._ontology = instance._ontology;
this._code = instance._code;
this._name = instance._name;
for(FeatureProperty item : instance._featurePropertyMap.values())this.addFeature(item);
}protected void merge(DimensionProperty child) {super.merge(child);if(child._label != null)this._label = child._label;
if(child._ontology != null)this._ontology = child._ontology;
for(FeatureProperty item : child._featurePropertyMap.values())this.addFeature(item);
}}protected LinkedHashMap<String, DimensionProperty> _dimensionPropertyMap = new LinkedHashMap<String, DimensionProperty>();public void addDimension(DimensionProperty value) {String key = value.getName() != null ? value.getName() : value.getCode();DimensionProperty current = _dimensionPropertyMap.get(key);if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {DimensionProperty instance = value.getClass().newInstance();instance.copy(current);instance.setCode(value.getCode());instance.setName(value.getName());instance.merge(value);_dimensionPropertyMap.put(key, instance);}catch (Exception exception) {}}else current.merge(value);} else {_dimensionPropertyMap.put(key, value);} }public java.util.Map<String,DimensionProperty> getDimensionMap() { return _dimensionPropertyMap; }public java.util.Collection<DimensionProperty> getDimensionList() { return _dimensionPropertyMap.values(); }public static class CubeProperty extends org.monet.metamodel.ReferenceableProperty {protected Object _label;public Object getLabel() { return _label; }public void setLabel(Object value) { _label = value; }public enum ResolutionEnumeration { YEARS,MONTHS,DAYS,HOURS,MINUTES,SECONDS }protected ResolutionEnumeration _resolution;public ResolutionEnumeration getResolution() { return _resolution; }public void setResolution(ResolutionEnumeration value) { _resolution = value; }protected ArrayList<org.monet.metamodel.internal.Ref> _use = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getUse() { return _use; }public void setUse(ArrayList<org.monet.metamodel.internal.Ref> value) { _use = value; }public static class MetricProperty extends org.monet.metamodel.ReferenceableProperty {protected Object _label;public Object getLabel() { return _label; }public void setLabel(Object value) { _label = value; }protected org.monet.metamodel.internal.Ref _scale;public org.monet.metamodel.internal.Ref getScale() { return _scale; }public void setScale(org.monet.metamodel.internal.Ref value) { _scale = value; }protected void copy(MetricProperty instance) {this._label = instance._label;
this._scale = instance._scale;
this._code = instance._code;
this._name = instance._name;
}protected void merge(MetricProperty child) {super.merge(child);if(child._label != null)this._label = child._label;
if(child._scale != null)this._scale = child._scale;
}}protected LinkedHashMap<String, MetricProperty> _metricPropertyMap = new LinkedHashMap<String, MetricProperty>();public void addMetric(MetricProperty value) {String key = value.getName() != null ? value.getName() : value.getCode();MetricProperty current = _metricPropertyMap.get(key);if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {MetricProperty instance = value.getClass().newInstance();instance.copy(current);instance.setCode(value.getCode());instance.setName(value.getName());instance.merge(value);_metricPropertyMap.put(key, instance);}catch (Exception exception) {}}else current.merge(value);} else {_metricPropertyMap.put(key, value);} }public java.util.Map<String,MetricProperty> getMetricMap() { return _metricPropertyMap; }public java.util.Collection<MetricProperty> getMetricList() { return _metricPropertyMap.values(); }protected void copy(CubeProperty instance) {this._label = instance._label;
this._resolution = instance._resolution;
this._use.addAll(instance._use);
this._code = instance._code;
this._name = instance._name;
for(MetricProperty item : instance._metricPropertyMap.values())this.addMetric(item);
}protected void merge(CubeProperty child) {super.merge(child);if(child._label != null)this._label = child._label;
if(child._resolution != null)this._resolution = child._resolution;
if(child._use != null)this._use.addAll(child._use);
for(MetricProperty item : child._metricPropertyMap.values())this.addMetric(item);
}}protected LinkedHashMap<String, CubeProperty> _cubePropertyMap = new LinkedHashMap<String, CubeProperty>();public void addCube(CubeProperty value) {String key = value.getName() != null ? value.getName() : value.getCode();CubeProperty current = _cubePropertyMap.get(key);if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {CubeProperty instance = value.getClass().newInstance();instance.copy(current);instance.setCode(value.getCode());instance.setName(value.getName());instance.merge(value);_cubePropertyMap.put(key, instance);}catch (Exception exception) {}}else current.merge(value);} else {_cubePropertyMap.put(key, value);} }public java.util.Map<String,CubeProperty> getCubeMap() { return _cubePropertyMap; }public java.util.Collection<CubeProperty> getCubeList() { return _cubePropertyMap.values(); }
	

	public void copy(DatastoreDefinitionBase instance) {
		this._code = instance._code;
this._name = instance._name;
this._parent = instance._parent;
this._label = instance._label;
this._description = instance._description;
this._help = instance._help;

		this._isExternalFed = instance._isExternalFed; 
for(DimensionProperty item : instance._dimensionPropertyMap.values())this.addDimension(item);
for(CubeProperty item : instance._cubePropertyMap.values())this.addCube(item);
this._isAbstract = instance._isAbstract; 

		
	}

	public void merge(DatastoreDefinitionBase child) {
		super.merge(child);
		
		
		if(_isExternalFed == null) _isExternalFed = child._isExternalFed; else if (child._isExternalFed != null) {_isExternalFed.merge(child._isExternalFed);}
for(DimensionProperty item : child._dimensionPropertyMap.values())this.addDimension(item);
for(CubeProperty item : child._cubePropertyMap.values())this.addCube(item);

		
	}

	public Class<?> getMetamodelClass() {
		return DatastoreDefinitionBase.class;
	}

}

