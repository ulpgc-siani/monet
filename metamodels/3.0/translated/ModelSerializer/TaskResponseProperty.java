package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
TaskResponseProperty


*/

public abstract class TaskResponseProperty  {

	protected String _code;public String getCode() { return _code; }public void setCode(String value) { _code = value; }protected String _name;public String getName() { return _name; }public void setName(String value) { _name = value; }protected org.monet.metamodel.internal.Ref _unlock;public org.monet.metamodel.internal.Ref getUnlock() { return _unlock; }public void setUnlock(org.monet.metamodel.internal.Ref value) { _unlock = value; }protected org.monet.metamodel.internal.Ref _goto;public org.monet.metamodel.internal.Ref getGoto() { return _goto; }public void setGoto(org.monet.metamodel.internal.Ref value) { _goto = value; }
	
	

	public void copy(TaskResponseProperty instance) {
		this._code = instance._code;
this._name = instance._name;
this._unlock = instance._unlock;
this._goto = instance._goto;

		
		
	}

	public void merge(TaskResponseProperty child) {
		
		
		if(child._code != null)this._code = child._code;
if(child._name != null)this._name = child._name;
if(child._unlock != null)this._unlock = child._unlock;
if(child._goto != null)this._goto = child._goto;

		
		
	}

	public Class<?> getMetamodelClass() {
		return TaskResponseProperty.class;
	}

}

