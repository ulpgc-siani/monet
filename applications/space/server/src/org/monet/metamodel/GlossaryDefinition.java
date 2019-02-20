package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
GlossaryDefinition
Un glosario es el mecanismo de Monet que permite obtener información de vocabulario de otra unidad de negocio
Se utilizan para clasificar la información
*/

public  class GlossaryDefinition extends SourceDefinition {

	
	
	

	public void copy(GlossaryDefinition instance) {
		this._ontology = instance._ontology;
this._code = instance._code;
this._name = instance._name;
this._parent = instance._parent;
this._label = instance._label;
this._description = instance._description;
this._help = instance._help;

		this._isAbstract = instance._isAbstract; 

		
	}

	public void merge(GlossaryDefinition child) {
		super.merge(child);
		
		
		
		
	}

	public Class<?> getMetamodelClass() {
		return GlossaryDefinition.class;
	}

}

