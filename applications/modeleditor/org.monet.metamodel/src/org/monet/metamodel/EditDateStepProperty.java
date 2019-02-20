package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
EditDateStepProperty


*/

public  class EditDateStepProperty extends StepEditProperty {

	
	
	

	public void copy(EditDateStepProperty instance) {
		this._label = instance._label;
this._code = instance._code;
this._name = instance._name;

		this._isRequired = instance._isRequired; 
this._isReadOnly = instance._isReadOnly; 
this._isMultiple = instance._isMultiple; 

		
	}

	public void merge(EditDateStepProperty child) {
		super.merge(child);
		
		
		
		
	}

	public Class<?> getMetamodelClass() {
		return EditDateStepProperty.class;
	}

}

