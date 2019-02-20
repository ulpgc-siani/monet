package client.core.model;

import client.core.model.definition.entity.RoleDefinition;

public interface Role<Definition extends RoleDefinition> extends Entity<Definition> {

	ClassName CLASS_NAME = new ClassName("Role");

	boolean isForUser();
	boolean isForService();
	boolean equals(Object object);

}
