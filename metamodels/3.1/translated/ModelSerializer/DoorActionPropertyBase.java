package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
DoorActionProperty


*/

public  class DoorActionPropertyBase extends PlaceActionProperty {

	
	public static class TimeoutProperty  {protected org.monet.metamodel.internal.Time _after;public org.monet.metamodel.internal.Time getAfter() { return _after; }public void setAfter(org.monet.metamodel.internal.Time value) { _after = value; }protected org.monet.metamodel.internal.Ref _take;public org.monet.metamodel.internal.Ref getTake() { return _take; }public void setTake(org.monet.metamodel.internal.Ref value) { _take = value; }protected void copy(TimeoutProperty instance) {this._after = instance._after;
this._take = instance._take;
}protected void merge(TimeoutProperty child) {if(child._after != null)this._after = child._after;
if(child._take != null)this._take = child._take;
}}protected TimeoutProperty _timeoutProperty;public TimeoutProperty getTimeout() { return _timeoutProperty; }public void setTimeout(TimeoutProperty value) { if(_timeoutProperty!=null) _timeoutProperty.merge(value); else {_timeoutProperty = value;} }public static class ExitProperty extends org.monet.metamodel.ReferenceableProperty {protected org.monet.metamodel.internal.Ref _goto;public org.monet.metamodel.internal.Ref getGoto() { return _goto; }public void setGoto(org.monet.metamodel.internal.Ref value) { _goto = value; }protected Object _history;public Object getHistory() { return _history; }public void setHistory(Object value) { _history = value; }protected void copy(ExitProperty instance) {this._goto = instance._goto;
this._history = instance._history;
this._code = instance._code;
this._name = instance._name;
}protected void merge(ExitProperty child) {super.merge(child);if(child._goto != null)this._goto = child._goto;
if(child._history != null)this._history = child._history;
}}protected LinkedHashMap<String, ExitProperty> _exitPropertyMap = new LinkedHashMap<String, ExitProperty>();public void addExit(ExitProperty value) {String key = value.getName() != null ? value.getName() : value.getCode();ExitProperty current = _exitPropertyMap.get(key);if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {ExitProperty instance = value.getClass().newInstance();instance.copy(current);instance.setCode(value.getCode());instance.setName(value.getName());instance.merge(value);_exitPropertyMap.put(key, instance);}catch (Exception exception) {}}else current.merge(value);} else {_exitPropertyMap.put(key, value);} }public java.util.Map<String,ExitProperty> getExitMap() { return _exitPropertyMap; }public java.util.Collection<ExitProperty> getExitList() { return _exitPropertyMap.values(); }
	

	public void copy(DoorActionPropertyBase instance) {
		this._name = instance._name;
this._label = instance._label;
this._code = instance._code;
this._name = instance._name;

		this._timeoutProperty = instance._timeoutProperty; 
for(ExitProperty item : instance._exitPropertyMap.values())this.addExit(item);

		
	}

	public void merge(DoorActionPropertyBase child) {
		super.merge(child);
		
		
		if(_timeoutProperty == null) _timeoutProperty = child._timeoutProperty; else if (child._timeoutProperty != null) {_timeoutProperty.merge(child._timeoutProperty);}
for(ExitProperty item : child._exitPropertyMap.values())this.addExit(item);

		
	}

	public Class<?> getMetamodelClass() {
		return DoorActionPropertyBase.class;
	}

}

