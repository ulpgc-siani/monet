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
	public static class IsDefault  {protected void copy(IsDefault instance) {}protected void merge(IsDefault child) {}}protected IsDefault _isDefault;public boolean isDefault() { return (_isDefault != null); }public IsDefault getIsDefault() { return _isDefault; }public void setIsDefault(boolean value) { if(value) _isDefault = new IsDefault(); else {_isDefault = null;}}public static class IsWidget  {protected void copy(IsWidget instance) {}protected void merge(IsWidget child) {}}protected IsWidget _isWidget;public boolean isWidget() { return (_isWidget != null); }public IsWidget getIsWidget() { return _isWidget; }public void setIsWidget(boolean value) { if(value) _isWidget = new IsWidget(); else {_isWidget = null;}}public static class ForProperty  {protected org.monet.metamodel.internal.Ref _role;public org.monet.metamodel.internal.Ref getRole() { return _role; }public void setRole(org.monet.metamodel.internal.Ref value) { _role = value; }protected void copy(ForProperty instance) {this._role = instance._role;
}protected void merge(ForProperty child) {if(child._role != null)this._role = child._role;
}}protected ForProperty _forProperty;public ForProperty getFor() { return _forProperty; }public void setFor(ForProperty value) { if(_forProperty!=null) _forProperty.merge(value); else {_forProperty = value;} }
	

	public void copy(NodeViewProperty instance) {
		this._label = instance._label;
this._code = instance._code;
this._name = instance._name;

		this._isDefault = instance._isDefault; 
this._isWidget = instance._isWidget; 
this._forProperty = instance._forProperty; 

		
	}

	public void merge(NodeViewProperty child) {
		super.merge(child);
		
		if(child._label != null)this._label = child._label;

		if(_isDefault == null) _isDefault = child._isDefault; else if (child._isDefault != null) {_isDefault.merge(child._isDefault);}
if(_isWidget == null) _isWidget = child._isWidget; else if (child._isWidget != null) {_isWidget.merge(child._isWidget);}
if(_forProperty == null) _forProperty = child._forProperty; else if (child._forProperty != null) {_forProperty.merge(child._forProperty);}

		
	}

	public Class<?> getMetamodelClass() {
		return NodeViewProperty.class;
	}

}

