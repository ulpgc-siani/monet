package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
AgentDefinition
Un agente es un objeto de la unidad de negocio capaz de realizar algún tipo de proceso

*/

public abstract class AgentDefinition extends Definition {

	
	
	

	public void copy(AgentDefinition instance) {
		this._code = instance._code;
this._name = instance._name;
this._parent = instance._parent;
this._label = instance._label;
this._description = instance._description;
this._help = instance._help;

		this._isAbstract = instance._isAbstract; 

		
	}

	public void merge(AgentDefinition child) {
		super.merge(child);
		
		
		
		
	}

	public Class<?> getMetamodelClass() {
		return AgentDefinition.class;
	}

}

