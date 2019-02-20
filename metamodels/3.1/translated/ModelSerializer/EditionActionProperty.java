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
}}protected UseProperty _useProperty;public UseProperty getUse() { return _useProperty; }public void setUse(UseProperty value) { if(_useProperty!=null) _useProperty.merge(value); else {_useProperty = value;} }
	

	public void copy(EditionActionProperty instance) {
		this._goto = instance._goto;
this._history = instance._history;
this._name = instance._name;
this._label = instance._label;
this._code = instance._code;
this._name = instance._name;

		this._useProperty = instance._useProperty; 

		
	}

	public void merge(EditionActionProperty child) {
		super.merge(child);
		
		
		if(_useProperty == null) _useProperty = child._useProperty; else if (child._useProperty != null) {_useProperty.merge(child._useProperty);}

		
	}

	public Class<?> getMetamodelClass() {
		return EditionActionProperty.class;
	}

}

