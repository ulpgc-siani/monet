package org.monet.space.explorer.control.dialogs.deserializers.field;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Indicator;

public class CheckFieldDeserializer extends AbstractFieldDeserializer {

	public CheckFieldDeserializer() {
	}

	public CheckFieldDeserializer(Helper helper) {
		super(helper);
	}

	@Override
	public void deserializeValue(Attribute attribute, JsonElement value) {
		JsonObject data = (JsonObject) value;

		deserializeCheckList(attribute, data.get("terms").getAsJsonArray());

		if (data.get("source") != null)
			attribute.addOrSetIndicatorValue(Indicator.SOURCE, 0, data.get("source").getAsString());
	}

	private void deserializeCheckList(Attribute attribute, JsonArray checkList) {
		for (int i = 0; i < checkList.size(); i++) {
			JsonObject term = (JsonObject) checkList.get(i);
			addTerm(attribute, term);
		}
	}

	private void addTerm(Attribute attribute, JsonObject term) {
		Attribute optionAttribute = new Attribute();

		optionAttribute.setCode(Attribute.OPTION);
		optionAttribute.addOrSetIndicatorValue(Indicator.CODE, 0, term.get("value").getAsString());
		optionAttribute.addOrSetIndicatorValue(Indicator.VALUE, 0, term.get("label").getAsString());
		optionAttribute.addOrSetIndicatorValue(Indicator.CHECKED, 0, term.get("checked").getAsString());

		attribute.getAttributeList().add(optionAttribute);
	}

}
