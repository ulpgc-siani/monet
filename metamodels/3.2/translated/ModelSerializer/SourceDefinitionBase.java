package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
SourceDefinition
Una fuente permite definir un lugar donde se encuentra información que formará parte del vocabulario de la unidad de negocio.

	  Se utilizan para clasificar la información. 
	  Toda fuente de monet está compuesta por términos. 
	  Cada término se define con un código que lo representa unívocamente y una etiqueta descriptiva del término.
	  Se permite jerarquía en la fuente, por lo que existe la posibilidad de generar árboles de términos y por tanto un término puede contener otros términos. 
	
*/

public abstract class SourceDefinitionBase extends EntityDefinition {

	protected String _ontology;public String getOntology() { return _ontology; }public void setOntology(String value) { _ontology = value; }
	
	

	public void copy(SourceDefinitionBase instance) {
		this._ontology = instance._ontology;
this._code = instance._code;
this._name = instance._name;
this._parent = instance._parent;
this._label = instance._label;
this._description = instance._description;
this._help = instance._help;

		this._isAbstract = instance._isAbstract; 

		
	}

	public void merge(SourceDefinitionBase child) {
		super.merge(child);
		
		if(child._ontology != null)this._ontology = child._ontology;

		
		
	}

	public Class<?> getMetamodelClass() {
		return SourceDefinitionBase.class;
	}

}

