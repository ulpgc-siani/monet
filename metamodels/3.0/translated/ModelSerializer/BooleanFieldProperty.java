package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
BooleanFieldProperty
Esta propiedad se utiliza para incluir un campo booleano en un formulario

*/

public  class BooleanFieldProperty extends FieldProperty {

	
	public static class ValueProperty  {protected Object _whenTrue;public Object getWhenTrue() { return _whenTrue; }public void setWhenTrue(Object value) { _whenTrue = value; }protected Object _whenFalse;public Object getWhenFalse() { return _whenFalse; }public void setWhenFalse(Object value) { _whenFalse = value; }protected void copy(ValueProperty instance) {this._whenTrue = instance._whenTrue;
this._whenFalse = instance._whenFalse;
}protected void merge(ValueProperty child) {if(child._whenTrue != null)this._whenTrue = child._whenTrue;
if(child._whenFalse != null)this._whenFalse = child._whenFalse;
}}protected ValueProperty _valueProperty;public ValueProperty getValue() { return _valueProperty; }public void setValue(ValueProperty value) { if(_valueProperty!=null) _valueProperty.merge(value); else {_valueProperty = value;} }
	

	public void copy(BooleanFieldProperty instance) {
		this._label = instance._label;
this._description = instance._description;
this._code = instance._code;
this._name = instance._name;

		this._valueProperty = instance._valueProperty; 
this._isRequired = instance._isRequired; 
this._isReadonly = instance._isReadonly; 
this._isExtended = instance._isExtended; 
this._isSuperfield = instance._isSuperfield; 
this._isStatic = instance._isStatic; 
this._isUnivocal = instance._isUnivocal; 
_displayPropertyList.addAll(instance._displayPropertyList);

		
	}

	public void merge(BooleanFieldProperty child) {
		super.merge(child);
		
		
		if(_valueProperty == null) _valueProperty = child._valueProperty; else if (child._valueProperty != null) {_valueProperty.merge(child._valueProperty);}

		
	}

	public Class<?> getMetamodelClass() {
		return BooleanFieldProperty.class;
	}

}

