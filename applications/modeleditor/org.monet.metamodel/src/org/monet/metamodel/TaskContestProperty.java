package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
TaskContestProperty


*/

public  class TaskContestProperty extends ReferenceableProperty {

	
	
	

	public void copy(TaskContestProperty instance) {
		this._code = instance._code;
this._name = instance._name;

		
		
	}

	public void merge(TaskContestProperty child) {
		super.merge(child);
		
		
		
		
	}

	public Class<?> getMetamodelClass() {
		return TaskContestProperty.class;
	}

}

