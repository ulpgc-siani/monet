package client.presenters.displays.entity.field;

import client.core.model.Node;
import client.core.model.definition.entity.field.SerialFieldDefinition;
import client.core.model.fields.MultipleSerialField;
import client.presenters.displays.entity.MultipleFieldDisplay;

public class MultipleSerialFieldDisplay extends MultipleFieldDisplay<MultipleSerialField, SerialFieldDefinition, String> implements IsMultipleSerialFieldDisplay {

    public static final Type TYPE = new Type("MultipleSerialFieldDisplay", MultipleFieldDisplay.TYPE);

    public MultipleSerialFieldDisplay(Node node, MultipleSerialField field) {
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
    public boolean isReadonly() {
        return true;
    }

    @Override
    public void addHook(SerialFieldDisplay.Hook hook) {
        super.addHook(hook);
    }
}
