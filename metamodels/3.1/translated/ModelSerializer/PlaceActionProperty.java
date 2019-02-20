package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
PlaceActionProperty
Declaración del tipo abstracto de acción a realizar en un lugar

*/

public abstract class PlaceActionProperty extends ReferenceableProperty {

	protected String _name;public String getName() { return _name; }public void setName(String value) { _name = value; }protected Object _label;public Object getLabel() { return _label; }public void setLabel(Object value) { _label = value; }
	
	

	public void copy(PlaceActionProperty instance) {
		this._name = instance._name;
this._label = instance._label;
this._code = instance._code;
this._name = instance._name;

		
		
	}

	public void merge(PlaceActionProperty child) {
		super.merge(child);
		
		if(child._name != null)this._name = child._name;
if(child._label != null)this._label = child._label;

		
		
	}

	public Class<?> getMetamodelClass() {
		return PlaceActionProperty.class;
	}

}

