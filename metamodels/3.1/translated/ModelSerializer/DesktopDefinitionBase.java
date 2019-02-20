package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
DesktopDefinition
Un escritorio es un tipo de nodo que se utiliza para crear el entorno de trabajo de un usuario

*/

public  class DesktopDefinitionBase extends NodeDefinition {

	
	public static class ContainProperty  {protected ArrayList<org.monet.metamodel.internal.Ref> _node = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getNode() { return _node; }public void setNode(ArrayList<org.monet.metamodel.internal.Ref> value) { _node = value; }protected void copy(ContainProperty instance) {this._node.addAll(instance._node);
}protected void merge(ContainProperty child) {if(child._node != null)this._node.addAll(child._node);
}}protected ContainProperty _containProperty;public ContainProperty getContain() { return _containProperty; }public void setContain(ContainProperty value) { if(_containProperty!=null) _containProperty.merge(value); else {_containProperty = value;} }public static class ForProperty  {protected ArrayList<org.monet.metamodel.internal.Ref> _role = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getRole() { return _role; }public void setRole(ArrayList<org.monet.metamodel.internal.Ref> value) { _role = value; }protected void copy(ForProperty instance) {this._role.addAll(instance._role);
}protected void merge(ForProperty child) {if(child._role != null)this._role.addAll(child._role);
}}protected ForProperty _forProperty;public ForProperty getFor() { return _forProperty; }public void setFor(ForProperty value) { if(_forProperty!=null) _forProperty.merge(value); else {_forProperty = value;} }public static class ViewProperty extends org.monet.metamodel.NodeViewProperty {public static class ShowProperty  {protected ArrayList<org.monet.metamodel.internal.Ref> _link = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getLink() { return _link; }public void setLink(ArrayList<org.monet.metamodel.internal.Ref> value) { _link = value; }protected org.monet.metamodel.internal.Ref _dashboard;public org.monet.metamodel.internal.Ref getDashboard() { return _dashboard; }public void setDashboard(org.monet.metamodel.internal.Ref value) { _dashboard = value; }protected void copy(ShowProperty instance) {this._link.addAll(instance._link);
this._dashboard = instance._dashboard;
}protected void merge(ShowProperty child) {if(child._link != null)this._link.addAll(child._link);
if(child._dashboard != null)this._dashboard = child._dashboard;
}}protected ShowProperty _showProperty;public ShowProperty getShow() { return _showProperty; }public void setShow(ShowProperty value) { if(_showProperty!=null) _showProperty.merge(value); else {_showProperty = value;} }protected void copy(ViewProperty instance) {this._label = instance._label;
this._code = instance._code;
this._name = instance._name;
this._showProperty = instance._showProperty; 
this._isDefault = instance._isDefault; 
this._isVisibleWhenEmbedded = instance._isVisibleWhenEmbedded; 
this._forProperty = instance._forProperty; 
}protected void merge(ViewProperty child) {super.merge(child);if(_showProperty == null) _showProperty = child._showProperty; else if (child._showProperty != null) {_showProperty.merge(child._showProperty);}
}}protected LinkedHashMap<String, ViewProperty> _viewPropertyMap = new LinkedHashMap<String, ViewProperty>();public void addView(ViewProperty value) {String key = value.getName() != null ? value.getName() : value.getCode();ViewProperty current = _viewPropertyMap.get(key);if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {ViewProperty instance = value.getClass().newInstance();instance.copy(current);instance.setCode(value.getCode());instance.setName(value.getName());instance.merge(value);_viewPropertyMap.put(key, instance);}catch (Exception exception) {}}else current.merge(value);} else {_viewPropertyMap.put(key, value);} }public java.util.Map<String,ViewProperty> getViewMap() { return _viewPropertyMap; }public java.util.Collection<ViewProperty> getViewList() { return _viewPropertyMap.values(); }public static class RuleLinkProperty extends org.monet.metamodel.RuleProperty {public enum AddFlagEnumeration { HIDDEN }protected ArrayList<AddFlagEnumeration> _addFlag = new ArrayList<AddFlagEnumeration>();public ArrayList<AddFlagEnumeration> getAddFlag() { return _addFlag; }public void setAddFlag(ArrayList<AddFlagEnumeration> value) { _addFlag = value; }public static class ToProperty  {protected ArrayList<org.monet.metamodel.internal.Ref> _link = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getLink() { return _link; }public void setLink(ArrayList<org.monet.metamodel.internal.Ref> value) { _link = value; }protected void copy(ToProperty instance) {this._link.addAll(instance._link);
}protected void merge(ToProperty child) {if(child._link != null)this._link.addAll(child._link);
}}protected ToProperty _toProperty;public ToProperty getTo() { return _toProperty; }public void setTo(ToProperty value) { if(_toProperty!=null) _toProperty.merge(value); else {_toProperty = value;} }protected void copy(RuleLinkProperty instance) {this._addFlag.addAll(instance._addFlag);
this._code = instance._code;
this._toProperty = instance._toProperty; 
this._listenProperty = instance._listenProperty; 
}protected void merge(RuleLinkProperty child) {super.merge(child);if(child._addFlag != null)this._addFlag.addAll(child._addFlag);
if(_toProperty == null) _toProperty = child._toProperty; else if (child._toProperty != null) {_toProperty.merge(child._toProperty);}
}}protected ArrayList<RuleLinkProperty> _ruleLinkPropertyList = new ArrayList<RuleLinkProperty>();public ArrayList<RuleLinkProperty> getRuleLinkList() { return _ruleLinkPropertyList; }
	

	public void copy(DesktopDefinitionBase instance) {
		this._code = instance._code;
this._name = instance._name;
this._parent = instance._parent;
this._label = instance._label;
this._description = instance._description;
this._help = instance._help;

		this._containProperty = instance._containProperty; 
this._forProperty = instance._forProperty; 
for(ViewProperty item : instance._viewPropertyMap.values())this.addView(item);
_ruleLinkPropertyList.addAll(instance._ruleLinkPropertyList);
this._isSingleton = instance._isSingleton; 
this._isReadonly = instance._isReadonly; 
this._isPrivate = instance._isPrivate; 
this._requirePartnerContextProperty = instance._requirePartnerContextProperty; 
for(OperationProperty item : instance._operationPropertyMap.values())this.addOperation(item);
_ruleNodePropertyList.addAll(instance._ruleNodePropertyList);
_ruleViewPropertyList.addAll(instance._ruleViewPropertyList);
_ruleOperationPropertyList.addAll(instance._ruleOperationPropertyList);
_displayPropertyList.addAll(instance._displayPropertyList);
this._isAbstract = instance._isAbstract; 

		
	}

	public void merge(DesktopDefinitionBase child) {
		super.merge(child);
		
		
		if(_containProperty == null) _containProperty = child._containProperty; else if (child._containProperty != null) {_containProperty.merge(child._containProperty);}
if(_forProperty == null) _forProperty = child._forProperty; else if (child._forProperty != null) {_forProperty.merge(child._forProperty);}
for(ViewProperty item : child._viewPropertyMap.values())this.addView(item);
_ruleLinkPropertyList.addAll(child._ruleLinkPropertyList);

		
	}

	public Class<?> getMetamodelClass() {
		return DesktopDefinitionBase.class;
	}

}

