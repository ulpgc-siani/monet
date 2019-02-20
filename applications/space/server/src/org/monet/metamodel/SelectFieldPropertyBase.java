package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
SelectFieldProperty
Esta propiedad se utiliza para incluir un campo selección en un formulario

*/

public  class SelectFieldPropertyBase extends MultipleableFieldProperty {

	protected org.monet.metamodel.internal.Ref _source;public org.monet.metamodel.internal.Ref getSource() { return _source; }public void setSource(org.monet.metamodel.internal.Ref value) { _source = value; }
	public static class TermsProperty  {protected ArrayList<TermProperty> _termPropertyList = new ArrayList<TermProperty>();public void addTermProperty(TermProperty value) { _termPropertyList.add(value); }public ArrayList<TermProperty> getTermPropertyList() { return _termPropertyList; }protected void copy(TermsProperty instance) {_termPropertyList.addAll(instance._termPropertyList);}protected void merge(TermsProperty child) {_termPropertyList.addAll(child._termPropertyList);}}protected TermsProperty _termsProperty;public TermsProperty getTerms() { return _termsProperty; }public void setTerms(TermsProperty value) { if(_termsProperty!=null) _termsProperty.merge(value); else {_termsProperty = value;} }public static class AllowHistoryProperty  {protected String _datastore;public String getDatastore() { return _datastore; }public void setDatastore(String value) { _datastore = value; }protected void copy(AllowHistoryProperty instance) {this._datastore = instance._datastore;
}protected void merge(AllowHistoryProperty child) {if(child._datastore != null)this._datastore = child._datastore;
}}protected AllowHistoryProperty _allowHistoryProperty;public boolean allowHistory() { return (_allowHistoryProperty != null); }public AllowHistoryProperty getAllowHistory() { return _allowHistoryProperty; }public void setAllowHistory(boolean value) { if(value) _allowHistoryProperty = new AllowHistoryProperty(); else {_allowHistoryProperty = null;}}public static class EnableHistoryProperty  {protected String _datastore;public String getDatastore() { return _datastore; }public void setDatastore(String value) { _datastore = value; }protected void copy(EnableHistoryProperty instance) {this._datastore = instance._datastore;
}protected void merge(EnableHistoryProperty child) {if(child._datastore != null)this._datastore = child._datastore;
}}protected EnableHistoryProperty _enableHistoryProperty;public EnableHistoryProperty getEnableHistory() { return _enableHistoryProperty; }public void setEnableHistory(EnableHistoryProperty value) { if(_enableHistoryProperty!=null) _enableHistoryProperty.merge(value); else {_enableHistoryProperty = value;} }public static class AllowSearchProperty  {protected void copy(AllowSearchProperty instance) {}protected void merge(AllowSearchProperty child) {}}protected AllowSearchProperty _allowSearchProperty;public boolean allowSearch() { return (_allowSearchProperty != null); }public AllowSearchProperty getAllowSearch() { return _allowSearchProperty; }public void setAllowSearch(boolean value) { if(value) _allowSearchProperty = new AllowSearchProperty(); else {_allowSearchProperty = null;}}public static class AllowOtherProperty  {protected void copy(AllowOtherProperty instance) {}protected void merge(AllowOtherProperty child) {}}protected AllowOtherProperty _allowOtherProperty;public boolean allowOther() { return (_allowOtherProperty != null); }public AllowOtherProperty getAllowOther() { return _allowOtherProperty; }public void setAllowOther(boolean value) { if(value) _allowOtherProperty = new AllowOtherProperty(); else {_allowOtherProperty = null;}}public static class AllowKeyProperty  {protected void copy(AllowKeyProperty instance) {}protected void merge(AllowKeyProperty child) {}}protected AllowKeyProperty _allowKeyProperty;public boolean allowKey() { return (_allowKeyProperty != null); }public AllowKeyProperty getAllowKey() { return _allowKeyProperty; }public void setAllowKey(boolean value) { if(value) _allowKeyProperty = new AllowKeyProperty(); else {_allowKeyProperty = null;}}public static class SelectProperty  {public enum FlattenEnumeration { NONE,ALL,LEVEL,LEAF,INTERNAL }protected FlattenEnumeration _flatten;public FlattenEnumeration getFlatten() { return _flatten; }public void setFlatten(FlattenEnumeration value) { _flatten = value; }protected Long _depth;public Long getDepth() { return _depth; }public void setDepth(Long value) { _depth = value; }protected Object _context;public Object getContext() { return _context; }public void setContext(Object value) { _context = value; }protected Object _root;public Object getRoot() { return _root; }public void setRoot(Object value) { _root = value; }public static class FilterProperty  {protected ArrayList<Object> _tag = new ArrayList<Object>();public ArrayList<Object> getTag() { return _tag; }public void setTag(ArrayList<Object> value) { _tag = value; }protected void copy(FilterProperty instance) {this._tag.addAll(instance._tag);
}protected void merge(FilterProperty child) {if(child._tag != null)this._tag.addAll(child._tag);
}}protected FilterProperty _filterProperty;public FilterProperty getFilter() { return _filterProperty; }public void setFilter(FilterProperty value) { if(_filterProperty!=null) _filterProperty.merge(value); else {_filterProperty = value;} }public static class IsEmbedded  {protected void copy(IsEmbedded instance) {}protected void merge(IsEmbedded child) {}}protected IsEmbedded _isEmbedded;public boolean isEmbedded() { return (_isEmbedded != null); }public IsEmbedded getIsEmbedded() { return _isEmbedded; }public void setIsEmbedded(boolean value) { if(value) _isEmbedded = new IsEmbedded(); else {_isEmbedded = null;}}protected void copy(SelectProperty instance) {this._flatten = instance._flatten;
this._depth = instance._depth;
this._context = instance._context;
this._root = instance._root;
this._filterProperty = instance._filterProperty; 
this._isEmbedded = instance._isEmbedded; 
}protected void merge(SelectProperty child) {if(child._flatten != null)this._flatten = child._flatten;
if(child._depth != null)this._depth = child._depth;
if(child._context != null)this._context = child._context;
if(child._root != null)this._root = child._root;
if(_filterProperty == null) _filterProperty = child._filterProperty; else if (child._filterProperty != null) {_filterProperty.merge(child._filterProperty);}
if(_isEmbedded == null) _isEmbedded = child._isEmbedded; else if (child._isEmbedded != null) {_isEmbedded.merge(child._isEmbedded);}
}}protected SelectProperty _selectProperty;public SelectProperty getSelect() { return _selectProperty; }public void setSelect(SelectProperty value) { if(_selectProperty!=null) _selectProperty.merge(value); else {_selectProperty = value;} }
	

	public void copy(SelectFieldPropertyBase instance) {
		this._source = instance._source;
this._label = instance._label;
this._description = instance._description;
this._template = instance._template;
this._code = instance._code;
this._name = instance._name;

		this._termsProperty = instance._termsProperty; 
this._allowHistoryProperty = instance._allowHistoryProperty; 
this._enableHistoryProperty = instance._enableHistoryProperty; 
this._allowSearchProperty = instance._allowSearchProperty; 
this._allowOtherProperty = instance._allowOtherProperty; 
this._allowKeyProperty = instance._allowKeyProperty; 
this._selectProperty = instance._selectProperty; 
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

	public void merge(SelectFieldPropertyBase child) {
		super.merge(child);
		
		if(child._source != null)this._source = child._source;

		if(_termsProperty == null) _termsProperty = child._termsProperty; else if (child._termsProperty != null) {_termsProperty.merge(child._termsProperty);}
if(_allowHistoryProperty == null) _allowHistoryProperty = child._allowHistoryProperty; else if (child._allowHistoryProperty != null) {_allowHistoryProperty.merge(child._allowHistoryProperty);}
if(_enableHistoryProperty == null) _enableHistoryProperty = child._enableHistoryProperty; else if (child._enableHistoryProperty != null) {_enableHistoryProperty.merge(child._enableHistoryProperty);}
if(_allowSearchProperty == null) _allowSearchProperty = child._allowSearchProperty; else if (child._allowSearchProperty != null) {_allowSearchProperty.merge(child._allowSearchProperty);}
if(_allowOtherProperty == null) _allowOtherProperty = child._allowOtherProperty; else if (child._allowOtherProperty != null) {_allowOtherProperty.merge(child._allowOtherProperty);}
if(_allowKeyProperty == null) _allowKeyProperty = child._allowKeyProperty; else if (child._allowKeyProperty != null) {_allowKeyProperty.merge(child._allowKeyProperty);}
if(_selectProperty == null) _selectProperty = child._selectProperty; else if (child._selectProperty != null) {_selectProperty.merge(child._selectProperty);}

		
	}

	public Class<?> getMetamodelClass() {
		return SelectFieldPropertyBase.class;
	}

}

