package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
TermProperty
Esta propiedad se utiliza para añadir un término

*/

public  class TermProperty  {

	protected Object _label;public Object getLabel() { return _label; }public void setLabel(Object value) { _label = value; }protected String _key;public String getKey() { return _key; }public void setKey(String value) { _key = value; }protected ArrayList<String> _tag = new ArrayList<String>();public ArrayList<String> getTag() { return _tag; }public void setTag(ArrayList<String> value) { _tag = value; }
	public static class IsCategory  {protected void copy(IsCategory instance) {}protected void merge(IsCategory child) {}}protected IsCategory _isCategory;public boolean isCategory() { return (_isCategory != null); }public IsCategory getIsCategory() { return _isCategory; }public void setIsCategory(boolean value) { if(value) _isCategory = new IsCategory(); else {_isCategory = null;}}
	protected ArrayList<TermProperty> _termPropertyList = new ArrayList<TermProperty>();public void addTermProperty(TermProperty value) { _termPropertyList.add(value); }public ArrayList<TermProperty> getTermPropertyList() { return _termPropertyList; }

	public void copy(TermProperty instance) {
		this._label = instance._label;
this._key = instance._key;
this._tag.addAll(instance._tag);

		this._isCategory = instance._isCategory; 

		_termPropertyList.addAll(instance._termPropertyList);
	}

	public void merge(TermProperty child) {
		
		
		if(child._label != null)this._label = child._label;
if(child._key != null)this._key = child._key;
if(child._tag != null)this._tag.addAll(child._tag);

		if(_isCategory == null) _isCategory = child._isCategory; else if (child._isCategory != null) {_isCategory.merge(child._isCategory);}

		_termPropertyList.addAll(child._termPropertyList);
	}

	public Class<?> getMetamodelClass() {
		return TermProperty.class;
	}

}

