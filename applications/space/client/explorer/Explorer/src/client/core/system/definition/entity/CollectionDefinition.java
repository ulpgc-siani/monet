package client.core.system.definition.entity;

import client.core.model.List;
import client.core.model.definition.Ref;

public class CollectionDefinition extends SetDefinition implements client.core.model.definition.entity.CollectionDefinition {
	private client.core.model.definition.entity.CollectionDefinition.AddDefinition addDefinition;

	@Override
	public client.core.model.definition.entity.CollectionDefinition.AddDefinition getAdd() {
		return addDefinition;
	}

	public void setAddDefinition(client.core.model.definition.entity.CollectionDefinition.AddDefinition addDefinition) {
		this.addDefinition = addDefinition;
	}

	public static class AddDefinition implements client.core.model.definition.entity.CollectionDefinition.AddDefinition {
		private List<Ref> node;

		@Override
		public List<Ref> getNode() {
			return node;
		}

		public void setNode(List<Ref> node) {
			this.node = node;
		}
	}
}
