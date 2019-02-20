package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
StepEditProperty


*/

public abstract class StepEditProperty extends ReferenceableProperty {

	protected Object _label;public Object getLabel() { return _label; }public void setLabel(Object value) { _label = value; }
	public static class IsRequired  {protected void copy(IsRequired instance) {}protected void merge(IsRequired child) {}}protected IsRequired _isRequired;public boolean isRequired() { return (_isRequired != null); }public IsRequired getIsRequired() { return _isRequired; }public void setIsRequired(boolean value) { if(value) _isRequired = new IsRequired(); else {_isRequired = null;}}public static class IsReadOnly  {protected void copy(IsReadOnly instance) {}protected void merge(IsReadOnly child) {}}protected IsReadOnly _isReadOnly;public boolean isReadOnly() { return (_isReadOnly != null); }public IsReadOnly getIsReadOnly() { return _isReadOnly; }public void setIsReadOnly(boolean value) { if(value) _isReadOnly = new IsReadOnly(); else {_isReadOnly = null;}}public static class IsMultiple  {protected void copy(IsMultiple instance) {}protected void merge(IsMultiple child) {}}protected IsMultiple _isMultiple;public boolean isMultiple() { return (_isMultiple != null); }public IsMultiple getIsMultiple() { return _isMultiple; }public void setIsMultiple(boolean value) { if(value) _isMultiple = new IsMultiple(); else {_isMultiple = null;}}
	

	public void copy(StepEditProperty instance) {
		this._label = instance._label;
this._code = instance._code;
this._name = instance._name;

		this._isRequired = instance._isRequired; 
this._isReadOnly = instance._isReadOnly; 
this._isMultiple = instance._isMultiple; 

		
	}

	public void merge(StepEditProperty child) {
		super.merge(child);
		
		if(child._label != null)this._label = child._label;

		if(_isRequired == null) _isRequired = child._isRequired; else if (child._isRequired != null) {_isRequired.merge(child._isRequired);}
if(_isReadOnly == null) _isReadOnly = child._isReadOnly; else if (child._isReadOnly != null) {_isReadOnly.merge(child._isReadOnly);}
if(_isMultiple == null) _isMultiple = child._isMultiple; else if (child._isMultiple != null) {_isMultiple.merge(child._isMultiple);}

		
	}

	public Class<?> getMetamodelClass() {
		return StepEditProperty.class;
	}

}

