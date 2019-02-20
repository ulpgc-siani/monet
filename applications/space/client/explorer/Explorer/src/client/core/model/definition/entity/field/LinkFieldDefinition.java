package client.core.model.definition.entity.field;

import client.core.model.Instance;
import client.core.model.List;
import client.core.model.definition.entity.MultipleableFieldDefinition;

public interface LinkFieldDefinition extends MultipleableFieldDefinition {

	Instance.ClassName CLASS_NAME = new Instance.ClassName("LinkFieldDefinition");

	SourceDefinition getSource();
	boolean allowHistory();
	AllowHistoryDefinition getAllowHistory();
	boolean allowSearch();
	boolean allowAdd();
	boolean allowEdit();

	interface SourceDefinition {
		String getIndex();
		String getView();
		String getCollection();
		List<FilterDefinition> getFilters();
		AnalyzeDefinition getAnalyze();

		interface FilterDefinition {
			String getAttribute();
			Object getValue();
		}

		interface AnalyzeDefinition {
			SortingDefinition getSorting();
			DimensionDefinition getDimension();

			interface SortingDefinition {
				List<String> getAttributes();
			}

			interface DimensionDefinition {
				List<String> getAttributes();
			}
		}
	}

	interface AllowHistoryDefinition {
		String getDataStore();
	}

}