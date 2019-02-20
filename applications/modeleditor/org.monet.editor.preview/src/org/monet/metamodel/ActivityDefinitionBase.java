package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
ActivityDefinition
Una tarea es una trabajo colectivo o individual que se desarrolla en la unidad de negocio

*/

public  class ActivityDefinitionBase extends ProcessDefinition {

	
	public static class ActivityPlacePropertyBase extends org.monet.metamodel.PlaceProperty {protected void copy(ActivityPlacePropertyBase instance) {this._code = instance._code;
this._name = instance._name;
this._isInitial = instance._isInitial; 
this._isOust = instance._isOust; 
this._isFinal = instance._isFinal; 
this._backEnableProperty = instance._backEnableProperty; 
this.setEditionActionProperty(instance._editionActionProperty);this.setLineActionProperty(instance._lineActionProperty);this.setDoorActionProperty(instance._doorActionProperty);this.setSendResponseActionProperty(instance._sendResponseActionProperty);this.setWaitActionProperty(instance._waitActionProperty);this.setDelegationActionProperty(instance._delegationActionProperty);this.setEnrollActionProperty(instance._enrollActionProperty);this.setSendJobActionProperty(instance._sendJobActionProperty);this.setSendRequestActionProperty(instance._sendRequestActionProperty);}protected void merge(ActivityPlacePropertyBase child) {super.merge(child);}}protected LinkedHashMap<String, ActivityDefinition.ActivityPlaceProperty> _activityPlacePropertyMap = new LinkedHashMap<String, ActivityDefinition.ActivityPlaceProperty>();public void addPlace(ActivityDefinition.ActivityPlaceProperty value) {String key = value.getName() != null ? value.getName() : value.getCode();ActivityDefinition.ActivityPlaceProperty current = _activityPlacePropertyMap.get(key);if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {ActivityDefinition.ActivityPlaceProperty instance = value.getClass().newInstance();instance.copy(current);instance.setCode(value.getCode());instance.setName(value.getName());instance.merge(value);_activityPlacePropertyMap.put(key, instance);}catch (Exception exception) {}}else current.merge(value);} else {_activityPlacePropertyMap.put(key, value);} }public java.util.Map<String,ActivityDefinition.ActivityPlaceProperty> getPlaceMap() { return _activityPlacePropertyMap; }public java.util.Collection<ActivityDefinition.ActivityPlaceProperty> getPlaceList() { return _activityPlacePropertyMap.values(); }
	

	public void copy(ActivityDefinitionBase instance) {
		this._target = instance._target;
this._role = instance._role;
this._code = instance._code;
this._name = instance._name;
this._parent = instance._parent;
this._label = instance._label;
this._description = instance._description;
this._help = instance._help;

		for(ActivityDefinition.ActivityPlaceProperty item : instance._activityPlacePropertyMap.values())this.addPlace(item);
for(ShortcutProperty item : instance._shortcutPropertyMap.values())this.addShortcut(item);
this._isManual = instance._isManual; 
for(ViewProperty item : instance._viewPropertyMap.values())this.addView(item);
this._isPrivate = instance._isPrivate; 
this._isBackground = instance._isBackground; 
this._isAbstract = instance._isAbstract; 

		for(TaskContestProperty item : instance._taskContestPropertyMap.values())this.addTaskContestProperty(item);for(TaskProviderProperty item : instance._taskProviderPropertyMap.values())this.addTaskProviderProperty(item);
	}

	public void merge(ActivityDefinitionBase child) {
		super.merge(child);
		
		
		for(ActivityDefinition.ActivityPlaceProperty item : child._activityPlacePropertyMap.values())this.addPlace(item);

		
	}

	public Class<?> getMetamodelClass() {
		return ActivityDefinitionBase.class;
	}

}

