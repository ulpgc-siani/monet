package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
MultipleableFieldProperty
Declaración abstracta del tipo campo múltiple

*/

public abstract class MultipleableFieldProperty extends FieldProperty {

	
	public static class IsMultiple  {protected void copy(IsMultiple instance) {}protected void merge(IsMultiple child) {}}protected IsMultiple _isMultiple;public boolean isMultiple() { return (_isMultiple != null); }public IsMultiple getIsMultiple() { return _isMultiple; }public void setIsMultiple(boolean value) { if(value) _isMultiple = new IsMultiple(); else {_isMultiple = null;}}
	

	public void copy(MultipleableFieldProperty instance) {
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

	public void merge(MultipleableFieldProperty child) {
		super.merge(child);
		
		
		if(_isMultiple == null) _isMultiple = child._isMultiple; else if (child._isMultiple != null) {_isMultiple.merge(child._isMultiple);}

		
	}

	public Class<?> getMetamodelClass() {
		return MultipleableFieldProperty.class;
	}

}

