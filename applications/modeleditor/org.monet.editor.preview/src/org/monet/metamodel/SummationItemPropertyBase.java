package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
SummationItemProperty
Esta propiedad se utiliza para definir un epígrafe de un campo sumatoria

*/

public  class SummationItemPropertyBase  {

	protected Object _label;public Object getLabel() { return _label; }public void setLabel(Object value) { _label = value; }protected String _key;public String getKey() { return _key; }public void setKey(String value) { _key = value; }public enum TypeEnumeration { SIMPLE,INVOICE,ACCOUNT }protected TypeEnumeration _type;public TypeEnumeration getType() { return _type; }public void setType(TypeEnumeration value) { _type = value; }
	public static class IsMultiple  {protected void copy(IsMultiple instance) {}protected void merge(IsMultiple child) {}}protected IsMultiple _isMultiple;public boolean isMultiple() { return (_isMultiple != null); }public IsMultiple getIsMultiple() { return _isMultiple; }public void setIsMultiple(boolean value) { if(value) _isMultiple = new IsMultiple(); else {_isMultiple = null;}}public static class IsNegative  {protected void copy(IsNegative instance) {}protected void merge(IsNegative child) {}}protected IsNegative _isNegative;public boolean isNegative() { return (_isNegative != null); }public IsNegative getIsNegative() { return _isNegative; }public void setIsNegative(boolean value) { if(value) _isNegative = new IsNegative(); else {_isNegative = null;}}
	protected ArrayList<SummationItemProperty> _summationItemPropertyList = new ArrayList<SummationItemProperty>();public void addSummationItemProperty(SummationItemProperty value) { _summationItemPropertyList.add(value); }public ArrayList<SummationItemProperty> getSummationItemPropertyList() { return _summationItemPropertyList; }

	public void copy(SummationItemPropertyBase instance) {
		this._label = instance._label;
this._key = instance._key;
this._type = instance._type;

		this._isMultiple = instance._isMultiple; 
this._isNegative = instance._isNegative; 

		_summationItemPropertyList.addAll(instance._summationItemPropertyList);
	}

	public void merge(SummationItemPropertyBase child) {
		
		
		if(child._label != null)this._label = child._label;
if(child._key != null)this._key = child._key;
if(child._type != null)this._type = child._type;

		if(_isMultiple == null) _isMultiple = child._isMultiple; else if (child._isMultiple != null) {_isMultiple.merge(child._isMultiple);}
if(_isNegative == null) _isNegative = child._isNegative; else if (child._isNegative != null) {_isNegative.merge(child._isNegative);}

		_summationItemPropertyList.addAll(child._summationItemPropertyList);
	}

	public Class<?> getMetamodelClass() {
		return SummationItemPropertyBase.class;
	}

}

