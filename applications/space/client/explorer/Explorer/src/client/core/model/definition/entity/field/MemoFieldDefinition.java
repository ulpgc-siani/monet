package client.core.model.definition.entity.field;

import client.core.model.Instance;
import client.core.model.definition.entity.MultipleableFieldDefinition;

public interface MemoFieldDefinition extends MultipleableFieldDefinition {

	Instance.ClassName CLASS_NAME = new Instance.ClassName("MemoFieldDefinition");

	boolean allowHistory();
	AllowHistoryDefinition getAllowHistory();

	LengthDefinition getLength();
	EditionDefinition getEdition();

	interface LengthDefinition {
		int getMax();
	}

	interface EditionDefinition {
		enum Mode {
			RICH;

			public static Mode fromString(String mode) {
				return Mode.valueOf(mode.toUpperCase());
			}
		}
		Mode getMode();
	}

	interface AllowHistoryDefinition {
		String getDataStore();
	}

}
