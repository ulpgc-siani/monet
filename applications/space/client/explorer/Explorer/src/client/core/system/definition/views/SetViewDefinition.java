package client.core.system.definition.views;

import client.core.model.List;
import client.core.model.definition.Ref;

public class SetViewDefinition extends NodeViewDefinition implements client.core.model.definition.views.SetViewDefinition {
	private client.core.model.definition.views.SetViewDefinition.ShowDefinition show;
	private client.core.model.definition.views.SetViewDefinition.AnalyzeDefinition analyze;
	private client.core.model.definition.views.SetViewDefinition.SelectDefinition select;

	@Override
	public client.core.model.definition.views.SetViewDefinition.ShowDefinition getShow() {
		return show;
	}

	public void setShow(client.core.model.definition.views.SetViewDefinition.ShowDefinition show) {
		this.show = show;
	}

	@Override
	public client.core.model.definition.views.SetViewDefinition.AnalyzeDefinition getAnalyze() {
		return analyze;
	}

	public void setAnalyze(client.core.model.definition.views.SetViewDefinition.AnalyzeDefinition analyze) {
		this.analyze = analyze;
	}

	@Override
	public client.core.model.definition.views.SetViewDefinition.SelectDefinition getSelect() {
		return select;
	}

	public void setSelect(client.core.model.definition.views.SetViewDefinition.SelectDefinition select) {
		this.select = select;
	}

	public static class ShowDefinition implements client.core.model.definition.views.SetViewDefinition.ShowDefinition {
		private client.core.model.definition.views.SetViewDefinition.ShowDefinition.IndexDefinition index;
		private client.core.model.definition.views.SetViewDefinition.ShowDefinition.ItemsDefinition items;

		@Override
		public client.core.model.definition.views.SetViewDefinition.ShowDefinition.IndexDefinition getIndex() {
			return index;
		}

		public void setIndex(client.core.model.definition.views.SetViewDefinition.ShowDefinition.IndexDefinition index) {
			this.index = index;
		}

		@Override
		public client.core.model.definition.views.SetViewDefinition.ShowDefinition.ItemsDefinition getItems() {
			return items;
		}

		public void setItems(client.core.model.definition.views.SetViewDefinition.ShowDefinition.ItemsDefinition items) {
			this.items = items;
		}

		public static class IndexDefinition implements client.core.model.definition.views.SetViewDefinition.ShowDefinition.IndexDefinition {
			private String withView;
			private String sortBy;
			private String sortMode;

			@Override
			public String getWithView() {
				return withView;
			}

			public void setWithView(String withView) {
				this.withView = withView;
			}

			@Override
			public String getSortBy() {
				return sortBy;
			}

			public void setSortBy(String sortBy) {
				this.sortBy = sortBy;
			}

			@Override
			public String getSortMode() {
				return sortMode;
			}

			public void setSortMode(String sortMode) {
				this.sortMode = sortMode;
			}
		}

		public static class ItemsDefinition implements client.core.model.definition.views.SetViewDefinition.ShowDefinition.ItemsDefinition {
		}

	}

	public static class AnalyzeDefinition implements client.core.model.definition.views.SetViewDefinition.AnalyzeDefinition {
		private client.core.model.definition.views.SetViewDefinition.AnalyzeDefinition.DimensionDefinition dimension;
		private client.core.model.definition.views.SetViewDefinition.AnalyzeDefinition.SortingDefinition sorting;

		@Override
		public client.core.model.definition.views.SetViewDefinition.AnalyzeDefinition.DimensionDefinition getDimension() {
			return dimension;
		}

		public void setDimension(client.core.model.definition.views.SetViewDefinition.AnalyzeDefinition.DimensionDefinition dimension) {
			this.dimension = dimension;
		}

		@Override
		public client.core.model.definition.views.SetViewDefinition.AnalyzeDefinition.SortingDefinition getSorting() {
			return sorting;
		}

		public void setSorting(client.core.model.definition.views.SetViewDefinition.AnalyzeDefinition.SortingDefinition sorting) {
			this.sorting = sorting;
		}

		public static class DimensionDefinition implements client.core.model.definition.views.SetViewDefinition.AnalyzeDefinition.DimensionDefinition {
			private List<Ref> attributes;

			@Override
			public List<Ref> getAttribute() {
				return attributes;
			}

			public void setAttributes(List<Ref> attributes) {
				this.attributes = attributes;
			}
		}

		public static class SortingDefinition implements client.core.model.definition.views.SetViewDefinition.AnalyzeDefinition.SortingDefinition {
			private List<Ref> attributes;

			@Override
			public List<Ref> getAttribute() {
				return attributes;
			}

			public void setAttributes(List<Ref> attributes) {
				this.attributes = attributes;
			}
		}
	}

	public static class SelectDefinition implements client.core.model.definition.views.SetViewDefinition.SelectDefinition {
		private List<Ref> nodes;

		@Override
		public List<Ref> getNode() {
			return nodes;
		}

		public void setNodes(List<Ref> nodes) {
			this.nodes = nodes;
		}
	}
}
