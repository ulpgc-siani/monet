package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
ServiceDefinition
Una tarea es una trabajo colectivo o individual que se desarrolla en la unidad de negocio

*/

public  class ServiceDefinitionBase extends ProcessDefinition {

	protected String _ontology;public String getOntology() { return _ontology; }public void setOntology(String value) { _ontology = value; }
	public static class CustomerPropertyBase  {public static class CustomerRequestProperty  {protected String _code;public String getCode() { return _code; }public void setCode(String value) { _code = value; }protected String _name;public String getName() { return _name; }public void setName(String value) { _name = value; }protected org.monet.metamodel.internal.Ref _unlock;public org.monet.metamodel.internal.Ref getUnlock() { return _unlock; }public void setUnlock(org.monet.metamodel.internal.Ref value) { _unlock = value; }protected org.monet.metamodel.internal.Ref _goto;public org.monet.metamodel.internal.Ref getGoto() { return _goto; }public void setGoto(org.monet.metamodel.internal.Ref value) { _goto = value; }protected void copy(CustomerRequestProperty instance) {this._code = instance._code;
this._name = instance._name;
this._unlock = instance._unlock;
this._goto = instance._goto;
}protected void merge(CustomerRequestProperty child) {if(child._code != null)this._code = child._code;
if(child._name != null)this._name = child._name;
if(child._unlock != null)this._unlock = child._unlock;
if(child._goto != null)this._goto = child._goto;
}}protected LinkedHashMap<String, CustomerRequestProperty> _customerRequestPropertyMap = new LinkedHashMap<String, CustomerRequestProperty>();public void addRequest(CustomerRequestProperty value) {CustomerRequestProperty current = _customerRequestPropertyMap.get(value.getName());if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {CustomerRequestProperty instance = value.getClass().newInstance();instance.copy(current);instance.setName(value.getName());instance.merge(value);_customerRequestPropertyMap.put(value.getName(), instance);}catch (Exception exception) {}}else current.merge(value);} else {_customerRequestPropertyMap.put(value.getName(), value);} }public java.util.Map<String,CustomerRequestProperty> getRequestMap() { return _customerRequestPropertyMap; }public java.util.Collection<CustomerRequestProperty> getRequestList() { return _customerRequestPropertyMap.values(); }public static class CustomerResponseProperty  {protected String _code;public String getCode() { return _code; }public void setCode(String value) { _code = value; }protected String _name;public String getName() { return _name; }public void setName(String value) { _name = value; }protected void copy(CustomerResponseProperty instance) {this._code = instance._code;
this._name = instance._name;
}protected void merge(CustomerResponseProperty child) {if(child._code != null)this._code = child._code;
if(child._name != null)this._name = child._name;
}}protected LinkedHashMap<String, CustomerResponseProperty> _customerResponsePropertyMap = new LinkedHashMap<String, CustomerResponseProperty>();public void addResponse(CustomerResponseProperty value) {CustomerResponseProperty current = _customerResponsePropertyMap.get(value.getName());if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {CustomerResponseProperty instance = value.getClass().newInstance();instance.copy(current);instance.setName(value.getName());instance.merge(value);_customerResponsePropertyMap.put(value.getName(), instance);}catch (Exception exception) {}}else current.merge(value);} else {_customerResponsePropertyMap.put(value.getName(), value);} }public java.util.Map<String,CustomerResponseProperty> getResponseMap() { return _customerResponsePropertyMap; }public java.util.Collection<CustomerResponseProperty> getResponseList() { return _customerResponsePropertyMap.values(); }public static class ExpirationProperty  {protected org.monet.metamodel.internal.Ref _unlock;public org.monet.metamodel.internal.Ref getUnlock() { return _unlock; }public void setUnlock(org.monet.metamodel.internal.Ref value) { _unlock = value; }protected org.monet.metamodel.internal.Ref _goto;public org.monet.metamodel.internal.Ref getGoto() { return _goto; }public void setGoto(org.monet.metamodel.internal.Ref value) { _goto = value; }protected void copy(ExpirationProperty instance) {this._unlock = instance._unlock;
this._goto = instance._goto;
}protected void merge(ExpirationProperty child) {if(child._unlock != null)this._unlock = child._unlock;
if(child._goto != null)this._goto = child._goto;
}}protected ExpirationProperty _expirationProperty;public ExpirationProperty getExpiration() { return _expirationProperty; }public void setExpiration(ExpirationProperty value) { if(_expirationProperty!=null) _expirationProperty.merge(value); else {_expirationProperty = value;} }public static class AbortedProperty  {protected org.monet.metamodel.internal.Ref _unlock;public org.monet.metamodel.internal.Ref getUnlock() { return _unlock; }public void setUnlock(org.monet.metamodel.internal.Ref value) { _unlock = value; }protected org.monet.metamodel.internal.Ref _goto;public org.monet.metamodel.internal.Ref getGoto() { return _goto; }public void setGoto(org.monet.metamodel.internal.Ref value) { _goto = value; }protected void copy(AbortedProperty instance) {this._unlock = instance._unlock;
this._goto = instance._goto;
}protected void merge(AbortedProperty child) {if(child._unlock != null)this._unlock = child._unlock;
if(child._goto != null)this._goto = child._goto;
}}protected AbortedProperty _abortedProperty;public AbortedProperty getAborted() { return _abortedProperty; }public void setAborted(AbortedProperty value) { if(_abortedProperty!=null) _abortedProperty.merge(value); else {_abortedProperty = value;} }protected void copy(CustomerPropertyBase instance) {for(CustomerRequestProperty item : instance._customerRequestPropertyMap.values())this.addRequest(item);
for(CustomerResponseProperty item : instance._customerResponsePropertyMap.values())this.addResponse(item);
this._expirationProperty = instance._expirationProperty; 
this._abortedProperty = instance._abortedProperty; 
}protected void merge(CustomerPropertyBase child) {for(CustomerRequestProperty item : child._customerRequestPropertyMap.values())this.addRequest(item);
for(CustomerResponseProperty item : child._customerResponsePropertyMap.values())this.addResponse(item);
if(_expirationProperty == null) _expirationProperty = child._expirationProperty; else if (child._expirationProperty != null) {_expirationProperty.merge(child._expirationProperty);}
if(_abortedProperty == null) _abortedProperty = child._abortedProperty; else if (child._abortedProperty != null) {_abortedProperty.merge(child._abortedProperty);}
}}protected ServiceDefinition.CustomerProperty _customerProperty;public ServiceDefinition.CustomerProperty getCustomer() { return _customerProperty; }public void setCustomer(ServiceDefinition.CustomerProperty value) { if(_customerProperty!=null) _customerProperty.merge(value); else {_customerProperty = value;} }public static class ServicePlacePropertyBase extends org.monet.metamodel.PlaceProperty {protected void copy(ServicePlacePropertyBase instance) {this._code = instance._code;
this._name = instance._name;
this._isInitial = instance._isInitial; 
this._isOust = instance._isOust; 
this._isFinal = instance._isFinal; 
this._backEnableProperty = instance._backEnableProperty; 
this.setWaitActionProperty(instance._waitActionProperty);this.setSendJobActionProperty(instance._sendJobActionProperty);this.setSendRequestActionProperty(instance._sendRequestActionProperty);this.setLineActionProperty(instance._lineActionProperty);this.setEditionActionProperty(instance._editionActionProperty);this.setEnrollActionProperty(instance._enrollActionProperty);this.setSendResponseActionProperty(instance._sendResponseActionProperty);this.setDelegationActionProperty(instance._delegationActionProperty);this.setDoorActionProperty(instance._doorActionProperty);}protected void merge(ServicePlacePropertyBase child) {super.merge(child);}}protected LinkedHashMap<String, ServiceDefinition.ServicePlaceProperty> _servicePlacePropertyMap = new LinkedHashMap<String, ServiceDefinition.ServicePlaceProperty>();public void addPlace(ServiceDefinition.ServicePlaceProperty value) {String key = value.getName() != null ? value.getName() : value.getCode();ServiceDefinition.ServicePlaceProperty current = _servicePlacePropertyMap.get(key);if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {ServiceDefinition.ServicePlaceProperty instance = value.getClass().newInstance();instance.copy(current);instance.setCode(value.getCode());instance.setName(value.getName());instance.merge(value);_servicePlacePropertyMap.put(key, instance);}catch (Exception exception) {}}else current.merge(value);} else {_servicePlacePropertyMap.put(key, value);} }public java.util.Map<String,ServiceDefinition.ServicePlaceProperty> getPlaceMap() { return _servicePlacePropertyMap; }public java.util.Collection<ServiceDefinition.ServicePlaceProperty> getPlaceList() { return _servicePlacePropertyMap.values(); }
	

	public void copy(ServiceDefinitionBase instance) {
		this._ontology = instance._ontology;
this._target = instance._target;
this._role = instance._role;
this._code = instance._code;
this._name = instance._name;
this._parent = instance._parent;
this._label = instance._label;
this._description = instance._description;
this._help = instance._help;

		this._customerProperty = instance._customerProperty; 
for(ServiceDefinition.ServicePlaceProperty item : instance._servicePlacePropertyMap.values())this.addPlace(item);
for(ShortcutProperty item : instance._shortcutPropertyMap.values())this.addShortcut(item);
this._isManual = instance._isManual; 
for(ViewProperty item : instance._viewPropertyMap.values())this.addView(item);
this._isPrivate = instance._isPrivate; 
this._isAbstract = instance._isAbstract; 

		for(TaskContestProperty item : instance._taskContestPropertyMap.values())this.addTaskContestProperty(item);for(TaskProviderProperty item : instance._taskProviderPropertyMap.values())this.addTaskProviderProperty(item);
	}

	public void merge(ServiceDefinitionBase child) {
		super.merge(child);
		
		if(child._ontology != null)this._ontology = child._ontology;

		if(_customerProperty == null) _customerProperty = child._customerProperty; else if (child._customerProperty != null) {_customerProperty.merge(child._customerProperty);}
for(ServiceDefinition.ServicePlaceProperty item : child._servicePlacePropertyMap.values())this.addPlace(item);

		
	}

	public Class<?> getMetamodelClass() {
		return ServiceDefinitionBase.class;
	}

}

