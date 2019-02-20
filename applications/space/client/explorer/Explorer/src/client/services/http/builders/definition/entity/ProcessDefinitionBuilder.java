package client.services.http.builders.definition.entity;

import client.core.system.definition.entity.ProcessDefinition;
import client.services.http.HttpInstance;
import client.services.http.builders.definition.entity.views.ViewDefinitionBuilder;

public class ProcessDefinitionBuilder<T extends client.core.model.definition.entity.TaskDefinition> extends TaskDefinitionBuilder<T> {

	@Override
	public void initialize(T object, HttpInstance instance) {
		super.initialize(object, instance);

		ProcessDefinition definition = (ProcessDefinition)object;
		definition.setViews(new ViewDefinitionBuilder().buildList(instance.getList("views")));
	}
}