package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
DimensionDefinition
Permite definir una dimensión de análisis de negocio

*/

public  class DimensionDefinition extends EntityDefinition {

	
	public static class FeatureProperty extends org.monet.metamodel.ReferenceableProperty {protected Object _label;public Object getLabel() { return _label; }public void setLabel(Object value) { _label = value; }public enum TypeEnumeration { STRING,TERM,BOOLEAN,INTEGER,REAL,DATE }protected TypeEnumeration _type;public TypeEnumeration getType() { return _type; }public void setType(TypeEnumeration value) { _type = value; }protected org.monet.metamodel.internal.Ref _source;public org.monet.metamodel.internal.Ref getSource() { return _source; }public void setSource(org.monet.metamodel.internal.Ref value) { _source = value; }protected org.monet.metamodel.internal.Ref _metric;public org.monet.metamodel.internal.Ref getMetric() { return _metric; }public void setMetric(org.monet.metamodel.internal.Ref value) { _metric = value; }protected void copy(FeatureProperty instance) {this._label = instance._label;
this._type = instance._type;
this._source = instance._source;
this._metric = instance._metric;
this._code = instance._code;
this._name = instance._name;
}protected void merge(FeatureProperty child) {super.merge(child);if(child._label != null)this._label = child._label;
if(child._type != null)this._type = child._type;
if(child._source != null)this._source = child._source;
if(child._metric != null)this._metric = child._metric;
}}protected LinkedHashMap<String, FeatureProperty> _featurePropertyMap = new LinkedHashMap<String, FeatureProperty>();public void addFeature(FeatureProperty value) {String key = value.getName() != null ? value.getName() : value.getCode();FeatureProperty current = _featurePropertyMap.get(key);if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {FeatureProperty instance = value.getClass().newInstance();instance.copy(current);instance.setCode(value.getCode());instance.setName(value.getName());instance.merge(value);_featurePropertyMap.put(key, instance);}catch (Exception exception) {}}else current.merge(value);} else {_featurePropertyMap.put(key, value);} }public java.util.Map<String,FeatureProperty> getFeatureMap() { return _featurePropertyMap; }public java.util.Collection<FeatureProperty> getFeatureList() { return _featurePropertyMap.values(); }public static class TaxonomyProperty extends org.monet.metamodel.ReferenceableProperty {protected Object _label;public Object getLabel() { return _label; }public void setLabel(Object value) { _label = value; }protected LinkedHashMap<String, CategoryProperty> _categoryPropertyMap = new LinkedHashMap<String, CategoryProperty>();public void addCategoryProperty(CategoryProperty value) {String key = value.getName() != null ? value.getName() : value.getCode();CategoryProperty current = _categoryPropertyMap.get(key);if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {CategoryProperty instance = value.getClass().newInstance();instance.copy(current);instance.setCode(value.getCode());instance.setName(value.getName());instance.merge(value);_categoryPropertyMap.put(key, instance);}catch (Exception exception) {}}else current.merge(value);} else {_categoryPropertyMap.put(key, value);} }public java.util.Map<String,CategoryProperty> getCategoryPropertyMap() { return _categoryPropertyMap; }public java.util.Collection<CategoryProperty> getCategoryPropertyList() { return _categoryPropertyMap.values(); }protected ArrayList<TaxonomyRuleProperty> _taxonomyRulePropertyList = new ArrayList<TaxonomyRuleProperty>();public void addTaxonomyRuleProperty(TaxonomyRuleProperty value) { _taxonomyRulePropertyList.add(value); }public ArrayList<TaxonomyRuleProperty> getTaxonomyRulePropertyList() { return _taxonomyRulePropertyList; }protected void copy(TaxonomyProperty instance) {this._label = instance._label;
this._code = instance._code;
this._name = instance._name;
for(CategoryProperty item : instance._categoryPropertyMap.values())this.addCategoryProperty(item);_taxonomyRulePropertyList.addAll(instance._taxonomyRulePropertyList);}protected void merge(TaxonomyProperty child) {super.merge(child);if(child._label != null)this._label = child._label;
for(CategoryProperty item : child._categoryPropertyMap.values())this.addCategoryProperty(item);_taxonomyRulePropertyList.addAll(child._taxonomyRulePropertyList);}}protected LinkedHashMap<String, TaxonomyProperty> _taxonomyPropertyMap = new LinkedHashMap<String, TaxonomyProperty>();public void addTaxonomy(TaxonomyProperty value) {String key = value.getName() != null ? value.getName() : value.getCode();TaxonomyProperty current = _taxonomyPropertyMap.get(key);if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {TaxonomyProperty instance = value.getClass().newInstance();instance.copy(current);instance.setCode(value.getCode());instance.setName(value.getName());instance.merge(value);_taxonomyPropertyMap.put(key, instance);}catch (Exception exception) {}}else current.merge(value);} else {_taxonomyPropertyMap.put(key, value);} }public java.util.Map<String,TaxonomyProperty> getTaxonomyMap() { return _taxonomyPropertyMap; }public java.util.Collection<TaxonomyProperty> getTaxonomyList() { return _taxonomyPropertyMap.values(); }
	

	public void copy(DimensionDefinition instance) {
		this._code = instance._code;
this._name = instance._name;
this._parent = instance._parent;
this._label = instance._label;
this._description = instance._description;
this._help = instance._help;

		for(FeatureProperty item : instance._featurePropertyMap.values())this.addFeature(item);
for(TaxonomyProperty item : instance._taxonomyPropertyMap.values())this.addTaxonomy(item);
this._isAbstract = instance._isAbstract; 

		
	}

	public void merge(DimensionDefinition child) {
		super.merge(child);
		
		
		for(FeatureProperty item : child._featurePropertyMap.values())this.addFeature(item);
for(TaxonomyProperty item : child._taxonomyPropertyMap.values())this.addTaxonomy(item);

		
	}

	public Class<?> getMetamodelClass() {
		return DimensionDefinition.class;
	}

}

