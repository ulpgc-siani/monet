package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
SendResponseActionProperty


*/

public  class SendResponseActionProperty extends SimpleActionProperty {

	protected org.monet.metamodel.internal.Ref _response;public org.monet.metamodel.internal.Ref getResponse() { return _response; }public void setResponse(org.monet.metamodel.internal.Ref value) { _response = value; }
	
	

	public void copy(SendResponseActionProperty instance) {
		this._response = instance._response;
this._goto = instance._goto;
this._history = instance._history;
this._name = instance._name;
this._label = instance._label;
this._code = instance._code;
this._name = instance._name;

		this._requireConfirmationProperty = instance._requireConfirmationProperty; 

		
	}

	public void merge(SendResponseActionProperty child) {
		super.merge(child);
		
		if(child._response != null)this._response = child._response;

		
		
	}

	public Class<?> getMetamodelClass() {
		return SendResponseActionProperty.class;
	}

}

