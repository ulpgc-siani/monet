package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
TaskProviderProperty


*/

public  class TaskProviderProperty extends ReferenceableProperty {

	protected org.monet.metamodel.internal.Ref _role;public org.monet.metamodel.internal.Ref getRole() { return _role; }public void setRole(org.monet.metamodel.internal.Ref value) { _role = value; }
	public static class InternalProperty  {protected org.monet.metamodel.internal.Ref _service;public org.monet.metamodel.internal.Ref getService() { return _service; }public void setService(org.monet.metamodel.internal.Ref value) { _service = value; }public static class RequestProperty extends org.monet.metamodel.TaskRequestProperty {protected void copy(RequestProperty instance) {this._code = instance._code;
this._name = instance._name;
}protected void merge(RequestProperty child) {super.merge(child);}}protected LinkedHashMap<String, RequestProperty> _requestPropertyMap = new LinkedHashMap<String, RequestProperty>();public void addRequest(RequestProperty value) {RequestProperty current = _requestPropertyMap.get(value.getName());if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {RequestProperty instance = value.getClass().newInstance();instance.copy(current);instance.setName(value.getName());instance.merge(value);_requestPropertyMap.put(value.getName(), instance);}catch (Exception exception) {}}else current.merge(value);} else {_requestPropertyMap.put(value.getName(), value);} }public java.util.Map<String,RequestProperty> getRequestMap() { return _requestPropertyMap; }public java.util.Collection<RequestProperty> getRequestList() { return _requestPropertyMap.values(); }public static class ResponseProperty extends org.monet.metamodel.TaskResponseProperty {protected void copy(ResponseProperty instance) {this._code = instance._code;
this._name = instance._name;
this._unlock = instance._unlock;
this._goto = instance._goto;
}protected void merge(ResponseProperty child) {super.merge(child);}}protected LinkedHashMap<String, ResponseProperty> _responsePropertyMap = new LinkedHashMap<String, ResponseProperty>();public void addResponse(ResponseProperty value) {ResponseProperty current = _responsePropertyMap.get(value.getName());if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {ResponseProperty instance = value.getClass().newInstance();instance.copy(current);instance.setName(value.getName());instance.merge(value);_responsePropertyMap.put(value.getName(), instance);}catch (Exception exception) {}}else current.merge(value);} else {_responsePropertyMap.put(value.getName(), value);} }public java.util.Map<String,ResponseProperty> getResponseMap() { return _responsePropertyMap; }public java.util.Collection<ResponseProperty> getResponseList() { return _responsePropertyMap.values(); }public static class ExpirationProperty  {protected org.monet.metamodel.internal.Ref _unlock;public org.monet.metamodel.internal.Ref getUnlock() { return _unlock; }public void setUnlock(org.monet.metamodel.internal.Ref value) { _unlock = value; }protected org.monet.metamodel.internal.Ref _goto;public org.monet.metamodel.internal.Ref getGoto() { return _goto; }public void setGoto(org.monet.metamodel.internal.Ref value) { _goto = value; }protected void copy(ExpirationProperty instance) {this._unlock = instance._unlock;
this._goto = instance._goto;
}protected void merge(ExpirationProperty child) {if(child._unlock != null)this._unlock = child._unlock;
if(child._goto != null)this._goto = child._goto;
}}protected ExpirationProperty _expirationProperty;public ExpirationProperty getExpiration() { return _expirationProperty; }public void setExpiration(ExpirationProperty value) { if(_expirationProperty!=null) _expirationProperty.merge(value); else {_expirationProperty = value;} }public static class RejectedProperty  {protected org.monet.metamodel.internal.Ref _unlock;public org.monet.metamodel.internal.Ref getUnlock() { return _unlock; }public void setUnlock(org.monet.metamodel.internal.Ref value) { _unlock = value; }protected org.monet.metamodel.internal.Ref _goto;public org.monet.metamodel.internal.Ref getGoto() { return _goto; }public void setGoto(org.monet.metamodel.internal.Ref value) { _goto = value; }protected void copy(RejectedProperty instance) {this._unlock = instance._unlock;
this._goto = instance._goto;
}protected void merge(RejectedProperty child) {if(child._unlock != null)this._unlock = child._unlock;
if(child._goto != null)this._goto = child._goto;
}}protected RejectedProperty _rejectedProperty;public RejectedProperty getRejected() { return _rejectedProperty; }public void setRejected(RejectedProperty value) { if(_rejectedProperty!=null) _rejectedProperty.merge(value); else {_rejectedProperty = value;} }protected void copy(InternalProperty instance) {this._service = instance._service;
for(RequestProperty item : instance._requestPropertyMap.values())this.addRequest(item);
for(ResponseProperty item : instance._responsePropertyMap.values())this.addResponse(item);
this._expirationProperty = instance._expirationProperty; 
this._rejectedProperty = instance._rejectedProperty; 
}protected void merge(InternalProperty child) {if(child._service != null)this._service = child._service;
for(RequestProperty item : child._requestPropertyMap.values())this.addRequest(item);
for(ResponseProperty item : child._responsePropertyMap.values())this.addResponse(item);
if(_expirationProperty == null) _expirationProperty = child._expirationProperty; else if (child._expirationProperty != null) {_expirationProperty.merge(child._expirationProperty);}
if(_rejectedProperty == null) _rejectedProperty = child._rejectedProperty; else if (child._rejectedProperty != null) {_rejectedProperty.merge(child._rejectedProperty);}
}}protected InternalProperty _internalProperty;public InternalProperty getInternal() { return _internalProperty; }public void setInternal(InternalProperty value) { if(_internalProperty!=null) _internalProperty.merge(value); else {_internalProperty = value;} }public static class ExternalProperty  {public static class RequestProperty extends org.monet.metamodel.TaskRequestProperty {protected void copy(RequestProperty instance) {this._code = instance._code;
this._name = instance._name;
}protected void merge(RequestProperty child) {super.merge(child);}}protected LinkedHashMap<String, RequestProperty> _requestPropertyMap = new LinkedHashMap<String, RequestProperty>();public void addRequest(RequestProperty value) {RequestProperty current = _requestPropertyMap.get(value.getName());if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {RequestProperty instance = value.getClass().newInstance();instance.copy(current);instance.setName(value.getName());instance.merge(value);_requestPropertyMap.put(value.getName(), instance);}catch (Exception exception) {}}else current.merge(value);} else {_requestPropertyMap.put(value.getName(), value);} }public java.util.Map<String,RequestProperty> getRequestMap() { return _requestPropertyMap; }public java.util.Collection<RequestProperty> getRequestList() { return _requestPropertyMap.values(); }public static class ResponseProperty extends org.monet.metamodel.TaskResponseProperty {protected void copy(ResponseProperty instance) {this._code = instance._code;
this._name = instance._name;
this._unlock = instance._unlock;
this._goto = instance._goto;
}protected void merge(ResponseProperty child) {super.merge(child);}}protected LinkedHashMap<String, ResponseProperty> _responsePropertyMap = new LinkedHashMap<String, ResponseProperty>();public void addResponse(ResponseProperty value) {ResponseProperty current = _responsePropertyMap.get(value.getName());if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {ResponseProperty instance = value.getClass().newInstance();instance.copy(current);instance.setName(value.getName());instance.merge(value);_responsePropertyMap.put(value.getName(), instance);}catch (Exception exception) {}}else current.merge(value);} else {_responsePropertyMap.put(value.getName(), value);} }public java.util.Map<String,ResponseProperty> getResponseMap() { return _responsePropertyMap; }public java.util.Collection<ResponseProperty> getResponseList() { return _responsePropertyMap.values(); }public static class ExpirationProperty  {protected org.monet.metamodel.internal.Ref _unlock;public org.monet.metamodel.internal.Ref getUnlock() { return _unlock; }public void setUnlock(org.monet.metamodel.internal.Ref value) { _unlock = value; }protected org.monet.metamodel.internal.Ref _goto;public org.monet.metamodel.internal.Ref getGoto() { return _goto; }public void setGoto(org.monet.metamodel.internal.Ref value) { _goto = value; }protected void copy(ExpirationProperty instance) {this._unlock = instance._unlock;
this._goto = instance._goto;
}protected void merge(ExpirationProperty child) {if(child._unlock != null)this._unlock = child._unlock;
if(child._goto != null)this._goto = child._goto;
}}protected ExpirationProperty _expirationProperty;public ExpirationProperty getExpiration() { return _expirationProperty; }public void setExpiration(ExpirationProperty value) { if(_expirationProperty!=null) _expirationProperty.merge(value); else {_expirationProperty = value;} }public static class RejectedProperty  {protected org.monet.metamodel.internal.Ref _unlock;public org.monet.metamodel.internal.Ref getUnlock() { return _unlock; }public void setUnlock(org.monet.metamodel.internal.Ref value) { _unlock = value; }protected org.monet.metamodel.internal.Ref _goto;public org.monet.metamodel.internal.Ref getGoto() { return _goto; }public void setGoto(org.monet.metamodel.internal.Ref value) { _goto = value; }protected void copy(RejectedProperty instance) {this._unlock = instance._unlock;
this._goto = instance._goto;
}protected void merge(RejectedProperty child) {if(child._unlock != null)this._unlock = child._unlock;
if(child._goto != null)this._goto = child._goto;
}}protected RejectedProperty _rejectedProperty;public RejectedProperty getRejected() { return _rejectedProperty; }public void setRejected(RejectedProperty value) { if(_rejectedProperty!=null) _rejectedProperty.merge(value); else {_rejectedProperty = value;} }protected void copy(ExternalProperty instance) {for(RequestProperty item : instance._requestPropertyMap.values())this.addRequest(item);
for(ResponseProperty item : instance._responsePropertyMap.values())this.addResponse(item);
this._expirationProperty = instance._expirationProperty; 
this._rejectedProperty = instance._rejectedProperty; 
}protected void merge(ExternalProperty child) {for(RequestProperty item : child._requestPropertyMap.values())this.addRequest(item);
for(ResponseProperty item : child._responsePropertyMap.values())this.addResponse(item);
if(_expirationProperty == null) _expirationProperty = child._expirationProperty; else if (child._expirationProperty != null) {_expirationProperty.merge(child._expirationProperty);}
if(_rejectedProperty == null) _rejectedProperty = child._rejectedProperty; else if (child._rejectedProperty != null) {_rejectedProperty.merge(child._rejectedProperty);}
}}protected ExternalProperty _externalProperty;public ExternalProperty getExternal() { return _externalProperty; }public void setExternal(ExternalProperty value) { if(_externalProperty!=null) _externalProperty.merge(value); else {_externalProperty = value;} }
	

	public void copy(TaskProviderProperty instance) {
		this._role = instance._role;
this._code = instance._code;
this._name = instance._name;

		this._internalProperty = instance._internalProperty; 
this._externalProperty = instance._externalProperty; 

		
	}

	public void merge(TaskProviderProperty child) {
		super.merge(child);
		
		if(child._role != null)this._role = child._role;

		if(_internalProperty == null) _internalProperty = child._internalProperty; else if (child._internalProperty != null) {_internalProperty.merge(child._internalProperty);}
if(_externalProperty == null) _externalProperty = child._externalProperty; else if (child._externalProperty != null) {_externalProperty.merge(child._externalProperty);}

		
	}

	public Class<?> getMetamodelClass() {
		return TaskProviderProperty.class;
	}

}

