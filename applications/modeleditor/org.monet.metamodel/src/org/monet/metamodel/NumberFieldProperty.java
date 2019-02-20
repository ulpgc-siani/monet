package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
NumberFieldProperty
Esta propiedad se utiliza para incluir un campo numérico en un formulario

*/

public  class NumberFieldProperty extends MultipleableFieldProperty {

	protected String _format;public String getFormat() { return _format; }public void setFormat(String value) { _format = value; }public enum EditionEnumeration { BUTTON,SLIDER }protected EditionEnumeration _edition;public EditionEnumeration getEdition() { return _edition; }public void setEdition(EditionEnumeration value) { _edition = value; }
	public static class RangeProperty  {protected Long _min;public Long getMin() { return _min; }public void setMin(Long value) { _min = value; }protected Long _max;public Long getMax() { return _max; }public void setMax(Long value) { _max = value; }protected void copy(RangeProperty instance) {this._min = instance._min;
this._max = instance._max;
}protected void merge(RangeProperty child) {if(child._min != null)this._min = child._min;
if(child._max != null)this._max = child._max;
}}protected RangeProperty _rangeProperty;public RangeProperty getRange() { return _rangeProperty; }public void setRange(RangeProperty value) { if(_rangeProperty!=null) _rangeProperty.merge(value); else {_rangeProperty = value;} }
	

	public void copy(NumberFieldProperty instance) {
		this._format = instance._format;
this._edition = instance._edition;
this._label = instance._label;
this._description = instance._description;
this._template = instance._template;
this._code = instance._code;
this._name = instance._name;

		this._rangeProperty = instance._rangeProperty; 
this._isMultiple = instance._isMultiple; 
this._boundaryProperty = instance._boundaryProperty; 
this._isRequired = instance._isRequired; 
this._isReadonly = instance._isReadonly; 
this._isCollapsible = instance._isCollapsible; 
this._isExtended = instance._isExtended; 
this._isStatic = instance._isStatic; 
this._isUnivocal = instance._isUnivocal; 
_displayPropertyList.addAll(instance._displayPropertyList);

		
	}

	public void merge(NumberFieldProperty child) {
		super.merge(child);
		
		if(child._format != null)this._format = child._format;
if(child._edition != null)this._edition = child._edition;

		if(_rangeProperty == null) _rangeProperty = child._rangeProperty; else if (child._rangeProperty != null) {_rangeProperty.merge(child._rangeProperty);}

		
	}

	public Class<?> getMetamodelClass() {
		return NumberFieldProperty.class;
	}

}

