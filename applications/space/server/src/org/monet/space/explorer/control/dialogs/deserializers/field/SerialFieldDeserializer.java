package org.monet.space.explorer.control.dialogs.deserializers.field;

import com.google.gson.JsonElement;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Indicator;

public class SerialFieldDeserializer extends AbstractFieldDeserializer {

	public SerialFieldDeserializer() {
	}

	public SerialFieldDeserializer(Helper helper) {
		super(helper);
	}

	@Override
	public void deserializeValue(Attribute attribute, JsonElement value) {
		attribute.addOrSetIndicatorValue(Indicator.VALUE, 0, value.getAsString());
	}

}
