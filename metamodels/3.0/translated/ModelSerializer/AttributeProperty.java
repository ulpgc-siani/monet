package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
AttributeProperty
Esta propiedad se utiliza para añadir un atributo a un índice

*/

public  class AttributeProperty extends ReferenceableProperty {

	protected Object _label;public Object getLabel() { return _label; }public void setLabel(Object value) { _label = value; }protected Object _description;public Object getDescription() { return _description; }public void setDescription(Object value) { _description = value; }public enum TypeEnumeration { BOOLEAN,STRING,INTEGER,REAL,DATE,TERM,LINK,CHECK,PICTURE,CATEGORY }protected TypeEnumeration _type;public TypeEnumeration getType() { return _type; }public void setType(TypeEnumeration value) { _type = value; }public enum PrecisionEnumeration { YEARS,MONTHS,DAYS,HOURS,MINUTES,SECONDS }protected PrecisionEnumeration _precision;public PrecisionEnumeration getPrecision() { return _precision; }public void setPrecision(PrecisionEnumeration value) { _precision = value; }protected org.monet.metamodel.internal.Ref _source;public org.monet.metamodel.internal.Ref getSource() { return _source; }public void setSource(org.monet.metamodel.internal.Ref value) { _source = value; }
	
	

	public void copy(AttributeProperty instance) {
		this._label = instance._label;
this._description = instance._description;
this._type = instance._type;
this._precision = instance._precision;
this._source = instance._source;
this._code = instance._code;
this._name = instance._name;

		
		
	}

	public void merge(AttributeProperty child) {
		super.merge(child);
		
		if(child._label != null)this._label = child._label;
if(child._description != null)this._description = child._description;
if(child._type != null)this._type = child._type;
if(child._precision != null)this._precision = child._precision;
if(child._source != null)this._source = child._source;

		
		
	}

	public Class<?> getMetamodelClass() {
		return AttributeProperty.class;
	}

}

