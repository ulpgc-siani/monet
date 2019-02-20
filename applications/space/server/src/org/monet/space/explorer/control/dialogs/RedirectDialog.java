package org.monet.space.explorer.control.dialogs;

import org.monet.space.explorer.control.dialogs.constants.Parameter;

public class RedirectDialog extends HttpDialog {

	public String getPage() {
		if (request.getAttribute(Parameter.PAGE) != null)
			return (String)request.getAttribute(Parameter.PAGE);

		return getString(Parameter.PAGE);
	}

}
