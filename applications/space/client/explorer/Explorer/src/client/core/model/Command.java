package client.core.model;

import client.core.model.definition.entity.EntityDefinition;

public interface Command<T extends EntityDefinition> extends Entity<T> {

	ClassName CLASS_NAME = new ClassName("Command");

	String getLabel();
	ClassName getClassName();
	boolean isEnabled();
	void setEnabled(boolean enabled);
	boolean isVisible();
	void setVisible(boolean visible);
	boolean equals(Object object);

}
