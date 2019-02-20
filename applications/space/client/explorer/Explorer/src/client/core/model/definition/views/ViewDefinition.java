package client.core.model.definition.views;

import client.core.model.definition.entity.EntityDefinition;

public interface ViewDefinition extends EntityDefinition {

	enum Design {
		LIST, DOCUMENT;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}

		public static Design fromString(String design) {
			return Design.valueOf(design.toUpperCase());
		}
	}

	boolean isDefault();
	Design getDesign();
}
