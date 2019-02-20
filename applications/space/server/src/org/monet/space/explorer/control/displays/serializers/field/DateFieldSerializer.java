package org.monet.space.explorer.control.displays.serializers.field;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import org.monet.space.kernel.library.LibraryDate;
import org.monet.space.kernel.model.Field;
import org.monet.space.kernel.model.Indicator;

public class DateFieldSerializer extends SimpleFieldSerializer {

    @Override
    public JsonElement serialize(Field field, JsonSerializationContext jsonSerializationContext) {
        final JsonObject result = (JsonObject) super.serialize(field, jsonSerializationContext);
        result.addProperty("formattedValue", getIndicatorValue(field.getAttribute(), Indicator.VALUE));
        return result;
    }

    @Override
	public JsonElement serializeValue(Field field, JsonSerializationContext jsonSerializationContext) {
        String internalValue = getIndicatorValue(field.getAttribute(), Indicator.INTERNAL);

		if (internalValue.isEmpty())
			return new JsonPrimitive("");

		return new JsonPrimitive(String.valueOf(LibraryDate.parseDate(internalValue).getTime()));
	}

}
