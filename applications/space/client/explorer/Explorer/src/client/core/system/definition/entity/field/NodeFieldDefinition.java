package client.core.system.definition.entity.field;

import client.core.model.Instance;
import client.core.model.List;
import client.core.system.definition.entity.MultipleableFieldDefinition;

public class NodeFieldDefinition extends MultipleableFieldDefinition implements client.core.model.definition.entity.field.NodeFieldDefinition {
	private client.core.model.definition.entity.field.NodeFieldDefinition.ContainDefinition contain;
	private client.core.model.definition.entity.field.NodeFieldDefinition.AddDefinition add;

	@Override
	public Instance.ClassName getClassName() {
		return client.core.model.definition.entity.field.NodeFieldDefinition.CLASS_NAME;
	}

	@Override
	public client.core.model.definition.entity.field.NodeFieldDefinition.ContainDefinition getContain() {
		return contain;
	}

	public void setContain(client.core.model.definition.entity.field.NodeFieldDefinition.ContainDefinition contain) {
		this.contain = contain;
	}

	@Override
	public client.core.model.definition.entity.field.NodeFieldDefinition.AddDefinition getAdd() {
		return add;
	}

	public void setAdd(client.core.model.definition.entity.field.NodeFieldDefinition.AddDefinition add) {
		this.add = add;
	}

	public static class ContainDefinition implements client.core.model.definition.entity.field.NodeFieldDefinition.ContainDefinition {
		private String node;

		@Override
		public String getNode() {
			return node;
		}

		public void setNode(String node) {
			this.node = node;
		}
	}

	public static class AddDefinition implements client.core.model.definition.entity.field.NodeFieldDefinition.AddDefinition {
		private List<String> nodes;

		@Override
		public List<String> getNodes() {
			return nodes;
		}

		public void setNodes(List<String> nodes) {
			this.nodes = nodes;
		}
	}

}
