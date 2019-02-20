package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
UrlFieldProperty
Esta propiedad se utiliza para incluir un campo texto en un formulario

*/

public  class UrlFieldProperty extends MultipleableFieldProperty {

	
	public static class LengthProperty  {protected Long _max;public Long getMax() { return _max; }public void setMax(Long value) { _max = value; }protected Long _min;public Long getMin() { return _min; }public void setMin(Long value) { _min = value; }protected void copy(LengthProperty instance) {this._max = instance._max;
this._min = instance._min;
}protected void merge(LengthProperty child) {if(child._max != null)this._max = child._max;
if(child._min != null)this._min = child._min;
}}protected LengthProperty _lengthProperty;public LengthProperty getLength() { return _lengthProperty; }public void setLength(LengthProperty value) { if(_lengthProperty!=null) _lengthProperty.merge(value); else {_lengthProperty = value;} }
	

	public void copy(UrlFieldProperty instance) {
		this._label = instance._label;
this._description = instance._description;
this._code = instance._code;
this._name = instance._name;

		this._lengthProperty = instance._lengthProperty; 
this._isMultiple = instance._isMultiple; 
this._isRequired = instance._isRequired; 
this._isReadonly = instance._isReadonly; 
this._isExtended = instance._isExtended; 
this._isSuperfield = instance._isSuperfield; 
this._isStatic = instance._isStatic; 
this._isUnivocal = instance._isUnivocal; 
_displayPropertyList.addAll(instance._displayPropertyList);

		
	}

	public void merge(UrlFieldProperty child) {
		super.merge(child);
		
		
		if(_lengthProperty == null) _lengthProperty = child._lengthProperty; else if (child._lengthProperty != null) {_lengthProperty.merge(child._lengthProperty);}

		
	}

	public Class<?> getMetamodelClass() {
		return UrlFieldProperty.class;
	}

}

