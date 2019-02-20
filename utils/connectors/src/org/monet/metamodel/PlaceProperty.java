package org.monet.metamodel;

/**
 * PlaceProperty
 */

public abstract class PlaceProperty extends PlacePropertyBase {

	public void init() {
		if (this._doorActionProperty != null)
			this._doorActionProperty.init();
		else if (this._lineActionProperty != null)
			this._lineActionProperty.init();
	}

	public PlaceActionProperty getPlaceActionProperty() {
		if (this._delegationActionProperty != null) {
			return this._delegationActionProperty;
		} else if (this._doorActionProperty != null) {
			return this._doorActionProperty;
		} else if (this._editionActionProperty != null) {
			return this._editionActionProperty;
		} else if (this._lineActionProperty != null) {
			return this._lineActionProperty;
		} else if (this._sendRequestActionProperty != null) {
			return this._sendRequestActionProperty;
		} else if (this._sendResponseActionProperty != null) {
			return this._sendResponseActionProperty;
		} else if (this._enrollActionProperty != null) {
			return this._enrollActionProperty;
		} else if (this._waitActionProperty != null) {
			return this._waitActionProperty;
		}
		return null;
	}

}
