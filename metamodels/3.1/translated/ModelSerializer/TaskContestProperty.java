package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
TaskContestProperty


*/

public  class TaskContestProperty extends ReferenceableProperty {

	protected org.monet.metamodel.internal.Ref _activity;public org.monet.metamodel.internal.Ref getActivity() { return _activity; }public void setActivity(org.monet.metamodel.internal.Ref value) { _activity = value; }
	public static class RequestProperty extends org.monet.metamodel.TaskRequestProperty {protected void copy(RequestProperty instance) {this._code = instance._code;
this._name = instance._name;
}protected void merge(RequestProperty child) {super.merge(child);}}protected LinkedHashMap<String, RequestProperty> _requestPropertyMap = new LinkedHashMap<String, RequestProperty>();public void addRequest(RequestProperty value) {RequestProperty current = _requestPropertyMap.get(value.getName());if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {RequestProperty instance = value.getClass().newInstance();instance.copy(current);instance.setName(value.getName());instance.merge(value);_requestPropertyMap.put(value.getName(), instance);}catch (Exception exception) {}}else current.merge(value);} else {_requestPropertyMap.put(value.getName(), value);} }public java.util.Map<String,RequestProperty> getRequestMap() { return _requestPropertyMap; }public java.util.Collection<RequestProperty> getRequestList() { return _requestPropertyMap.values(); }public static class ResponseProperty extends org.monet.metamodel.TaskResponseProperty {protected void copy(ResponseProperty instance) {this._code = instance._code;
this._name = instance._name;
this._unlock = instance._unlock;
this._goto = instance._goto;
}protected void merge(ResponseProperty child) {super.merge(child);}}protected LinkedHashMap<String, ResponseProperty> _responsePropertyMap = new LinkedHashMap<String, ResponseProperty>();public void addResponse(ResponseProperty value) {ResponseProperty current = _responsePropertyMap.get(value.getName());if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {ResponseProperty instance = value.getClass().newInstance();instance.copy(current);instance.setName(value.getName());instance.merge(value);_responsePropertyMap.put(value.getName(), instance);}catch (Exception exception) {}}else current.merge(value);} else {_responsePropertyMap.put(value.getName(), value);} }public java.util.Map<String,ResponseProperty> getResponseMap() { return _responsePropertyMap; }public java.util.Collection<ResponseProperty> getResponseList() { return _responsePropertyMap.values(); }public static class AbortedProperty  {protected org.monet.metamodel.internal.Ref _unlock;public org.monet.metamodel.internal.Ref getUnlock() { return _unlock; }public void setUnlock(org.monet.metamodel.internal.Ref value) { _unlock = value; }protected org.monet.metamodel.internal.Ref _goto;public org.monet.metamodel.internal.Ref getGoto() { return _goto; }public void setGoto(org.monet.metamodel.internal.Ref value) { _goto = value; }protected void copy(AbortedProperty instance) {this._unlock = instance._unlock;
this._goto = instance._goto;
}protected void merge(AbortedProperty child) {if(child._unlock != null)this._unlock = child._unlock;
if(child._goto != null)this._goto = child._goto;
}}protected AbortedProperty _abortedProperty;public AbortedProperty getAborted() { return _abortedProperty; }public void setAborted(AbortedProperty value) { if(_abortedProperty!=null) _abortedProperty.merge(value); else {_abortedProperty = value;} }
	

	public void copy(TaskContestProperty instance) {
		this._activity = instance._activity;
this._code = instance._code;
this._name = instance._name;

		for(RequestProperty item : instance._requestPropertyMap.values())this.addRequest(item);
for(ResponseProperty item : instance._responsePropertyMap.values())this.addResponse(item);
this._abortedProperty = instance._abortedProperty; 

		
	}

	public void merge(TaskContestProperty child) {
		super.merge(child);
		
		if(child._activity != null)this._activity = child._activity;

		for(RequestProperty item : child._requestPropertyMap.values())this.addRequest(item);
for(ResponseProperty item : child._responsePropertyMap.values())this.addResponse(item);
if(_abortedProperty == null) _abortedProperty = child._abortedProperty; else if (child._abortedProperty != null) {_abortedProperty.merge(child._abortedProperty);}

		
	}

	public Class<?> getMetamodelClass() {
		return TaskContestProperty.class;
	}

}

