package org.monet.metamodel;

/**
 * MetricProperty Esta propiedad se utiliza para establecer la métrica de un
 * campo de tipo numérico o sumatoria
 */

public class MetricProperty extends ReferenceableProperty {

	protected Object _label;

	public Object getLabel() {
		return _label;
	}

	public void setLabel(Object value) {
		_label = value;
	}

	public static class IsDefault {
		protected void merge(IsDefault child) {
		}
	}

	protected IsDefault _isDefault;

	public boolean isDefault() {
		return (_isDefault != null);
	}

	public IsDefault getIsDefault() {
		return _isDefault;
	}

	public void setIsDefault(boolean value) {
		if (value)
			_isDefault = new IsDefault();
		else {
			_isDefault = null;
		}
	}

	public static class EquivalenceProperty {
		protected Long _value;

		public Long getValue() {
			return _value;
		}

		public void setValue(Long value) {
			_value = value;
		}

		protected void merge(EquivalenceProperty child) {
			if (child._value != null)
				this._value = child._value;
		}
	}

	protected EquivalenceProperty _equivalenceProperty;

	public EquivalenceProperty getEquivalence() {
		return _equivalenceProperty;
	}

	public void setEquivalence(EquivalenceProperty value) {
		if (_equivalenceProperty != null)
			_equivalenceProperty.merge(value);
		else {
			_equivalenceProperty = value;
		}
	}

	public void merge(MetricProperty child) {
		super.merge(child);

		if (child._label != null)
			this._label = child._label;

		if (_isDefault == null)
			_isDefault = child._isDefault;
		else {
			_isDefault.merge(child._isDefault);
		}
		if (_equivalenceProperty == null)
			_equivalenceProperty = child._equivalenceProperty;
		else {
			_equivalenceProperty.merge(child._equivalenceProperty);
		}

	}

	public Class<?> getMetamodelClass() {
		return MetricProperty.class;
	}

}
