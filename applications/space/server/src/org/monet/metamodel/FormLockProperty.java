package org.monet.metamodel;

/**
 * FormLockProperty
 */

public class FormLockProperty extends SimpleActionProperty {

	public static class UseProperty {
		protected org.monet.metamodel.internal.Ref _form;

		public org.monet.metamodel.internal.Ref getForm() {
			return _form;
		}

		public void setForm(org.monet.metamodel.internal.Ref value) {
			_form = value;
		}

		protected org.monet.metamodel.internal.Ref _withView;

		public org.monet.metamodel.internal.Ref getWithView() {
			return _withView;
		}

		public void setWithView(org.monet.metamodel.internal.Ref value) {
			_withView = value;
		}

		protected void merge(UseProperty child) {
			if (child._form != null)
				this._form = child._form;
			if (child._withView != null)
				this._withView = child._withView;
		}
	}

	protected UseProperty _useProperty;

	public UseProperty getUse() {
		return _useProperty;
	}

	public void setUse(UseProperty value) {
		if (_useProperty != null)
			_useProperty.merge(value);
		else {
			_useProperty = value;
		}
	}

	public void merge(FormLockProperty child) {
		super.merge(child);

		if (_useProperty == null)
			_useProperty = child._useProperty;
		else {
			_useProperty.merge(child._useProperty);
		}

	}

	public Class<?> getMetamodelClass() {
		return FormLockProperty.class;
	}

}
