package org.monet.space.explorer.control.dialogs.deserializers.field;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Indicator;

public class CompositeFieldDeserializer extends AbstractFieldDeserializer {

	public CompositeFieldDeserializer() {
	}

	public CompositeFieldDeserializer(Helper helper) {
		super(helper);
	}

	@Override
	public void deserializeValue(Attribute attribute, JsonElement value) {
		JsonObject compositeValue = (JsonObject)value;

		if (compositeValue.get("conditioned") != null)
			attribute.addOrSetIndicatorValue(Indicator.CONDITIONED, 0, String.valueOf(compositeValue.get("conditioned").getAsBoolean()));

		attribute.setAttributeList(null);
	}

}
