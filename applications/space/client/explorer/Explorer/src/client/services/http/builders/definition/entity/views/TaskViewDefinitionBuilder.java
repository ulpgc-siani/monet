package client.services.http.builders.definition.entity.views;

import client.core.system.definition.views.TaskViewDefinition;
import client.services.http.HttpInstance;

public class TaskViewDefinitionBuilder extends ViewDefinitionBuilder<client.core.model.definition.views.TaskViewDefinition> {
	@Override
	public client.core.model.definition.views.TaskViewDefinition build(HttpInstance instance) {
		if (instance == null)
			return null;

		TaskViewDefinition viewDefinition = new TaskViewDefinition();
		initialize(viewDefinition, instance);
		return viewDefinition;
	}

	@Override
	public void initialize(client.core.model.definition.views.TaskViewDefinition object, HttpInstance instance) {
		super.initialize(object, instance);

		TaskViewDefinition definition = (TaskViewDefinition)object;
		definition.setShow(getShow(instance.getObject("show")));
	}

	private client.core.model.definition.views.TaskViewDefinition.ShowDefinition getShow(HttpInstance instance) {
		TaskViewDefinition.ShowDefinition show = new TaskViewDefinition.ShowDefinition();

		show.setShortcut(instance.getString("shortcut"));

		if (!instance.getString("shortcutView").isEmpty())
			show.setShortcutView(instance.getString("shortcutView"));

		return show;
	}
}