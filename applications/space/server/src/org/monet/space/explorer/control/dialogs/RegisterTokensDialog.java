package org.monet.space.explorer.control.dialogs;

import org.monet.space.explorer.control.dialogs.constants.Parameter;

public class RegisterTokensDialog extends HttpDialog {

	public String getRequestToken() {
		return (String)get(Parameter.OAUTH_REQUEST_TOKEN);
	}

	public String getAccessToken() {
		return (String)get(Parameter.OAUTH_ACCESS_TOKEN);
	}

}
