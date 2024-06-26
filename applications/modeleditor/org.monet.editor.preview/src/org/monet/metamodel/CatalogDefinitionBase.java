package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
CatalogDefinition
Un catálogo es un tipo de conjunto que se utiliza para permitir hacer búsquedas de nodos en el espacio de negocio

*/

public  class CatalogDefinitionBase extends SetDefinition {

	
	public static class IsComponent  {protected void copy(IsComponent instance) {}protected void merge(IsComponent child) {}}protected IsComponent _isComponent;public boolean isComponent() { return (_isComponent != null); }public IsComponent getIsComponent() { return _isComponent; }public void setIsComponent(boolean value) { if(value) _isComponent = new IsComponent(); else {_isComponent = null;}}
	

	public void copy(CatalogDefinitionBase instance) {
		this._index = instance._index;
this._code = instance._code;
this._name = instance._name;
this._parent = instance._parent;
this._label = instance._label;
this._description = instance._description;
this._help = instance._help;

		this._isComponent = instance._isComponent; 
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

	public void merge(CatalogDefinitionBase child) {
		super.merge(child);
		
		
		if(_isComponent == null) _isComponent = child._isComponent; else if (child._isComponent != null) {_isComponent.merge(child._isComponent);}

		
	}

	public Class<?> getMetamodelClass() {
		return CatalogDefinitionBase.class;
	}

}

