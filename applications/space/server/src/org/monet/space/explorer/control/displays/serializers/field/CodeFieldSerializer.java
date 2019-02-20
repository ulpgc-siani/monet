package org.monet.space.explorer.control.displays.serializers.field;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.monet.metamodel.FieldProperty;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Field;
import org.monet.space.kernel.model.Indicator;

public class CodeFieldSerializer<T extends FieldProperty> extends AbstractFieldSerializer<T> {

	@Override
	public JsonElement serializeValue(Field field, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();
		Attribute attribute = field.getAttribute();
		Attribute optionAttribute = null;

		if (attribute != null)
			optionAttribute = attribute.getAttributeList().get(Attribute.OPTION);

		if (optionAttribute != null)
			attribute = optionAttribute;

		result.addProperty("value", getIndicatorValue(attribute, Indicator.CODE));
		result.addProperty("label", getIndicatorValue(attribute, Indicator.VALUE));

		return result;
	}

}
