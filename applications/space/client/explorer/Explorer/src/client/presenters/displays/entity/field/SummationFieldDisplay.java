package client.presenters.displays.entity.field;

import client.core.model.definition.entity.field.SummationFieldDefinition;
import client.core.model.Node;
import client.core.model.fields.SummationField;
import client.presenters.displays.EntityDisplay;
import client.presenters.displays.entity.FieldDisplay;

public class SummationFieldDisplay extends FieldDisplay<SummationField, SummationFieldDefinition, String> implements IsSummationFieldDisplay {

	public static final Type TYPE = new Type("SummationFieldDisplay", FieldDisplay.TYPE);

	public SummationFieldDisplay(Node node, SummationField field) {
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
	protected String format(String s) {
		return s;
	}

	@Override
	public void addHook(Hook hook) {
		super.addHook(hook);
	}

	public interface Hook extends EntityDisplay.Hook {
		void value();
	}
}
