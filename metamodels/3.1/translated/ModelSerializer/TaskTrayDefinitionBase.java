package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
TaskTrayDefinition
Permite definir cómo se verán las tareas en el panel de tareas
Se utiliza para definir la vista de las tareas
*/

public  class TaskTrayDefinitionBase extends SetDefinition {

	
	
	

	public void copy(TaskTrayDefinitionBase instance) {
		this._index = instance._index;
this._code = instance._code;
this._name = instance._name;
this._parent = instance._parent;
this._label = instance._label;
this._description = instance._description;
this._help = instance._help;

		for(SetDefinition.SetViewProperty item : instance._setViewPropertyMap.values())this.addView(item);
this._isSingleton = instance._isSingleton; 
this._isReadonly = instance._isReadonly; 
this._isPrivate = instance._isPrivate; 
this._requirePartnerContextProperty = instance._requirePartnerContextProperty; 
for(OperationProperty item : instance._operationPropertyMap.values())this.addOperation(item);
_ruleNodePropertyList.addAll(instance._ruleNodePropertyList);
_ruleViewPropertyList.addAll(instance._ruleViewPropertyList);
_ruleOperationPropertyList.addAll(instance._ruleOperationPropertyList);
_displayPropertyList.addAll(instance._displayPropertyList);
this._isAbstract = instance._isAbstract; 

		
	}

	public void merge(TaskTrayDefinitionBase child) {
		super.merge(child);
		
		
		
		
	}

	public Class<?> getMetamodelClass() {
		return TaskTrayDefinitionBase.class;
	}

}

