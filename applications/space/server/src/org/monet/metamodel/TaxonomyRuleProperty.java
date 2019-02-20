package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
TaxonomyRuleProperty
Declaración de una regla para la clasificación de los componentes en alguna categoría de una taxonomía

*/

public  class TaxonomyRuleProperty  {

	protected Boolean _exclusive;public boolean exclusive() { return _exclusive != null && _exclusive; }public void setExclusive(boolean value) { _exclusive = value; }
	public static class CheckProperty  {public enum TypeEnumeration { RANGE,VALUE }protected TypeEnumeration _type;public TypeEnumeration getType() { return _type; }public void setType(TypeEnumeration value) { _type = value; }protected org.monet.metamodel.internal.Ref _feature;public org.monet.metamodel.internal.Ref getFeature() { return _feature; }public void setFeature(org.monet.metamodel.internal.Ref value) { _feature = value; }protected Long _from;public Long getFrom() { return _from; }public void setFrom(Long value) { _from = value; }protected Long _to;public Long getTo() { return _to; }public void setTo(Long value) { _to = value; }protected String _value;public String getValue() { return _value; }public void setValue(String value) { _value = value; }protected void copy(CheckProperty instance) {this._type = instance._type;
this._feature = instance._feature;
this._from = instance._from;
this._to = instance._to;
this._value = instance._value;
}protected void merge(CheckProperty child) {if(child._type != null)this._type = child._type;
if(child._feature != null)this._feature = child._feature;
if(child._from != null)this._from = child._from;
if(child._to != null)this._to = child._to;
if(child._value != null)this._value = child._value;
}}protected ArrayList<CheckProperty> _checkPropertyList = new ArrayList<CheckProperty>();public ArrayList<CheckProperty> getCheckList() { return _checkPropertyList; }public static class ReportProperty  {public enum TypeEnumeration { CATEGORY,FEATURE }protected TypeEnumeration _type;public TypeEnumeration getType() { return _type; }public void setType(TypeEnumeration value) { _type = value; }protected String _category;public String getCategory() { return _category; }public void setCategory(String value) { _category = value; }protected org.monet.metamodel.internal.Ref _feature;public org.monet.metamodel.internal.Ref getFeature() { return _feature; }public void setFeature(org.monet.metamodel.internal.Ref value) { _feature = value; }protected void copy(ReportProperty instance) {this._type = instance._type;
this._category = instance._category;
this._feature = instance._feature;
}protected void merge(ReportProperty child) {if(child._type != null)this._type = child._type;
if(child._category != null)this._category = child._category;
if(child._feature != null)this._feature = child._feature;
}}protected ReportProperty _reportProperty;public ReportProperty getReport() { return _reportProperty; }public void setReport(ReportProperty value) { if(_reportProperty!=null) _reportProperty.merge(value); else {_reportProperty = value;} }
	

	public void copy(TaxonomyRuleProperty instance) {
		this._exclusive = instance._exclusive;

		_checkPropertyList.addAll(instance._checkPropertyList);
this._reportProperty = instance._reportProperty; 

		
	}

	public void merge(TaxonomyRuleProperty child) {
		
		
		if(child._exclusive != null)this._exclusive = child._exclusive;

		_checkPropertyList.addAll(child._checkPropertyList);
if(_reportProperty == null) _reportProperty = child._reportProperty; else if (child._reportProperty != null) {_reportProperty.merge(child._reportProperty);}

		
	}

	public Class<?> getMetamodelClass() {
		return TaxonomyRuleProperty.class;
	}

}

