package client.core.model;

import client.core.model.definition.entity.JobDefinition;

public interface Job<Definition extends JobDefinition> extends Task<Definition> {

	ClassName CLASS_NAME = new ClassName("Job");

	ClassName getClassName();

}
