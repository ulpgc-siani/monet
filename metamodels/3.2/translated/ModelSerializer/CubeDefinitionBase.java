package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
CubeDefinition
Un cubo es un repositorio de hechos en un esquema multidimensional

*/

public  class CubeDefinitionBase extends EntityDefinition {

	public enum ResolutionEnumeration { MONTHS,DAYS,HOURS,MINUTES,SECONDS }protected ResolutionEnumeration _resolution;public ResolutionEnumeration getResolution() { return _resolution; }public void setResolution(ResolutionEnumeration value) { _resolution = value; }protected ArrayList<org.monet.metamodel.internal.Ref> _dimension = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getDimension() { return _dimension; }public void setDimension(ArrayList<org.monet.metamodel.internal.Ref> value) { _dimension = value; }
	public static class MeasureProperty extends org.monet.metamodel.ReferenceableProperty {protected Object _label;public Object getLabel() { return _label; }public void setLabel(Object value) { _label = value; }protected org.monet.metamodel.internal.Ref _metric;public org.monet.metamodel.internal.Ref getMetric() { return _metric; }public void setMetric(org.monet.metamodel.internal.Ref value) { _metric = value; }protected void copy(MeasureProperty instance) {this._label = instance._label;
this._metric = instance._metric;
this._code = instance._code;
this._name = instance._name;
}protected void merge(MeasureProperty child) {super.merge(child);if(child._label != null)this._label = child._label;
if(child._metric != null)this._metric = child._metric;
}}protected LinkedHashMap<String, MeasureProperty> _measurePropertyMap = new LinkedHashMap<String, MeasureProperty>();public void addMeasure(MeasureProperty value) {String key = value.getName() != null ? value.getName() : value.getCode();MeasureProperty current = _measurePropertyMap.get(key);if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {MeasureProperty instance = value.getClass().newInstance();instance.copy(current);instance.setCode(value.getCode());instance.setName(value.getName());instance.merge(value);_measurePropertyMap.put(key, instance);}catch (Exception exception) {}}else current.merge(value);} else {_measurePropertyMap.put(key, value);} }public java.util.Map<String,MeasureProperty> getMeasureMap() { return _measurePropertyMap; }public java.util.Collection<MeasureProperty> getMeasureList() { return _measurePropertyMap.values(); }public static class IndicatorProperty extends org.monet.metamodel.ReferenceableProperty {protected Object _label;public Object getLabel() { return _label; }public void setLabel(Object value) { _label = value; }protected org.monet.metamodel.internal.Ref _measure;public org.monet.metamodel.internal.Ref getMeasure() { return _measure; }public void setMeasure(org.monet.metamodel.internal.Ref value) { _measure = value; }protected org.monet.metamodel.internal.Ref _metric;public org.monet.metamodel.internal.Ref getMetric() { return _metric; }public void setMetric(org.monet.metamodel.internal.Ref value) { _metric = value; }public enum InterpolationEnumeration { LEFT_STEP,MIDDLE_STEP,RIGHT_STEP,LINEAR }protected InterpolationEnumeration _interpolation;public InterpolationEnumeration getInterpolation() { return _interpolation; }public void setInterpolation(InterpolationEnumeration value) { _interpolation = value; }public enum OperatorEnumeration { SUM,COUNT,AVERAGE,DEVIATION,INTEGRAL }protected OperatorEnumeration _operator;public OperatorEnumeration getOperator() { return _operator; }public void setOperator(OperatorEnumeration value) { _operator = value; }public enum ChartEnumeration { BOTTOM,BASE,TOP }protected ChartEnumeration _chart;public ChartEnumeration getChart() { return _chart; }public void setChart(ChartEnumeration value) { _chart = value; }public enum AlertEnumeration { INCREASE,DECREASE }protected AlertEnumeration _alert;public AlertEnumeration getAlert() { return _alert; }public void setAlert(AlertEnumeration value) { _alert = value; }protected void copy(IndicatorProperty instance) {this._label = instance._label;
this._measure = instance._measure;
this._metric = instance._metric;
this._interpolation = instance._interpolation;
this._operator = instance._operator;
this._chart = instance._chart;
this._alert = instance._alert;
this._code = instance._code;
this._name = instance._name;
}protected void merge(IndicatorProperty child) {super.merge(child);if(child._label != null)this._label = child._label;
if(child._measure != null)this._measure = child._measure;
if(child._metric != null)this._metric = child._metric;
if(child._interpolation != null)this._interpolation = child._interpolation;
if(child._operator != null)this._operator = child._operator;
if(child._chart != null)this._chart = child._chart;
if(child._alert != null)this._alert = child._alert;
}}protected LinkedHashMap<String, IndicatorProperty> _indicatorPropertyMap = new LinkedHashMap<String, IndicatorProperty>();public void addIndicator(IndicatorProperty value) {String key = value.getName() != null ? value.getName() : value.getCode();IndicatorProperty current = _indicatorPropertyMap.get(key);if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {IndicatorProperty instance = value.getClass().newInstance();instance.copy(current);instance.setCode(value.getCode());instance.setName(value.getName());instance.merge(value);_indicatorPropertyMap.put(key, instance);}catch (Exception exception) {}}else current.merge(value);} else {_indicatorPropertyMap.put(key, value);} }public java.util.Map<String,IndicatorProperty> getIndicatorMap() { return _indicatorPropertyMap; }public java.util.Collection<IndicatorProperty> getIndicatorList() { return _indicatorPropertyMap.values(); }public static class ViewProperty extends org.monet.metamodel.ViewProperty {protected Object _label;public Object getLabel() { return _label; }public void setLabel(Object value) { _label = value; }public static class IsDefault  {protected void copy(IsDefault instance) {}protected void merge(IsDefault child) {}}protected IsDefault _isDefault;public boolean isDefault() { return (_isDefault != null); }public IsDefault getIsDefault() { return _isDefault; }public void setIsDefault(boolean value) { if(value) _isDefault = new IsDefault(); else {_isDefault = null;}}public static class ForProperty  {protected org.monet.metamodel.internal.Ref _role;public org.monet.metamodel.internal.Ref getRole() { return _role; }public void setRole(org.monet.metamodel.internal.Ref value) { _role = value; }protected void copy(ForProperty instance) {this._role = instance._role;
}protected void merge(ForProperty child) {if(child._role != null)this._role = child._role;
}}protected ForProperty _forProperty;public ForProperty getFor() { return _forProperty; }public void setFor(ForProperty value) { if(_forProperty!=null) _forProperty.merge(value); else {_forProperty = value;} }public static class ShowProperty  {protected ArrayList<org.monet.metamodel.internal.Ref> _indicator = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getIndicator() { return _indicator; }public void setIndicator(ArrayList<org.monet.metamodel.internal.Ref> value) { _indicator = value; }public enum ScaleEnumeration { YEAR,MONTH,DAY,HOUR,MINUTE,SECOND }protected ScaleEnumeration _scale;public ScaleEnumeration getScale() { return _scale; }public void setScale(ScaleEnumeration value) { _scale = value; }protected void copy(ShowProperty instance) {this._indicator.addAll(instance._indicator);
this._scale = instance._scale;
}protected void merge(ShowProperty child) {if(child._indicator != null)this._indicator.addAll(child._indicator);
if(child._scale != null)this._scale = child._scale;
}}protected ShowProperty _showProperty;public ShowProperty getShow() { return _showProperty; }public void setShow(ShowProperty value) { if(_showProperty!=null) _showProperty.merge(value); else {_showProperty = value;} }protected void copy(ViewProperty instance) {this._label = instance._label;
this._code = instance._code;
this._name = instance._name;
this._isDefault = instance._isDefault; 
this._forProperty = instance._forProperty; 
this._showProperty = instance._showProperty; 
}protected void merge(ViewProperty child) {super.merge(child);if(child._label != null)this._label = child._label;
if(_isDefault == null) _isDefault = child._isDefault; else if (child._isDefault != null) {_isDefault.merge(child._isDefault);}
if(_forProperty == null) _forProperty = child._forProperty; else if (child._forProperty != null) {_forProperty.merge(child._forProperty);}
if(_showProperty == null) _showProperty = child._showProperty; else if (child._showProperty != null) {_showProperty.merge(child._showProperty);}
}}protected LinkedHashMap<String, ViewProperty> _viewPropertyMap = new LinkedHashMap<String, ViewProperty>();public void addView(ViewProperty value) {String key = value.getName() != null ? value.getName() : value.getCode();ViewProperty current = _viewPropertyMap.get(key);if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {ViewProperty instance = value.getClass().newInstance();instance.copy(current);instance.setCode(value.getCode());instance.setName(value.getName());instance.merge(value);_viewPropertyMap.put(key, instance);}catch (Exception exception) {}}else current.merge(value);} else {_viewPropertyMap.put(key, value);} }public java.util.Map<String,ViewProperty> getViewMap() { return _viewPropertyMap; }public java.util.Collection<ViewProperty> getViewList() { return _viewPropertyMap.values(); }
	

	public void copy(CubeDefinitionBase instance) {
		this._resolution = instance._resolution;
this._dimension.addAll(instance._dimension);
this._code = instance._code;
this._name = instance._name;
this._parent = instance._parent;
this._label = instance._label;
this._description = instance._description;
this._help = instance._help;

		for(MeasureProperty item : instance._measurePropertyMap.values())this.addMeasure(item);
for(IndicatorProperty item : instance._indicatorPropertyMap.values())this.addIndicator(item);
for(ViewProperty item : instance._viewPropertyMap.values())this.addView(item);
this._isAbstract = instance._isAbstract; 

		
	}

	public void merge(CubeDefinitionBase child) {
		super.merge(child);
		
		if(child._resolution != null)this._resolution = child._resolution;
if(child._dimension != null)this._dimension.addAll(child._dimension);

		for(MeasureProperty item : child._measurePropertyMap.values())this.addMeasure(item);
for(IndicatorProperty item : child._indicatorPropertyMap.values())this.addIndicator(item);
for(ViewProperty item : child._viewPropertyMap.values())this.addView(item);

		
	}

	public Class<?> getMetamodelClass() {
		return CubeDefinitionBase.class;
	}

}

