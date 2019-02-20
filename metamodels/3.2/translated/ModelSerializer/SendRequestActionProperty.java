package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
SendRequestActionProperty


*/

public  class SendRequestActionProperty extends SimpleActionProperty {

	protected org.monet.metamodel.internal.Ref _collaborator;public org.monet.metamodel.internal.Ref getCollaborator() { return _collaborator; }public void setCollaborator(org.monet.metamodel.internal.Ref value) { _collaborator = value; }protected org.monet.metamodel.internal.Ref _provider;public org.monet.metamodel.internal.Ref getProvider() { return _provider; }public void setProvider(org.monet.metamodel.internal.Ref value) { _provider = value; }protected org.monet.metamodel.internal.Ref _request;public org.monet.metamodel.internal.Ref getRequest() { return _request; }public void setRequest(org.monet.metamodel.internal.Ref value) { _request = value; }
	
	

	public void copy(SendRequestActionProperty instance) {
		this._collaborator = instance._collaborator;
this._provider = instance._provider;
this._request = instance._request;
this._goto = instance._goto;
this._history = instance._history;
this._name = instance._name;
this._label = instance._label;
this._code = instance._code;
this._name = instance._name;

		this._requireConfirmationProperty = instance._requireConfirmationProperty; 

		
	}

	public void merge(SendRequestActionProperty child) {
		super.merge(child);
		
		if(child._collaborator != null)this._collaborator = child._collaborator;
if(child._provider != null)this._provider = child._provider;
if(child._request != null)this._request = child._request;

		
		
	}

	public Class<?> getMetamodelClass() {
		return SendRequestActionProperty.class;
	}

}

