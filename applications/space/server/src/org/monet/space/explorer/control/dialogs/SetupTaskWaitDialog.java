package org.monet.space.explorer.control.dialogs;

import org.monet.space.explorer.control.dialogs.constants.Parameter;

public class SetupTaskWaitDialog extends TaskDialog {

	public enum Scale {
		HOUR, DAY, MONTH, YEAR;

		public static Scale fromString(String value) {
			return Scale.valueOf(value.toUpperCase());
		}
	}

	public Scale getScale() {
		return Scale.fromString(getString(Parameter.SCALE));
	}

	public int getValue() {
		return getInt(Parameter.VALUE);
	}

}
