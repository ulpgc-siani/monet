package client.core.system.definition.entity.field;

import client.core.model.Instance;
import client.core.model.List;
import client.core.system.definition.entity.MultipleableFieldDefinition;

public class CheckFieldDefinition extends MultipleableFieldDefinition implements client.core.model.definition.entity.field.CheckFieldDefinition {
	private boolean allowKey;
	private List<client.core.model.definition.entity.field.CheckFieldDefinition.TermDefinition> terms;
	private String source;
	private client.core.model.definition.entity.field.CheckFieldDefinition.SelectDefinition select;

	@Override
	public Instance.ClassName getClassName() {
		return client.core.model.definition.entity.field.CheckFieldDefinition.CLASS_NAME;
	}

	@Override
	public boolean allowKey() {
		return allowKey;
	}

	public void setAllowKey(boolean allowKey) {
		this.allowKey = allowKey;
	}

	@Override
	public List<client.core.model.definition.entity.field.CheckFieldDefinition.TermDefinition> getTerms() {
		return terms;
	}

	public void setTerms(List<client.core.model.definition.entity.field.CheckFieldDefinition.TermDefinition> terms) {
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
	public client.core.model.definition.entity.field.CheckFieldDefinition.SelectDefinition getSelect() {
		return select;
	}

	public void setSelect(client.core.model.definition.entity.field.CheckFieldDefinition.SelectDefinition select) {
		this.select = select;
	}

	public static class TermDefinition implements client.core.model.definition.entity.field.CheckFieldDefinition.TermDefinition {
		private String key;
		private String label;
		private List<client.core.model.definition.entity.field.CheckFieldDefinition.TermDefinition> termDefinitions;
		private boolean isCategory;

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
		public List<client.core.model.definition.entity.field.CheckFieldDefinition.TermDefinition> getTerms() {
			return termDefinitions;
		}

		@Override
		public boolean isCategory() {
			return isCategory;
		}

		public void setCategory(boolean isCategory) {
			this.isCategory = isCategory;
		}

		public void setTermDefinitions(List<client.core.model.definition.entity.field.CheckFieldDefinition.TermDefinition> termDefinitions) {
			this.termDefinitions = termDefinitions;
		}
	}

	public static class SelectDefinition implements client.core.model.definition.entity.field.CheckFieldDefinition.SelectDefinition {
		private Flatten flatten;
		private int depth;
		private Object root;
		private client.core.model.definition.entity.field.CheckFieldDefinition.SelectDefinition.FilterDefinition filter;

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
		public Object getRoot() {
			return root;
		}

		public void setRoot(Object root) {
			this.root = root;
		}

		@Override
		public client.core.model.definition.entity.field.CheckFieldDefinition.SelectDefinition.FilterDefinition getFilter() {
			return filter;
		}

		public void setFilter(client.core.model.definition.entity.field.CheckFieldDefinition.SelectDefinition.FilterDefinition filter) {
			this.filter = filter;
		}

		public static class FilterDefinition implements client.core.model.definition.entity.field.CheckFieldDefinition.SelectDefinition.FilterDefinition {
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
}


