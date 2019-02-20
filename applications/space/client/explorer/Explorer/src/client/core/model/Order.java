package client.core.model;

import static client.core.model.Instance.*;

public interface Order {

	ClassName CLASS_NAME = new ClassName("Order");

	enum Mode {
		ASC, DESC;

		public static Mode fromString(String mode) {
			return Mode.valueOf(mode.toUpperCase());
		}
	}

	String getName();
	String getLabel();
	Mode getMode();
	void setMode(Mode mode);
}
