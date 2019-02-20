package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
SensorDefinition
Un sensor es una pequeña tarea o actividad que se realiza en un entorno de mobilidad

*/

public  class SensorDefinitionBase extends JobDefinition {

	
	
	

	public void copy(SensorDefinitionBase instance) {
		this._role = instance._role;
this._code = instance._code;
this._name = instance._name;
this._parent = instance._parent;
this._label = instance._label;
this._description = instance._description;
this._help = instance._help;

		this._checkPositionProperty = instance._checkPositionProperty; 
for(StepProperty item : instance._stepPropertyMap.values())this.addStep(item);
this._isPrivate = instance._isPrivate; 
this._isBackground = instance._isBackground; 
this._isAbstract = instance._isAbstract; 

		
	}

	public void merge(SensorDefinitionBase child) {
		super.merge(child);
		
		
		
		
	}

	public Class<?> getMetamodelClass() {
		return SensorDefinitionBase.class;
	}

}

