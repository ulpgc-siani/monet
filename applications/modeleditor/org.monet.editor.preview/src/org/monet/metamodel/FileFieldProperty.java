package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
FileFieldProperty
Esta propiedad se utiliza para incluir un campo fichero en un formulario

*/

public  class FileFieldProperty extends MultipleableFieldProperty {

	protected Long _limit;public Long getLimit() { return _limit; }public void setLimit(Long value) { _limit = value; }
	
	

	public void copy(FileFieldProperty instance) {
		this._limit = instance._limit;
this._label = instance._label;
this._description = instance._description;
this._template = instance._template;
this._code = instance._code;
this._name = instance._name;

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

	public void merge(FileFieldProperty child) {
		super.merge(child);
		
		if(child._limit != null)this._limit = child._limit;

		
		
	}

	public Class<?> getMetamodelClass() {
		return FileFieldProperty.class;
	}

}

