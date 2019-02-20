package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
TaskListDefinition
Permite definir cómo se verán las tareas en el panel de tareas
Se utiliza para definir la vista de las tareas
*/

public  class TaskListDefinitionBase extends Definition {

	
	public static class ReferenceProperty  {protected LinkedHashMap<String, AttributeProperty> _attributePropertyMap = new LinkedHashMap<String, AttributeProperty>();public void addAttributeProperty(AttributeProperty value) {String key = value.getName() != null ? value.getName() : value.getCode();AttributeProperty current = _attributePropertyMap.get(key);if(current != null) {current.merge(value);} else {_attributePropertyMap.put(key, value);} }public java.util.Map<String,AttributeProperty> getAttributePropertyMap() { return _attributePropertyMap; }public java.util.Collection<AttributeProperty> getAttributePropertyList() { return _attributePropertyMap.values(); }protected void merge(ReferenceProperty child) {for(AttributeProperty item : child._attributePropertyMap.values())this.addAttributeProperty(item);}}protected ReferenceProperty _referenceProperty;public ReferenceProperty getReference() { return _referenceProperty; }public void setReference(ReferenceProperty value) { if(_referenceProperty!=null) _referenceProperty.merge(value); else {_referenceProperty = value;} }public static class TaskListViewProperty extends org.monet.metamodel.ViewProperty {public static class IsDefault  {protected void merge(IsDefault child) {}}protected IsDefault _isDefault;public boolean isDefault() { return (_isDefault != null); }public IsDefault getIsDefault() { return _isDefault; }public void setIsDefault(boolean value) { if(value) _isDefault = new IsDefault(); else {_isDefault = null;}}public static class ShowProperty  {protected org.monet.metamodel.internal.Ref _title;public org.monet.metamodel.internal.Ref getTitle() { return _title; }public void setTitle(org.monet.metamodel.internal.Ref value) { _title = value; }protected ArrayList<org.monet.metamodel.internal.Ref> _line = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getLine() { return _line; }public void setLine(ArrayList<org.monet.metamodel.internal.Ref> value) { _line = value; }protected ArrayList<org.monet.metamodel.internal.Ref> _lineBelow = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getLineBelow() { return _lineBelow; }public void setLineBelow(ArrayList<org.monet.metamodel.internal.Ref> value) { _lineBelow = value; }protected ArrayList<org.monet.metamodel.internal.Ref> _highlight = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getHighlight() { return _highlight; }public void setHighlight(ArrayList<org.monet.metamodel.internal.Ref> value) { _highlight = value; }protected ArrayList<org.monet.metamodel.internal.Ref> _footer = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getFooter() { return _footer; }public void setFooter(ArrayList<org.monet.metamodel.internal.Ref> value) { _footer = value; }protected org.monet.metamodel.internal.Ref _icon;public org.monet.metamodel.internal.Ref getIcon() { return _icon; }public void setIcon(org.monet.metamodel.internal.Ref value) { _icon = value; }protected org.monet.metamodel.internal.Ref _picture;public org.monet.metamodel.internal.Ref getPicture() { return _picture; }public void setPicture(org.monet.metamodel.internal.Ref value) { _picture = value; }protected void merge(ShowProperty child) {if(child._title != null)this._title = child._title;
if(child._line != null)this._line.addAll(child._line);
if(child._lineBelow != null)this._lineBelow.addAll(child._lineBelow);
if(child._highlight != null)this._highlight.addAll(child._highlight);
if(child._footer != null)this._footer.addAll(child._footer);
if(child._icon != null)this._icon = child._icon;
if(child._picture != null)this._picture = child._picture;
}}protected ShowProperty _showProperty;public ShowProperty getShow() { return _showProperty; }public void setShow(ShowProperty value) { if(_showProperty!=null) _showProperty.merge(value); else {_showProperty = value;} }protected void merge(TaskListViewProperty child) {super.merge(child);if(_isDefault == null) _isDefault = child._isDefault; else {_isDefault.merge(child._isDefault);}
if(_showProperty == null) _showProperty = child._showProperty; else {_showProperty.merge(child._showProperty);}
}}protected LinkedHashMap<String, TaskListViewProperty> _taskListViewPropertyMap = new LinkedHashMap<String, TaskListViewProperty>();public void addView(TaskListViewProperty value) {String key = value.getName() != null ? value.getName() : value.getCode();TaskListViewProperty current = _taskListViewPropertyMap.get(key);if(current != null) {current.merge(value);} else {_taskListViewPropertyMap.put(key, value);} }public java.util.Map<String,TaskListViewProperty> getViewMap() { return _taskListViewPropertyMap; }public java.util.Collection<TaskListViewProperty> getViewList() { return _taskListViewPropertyMap.values(); }
	

	public void merge(TaskListDefinitionBase child) {
		super.merge(child);
		
		
		if(_referenceProperty == null) _referenceProperty = child._referenceProperty; else {_referenceProperty.merge(child._referenceProperty);}
for(TaskListViewProperty item : child._taskListViewPropertyMap.values())this.addView(item);

		
	}

	public Class<?> getMetamodelClass() {
		return TaskListDefinitionBase.class;
	}

}

