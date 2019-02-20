package org.monet.space.explorer.control.dialogs.deserializers.field;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Indicator;

public class SelectFieldDeserializer extends AbstractFieldDeserializer {

	public SelectFieldDeserializer() {
	}

	public SelectFieldDeserializer(Helper helper) {
		super(helper);
	}

	@Override
	public void deserializeValue(Attribute attribute, JsonElement value) {
		JsonObject data = (JsonObject) value;

		Attribute optionAttribute = new Attribute();
		optionAttribute.setCode(Attribute.OPTION);
		optionAttribute.addOrSetIndicatorValue(Indicator.CODE, 0, data.get("value").getAsString());

		deserializeLabel(data, optionAttribute);

		if (data.get("source") != null)
			optionAttribute.addOrSetIndicatorValue(Indicator.SOURCE, 0, data.get("source").getAsString());

		attribute.getAttributeList().add(optionAttribute);
	}

	private void deserializeLabel(JsonObject data, Attribute optionAttribute) {
		boolean other = data.get("other").getAsBoolean();
		String label = data.get("label").getAsString();

		if (other) {
			optionAttribute.addOrSetIndicatorValue(Indicator.VALUE, 0, "");
			optionAttribute.addOrSetIndicatorValue(Indicator.OTHER, 0, label);
			return;
		}

		optionAttribute.addOrSetIndicatorValue(Indicator.VALUE, 0, label);
		optionAttribute.addOrSetIndicatorValue(Indicator.OTHER, 0, "");
	}

}
