package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
SetDefinition
Un conjunto es un tipo de nodo abstracto que se utiliza para agrupar otros nodos

*/

public abstract class SetDefinitionBase extends NodeDefinition {

	protected org.monet.metamodel.internal.Ref _index;public org.monet.metamodel.internal.Ref getIndex() { return _index; }public void setIndex(org.monet.metamodel.internal.Ref value) { _index = value; }
	public static class SetViewPropertyBase extends NodeViewProperty {public enum DesignEnumeration { LIST,DOCUMENT }protected DesignEnumeration _design;public DesignEnumeration getDesign() { return _design; }public void setDesign(DesignEnumeration value) { _design = value; }public static class ShowProperty  {public static class IndexProperty  {protected org.monet.metamodel.internal.Ref _withView;public org.monet.metamodel.internal.Ref getWithView() { return _withView; }public void setWithView(org.monet.metamodel.internal.Ref value) { _withView = value; }protected org.monet.metamodel.internal.Ref _sortBy;public org.monet.metamodel.internal.Ref getSortBy() { return _sortBy; }public void setSortBy(org.monet.metamodel.internal.Ref value) { _sortBy = value; }public enum SortModeEnumeration { ASC,DESC }protected SortModeEnumeration _sortMode;public SortModeEnumeration getSortMode() { return _sortMode; }public void setSortMode(SortModeEnumeration value) { _sortMode = value; }protected void copy(IndexProperty instance) {this._withView = instance._withView;
this._sortBy = instance._sortBy;
this._sortMode = instance._sortMode;
}protected void merge(IndexProperty child) {if(child._withView != null)this._withView = child._withView;
if(child._sortBy != null)this._sortBy = child._sortBy;
if(child._sortMode != null)this._sortMode = child._sortMode;
}}protected IndexProperty _indexProperty;public IndexProperty getIndex() { return _indexProperty; }public void setIndex(IndexProperty value) { if(_indexProperty!=null) _indexProperty.merge(value); else {_indexProperty = value;} }public static class ItemsProperty  {protected void copy(ItemsProperty instance) {}protected void merge(ItemsProperty child) {}}protected ItemsProperty _itemsProperty;public ItemsProperty getItems() { return _itemsProperty; }public void setItems(ItemsProperty value) { if(_itemsProperty!=null) _itemsProperty.merge(value); else {_itemsProperty = value;} }public static class LocationsProperty  {public enum LayerEnumeration { POINTS,HEAT }protected LayerEnumeration _layer;public LayerEnumeration getLayer() { return _layer; }public void setLayer(LayerEnumeration value) { _layer = value; }protected org.monet.metamodel.internal.Ref _withView;public org.monet.metamodel.internal.Ref getWithView() { return _withView; }public void setWithView(org.monet.metamodel.internal.Ref value) { _withView = value; }protected String _center;public String getCenter() { return _center; }public void setCenter(String value) { _center = value; }protected void copy(LocationsProperty instance) {this._layer = instance._layer;
this._withView = instance._withView;
this._center = instance._center;
}protected void merge(LocationsProperty child) {if(child._layer != null)this._layer = child._layer;
if(child._withView != null)this._withView = child._withView;
if(child._center != null)this._center = child._center;
}}protected LocationsProperty _locationsProperty;public LocationsProperty getLocations() { return _locationsProperty; }public void setLocations(LocationsProperty value) { if(_locationsProperty!=null) _locationsProperty.merge(value); else {_locationsProperty = value;} }public static class ReportProperty  {protected org.monet.metamodel.internal.Ref _view;public org.monet.metamodel.internal.Ref getView() { return _view; }public void setView(org.monet.metamodel.internal.Ref value) { _view = value; }protected void copy(ReportProperty instance) {this._view = instance._view;
}protected void merge(ReportProperty child) {if(child._view != null)this._view = child._view;
}}protected ReportProperty _reportProperty;public ReportProperty getReport() { return _reportProperty; }public void setReport(ReportProperty value) { if(_reportProperty!=null) _reportProperty.merge(value); else {_reportProperty = value;} }public static class OwnedPrototypesProperty  {protected void copy(OwnedPrototypesProperty instance) {}protected void merge(OwnedPrototypesProperty child) {}}protected OwnedPrototypesProperty _ownedPrototypesProperty;public OwnedPrototypesProperty getOwnedPrototypes() { return _ownedPrototypesProperty; }public void setOwnedPrototypes(OwnedPrototypesProperty value) { if(_ownedPrototypesProperty!=null) _ownedPrototypesProperty.merge(value); else {_ownedPrototypesProperty = value;} }public static class SharedPrototypesProperty  {protected void copy(SharedPrototypesProperty instance) {}protected void merge(SharedPrototypesProperty child) {}}protected SharedPrototypesProperty _sharedPrototypesProperty;public SharedPrototypesProperty getSharedPrototypes() { return _sharedPrototypesProperty; }public void setSharedPrototypes(SharedPrototypesProperty value) { if(_sharedPrototypesProperty!=null) _sharedPrototypesProperty.merge(value); else {_sharedPrototypesProperty = value;} }protected void copy(ShowProperty instance) {this._indexProperty = instance._indexProperty; 
this._itemsProperty = instance._itemsProperty; 
this._locationsProperty = instance._locationsProperty; 
this._reportProperty = instance._reportProperty; 
this._ownedPrototypesProperty = instance._ownedPrototypesProperty; 
this._sharedPrototypesProperty = instance._sharedPrototypesProperty; 
}protected void merge(ShowProperty child) {if(_indexProperty == null) _indexProperty = child._indexProperty; else if (child._indexProperty != null) {_indexProperty.merge(child._indexProperty);}
if(_itemsProperty == null) _itemsProperty = child._itemsProperty; else if (child._itemsProperty != null) {_itemsProperty.merge(child._itemsProperty);}
if(_locationsProperty == null) _locationsProperty = child._locationsProperty; else if (child._locationsProperty != null) {_locationsProperty.merge(child._locationsProperty);}
if(_reportProperty == null) _reportProperty = child._reportProperty; else if (child._reportProperty != null) {_reportProperty.merge(child._reportProperty);}
if(_ownedPrototypesProperty == null) _ownedPrototypesProperty = child._ownedPrototypesProperty; else if (child._ownedPrototypesProperty != null) {_ownedPrototypesProperty.merge(child._ownedPrototypesProperty);}
if(_sharedPrototypesProperty == null) _sharedPrototypesProperty = child._sharedPrototypesProperty; else if (child._sharedPrototypesProperty != null) {_sharedPrototypesProperty.merge(child._sharedPrototypesProperty);}
}}protected ShowProperty _showProperty;public ShowProperty getShow() { return _showProperty; }public void setShow(ShowProperty value) { if(_showProperty!=null) _showProperty.merge(value); else {_showProperty = value;} }public static class AnalyzePropertyBase  {public static class SortingProperty  {protected ArrayList<org.monet.metamodel.internal.Ref> _attribute = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getAttribute() { return _attribute; }public void setAttribute(ArrayList<org.monet.metamodel.internal.Ref> value) { _attribute = value; }protected void copy(SortingProperty instance) {this._attribute.addAll(instance._attribute);
}protected void merge(SortingProperty child) {if(child._attribute != null)this._attribute.addAll(child._attribute);
}}protected SortingProperty _sortingProperty;public SortingProperty getSorting() { return _sortingProperty; }public void setSorting(SortingProperty value) { if(_sortingProperty!=null) _sortingProperty.merge(value); else {_sortingProperty = value;} }public static class DimensionProperty  {protected ArrayList<org.monet.metamodel.internal.Ref> _attribute = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getAttribute() { return _attribute; }public void setAttribute(ArrayList<org.monet.metamodel.internal.Ref> value) { _attribute = value; }protected void copy(DimensionProperty instance) {this._attribute.addAll(instance._attribute);
}protected void merge(DimensionProperty child) {if(child._attribute != null)this._attribute.addAll(child._attribute);
}}protected DimensionProperty _dimensionProperty;public DimensionProperty getDimension() { return _dimensionProperty; }public void setDimension(DimensionProperty value) { if(_dimensionProperty!=null) _dimensionProperty.merge(value); else {_dimensionProperty = value;} }protected void copy(AnalyzePropertyBase instance) {this._sortingProperty = instance._sortingProperty; 
this._dimensionProperty = instance._dimensionProperty; 
}protected void merge(AnalyzePropertyBase child) {if(_sortingProperty == null) _sortingProperty = child._sortingProperty; else if (child._sortingProperty != null) {_sortingProperty.merge(child._sortingProperty);}
if(_dimensionProperty == null) _dimensionProperty = child._dimensionProperty; else if (child._dimensionProperty != null) {_dimensionProperty.merge(child._dimensionProperty);}
}}protected SetDefinition.SetViewProperty.AnalyzeProperty _analyzeProperty;public SetDefinition.SetViewProperty.AnalyzeProperty getAnalyze() { return _analyzeProperty; }public void setAnalyze(SetDefinition.SetViewProperty.AnalyzeProperty value) { if(_analyzeProperty!=null) _analyzeProperty.merge(value); else {_analyzeProperty = value;} }public static class SelectProperty  {protected ArrayList<org.monet.metamodel.internal.Ref> _node = new ArrayList<org.monet.metamodel.internal.Ref>();public ArrayList<org.monet.metamodel.internal.Ref> getNode() { return _node; }public void setNode(ArrayList<org.monet.metamodel.internal.Ref> value) { _node = value; }protected void copy(SelectProperty instance) {this._node.addAll(instance._node);
}protected void merge(SelectProperty child) {if(child._node != null)this._node.addAll(child._node);
}}protected SelectProperty _selectProperty;public SelectProperty getSelect() { return _selectProperty; }public void setSelect(SelectProperty value) { if(_selectProperty!=null) _selectProperty.merge(value); else {_selectProperty = value;} }public static class FilterProperty  {protected org.monet.metamodel.internal.Ref _attribute;public org.monet.metamodel.internal.Ref getAttribute() { return _attribute; }public void setAttribute(org.monet.metamodel.internal.Ref value) { _attribute = value; }protected String _value;public String getValue() { return _value; }public void setValue(String value) { _value = value; }protected void copy(FilterProperty instance) {this._attribute = instance._attribute;
this._value = instance._value;
}protected void merge(FilterProperty child) {if(child._attribute != null)this._attribute = child._attribute;
if(child._value != null)this._value = child._value;
}}protected ArrayList<FilterProperty> _filterPropertyList = new ArrayList<FilterProperty>();public ArrayList<FilterProperty> getFilterList() { return _filterPropertyList; }protected void copy(SetViewPropertyBase instance) {this._design = instance._design;
this._label = instance._label;
this._code = instance._code;
this._name = instance._name;
this._showProperty = instance._showProperty; 
this._analyzeProperty = instance._analyzeProperty; 
this._selectProperty = instance._selectProperty; 
_filterPropertyList.addAll(instance._filterPropertyList);
this._isDefault = instance._isDefault; 
this._isVisibleWhenEmbedded = instance._isVisibleWhenEmbedded; 
this._forProperty = instance._forProperty; 
}protected void merge(SetViewPropertyBase child) {super.merge(child);if(child._design != null)this._design = child._design;
if(_showProperty == null) _showProperty = child._showProperty; else if (child._showProperty != null) {_showProperty.merge(child._showProperty);}
if(_analyzeProperty == null) _analyzeProperty = child._analyzeProperty; else if (child._analyzeProperty != null) {_analyzeProperty.merge(child._analyzeProperty);}
if(_selectProperty == null) _selectProperty = child._selectProperty; else if (child._selectProperty != null) {_selectProperty.merge(child._selectProperty);}
_filterPropertyList.addAll(child._filterPropertyList);
}}protected LinkedHashMap<String, SetDefinition.SetViewProperty> _setViewPropertyMap = new LinkedHashMap<String, SetDefinition.SetViewProperty>();public void addView(SetDefinition.SetViewProperty value) {String key = value.getName() != null ? value.getName() : value.getCode();SetDefinition.SetViewProperty current = _setViewPropertyMap.get(key);if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {SetDefinition.SetViewProperty instance = value.getClass().newInstance();instance.copy(current);instance.setCode(value.getCode());instance.setName(value.getName());instance.merge(value);_setViewPropertyMap.put(key, instance);}catch (Exception exception) {}}else current.merge(value);} else {_setViewPropertyMap.put(key, value);} }public Map<String,SetDefinition.SetViewProperty> getViewMap() { return _setViewPropertyMap; }public Collection<SetDefinition.SetViewProperty> getViewList() { return _setViewPropertyMap.values(); }
	

	public void copy(SetDefinitionBase instance) {
		this._index = instance._index;
this._code = instance._code;
this._name = instance._name;
this._parent = instance._parent;
this._label = instance._label;
this._description = instance._description;
this._help = instance._help;

		for(SetDefinition.SetViewProperty item : instance._setViewPropertyMap.values())this.addView(item);
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

	public void merge(SetDefinitionBase child) {
		super.merge(child);
		
		if(child._index != null)this._index = child._index;

		for(SetDefinition.SetViewProperty item : child._setViewPropertyMap.values())this.addView(item);

		
	}

	public Class<?> getMetamodelClass() {
		return SetDefinitionBase.class;
	}

}

