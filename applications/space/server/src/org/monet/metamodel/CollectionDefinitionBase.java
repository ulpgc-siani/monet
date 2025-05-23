package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
CollectionDefinition
Una colección es un tipo de conjunto que permite agregar otros nodos

*/

public  class CollectionDefinitionBase extends SetDefinition {

	
	public static class IsComponent  {protected void copy(IsComponent instance) {}protected void merge(IsComponent child) {}}protected IsComponent _isComponent;public boolean isComponent() { return (_isComponent != null); }public IsComponent getIsComponent() { return _isComponent; }public void setIsComponent(boolean value) { if(value) _isComponent = new IsComponent(); else {_isComponent = null;}}public static class AddProperty  {protected ArrayList<org.monet.metamodel.internal.Ref> _node = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getNode() { return _node; }public void setNode(ArrayList<org.monet.metamodel.internal.Ref> value) { _node = value; }protected void copy(AddProperty instance) {this._node.addAll(instance._node);
}protected void merge(AddProperty child) {if(child._node != null)this._node.addAll(child._node);
}}protected AddProperty _addProperty;public AddProperty getAdd() { return _addProperty; }public void setAdd(AddProperty value) { if(_addProperty!=null) _addProperty.merge(value); else {_addProperty = value;} }public static class ToolbarProperty  {public static class AddOperationProperty  {protected ArrayList<org.monet.metamodel.internal.Ref> _enable = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getEnable() { return _enable; }public void setEnable(ArrayList<org.monet.metamodel.internal.Ref> value) { _enable = value; }protected void copy(AddOperationProperty instance) {this._enable.addAll(instance._enable);
}protected void merge(AddOperationProperty child) {if(child._enable != null)this._enable.addAll(child._enable);
}}protected AddOperationProperty _addOperationProperty;public AddOperationProperty getAddOperation() { return _addOperationProperty; }public void setAddOperation(AddOperationProperty value) { if(_addOperationProperty!=null) _addOperationProperty.merge(value); else {_addOperationProperty = value;} }protected void copy(ToolbarProperty instance) {this._addOperationProperty = instance._addOperationProperty; 
}protected void merge(ToolbarProperty child) {if(_addOperationProperty == null) _addOperationProperty = child._addOperationProperty; else if (child._addOperationProperty != null) {_addOperationProperty.merge(child._addOperationProperty);}
}}protected ToolbarProperty _toolbarProperty;public ToolbarProperty getToolbar() { return _toolbarProperty; }public void setToolbar(ToolbarProperty value) { if(_toolbarProperty!=null) _toolbarProperty.merge(value); else {_toolbarProperty = value;} }
	

	public void copy(CollectionDefinitionBase instance) {
		this._index = instance._index;
this._code = instance._code;
this._name = instance._name;
this._parent = instance._parent;
this._label = instance._label;
this._description = instance._description;
this._help = instance._help;

		this._isComponent = instance._isComponent; 
this._addProperty = instance._addProperty; 
this._toolbarProperty = instance._toolbarProperty; 
for(SetDefinition.SetViewProperty item : instance._setViewPropertyMap.values())this.addView(item);
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

	public void merge(CollectionDefinitionBase child) {
		super.merge(child);
		
		
		if(_isComponent == null) _isComponent = child._isComponent; else if (child._isComponent != null) {_isComponent.merge(child._isComponent);}
if(_addProperty == null) _addProperty = child._addProperty; else if (child._addProperty != null) {_addProperty.merge(child._addProperty);}
if(_toolbarProperty == null) _toolbarProperty = child._toolbarProperty; else if (child._toolbarProperty != null) {_toolbarProperty.merge(child._toolbarProperty);}

		
	}

	public Class<?> getMetamodelClass() {
		return CollectionDefinitionBase.class;
	}

}

