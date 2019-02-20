package org.monet.space.explorer.control.dialogs;

import org.monet.space.explorer.control.dialogs.constants.Parameter;

public class FileDialog extends HttpDialog {

	public String getFilename() {
		return getString(Parameter.FILE);
	}

}
