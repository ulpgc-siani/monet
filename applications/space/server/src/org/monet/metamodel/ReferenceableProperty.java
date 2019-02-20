package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
ReferenceableProperty
Declaración del tipo abstracto propiedad referenciable por nombre en el modelo de negocio

*/

public abstract class ReferenceableProperty  {

	protected String _code;public String getCode() { return _code; }public void setCode(String value) { _code = value; }protected String _name;public String getName() { return _name; }public void setName(String value) { _name = value; }
	
	

	public void copy(ReferenceableProperty instance) {
		this._code = instance._code;
this._name = instance._name;

		
		
	}

	public void merge(ReferenceableProperty child) {
		
		
		if(child._code != null)this._code = child._code;
if(child._name != null)this._name = child._name;

		
		
	}

	public Class<?> getMetamodelClass() {
		return ReferenceableProperty.class;
	}

}

