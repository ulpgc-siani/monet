package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
MeasureUnitDefinition
Permite definir una unidad de medida para ser usada en las métricas del análisis del negocio

*/

public  class MeasureUnitDefinition extends Definition {

	
	public static class ScaleProperty extends org.monet.metamodel.ReferenceableProperty {protected Object _label;public Object getLabel() { return _label; }public void setLabel(Object value) { _label = value; }protected Double _max;public Double getMax() { return _max; }public void setMax(Double value) { _max = value; }protected Double _factor;public Double getFactor() { return _factor; }public void setFactor(Double value) { _factor = value; }protected void copy(ScaleProperty instance) {this._label = instance._label;
this._max = instance._max;
this._factor = instance._factor;
this._code = instance._code;
this._name = instance._name;
}protected void merge(ScaleProperty child) {super.merge(child);if(child._label != null)this._label = child._label;
if(child._max != null)this._max = child._max;
if(child._factor != null)this._factor = child._factor;
}}protected LinkedHashMap<String, ScaleProperty> _scalePropertyMap = new LinkedHashMap<String, ScaleProperty>();public void addScale(ScaleProperty value) {String key = value.getName() != null ? value.getName() : value.getCode();ScaleProperty current = _scalePropertyMap.get(key);if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {ScaleProperty instance = value.getClass().newInstance();instance.copy(current);instance.setCode(value.getCode());instance.setName(value.getName());instance.merge(value);_scalePropertyMap.put(key, instance);}catch (Exception exception) {}}else current.merge(value);} else {_scalePropertyMap.put(key, value);} }public java.util.Map<String,ScaleProperty> getScaleMap() { return _scalePropertyMap; }public java.util.Collection<ScaleProperty> getScaleList() { return _scalePropertyMap.values(); }
	

	public void copy(MeasureUnitDefinition instance) {
		this._code = instance._code;
this._name = instance._name;
this._parent = instance._parent;
this._label = instance._label;
this._description = instance._description;
this._help = instance._help;

		for(ScaleProperty item : instance._scalePropertyMap.values())this.addScale(item);
this._isAbstract = instance._isAbstract; 

		
	}

	public void merge(MeasureUnitDefinition child) {
		super.merge(child);
		
		
		for(ScaleProperty item : child._scalePropertyMap.values())this.addScale(item);

		
	}

	public Class<?> getMetamodelClass() {
		return MeasureUnitDefinition.class;
	}

}

