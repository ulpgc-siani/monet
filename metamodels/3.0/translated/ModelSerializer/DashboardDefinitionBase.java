package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
DashboardDefinition
Representa el panel de control del sistema de información. En él se podrá tener una visión del estado actual de la organización

*/

public  class DashboardDefinitionBase extends EntityDefinition {

	protected org.monet.metamodel.internal.Ref _use;public org.monet.metamodel.internal.Ref getUse() { return _use; }public void setUse(org.monet.metamodel.internal.Ref value) { _use = value; }
	public static class ForProperty  {protected ArrayList<org.monet.metamodel.internal.Ref> _role = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getRole() { return _role; }public void setRole(ArrayList<org.monet.metamodel.internal.Ref> value) { _role = value; }protected void copy(ForProperty instance) {this._role.addAll(instance._role);
}protected void merge(ForProperty child) {if(child._role != null)this._role.addAll(child._role);
}}protected ForProperty _forProperty;public ForProperty getFor() { return _forProperty; }public void setFor(ForProperty value) { if(_forProperty!=null) _forProperty.merge(value); else {_forProperty = value;} }public static class IndicatorProperty extends org.monet.metamodel.ReferenceableProperty {protected Object _label;public Object getLabel() { return _label; }public void setLabel(Object value) { _label = value; }public enum RangeEnumeration { POSITIVE,ALL,NEGATIVE }protected RangeEnumeration _range;public RangeEnumeration getRange() { return _range; }public void setRange(RangeEnumeration value) { _range = value; }protected org.monet.metamodel.internal.Ref _measureUnit;public org.monet.metamodel.internal.Ref getMeasureUnit() { return _measureUnit; }public void setMeasureUnit(org.monet.metamodel.internal.Ref value) { _measureUnit = value; }public static class LevelProperty  {public static class PrimaryProperty  {protected org.monet.metamodel.internal.Ref _metric;public org.monet.metamodel.internal.Ref getMetric() { return _metric; }public void setMetric(org.monet.metamodel.internal.Ref value) { _metric = value; }public enum OperatorEnumeration { SUM,COUNT,AVERAGE,DEVIATION,TIME_INTEGRATION }protected OperatorEnumeration _operator;public OperatorEnumeration getOperator() { return _operator; }public void setOperator(OperatorEnumeration value) { _operator = value; }public enum InterpolationEnumeration { LEFT,RIGHT,NEAREST,LINEAR,NULL }protected InterpolationEnumeration _interpolation;public InterpolationEnumeration getInterpolation() { return _interpolation; }public void setInterpolation(InterpolationEnumeration value) { _interpolation = value; }protected void copy(PrimaryProperty instance) {this._metric = instance._metric;
this._operator = instance._operator;
this._interpolation = instance._interpolation;
}protected void merge(PrimaryProperty child) {if(child._metric != null)this._metric = child._metric;
if(child._operator != null)this._operator = child._operator;
if(child._interpolation != null)this._interpolation = child._interpolation;
}}protected PrimaryProperty _primaryProperty;public PrimaryProperty getPrimary() { return _primaryProperty; }public void setPrimary(PrimaryProperty value) { if(_primaryProperty!=null) _primaryProperty.merge(value); else {_primaryProperty = value;} }public static class SecondaryProperty  {protected ArrayList<org.monet.metamodel.internal.Ref> _use = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getUse() { return _use; }public void setUse(ArrayList<org.monet.metamodel.internal.Ref> value) { _use = value; }protected void copy(SecondaryProperty instance) {this._use.addAll(instance._use);
}protected void merge(SecondaryProperty child) {if(child._use != null)this._use.addAll(child._use);
}}protected SecondaryProperty _secondaryProperty;public SecondaryProperty getSecondary() { return _secondaryProperty; }public void setSecondary(SecondaryProperty value) { if(_secondaryProperty!=null) _secondaryProperty.merge(value); else {_secondaryProperty = value;} }protected void copy(LevelProperty instance) {this._primaryProperty = instance._primaryProperty; 
this._secondaryProperty = instance._secondaryProperty; 
}protected void merge(LevelProperty child) {if(_primaryProperty == null) _primaryProperty = child._primaryProperty; else if (child._primaryProperty != null) {_primaryProperty.merge(child._primaryProperty);}
if(_secondaryProperty == null) _secondaryProperty = child._secondaryProperty; else if (child._secondaryProperty != null) {_secondaryProperty.merge(child._secondaryProperty);}
}}protected LevelProperty _levelProperty;public LevelProperty getLevel() { return _levelProperty; }public void setLevel(LevelProperty value) { if(_levelProperty!=null) _levelProperty.merge(value); else {_levelProperty = value;} }protected void copy(IndicatorProperty instance) {this._label = instance._label;
this._range = instance._range;
this._measureUnit = instance._measureUnit;
this._code = instance._code;
this._name = instance._name;
this._levelProperty = instance._levelProperty; 
}protected void merge(IndicatorProperty child) {super.merge(child);if(child._label != null)this._label = child._label;
if(child._range != null)this._range = child._range;
if(child._measureUnit != null)this._measureUnit = child._measureUnit;
if(_levelProperty == null) _levelProperty = child._levelProperty; else if (child._levelProperty != null) {_levelProperty.merge(child._levelProperty);}
}}protected LinkedHashMap<String, IndicatorProperty> _indicatorPropertyMap = new LinkedHashMap<String, IndicatorProperty>();public void addIndicator(IndicatorProperty value) {String key = value.getName() != null ? value.getName() : value.getCode();IndicatorProperty current = _indicatorPropertyMap.get(key);if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {IndicatorProperty instance = value.getClass().newInstance();instance.copy(current);instance.setCode(value.getCode());instance.setName(value.getName());instance.merge(value);_indicatorPropertyMap.put(key, instance);}catch (Exception exception) {}}else current.merge(value);} else {_indicatorPropertyMap.put(key, value);} }public java.util.Map<String,IndicatorProperty> getIndicatorMap() { return _indicatorPropertyMap; }public java.util.Collection<IndicatorProperty> getIndicatorList() { return _indicatorPropertyMap.values(); }public static class TaxonomyProperty extends org.monet.metamodel.ReferenceableProperty {protected Object _label;public Object getLabel() { return _label; }public void setLabel(Object value) { _label = value; }protected String _ontology;public String getOntology() { return _ontology; }public void setOntology(String value) { _ontology = value; }protected org.monet.metamodel.internal.Ref _feature;public org.monet.metamodel.internal.Ref getFeature() { return _feature; }public void setFeature(org.monet.metamodel.internal.Ref value) { _feature = value; }public static class ExplicitProperty  {protected LinkedHashMap<String, CategoryProperty> _categoryPropertyMap = new LinkedHashMap<String, CategoryProperty>();public void addCategoryProperty(CategoryProperty value) {String key = value.getName() != null ? value.getName() : value.getCode();CategoryProperty current = _categoryPropertyMap.get(key);if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {CategoryProperty instance = value.getClass().newInstance();instance.copy(current);instance.setCode(value.getCode());instance.setName(value.getName());instance.merge(value);_categoryPropertyMap.put(key, instance);}catch (Exception exception) {}}else current.merge(value);} else {_categoryPropertyMap.put(key, value);} }public java.util.Map<String,CategoryProperty> getCategoryPropertyMap() { return _categoryPropertyMap; }public java.util.Collection<CategoryProperty> getCategoryPropertyList() { return _categoryPropertyMap.values(); }protected void copy(ExplicitProperty instance) {for(CategoryProperty item : instance._categoryPropertyMap.values())this.addCategoryProperty(item);}protected void merge(ExplicitProperty child) {for(CategoryProperty item : child._categoryPropertyMap.values())this.addCategoryProperty(item);}}protected ExplicitProperty _explicitProperty;public ExplicitProperty getExplicit() { return _explicitProperty; }public void setExplicit(ExplicitProperty value) { if(_explicitProperty!=null) _explicitProperty.merge(value); else {_explicitProperty = value;} }public static class ImplicitProperty  {protected void copy(ImplicitProperty instance) {}protected void merge(ImplicitProperty child) {}}protected ImplicitProperty _implicitProperty;public ImplicitProperty getImplicit() { return _implicitProperty; }public void setImplicit(ImplicitProperty value) { if(_implicitProperty!=null) _implicitProperty.merge(value); else {_implicitProperty = value;} }protected void copy(TaxonomyProperty instance) {this._label = instance._label;
this._ontology = instance._ontology;
this._feature = instance._feature;
this._code = instance._code;
this._name = instance._name;
this._explicitProperty = instance._explicitProperty; 
this._implicitProperty = instance._implicitProperty; 
}protected void merge(TaxonomyProperty child) {super.merge(child);if(child._label != null)this._label = child._label;
if(child._ontology != null)this._ontology = child._ontology;
if(child._feature != null)this._feature = child._feature;
if(_explicitProperty == null) _explicitProperty = child._explicitProperty; else if (child._explicitProperty != null) {_explicitProperty.merge(child._explicitProperty);}
if(_implicitProperty == null) _implicitProperty = child._implicitProperty; else if (child._implicitProperty != null) {_implicitProperty.merge(child._implicitProperty);}
}}protected LinkedHashMap<String, TaxonomyProperty> _taxonomyPropertyMap = new LinkedHashMap<String, TaxonomyProperty>();public void addTaxonomy(TaxonomyProperty value) {String key = value.getName() != null ? value.getName() : value.getCode();TaxonomyProperty current = _taxonomyPropertyMap.get(key);if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {TaxonomyProperty instance = value.getClass().newInstance();instance.copy(current);instance.setCode(value.getCode());instance.setName(value.getName());instance.merge(value);_taxonomyPropertyMap.put(key, instance);}catch (Exception exception) {}}else current.merge(value);} else {_taxonomyPropertyMap.put(key, value);} }public java.util.Map<String,TaxonomyProperty> getTaxonomyMap() { return _taxonomyPropertyMap; }public java.util.Collection<TaxonomyProperty> getTaxonomyList() { return _taxonomyPropertyMap.values(); }public static class DashboardViewProperty extends org.monet.metamodel.ViewProperty {public static class ShowProperty  {public static class OlapProperty  {protected ArrayList<FolderProperty> _folderPropertyList = new ArrayList<FolderProperty>();public void addFolderProperty(FolderProperty value) { _folderPropertyList.add(value); }public ArrayList<FolderProperty> getFolderPropertyList() { return _folderPropertyList; }protected void copy(OlapProperty instance) {_folderPropertyList.addAll(instance._folderPropertyList);}protected void merge(OlapProperty child) {_folderPropertyList.addAll(child._folderPropertyList);}}protected OlapProperty _olapProperty;public OlapProperty getOlap() { return _olapProperty; }public void setOlap(OlapProperty value) { if(_olapProperty!=null) _olapProperty.merge(value); else {_olapProperty = value;} }public static class ReportProperty  {protected void copy(ReportProperty instance) {}protected void merge(ReportProperty child) {}}protected ReportProperty _reportProperty;public ReportProperty getReport() { return _reportProperty; }public void setReport(ReportProperty value) { if(_reportProperty!=null) _reportProperty.merge(value); else {_reportProperty = value;} }protected void copy(ShowProperty instance) {this._olapProperty = instance._olapProperty; 
this._reportProperty = instance._reportProperty; 
}protected void merge(ShowProperty child) {if(_olapProperty == null) _olapProperty = child._olapProperty; else if (child._olapProperty != null) {_olapProperty.merge(child._olapProperty);}
if(_reportProperty == null) _reportProperty = child._reportProperty; else if (child._reportProperty != null) {_reportProperty.merge(child._reportProperty);}
}}protected ShowProperty _showProperty;public ShowProperty getShow() { return _showProperty; }public void setShow(ShowProperty value) { if(_showProperty!=null) _showProperty.merge(value); else {_showProperty = value;} }protected void copy(DashboardViewProperty instance) {this._code = instance._code;
this._name = instance._name;
this._showProperty = instance._showProperty; 
}protected void merge(DashboardViewProperty child) {super.merge(child);if(_showProperty == null) _showProperty = child._showProperty; else if (child._showProperty != null) {_showProperty.merge(child._showProperty);}
}}protected LinkedHashMap<String, DashboardViewProperty> _dashboardViewPropertyMap = new LinkedHashMap<String, DashboardViewProperty>();public void addView(DashboardViewProperty value) {String key = value.getName() != null ? value.getName() : value.getCode();DashboardViewProperty current = _dashboardViewPropertyMap.get(key);if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {DashboardViewProperty instance = value.getClass().newInstance();instance.copy(current);instance.setCode(value.getCode());instance.setName(value.getName());instance.merge(value);_dashboardViewPropertyMap.put(key, instance);}catch (Exception exception) {}}else current.merge(value);} else {_dashboardViewPropertyMap.put(key, value);} }public java.util.Map<String,DashboardViewProperty> getViewMap() { return _dashboardViewPropertyMap; }public java.util.Collection<DashboardViewProperty> getViewList() { return _dashboardViewPropertyMap.values(); }
	

	public void copy(DashboardDefinitionBase instance) {
		this._use = instance._use;
this._code = instance._code;
this._name = instance._name;
this._parent = instance._parent;
this._label = instance._label;
this._description = instance._description;
this._help = instance._help;

		this._forProperty = instance._forProperty; 
for(IndicatorProperty item : instance._indicatorPropertyMap.values())this.addIndicator(item);
for(TaxonomyProperty item : instance._taxonomyPropertyMap.values())this.addTaxonomy(item);
for(DashboardViewProperty item : instance._dashboardViewPropertyMap.values())this.addView(item);
this._isAbstract = instance._isAbstract; 

		
	}

	public void merge(DashboardDefinitionBase child) {
		super.merge(child);
		
		if(child._use != null)this._use = child._use;

		if(_forProperty == null) _forProperty = child._forProperty; else if (child._forProperty != null) {_forProperty.merge(child._forProperty);}
for(IndicatorProperty item : child._indicatorPropertyMap.values())this.addIndicator(item);
for(TaxonomyProperty item : child._taxonomyPropertyMap.values())this.addTaxonomy(item);
for(DashboardViewProperty item : child._dashboardViewPropertyMap.values())this.addView(item);

		
	}

	public Class<?> getMetamodelClass() {
		return DashboardDefinitionBase.class;
	}

}

