package client.core.model.definition.entity.field;

import client.core.model.Instance;
import client.core.model.List;
import client.core.model.definition.entity.MultipleableFieldDefinition;

public interface TextFieldDefinition extends MultipleableFieldDefinition {

	Instance.ClassName CLASS_NAME = new Instance.ClassName("TextFieldDefinition");

	boolean allowHistory();
	AllowHistoryDefinition getAllowHistory();

	LengthDefinition getLength();
	EditionDefinition getEdition();
	List<PatternDefinition> getPatterns();

	interface LengthDefinition {
		int getMin();
		int getMax();
	}

	interface EditionDefinition {
		enum Mode { UPPERCASE, LOWERCASE, TITLE, SENTENCE;

			public static Mode fromString(String mode) {
				return Mode.valueOf(mode.toUpperCase());
			}
		}
		Mode getMode();
	}

	interface PatternDefinition {
		String getRegExp();
		List<MetaDefinition> getMetaList();

		interface MetaDefinition {
			long getPosition();
			String getIndicator();
		}
	}

	interface AllowHistoryDefinition {
		String getDataStore();
	}
}
