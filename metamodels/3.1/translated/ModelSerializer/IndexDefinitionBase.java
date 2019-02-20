package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
IndexDefinition
Un índice es una lista de enlaces a nodos con atributos descriptivos
Se utiliza para mostrar un conjunto (colección o catálogo)
*/

public  class IndexDefinitionBase extends EntityDefinition {

	
	public static class ReferenceProperty  {protected LinkedHashMap<String, AttributeProperty> _attributePropertyMap = new LinkedHashMap<String, AttributeProperty>();public void addAttributeProperty(AttributeProperty value) {String key = value.getName() != null ? value.getName() : value.getCode();AttributeProperty current = _attributePropertyMap.get(key);if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {AttributeProperty instance = value.getClass().newInstance();instance.copy(current);instance.setCode(value.getCode());instance.setName(value.getName());instance.merge(value);_attributePropertyMap.put(key, instance);}catch (Exception exception) {}}else current.merge(value);} else {_attributePropertyMap.put(key, value);} }public java.util.Map<String,AttributeProperty> getAttributePropertyMap() { return _attributePropertyMap; }public java.util.Collection<AttributeProperty> getAttributePropertyList() { return _attributePropertyMap.values(); }protected void copy(ReferenceProperty instance) {for(AttributeProperty item : instance._attributePropertyMap.values())this.addAttributeProperty(item);}protected void merge(ReferenceProperty child) {for(AttributeProperty item : child._attributePropertyMap.values())this.addAttributeProperty(item);}}protected ReferenceProperty _referenceProperty;public ReferenceProperty getReference() { return _referenceProperty; }public void setReference(ReferenceProperty value) { if(_referenceProperty!=null) _referenceProperty.merge(value); else {_referenceProperty = value;} }public static class IndexViewProperty extends org.monet.metamodel.ViewProperty {public static class IsDefault  {protected void copy(IsDefault instance) {}protected void merge(IsDefault child) {}}protected IsDefault _isDefault;public boolean isDefault() { return (_isDefault != null); }public IsDefault getIsDefault() { return _isDefault; }public void setIsDefault(boolean value) { if(value) _isDefault = new IsDefault(); else {_isDefault = null;}}public static class ShowProperty  {protected org.monet.metamodel.internal.Ref _title;public org.monet.metamodel.internal.Ref getTitle() { return _title; }public void setTitle(org.monet.metamodel.internal.Ref value) { _title = value; }protected ArrayList<org.monet.metamodel.internal.Ref> _line = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getLine() { return _line; }public void setLine(ArrayList<org.monet.metamodel.internal.Ref> value) { _line = value; }protected ArrayList<org.monet.metamodel.internal.Ref> _lineBelow = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getLineBelow() { return _lineBelow; }public void setLineBelow(ArrayList<org.monet.metamodel.internal.Ref> value) { _lineBelow = value; }protected ArrayList<org.monet.metamodel.internal.Ref> _highlight = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getHighlight() { return _highlight; }public void setHighlight(ArrayList<org.monet.metamodel.internal.Ref> value) { _highlight = value; }protected ArrayList<org.monet.metamodel.internal.Ref> _footer = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getFooter() { return _footer; }public void setFooter(ArrayList<org.monet.metamodel.internal.Ref> value) { _footer = value; }protected org.monet.metamodel.internal.Ref _icon;public org.monet.metamodel.internal.Ref getIcon() { return _icon; }public void setIcon(org.monet.metamodel.internal.Ref value) { _icon = value; }protected org.monet.metamodel.internal.Ref _picture;public org.monet.metamodel.internal.Ref getPicture() { return _picture; }public void setPicture(org.monet.metamodel.internal.Ref value) { _picture = value; }protected void copy(ShowProperty instance) {this._title = instance._title;
this._line.addAll(instance._line);
this._lineBelow.addAll(instance._lineBelow);
this._highlight.addAll(instance._highlight);
this._footer.addAll(instance._footer);
this._icon = instance._icon;
this._picture = instance._picture;
}protected void merge(ShowProperty child) {if(child._title != null)this._title = child._title;
if(child._line != null)this._line.addAll(child._line);
if(child._lineBelow != null)this._lineBelow.addAll(child._lineBelow);
if(child._highlight != null)this._highlight.addAll(child._highlight);
if(child._footer != null)this._footer.addAll(child._footer);
if(child._icon != null)this._icon = child._icon;
if(child._picture != null)this._picture = child._picture;
}}protected ShowProperty _showProperty;public ShowProperty getShow() { return _showProperty; }public void setShow(ShowProperty value) { if(_showProperty!=null) _showProperty.merge(value); else {_showProperty = value;} }protected void copy(IndexViewProperty instance) {this._code = instance._code;
this._name = instance._name;
this._isDefault = instance._isDefault; 
this._showProperty = instance._showProperty; 
}protected void merge(IndexViewProperty child) {super.merge(child);if(_isDefault == null) _isDefault = child._isDefault; else if (child._isDefault != null) {_isDefault.merge(child._isDefault);}
if(_showProperty == null) _showProperty = child._showProperty; else if (child._showProperty != null) {_showProperty.merge(child._showProperty);}
}}protected LinkedHashMap<String, IndexViewProperty> _indexViewPropertyMap = new LinkedHashMap<String, IndexViewProperty>();public void addView(IndexViewProperty value) {String key = value.getName() != null ? value.getName() : value.getCode();IndexViewProperty current = _indexViewPropertyMap.get(key);if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {IndexViewProperty instance = value.getClass().newInstance();instance.copy(current);instance.setCode(value.getCode());instance.setName(value.getName());instance.merge(value);_indexViewPropertyMap.put(key, instance);}catch (Exception exception) {}}else current.merge(value);} else {_indexViewPropertyMap.put(key, value);} }public java.util.Map<String,IndexViewProperty> getViewMap() { return _indexViewPropertyMap; }public java.util.Collection<IndexViewProperty> getViewList() { return _indexViewPropertyMap.values(); }
	

	public void copy(IndexDefinitionBase instance) {
		this._code = instance._code;
this._name = instance._name;
this._parent = instance._parent;
this._label = instance._label;
this._description = instance._description;
this._help = instance._help;

		this._referenceProperty = instance._referenceProperty; 
for(IndexViewProperty item : instance._indexViewPropertyMap.values())this.addView(item);
this._isAbstract = instance._isAbstract; 

		
	}

	public void merge(IndexDefinitionBase child) {
		super.merge(child);
		
		
		if(_referenceProperty == null) _referenceProperty = child._referenceProperty; else if (child._referenceProperty != null) {_referenceProperty.merge(child._referenceProperty);}
for(IndexViewProperty item : child._indexViewPropertyMap.values())this.addView(item);

		
	}

	public Class<?> getMetamodelClass() {
		return IndexDefinitionBase.class;
	}

}

