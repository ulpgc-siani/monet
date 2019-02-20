package org.monet.metamodel;

import java.util.ArrayList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collection;

/**
CategoryProperty
Esta propiedad se utiliza para añadir una categoría a una taxonomía

*/

public  class CategoryProperty extends ReferenceableProperty {

	protected Object _label;public Object getLabel() { return _label; }public void setLabel(Object value) { _label = value; }
	
	protected LinkedHashMap<String, CategoryProperty> _categoryPropertyMap = new LinkedHashMap<String, CategoryProperty>();public void addCategoryProperty(CategoryProperty value) {String key = value.getName() != null ? value.getName() : value.getCode();CategoryProperty current = _categoryPropertyMap.get(key);if (current != null) {if (current.getClass().isAssignableFrom(value.getClass())) {try {CategoryProperty instance = value.getClass().newInstance();instance.copy(current);instance.setCode(value.getCode());instance.setName(value.getName());instance.merge(value);_categoryPropertyMap.put(key, instance);}catch (Exception exception) {}}else current.merge(value);} else {_categoryPropertyMap.put(key, value);} }public java.util.Map<String,CategoryProperty> getCategoryPropertyMap() { return _categoryPropertyMap; }public java.util.Collection<CategoryProperty> getCategoryPropertyList() { return _categoryPropertyMap.values(); }

	public void copy(CategoryProperty instance) {
		this._label = instance._label;
this._code = instance._code;
this._name = instance._name;

		
		for(CategoryProperty item : instance._categoryPropertyMap.values())this.addCategoryProperty(item);
	}

	public void merge(CategoryProperty child) {
		super.merge(child);
		
		if(child._label != null)this._label = child._label;

		
		for(CategoryProperty item : child._categoryPropertyMap.values())this.addCategoryProperty(item);
	}

	public Class<?> getMetamodelClass() {
		return CategoryProperty.class;
	}

}

