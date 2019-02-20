package client.core.system.definition.views;

import client.core.model.List;
import client.core.model.definition.Ref;
import client.core.system.MonetList;

public class IndexViewDefinition extends ViewDefinition implements client.core.model.definition.views.IndexViewDefinition {
	private client.core.model.definition.views.IndexViewDefinition.ShowDefinition show;

	@Override
	public client.core.model.definition.views.IndexViewDefinition.ShowDefinition getShow() {
		return show;
	}

	@Override
	public List<Ref> getAttributes() {
		List<Ref> attributes = new MonetList<>();
		if (getShow().getTitle() != null) attributes.add(getShow().getTitle());
		if (getShow().getLines() != null) attributes.addAll(getShow().getLines());
		if (getShow().getLinesBelow() != null) attributes.addAll(getShow().getLinesBelow());
		if (getShow().getHighlight() != null) attributes.addAll(getShow().getHighlight());
		if (getShow().getFooter() != null) attributes.addAll(getShow().getFooter());
		if (getShow().getIcon() != null) attributes.add(getShow().getIcon());
		if (getShow().getPicture() != null) attributes.add(getShow().getPicture());
		return attributes;
	}

	public void setShow(client.core.model.definition.views.IndexViewDefinition.ShowDefinition show) {
		this.show = show;
	}

	public static class ShowDefinition implements client.core.model.definition.views.IndexViewDefinition.ShowDefinition {
		private client.core.model.definition.Ref title;
		private List<client.core.model.definition.Ref> lines;
		private List<client.core.model.definition.Ref> linesBelow;
		private List<client.core.model.definition.Ref> highlight;
		private List<client.core.model.definition.Ref> footer;
		private client.core.model.definition.Ref icon;
		private client.core.model.definition.Ref picture;

		@Override
		public client.core.model.definition.Ref getTitle() {
			return title;
		}

		public void setTitle(client.core.model.definition.Ref title) {
			this.title = title;
		}

		@Override
		public List<client.core.model.definition.Ref> getLines() {
			return lines;
		}

		public void setLines(List<client.core.model.definition.Ref> lines) {
			this.lines = lines;
		}

		@Override
		public List<client.core.model.definition.Ref> getLinesBelow() {
			return linesBelow;
		}

		public void setLinesBelow(List<client.core.model.definition.Ref> linesBelow) {
			this.linesBelow = linesBelow;
		}

		@Override
		public List<client.core.model.definition.Ref> getHighlight() {
			return highlight;
		}

		public void setHighlight(List<client.core.model.definition.Ref> highlight) {
			this.highlight = highlight;
		}

		@Override
		public List<client.core.model.definition.Ref> getFooter() {
			return footer;
		}

		public void setFooter(List<client.core.model.definition.Ref> footer) {
			this.footer = footer;
		}

		@Override
		public client.core.model.definition.Ref getIcon() {
			return icon;
		}

		public void setIcon(client.core.model.definition.Ref icon) {
			this.icon = icon;
		}

		@Override
		public client.core.model.definition.Ref getPicture() {
			return picture;
		}

		public void setPicture(client.core.model.definition.Ref picture) {
			this.picture = picture;
		}
	}
}
