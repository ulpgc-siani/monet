package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
TaskRequestProperty


*/

public abstract class TaskRequestProperty  {

	protected String _code;public String getCode() { return _code; }public void setCode(String value) { _code = value; }protected String _name;public String getName() { return _name; }public void setName(String value) { _name = value; }
	
	

	public void copy(TaskRequestProperty instance) {
		this._code = instance._code;
this._name = instance._name;

		
		
	}

	public void merge(TaskRequestProperty child) {
		
		
		if(child._code != null)this._code = child._code;
if(child._name != null)this._name = child._name;

		
		
	}

	public Class<?> getMetamodelClass() {
		return TaskRequestProperty.class;
	}

}

