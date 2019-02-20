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

	
	
	

	public void copy(FileFieldProperty instance) {
		this._label = instance._label;
this._description = instance._description;
this._template = instance._template;
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

	public void merge(FileFieldProperty child) {
		super.merge(child);
		
		
		
		
	}

	public Class<?> getMetamodelClass() {
		return FileFieldProperty.class;
	}

}

