package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
ImporterDefinition
Un importador es un agente que permite incorporar datos externos en el sistema de información a partir de un documento

*/

public  class ImporterDefinition extends AgentDefinition {

	protected org.monet.metamodel.internal.Ref _target;public org.monet.metamodel.internal.Ref getTarget() { return _target; }public void setTarget(org.monet.metamodel.internal.Ref value) { _target = value; }
	
	

	public void copy(ImporterDefinition instance) {
		this._target = instance._target;
this._code = instance._code;
this._name = instance._name;
this._parent = instance._parent;
this._label = instance._label;
this._description = instance._description;
this._help = instance._help;

		this._isAbstract = instance._isAbstract; 

		
	}

	public void merge(ImporterDefinition child) {
		super.merge(child);
		
		if(child._target != null)this._target = child._target;

		
		
	}

	public Class<?> getMetamodelClass() {
		return ImporterDefinition.class;
	}

}

