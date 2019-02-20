package client.core.system.definition.entity;

import client.core.model.List;

public class NodeDefinition<V extends client.core.model.definition.views.NodeViewDefinition> extends EntityDefinition implements client.core.model.definition.entity.NodeDefinition<V> {
	private List<client.core.model.definition.entity.NodeDefinition.OperationDefinition> operations;
	private List<V> views;
	private boolean isReadonly;

	public NodeDefinition() {
	}

	public NodeDefinition(String code, String name, String label, String description) {
		super(code, name, label, description);
	}

	@Override
	public List<client.core.model.definition.entity.NodeDefinition.OperationDefinition> getOperations() {
		return operations;
	}

	public void setOperations(List<client.core.model.definition.entity.NodeDefinition.OperationDefinition> operations) {
		this.operations = operations;
	}

	@Override
	public List<V> getViews() {
		return views;
	}

	@Override
	public V getView(String key) {

		for (V view : getViews()) {
			if (view.getCode().equals(key) || view.getName().equals(key))
				return view;
		}

		return null;
	}

	@Override
	public boolean isReadonly() {
		return isReadonly;
	}

	public void setReadonly(boolean isReadonly) {
		this.isReadonly = isReadonly;
	}

	public void setViews(List<V> views) {
		this.views = views;
	}

	public static class OperationDefinition implements client.core.model.definition.entity.NodeDefinition.OperationDefinition {
		private String code;
		private String name;
		private String label;
		private String description;
		private boolean enabled;
		private boolean visible;

		@Override
		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		@Override
		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		@Override
		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		@Override
		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}

		@Override
		public boolean isVisible() {
			return visible;
		}

		public void setVisible(boolean visible) {
			this.visible = visible;
		}
	}
}
