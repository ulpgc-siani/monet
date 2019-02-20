package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
EditNumberStepProperty


*/

public  class EditNumberStepProperty extends StepEditProperty {

	
	
	

	public void copy(EditNumberStepProperty instance) {
		this._label = instance._label;
this._code = instance._code;
this._name = instance._name;

		this._isRequired = instance._isRequired; 
this._isReadOnly = instance._isReadOnly; 
this._isMultiple = instance._isMultiple; 

		
	}

	public void merge(EditNumberStepProperty child) {
		super.merge(child);
		
		
		
		
	}

	public Class<?> getMetamodelClass() {
		return EditNumberStepProperty.class;
	}

}

