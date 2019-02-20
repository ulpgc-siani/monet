package client.presenters.displays.entity.field;

import client.core.model.definition.entity.field.NodeFieldDefinition;
import client.core.model.Node;
import client.core.model.fields.NodeField;
import client.presenters.displays.EntityDisplay;
import client.presenters.displays.entity.FieldDisplay;

public class NodeFieldDisplay extends FieldDisplay<NodeField, NodeFieldDefinition, Node> implements IsNodeFieldDisplay {

	public static final Type TYPE = new Type("NodeFieldDisplay", FieldDisplay.TYPE);

	public NodeFieldDisplay(Node node, NodeField field) {
		super(node, field);
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public String getValueAsString() {
		return "";
	}

	@Override
	protected Node format(Node node) {
		return node;
	}

	@Override
	public void addHook(Hook hook) {
		super.addHook(hook);
	}

	public interface Hook extends EntityDisplay.Hook {
		void value();
	}
}
