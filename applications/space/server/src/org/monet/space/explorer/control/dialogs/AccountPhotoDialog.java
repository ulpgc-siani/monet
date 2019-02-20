package org.monet.space.explorer.control.dialogs;

import org.monet.space.explorer.control.dialogs.constants.Parameter;

public class AccountPhotoDialog extends HttpDialog {

	public String getId() {
		return getEntityId();
	}

	public String getPhoto() {
		return getString(Parameter.PHOTO);
	}

}
