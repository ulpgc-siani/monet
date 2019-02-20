package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
SummationFieldProperty
Esta propiedad se utiliza para incluir un campo sumatorio en un formulario

*/

public  class SummationFieldPropertyBase extends MultipleableFieldProperty {

	protected org.monet.metamodel.internal.Ref _source;public org.monet.metamodel.internal.Ref getSource() { return _source; }public void setSource(org.monet.metamodel.internal.Ref value) { _source = value; }public enum MetricEnumeration { WEIGHT,DISTANCE,AREA,VOLUME,CURRENCY,POWER }protected MetricEnumeration _metric;public MetricEnumeration getMetric() { return _metric; }public void setMetric(MetricEnumeration value) { _metric = value; }protected String _format;public String getFormat() { return _format; }public void setFormat(String value) { _format = value; }
	public static class TermsProperty  {protected ArrayList<SummationItemProperty> _summationItemPropertyList = new ArrayList<SummationItemProperty>();public void addSummationItemProperty(SummationItemProperty value) { _summationItemPropertyList.add(value); }public ArrayList<SummationItemProperty> getSummationItemPropertyList() { return _summationItemPropertyList; }protected void copy(TermsProperty instance) {_summationItemPropertyList.addAll(instance._summationItemPropertyList);}protected void merge(TermsProperty child) {_summationItemPropertyList.addAll(child._summationItemPropertyList);}}protected TermsProperty _termsProperty;public TermsProperty getTerms() { return _termsProperty; }public void setTerms(TermsProperty value) { if(_termsProperty!=null) _termsProperty.merge(value); else {_termsProperty = value;} }public static class ShowProperty  {public enum FlattenEnumeration { NONE,ALL,LEVEL,LEAF,INTERNAL }protected FlattenEnumeration _flatten;public FlattenEnumeration getFlatten() { return _flatten; }public void setFlatten(FlattenEnumeration value) { _flatten = value; }protected Long _depth;public Long getDepth() { return _depth; }public void setDepth(Long value) { _depth = value; }protected String _from;public String getFrom() { return _from; }public void setFrom(String value) { _from = value; }protected void copy(ShowProperty instance) {this._flatten = instance._flatten;
this._depth = instance._depth;
this._from = instance._from;
}protected void merge(ShowProperty child) {if(child._flatten != null)this._flatten = child._flatten;
if(child._depth != null)this._depth = child._depth;
if(child._from != null)this._from = child._from;
}}protected ShowProperty _showProperty;public ShowProperty getShow() { return _showProperty; }public void setShow(ShowProperty value) { if(_showProperty!=null) _showProperty.merge(value); else {_showProperty = value;} }public static class RangeProperty  {protected Long _min;public Long getMin() { return _min; }public void setMin(Long value) { _min = value; }protected Long _max;public Long getMax() { return _max; }public void setMax(Long value) { _max = value; }protected void copy(RangeProperty instance) {this._min = instance._min;
this._max = instance._max;
}protected void merge(RangeProperty child) {if(child._min != null)this._min = child._min;
if(child._max != null)this._max = child._max;
}}protected RangeProperty _rangeProperty;public RangeProperty getRange() { return _rangeProperty; }public void setRange(RangeProperty value) { if(_rangeProperty!=null) _rangeProperty.merge(value); else {_rangeProperty = value;} }
	

	public void copy(SummationFieldPropertyBase instance) {
		this._source = instance._source;
this._metric = instance._metric;
this._format = instance._format;
this._label = instance._label;
this._description = instance._description;
this._code = instance._code;
this._name = instance._name;

		this._termsProperty = instance._termsProperty; 
this._showProperty = instance._showProperty; 
this._rangeProperty = instance._rangeProperty; 
this._isMultiple = instance._isMultiple; 
this._isRequired = instance._isRequired; 
this._isReadonly = instance._isReadonly; 
this._isExtended = instance._isExtended; 
this._isSuperfield = instance._isSuperfield; 
this._isStatic = instance._isStatic; 
this._isUnivocal = instance._isUnivocal; 
_displayPropertyList.addAll(instance._displayPropertyList);

		
	}

	public void merge(SummationFieldPropertyBase child) {
		super.merge(child);
		
		if(child._source != null)this._source = child._source;
if(child._metric != null)this._metric = child._metric;
if(child._format != null)this._format = child._format;

		if(_termsProperty == null) _termsProperty = child._termsProperty; else if (child._termsProperty != null) {_termsProperty.merge(child._termsProperty);}
if(_showProperty == null) _showProperty = child._showProperty; else if (child._showProperty != null) {_showProperty.merge(child._showProperty);}
if(_rangeProperty == null) _rangeProperty = child._rangeProperty; else if (child._rangeProperty != null) {_rangeProperty.merge(child._rangeProperty);}

		
	}

	public Class<?> getMetamodelClass() {
		return SummationFieldPropertyBase.class;
	}

}

