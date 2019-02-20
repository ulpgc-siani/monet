package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
Setup
Una definición describe una entidad en el modelo de negocio

*/

public  class Setup extends AbstractManifest {

	
	
	

	public void copy(Setup instance) {
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

	public void merge(Setup child) {
		super.merge(child);
		
		
		
		
	}

	public Class<?> getMetamodelClass() {
		return Setup.class;
	}

}

