package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
AbstractManifest
Una definición describe una entidad en el modelo de negocio

*/

public abstract class AbstractManifestBase  {

	protected String _name;public String getName() { return _name; }public void setName(String value) { _name = value; }protected Object _title;public Object getTitle() { return _title; }public void setTitle(Object value) { _title = value; }protected Object _subtitle;public Object getSubtitle() { return _subtitle; }public void setSubtitle(Object value) { _subtitle = value; }protected ArrayList<org.monet.metamodel.internal.Ref> _script = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getScript() { return _script; }public void setScript(ArrayList<org.monet.metamodel.internal.Ref> value) { _script = value; }
	public static class SpaceProperty  {protected String _name;public String getName() { return _name; }public void setName(String value) { _name = value; }protected java.net.URI _deployUri;public java.net.URI getDeployUri() { return _deployUri; }public void setDeployUri(java.net.URI value) { _deployUri = value; }protected String _deployPath;public String getDeployPath() { return _deployPath; }public void setDeployPath(String value) { _deployPath = value; }protected void copy(SpaceProperty instance) {this._name = instance._name;
this._deployUri = instance._deployUri;
this._deployPath = instance._deployPath;
}protected void merge(SpaceProperty child) {if(child._name != null)this._name = child._name;
if(child._deployUri != null)this._deployUri = child._deployUri;
if(child._deployPath != null)this._deployPath = child._deployPath;
}}protected SpaceProperty _spaceProperty;public SpaceProperty getSpace() { return _spaceProperty; }public void setSpace(SpaceProperty value) { if(_spaceProperty!=null) _spaceProperty.merge(value); else {_spaceProperty = value;} }public static class DefaultLocationProperty  {protected Double _latitude;public Double getLatitude() { return _latitude; }public void setLatitude(Double value) { _latitude = value; }protected Double _longitude;public Double getLongitude() { return _longitude; }public void setLongitude(Double value) { _longitude = value; }protected void copy(DefaultLocationProperty instance) {this._latitude = instance._latitude;
this._longitude = instance._longitude;
}protected void merge(DefaultLocationProperty child) {if(child._latitude != null)this._latitude = child._latitude;
if(child._longitude != null)this._longitude = child._longitude;
}}protected DefaultLocationProperty _defaultLocationProperty;public DefaultLocationProperty getDefaultLocation() { return _defaultLocationProperty; }public void setDefaultLocation(DefaultLocationProperty value) { if(_defaultLocationProperty!=null) _defaultLocationProperty.merge(value); else {_defaultLocationProperty = value;} }public static class FederationProperty  {protected Object _title;public Object getTitle() { return _title; }public void setTitle(Object value) { _title = value; }protected java.net.URI _uri;public java.net.URI getUri() { return _uri; }public void setUri(java.net.URI value) { _uri = value; }protected String _secret;public String getSecret() { return _secret; }public void setSecret(String value) { _secret = value; }protected void copy(FederationProperty instance) {this._title = instance._title;
this._uri = instance._uri;
this._secret = instance._secret;
}protected void merge(FederationProperty child) {if(child._title != null)this._title = child._title;
if(child._uri != null)this._uri = child._uri;
if(child._secret != null)this._secret = child._secret;
}}protected FederationProperty _federationProperty;public FederationProperty getFederation() { return _federationProperty; }public void setFederation(FederationProperty value) { if(_federationProperty!=null) _federationProperty.merge(value); else {_federationProperty = value;} }public static class PublishProperty  {protected ArrayList<org.monet.metamodel.internal.Ref> _service = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getService() { return _service; }public void setService(ArrayList<org.monet.metamodel.internal.Ref> value) { _service = value; }protected ArrayList<org.monet.metamodel.internal.Ref> _source = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getSource() { return _source; }public void setSource(ArrayList<org.monet.metamodel.internal.Ref> value) { _source = value; }public static class FederationProperty  {protected java.net.URI _setupUri;public java.net.URI getSetupUri() { return _setupUri; }public void setSetupUri(java.net.URI value) { _setupUri = value; }protected void copy(FederationProperty instance) {this._setupUri = instance._setupUri;
}protected void merge(FederationProperty child) {if(child._setupUri != null)this._setupUri = child._setupUri;
}}protected FederationProperty _federationProperty;public FederationProperty getFederation() { return _federationProperty; }public void setFederation(FederationProperty value) { if(_federationProperty!=null) _federationProperty.merge(value); else {_federationProperty = value;} }protected void copy(PublishProperty instance) {this._service.addAll(instance._service);
this._source.addAll(instance._source);
this._federationProperty = instance._federationProperty; 
}protected void merge(PublishProperty child) {if(child._service != null)this._service.addAll(child._service);
if(child._source != null)this._source.addAll(child._source);
if(_federationProperty == null) _federationProperty = child._federationProperty; else if (child._federationProperty != null) {_federationProperty.merge(child._federationProperty);}
}}protected ArrayList<PublishProperty> _publishPropertyList = new ArrayList<PublishProperty>();public ArrayList<PublishProperty> getPublishList() { return _publishPropertyList; }public static class UnpublishProperty  {public static class FederationProperty  {protected String _name;public String getName() { return _name; }public void setName(String value) { _name = value; }protected java.net.URI _setupUri;public java.net.URI getSetupUri() { return _setupUri; }public void setSetupUri(java.net.URI value) { _setupUri = value; }protected void copy(FederationProperty instance) {this._name = instance._name;
this._setupUri = instance._setupUri;
}protected void merge(FederationProperty child) {if(child._name != null)this._name = child._name;
if(child._setupUri != null)this._setupUri = child._setupUri;
}}protected FederationProperty _federationProperty;public FederationProperty getFederation() { return _federationProperty; }public void setFederation(FederationProperty value) { if(_federationProperty!=null) _federationProperty.merge(value); else {_federationProperty = value;} }protected void copy(UnpublishProperty instance) {this._federationProperty = instance._federationProperty; 
}protected void merge(UnpublishProperty child) {if(_federationProperty == null) _federationProperty = child._federationProperty; else if (child._federationProperty != null) {_federationProperty.merge(child._federationProperty);}
}}protected ArrayList<UnpublishProperty> _unpublishPropertyList = new ArrayList<UnpublishProperty>();public ArrayList<UnpublishProperty> getUnpublishList() { return _unpublishPropertyList; }public static class DisableProperty  {protected ArrayList<org.monet.metamodel.internal.Ref> _definition = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getDefinition() { return _definition; }public void setDefinition(ArrayList<org.monet.metamodel.internal.Ref> value) { _definition = value; }protected void copy(DisableProperty instance) {this._definition.addAll(instance._definition);
}protected void merge(DisableProperty child) {if(child._definition != null)this._definition.addAll(child._definition);
}}protected DisableProperty _disableProperty;public DisableProperty getDisable() { return _disableProperty; }public void setDisable(DisableProperty value) { if(_disableProperty!=null) _disableProperty.merge(value); else {_disableProperty = value;} }public static class AssignRoleProperty  {protected String _user;public String getUser() { return _user; }public void setUser(String value) { _user = value; }protected ArrayList<org.monet.metamodel.internal.Ref> _role = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getRole() { return _role; }public void setRole(ArrayList<org.monet.metamodel.internal.Ref> value) { _role = value; }public static class PartnerProperty  {protected String _name;public String getName() { return _name; }public void setName(String value) { _name = value; }protected ArrayList<String> _service = new ArrayList<String>();public ArrayList<String> getService() { return _service; }public void setService(ArrayList<String> value) { _service = value; }protected void copy(PartnerProperty instance) {this._name = instance._name;
this._service.addAll(instance._service);
}protected void merge(PartnerProperty child) {if(child._name != null)this._name = child._name;
if(child._service != null)this._service.addAll(child._service);
}}protected PartnerProperty _partnerProperty;public PartnerProperty getPartner() { return _partnerProperty; }public void setPartner(PartnerProperty value) { if(_partnerProperty!=null) _partnerProperty.merge(value); else {_partnerProperty = value;} }protected void copy(AssignRoleProperty instance) {this._user = instance._user;
this._role.addAll(instance._role);
this._partnerProperty = instance._partnerProperty; 
}protected void merge(AssignRoleProperty child) {if(child._user != null)this._user = child._user;
if(child._role != null)this._role.addAll(child._role);
if(_partnerProperty == null) _partnerProperty = child._partnerProperty; else if (child._partnerProperty != null) {_partnerProperty.merge(child._partnerProperty);}
}}protected ArrayList<AssignRoleProperty> _assignRolePropertyList = new ArrayList<AssignRoleProperty>();public ArrayList<AssignRoleProperty> getAssignRoleList() { return _assignRolePropertyList; }
	

	public void copy(AbstractManifestBase instance) {
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

	public void merge(AbstractManifestBase child) {
		
		
		if(child._name != null)this._name = child._name;
if(child._title != null)this._title = child._title;
if(child._subtitle != null)this._subtitle = child._subtitle;
if(child._script != null)this._script.addAll(child._script);

		if(_spaceProperty == null) _spaceProperty = child._spaceProperty; else if (child._spaceProperty != null) {_spaceProperty.merge(child._spaceProperty);}
if(_defaultLocationProperty == null) _defaultLocationProperty = child._defaultLocationProperty; else if (child._defaultLocationProperty != null) {_defaultLocationProperty.merge(child._defaultLocationProperty);}
if(_federationProperty == null) _federationProperty = child._federationProperty; else if (child._federationProperty != null) {_federationProperty.merge(child._federationProperty);}
_publishPropertyList.addAll(child._publishPropertyList);
_unpublishPropertyList.addAll(child._unpublishPropertyList);
if(_disableProperty == null) _disableProperty = child._disableProperty; else if (child._disableProperty != null) {_disableProperty.merge(child._disableProperty);}
_assignRolePropertyList.addAll(child._assignRolePropertyList);

		
	}

	public Class<?> getMetamodelClass() {
		return AbstractManifestBase.class;
	}

}

