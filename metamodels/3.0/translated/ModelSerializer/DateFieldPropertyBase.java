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

	public enum PrecisionEnumeration { YEARS,MONTHS,DAYS,HOURS,MINUTES,SECONDS }protected PrecisionEnumeration _precision;public PrecisionEnumeration getPrecision() { return _precision; }public void setPrecision(PrecisionEnumeration value) { _precision = value; }public enum EditionEnumeration { RANDOM,SEQUENTIAL }protected EditionEnumeration _edition;public EditionEnumeration getEdition() { return _edition; }public void setEdition(EditionEnumeration value) { _edition = value; }
	
	

	public void copy(DateFieldPropertyBase instance) {
		this._precision = instance._precision;
this._edition = instance._edition;
this._label = instance._label;
this._description = instance._description;
this._code = instance._code;
this._name = instance._name;

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
if(child._edition != null)this._edition = child._edition;

		
		
	}

	public Class<?> getMetamodelClass() {
		return DateFieldPropertyBase.class;
	}

}

