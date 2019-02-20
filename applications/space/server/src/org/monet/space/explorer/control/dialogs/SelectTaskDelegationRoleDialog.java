package org.monet.space.explorer.control.dialogs;

import org.monet.space.explorer.control.dialogs.constants.Parameter;

public class SelectTaskDelegationRoleDialog extends TaskDialog {

	public String getRoleId() {
		return getString(Parameter.ROLE);
	}

}
