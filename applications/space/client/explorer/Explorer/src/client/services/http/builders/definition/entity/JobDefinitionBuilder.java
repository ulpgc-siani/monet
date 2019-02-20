package client.services.http.builders.definition.entity;

import client.core.system.definition.entity.JobDefinition;
import client.services.http.HttpInstance;

public class JobDefinitionBuilder extends TaskDefinitionBuilder<client.core.model.definition.entity.JobDefinition> {

	@Override
	public client.core.model.definition.entity.JobDefinition build(HttpInstance instance) {
		if (instance == null)
			return null;

		JobDefinition definition = new JobDefinition();
		initialize(definition, instance);
		return definition;
	}

}