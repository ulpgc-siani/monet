package client.core.model.definition;

import static client.core.model.Instance.ClassName;

public interface Definition {

	ClassName CLASS_NAME = new ClassName("Definition");

	String getCode();
	String getName();
	String getLabel();
	String getDescription();
	ClassName getClassName();
}
