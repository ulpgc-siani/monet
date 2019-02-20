package client.core.model.definition.views;

import client.core.model.definition.Ref;
import client.core.model.List;

public interface SetViewDefinition extends NodeViewDefinition {
	ShowDefinition getShow();
	AnalyzeDefinition getAnalyze();
	SelectDefinition getSelect();

	interface ShowDefinition {

		IndexDefinition getIndex();
		ItemsDefinition getItems();

		interface IndexDefinition {
			String getWithView();
			String getSortBy();
			String getSortMode();
		}

		interface ItemsDefinition {
		}
	}

	interface AnalyzeDefinition {
		DimensionDefinition getDimension();
		SortingDefinition getSorting();

		interface DimensionDefinition {
			List<Ref> getAttribute();
		}

		interface SortingDefinition {
			List<Ref> getAttribute();
		}
	}

	interface SelectDefinition {
		List<Ref> getNode();
	}
}
