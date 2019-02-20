package org.monet.space.explorer.control.dialogs;

import org.monet.space.explorer.control.dialogs.constants.Operation;
import org.monet.space.explorer.control.dialogs.constants.Parameter;

public class HomeDialog extends HttpDialog {

	public boolean isLoginOperation() {
		String operation = getOperation();
		return operation!=null && operation.equals(Operation.LOGIN);
	}

	public String getVerifier() {
		return (String)get(Parameter.OAUTH_VERIFIER);
	}

}
