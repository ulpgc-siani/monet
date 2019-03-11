package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
ProcessDefinition
Una tarea es una trabajo colectivo o individual que se desarrolla en la unidad de negocio

*/

public abstract class ProcessDefinitionBase extends TaskDefinition {

	protected org.monet.metamodel.internal.Ref _target;public org.monet.metamodel.internal.Ref getTarget() { return _target; }public void setTarget(org.monet.metamodel.internal.Ref value) { _target = value; }
	public static class ShortcutProperty  {protected String _name;public String getName() { return _name; }public void setName(String value) { _name = value; }protected void copy(ShortcutProperty instance) {this._name = instance._name;
}protected void merge(ShortcutProperty child) {if(child._name != null)this._name = child._name;
}}protected LinkedHashMap<String, ShortcutProperty> _shortcutPropertyMap = new LinkedHashMap<String, ShortcutProperty>();public void addShortcut(ShortcutProperty value) {ShortcutProperty current = _shortcutPropertyMap.get(value.getName());if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {ShortcutProperty instance = value.getClass().newInstance();instance.copy(current);instance.setName(value.getName());instance.merge(value);_shortcutPropertyMap.put(value.getName(), instance);}catch (Exception exception) {}}else current.merge(value);} else {_shortcutPropertyMap.put(value.getName(), value);} }public java.util.Map<String,ShortcutProperty> getShortcutMap() { return _shortcutPropertyMap; }public java.util.Collection<ShortcutProperty> getShortcutList() { return _shortcutPropertyMap.values(); }public static class IsManual  {protected void copy(IsManual instance) {}protected void merge(IsManual child) {}}protected IsManual _isManual;public boolean isManual() { return (_isManual != null); }public IsManual getIsManual() { return _isManual; }public void setIsManual(boolean value) { if(value) _isManual = new IsManual(); else {_isManual = null;}}public static class ViewProperty extends org.monet.metamodel.ViewProperty {protected Object _label;public Object getLabel() { return _label; }public void setLabel(Object value) { _label = value; }public static class IsDefault  {protected void copy(IsDefault instance) {}protected void merge(IsDefault child) {}}protected IsDefault _isDefault;public boolean isDefault() { return (_isDefault != null); }public IsDefault getIsDefault() { return _isDefault; }public void setIsDefault(boolean value) { if(value) _isDefault = new IsDefault(); else {_isDefault = null;}}public static class ForProperty  {protected ArrayList<org.monet.metamodel.internal.Ref> _role = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getRole() { return _role; }public void setRole(ArrayList<org.monet.metamodel.internal.Ref> value) { _role = value; }protected void copy(ForProperty instance) {this._role.addAll(instance._role);
}protected void merge(ForProperty child) {if(child._role != null)this._role.addAll(child._role);
}}protected ForProperty _forProperty;public ForProperty getFor() { return _forProperty; }public void setFor(ForProperty value) { if(_forProperty!=null) _forProperty.merge(value); else {_forProperty = value;} }public static class ShowProperty  {protected String _shortcut;public String getShortcut() { return _shortcut; }public void setShortcut(String value) { _shortcut = value; }protected String _shortcutView;public String getShortcutView() { return _shortcutView; }public void setShortcutView(String value) { _shortcutView = value; }protected org.monet.metamodel.internal.Ref _target;public org.monet.metamodel.internal.Ref getTarget() { return _target; }public void setTarget(org.monet.metamodel.internal.Ref value) { _target = value; }public static class OrdersProperty  {protected void copy(OrdersProperty instance) {}protected void merge(OrdersProperty child) {}}protected OrdersProperty _ordersProperty;public OrdersProperty getOrders() { return _ordersProperty; }public void setOrders(OrdersProperty value) { if(_ordersProperty!=null) _ordersProperty.merge(value); else {_ordersProperty = value;} }protected void copy(ShowProperty instance) {this._shortcut = instance._shortcut;
this._shortcutView = instance._shortcutView;
this._target = instance._target;
this._ordersProperty = instance._ordersProperty; 
}protected void merge(ShowProperty child) {if(child._shortcut != null)this._shortcut = child._shortcut;
if(child._shortcutView != null)this._shortcutView = child._shortcutView;
if(child._target != null)this._target = child._target;
if(_ordersProperty == null) _ordersProperty = child._ordersProperty; else if (child._ordersProperty != null) {_ordersProperty.merge(child._ordersProperty);}
}}protected ShowProperty _showProperty;public ShowProperty getShow() { return _showProperty; }public void setShow(ShowProperty value) { if(_showProperty!=null) _showProperty.merge(value); else {_showProperty = value;} }protected void copy(ViewProperty instance) {this._label = instance._label;
this._code = instance._code;
this._name = instance._name;
this._isDefault = instance._isDefault; 
this._forProperty = instance._forProperty; 
this._showProperty = instance._showProperty; 
}protected void merge(ViewProperty child) {super.merge(child);if(child._label != null)this._label = child._label;
if(_isDefault == null) _isDefault = child._isDefault; else if (child._isDefault != null) {_isDefault.merge(child._isDefault);}
if(_forProperty == null) _forProperty = child._forProperty; else if (child._forProperty != null) {_forProperty.merge(child._forProperty);}
if(_showProperty == null) _showProperty = child._showProperty; else if (child._showProperty != null) {_showProperty.merge(child._showProperty);}
}}protected LinkedHashMap<String, ViewProperty> _viewPropertyMap = new LinkedHashMap<String, ViewProperty>();public void addView(ViewProperty value) {String key = value.getName() != null ? value.getName() : value.getCode();ViewProperty current = _viewPropertyMap.get(key);if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {ViewProperty instance = value.getClass().newInstance();instance.copy(current);instance.setCode(value.getCode());instance.setName(value.getName());instance.merge(value);_viewPropertyMap.put(key, instance);}catch (Exception exception) {}}else current.merge(value);} else {_viewPropertyMap.put(key, value);} }public java.util.Map<String,ViewProperty> getViewMap() { return _viewPropertyMap; }public java.util.Collection<ViewProperty> getViewList() { return _viewPropertyMap.values(); }
	protected LinkedHashMap<String, TaskContestProperty> _taskContestPropertyMap = new LinkedHashMap<String, TaskContestProperty>();public void addTaskContestProperty(TaskContestProperty value) {String key = value.getName() != null ? value.getName() : value.getCode();TaskContestProperty current = _taskContestPropertyMap.get(key);if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {TaskContestProperty instance = value.getClass().newInstance();instance.copy(current);instance.setCode(value.getCode());instance.setName(value.getName());instance.merge(value);_taskContestPropertyMap.put(key, instance);}catch (Exception exception) {}}else current.merge(value);} else {_taskContestPropertyMap.put(key, value);} }public java.util.Map<String,TaskContestProperty> getTaskContestPropertyMap() { return _taskContestPropertyMap; }public java.util.Collection<TaskContestProperty> getTaskContestPropertyList() { return _taskContestPropertyMap.values(); }protected LinkedHashMap<String, TaskProviderProperty> _taskProviderPropertyMap = new LinkedHashMap<String, TaskProviderProperty>();public void addTaskProviderProperty(TaskProviderProperty value) {String key = value.getName() != null ? value.getName() : value.getCode();TaskProviderProperty current = _taskProviderPropertyMap.get(key);if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {TaskProviderProperty instance = value.getClass().newInstance();instance.copy(current);instance.setCode(value.getCode());instance.setName(value.getName());instance.merge(value);_taskProviderPropertyMap.put(key, instance);}catch (Exception exception) {}}else current.merge(value);} else {_taskProviderPropertyMap.put(key, value);} }public java.util.Map<String,TaskProviderProperty> getTaskProviderPropertyMap() { return _taskProviderPropertyMap; }public java.util.Collection<TaskProviderProperty> getTaskProviderPropertyList() { return _taskProviderPropertyMap.values(); }

	public void copy(ProcessDefinitionBase instance) {
		this._target = instance._target;
this._role = instance._role;
this._code = instance._code;
this._name = instance._name;
this._parent = instance._parent;
this._label = instance._label;
this._description = instance._description;
this._help = instance._help;

		for(ShortcutProperty item : instance._shortcutPropertyMap.values())this.addShortcut(item);
this._isManual = instance._isManual; 
for(ViewProperty item : instance._viewPropertyMap.values())this.addView(item);
this._isPrivate = instance._isPrivate; 
this._isBackground = instance._isBackground; 
this._isAbstract = instance._isAbstract; 

		for(TaskContestProperty item : instance._taskContestPropertyMap.values())this.addTaskContestProperty(item);for(TaskProviderProperty item : instance._taskProviderPropertyMap.values())this.addTaskProviderProperty(item);
	}

	public void merge(ProcessDefinitionBase child) {
		super.merge(child);
		
		if(child._target != null)this._target = child._target;

		for(ShortcutProperty item : child._shortcutPropertyMap.values())this.addShortcut(item);
if(_isManual == null) _isManual = child._isManual; else if (child._isManual != null) {_isManual.merge(child._isManual);}
for(ViewProperty item : child._viewPropertyMap.values())this.addView(item);

		for(TaskContestProperty item : child._taskContestPropertyMap.values())this.addTaskContestProperty(item);for(TaskProviderProperty item : child._taskProviderPropertyMap.values())this.addTaskProviderProperty(item);
	}

	public Class<?> getMetamodelClass() {
		return ProcessDefinitionBase.class;
	}

}

