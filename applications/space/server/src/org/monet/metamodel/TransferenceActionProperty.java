package org.monet.metamodel;

/**
 * TransferenceActionProperty
 */

public class TransferenceActionProperty extends SimpleActionProperty {

	protected org.monet.metamodel.internal.Ref _collaborator;

	public org.monet.metamodel.internal.Ref getCollaborator() {
		return _collaborator;
	}

	public void setCollaborator(org.monet.metamodel.internal.Ref value) {
		_collaborator = value;
	}

	public void merge(TransferenceActionProperty child) {
		super.merge(child);

		if (child._collaborator != null)
			this._collaborator = child._collaborator;

	}

	public Class<?> getMetamodelClass() {
		return TransferenceActionProperty.class;
	}

}
