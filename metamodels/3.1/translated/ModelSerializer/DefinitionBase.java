package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
Definition
Una definición describe una entidad en el modelo de negocio

*/

public abstract class DefinitionBase  {

	protected String _code;public String getCode() { return _code; }public void setCode(String value) { _code = value; }protected String _name;public String getName() { return _name; }public void setName(String value) { _name = value; }protected String _parent;public String getParent() { return _parent; }public void setParent(String value) { _parent = value; }protected Object _label;public Object getLabel() { return _label; }public void setLabel(Object value) { _label = value; }protected Object _description;public Object getDescription() { return _description; }public void setDescription(Object value) { _description = value; }protected Object _help;public Object getHelp() { return _help; }public void setHelp(Object value) { _help = value; }
	public static class IsAbstract  {protected void copy(IsAbstract instance) {}protected void merge(IsAbstract child) {}}protected IsAbstract _isAbstract;public boolean isAbstract() { return (_isAbstract != null); }public IsAbstract getIsAbstract() { return _isAbstract; }public void setIsAbstract(boolean value) { if(value) _isAbstract = new IsAbstract(); else {_isAbstract = null;}}
	

	public void copy(DefinitionBase instance) {
		this._code = instance._code;
this._name = instance._name;
this._parent = instance._parent;
this._label = instance._label;
this._description = instance._description;
this._help = instance._help;

		this._isAbstract = instance._isAbstract; 

		
	}

	public void merge(DefinitionBase child) {
		
		
		if(child._code != null)this._code = child._code;
if(child._name != null)this._name = child._name;
if(child._parent != null)this._parent = child._parent;
if(child._label != null)this._label = child._label;
if(child._description != null)this._description = child._description;
if(child._help != null)this._help = child._help;

		if(_isAbstract == null) _isAbstract = child._isAbstract; else if (child._isAbstract != null) {_isAbstract.merge(child._isAbstract);}

		
	}

	public Class<?> getMetamodelClass() {
		return DefinitionBase.class;
	}

}

