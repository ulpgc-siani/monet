package client.services.http.builders.definition.entity.views;

import client.core.system.definition.views.ContainerViewDefinition;
import client.services.http.HttpInstance;
import client.services.http.builders.definition.RefBuilder;

public class ContainerViewDefinitionBuilder extends NodeViewDefinitionBuilder<client.core.model.definition.views.ContainerViewDefinition> {

	@Override
	public client.core.model.definition.views.ContainerViewDefinition build(HttpInstance instance) {
		if (instance == null)
			return null;

		ContainerViewDefinition viewDefinition = new ContainerViewDefinition();
		initialize(viewDefinition, instance);
		return viewDefinition;
	}

	@Override
	public void initialize(client.core.model.definition.views.ContainerViewDefinition object, HttpInstance instance) {
		super.initialize(object, instance);

		ContainerViewDefinition definition = (ContainerViewDefinition)object;
		definition.setShow(getShow(instance.getObject("show")));
	}

	private client.core.model.definition.views.ContainerViewDefinition.ShowDefinition getShow(HttpInstance instance) {
		ContainerViewDefinition.ShowDefinition show = new ContainerViewDefinition.ShowDefinition();
		show.setComponent(new RefBuilder().buildList(instance.getList("components")));
		return show;
	}

}