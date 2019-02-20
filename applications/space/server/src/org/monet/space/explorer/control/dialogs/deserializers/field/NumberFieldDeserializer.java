package org.monet.space.explorer.control.dialogs.deserializers.field;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Indicator;

public class NumberFieldDeserializer extends AbstractFieldDeserializer {

	public NumberFieldDeserializer() {
	}

	public NumberFieldDeserializer(Helper helper) {
		super(helper);
	}

	@Override
	public void deserializeValue(Attribute attribute, JsonElement value) {
		JsonObject number = (JsonObject) value;

		attribute.addOrSetIndicatorValue(Indicator.INTERNAL, 0, String.valueOf(number.get("value").getAsDouble()));
		attribute.addOrSetIndicatorValue(Indicator.VALUE, 0, number.get("formattedValue").getAsString());
	}

}
