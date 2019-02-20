package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
KpiDefinition


*/

public  class KpiDefinition extends EntityDefinition {

	
	public static class ParameterProperty extends org.monet.metamodel.ReferenceableProperty {protected Long _historic;public Long getHistoric() { return _historic; }public void setHistoric(Long value) { _historic = value; }protected org.monet.metamodel.internal.Ref _indicator;public org.monet.metamodel.internal.Ref getIndicator() { return _indicator; }public void setIndicator(org.monet.metamodel.internal.Ref value) { _indicator = value; }protected org.monet.metamodel.internal.Ref _feature;public org.monet.metamodel.internal.Ref getFeature() { return _feature; }public void setFeature(org.monet.metamodel.internal.Ref value) { _feature = value; }protected org.monet.metamodel.internal.Ref _kpi;public org.monet.metamodel.internal.Ref getKpi() { return _kpi; }public void setKpi(org.monet.metamodel.internal.Ref value) { _kpi = value; }protected void copy(ParameterProperty instance) {this._historic = instance._historic;
this._indicator = instance._indicator;
this._feature = instance._feature;
this._kpi = instance._kpi;
this._code = instance._code;
this._name = instance._name;
}protected void merge(ParameterProperty child) {super.merge(child);if(child._historic != null)this._historic = child._historic;
if(child._indicator != null)this._indicator = child._indicator;
if(child._feature != null)this._feature = child._feature;
if(child._kpi != null)this._kpi = child._kpi;
}}protected LinkedHashMap<String, ParameterProperty> _parameterPropertyMap = new LinkedHashMap<String, ParameterProperty>();public void addParameter(ParameterProperty value) {String key = value.getName() != null ? value.getName() : value.getCode();ParameterProperty current = _parameterPropertyMap.get(key);if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {ParameterProperty instance = value.getClass().newInstance();instance.copy(current);instance.setCode(value.getCode());instance.setName(value.getName());instance.merge(value);_parameterPropertyMap.put(key, instance);}catch (Exception exception) {}}else current.merge(value);} else {_parameterPropertyMap.put(key, value);} }public java.util.Map<String,ParameterProperty> getParameterMap() { return _parameterPropertyMap; }public java.util.Collection<ParameterProperty> getParameterList() { return _parameterPropertyMap.values(); }
	

	public void copy(KpiDefinition instance) {
		this._code = instance._code;
this._name = instance._name;
this._parent = instance._parent;
this._label = instance._label;
this._description = instance._description;
this._help = instance._help;

		for(ParameterProperty item : instance._parameterPropertyMap.values())this.addParameter(item);
this._isAbstract = instance._isAbstract; 

		
	}

	public void merge(KpiDefinition child) {
		super.merge(child);
		
		
		for(ParameterProperty item : child._parameterPropertyMap.values())this.addParameter(item);

		
	}

	public Class<?> getMetamodelClass() {
		return KpiDefinition.class;
	}

}

