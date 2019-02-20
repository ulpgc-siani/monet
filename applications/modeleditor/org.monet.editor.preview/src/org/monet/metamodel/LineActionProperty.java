package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
LineActionProperty


*/

public  class LineActionProperty extends PlaceActionProperty {

	
	public static class TimeoutProperty  {protected org.monet.metamodel.internal.Time _after;public org.monet.metamodel.internal.Time getAfter() { return _after; }public void setAfter(org.monet.metamodel.internal.Time value) { _after = value; }protected org.monet.metamodel.internal.Ref _take;public org.monet.metamodel.internal.Ref getTake() { return _take; }public void setTake(org.monet.metamodel.internal.Ref value) { _take = value; }protected void merge(TimeoutProperty child) {if(child._after != null)this._after = child._after;
if(child._take != null)this._take = child._take;
}}protected TimeoutProperty _timeoutProperty;public TimeoutProperty getTimeout() { return _timeoutProperty; }public void setTimeout(TimeoutProperty value) { if(_timeoutProperty!=null) _timeoutProperty.merge(value); else {_timeoutProperty = value;} }public static class LineStopProperty extends org.monet.metamodel.ReferenceableProperty {protected Object _label;public Object getLabel() { return _label; }public void setLabel(Object value) { _label = value; }protected Object _help;public Object getHelp() { return _help; }public void setHelp(Object value) { _help = value; }protected org.monet.metamodel.internal.Ref _goto;public org.monet.metamodel.internal.Ref getGoto() { return _goto; }public void setGoto(org.monet.metamodel.internal.Ref value) { _goto = value; }protected Object _history;public Object getHistory() { return _history; }public void setHistory(Object value) { _history = value; }public static class IsDefault  {protected void merge(IsDefault child) {}}protected IsDefault _isDefault;public boolean isDefault() { return (_isDefault != null); }public IsDefault getIsDefault() { return _isDefault; }public void setIsDefault(boolean value) { if(value) _isDefault = new IsDefault(); else {_isDefault = null;}}protected void merge(LineStopProperty child) {super.merge(child);if(child._label != null)this._label = child._label;
if(child._help != null)this._help = child._help;
if(child._goto != null)this._goto = child._goto;
if(child._history != null)this._history = child._history;
if(_isDefault == null) _isDefault = child._isDefault; else {_isDefault.merge(child._isDefault);}
}}protected LinkedHashMap<String, LineStopProperty> _lineStopPropertyMap = new LinkedHashMap<String, LineStopProperty>();public void addStop(LineStopProperty value) {String key = value.getName() != null ? value.getName() : value.getCode();LineStopProperty current = _lineStopPropertyMap.get(key);if(current != null) {current.merge(value);} else {_lineStopPropertyMap.put(key, value);} }public java.util.Map<String,LineStopProperty> getStopMap() { return _lineStopPropertyMap; }public java.util.Collection<LineStopProperty> getStopList() { return _lineStopPropertyMap.values(); }
	

	public void merge(LineActionProperty child) {
		super.merge(child);
		
		
		if(_timeoutProperty == null) _timeoutProperty = child._timeoutProperty; else {_timeoutProperty.merge(child._timeoutProperty);}
for(LineStopProperty item : child._lineStopPropertyMap.values())this.addStop(item);

		
	}

	public Class<?> getMetamodelClass() {
		return LineActionProperty.class;
	}

}

