package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
ComponentTransformerDefinition
Un transformador es un agente que permite generar componentes para una dimensión a partir del contenido de un nodo

*/

public  class ComponentTransformerDefinition extends AgentDefinition {

	protected org.monet.metamodel.internal.Ref _source;public org.monet.metamodel.internal.Ref getSource() { return _source; }public void setSource(org.monet.metamodel.internal.Ref value) { _source = value; }
	public static class ToProperty  {protected org.monet.metamodel.internal.Ref _dimension;public org.monet.metamodel.internal.Ref getDimension() { return _dimension; }public void setDimension(org.monet.metamodel.internal.Ref value) { _dimension = value; }protected void merge(ToProperty child) {if(child._dimension != null)this._dimension = child._dimension;
}}protected ToProperty _toProperty;public ToProperty getTo() { return _toProperty; }public void setTo(ToProperty value) { if(_toProperty!=null) _toProperty.merge(value); else {_toProperty = value;} }
	

	public void merge(ComponentTransformerDefinition child) {
		super.merge(child);
		
		if(child._source != null)this._source = child._source;

		if(_toProperty == null) _toProperty = child._toProperty; else {_toProperty.merge(child._toProperty);}

		
	}

	public Class<?> getMetamodelClass() {
		return ComponentTransformerDefinition.class;
	}

}

