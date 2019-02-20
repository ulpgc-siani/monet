package client.core.model;

import client.core.model.definition.entity.ServiceDefinition;

public interface Service<Definition extends ServiceDefinition> extends Process<Definition> {

	ClassName CLASS_NAME = new ClassName("Service");

	ClassName getClassName();

}
