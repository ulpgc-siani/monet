package org.monet.space.explorer.control.dialogs.deserializers.field;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Indicator;

public class LinkFieldDeserializer extends AbstractFieldDeserializer {

	public LinkFieldDeserializer() {
	}

	public LinkFieldDeserializer(Helper helper) {
		super(helper);
	}

	@Override
	public void deserializeValue(Attribute attribute, JsonElement value) {
		JsonObject link = (JsonObject) value;

		attribute.addOrSetIndicatorValue(Indicator.CODE, 0, link.get("id").getAsString());
		attribute.addOrSetIndicatorValue(Indicator.VALUE, 0, link.get("label").getAsString());
	}

}
