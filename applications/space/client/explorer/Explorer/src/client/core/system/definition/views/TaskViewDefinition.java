package client.core.system.definition.views;

public class TaskViewDefinition extends client.core.system.definition.views.ViewDefinition implements client.core.model.definition.views.TaskViewDefinition {
	private client.core.model.definition.views.TaskViewDefinition.ShowDefinition showDefinition;

	@Override
	public client.core.model.definition.views.TaskViewDefinition.ShowDefinition getShow() {
		return showDefinition;
	}

	public void setShow(client.core.model.definition.views.TaskViewDefinition.ShowDefinition showDefinition) {
		this.showDefinition = showDefinition;
	}

	public static class ShowDefinition implements client.core.model.definition.views.TaskViewDefinition.ShowDefinition {
		private String shortcut;
		private String shortcutView;

		@Override
		public String getShortcut() {
			return shortcut;
		}

		public void setShortcut(String shortcut) {
			this.shortcut = shortcut;
		}

		@Override
		public String getShortcutView() {
			return shortcutView;
		}

		public void setShortcutView(String shortcutView) {
			this.shortcutView = shortcutView;
		}
	}
}