package client.core.system;

import client.core.model.definition.entity.ProcessDefinition;
import client.core.model.definition.views.TaskViewDefinition;

import java.util.HashMap;
import java.util.Map;

public abstract class Process<Definition extends ProcessDefinition> extends Task<Definition> implements client.core.model.Process<Definition> {
	private Map<String, client.core.model.Node> shortcuts = new HashMap<>();

	public Process() {
	}

	public Process(String id, String label) {
		super(id, label);
	}

	@Override
	protected client.core.model.ViewList<client.core.model.TaskView> loadViews() {
		client.core.model.ViewList<client.core.model.TaskView> result = new client.core.system.ViewList<>();

		result.add(new TaskStateView(TaskStateView.STATE_VIEW, "", true, this));

		ProcessDefinition definition = getDefinition();
		for (TaskViewDefinition taskViewDefinition : definition.getViews()) {
			String shortcut = taskViewDefinition.getShow().getShortcut();
			client.core.model.Node shortcutNode = getShortcut(shortcut);
			TaskShortcutView taskShortcutView = new TaskShortcutView(new Key(shortcut), taskViewDefinition.getLabel(), taskViewDefinition.isDefault(), this);
			taskShortcutView.setDefinition(taskViewDefinition);

			if (shortcutNode != null)
				result.add(taskShortcutView);
		}

		return result;
	}

	@Override
	public Map<String, client.core.model.Node> getShortcuts() {
		return shortcuts;
	}

	@Override
	public client.core.model.Node getShortcut(String name) {
		return shortcuts.get(name);
	}

	public void addShortcut(String name, Node shortcut) {
		this.shortcuts.put(name, shortcut);
	}
}
