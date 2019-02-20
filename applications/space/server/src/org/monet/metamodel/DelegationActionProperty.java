package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
DelegationActionProperty


*/

public  class DelegationActionProperty extends SimpleActionProperty {

	protected org.monet.metamodel.internal.Ref _provider;public org.monet.metamodel.internal.Ref getProvider() { return _provider; }public void setProvider(org.monet.metamodel.internal.Ref value) { _provider = value; }public enum ModeEnumeration { AUTOMATIC,SELECT }protected ModeEnumeration _mode;public ModeEnumeration getMode() { return _mode; }public void setMode(ModeEnumeration value) { _mode = value; }
	
	

	public void copy(DelegationActionProperty instance) {
		this._provider = instance._provider;
this._mode = instance._mode;
this._goto = instance._goto;
this._history = instance._history;
this._name = instance._name;
this._label = instance._label;
this._code = instance._code;
this._name = instance._name;

		this._requireConfirmationProperty = instance._requireConfirmationProperty; 

		
	}

	public void merge(DelegationActionProperty child) {
		super.merge(child);
		
		if(child._provider != null)this._provider = child._provider;
if(child._mode != null)this._mode = child._mode;

		
		
	}

	public Class<?> getMetamodelClass() {
		return DelegationActionProperty.class;
	}

}

