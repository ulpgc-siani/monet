package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
LinkFieldProperty
Esta propiedad se utiliza para incluir un campo vínculo en un formulario

*/

public  class LinkFieldProperty extends MultipleableFieldProperty {

	
	public static class SourceProperty  {protected org.monet.metamodel.internal.Ref _index;public org.monet.metamodel.internal.Ref getIndex() { return _index; }public void setIndex(org.monet.metamodel.internal.Ref value) { _index = value; }protected org.monet.metamodel.internal.Ref _view;public org.monet.metamodel.internal.Ref getView() { return _view; }public void setView(org.monet.metamodel.internal.Ref value) { _view = value; }protected org.monet.metamodel.internal.Ref _collection;public org.monet.metamodel.internal.Ref getCollection() { return _collection; }public void setCollection(org.monet.metamodel.internal.Ref value) { _collection = value; }public static class FilterProperty  {protected org.monet.metamodel.internal.Ref _attribute;public org.monet.metamodel.internal.Ref getAttribute() { return _attribute; }public void setAttribute(org.monet.metamodel.internal.Ref value) { _attribute = value; }protected Object _value;public Object getValue() { return _value; }public void setValue(Object value) { _value = value; }public enum OperatorEnumeration { EQUALS,CONTAINS }protected OperatorEnumeration _operator;public OperatorEnumeration getOperator() { return _operator; }public void setOperator(OperatorEnumeration value) { _operator = value; }protected void copy(FilterProperty instance) {this._attribute = instance._attribute;
this._value = instance._value;
this._operator = instance._operator;
}protected void merge(FilterProperty child) {if(child._attribute != null)this._attribute = child._attribute;
if(child._value != null)this._value = child._value;
if(child._operator != null)this._operator = child._operator;
}}protected ArrayList<FilterProperty> _filterPropertyList = new ArrayList<FilterProperty>();public ArrayList<FilterProperty> getFilterList() { return _filterPropertyList; }protected void copy(SourceProperty instance) {this._index = instance._index;
this._view = instance._view;
this._collection = instance._collection;
_filterPropertyList.addAll(instance._filterPropertyList);
}protected void merge(SourceProperty child) {if(child._index != null)this._index = child._index;
if(child._view != null)this._view = child._view;
if(child._collection != null)this._collection = child._collection;
_filterPropertyList.addAll(child._filterPropertyList);
}}protected SourceProperty _sourceProperty;public SourceProperty getSource() { return _sourceProperty; }public void setSource(SourceProperty value) { if(_sourceProperty!=null) _sourceProperty.merge(value); else {_sourceProperty = value;} }public static class AllowHistoryProperty  {protected String _datastore;public String getDatastore() { return _datastore; }public void setDatastore(String value) { _datastore = value; }protected void copy(AllowHistoryProperty instance) {this._datastore = instance._datastore;
}protected void merge(AllowHistoryProperty child) {if(child._datastore != null)this._datastore = child._datastore;
}}protected AllowHistoryProperty _allowHistoryProperty;public boolean allowHistory() { return (_allowHistoryProperty != null); }public AllowHistoryProperty getAllowHistory() { return _allowHistoryProperty; }public void setAllowHistory(boolean value) { if(value) _allowHistoryProperty = new AllowHistoryProperty(); else {_allowHistoryProperty = null;}}public static class EnableHistoryProperty  {protected String _datastore;public String getDatastore() { return _datastore; }public void setDatastore(String value) { _datastore = value; }protected void copy(EnableHistoryProperty instance) {this._datastore = instance._datastore;
}protected void merge(EnableHistoryProperty child) {if(child._datastore != null)this._datastore = child._datastore;
}}protected EnableHistoryProperty _enableHistoryProperty;public EnableHistoryProperty getEnableHistory() { return _enableHistoryProperty; }public void setEnableHistory(EnableHistoryProperty value) { if(_enableHistoryProperty!=null) _enableHistoryProperty.merge(value); else {_enableHistoryProperty = value;} }public static class AllowSearchProperty  {protected void copy(AllowSearchProperty instance) {}protected void merge(AllowSearchProperty child) {}}protected AllowSearchProperty _allowSearchProperty;public boolean allowSearch() { return (_allowSearchProperty != null); }public AllowSearchProperty getAllowSearch() { return _allowSearchProperty; }public void setAllowSearch(boolean value) { if(value) _allowSearchProperty = new AllowSearchProperty(); else {_allowSearchProperty = null;}}public static class AllowLocationsProperty  {protected void copy(AllowLocationsProperty instance) {}protected void merge(AllowLocationsProperty child) {}}protected AllowLocationsProperty _allowLocationsProperty;public boolean allowLocations() { return (_allowLocationsProperty != null); }public AllowLocationsProperty getAllowLocations() { return _allowLocationsProperty; }public void setAllowLocations(boolean value) { if(value) _allowLocationsProperty = new AllowLocationsProperty(); else {_allowLocationsProperty = null;}}public static class AllowAddProperty  {protected void copy(AllowAddProperty instance) {}protected void merge(AllowAddProperty child) {}}protected AllowAddProperty _allowAddProperty;public boolean allowAdd() { return (_allowAddProperty != null); }public AllowAddProperty getAllowAdd() { return _allowAddProperty; }public void setAllowAdd(boolean value) { if(value) _allowAddProperty = new AllowAddProperty(); else {_allowAddProperty = null;}}public static class AllowEditProperty  {protected void copy(AllowEditProperty instance) {}protected void merge(AllowEditProperty child) {}}protected AllowEditProperty _allowEditProperty;public boolean allowEdit() { return (_allowEditProperty != null); }public AllowEditProperty getAllowEdit() { return _allowEditProperty; }public void setAllowEdit(boolean value) { if(value) _allowEditProperty = new AllowEditProperty(); else {_allowEditProperty = null;}}
	

	public void copy(LinkFieldProperty instance) {
		this._label = instance._label;
this._description = instance._description;
this._template = instance._template;
this._code = instance._code;
this._name = instance._name;

		this._sourceProperty = instance._sourceProperty; 
this._allowHistoryProperty = instance._allowHistoryProperty; 
this._enableHistoryProperty = instance._enableHistoryProperty; 
this._allowSearchProperty = instance._allowSearchProperty; 
this._allowLocationsProperty = instance._allowLocationsProperty; 
this._allowAddProperty = instance._allowAddProperty; 
this._allowEditProperty = instance._allowEditProperty; 
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

	public void merge(LinkFieldProperty child) {
		super.merge(child);
		
		
		if(_sourceProperty == null) _sourceProperty = child._sourceProperty; else if (child._sourceProperty != null) {_sourceProperty.merge(child._sourceProperty);}
if(_allowHistoryProperty == null) _allowHistoryProperty = child._allowHistoryProperty; else if (child._allowHistoryProperty != null) {_allowHistoryProperty.merge(child._allowHistoryProperty);}
if(_enableHistoryProperty == null) _enableHistoryProperty = child._enableHistoryProperty; else if (child._enableHistoryProperty != null) {_enableHistoryProperty.merge(child._enableHistoryProperty);}
if(_allowSearchProperty == null) _allowSearchProperty = child._allowSearchProperty; else if (child._allowSearchProperty != null) {_allowSearchProperty.merge(child._allowSearchProperty);}
if(_allowLocationsProperty == null) _allowLocationsProperty = child._allowLocationsProperty; else if (child._allowLocationsProperty != null) {_allowLocationsProperty.merge(child._allowLocationsProperty);}
if(_allowAddProperty == null) _allowAddProperty = child._allowAddProperty; else if (child._allowAddProperty != null) {_allowAddProperty.merge(child._allowAddProperty);}
if(_allowEditProperty == null) _allowEditProperty = child._allowEditProperty; else if (child._allowEditProperty != null) {_allowEditProperty.merge(child._allowEditProperty);}

		
	}

	public Class<?> getMetamodelClass() {
		return LinkFieldProperty.class;
	}

}

