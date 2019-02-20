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

	
	public static class IsMultiple  {protected void copy(IsMultiple instance) {}protected void merge(IsMultiple child) {}}protected IsMultiple _isMultiple;public boolean isMultiple() { return (_isMultiple != null); }public IsMultiple getIsMultiple() { return _isMultiple; }public void setIsMultiple(boolean value) { if(value) _isMultiple = new IsMultiple(); else {_isMultiple = null;}}public static class BoundaryProperty  {protected Long _min;public Long getMin() { return _min; }public void setMin(Long value) { _min = value; }protected Long _max;public Long getMax() { return _max; }public void setMax(Long value) { _max = value; }protected void copy(BoundaryProperty instance) {this._min = instance._min;
this._max = instance._max;
}protected void merge(BoundaryProperty child) {if(child._min != null)this._min = child._min;
if(child._max != null)this._max = child._max;
}}protected BoundaryProperty _boundaryProperty;public BoundaryProperty getBoundary() { return _boundaryProperty; }public void setBoundary(BoundaryProperty value) { if(_boundaryProperty!=null) _boundaryProperty.merge(value); else {_boundaryProperty = value;} }
	

	public void copy(MultipleableFieldProperty instance) {
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

	public void merge(MultipleableFieldProperty child) {
		super.merge(child);
		
		
		if(_isMultiple == null) _isMultiple = child._isMultiple; else if (child._isMultiple != null) {_isMultiple.merge(child._isMultiple);}
if(_boundaryProperty == null) _boundaryProperty = child._boundaryProperty; else if (child._boundaryProperty != null) {_boundaryProperty.merge(child._boundaryProperty);}

		
	}

	public Class<?> getMetamodelClass() {
		return MultipleableFieldProperty.class;
	}

}

