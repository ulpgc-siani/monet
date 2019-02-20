package client.core.model.definition.entity.field;

import client.core.model.Instance;
import client.core.model.List;
import client.core.model.definition.entity.MultipleableFieldDefinition;

public interface SelectFieldDefinition extends MultipleableFieldDefinition {

	Instance.ClassName CLASS_NAME = new Instance.ClassName("SelectFieldDefinition");

	List<TermDefinition> getTerms();
	String getSource();
	boolean allowHistory();
	AllowHistoryDefinition getAllowHistory();
	boolean allowOther();
	boolean allowKey();
	boolean allowSearch();
	SelectDefinition getSelect();
    String getValueForTermOther();

    interface TermDefinition {
		String getKey();
		String getLabel();
		List<TermDefinition> getTerms();
		boolean isCategory();
	}

	interface SelectDefinition {
		enum Flatten {
			NONE, ALL, LEVEL, LEAF, INTERNAL;

			public static Flatten fromString(String flatten) {
				return Flatten.valueOf(flatten.toUpperCase());
			}
		}

		Flatten getFlatten();
		int getDepth();
		String getContext();
		Object getRoot();
		FilterDefinition getFilter();
		boolean isEmbedded();

		interface FilterDefinition {
			List<Object> getTags();
		}
	}

	interface AllowHistoryDefinition {
		String getDataStore();
	}

}