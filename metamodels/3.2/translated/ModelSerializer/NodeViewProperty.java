package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
NodeViewProperty
Declaración del tipo abstracto de vista de nodo

*/

public abstract class NodeViewProperty extends ViewProperty {

	protected Object _label;public Object getLabel() { return _label; }public void setLabel(Object value) { _label = value; }
	public static class IsDefault  {protected void copy(IsDefault instance) {}protected void merge(IsDefault child) {}}protected IsDefault _isDefault;public boolean isDefault() { return (_isDefault != null); }public IsDefault getIsDefault() { return _isDefault; }public void setIsDefault(boolean value) { if(value) _isDefault = new IsDefault(); else {_isDefault = null;}}public static class IsVisibleWhenEmbedded  {protected void copy(IsVisibleWhenEmbedded instance) {}protected void merge(IsVisibleWhenEmbedded child) {}}protected IsVisibleWhenEmbedded _isVisibleWhenEmbedded;public boolean isVisibleWhenEmbedded() { return (_isVisibleWhenEmbedded != null); }public IsVisibleWhenEmbedded getIsVisibleWhenEmbedded() { return _isVisibleWhenEmbedded; }public void setIsVisibleWhenEmbedded(boolean value) { if(value) _isVisibleWhenEmbedded = new IsVisibleWhenEmbedded(); else {_isVisibleWhenEmbedded = null;}}public static class ForProperty  {protected org.monet.metamodel.internal.Ref _role;public org.monet.metamodel.internal.Ref getRole() { return _role; }public void setRole(org.monet.metamodel.internal.Ref value) { _role = value; }protected void copy(ForProperty instance) {this._role = instance._role;
}protected void merge(ForProperty child) {if(child._role != null)this._role = child._role;
}}protected ForProperty _forProperty;public ForProperty getFor() { return _forProperty; }public void setFor(ForProperty value) { if(_forProperty!=null) _forProperty.merge(value); else {_forProperty = value;} }
	

	public void copy(NodeViewProperty instance) {
		this._label = instance._label;
this._code = instance._code;
this._name = instance._name;

		this._isDefault = instance._isDefault; 
this._isVisibleWhenEmbedded = instance._isVisibleWhenEmbedded; 
this._forProperty = instance._forProperty; 

		
	}

	public void merge(NodeViewProperty child) {
		super.merge(child);
		
		if(child._label != null)this._label = child._label;

		if(_isDefault == null) _isDefault = child._isDefault; else if (child._isDefault != null) {_isDefault.merge(child._isDefault);}
if(_isVisibleWhenEmbedded == null) _isVisibleWhenEmbedded = child._isVisibleWhenEmbedded; else if (child._isVisibleWhenEmbedded != null) {_isVisibleWhenEmbedded.merge(child._isVisibleWhenEmbedded);}
if(_forProperty == null) _forProperty = child._forProperty; else if (child._forProperty != null) {_forProperty.merge(child._forProperty);}

		
	}

	public Class<?> getMetamodelClass() {
		return NodeViewProperty.class;
	}

}

