package client.presenters.displays.entity.field;

import client.core.model.definition.entity.field.SummationFieldDefinition;
import client.core.model.Node;
import client.core.model.fields.SerialField;
import client.presenters.displays.entity.FieldDisplay;

public class SerialFieldDisplay extends FieldDisplay<SerialField, SummationFieldDefinition, String> implements IsSerialFieldDisplay {

	public static final Type TYPE = new Type("SerialFieldDisplay", FieldDisplay.TYPE);

	public SerialFieldDisplay(Node node, SerialField field) {
		super(node, field);
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public String getValueAsString() {
		return getValue();
	}

	@Override
	protected String format(String value) {
		return value;
	}

	@Override
	public void addHook(Hook hook) {
		super.addHook(hook);
	}

	@Override
	public boolean isReadonly() {
		return true;
	}

	public interface Hook extends FieldDisplay.Hook {
		void value();
	}
}
