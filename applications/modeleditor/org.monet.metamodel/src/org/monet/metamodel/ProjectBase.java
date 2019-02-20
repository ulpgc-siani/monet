package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
Project
Define las características del modelo de negocio

*/

public  class ProjectBase extends AbstractManifest {

	protected String _version;public String getVersion() { return _version; }public void setVersion(String value) { _version = value; }public enum TypeEnumeration { BACK,FRONT }protected TypeEnumeration _type;public TypeEnumeration getType() { return _type; }public void setType(TypeEnumeration value) { _type = value; }protected String _author;public String getAuthor() { return _author; }public void setAuthor(String value) { _author = value; }
	
	

	public void copy(ProjectBase instance) {
		this._version = instance._version;
this._type = instance._type;
this._author = instance._author;
this._name = instance._name;
this._title = instance._title;
this._subtitle = instance._subtitle;
this._script.addAll(instance._script);

		this._spaceProperty = instance._spaceProperty; 
this._defaultLocationProperty = instance._defaultLocationProperty; 
this._federationProperty = instance._federationProperty; 
_publishPropertyList.addAll(instance._publishPropertyList);
_unpublishPropertyList.addAll(instance._unpublishPropertyList);
this._disableProperty = instance._disableProperty; 
_assignRolePropertyList.addAll(instance._assignRolePropertyList);

		
	}

	public void merge(ProjectBase child) {
		super.merge(child);
		
		if(child._version != null)this._version = child._version;
if(child._type != null)this._type = child._type;
if(child._author != null)this._author = child._author;

		
		
	}

	public Class<?> getMetamodelClass() {
		return ProjectBase.class;
	}

}

