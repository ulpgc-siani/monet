package client.core.model.definition.views;

public interface NodeViewDefinition extends ViewDefinition {

	enum Type {
		DESKTOP, CONTAINER, SET, FORM, DOCUMENT;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}

		public static Type fromString(String type) {
			return Type.valueOf(type.toUpperCase());
		}

	}

}
