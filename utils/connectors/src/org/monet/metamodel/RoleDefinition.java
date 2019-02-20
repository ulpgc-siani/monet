package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
RoleDefinition
Un rol es una competencia necesaria para realizar un trabajo
Los roles se pueden asignar tanto a trabajadores como a unidades de negocio de la federaciÃ³n
*/

public  class RoleDefinition extends EntityDefinition {

	
	public static class DisableUsersProperty  {protected void copy(DisableUsersProperty instance) {}protected void merge(DisableUsersProperty child) {}}protected DisableUsersProperty _disableUsersProperty;public DisableUsersProperty getDisableUsers() { return _disableUsersProperty; }public void setDisableUsers(DisableUsersProperty value) { if(_disableUsersProperty!=null) _disableUsersProperty.merge(value); else {_disableUsersProperty = value;} }public static class EnableServicesProperty  {protected ArrayList<String> _ontology = new ArrayList<String>();public ArrayList<String> getOntology() { return _ontology; }public void setOntology(ArrayList<String> value) { _ontology = value; }protected void copy(EnableServicesProperty instance) {this._ontology.addAll(instance._ontology);
}protected void merge(EnableServicesProperty child) {if(child._ontology != null)this._ontology.addAll(child._ontology);
}}protected EnableServicesProperty _enableServicesProperty;public EnableServicesProperty getEnableServices() { return _enableServicesProperty; }public void setEnableServices(EnableServicesProperty value) { if(_enableServicesProperty!=null) _enableServicesProperty.merge(value); else {_enableServicesProperty = value;} }public static class EnableFeedersProperty  {protected ArrayList<String> _ontology = new ArrayList<String>();public ArrayList<String> getOntology() { return _ontology; }public void setOntology(ArrayList<String> value) { _ontology = value; }protected void copy(EnableFeedersProperty instance) {this._ontology.addAll(instance._ontology);
}protected void merge(EnableFeedersProperty child) {if(child._ontology != null)this._ontology.addAll(child._ontology);
}}protected EnableFeedersProperty _enableFeedersProperty;public EnableFeedersProperty getEnableFeeders() { return _enableFeedersProperty; }public void setEnableFeeders(EnableFeedersProperty value) { if(_enableFeedersProperty!=null) _enableFeedersProperty.merge(value); else {_enableFeedersProperty = value;} }
	

	public void copy(RoleDefinition instance) {
		this._code = instance._code;
this._name = instance._name;
this._parent = instance._parent;
this._label = instance._label;
this._description = instance._description;
this._help = instance._help;

		this._disableUsersProperty = instance._disableUsersProperty; 
this._enableServicesProperty = instance._enableServicesProperty; 
this._enableFeedersProperty = instance._enableFeedersProperty; 
this._isAbstract = instance._isAbstract; 

		
	}

	public void merge(RoleDefinition child) {
		super.merge(child);
		
		
		if(_disableUsersProperty == null) _disableUsersProperty = child._disableUsersProperty; else if (child._disableUsersProperty != null) {_disableUsersProperty.merge(child._disableUsersProperty);}
if(_enableServicesProperty == null) _enableServicesProperty = child._enableServicesProperty; else if (child._enableServicesProperty != null) {_enableServicesProperty.merge(child._enableServicesProperty);}
if(_enableFeedersProperty == null) _enableFeedersProperty = child._enableFeedersProperty; else if (child._enableFeedersProperty != null) {_enableFeedersProperty.merge(child._enableFeedersProperty);}

		
	}

	public Class<?> getMetamodelClass() {
		return RoleDefinition.class;
	}

}

