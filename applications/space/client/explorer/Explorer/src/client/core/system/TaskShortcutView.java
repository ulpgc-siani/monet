package client.core.system;

import client.core.model.definition.views.TaskViewDefinition;

public class TaskShortcutView extends TaskView implements client.core.model.TaskShortcutView {

	public TaskShortcutView() {
	}

	public TaskShortcutView(client.core.model.Key key, String label, boolean isDefault) {
		super(key, label, isDefault);
	}

	public TaskShortcutView(client.core.model.Key key, String label, boolean isDefault, Process scope) {
		super(key, label, isDefault, scope);
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.TaskShortcutView.CLASS_NAME;
	}

	@Override
	public String getLabel() {

		String label = getDefinition().getLabel();
		if (label != null && !label.isEmpty())
			return label;

		client.core.model.Node shortcut = getShortcut();
		if (shortcut != null)
			return shortcut.getLabel();

		return "";
	}

	@Override
	public client.core.model.Node getShortcut() {
		client.core.model.Task scope = getScope();
		client.core.model.Node shortcut = null;

		if (scope instanceof Process) {
			TaskViewDefinition definition = (TaskViewDefinition) getDefinition();
			shortcut = ((Process) scope).getShortcut(definition.getShow().getShortcut());
		}

		return shortcut;
	}

}
