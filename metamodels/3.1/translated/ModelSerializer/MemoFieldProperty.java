package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
MemoFieldProperty
Esta propiedad se utiliza para incluir un campo descripción en un formulario
Este tipo de campos permite que el usuario puede editar un texto en varias línea
*/

public  class MemoFieldProperty extends MultipleableFieldProperty {

	
	public static class AllowHistoryProperty  {protected String _datastore;public String getDatastore() { return _datastore; }public void setDatastore(String value) { _datastore = value; }protected void copy(AllowHistoryProperty instance) {this._datastore = instance._datastore;
}protected void merge(AllowHistoryProperty child) {if(child._datastore != null)this._datastore = child._datastore;
}}protected AllowHistoryProperty _allowHistoryProperty;public boolean allowHistory() { return (_allowHistoryProperty != null); }public AllowHistoryProperty getAllowHistory() { return _allowHistoryProperty; }public void setAllowHistory(boolean value) { if(value) _allowHistoryProperty = new AllowHistoryProperty(); else {_allowHistoryProperty = null;}}public static class LengthProperty  {protected Long _max;public Long getMax() { return _max; }public void setMax(Long value) { _max = value; }protected void copy(LengthProperty instance) {this._max = instance._max;
}protected void merge(LengthProperty child) {if(child._max != null)this._max = child._max;
}}protected LengthProperty _lengthProperty;public LengthProperty getLength() { return _lengthProperty; }public void setLength(LengthProperty value) { if(_lengthProperty!=null) _lengthProperty.merge(value); else {_lengthProperty = value;} }
	

	public void copy(MemoFieldProperty instance) {
		this._label = instance._label;
this._description = instance._description;
this._code = instance._code;
this._name = instance._name;

		this._allowHistoryProperty = instance._allowHistoryProperty; 
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

	public void merge(MemoFieldProperty child) {
		super.merge(child);
		
		
		if(_allowHistoryProperty == null) _allowHistoryProperty = child._allowHistoryProperty; else if (child._allowHistoryProperty != null) {_allowHistoryProperty.merge(child._allowHistoryProperty);}
if(_lengthProperty == null) _lengthProperty = child._lengthProperty; else if (child._lengthProperty != null) {_lengthProperty.merge(child._lengthProperty);}

		
	}

	public Class<?> getMetamodelClass() {
		return MemoFieldProperty.class;
	}

}

