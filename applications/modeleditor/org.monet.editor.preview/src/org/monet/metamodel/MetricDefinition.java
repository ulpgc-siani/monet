package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
MetricDefinition
Permite definir una métrica para ser usada en el análisis del negocio

*/

public  class MetricDefinition extends EntityDefinition {

	
	public static class LabelProperty  {protected String _max;public String getMax() { return _max; }public void setMax(String value) { _max = value; }protected String _factor;public String getFactor() { return _factor; }public void setFactor(String value) { _factor = value; }protected String _text;public String getText() { return _text; }public void setText(String value) { _text = value; }protected void copy(LabelProperty instance) {this._max = instance._max;
this._factor = instance._factor;
this._text = instance._text;
}protected void merge(LabelProperty child) {if(child._max != null)this._max = child._max;
if(child._factor != null)this._factor = child._factor;
if(child._text != null)this._text = child._text;
}}protected ArrayList<LabelProperty> _labelPropertyList = new ArrayList<LabelProperty>();public ArrayList<LabelProperty> getLabelList() { return _labelPropertyList; }
	

	public void copy(MetricDefinition instance) {
		this._code = instance._code;
this._name = instance._name;
this._parent = instance._parent;
this._label = instance._label;
this._description = instance._description;
this._help = instance._help;

		_labelPropertyList.addAll(instance._labelPropertyList);
this._isAbstract = instance._isAbstract; 

		
	}

	public void merge(MetricDefinition child) {
		super.merge(child);
		
		
		_labelPropertyList.addAll(child._labelPropertyList);

		
	}

	public Class<?> getMetamodelClass() {
		return MetricDefinition.class;
	}

}

