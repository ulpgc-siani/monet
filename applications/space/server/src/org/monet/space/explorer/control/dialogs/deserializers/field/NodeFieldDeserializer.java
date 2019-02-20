package org.monet.space.explorer.control.dialogs.deserializers.field;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Indicator;

public class NodeFieldDeserializer extends AbstractFieldDeserializer {

	public NodeFieldDeserializer() {
	}

	public NodeFieldDeserializer(Helper helper) {
		super(helper);
	}

	@Override
	public void deserializeValue(Attribute attribute, JsonElement value) {
		JsonObject node = (JsonObject) value;

		attribute.addOrSetIndicatorValue(Indicator.CODE, 0, node.get("id").getAsString());
		attribute.addOrSetIndicatorValue(Indicator.NODE, 0, node.get("id").getAsString());
		attribute.addOrSetIndicatorValue(Indicator.VALUE, 0, node.get("label").getAsString());
	}

}
