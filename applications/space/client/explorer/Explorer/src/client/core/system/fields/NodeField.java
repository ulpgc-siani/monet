package client.core.system.fields;

import client.core.model.definition.entity.field.NodeFieldDefinition;
import client.core.model.Node;

public class NodeField extends Field<NodeFieldDefinition, Node> implements client.core.model.fields.NodeField {
	private Node value;

	public NodeField() {
		super(Type.NODE);
	}

	public NodeField(String code, String label) {
		super(code, label, Type.NODE);
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.fields.NodeField.CLASS_NAME;
	}

	@Override
	public Node getValue() {
		return this.value;
	}

	@Override
	public String getValueAsString() {
		return getValue().getId();
	}

	@Override
	public void setValue(Node value) {
		this.value = value;
	}

	@Override
	public final boolean isMultiple() {
		return false;
	}

	@Override
	public final boolean isNullOrEmpty() {
		Node value = this.getValue();
		return value == null || value.getId() == null || value.getId().isEmpty();
	}

}
