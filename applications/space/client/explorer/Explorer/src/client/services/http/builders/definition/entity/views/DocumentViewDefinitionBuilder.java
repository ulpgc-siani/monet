package client.services.http.builders.definition.entity.views;

import client.core.system.definition.views.DocumentViewDefinition;
import client.core.system.definition.views.FormViewDefinition;
import client.services.http.HttpInstance;
import client.services.http.builders.definition.RefBuilder;

public class DocumentViewDefinitionBuilder extends NodeViewDefinitionBuilder<client.core.model.definition.views.DocumentViewDefinition> {

	@Override
	public client.core.model.definition.views.DocumentViewDefinition build(HttpInstance instance) {
		if (instance == null)
			return null;

		DocumentViewDefinition viewDefinition = new DocumentViewDefinition();
		initialize(viewDefinition, instance);
		return viewDefinition;
	}

}