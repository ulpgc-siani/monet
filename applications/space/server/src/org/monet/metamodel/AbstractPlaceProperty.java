package org.monet.metamodel;

/**
 * AbstractPlaceProperty
 */

public abstract class AbstractPlaceProperty extends ReferenceableProperty {

	public static class IsInitial {
		protected void merge(IsInitial child) {
		}
	}

	protected IsInitial _isInitial;

	public boolean isInitial() {
		return (_isInitial != null);
	}

	public IsInitial getIsInitial() {
		return _isInitial;
	}

	public void setIsInitial(boolean value) {
		if (value)
			_isInitial = new IsInitial();
		else {
			_isInitial = null;
		}
	}

	public static class IsOust {
		protected void merge(IsOust child) {
		}
	}

	protected IsOust _isOust;

	public boolean isOust() {
		return (_isOust != null);
	}

	public IsOust getIsOust() {
		return _isOust;
	}

	public void setIsOust(boolean value) {
		if (value)
			_isOust = new IsOust();
		else {
			_isOust = null;
		}
	}

	public static class IsFinal {
		protected void merge(IsFinal child) {
		}
	}

	protected IsFinal _isFinal;

	public boolean isFinal() {
		return (_isFinal != null);
	}

	public IsFinal getIsFinal() {
		return _isFinal;
	}

	public void setIsFinal(boolean value) {
		if (value)
			_isFinal = new IsFinal();
		else {
			_isFinal = null;
		}
	}

	public void merge(AbstractPlaceProperty child) {
		super.merge(child);

		if (_isInitial == null)
			_isInitial = child._isInitial;
		else {
			_isInitial.merge(child._isInitial);
		}
		if (_isOust == null)
			_isOust = child._isOust;
		else {
			_isOust.merge(child._isOust);
		}
		if (_isFinal == null)
			_isFinal = child._isFinal;
		else {
			_isFinal.merge(child._isFinal);
		}

	}

	public Class<?> getMetamodelClass() {
		return AbstractPlaceProperty.class;
	}

}
