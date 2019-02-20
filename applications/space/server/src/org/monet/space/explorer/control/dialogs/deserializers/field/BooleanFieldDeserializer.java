package org.monet.space.explorer.control.dialogs.deserializers.field;

import com.google.gson.JsonElement;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Indicator;

public class BooleanFieldDeserializer extends AbstractFieldDeserializer {

	public BooleanFieldDeserializer() {
	}

	public BooleanFieldDeserializer(Helper helper) {
		super(helper);
	}

	@Override
	public void deserializeValue(Attribute attribute, JsonElement value) {
		Attribute optionAttribute = new Attribute();

		optionAttribute.setCode(Attribute.OPTION);
		optionAttribute.addOrSetIndicatorValue(Indicator.CODE, 0, value.getAsString());

		attribute.getAttributeList().add(optionAttribute);
	}

}
