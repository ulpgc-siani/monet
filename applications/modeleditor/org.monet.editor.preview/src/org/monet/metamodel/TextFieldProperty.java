package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
TextFieldProperty
Esta propiedad se utiliza para incluir un campo texto en un formulario

*/

public  class TextFieldProperty extends MultipleableFieldProperty {

	
	public static class AllowHistoryProperty  {protected String _datastore;public String getDatastore() { return _datastore; }public void setDatastore(String value) { _datastore = value; }protected void copy(AllowHistoryProperty instance) {this._datastore = instance._datastore;
}protected void merge(AllowHistoryProperty child) {if(child._datastore != null)this._datastore = child._datastore;
}}protected AllowHistoryProperty _allowHistoryProperty;public boolean allowHistory() { return (_allowHistoryProperty != null); }public AllowHistoryProperty getAllowHistory() { return _allowHistoryProperty; }public void setAllowHistory(boolean value) { if(value) _allowHistoryProperty = new AllowHistoryProperty(); else {_allowHistoryProperty = null;}}public static class EnableHistoryProperty  {protected String _datastore;public String getDatastore() { return _datastore; }public void setDatastore(String value) { _datastore = value; }protected void copy(EnableHistoryProperty instance) {this._datastore = instance._datastore;
}protected void merge(EnableHistoryProperty child) {if(child._datastore != null)this._datastore = child._datastore;
}}protected EnableHistoryProperty _enableHistoryProperty;public EnableHistoryProperty getEnableHistory() { return _enableHistoryProperty; }public void setEnableHistory(EnableHistoryProperty value) { if(_enableHistoryProperty!=null) _enableHistoryProperty.merge(value); else {_enableHistoryProperty = value;} }public static class LengthProperty  {protected Long _max;public Long getMax() { return _max; }public void setMax(Long value) { _max = value; }protected Long _min;public Long getMin() { return _min; }public void setMin(Long value) { _min = value; }protected void copy(LengthProperty instance) {this._max = instance._max;
this._min = instance._min;
}protected void merge(LengthProperty child) {if(child._max != null)this._max = child._max;
if(child._min != null)this._min = child._min;
}}protected LengthProperty _lengthProperty;public LengthProperty getLength() { return _lengthProperty; }public void setLength(LengthProperty value) { if(_lengthProperty!=null) _lengthProperty.merge(value); else {_lengthProperty = value;} }public static class EditionProperty  {public enum ModeEnumeration { UPPERCASE,LOWERCASE,SENTENCE,TITLE }protected ModeEnumeration _mode;public ModeEnumeration getMode() { return _mode; }public void setMode(ModeEnumeration value) { _mode = value; }protected void copy(EditionProperty instance) {this._mode = instance._mode;
}protected void merge(EditionProperty child) {if(child._mode != null)this._mode = child._mode;
}}protected EditionProperty _editionProperty;public EditionProperty getEdition() { return _editionProperty; }public void setEdition(EditionProperty value) { if(_editionProperty!=null) _editionProperty.merge(value); else {_editionProperty = value;} }public static class IsEmail  {protected void copy(IsEmail instance) {}protected void merge(IsEmail child) {}}protected IsEmail _isEmail;public boolean isEmail() { return (_isEmail != null); }public IsEmail getIsEmail() { return _isEmail; }public void setIsEmail(boolean value) { if(value) _isEmail = new IsEmail(); else {_isEmail = null;}}public static class PatternProperty  {protected Object _regexp;public Object getRegexp() { return _regexp; }public void setRegexp(Object value) { _regexp = value; }public static class MetaProperty  {protected Long _position;public Long getPosition() { return _position; }public void setPosition(Long value) { _position = value; }protected String _indicator;public String getIndicator() { return _indicator; }public void setIndicator(String value) { _indicator = value; }protected void copy(MetaProperty instance) {this._position = instance._position;
this._indicator = instance._indicator;
}protected void merge(MetaProperty child) {if(child._position != null)this._position = child._position;
if(child._indicator != null)this._indicator = child._indicator;
}}protected ArrayList<MetaProperty> _metaPropertyList = new ArrayList<MetaProperty>();public ArrayList<MetaProperty> getMetaList() { return _metaPropertyList; }protected void copy(PatternProperty instance) {this._regexp = instance._regexp;
_metaPropertyList.addAll(instance._metaPropertyList);
}protected void merge(PatternProperty child) {if(child._regexp != null)this._regexp = child._regexp;
_metaPropertyList.addAll(child._metaPropertyList);
}}protected ArrayList<PatternProperty> _patternPropertyList = new ArrayList<PatternProperty>();public ArrayList<PatternProperty> getPatternList() { return _patternPropertyList; }
	

	public void copy(TextFieldProperty instance) {
		this._label = instance._label;
this._description = instance._description;
this._template = instance._template;
this._code = instance._code;
this._name = instance._name;

		this._allowHistoryProperty = instance._allowHistoryProperty; 
this._enableHistoryProperty = instance._enableHistoryProperty; 
this._lengthProperty = instance._lengthProperty; 
this._editionProperty = instance._editionProperty; 
this._isEmail = instance._isEmail; 
_patternPropertyList.addAll(instance._patternPropertyList);
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

	public void merge(TextFieldProperty child) {
		super.merge(child);
		
		
		if(_allowHistoryProperty == null) _allowHistoryProperty = child._allowHistoryProperty; else if (child._allowHistoryProperty != null) {_allowHistoryProperty.merge(child._allowHistoryProperty);}
if(_enableHistoryProperty == null) _enableHistoryProperty = child._enableHistoryProperty; else if (child._enableHistoryProperty != null) {_enableHistoryProperty.merge(child._enableHistoryProperty);}
if(_lengthProperty == null) _lengthProperty = child._lengthProperty; else if (child._lengthProperty != null) {_lengthProperty.merge(child._lengthProperty);}
if(_editionProperty == null) _editionProperty = child._editionProperty; else if (child._editionProperty != null) {_editionProperty.merge(child._editionProperty);}
if(_isEmail == null) _isEmail = child._isEmail; else if (child._isEmail != null) {_isEmail.merge(child._isEmail);}
_patternPropertyList.addAll(child._patternPropertyList);

		
	}

	public Class<?> getMetamodelClass() {
		return TextFieldProperty.class;
	}

}

