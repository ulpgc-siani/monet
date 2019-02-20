package client.core.model.types;

import client.core.model.Instance;

public interface Check {
	Instance.ClassName CLASS_NAME = new Instance.ClassName("Check");

	String getValue();
	void setValue(String value);
	String getLabel();
	void setLabel(String value);
	boolean isChecked();
	void setChecked(boolean checked);

	boolean isLeaf();
	boolean equals(Object check);
}
