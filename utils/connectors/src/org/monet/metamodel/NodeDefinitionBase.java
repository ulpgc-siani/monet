package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
NodeDefinition
Un nodo es una entidad que contiene información
		relacionada con el espacio de negocio
	

*/

public abstract class NodeDefinitionBase extends EntityDefinition {

	
	public static class IsSingleton  {protected void copy(IsSingleton instance) {}protected void merge(IsSingleton child) {}}protected IsSingleton _isSingleton;public boolean isSingleton() { return (_isSingleton != null); }public IsSingleton getIsSingleton() { return _isSingleton; }public void setIsSingleton(boolean value) { if(value) _isSingleton = new IsSingleton(); else {_isSingleton = null;}}public static class IsReadonly  {protected void copy(IsReadonly instance) {}protected void merge(IsReadonly child) {}}protected IsReadonly _isReadonly;public boolean isReadonly() { return (_isReadonly != null); }public IsReadonly getIsReadonly() { return _isReadonly; }public void setIsReadonly(boolean value) { if(value) _isReadonly = new IsReadonly(); else {_isReadonly = null;}}public static class IsPrivate  {protected void copy(IsPrivate instance) {}protected void merge(IsPrivate child) {}}protected IsPrivate _isPrivate;public boolean isPrivate() { return (_isPrivate != null); }public IsPrivate getIsPrivate() { return _isPrivate; }public void setIsPrivate(boolean value) { if(value) _isPrivate = new IsPrivate(); else {_isPrivate = null;}}public static class RequirePartnerContextProperty  {protected void copy(RequirePartnerContextProperty instance) {}protected void merge(RequirePartnerContextProperty child) {}}protected RequirePartnerContextProperty _requirePartnerContextProperty;public RequirePartnerContextProperty getRequirePartnerContext() { return _requirePartnerContextProperty; }public void setRequirePartnerContext(RequirePartnerContextProperty value) { if(_requirePartnerContextProperty!=null) _requirePartnerContextProperty.merge(value); else {_requirePartnerContextProperty = value;} }public static class IsBreadcrumbsDisabled  {protected void copy(IsBreadcrumbsDisabled instance) {}protected void merge(IsBreadcrumbsDisabled child) {}}protected IsBreadcrumbsDisabled _isBreadcrumbsDisabled;public boolean isBreadcrumbsDisabled() { return (_isBreadcrumbsDisabled != null); }public IsBreadcrumbsDisabled getIsBreadcrumbsDisabled() { return _isBreadcrumbsDisabled; }public void setIsBreadcrumbsDisabled(boolean value) { if(value) _isBreadcrumbsDisabled = new IsBreadcrumbsDisabled(); else {_isBreadcrumbsDisabled = null;}}public static class OperationProperty extends org.monet.metamodel.ReferenceableProperty {protected Object _label;public Object getLabel() { return _label; }public void setLabel(Object value) { _label = value; }protected Object _description;public Object getDescription() { return _description; }public void setDescription(Object value) { _description = value; }public static class ForProperty  {protected ArrayList<org.monet.metamodel.internal.Ref> _role = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getRole() { return _role; }public void setRole(ArrayList<org.monet.metamodel.internal.Ref> value) { _role = value; }protected void copy(ForProperty instance) {this._role.addAll(instance._role);
}protected void merge(ForProperty child) {if(child._role != null)this._role.addAll(child._role);
}}protected ForProperty _forProperty;public ForProperty getFor() { return _forProperty; }public void setFor(ForProperty value) { if(_forProperty!=null) _forProperty.merge(value); else {_forProperty = value;} }protected void copy(OperationProperty instance) {this._label = instance._label;
this._description = instance._description;
this._code = instance._code;
this._name = instance._name;
this._forProperty = instance._forProperty; 
}protected void merge(OperationProperty child) {super.merge(child);if(child._label != null)this._label = child._label;
if(child._description != null)this._description = child._description;
if(_forProperty == null) _forProperty = child._forProperty; else if (child._forProperty != null) {_forProperty.merge(child._forProperty);}
}}protected LinkedHashMap<String, OperationProperty> _operationPropertyMap = new LinkedHashMap<String, OperationProperty>();public void addOperation(OperationProperty value) {String key = value.getName() != null ? value.getName() : value.getCode();OperationProperty current = _operationPropertyMap.get(key);if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {OperationProperty instance = value.getClass().newInstance();instance.copy(current);instance.setCode(value.getCode());instance.setName(value.getName());instance.merge(value);_operationPropertyMap.put(key, instance);}catch (Exception exception) {}}else current.merge(value);} else {_operationPropertyMap.put(key, value);} }public java.util.Map<String,OperationProperty> getOperationMap() { return _operationPropertyMap; }public java.util.Collection<OperationProperty> getOperationList() { return _operationPropertyMap.values(); }public static class RuleNodeProperty extends org.monet.metamodel.RuleProperty {public enum AddFlagEnumeration { READ_ONLY,INVALID }protected ArrayList<AddFlagEnumeration> _addFlag = new ArrayList<AddFlagEnumeration>();public ArrayList<AddFlagEnumeration> getAddFlag() { return _addFlag; }public void setAddFlag(ArrayList<AddFlagEnumeration> value) { _addFlag = value; }protected void copy(RuleNodeProperty instance) {this._addFlag.addAll(instance._addFlag);
this._code = instance._code;
this._listenProperty = instance._listenProperty; 
}protected void merge(RuleNodeProperty child) {super.merge(child);if(child._addFlag != null)this._addFlag.addAll(child._addFlag);
}}protected ArrayList<RuleNodeProperty> _ruleNodePropertyList = new ArrayList<RuleNodeProperty>();public ArrayList<RuleNodeProperty> getRuleNodeList() { return _ruleNodePropertyList; }public static class RuleViewProperty extends org.monet.metamodel.RuleProperty {public enum AddFlagEnumeration { HIDDEN }protected ArrayList<AddFlagEnumeration> _addFlag = new ArrayList<AddFlagEnumeration>();public ArrayList<AddFlagEnumeration> getAddFlag() { return _addFlag; }public void setAddFlag(ArrayList<AddFlagEnumeration> value) { _addFlag = value; }public static class ToProperty  {protected ArrayList<org.monet.metamodel.internal.Ref> _view = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getView() { return _view; }public void setView(ArrayList<org.monet.metamodel.internal.Ref> value) { _view = value; }protected void copy(ToProperty instance) {this._view.addAll(instance._view);
}protected void merge(ToProperty child) {if(child._view != null)this._view.addAll(child._view);
}}protected ToProperty _toProperty;public ToProperty getTo() { return _toProperty; }public void setTo(ToProperty value) { if(_toProperty!=null) _toProperty.merge(value); else {_toProperty = value;} }protected void copy(RuleViewProperty instance) {this._addFlag.addAll(instance._addFlag);
this._code = instance._code;
this._toProperty = instance._toProperty; 
this._listenProperty = instance._listenProperty; 
}protected void merge(RuleViewProperty child) {super.merge(child);if(child._addFlag != null)this._addFlag.addAll(child._addFlag);
if(_toProperty == null) _toProperty = child._toProperty; else if (child._toProperty != null) {_toProperty.merge(child._toProperty);}
}}protected ArrayList<RuleViewProperty> _ruleViewPropertyList = new ArrayList<RuleViewProperty>();public ArrayList<RuleViewProperty> getRuleViewList() { return _ruleViewPropertyList; }public static class RuleOperationProperty extends org.monet.metamodel.RuleProperty {public enum AddFlagEnumeration { HIDDEN,DISABLED }protected ArrayList<AddFlagEnumeration> _addFlag = new ArrayList<AddFlagEnumeration>();public ArrayList<AddFlagEnumeration> getAddFlag() { return _addFlag; }public void setAddFlag(ArrayList<AddFlagEnumeration> value) { _addFlag = value; }public static class ToProperty  {protected ArrayList<org.monet.metamodel.internal.Ref> _operation = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getOperation() { return _operation; }public void setOperation(ArrayList<org.monet.metamodel.internal.Ref> value) { _operation = value; }protected void copy(ToProperty instance) {this._operation.addAll(instance._operation);
}protected void merge(ToProperty child) {if(child._operation != null)this._operation.addAll(child._operation);
}}protected ToProperty _toProperty;public ToProperty getTo() { return _toProperty; }public void setTo(ToProperty value) { if(_toProperty!=null) _toProperty.merge(value); else {_toProperty = value;} }protected void copy(RuleOperationProperty instance) {this._addFlag.addAll(instance._addFlag);
this._code = instance._code;
this._toProperty = instance._toProperty; 
this._listenProperty = instance._listenProperty; 
}protected void merge(RuleOperationProperty child) {super.merge(child);if(child._addFlag != null)this._addFlag.addAll(child._addFlag);
if(_toProperty == null) _toProperty = child._toProperty; else if (child._toProperty != null) {_toProperty.merge(child._toProperty);}
}}protected ArrayList<RuleOperationProperty> _ruleOperationPropertyList = new ArrayList<RuleOperationProperty>();public ArrayList<RuleOperationProperty> getRuleOperationList() { return _ruleOperationPropertyList; }public static class DisplayProperty  {protected Object _message;public Object getMessage() { return _message; }public void setMessage(Object value) { _message = value; }public enum WhenEnumeration { NEW,READ_ONLY,INVALID }protected WhenEnumeration _when;public WhenEnumeration getWhen() { return _when; }public void setWhen(WhenEnumeration value) { _when = value; }protected void copy(DisplayProperty instance) {this._message = instance._message;
this._when = instance._when;
}protected void merge(DisplayProperty child) {if(child._message != null)this._message = child._message;
if(child._when != null)this._when = child._when;
}}protected ArrayList<DisplayProperty> _displayPropertyList = new ArrayList<DisplayProperty>();public ArrayList<DisplayProperty> getDisplayList() { return _displayPropertyList; }
	

	public void copy(NodeDefinitionBase instance) {
		this._code = instance._code;
this._name = instance._name;
this._parent = instance._parent;
this._label = instance._label;
this._description = instance._description;
this._help = instance._help;

		this._isSingleton = instance._isSingleton; 
this._isReadonly = instance._isReadonly; 
this._isPrivate = instance._isPrivate; 
this._requirePartnerContextProperty = instance._requirePartnerContextProperty; 
this._isBreadcrumbsDisabled = instance._isBreadcrumbsDisabled; 
for(OperationProperty item : instance._operationPropertyMap.values())this.addOperation(item);
_ruleNodePropertyList.addAll(instance._ruleNodePropertyList);
_ruleViewPropertyList.addAll(instance._ruleViewPropertyList);
_ruleOperationPropertyList.addAll(instance._ruleOperationPropertyList);
_displayPropertyList.addAll(instance._displayPropertyList);
this._isAbstract = instance._isAbstract; 

		
	}

	public void merge(NodeDefinitionBase child) {
		super.merge(child);
		
		
		if(_isSingleton == null) _isSingleton = child._isSingleton; else if (child._isSingleton != null) {_isSingleton.merge(child._isSingleton);}
if(_isReadonly == null) _isReadonly = child._isReadonly; else if (child._isReadonly != null) {_isReadonly.merge(child._isReadonly);}
if(_isPrivate == null) _isPrivate = child._isPrivate; else if (child._isPrivate != null) {_isPrivate.merge(child._isPrivate);}
if(_requirePartnerContextProperty == null) _requirePartnerContextProperty = child._requirePartnerContextProperty; else if (child._requirePartnerContextProperty != null) {_requirePartnerContextProperty.merge(child._requirePartnerContextProperty);}
if(_isBreadcrumbsDisabled == null) _isBreadcrumbsDisabled = child._isBreadcrumbsDisabled; else if (child._isBreadcrumbsDisabled != null) {_isBreadcrumbsDisabled.merge(child._isBreadcrumbsDisabled);}
for(OperationProperty item : child._operationPropertyMap.values())this.addOperation(item);
_ruleNodePropertyList.addAll(child._ruleNodePropertyList);
_ruleViewPropertyList.addAll(child._ruleViewPropertyList);
_ruleOperationPropertyList.addAll(child._ruleOperationPropertyList);
_displayPropertyList.addAll(child._displayPropertyList);

		
	}

	public Class<?> getMetamodelClass() {
		return NodeDefinitionBase.class;
	}

}

