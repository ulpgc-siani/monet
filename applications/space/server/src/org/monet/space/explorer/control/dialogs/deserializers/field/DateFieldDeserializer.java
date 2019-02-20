package org.monet.space.explorer.control.dialogs.deserializers.field;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Indicator;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFieldDeserializer extends AbstractFieldDeserializer {

	public DateFieldDeserializer() {
	}

	public DateFieldDeserializer(Helper helper) {
		super(helper);
	}

	@Override
	public void deserializeValue(Attribute attribute, JsonElement value) {
		JsonObject date = (JsonObject) value;
		SimpleDateFormat internalFormat = new SimpleDateFormat("dd/MM/yyyy/HH:mm:ss");
		String longValue = date.get("value").getAsString();
		String internalValue = "";

		if (!longValue.isEmpty())
			internalValue = internalFormat.format(new Date(Long.valueOf(longValue)));

		attribute.addOrSetIndicatorValue(Indicator.INTERNAL, 0, internalValue);
		attribute.addOrSetIndicatorValue(Indicator.VALUE, 0, date.get("formattedValue").getAsString());
	}

}
