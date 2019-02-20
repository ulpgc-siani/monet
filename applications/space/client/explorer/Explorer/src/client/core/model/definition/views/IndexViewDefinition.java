package client.core.model.definition.views;

import client.core.model.List;
import client.core.model.definition.Ref;

public interface IndexViewDefinition extends ViewDefinition {

	enum Type {
		INDEX;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}

		public static Type fromString(String type) {
			return Type.valueOf(type.toUpperCase());
		}

	}

	ShowDefinition getShow();
	List<Ref> getAttributes();

	interface ShowDefinition {
		Ref getTitle();
		List<Ref> getLines();
		List<Ref> getLinesBelow();
		List<Ref> getHighlight();
		List<Ref> getFooter();
		Ref getIcon();
		Ref getPicture();
	}
}
