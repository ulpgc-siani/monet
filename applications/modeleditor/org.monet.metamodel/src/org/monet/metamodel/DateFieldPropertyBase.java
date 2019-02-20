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

	public enum PrecisionEnumeration { YEARS,MONTHS,DAYS,HOURS,MINUTES,SECONDS }protected PrecisionEnumeration _precision;public PrecisionEnumeration getPrecision() { return _precision; }public void setPrecision(PrecisionEnumeration value) { _precision = value; }public enum PurposeEnumeration { NEAR_DATE,DISTANT_DATE }protected PurposeEnumeration _purpose;public PurposeEnumeration getPurpose() { return _purpose; }public void setPurpose(PurposeEnumeration value) { _purpose = value; }
	public static class AllowLessPrecisionProperty  {protected void copy(AllowLessPrecisionProperty instance) {}protected void merge(AllowLessPrecisionProperty child) {}}protected AllowLessPrecisionProperty _allowLessPrecisionProperty;public boolean allowLessPrecision() { return (_allowLessPrecisionProperty != null); }public AllowLessPrecisionProperty getAllowLessPrecision() { return _allowLessPrecisionProperty; }public void setAllowLessPrecision(boolean value) { if(value) _allowLessPrecisionProperty = new AllowLessPrecisionProperty(); else {_allowLessPrecisionProperty = null;}}public static class RangeProperty  {protected Long _min;public Long getMin() { return _min; }public void setMin(Long value) { _min = value; }protected Long _max;public Long getMax() { return _max; }public void setMax(Long value) { _max = value; }protected void copy(RangeProperty instance) {this._min = instance._min;
this._max = instance._max;
}protected void merge(RangeProperty child) {if(child._min != null)this._min = child._min;
if(child._max != null)this._max = child._max;
}}protected RangeProperty _rangeProperty;public RangeProperty getRange() { return _rangeProperty; }public void setRange(RangeProperty value) { if(_rangeProperty!=null) _rangeProperty.merge(value); else {_rangeProperty = value;} }
	

	public void copy(DateFieldPropertyBase instance) {
		this._precision = instance._precision;
this._purpose = instance._purpose;
this._label = instance._label;
this._description = instance._description;
this._template = instance._template;
this._code = instance._code;
this._name = instance._name;

		this._allowLessPrecisionProperty = instance._allowLessPrecisionProperty; 
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

	public void merge(DateFieldPropertyBase child) {
		super.merge(child);
		
		if(child._precision != null)this._precision = child._precision;
if(child._purpose != null)this._purpose = child._purpose;

		if(_allowLessPrecisionProperty == null) _allowLessPrecisionProperty = child._allowLessPrecisionProperty; else if (child._allowLessPrecisionProperty != null) {_allowLessPrecisionProperty.merge(child._allowLessPrecisionProperty);}
if(_rangeProperty == null) _rangeProperty = child._rangeProperty; else if (child._rangeProperty != null) {_rangeProperty.merge(child._rangeProperty);}

		
	}

	public Class<?> getMetamodelClass() {
		return DateFieldPropertyBase.class;
	}

}

