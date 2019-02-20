package client.core.system.definition.views;

import client.core.model.List;
import client.core.model.definition.Ref;

public class ContainerViewDefinition extends NodeViewDefinition implements client.core.model.definition.views.ContainerViewDefinition {
	private client.core.model.definition.views.ContainerViewDefinition.ShowDefinition show;

	@Override
	public client.core.model.definition.views.ContainerViewDefinition.ShowDefinition getShow() {
		return show;
	}

	public void setShow(client.core.model.definition.views.ContainerViewDefinition.ShowDefinition show) {
		this.show = show;
	}

	public static class ShowDefinition implements client.core.model.definition.views.ContainerViewDefinition.ShowDefinition {
		private List<Ref> component;

		@Override
		public List<Ref> getComponent() {
			return component;
		}

		public void setComponent(List<Ref> component) {
			this.component = component;
		}
	}

}
