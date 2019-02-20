package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
ThesaurusDefinition
Un tesauro es un conjunto de términos y representa el vocabulario de la unidad de negocio
Se utilizan para clasificar la información
*/

public  class ThesaurusDefinitionBase extends SourceDefinition {

	
	public static class TermsProperty  {protected ArrayList<TermProperty> _termPropertyList = new ArrayList<TermProperty>();public void addTermProperty(TermProperty value) { _termPropertyList.add(value); }public ArrayList<TermProperty> getTermPropertyList() { return _termPropertyList; }protected void copy(TermsProperty instance) {_termPropertyList.addAll(instance._termPropertyList);}protected void merge(TermsProperty child) {_termPropertyList.addAll(child._termPropertyList);}}protected TermsProperty _termsProperty;public TermsProperty getTerms() { return _termsProperty; }public void setTerms(TermsProperty value) { if(_termsProperty!=null) _termsProperty.merge(value); else {_termsProperty = value;} }public static class IsSelfGenerated  {protected void copy(IsSelfGenerated instance) {}protected void merge(IsSelfGenerated child) {}}protected IsSelfGenerated _isSelfGenerated;public boolean isSelfGenerated() { return (_isSelfGenerated != null); }public IsSelfGenerated getIsSelfGenerated() { return _isSelfGenerated; }public void setIsSelfGenerated(boolean value) { if(value) _isSelfGenerated = new IsSelfGenerated(); else {_isSelfGenerated = null;}}public static class SelfGeneratedProperty  {protected org.monet.metamodel.internal.Ref _index;public org.monet.metamodel.internal.Ref getIndex() { return _index; }public void setIndex(org.monet.metamodel.internal.Ref value) { _index = value; }public static class MappingProperty  {protected org.monet.metamodel.internal.Ref _key;public org.monet.metamodel.internal.Ref getKey() { return _key; }public void setKey(org.monet.metamodel.internal.Ref value) { _key = value; }protected org.monet.metamodel.internal.Ref _label;public org.monet.metamodel.internal.Ref getLabel() { return _label; }public void setLabel(org.monet.metamodel.internal.Ref value) { _label = value; }protected org.monet.metamodel.internal.Ref _tag;public org.monet.metamodel.internal.Ref getTag() { return _tag; }public void setTag(org.monet.metamodel.internal.Ref value) { _tag = value; }protected org.monet.metamodel.internal.Ref _parent;public org.monet.metamodel.internal.Ref getParent() { return _parent; }public void setParent(org.monet.metamodel.internal.Ref value) { _parent = value; }protected void copy(MappingProperty instance) {this._key = instance._key;
this._label = instance._label;
this._tag = instance._tag;
this._parent = instance._parent;
}protected void merge(MappingProperty child) {if(child._key != null)this._key = child._key;
if(child._label != null)this._label = child._label;
if(child._tag != null)this._tag = child._tag;
if(child._parent != null)this._parent = child._parent;
}}protected MappingProperty _mappingProperty;public MappingProperty getMapping() { return _mappingProperty; }public void setMapping(MappingProperty value) { if(_mappingProperty!=null) _mappingProperty.merge(value); else {_mappingProperty = value;} }protected void copy(SelfGeneratedProperty instance) {this._index = instance._index;
this._mappingProperty = instance._mappingProperty; 
}protected void merge(SelfGeneratedProperty child) {if(child._index != null)this._index = child._index;
if(_mappingProperty == null) _mappingProperty = child._mappingProperty; else if (child._mappingProperty != null) {_mappingProperty.merge(child._mappingProperty);}
}}protected SelfGeneratedProperty _selfGeneratedProperty;public SelfGeneratedProperty getSelfGenerated() { return _selfGeneratedProperty; }public void setSelfGenerated(SelfGeneratedProperty value) { if(_selfGeneratedProperty!=null) _selfGeneratedProperty.merge(value); else {_selfGeneratedProperty = value;} }
	

	public void copy(ThesaurusDefinitionBase instance) {
		this._ontology = instance._ontology;
this._code = instance._code;
this._name = instance._name;
this._parent = instance._parent;
this._label = instance._label;
this._description = instance._description;
this._help = instance._help;

		this._termsProperty = instance._termsProperty; 
this._isSelfGenerated = instance._isSelfGenerated; 
this._selfGeneratedProperty = instance._selfGeneratedProperty; 
this._isAbstract = instance._isAbstract; 

		
	}

	public void merge(ThesaurusDefinitionBase child) {
		super.merge(child);
		
		
		if(_termsProperty == null) _termsProperty = child._termsProperty; else if (child._termsProperty != null) {_termsProperty.merge(child._termsProperty);}
if(_isSelfGenerated == null) _isSelfGenerated = child._isSelfGenerated; else if (child._isSelfGenerated != null) {_isSelfGenerated.merge(child._isSelfGenerated);}
if(_selfGeneratedProperty == null) _selfGeneratedProperty = child._selfGeneratedProperty; else if (child._selfGeneratedProperty != null) {_selfGeneratedProperty.merge(child._selfGeneratedProperty);}

		
	}

	public Class<?> getMetamodelClass() {
		return ThesaurusDefinitionBase.class;
	}

}

