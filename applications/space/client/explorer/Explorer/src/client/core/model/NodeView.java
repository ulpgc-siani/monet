package client.core.model;

import client.core.model.definition.views.NodeViewDefinition;

public interface NodeView<Definition extends NodeViewDefinition> extends View<Definition> {

	enum Type {
		DESKTOP, COLLECTION, FORM, CONTAINER, CATALOG;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}

		public static Type fromString(String type) {
			return Type.valueOf(type.toUpperCase());
		}

	}

	Key PREVIEW = new Key() {
		@Override
		public String getCode() {
			return "preview";
		}

		@Override
		public String getName() {
			return "preview";
		}
	};

	<T extends Node> T getScope();
	void setScope(Node node);

}
