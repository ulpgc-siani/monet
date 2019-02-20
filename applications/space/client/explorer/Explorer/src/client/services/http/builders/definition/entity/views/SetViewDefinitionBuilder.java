package client.services.http.builders.definition.entity.views;

import client.core.model.definition.views.ViewDefinition.Design;
import client.core.system.definition.views.SetViewDefinition;
import client.services.http.HttpInstance;
import client.services.http.builders.definition.RefBuilder;

public class SetViewDefinitionBuilder extends NodeViewDefinitionBuilder<client.core.model.definition.views.SetViewDefinition> {

	@Override
	public client.core.model.definition.views.SetViewDefinition build(HttpInstance instance) {
		if (instance == null)
			return null;

		SetViewDefinition viewDefinition = new SetViewDefinition();
		initialize(viewDefinition, instance);
		return viewDefinition;
	}

	@Override
	public void initialize(client.core.model.definition.views.SetViewDefinition object, HttpInstance instance) {
		super.initialize(object, instance);

		SetViewDefinition definition = (SetViewDefinition)object;

		if (!instance.getString("design").isEmpty())
			definition.setDesign(Design.fromString(instance.getString("design")));

		definition.setShow(getShow(instance.getObject("show")));
		definition.setAnalyze(getAnalyze(instance.getObject("analyze")));
		definition.setSelect(getSelect(instance.getObject("select")));
	}

	private client.core.model.definition.views.SetViewDefinition.ShowDefinition getShow(HttpInstance instance) {
		SetViewDefinition.ShowDefinition show = new SetViewDefinition.ShowDefinition();
		show.setIndex(getIndex(instance.getObject("index")));
		show.setItems(getItems(instance.getObject("items")));
		return show;
	}

	private client.core.model.definition.views.SetViewDefinition.ShowDefinition.IndexDefinition getIndex(HttpInstance instance) {
		if (instance == null)
			return null;

		SetViewDefinition.ShowDefinition.IndexDefinition index = new SetViewDefinition.ShowDefinition.IndexDefinition();
		index.setSortBy(instance.getString("sortBy"));
		index.setSortMode(instance.getString("mode"));
		index.setWithView(instance.getString("withView"));
		return index;
	}

	private client.core.model.definition.views.SetViewDefinition.ShowDefinition.ItemsDefinition getItems(HttpInstance instance) {
		if (instance == null)
			return null;

		SetViewDefinition.ShowDefinition.ItemsDefinition items = new SetViewDefinition.ShowDefinition.ItemsDefinition();
		return items;
	}

	private client.core.model.definition.views.SetViewDefinition.AnalyzeDefinition getAnalyze(HttpInstance instance) {
		if (instance == null)
			return null;

		SetViewDefinition.AnalyzeDefinition analyzeDefinition = new SetViewDefinition.AnalyzeDefinition();
		analyzeDefinition.setDimension(getAnalyzeDimension(instance.getObject("dimension")));
		analyzeDefinition.setSorting(getAnalyzeSorting(instance.getObject("sorting")));

		return analyzeDefinition;
	}

	private client.core.model.definition.views.SetViewDefinition.AnalyzeDefinition.DimensionDefinition getAnalyzeDimension(HttpInstance instance) {
		if (instance == null)
			return null;

		SetViewDefinition.AnalyzeDefinition.DimensionDefinition dimensionDefinition = new SetViewDefinition.AnalyzeDefinition.DimensionDefinition();
		dimensionDefinition.setAttributes(new RefBuilder().buildList(instance.getList("attributes")));
		return dimensionDefinition;
	}

	private client.core.model.definition.views.SetViewDefinition.AnalyzeDefinition.SortingDefinition getAnalyzeSorting(HttpInstance instance) {
		if (instance == null)
			return null;

		SetViewDefinition.AnalyzeDefinition.SortingDefinition sortingDefinition = new SetViewDefinition.AnalyzeDefinition.SortingDefinition();
		sortingDefinition.setAttributes(new RefBuilder().buildList(instance.getList("attributes")));
		return sortingDefinition;
	}

	private client.core.model.definition.views.SetViewDefinition.SelectDefinition getSelect(HttpInstance instance) {
		if (instance == null)
			return null;

		SetViewDefinition.SelectDefinition select = new SetViewDefinition.SelectDefinition();
		select.setNodes(new RefBuilder().buildList(instance.getList("nodes")));
		return select;
	}

}