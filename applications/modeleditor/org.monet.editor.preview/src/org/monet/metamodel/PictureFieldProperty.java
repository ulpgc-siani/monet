package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
PictureFieldProperty
Esta propiedad se utiliza para incluir un campo imagen en un formulario

*/

public  class PictureFieldProperty extends MultipleableFieldProperty {

	protected String _default;public String getDefault() { return _default; }public void setDefault(String value) { _default = value; }protected Long _limit;public Long getLimit() { return _limit; }public void setLimit(Long value) { _limit = value; }
	public static class IsProfilePhoto  {protected void copy(IsProfilePhoto instance) {}protected void merge(IsProfilePhoto child) {}}protected IsProfilePhoto _isProfilePhoto;public boolean isProfilePhoto() { return (_isProfilePhoto != null); }public IsProfilePhoto getIsProfilePhoto() { return _isProfilePhoto; }public void setIsProfilePhoto(boolean value) { if(value) _isProfilePhoto = new IsProfilePhoto(); else {_isProfilePhoto = null;}}public static class SizeProperty  {protected Long _width;public Long getWidth() { return _width; }public void setWidth(Long value) { _width = value; }protected Long _height;public Long getHeight() { return _height; }public void setHeight(Long value) { _height = value; }protected void copy(SizeProperty instance) {this._width = instance._width;
this._height = instance._height;
}protected void merge(SizeProperty child) {if(child._width != null)this._width = child._width;
if(child._height != null)this._height = child._height;
}}protected SizeProperty _sizeProperty;public SizeProperty getSize() { return _sizeProperty; }public void setSize(SizeProperty value) { if(_sizeProperty!=null) _sizeProperty.merge(value); else {_sizeProperty = value;} }
	

	public void copy(PictureFieldProperty instance) {
		this._default = instance._default;
this._limit = instance._limit;
this._label = instance._label;
this._description = instance._description;
this._template = instance._template;
this._code = instance._code;
this._name = instance._name;

		this._isProfilePhoto = instance._isProfilePhoto; 
this._sizeProperty = instance._sizeProperty; 
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

	public void merge(PictureFieldProperty child) {
		super.merge(child);
		
		if(child._default != null)this._default = child._default;
if(child._limit != null)this._limit = child._limit;

		if(_isProfilePhoto == null) _isProfilePhoto = child._isProfilePhoto; else if (child._isProfilePhoto != null) {_isProfilePhoto.merge(child._isProfilePhoto);}
if(_sizeProperty == null) _sizeProperty = child._sizeProperty; else if (child._sizeProperty != null) {_sizeProperty.merge(child._sizeProperty);}

		
	}

	public Class<?> getMetamodelClass() {
		return PictureFieldProperty.class;
	}

}

