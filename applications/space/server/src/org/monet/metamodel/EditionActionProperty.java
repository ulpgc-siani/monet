package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
EditionActionProperty


*/

public  class EditionActionProperty extends SimpleActionProperty {

	
	public static class UseProperty  {protected org.monet.metamodel.internal.Ref _form;public org.monet.metamodel.internal.Ref getForm() { return _form; }public void setForm(org.monet.metamodel.internal.Ref value) { _form = value; }protected org.monet.metamodel.internal.Ref _withView;public org.monet.metamodel.internal.Ref getWithView() { return _withView; }public void setWithView(org.monet.metamodel.internal.Ref value) { _withView = value; }protected void copy(UseProperty instance) {this._form = instance._form;
this._withView = instance._withView;
}protected void merge(UseProperty child) {if(child._form != null)this._form = child._form;
if(child._withView != null)this._withView = child._withView;
}}protected UseProperty _useProperty;public UseProperty getUse() { return _useProperty; }public void setUse(UseProperty value) { if(_useProperty!=null) _useProperty.merge(value); else {_useProperty = value;} }public static class TimeoutProperty  {protected org.monet.metamodel.internal.Time _after;public org.monet.metamodel.internal.Time getAfter() { return _after; }public void setAfter(org.monet.metamodel.internal.Time value) { _after = value; }protected org.monet.metamodel.internal.Ref _take;public org.monet.metamodel.internal.Ref getTake() { return _take; }public void setTake(org.monet.metamodel.internal.Ref value) { _take = value; }protected void copy(TimeoutProperty instance) {this._after = instance._after;
this._take = instance._take;
}protected void merge(TimeoutProperty child) {if(child._after != null)this._after = child._after;
if(child._take != null)this._take = child._take;
}}protected TimeoutProperty _timeoutProperty;public TimeoutProperty getTimeout() { return _timeoutProperty; }public void setTimeout(TimeoutProperty value) { if(_timeoutProperty!=null) _timeoutProperty.merge(value); else {_timeoutProperty = value;} }
	

	public void copy(EditionActionProperty instance) {
		this._goto = instance._goto;
this._history = instance._history;
this._name = instance._name;
this._label = instance._label;
this._code = instance._code;
this._name = instance._name;

		this._useProperty = instance._useProperty; 
this._timeoutProperty = instance._timeoutProperty; 
this._requireConfirmationProperty = instance._requireConfirmationProperty; 

		
	}

	public void merge(EditionActionProperty child) {
		super.merge(child);
		
		
		if(_useProperty == null) _useProperty = child._useProperty; else if (child._useProperty != null) {_useProperty.merge(child._useProperty);}
if(_timeoutProperty == null) _timeoutProperty = child._timeoutProperty; else if (child._timeoutProperty != null) {_timeoutProperty.merge(child._timeoutProperty);}

		
	}

	public Class<?> getMetamodelClass() {
		return EditionActionProperty.class;
	}

}

