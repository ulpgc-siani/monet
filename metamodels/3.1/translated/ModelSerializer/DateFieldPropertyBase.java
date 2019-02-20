package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
DateFieldProperty
Esta propiedad se utiliza para incluir un campo fecha en un formulario

*/

public  class DateFieldPropertyBase extends MultipleableFieldProperty {

	public enum PrecisionEnumeration { YEARS,MONTHS,DAYS,HOURS,MINUTES,SECONDS }protected PrecisionEnumeration _precision;public PrecisionEnumeration getPrecision() { return _precision; }public void setPrecision(PrecisionEnumeration value) { _precision = value; }
	public static class RangeProperty  {protected Long _min;public Long getMin() { return _min; }public void setMin(Long value) { _min = value; }protected Long _max;public Long getMax() { return _max; }public void setMax(Long value) { _max = value; }protected void copy(RangeProperty instance) {this._min = instance._min;
this._max = instance._max;
}protected void merge(RangeProperty child) {if(child._min != null)this._min = child._min;
if(child._max != null)this._max = child._max;
}}protected RangeProperty _rangeProperty;public RangeProperty getRange() { return _rangeProperty; }public void setRange(RangeProperty value) { if(_rangeProperty!=null) _rangeProperty.merge(value); else {_rangeProperty = value;} }
	

	public void copy(DateFieldPropertyBase instance) {
		this._precision = instance._precision;
this._label = instance._label;
this._description = instance._description;
this._code = instance._code;
this._name = instance._name;

		this._rangeProperty = instance._rangeProperty; 
this._isMultiple = instance._isMultiple; 
this._isRequired = instance._isRequired; 
this._isReadonly = instance._isReadonly; 
this._isExtended = instance._isExtended; 
this._isSuperfield = instance._isSuperfield; 
this._isStatic = instance._isStatic; 
this._isUnivocal = instance._isUnivocal; 
_displayPropertyList.addAll(instance._displayPropertyList);

		
	}

	public void merge(DateFieldPropertyBase child) {
		super.merge(child);
		
		if(child._precision != null)this._precision = child._precision;

		if(_rangeProperty == null) _rangeProperty = child._rangeProperty; else if (child._rangeProperty != null) {_rangeProperty.merge(child._rangeProperty);}

		
	}

	public Class<?> getMetamodelClass() {
		return DateFieldPropertyBase.class;
	}

}

