package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
FactTransformerDefinition
Un transformador es un agente que permite generar hechos en varios cubos a partir del contenido de un documento

*/

public  class FactTransformerDefinition extends AgentDefinition {

	protected org.monet.metamodel.internal.Ref _source;public org.monet.metamodel.internal.Ref getSource() { return _source; }public void setSource(org.monet.metamodel.internal.Ref value) { _source = value; }
	public static class ToProperty  {protected org.monet.metamodel.internal.Ref _cube;public org.monet.metamodel.internal.Ref getCube() { return _cube; }public void setCube(org.monet.metamodel.internal.Ref value) { _cube = value; }protected void merge(ToProperty child) {if(child._cube != null)this._cube = child._cube;
}}protected ArrayList<ToProperty> _toPropertyList = new ArrayList<ToProperty>();public ArrayList<ToProperty> getToList() { return _toPropertyList; }
	

	public void merge(FactTransformerDefinition child) {
		super.merge(child);
		
		if(child._source != null)this._source = child._source;

		_toPropertyList.addAll(child._toPropertyList);

		
	}

	public Class<?> getMetamodelClass() {
		return FactTransformerDefinition.class;
	}

}

