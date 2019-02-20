package client.core.system.definition.entity.field;

import client.core.model.Instance;
import client.core.model.List;
import client.core.system.definition.entity.MultipleableFieldDefinition;

public class SelectFieldDefinition extends MultipleableFieldDefinition implements client.core.model.definition.entity.field.SelectFieldDefinition {
	private List<client.core.model.definition.entity.field.SelectFieldDefinition.TermDefinition> terms;
	private String source;
	private client.core.model.definition.entity.field.SelectFieldDefinition.AllowHistoryDefinition allowHistory;
	private boolean allowOther;
	private boolean allowKey;
	private boolean allowSearch;
	private client.core.model.definition.entity.field.SelectFieldDefinition.SelectDefinition select;

	@Override
	public Instance.ClassName getClassName() {
		return client.core.model.definition.entity.field.SelectFieldDefinition.CLASS_NAME;
	}

	@Override
	public List<client.core.model.definition.entity.field.SelectFieldDefinition.TermDefinition> getTerms() {
		return terms;
	}

	public void setTerms(List<client.core.model.definition.entity.field.SelectFieldDefinition.TermDefinition> terms) {
		this.terms = terms;
	}

	@Override
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	@Override
	public boolean allowHistory() {
		return allowHistory != null;
	}

	@Override
	public client.core.model.definition.entity.field.SelectFieldDefinition.AllowHistoryDefinition getAllowHistory() {
		return allowHistory;
	}

	public void setAllowHistory(client.core.model.definition.entity.field.SelectFieldDefinition.AllowHistoryDefinition allowHistory) {
		this.allowHistory = allowHistory;
	}

	@Override
	public boolean allowOther() {
		return allowOther;
	}

	public void setAllowOther(boolean allowOther) {
		this.allowOther = allowOther;
	}

	@Override
	public boolean allowKey() {
		return allowKey;
	}

	@Override
	public boolean allowSearch() {
		return allowSearch;
	}

	public void setAllowSearch(boolean allowSearch) {
		this.allowSearch = allowSearch;
	}

	public void setAllowKey(boolean allowKey) {
		this.allowKey = allowKey;
	}

	@Override
	public client.core.model.definition.entity.field.SelectFieldDefinition.SelectDefinition getSelect() {
		return select;
	}

	public void setSelect(client.core.model.definition.entity.field.SelectFieldDefinition.SelectDefinition select) {
		this.select = select;
	}

	@Override
	public String getValueForTermOther() {
		return "OTHER";
	}

	public static class TermDefinition implements client.core.model.definition.entity.field.SelectFieldDefinition.TermDefinition {
		private String key;
		private String label;
		private boolean isCategory;
		private List<client.core.model.definition.entity.field.SelectFieldDefinition.TermDefinition> terms;

		@Override
		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		@Override
		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		@Override
		public boolean isCategory() {
			return isCategory;
		}

		public void setCategory(boolean isCategory) {
			this.isCategory = isCategory;
		}

		@Override
		public List<client.core.model.definition.entity.field.SelectFieldDefinition.TermDefinition> getTerms() {
			return terms;
		}

		public void setTerms(List<client.core.model.definition.entity.field.SelectFieldDefinition.TermDefinition> terms) {
			this.terms = terms;
		}
	}

	public static class SelectDefinition implements client.core.model.definition.entity.field.SelectFieldDefinition.SelectDefinition {
		private Flatten flatten;
		private int depth;
		private String context;
		private Object root;
		private client.core.model.definition.entity.field.SelectFieldDefinition.SelectDefinition.FilterDefinition filter;
		private boolean embedded;

		@Override
		public Flatten getFlatten() {
			return flatten;
		}

		public void setFlatten(Flatten flatten) {
			this.flatten = flatten;
		}

		@Override
		public int getDepth() {
			return depth;
		}

		public void setDepth(int depth) {
			this.depth = depth;
		}

		@Override
		public String getContext() {
			return context;
		}

		public void setContext(String context) {
			this.context = context;
		}

		@Override
		public Object getRoot() {
			return root;
		}

		public void setRoot(Object root) {
			this.root = root;
		}

		@Override
		public client.core.model.definition.entity.field.SelectFieldDefinition.SelectDefinition.FilterDefinition getFilter() {
			return filter;
		}

		public void setFilter(client.core.model.definition.entity.field.SelectFieldDefinition.SelectDefinition.FilterDefinition filter) {
			this.filter = filter;
		}

		@Override
		public boolean isEmbedded() {
			return embedded;
		}

		public void setEmbedded(boolean embedded) {
			this.embedded = embedded;
		}

		public static class FilterDefinition implements client.core.model.definition.entity.field.SelectFieldDefinition.SelectDefinition.FilterDefinition {
			private List<Object> tags;

			@Override
			public List<Object> getTags() {
				return tags;
			}

			public void setTags(List<Object> tags) {
				this.tags = tags;
			}
		}
	}

	public static class AllowHistoryDefinition implements client.core.model.definition.entity.field.SelectFieldDefinition.AllowHistoryDefinition {
		public String dataStore;

		@Override
		public String getDataStore() {
			return dataStore;
		}

		public void setDataStore(String dataStore) {
			this.dataStore = dataStore;
		}
	}

}