package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
EditPictureStepProperty


*/

public  class EditPictureStepProperty extends StepEditProperty {

	
	public static class IsHandWritten  {protected void copy(IsHandWritten instance) {}protected void merge(IsHandWritten child) {}}protected IsHandWritten _isHandWritten;public boolean isHandWritten() { return (_isHandWritten != null); }public IsHandWritten getIsHandWritten() { return _isHandWritten; }public void setIsHandWritten(boolean value) { if(value) _isHandWritten = new IsHandWritten(); else {_isHandWritten = null;}}public static class SizeProperty  {protected Long _width;public Long getWidth() { return _width; }public void setWidth(Long value) { _width = value; }protected Long _height;public Long getHeight() { return _height; }public void setHeight(Long value) { _height = value; }protected void copy(SizeProperty instance) {this._width = instance._width;
this._height = instance._height;
}protected void merge(SizeProperty child) {if(child._width != null)this._width = child._width;
if(child._height != null)this._height = child._height;
}}protected SizeProperty _sizeProperty;public SizeProperty getSize() { return _sizeProperty; }public void setSize(SizeProperty value) { if(_sizeProperty!=null) _sizeProperty.merge(value); else {_sizeProperty = value;} }
	

	public void copy(EditPictureStepProperty instance) {
		this._label = instance._label;
this._code = instance._code;
this._name = instance._name;

		this._isHandWritten = instance._isHandWritten; 
this._sizeProperty = instance._sizeProperty; 
this._isRequired = instance._isRequired; 
this._isReadOnly = instance._isReadOnly; 
this._isMultiple = instance._isMultiple; 

		
	}

	public void merge(EditPictureStepProperty child) {
		super.merge(child);
		
		
		if(_isHandWritten == null) _isHandWritten = child._isHandWritten; else if (child._isHandWritten != null) {_isHandWritten.merge(child._isHandWritten);}
if(_sizeProperty == null) _sizeProperty = child._sizeProperty; else if (child._sizeProperty != null) {_sizeProperty.merge(child._sizeProperty);}

		
	}

	public Class<?> getMetamodelClass() {
		return EditPictureStepProperty.class;
	}

}

