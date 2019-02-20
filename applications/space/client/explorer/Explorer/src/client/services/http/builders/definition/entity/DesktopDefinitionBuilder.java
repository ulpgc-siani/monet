package client.services.http.builders.definition.entity;

import client.core.system.definition.entity.DesktopDefinition;
import client.services.http.HttpInstance;

public class DesktopDefinitionBuilder extends NodeDefinitionBuilder<client.core.model.definition.entity.DesktopDefinition> {

	@Override
	public client.core.model.definition.entity.DesktopDefinition build(HttpInstance instance) {
		if (instance == null)
			return null;

		DesktopDefinition definition = new DesktopDefinition();
		initialize(definition, instance);
		return definition;
	}

}