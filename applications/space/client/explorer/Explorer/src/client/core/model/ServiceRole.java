package client.core.model;

import client.core.model.definition.entity.RoleDefinition;

public interface ServiceRole<Definition extends RoleDefinition> extends Role<Definition> {

	ClassName CLASS_NAME = new ClassName("ServiceRole");

	ClassName getClassName();

}
