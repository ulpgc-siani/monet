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
	public static class RequireConfirmationProperty  {protected Object _message;public Object getMessage() { return _message; }public void setMessage(Object value) { _message = value; }protected void copy(RequireConfirmationProperty instance) {this._message = instance._message;
}protected void merge(RequireConfirmationProperty child) {if(child._message != null)this._message = child._message;
}}protected RequireConfirmationProperty _requireConfirmationProperty;public RequireConfirmationProperty getRequireConfirmation() { return _requireConfirmationProperty; }public void setRequireConfirmation(RequireConfirmationProperty value) { if(_requireConfirmationProperty!=null) _requireConfirmationProperty.merge(value); else {_requireConfirmationProperty = value;} }
	

	public void copy(PlaceActionProperty instance) {
		this._name = instance._name;
this._label = instance._label;
this._code = instance._code;
this._name = instance._name;

		this._requireConfirmationProperty = instance._requireConfirmationProperty; 

		
	}

	public void merge(PlaceActionProperty child) {
		super.merge(child);
		
		if(child._name != null)this._name = child._name;
if(child._label != null)this._label = child._label;

		if(_requireConfirmationProperty == null) _requireConfirmationProperty = child._requireConfirmationProperty; else if (child._requireConfirmationProperty != null) {_requireConfirmationProperty.merge(child._requireConfirmationProperty);}

		
	}

	public Class<?> getMetamodelClass() {
		return PlaceActionProperty.class;
	}

}

