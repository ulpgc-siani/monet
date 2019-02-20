package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
RuleProperty
Declaración del tipo abstracto de vista

*/

public abstract class RuleProperty  {

	protected String _code;public String getCode() { return _code; }public void setCode(String value) { _code = value; }
	public static class ListenProperty  {protected ArrayList<org.monet.metamodel.internal.Ref> _sibling = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getSibling() { return _sibling; }public void setSibling(ArrayList<org.monet.metamodel.internal.Ref> value) { _sibling = value; }protected ArrayList<org.monet.metamodel.internal.Ref> _link = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getLink() { return _link; }public void setLink(ArrayList<org.monet.metamodel.internal.Ref> value) { _link = value; }protected ArrayList<org.monet.metamodel.internal.Ref> _children = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getChildren() { return _children; }public void setChildren(ArrayList<org.monet.metamodel.internal.Ref> value) { _children = value; }public static class ParentProperty  {protected void copy(ParentProperty instance) {}protected void merge(ParentProperty child) {}}protected ParentProperty _parentProperty;public ParentProperty getParent() { return _parentProperty; }public void setParent(ParentProperty value) { if(_parentProperty!=null) _parentProperty.merge(value); else {_parentProperty = value;} }protected void copy(ListenProperty instance) {this._sibling.addAll(instance._sibling);
this._link.addAll(instance._link);
this._children.addAll(instance._children);
this._parentProperty = instance._parentProperty; 
}protected void merge(ListenProperty child) {if(child._sibling != null)this._sibling.addAll(child._sibling);
if(child._link != null)this._link.addAll(child._link);
if(child._children != null)this._children.addAll(child._children);
if(_parentProperty == null) _parentProperty = child._parentProperty; else if (child._parentProperty != null) {_parentProperty.merge(child._parentProperty);}
}}protected ListenProperty _listenProperty;public ListenProperty getListen() { return _listenProperty; }public void setListen(ListenProperty value) { if(_listenProperty!=null) _listenProperty.merge(value); else {_listenProperty = value;} }
	

	public void copy(RuleProperty instance) {
		this._code = instance._code;

		this._listenProperty = instance._listenProperty; 

		
	}

	public void merge(RuleProperty child) {
		
		
		if(child._code != null)this._code = child._code;

		if(_listenProperty == null) _listenProperty = child._listenProperty; else if (child._listenProperty != null) {_listenProperty.merge(child._listenProperty);}

		
	}

	public Class<?> getMetamodelClass() {
		return RuleProperty.class;
	}

}

