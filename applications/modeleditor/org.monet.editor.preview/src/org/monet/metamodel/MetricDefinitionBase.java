package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
MetricDefinition
Permite definir una métrica para ser usada en un cubo

*/

public  class MetricDefinitionBase extends EntityDefinition {

	
	public static class LabelProperty  {protected String _to;public String getTo() { return _to; }public void setTo(String value) { _to = value; }protected String _factor;public String getFactor() { return _factor; }public void setFactor(String value) { _factor = value; }protected Object _text;public Object getText() { return _text; }public void setText(Object value) { _text = value; }protected void merge(LabelProperty child) {if(child._to != null)this._to = child._to;
if(child._factor != null)this._factor = child._factor;
if(child._text != null)this._text = child._text;
}}protected ArrayList<LabelProperty> _labelPropertyList = new ArrayList<LabelProperty>();public ArrayList<LabelProperty> getLabelList() { return _labelPropertyList; }
	

	public void merge(MetricDefinitionBase child) {
		super.merge(child);
		
		
		_labelPropertyList.addAll(child._labelPropertyList);

		
	}

	public Class<?> getMetamodelClass() {
		return MetricDefinitionBase.class;
	}

}

