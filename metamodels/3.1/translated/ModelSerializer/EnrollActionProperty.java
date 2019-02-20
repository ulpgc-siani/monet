package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
EnrollActionProperty


*/

public  class EnrollActionProperty extends SimpleActionProperty {

	protected org.monet.metamodel.internal.Ref _contest;public org.monet.metamodel.internal.Ref getContest() { return _contest; }public void setContest(org.monet.metamodel.internal.Ref value) { _contest = value; }
	
	

	public void copy(EnrollActionProperty instance) {
		this._contest = instance._contest;
this._goto = instance._goto;
this._history = instance._history;
this._name = instance._name;
this._label = instance._label;
this._code = instance._code;
this._name = instance._name;

		
		
	}

	public void merge(EnrollActionProperty child) {
		super.merge(child);
		
		if(child._contest != null)this._contest = child._contest;

		
		
	}

	public Class<?> getMetamodelClass() {
		return EnrollActionProperty.class;
	}

}

