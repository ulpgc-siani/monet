package client.core.system.definition.entity.field;

import client.core.model.Instance;
import client.core.model.List;
import client.core.system.definition.entity.MultipleableFieldDefinition;

public class LinkFieldDefinition extends MultipleableFieldDefinition implements client.core.model.definition.entity.field.LinkFieldDefinition {
	private client.core.model.definition.entity.field.LinkFieldDefinition.SourceDefinition source;
	client.core.model.definition.entity.field.LinkFieldDefinition.AllowHistoryDefinition allowHistory;
	private boolean allowSearch;
	private boolean allowAdd;
    private boolean allowEdit;

    @Override
	public Instance.ClassName getClassName() {
		return client.core.model.definition.entity.field.LinkFieldDefinition.CLASS_NAME;
	}

	@Override
	public client.core.model.definition.entity.field.LinkFieldDefinition.SourceDefinition getSource() {
		return source;
	}

	public void setSource(client.core.model.definition.entity.field.LinkFieldDefinition.SourceDefinition source) {
		this.source = source;
	}

	@Override
	public boolean allowHistory() {
		return allowHistory != null;
	}

	@Override
	public client.core.model.definition.entity.field.LinkFieldDefinition.AllowHistoryDefinition getAllowHistory() {
		return allowHistory;
	}

	public void setAllowHistory(client.core.model.definition.entity.field.LinkFieldDefinition.AllowHistoryDefinition allowHistory) {
		this.allowHistory = allowHistory;
	}

	@Override
	public boolean allowSearch() {
		return allowSearch;
	}

	public void setAllowSearch(boolean allowSearch) {
		this.allowSearch = allowSearch;
	}

	@Override
	public boolean allowAdd() {
		return allowAdd;
	}

    public void setAllowAdd(boolean allowAdd) {
		this.allowAdd = allowAdd;
	}

    @Override
    public boolean allowEdit() {
        return allowEdit;
    }

    public void setAllowEdit(boolean allowEdit) {
        this.allowEdit = allowEdit;
    }

	public static class SourceDefinition implements client.core.model.definition.entity.field.LinkFieldDefinition.SourceDefinition {
		private String index;
		private String view;
		private String collection;
		private List<client.core.model.definition.entity.field.LinkFieldDefinition.SourceDefinition.FilterDefinition> filters;
		private client.core.model.definition.entity.field.LinkFieldDefinition.SourceDefinition.AnalyzeDefinition analyze;

		@Override
		public String getIndex() {
			return index;
		}

		public void setIndex(String index) {
			this.index = index;
		}

		@Override
		public String getView() {
			return view;
		}

		public void setView(String view) {
			this.view = view;
		}

		@Override
		public String getCollection() {
			return collection;
		}

		public void setCollection(String collection) {
			this.collection = collection;
		}

		@Override
		public List<client.core.model.definition.entity.field.LinkFieldDefinition.SourceDefinition.FilterDefinition> getFilters() {
			return filters;
		}

		public void setFilters(List<client.core.model.definition.entity.field.LinkFieldDefinition.SourceDefinition.FilterDefinition> filters) {
			this.filters = filters;
		}

		@Override
		public client.core.model.definition.entity.field.LinkFieldDefinition.SourceDefinition.AnalyzeDefinition getAnalyze() {
			return analyze;
		}

		public void setAnalyze(client.core.model.definition.entity.field.LinkFieldDefinition.SourceDefinition.AnalyzeDefinition analyze) {
			this.analyze = analyze;
		}

		public static class FilterDefinition implements client.core.model.definition.entity.field.LinkFieldDefinition.SourceDefinition.FilterDefinition {
			private String attribute;
			private Object value;

			@Override
			public String getAttribute() {
				return attribute;
			}

			public void setAttribute(String attribute) {
				this.attribute = attribute;
			}

			@Override
			public Object getValue() {
				return value;
			}

			public void setValue(Object value) {
				this.value = value;
			}
		}

		public static class AnalyzeDefinition implements client.core.model.definition.entity.field.LinkFieldDefinition.SourceDefinition.AnalyzeDefinition {
			private client.core.model.definition.entity.field.LinkFieldDefinition.SourceDefinition.AnalyzeDefinition.SortingDefinition sorting;
			private client.core.model.definition.entity.field.LinkFieldDefinition.SourceDefinition.AnalyzeDefinition.DimensionDefinition dimension;

			@Override
			public client.core.model.definition.entity.field.LinkFieldDefinition.SourceDefinition.AnalyzeDefinition.SortingDefinition getSorting() {
				return sorting;
			}

			public void setSorting(client.core.model.definition.entity.field.LinkFieldDefinition.SourceDefinition.AnalyzeDefinition.SortingDefinition sorting) {
				this.sorting = sorting;
			}

			@Override
			public client.core.model.definition.entity.field.LinkFieldDefinition.SourceDefinition.AnalyzeDefinition.DimensionDefinition getDimension() {
				return dimension;
			}

			public void setDimension(client.core.model.definition.entity.field.LinkFieldDefinition.SourceDefinition.AnalyzeDefinition.DimensionDefinition dimension) {
				this.dimension = dimension;
			}

			public static class SortingDefinition implements client.core.model.definition.entity.field.LinkFieldDefinition.SourceDefinition.AnalyzeDefinition.SortingDefinition {
				private List<String> attributes;

				@Override
				public List<String> getAttributes() {
					return attributes;
				}

				public void setAttributes(List<String> attributes) {
					this.attributes = attributes;
				}
			}

			public static class DimensionDefinition implements client.core.model.definition.entity.field.LinkFieldDefinition.SourceDefinition.AnalyzeDefinition.DimensionDefinition {
				private List<String> attributes;

				@Override
				public List<String> getAttributes() {
					return attributes;
				}

				public void setAttributes(List<String> attributes) {
					this.attributes = attributes;
				}
			}
		}
	}

	public static class AllowHistoryDefinition implements client.core.model.definition.entity.field.LinkFieldDefinition.AllowHistoryDefinition {
		private String dataStore;

		@Override
		public String getDataStore() {
			return dataStore;
		}

		public void setDataStore(String dataStore) {
			this.dataStore = dataStore;
		}
	}

}