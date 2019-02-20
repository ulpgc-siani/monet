package client.services.http.builders.definition.entity.field;

import client.core.model.List;
import client.core.system.MonetList;
import client.core.system.definition.entity.field.LinkFieldDefinition;
import client.services.http.HttpInstance;
import client.services.http.HttpList;
import client.services.http.builders.Builder;
import com.google.gwt.core.client.JsArray;

public class LinkFieldDefinitionBuilder extends FieldDefinitionBuilder<client.core.model.definition.entity.field.LinkFieldDefinition> {

	@Override
	public client.core.model.definition.entity.field.LinkFieldDefinition build(HttpInstance instance) {
		if (instance == null)
			return null;

		LinkFieldDefinition definition = new LinkFieldDefinition();
		initialize(definition, instance);
		return definition;
	}

	@Override
	public void initialize(client.core.model.definition.entity.field.LinkFieldDefinition object, HttpInstance instance) {
		super.initialize(object, instance);

		LinkFieldDefinition definition = (LinkFieldDefinition)object;
		definition.setSource(new SourceDefinitionBuilder().build(instance.getObject("source")));
		definition.setAllowHistory(getAllowHistory(instance.getObject("allowHistory")));
		definition.setAllowAdd(instance.getBoolean("allowAdd"));
		definition.setAllowSearch(instance.getBoolean("allowSearch"));
		definition.setAllowEdit(instance.getBoolean("allowEdit"));
	}

	private client.core.model.definition.entity.field.LinkFieldDefinition.AllowHistoryDefinition getAllowHistory(HttpInstance instance) {
		if (instance == null)
			return null;

		LinkFieldDefinition.AllowHistoryDefinition allowHistory = new LinkFieldDefinition.AllowHistoryDefinition();
		allowHistory.setDataStore(instance.getString("dataStore"));
		return allowHistory;
	}

	private static class SourceDefinitionBuilder implements Builder<client.core.model.definition.entity.field.LinkFieldDefinition.SourceDefinition, List<client.core.model.definition.entity.field.LinkFieldDefinition.SourceDefinition>> {

		@Override
		public client.core.model.definition.entity.field.LinkFieldDefinition.SourceDefinition build(HttpInstance instance) {
			if (instance == null)
				return null;

			LinkFieldDefinition.SourceDefinition definition = new LinkFieldDefinition.SourceDefinition();
			initialize(definition, instance);
			return definition;
		}

		@Override
		public void initialize(client.core.model.definition.entity.field.LinkFieldDefinition.SourceDefinition object, HttpInstance instance) {
			LinkFieldDefinition.SourceDefinition definition = (LinkFieldDefinition.SourceDefinition)object;

			definition.setAnalyze(getAnalyze(instance.getObject("analyze")));
			definition.setCollection(instance.getString("collection"));
			definition.setFilters(getFilters(instance.getArray("filters")));
			definition.setIndex(instance.getString("index"));
			definition.setView(instance.getString("view"));
		}

		@Override
		public List<client.core.model.definition.entity.field.LinkFieldDefinition.SourceDefinition> buildList(HttpList instance) {
			return new MonetList<>();
		}

		private client.core.model.definition.entity.field.LinkFieldDefinition.SourceDefinition.AnalyzeDefinition getAnalyze(HttpInstance instance) {
			LinkFieldDefinition.SourceDefinition.AnalyzeDefinition analyze = new LinkFieldDefinition.SourceDefinition.AnalyzeDefinition();

			if (instance == null)
				return analyze;

			analyze.setDimension(getAnalyzeDimension(instance.getObject("dimension")));
			analyze.setSorting(getAnalyzeSorting(instance.getObject("sorting")));

			return analyze;
		}

		private client.core.model.definition.entity.field.LinkFieldDefinition.SourceDefinition.AnalyzeDefinition.DimensionDefinition getAnalyzeDimension(HttpInstance instance) {
			LinkFieldDefinition.SourceDefinition.AnalyzeDefinition.DimensionDefinition dimension = new LinkFieldDefinition.SourceDefinition.AnalyzeDefinition.DimensionDefinition();

			if (instance == null)
				return dimension;

			dimension.setAttributes(HttpInstance.toList(instance.getArray("attributes")));

			return dimension;
		}

		private client.core.model.definition.entity.field.LinkFieldDefinition.SourceDefinition.AnalyzeDefinition.SortingDefinition getAnalyzeSorting(HttpInstance instance) {
			LinkFieldDefinition.SourceDefinition.AnalyzeDefinition.SortingDefinition sorting = new LinkFieldDefinition.SourceDefinition.AnalyzeDefinition.SortingDefinition();

			if (instance == null)
				return sorting;

			sorting.setAttributes(HttpInstance.toList(instance.getArray("attributes")));

			return sorting;
		}

		private List<client.core.model.definition.entity.field.LinkFieldDefinition.SourceDefinition.FilterDefinition> getFilters(JsArray<HttpInstance> instances) {
			List<client.core.model.definition.entity.field.LinkFieldDefinition.SourceDefinition.FilterDefinition> result = new MonetList<>();

			for (int i = 0; i < instances.length(); i++)
				result.add(getFilter(instances.get(i)));

			return result;
		}

		private client.core.model.definition.entity.field.LinkFieldDefinition.SourceDefinition.FilterDefinition getFilter(HttpInstance instance) {
			LinkFieldDefinition.SourceDefinition.FilterDefinition filter = new LinkFieldDefinition.SourceDefinition.FilterDefinition();
			filter.setAttribute(instance.getString("attribute"));
			filter.setValue(getObject(instance, "value"));
			return filter;
		}

	}

}
