package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
UriFieldProperty
Esta propiedad se utiliza para incluir un campo texto en un formulario

*/

public  class UriFieldProperty extends MultipleableFieldProperty {

	
	
	

	public void copy(UriFieldProperty instance) {
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

	public void merge(UriFieldProperty child) {
		super.merge(child);
		
		
		
		
	}

	public Class<?> getMetamodelClass() {
		return UriFieldProperty.class;
	}

}

