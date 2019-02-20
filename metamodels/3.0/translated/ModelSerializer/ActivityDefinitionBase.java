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

	
	public static class ContestantsPropertyBase  {public static class ContestantRequestProperty  {protected String _code;public String getCode() { return _code; }public void setCode(String value) { _code = value; }protected String _name;public String getName() { return _name; }public void setName(String value) { _name = value; }protected org.monet.metamodel.internal.Ref _unlock;public org.monet.metamodel.internal.Ref getUnlock() { return _unlock; }public void setUnlock(org.monet.metamodel.internal.Ref value) { _unlock = value; }protected org.monet.metamodel.internal.Ref _goto;public org.monet.metamodel.internal.Ref getGoto() { return _goto; }public void setGoto(org.monet.metamodel.internal.Ref value) { _goto = value; }protected void copy(ContestantRequestProperty instance) {this._code = instance._code;
this._name = instance._name;
this._unlock = instance._unlock;
this._goto = instance._goto;
}protected void merge(ContestantRequestProperty child) {if(child._code != null)this._code = child._code;
if(child._name != null)this._name = child._name;
if(child._unlock != null)this._unlock = child._unlock;
if(child._goto != null)this._goto = child._goto;
}}protected LinkedHashMap<String, ContestantRequestProperty> _contestantRequestPropertyMap = new LinkedHashMap<String, ContestantRequestProperty>();public void addRequest(ContestantRequestProperty value) {ContestantRequestProperty current = _contestantRequestPropertyMap.get(value.getName());if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {ContestantRequestProperty instance = value.getClass().newInstance();instance.copy(current);instance.setName(value.getName());instance.merge(value);_contestantRequestPropertyMap.put(value.getName(), instance);}catch (Exception exception) {}}else current.merge(value);} else {_contestantRequestPropertyMap.put(value.getName(), value);} }public java.util.Map<String,ContestantRequestProperty> getRequestMap() { return _contestantRequestPropertyMap; }public java.util.Collection<ContestantRequestProperty> getRequestList() { return _contestantRequestPropertyMap.values(); }public static class ContestantResponseProperty  {protected String _code;public String getCode() { return _code; }public void setCode(String value) { _code = value; }protected String _name;public String getName() { return _name; }public void setName(String value) { _name = value; }protected void copy(ContestantResponseProperty instance) {this._code = instance._code;
this._name = instance._name;
}protected void merge(ContestantResponseProperty child) {if(child._code != null)this._code = child._code;
if(child._name != null)this._name = child._name;
}}protected LinkedHashMap<String, ContestantResponseProperty> _contestantResponsePropertyMap = new LinkedHashMap<String, ContestantResponseProperty>();public void addResponse(ContestantResponseProperty value) {ContestantResponseProperty current = _contestantResponsePropertyMap.get(value.getName());if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {ContestantResponseProperty instance = value.getClass().newInstance();instance.copy(current);instance.setName(value.getName());instance.merge(value);_contestantResponsePropertyMap.put(value.getName(), instance);}catch (Exception exception) {}}else current.merge(value);} else {_contestantResponsePropertyMap.put(value.getName(), value);} }public java.util.Map<String,ContestantResponseProperty> getResponseMap() { return _contestantResponsePropertyMap; }public java.util.Collection<ContestantResponseProperty> getResponseList() { return _contestantResponsePropertyMap.values(); }protected void copy(ContestantsPropertyBase instance) {for(ContestantRequestProperty item : instance._contestantRequestPropertyMap.values())this.addRequest(item);
for(ContestantResponseProperty item : instance._contestantResponsePropertyMap.values())this.addResponse(item);
}protected void merge(ContestantsPropertyBase child) {for(ContestantRequestProperty item : child._contestantRequestPropertyMap.values())this.addRequest(item);
for(ContestantResponseProperty item : child._contestantResponsePropertyMap.values())this.addResponse(item);
}}protected ActivityDefinition.ContestantsProperty _contestantsProperty;public ActivityDefinition.ContestantsProperty getContestants() { return _contestantsProperty; }public void setContestants(ActivityDefinition.ContestantsProperty value) { if(_contestantsProperty!=null) _contestantsProperty.merge(value); else {_contestantsProperty = value;} }public static class ActivityPlacePropertyBase extends org.monet.metamodel.PlaceProperty {public static class OpenContestProperty  {protected void copy(OpenContestProperty instance) {}protected void merge(OpenContestProperty child) {}}protected OpenContestProperty _openContestProperty;public OpenContestProperty getOpenContest() { return _openContestProperty; }public void setOpenContest(OpenContestProperty value) { if(_openContestProperty!=null) _openContestProperty.merge(value); else {_openContestProperty = value;} }public static class CloseContestProperty  {protected void copy(CloseContestProperty instance) {}protected void merge(CloseContestProperty child) {}}protected CloseContestProperty _closeContestProperty;public CloseContestProperty getCloseContest() { return _closeContestProperty; }public void setCloseContest(CloseContestProperty value) { if(_closeContestProperty!=null) _closeContestProperty.merge(value); else {_closeContestProperty = value;} }protected void copy(ActivityPlacePropertyBase instance) {this._code = instance._code;
this._name = instance._name;
this._openContestProperty = instance._openContestProperty; 
this._closeContestProperty = instance._closeContestProperty; 
this._isInitial = instance._isInitial; 
this._isOust = instance._isOust; 
this._isFinal = instance._isFinal; 
this._backEnableProperty = instance._backEnableProperty; 
this.setSendRequestActionProperty(instance._sendRequestActionProperty);this.setSendJobActionProperty(instance._sendJobActionProperty);this.setSendResponseActionProperty(instance._sendResponseActionProperty);this.setEnrollActionProperty(instance._enrollActionProperty);this.setDoorActionProperty(instance._doorActionProperty);this.setDelegationActionProperty(instance._delegationActionProperty);this.setEditionActionProperty(instance._editionActionProperty);this.setWaitActionProperty(instance._waitActionProperty);this.setLineActionProperty(instance._lineActionProperty);}protected void merge(ActivityPlacePropertyBase child) {super.merge(child);if(_openContestProperty == null) _openContestProperty = child._openContestProperty; else if (child._openContestProperty != null) {_openContestProperty.merge(child._openContestProperty);}
if(_closeContestProperty == null) _closeContestProperty = child._closeContestProperty; else if (child._closeContestProperty != null) {_closeContestProperty.merge(child._closeContestProperty);}
}}protected LinkedHashMap<String, ActivityDefinition.ActivityPlaceProperty> _activityPlacePropertyMap = new LinkedHashMap<String, ActivityDefinition.ActivityPlaceProperty>();public void addPlace(ActivityDefinition.ActivityPlaceProperty value) {String key = value.getName() != null ? value.getName() : value.getCode();ActivityDefinition.ActivityPlaceProperty current = _activityPlacePropertyMap.get(key);if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {ActivityDefinition.ActivityPlaceProperty instance = value.getClass().newInstance();instance.copy(current);instance.setCode(value.getCode());instance.setName(value.getName());instance.merge(value);_activityPlacePropertyMap.put(key, instance);}catch (Exception exception) {}}else current.merge(value);} else {_activityPlacePropertyMap.put(key, value);} }public java.util.Map<String,ActivityDefinition.ActivityPlaceProperty> getPlaceMap() { return _activityPlacePropertyMap; }public java.util.Collection<ActivityDefinition.ActivityPlaceProperty> getPlaceList() { return _activityPlacePropertyMap.values(); }
	

	public void copy(ActivityDefinitionBase instance) {
		this._target = instance._target;
this._role = instance._role;
this._code = instance._code;
this._name = instance._name;
this._parent = instance._parent;
this._label = instance._label;
this._description = instance._description;
this._help = instance._help;

		this._contestantsProperty = instance._contestantsProperty; 
for(ActivityDefinition.ActivityPlaceProperty item : instance._activityPlacePropertyMap.values())this.addPlace(item);
for(ShortcutProperty item : instance._shortcutPropertyMap.values())this.addShortcut(item);
this._isManual = instance._isManual; 
for(ViewProperty item : instance._viewPropertyMap.values())this.addView(item);
this._isPrivate = instance._isPrivate; 
this._isAbstract = instance._isAbstract; 

		for(TaskContestProperty item : instance._taskContestPropertyMap.values())this.addTaskContestProperty(item);for(TaskProviderProperty item : instance._taskProviderPropertyMap.values())this.addTaskProviderProperty(item);
	}

	public void merge(ActivityDefinitionBase child) {
		super.merge(child);
		
		
		if(_contestantsProperty == null) _contestantsProperty = child._contestantsProperty; else if (child._contestantsProperty != null) {_contestantsProperty.merge(child._contestantsProperty);}
for(ActivityDefinition.ActivityPlaceProperty item : child._activityPlacePropertyMap.values())this.addPlace(item);

		
	}

	public Class<?> getMetamodelClass() {
		return ActivityDefinitionBase.class;
	}

}

