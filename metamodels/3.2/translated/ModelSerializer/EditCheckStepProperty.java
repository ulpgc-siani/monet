package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
EditCheckStepProperty


*/

public  class EditCheckStepProperty extends StepEditProperty {

	protected org.monet.metamodel.internal.Ref _source;public org.monet.metamodel.internal.Ref getSource() { return _source; }public void setSource(org.monet.metamodel.internal.Ref value) { _source = value; }
	public static class TermsProperty  {protected ArrayList<TermProperty> _termPropertyList = new ArrayList<TermProperty>();public void addTermProperty(TermProperty value) { _termPropertyList.add(value); }public ArrayList<TermProperty> getTermPropertyList() { return _termPropertyList; }protected void copy(TermsProperty instance) {_termPropertyList.addAll(instance._termPropertyList);}protected void merge(TermsProperty child) {_termPropertyList.addAll(child._termPropertyList);}}protected TermsProperty _termsProperty;public TermsProperty getTerms() { return _termsProperty; }public void setTerms(TermsProperty value) { if(_termsProperty!=null) _termsProperty.merge(value); else {_termsProperty = value;} }public static class ShowProperty  {public enum FlattenEnumeration { NONE,ALL,LEVEL,LEAF,INTERNAL }protected FlattenEnumeration _flatten;public FlattenEnumeration getFlatten() { return _flatten; }public void setFlatten(FlattenEnumeration value) { _flatten = value; }protected Long _depth;public Long getDepth() { return _depth; }public void setDepth(Long value) { _depth = value; }protected String _root;public String getRoot() { return _root; }public void setRoot(String value) { _root = value; }public static class FilterProperty  {protected ArrayList<Object> _tag = new ArrayList<Object>();public ArrayList<Object> getTag() { return _tag; }public void setTag(ArrayList<Object> value) { _tag = value; }protected void copy(FilterProperty instance) {this._tag.addAll(instance._tag);
}protected void merge(FilterProperty child) {if(child._tag != null)this._tag.addAll(child._tag);
}}protected FilterProperty _filterProperty;public FilterProperty getFilter() { return _filterProperty; }public void setFilter(FilterProperty value) { if(_filterProperty!=null) _filterProperty.merge(value); else {_filterProperty = value;} }protected void copy(ShowProperty instance) {this._flatten = instance._flatten;
this._depth = instance._depth;
this._root = instance._root;
this._filterProperty = instance._filterProperty; 
}protected void merge(ShowProperty child) {if(child._flatten != null)this._flatten = child._flatten;
if(child._depth != null)this._depth = child._depth;
if(child._root != null)this._root = child._root;
if(_filterProperty == null) _filterProperty = child._filterProperty; else if (child._filterProperty != null) {_filterProperty.merge(child._filterProperty);}
}}protected ShowProperty _showProperty;public ShowProperty getShow() { return _showProperty; }public void setShow(ShowProperty value) { if(_showProperty!=null) _showProperty.merge(value); else {_showProperty = value;} }
	

	public void copy(EditCheckStepProperty instance) {
		this._source = instance._source;
this._label = instance._label;
this._code = instance._code;
this._name = instance._name;

		this._termsProperty = instance._termsProperty; 
this._showProperty = instance._showProperty; 
this._isRequired = instance._isRequired; 
this._isReadOnly = instance._isReadOnly; 
this._isMultiple = instance._isMultiple; 

		
	}

	public void merge(EditCheckStepProperty child) {
		super.merge(child);
		
		if(child._source != null)this._source = child._source;

		if(_termsProperty == null) _termsProperty = child._termsProperty; else if (child._termsProperty != null) {_termsProperty.merge(child._termsProperty);}
if(_showProperty == null) _showProperty = child._showProperty; else if (child._showProperty != null) {_showProperty.merge(child._showProperty);}

		
	}

	public Class<?> getMetamodelClass() {
		return EditCheckStepProperty.class;
	}

}

