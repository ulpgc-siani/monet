package client.core.model.definition.entity.field;

import client.core.model.Instance;
import client.core.model.List;
import client.core.model.definition.entity.MultipleableFieldDefinition;

public interface CheckFieldDefinition extends MultipleableFieldDefinition {

	Instance.ClassName CLASS_NAME = new Instance.ClassName("CheckFieldDefinition");

	boolean allowKey();

	List<TermDefinition> getTerms();
	String getSource();
	SelectDefinition getSelect();

	interface TermDefinition {
		String getKey();
		String getLabel();
		List<TermDefinition> getTerms();
		boolean isCategory();
	}

	interface SelectDefinition {
		enum Flatten { NONE, ALL, LEVEL, LEAF, INTERNAL;

			public static Flatten fromString(String flatten) {
				return Flatten.valueOf(flatten.toUpperCase());
			}
		}

		Flatten getFlatten();
		int getDepth();
		Object getRoot();
		FilterDefinition getFilter();

		interface FilterDefinition {
			List<Object> getTags();
		}
	}
}


