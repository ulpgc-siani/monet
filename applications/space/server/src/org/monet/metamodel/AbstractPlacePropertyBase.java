package org.monet.metamodel;

/**
 * AbstractPlaceProperty
 */

public abstract class AbstractPlacePropertyBase extends ReferenceableProperty {

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

	protected TransferenceActionProperty _transferenceActionProperty;
	protected EditionActionProperty _editionActionProperty;
	protected SendResponseActionProperty _sendResponseActionProperty;
	protected SendRequestActionProperty _sendRequestActionProperty;
	protected DelegationActionProperty _delegationActionProperty;
	protected WaitActionProperty _waitActionProperty;
	protected DoorActionProperty _doorActionProperty;
	protected LineActionProperty _lineActionProperty;

	public TransferenceActionProperty getTransferenceActionProperty() {
		return _transferenceActionProperty;
	}

	public void setTransferenceActionProperty(TransferenceActionProperty value) {
		if (_transferenceActionProperty == null)
			_transferenceActionProperty = value;
		else {
			_transferenceActionProperty.merge(value);
		}
	}

	public EditionActionProperty getEditionActionProperty() {
		return _editionActionProperty;
	}

	public void setEditionActionProperty(EditionActionProperty value) {
		if (_editionActionProperty == null)
			_editionActionProperty = value;
		else {
			_editionActionProperty.merge(value);
		}
	}

	public SendResponseActionProperty getSendResponseActionProperty() {
		return _sendResponseActionProperty;
	}

	public void setSendResponseActionProperty(SendResponseActionProperty value) {
		if (_sendResponseActionProperty == null)
			_sendResponseActionProperty = value;
		else {
			_sendResponseActionProperty.merge(value);
		}
	}

	public SendRequestActionProperty getSendRequestActionProperty() {
		return _sendRequestActionProperty;
	}

	public void setSendRequestActionProperty(SendRequestActionProperty value) {
		if (_sendRequestActionProperty == null)
			_sendRequestActionProperty = value;
		else {
			_sendRequestActionProperty.merge(value);
		}
	}

	public DelegationActionProperty getDelegationActionProperty() {
		return _delegationActionProperty;
	}

	public void setDelegationActionProperty(DelegationActionProperty value) {
		if (_delegationActionProperty == null)
			_delegationActionProperty = value;
		else {
			_delegationActionProperty.merge(value);
		}
	}

	public WaitActionProperty getWaitActionProperty() {
		return _waitActionProperty;
	}

	public void setWaitActionProperty(WaitActionProperty value) {
		if (_waitActionProperty == null)
			_waitActionProperty = value;
		else {
			_waitActionProperty.merge(value);
		}
	}

	public DoorActionProperty getDoorActionProperty() {
		return _doorActionProperty;
	}

	public void setDoorActionProperty(DoorActionProperty value) {
		if (_doorActionProperty == null)
			_doorActionProperty = value;
		else {
			_doorActionProperty.merge(value);
		}
	}

	public LineActionProperty getLineActionProperty() {
		return _lineActionProperty;
	}

	public void setLineActionProperty(LineActionProperty value) {
		if (_lineActionProperty == null)
			_lineActionProperty = value;
		else {
			_lineActionProperty.merge(value);
		}
	}

	public void merge(AbstractPlacePropertyBase child) {
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

		this.setTransferenceActionProperty(child._transferenceActionProperty);
		this.setEditionActionProperty(child._editionActionProperty);
		this.setSendResponseActionProperty(child._sendResponseActionProperty);
		this.setSendRequestActionProperty(child._sendRequestActionProperty);
		this.setDelegationActionProperty(child._delegationActionProperty);
		this.setWaitActionProperty(child._waitActionProperty);
		this.setDoorActionProperty(child._doorActionProperty);
		this.setLineActionProperty(child._lineActionProperty);
	}

	public Class<?> getMetamodelClass() {
		return AbstractPlacePropertyBase.class;
	}

}
