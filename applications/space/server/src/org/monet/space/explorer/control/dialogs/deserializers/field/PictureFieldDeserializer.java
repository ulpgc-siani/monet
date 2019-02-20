package org.monet.space.explorer.control.dialogs.deserializers.field;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Indicator;

public class PictureFieldDeserializer extends AbstractFieldDeserializer {

	public PictureFieldDeserializer() {
	}

	public PictureFieldDeserializer(Helper helper) {
		super(helper);
	}

	@Override
	public void deserializeValue(Attribute attribute, JsonElement value) {
		JsonObject file = (JsonObject) value;

		attribute.addOrSetIndicatorValue(Indicator.VALUE, 0, file.get("id").getAsString());
		attribute.addOrSetIndicatorValue(Indicator.DETAILS, 0, file.get("label").getAsString());
	}

}
