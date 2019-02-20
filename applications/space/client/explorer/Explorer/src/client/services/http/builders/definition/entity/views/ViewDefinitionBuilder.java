package client.services.http.builders.definition.entity.views;

import client.core.model.definition.views.IndexViewDefinition;
import client.core.model.definition.views.NodeViewDefinition;
import client.core.model.definition.views.TaskViewDefinition;
import client.core.system.definition.views.ViewDefinition;
import client.services.http.HttpInstance;
import client.services.http.builders.definition.entity.EntityDefinitionBuilder;

import java.util.HashMap;
import java.util.Map;

public class ViewDefinitionBuilder<T extends client.core.model.definition.views.ViewDefinition> extends EntityDefinitionBuilder<T> {

	private static final Map<String, ViewDefinitionBuilder> builders = new HashMap<String, ViewDefinitionBuilder>() {{
		put(NodeViewDefinition.Type.DESKTOP.toString(), new DesktopViewDefinitionBuilder());
		put(NodeViewDefinition.Type.CONTAINER.toString(), new ContainerViewDefinitionBuilder());
		put(NodeViewDefinition.Type.SET.toString(), new SetViewDefinitionBuilder());
		put(NodeViewDefinition.Type.FORM.toString(), new FormViewDefinitionBuilder());
		put(NodeViewDefinition.Type.DOCUMENT.toString(), new DocumentViewDefinitionBuilder());
		put(IndexViewDefinition.Type.INDEX.toString(), new IndexViewDefinitionBuilder());
		put(TaskViewDefinition.Type.ABSTRACT.toString(), new TaskViewDefinitionBuilder());
	}};

	@Override
	public T build(HttpInstance instance) {
		String type = instance.getString("type");

		if (builders.containsKey(type))
			return (T) builders.get(type).build(instance);

		return super.build(instance);
	}

	@Override
	public void initialize(T object, HttpInstance instance) {
		super.initialize(object, instance);

		ViewDefinition definition = (ViewDefinition)object;
		definition.setDefault(instance.getBoolean("default"));
	}
}