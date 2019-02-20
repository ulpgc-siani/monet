package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
ProviderDefinition
Un proveedor es un tipo de agente que obtiene datos de otras unidades de negocio

*/

public abstract class ProviderDefinition extends Definition {

	
	
	

	public void copy(ProviderDefinition instance) {
		this._code = instance._code;
this._name = instance._name;
this._parent = instance._parent;
this._label = instance._label;
this._description = instance._description;
this._help = instance._help;

		this._isAbstract = instance._isAbstract; 

		
	}

	public void merge(ProviderDefinition child) {
		super.merge(child);
		
		
		
		
	}

	public Class<?> getMetamodelClass() {
		return ProviderDefinition.class;
	}

}

