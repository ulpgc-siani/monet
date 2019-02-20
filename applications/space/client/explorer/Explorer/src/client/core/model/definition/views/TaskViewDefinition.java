package client.core.model.definition.views;

public interface TaskViewDefinition extends ViewDefinition {

	enum Type {
		ABSTRACT;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}

		public static Type fromString(String type) {
			return Type.valueOf(type.toUpperCase());
		}

	}

	ShowDefinition getShow();

	interface ShowDefinition {
		String getShortcut();
		String getShortcutView();
	}
}
