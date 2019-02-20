package org.monet.metamodel;

import java.util.LinkedHashMap;

/**
 * DimensionProperty
 */

public abstract class DimensionProperty extends ReferenceableProperty {

	protected Object _label;

	public Object getLabel() {
		return _label;
	}

	public void setLabel(Object value) {
		_label = value;
	}

	public static class FeaturesProperty {
		protected LinkedHashMap<String, AttributeProperty> _attributePropertyMap = new LinkedHashMap<String, AttributeProperty>();

		public void addAttributeProperty(AttributeProperty value) {
			String key = value.getName() != null ? value.getName() : value.getCode();
			AttributeProperty current = _attributePropertyMap.get(key);
			if (current != null) {
				if (current.getClass().isAssignableFrom(value.getClass())) {
					try {
						AttributeProperty instance = value.getClass().newInstance();
						instance.copy(current);
						instance.setCode(value.getCode());
						instance.setName(value.getName());
						instance.merge(value);
						_attributePropertyMap.put(key, instance);
					} catch (Exception exception) {
					}
				} else
					current.merge(value);
			} else {
				_attributePropertyMap.put(key, value);
			}
		}

		public java.util.Map<String, AttributeProperty> getAttributePropertyMap() {
			return _attributePropertyMap;
		}

		public java.util.Collection<AttributeProperty> getAttributePropertyList() {
			return _attributePropertyMap.values();
		}

		protected void copy(FeaturesProperty instance) {
			for (AttributeProperty item : instance._attributePropertyMap.values())
				this.addAttributeProperty(item);
		}

		protected void merge(FeaturesProperty child) {
			for (AttributeProperty item : child._attributePropertyMap.values())
				this.addAttributeProperty(item);
		}
	}

	protected FeaturesProperty _featuresProperty;

	public FeaturesProperty getFeatures() {
		return _featuresProperty;
	}

	public void setFeatures(FeaturesProperty value) {
		if (_featuresProperty != null)
			_featuresProperty.merge(value);
		else {
			_featuresProperty = value;
		}
	}

	public void copy(DimensionProperty instance) {
		this._label = instance._label;
		this._code = instance._code;
		this._name = instance._name;

		this._featuresProperty = instance._featuresProperty;

	}

	public void merge(DimensionProperty child) {
		super.merge(child);

		if (child._label != null)
			this._label = child._label;

		if (_featuresProperty == null)
			_featuresProperty = child._featuresProperty;
		else if (child._featuresProperty != null) {
			_featuresProperty.merge(child._featuresProperty);
		}

	}

	public Class<?> getMetamodelClass() {
		return DimensionProperty.class;
	}

}
