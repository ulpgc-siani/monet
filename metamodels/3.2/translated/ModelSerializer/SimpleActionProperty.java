package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
SimpleActionProperty


*/

public abstract class SimpleActionProperty extends PlaceActionProperty {

	protected org.monet.metamodel.internal.Ref _goto;public org.monet.metamodel.internal.Ref getGoto() { return _goto; }public void setGoto(org.monet.metamodel.internal.Ref value) { _goto = value; }protected Object _history;public Object getHistory() { return _history; }public void setHistory(Object value) { _history = value; }
	
	

	public void copy(SimpleActionProperty instance) {
		this._goto = instance._goto;
this._history = instance._history;
this._name = instance._name;
this._label = instance._label;
this._code = instance._code;
this._name = instance._name;

		this._requireConfirmationProperty = instance._requireConfirmationProperty; 

		
	}

	public void merge(SimpleActionProperty child) {
		super.merge(child);
		
		if(child._goto != null)this._goto = child._goto;
if(child._history != null)this._history = child._history;

		
		
	}

	public Class<?> getMetamodelClass() {
		return SimpleActionProperty.class;
	}

}

