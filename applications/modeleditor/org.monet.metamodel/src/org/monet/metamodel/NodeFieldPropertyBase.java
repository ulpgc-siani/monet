package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
NodeFieldProperty
Esta propiedad se utiliza para incluir un campo nodo en un formulario

*/

public  class NodeFieldPropertyBase extends MultipleableFieldProperty {

	
	public static class ContainProperty  {protected org.monet.metamodel.internal.Ref _node;public org.monet.metamodel.internal.Ref getNode() { return _node; }public void setNode(org.monet.metamodel.internal.Ref value) { _node = value; }protected void copy(ContainProperty instance) {this._node = instance._node;
}protected void merge(ContainProperty child) {if(child._node != null)this._node = child._node;
}}protected ContainProperty _containProperty;public ContainProperty getContain() { return _containProperty; }public void setContain(ContainProperty value) { if(_containProperty!=null) _containProperty.merge(value); else {_containProperty = value;} }public static class AddProperty  {protected ArrayList<org.monet.metamodel.internal.Ref> _node = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getNode() { return _node; }public void setNode(ArrayList<org.monet.metamodel.internal.Ref> value) { _node = value; }protected void copy(AddProperty instance) {this._node.addAll(instance._node);
}protected void merge(AddProperty child) {if(child._node != null)this._node.addAll(child._node);
}}protected AddProperty _addProperty;public AddProperty getAdd() { return _addProperty; }public void setAdd(AddProperty value) { if(_addProperty!=null) _addProperty.merge(value); else {_addProperty = value;} }
	

	public void copy(NodeFieldPropertyBase instance) {
		this._label = instance._label;
this._description = instance._description;
this._template = instance._template;
this._code = instance._code;
this._name = instance._name;

		this._containProperty = instance._containProperty; 
this._addProperty = instance._addProperty; 
this._isMultiple = instance._isMultiple; 
this._boundaryProperty = instance._boundaryProperty; 
this._isRequired = instance._isRequired; 
this._isReadonly = instance._isReadonly; 
this._isCollapsible = instance._isCollapsible; 
this._isExtended = instance._isExtended; 
this._isStatic = instance._isStatic; 
this._isUnivocal = instance._isUnivocal; 
_displayPropertyList.addAll(instance._displayPropertyList);

		
	}

	public void merge(NodeFieldPropertyBase child) {
		super.merge(child);
		
		
		if(_containProperty == null) _containProperty = child._containProperty; else if (child._containProperty != null) {_containProperty.merge(child._containProperty);}
if(_addProperty == null) _addProperty = child._addProperty; else if (child._addProperty != null) {_addProperty.merge(child._addProperty);}

		
	}

	public Class<?> getMetamodelClass() {
		return NodeFieldPropertyBase.class;
	}

}

