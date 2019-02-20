package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
TaskDefinition
Una tarea es una trabajo colectivo o individual que se desarrolla en la unidad de negocio

*/

public abstract class TaskDefinitionBase extends EntityDefinition {

	protected org.monet.metamodel.internal.Ref _role;public org.monet.metamodel.internal.Ref getRole() { return _role; }public void setRole(org.monet.metamodel.internal.Ref value) { _role = value; }
	public static class IsPrivate  {protected void copy(IsPrivate instance) {}protected void merge(IsPrivate child) {}}protected IsPrivate _isPrivate;public boolean isPrivate() { return (_isPrivate != null); }public IsPrivate getIsPrivate() { return _isPrivate; }public void setIsPrivate(boolean value) { if(value) _isPrivate = new IsPrivate(); else {_isPrivate = null;}}public static class IsBackground  {protected void copy(IsBackground instance) {}protected void merge(IsBackground child) {}}protected IsBackground _isBackground;public boolean isBackground() { return (_isBackground != null); }public IsBackground getIsBackground() { return _isBackground; }public void setIsBackground(boolean value) { if(value) _isBackground = new IsBackground(); else {_isBackground = null;}}
	

	public void copy(TaskDefinitionBase instance) {
		this._role = instance._role;
this._code = instance._code;
this._name = instance._name;
this._parent = instance._parent;
this._label = instance._label;
this._description = instance._description;
this._help = instance._help;

		this._isPrivate = instance._isPrivate; 
this._isBackground = instance._isBackground; 
this._isAbstract = instance._isAbstract; 

		
	}

	public void merge(TaskDefinitionBase child) {
		super.merge(child);
		
		if(child._role != null)this._role = child._role;

		if(_isPrivate == null) _isPrivate = child._isPrivate; else if (child._isPrivate != null) {_isPrivate.merge(child._isPrivate);}
if(_isBackground == null) _isBackground = child._isBackground; else if (child._isBackground != null) {_isBackground.merge(child._isBackground);}

		
	}

	public Class<?> getMetamodelClass() {
		return TaskDefinitionBase.class;
	}

}

