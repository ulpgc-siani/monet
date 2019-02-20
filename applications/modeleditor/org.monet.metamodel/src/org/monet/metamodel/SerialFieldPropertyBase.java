package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
SerialFieldProperty
Esta propiedad se utiliza para incluir un campo selección en un formulario

*/

public  class SerialFieldPropertyBase extends MultipleableFieldProperty {

	
	public static class SerialProperty  {protected String _format;public String getFormat() { return _format; }public void setFormat(String value) { _format = value; }protected String _name;public String getName() { return _name; }public void setName(String value) { _name = value; }protected void copy(SerialProperty instance) {this._format = instance._format;
this._name = instance._name;
}protected void merge(SerialProperty child) {if(child._format != null)this._format = child._format;
if(child._name != null)this._name = child._name;
}}protected SerialProperty _serialProperty;public SerialProperty getSerial() { return _serialProperty; }public void setSerial(SerialProperty value) { if(_serialProperty!=null) _serialProperty.merge(value); else {_serialProperty = value;} }
	

	public void copy(SerialFieldPropertyBase instance) {
		this._label = instance._label;
this._description = instance._description;
this._template = instance._template;
this._code = instance._code;
this._name = instance._name;

		this._serialProperty = instance._serialProperty; 
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

	public void merge(SerialFieldPropertyBase child) {
		super.merge(child);
		
		
		if(_serialProperty == null) _serialProperty = child._serialProperty; else if (child._serialProperty != null) {_serialProperty.merge(child._serialProperty);}

		
	}

	public Class<?> getMetamodelClass() {
		return SerialFieldPropertyBase.class;
	}

}

