package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
DocumentDefinition
Un documento es un tipo nodo que se utiliza permitir la interoperabilidad con otra unidad de negocio

*/

public  class DocumentDefinitionBase extends NodeDefinition {

	protected String _template;public String getTemplate() { return _template; }public void setTemplate(String value) { _template = value; }
	public static class IsComponent  {protected void copy(IsComponent instance) {}protected void merge(IsComponent child) {}}protected IsComponent _isComponent;public boolean isComponent() { return (_isComponent != null); }public IsComponent getIsComponent() { return _isComponent; }public void setIsComponent(boolean value) { if(value) _isComponent = new IsComponent(); else {_isComponent = null;}}public static class IsShared  {protected void copy(IsShared instance) {}protected void merge(IsShared child) {}}protected IsShared _isShared;public boolean isShared() { return (_isShared != null); }public IsShared getIsShared() { return _isShared; }public void setIsShared(boolean value) { if(value) _isShared = new IsShared(); else {_isShared = null;}}public static class SignaturesProperty  {public enum PositionEnumeration { BEGINNING,NEW_PAGE_AT_BEGINNING,END,NEW_PAGE_AT_END }protected PositionEnumeration _position;public PositionEnumeration getPosition() { return _position; }public void setPosition(PositionEnumeration value) { _position = value; }public static class SignatureProperty extends org.monet.metamodel.ReferenceableProperty {protected Object _label;public Object getLabel() { return _label; }public void setLabel(Object value) { _label = value; }protected ArrayList<org.monet.metamodel.internal.Ref> _for = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getFor() { return _for; }public void setFor(ArrayList<org.monet.metamodel.internal.Ref> value) { _for = value; }protected org.monet.metamodel.internal.Ref _after;public org.monet.metamodel.internal.Ref getAfter() { return _after; }public void setAfter(org.monet.metamodel.internal.Ref value) { _after = value; }protected Long _amount;public Long getAmount() { return _amount; }public void setAmount(Long value) { _amount = value; }protected void copy(SignatureProperty instance) {this._label = instance._label;
this._for.addAll(instance._for);
this._after = instance._after;
this._amount = instance._amount;
this._code = instance._code;
this._name = instance._name;
}protected void merge(SignatureProperty child) {super.merge(child);if(child._label != null)this._label = child._label;
if(child._for != null)this._for.addAll(child._for);
if(child._after != null)this._after = child._after;
if(child._amount != null)this._amount = child._amount;
}}protected LinkedHashMap<String, SignatureProperty> _signaturePropertyMap = new LinkedHashMap<String, SignatureProperty>();public void addSignature(SignatureProperty value) {String key = value.getName() != null ? value.getName() : value.getCode();SignatureProperty current = _signaturePropertyMap.get(key);if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {SignatureProperty instance = value.getClass().newInstance();instance.copy(current);instance.setCode(value.getCode());instance.setName(value.getName());instance.merge(value);_signaturePropertyMap.put(key, instance);}catch (Exception exception) {}}else current.merge(value);} else {_signaturePropertyMap.put(key, value);} }public java.util.Map<String,SignatureProperty> getSignatureMap() { return _signaturePropertyMap; }public java.util.Collection<SignatureProperty> getSignatureList() { return _signaturePropertyMap.values(); }protected void copy(SignaturesProperty instance) {this._position = instance._position;
for(SignatureProperty item : instance._signaturePropertyMap.values())this.addSignature(item);
}protected void merge(SignaturesProperty child) {if(child._position != null)this._position = child._position;
for(SignatureProperty item : child._signaturePropertyMap.values())this.addSignature(item);
}}protected SignaturesProperty _signaturesProperty;public SignaturesProperty getSignatures() { return _signaturesProperty; }public void setSignatures(SignaturesProperty value) { if(_signaturesProperty!=null) _signaturesProperty.merge(value); else {_signaturesProperty = value;} }public static class PropertiesProperty  {protected void copy(PropertiesProperty instance) {}protected void merge(PropertiesProperty child) {}}protected PropertiesProperty _propertiesProperty;public PropertiesProperty getProperties() { return _propertiesProperty; }public void setProperties(PropertiesProperty value) { if(_propertiesProperty!=null) _propertiesProperty.merge(value); else {_propertiesProperty = value;} }public static class MappingProperty  {protected org.monet.metamodel.internal.Ref _index;public org.monet.metamodel.internal.Ref getIndex() { return _index; }public void setIndex(org.monet.metamodel.internal.Ref value) { _index = value; }protected void copy(MappingProperty instance) {this._index = instance._index;
}protected void merge(MappingProperty child) {if(child._index != null)this._index = child._index;
}}protected ArrayList<MappingProperty> _mappingPropertyList = new ArrayList<MappingProperty>();public ArrayList<MappingProperty> getMappingList() { return _mappingPropertyList; }
	

	public void copy(DocumentDefinitionBase instance) {
		this._template = instance._template;
this._code = instance._code;
this._name = instance._name;
this._parent = instance._parent;
this._label = instance._label;
this._description = instance._description;
this._help = instance._help;

		this._isComponent = instance._isComponent; 
this._isShared = instance._isShared; 
this._signaturesProperty = instance._signaturesProperty; 
this._propertiesProperty = instance._propertiesProperty; 
_mappingPropertyList.addAll(instance._mappingPropertyList);
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

	public void merge(DocumentDefinitionBase child) {
		super.merge(child);
		
		if(child._template != null)this._template = child._template;

		if(_isComponent == null) _isComponent = child._isComponent; else if (child._isComponent != null) {_isComponent.merge(child._isComponent);}
if(_isShared == null) _isShared = child._isShared; else if (child._isShared != null) {_isShared.merge(child._isShared);}
if(_signaturesProperty == null) _signaturesProperty = child._signaturesProperty; else if (child._signaturesProperty != null) {_signaturesProperty.merge(child._signaturesProperty);}
if(_propertiesProperty == null) _propertiesProperty = child._propertiesProperty; else if (child._propertiesProperty != null) {_propertiesProperty.merge(child._propertiesProperty);}
_mappingPropertyList.addAll(child._mappingPropertyList);

		
	}

	public Class<?> getMetamodelClass() {
		return DocumentDefinitionBase.class;
	}

}

