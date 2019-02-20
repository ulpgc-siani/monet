package client.core.system.definition.views;

import client.core.model.List;
import client.core.model.definition.Ref;

public class DesktopViewDefinition extends NodeViewDefinition implements client.core.model.definition.views.DesktopViewDefinition {
	private client.core.model.definition.views.DesktopViewDefinition.ShowDefinition show;

	@Override
	public client.core.model.definition.views.DesktopViewDefinition.ShowDefinition getShow() {
		return show;
	}

	public void setShow(client.core.model.definition.views.DesktopViewDefinition.ShowDefinition show) {
		this.show = show;
	}

	public static class ShowDefinition implements client.core.model.definition.views.DesktopViewDefinition.ShowDefinition {
		private List<Ref> links;

		@Override
		public List<Ref> getLinks() {
			return links;
		}

		public void setLinks(List<Ref> links) {
			this.links = links;
		}
	}
}
