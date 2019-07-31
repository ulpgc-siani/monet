package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
ContainerDefinition
Un contenedor es un tipo de nodo que contiene otros nodos como componentes

*/

public  class ContainerDefinitionBase extends NodeDefinition {

	
	public static class ContainProperty  {protected ArrayList<org.monet.metamodel.internal.Ref> _node = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getNode() { return _node; }public void setNode(ArrayList<org.monet.metamodel.internal.Ref> value) { _node = value; }protected void copy(ContainProperty instance) {this._node.addAll(instance._node);
}protected void merge(ContainProperty child) {if(child._node != null)this._node.addAll(child._node);
}}protected ContainProperty _containProperty;public ContainProperty getContain() { return _containProperty; }public void setContain(ContainProperty value) { if(_containProperty!=null) _containProperty.merge(value); else {_containProperty = value;} }public static class IsPrototypable  {protected void copy(IsPrototypable instance) {}protected void merge(IsPrototypable child) {}}protected IsPrototypable _isPrototypable;public boolean isPrototypable() { return (_isPrototypable != null); }public IsPrototypable getIsPrototypable() { return _isPrototypable; }public void setIsPrototypable(boolean value) { if(value) _isPrototypable = new IsPrototypable(); else {_isPrototypable = null;}}public static class IsGeoreferenced  {protected void copy(IsGeoreferenced instance) {}protected void merge(IsGeoreferenced child) {}}protected IsGeoreferenced _isGeoreferenced;public boolean isGeoreferenced() { return (_isGeoreferenced != null); }public IsGeoreferenced getIsGeoreferenced() { return _isGeoreferenced; }public void setIsGeoreferenced(boolean value) { if(value) _isGeoreferenced = new IsGeoreferenced(); else {_isGeoreferenced = null;}}public static class IsEnvironment  {protected void copy(IsEnvironment instance) {}protected void merge(IsEnvironment child) {}}protected IsEnvironment _isEnvironment;public boolean isEnvironment() { return (_isEnvironment != null); }public IsEnvironment getIsEnvironment() { return _isEnvironment; }public void setIsEnvironment(boolean value) { if(value) _isEnvironment = new IsEnvironment(); else {_isEnvironment = null;}}public static class ForProperty  {protected ArrayList<org.monet.metamodel.internal.Ref> _role = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getRole() { return _role; }public void setRole(ArrayList<org.monet.metamodel.internal.Ref> value) { _role = value; }protected void copy(ForProperty instance) {this._role.addAll(instance._role);
}protected void merge(ForProperty child) {if(child._role != null)this._role.addAll(child._role);
}}protected ForProperty _forProperty;public ForProperty getFor() { return _forProperty; }public void setFor(ForProperty value) { if(_forProperty!=null) _forProperty.merge(value); else {_forProperty = value;} }public static class ViewProperty extends org.monet.metamodel.NodeViewProperty {public static class ShowProperty  {protected ArrayList<org.monet.metamodel.internal.Ref> _component = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getComponent() { return _component; }public void setComponent(ArrayList<org.monet.metamodel.internal.Ref> value) { _component = value; }public static class LinksInProperty  {protected ArrayList<org.monet.metamodel.internal.Ref> _node = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getNode() { return _node; }public void setNode(ArrayList<org.monet.metamodel.internal.Ref> value) { _node = value; }protected void copy(LinksInProperty instance) {this._node.addAll(instance._node);
}protected void merge(LinksInProperty child) {if(child._node != null)this._node.addAll(child._node);
}}protected LinksInProperty _linksInProperty;public LinksInProperty getLinksIn() { return _linksInProperty; }public void setLinksIn(LinksInProperty value) { if(_linksInProperty!=null) _linksInProperty.merge(value); else {_linksInProperty = value;} }public static class LinksOutProperty  {protected ArrayList<org.monet.metamodel.internal.Ref> _node = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getNode() { return _node; }public void setNode(ArrayList<org.monet.metamodel.internal.Ref> value) { _node = value; }protected void copy(LinksOutProperty instance) {this._node.addAll(instance._node);
}protected void merge(LinksOutProperty child) {if(child._node != null)this._node.addAll(child._node);
}}protected LinksOutProperty _linksOutProperty;public LinksOutProperty getLinksOut() { return _linksOutProperty; }public void setLinksOut(LinksOutProperty value) { if(_linksOutProperty!=null) _linksOutProperty.merge(value); else {_linksOutProperty = value;} }public static class TasksProperty  {protected ArrayList<org.monet.metamodel.internal.Ref> _task = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getTask() { return _task; }public void setTask(ArrayList<org.monet.metamodel.internal.Ref> value) { _task = value; }protected void copy(TasksProperty instance) {this._task.addAll(instance._task);
}protected void merge(TasksProperty child) {if(child._task != null)this._task.addAll(child._task);
}}protected TasksProperty _tasksProperty;public TasksProperty getTasks() { return _tasksProperty; }public void setTasks(TasksProperty value) { if(_tasksProperty!=null) _tasksProperty.merge(value); else {_tasksProperty = value;} }public static class RecentTaskProperty  {protected void copy(RecentTaskProperty instance) {}protected void merge(RecentTaskProperty child) {}}protected RecentTaskProperty _recentTaskProperty;public RecentTaskProperty getRecentTask() { return _recentTaskProperty; }public void setRecentTask(RecentTaskProperty value) { if(_recentTaskProperty!=null) _recentTaskProperty.merge(value); else {_recentTaskProperty = value;} }public static class RevisionsProperty  {protected void copy(RevisionsProperty instance) {}protected void merge(RevisionsProperty child) {}}protected RevisionsProperty _revisionsProperty;public RevisionsProperty getRevisions() { return _revisionsProperty; }public void setRevisions(RevisionsProperty value) { if(_revisionsProperty!=null) _revisionsProperty.merge(value); else {_revisionsProperty = value;} }public static class NotesProperty  {protected void copy(NotesProperty instance) {}protected void merge(NotesProperty child) {}}protected NotesProperty _notesProperty;public NotesProperty getNotes() { return _notesProperty; }public void setNotes(NotesProperty value) { if(_notesProperty!=null) _notesProperty.merge(value); else {_notesProperty = value;} }public static class LocationProperty  {protected void copy(LocationProperty instance) {}protected void merge(LocationProperty child) {}}protected LocationProperty _locationProperty;public LocationProperty getLocation() { return _locationProperty; }public void setLocation(LocationProperty value) { if(_locationProperty!=null) _locationProperty.merge(value); else {_locationProperty = value;} }protected void copy(ShowProperty instance) {this._component.addAll(instance._component);
this._linksInProperty = instance._linksInProperty; 
this._linksOutProperty = instance._linksOutProperty; 
this._tasksProperty = instance._tasksProperty; 
this._recentTaskProperty = instance._recentTaskProperty; 
this._revisionsProperty = instance._revisionsProperty; 
this._notesProperty = instance._notesProperty; 
this._locationProperty = instance._locationProperty; 
}protected void merge(ShowProperty child) {if(child._component != null)this._component.addAll(child._component);
if(_linksInProperty == null) _linksInProperty = child._linksInProperty; else if (child._linksInProperty != null) {_linksInProperty.merge(child._linksInProperty);}
if(_linksOutProperty == null) _linksOutProperty = child._linksOutProperty; else if (child._linksOutProperty != null) {_linksOutProperty.merge(child._linksOutProperty);}
if(_tasksProperty == null) _tasksProperty = child._tasksProperty; else if (child._tasksProperty != null) {_tasksProperty.merge(child._tasksProperty);}
if(_recentTaskProperty == null) _recentTaskProperty = child._recentTaskProperty; else if (child._recentTaskProperty != null) {_recentTaskProperty.merge(child._recentTaskProperty);}
if(_revisionsProperty == null) _revisionsProperty = child._revisionsProperty; else if (child._revisionsProperty != null) {_revisionsProperty.merge(child._revisionsProperty);}
if(_notesProperty == null) _notesProperty = child._notesProperty; else if (child._notesProperty != null) {_notesProperty.merge(child._notesProperty);}
if(_locationProperty == null) _locationProperty = child._locationProperty; else if (child._locationProperty != null) {_locationProperty.merge(child._locationProperty);}
}}protected ShowProperty _showProperty;public ShowProperty getShow() { return _showProperty; }public void setShow(ShowProperty value) { if(_showProperty!=null) _showProperty.merge(value); else {_showProperty = value;} }protected void copy(ViewProperty instance) {this._label = instance._label;
this._code = instance._code;
this._name = instance._name;
this._showProperty = instance._showProperty; 
this._isDefault = instance._isDefault; 
this._isVisibleWhenEmbedded = instance._isVisibleWhenEmbedded; 
this._forProperty = instance._forProperty; 
}protected void merge(ViewProperty child) {super.merge(child);if(_showProperty == null) _showProperty = child._showProperty; else if (child._showProperty != null) {_showProperty.merge(child._showProperty);}
}}protected LinkedHashMap<String, ViewProperty> _viewPropertyMap = new LinkedHashMap<String, ViewProperty>();public void addView(ViewProperty value) {String key = value.getName() != null ? value.getName() : value.getCode();ViewProperty current = _viewPropertyMap.get(key);if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {ViewProperty instance = value.getClass().newInstance();instance.copy(current);instance.setCode(value.getCode());instance.setName(value.getName());instance.merge(value);_viewPropertyMap.put(key, instance);}catch (Exception exception) {}}else current.merge(value);} else {_viewPropertyMap.put(key, value);} }public java.util.Map<String,ViewProperty> getViewMap() { return _viewPropertyMap; }public java.util.Collection<ViewProperty> getViewList() { return _viewPropertyMap.values(); }
	

	public void copy(ContainerDefinitionBase instance) {
		this._code = instance._code;
this._name = instance._name;
this._parent = instance._parent;
this._label = instance._label;
this._description = instance._description;
this._help = instance._help;

		this._containProperty = instance._containProperty; 
this._isPrototypable = instance._isPrototypable; 
this._isGeoreferenced = instance._isGeoreferenced; 
this._isEnvironment = instance._isEnvironment; 
this._forProperty = instance._forProperty; 
for(ViewProperty item : instance._viewPropertyMap.values())this.addView(item);
this._isSingleton = instance._isSingleton; 
this._isReadonly = instance._isReadonly; 
this._isPrivate = instance._isPrivate; 
this._requirePartnerContextProperty = instance._requirePartnerContextProperty; 
this._isBreadcrumbsDisabled = instance._isBreadcrumbsDisabled; 
for(OperationProperty item : instance._operationPropertyMap.values())this.addOperation(item);
_ruleNodePropertyList.addAll(instance._ruleNodePropertyList);
_ruleViewPropertyList.addAll(instance._ruleViewPropertyList);
_ruleOperationPropertyList.addAll(instance._ruleOperationPropertyList);
_displayPropertyList.addAll(instance._displayPropertyList);
this._isAbstract = instance._isAbstract; 

		
	}

	public void merge(ContainerDefinitionBase child) {
		super.merge(child);
		
		
		if(_containProperty == null) _containProperty = child._containProperty; else if (child._containProperty != null) {_containProperty.merge(child._containProperty);}
if(_isPrototypable == null) _isPrototypable = child._isPrototypable; else if (child._isPrototypable != null) {_isPrototypable.merge(child._isPrototypable);}
if(_isGeoreferenced == null) _isGeoreferenced = child._isGeoreferenced; else if (child._isGeoreferenced != null) {_isGeoreferenced.merge(child._isGeoreferenced);}
if(_isEnvironment == null) _isEnvironment = child._isEnvironment; else if (child._isEnvironment != null) {_isEnvironment.merge(child._isEnvironment);}
if(_forProperty == null) _forProperty = child._forProperty; else if (child._forProperty != null) {_forProperty.merge(child._forProperty);}
for(ViewProperty item : child._viewPropertyMap.values())this.addView(item);

		
	}

	public Class<?> getMetamodelClass() {
		return ContainerDefinitionBase.class;
	}

}

