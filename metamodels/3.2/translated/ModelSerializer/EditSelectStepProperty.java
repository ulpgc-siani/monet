package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
EditSelectStepProperty


*/

public  class EditSelectStepProperty extends EditCheckStepProperty {

	
	public static class IsEmbedded  {protected void copy(IsEmbedded instance) {}protected void merge(IsEmbedded child) {}}protected IsEmbedded _isEmbedded;public boolean isEmbedded() { return (_isEmbedded != null); }public IsEmbedded getIsEmbedded() { return _isEmbedded; }public void setIsEmbedded(boolean value) { if(value) _isEmbedded = new IsEmbedded(); else {_isEmbedded = null;}}
	

	public void copy(EditSelectStepProperty instance) {
		this._source = instance._source;
this._label = instance._label;
this._code = instance._code;
this._name = instance._name;

		this._isEmbedded = instance._isEmbedded; 
this._termsProperty = instance._termsProperty; 
this._showProperty = instance._showProperty; 
this._isRequired = instance._isRequired; 
this._isReadOnly = instance._isReadOnly; 
this._isMultiple = instance._isMultiple; 

		
	}

	public void merge(EditSelectStepProperty child) {
		super.merge(child);
		
		
		if(_isEmbedded == null) _isEmbedded = child._isEmbedded; else if (child._isEmbedded != null) {_isEmbedded.merge(child._isEmbedded);}

		
	}

	public Class<?> getMetamodelClass() {
		return EditSelectStepProperty.class;
	}

}

