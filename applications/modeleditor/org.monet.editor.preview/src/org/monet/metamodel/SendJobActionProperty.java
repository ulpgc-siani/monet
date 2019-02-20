package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
SendJobActionProperty


*/

public  class SendJobActionProperty extends SimpleActionProperty {

	protected org.monet.metamodel.internal.Ref _job;public org.monet.metamodel.internal.Ref getJob() { return _job; }public void setJob(org.monet.metamodel.internal.Ref value) { _job = value; }protected org.monet.metamodel.internal.Ref _role;public org.monet.metamodel.internal.Ref getRole() { return _role; }public void setRole(org.monet.metamodel.internal.Ref value) { _role = value; }public enum ModeEnumeration { AUTOMATIC,SELECT }protected ModeEnumeration _mode;public ModeEnumeration getMode() { return _mode; }public void setMode(ModeEnumeration value) { _mode = value; }
	
	

	public void copy(SendJobActionProperty instance) {
		this._job = instance._job;
this._role = instance._role;
this._mode = instance._mode;
this._goto = instance._goto;
this._history = instance._history;
this._name = instance._name;
this._label = instance._label;
this._code = instance._code;
this._name = instance._name;

		this._requireConfirmationProperty = instance._requireConfirmationProperty; 

		
	}

	public void merge(SendJobActionProperty child) {
		super.merge(child);
		
		if(child._job != null)this._job = child._job;
if(child._role != null)this._role = child._role;
if(child._mode != null)this._mode = child._mode;

		
		
	}

	public Class<?> getMetamodelClass() {
		return SendJobActionProperty.class;
	}

}

