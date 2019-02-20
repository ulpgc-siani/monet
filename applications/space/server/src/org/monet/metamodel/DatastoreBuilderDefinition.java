package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
DatastoreBuilderDefinition
Un transformador es un agente que permite generar hechos en varios cubos a partir del contenido de un documento

*/

public  class DatastoreBuilderDefinition extends AgentDefinition {

	protected org.monet.metamodel.internal.Ref _source;public org.monet.metamodel.internal.Ref getSource() { return _source; }public void setSource(org.monet.metamodel.internal.Ref value) { _source = value; }
	public static class ToProperty  {protected org.monet.metamodel.internal.Ref _datastore;public org.monet.metamodel.internal.Ref getDatastore() { return _datastore; }public void setDatastore(org.monet.metamodel.internal.Ref value) { _datastore = value; }protected void copy(ToProperty instance) {this._datastore = instance._datastore;
}protected void merge(ToProperty child) {if(child._datastore != null)this._datastore = child._datastore;
}}protected ArrayList<ToProperty> _toPropertyList = new ArrayList<ToProperty>();public ArrayList<ToProperty> getToList() { return _toPropertyList; }
	

	public void copy(DatastoreBuilderDefinition instance) {
		this._source = instance._source;
this._code = instance._code;
this._name = instance._name;
this._parent = instance._parent;
this._label = instance._label;
this._description = instance._description;
this._help = instance._help;

		_toPropertyList.addAll(instance._toPropertyList);
this._isAbstract = instance._isAbstract; 

		
	}

	public void merge(DatastoreBuilderDefinition child) {
		super.merge(child);
		
		if(child._source != null)this._source = child._source;

		_toPropertyList.addAll(child._toPropertyList);

		
	}

	public Class<?> getMetamodelClass() {
		return DatastoreBuilderDefinition.class;
	}

}

