package org.monet.space.explorer.control.displays.serializers.field.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.monet.space.kernel.model.Field;
import org.monet.space.kernel.model.Indicator;

public class DateFieldValueSerializer extends FieldValueSerializer {

    public DateFieldValueSerializer(Helper helper) {
        super(helper);
    }

    @Override
    public JsonElement serialize(Field field, JsonSerializationContext jsonSerializationContext) {
        final JsonObject result = new JsonObject();
        result.addProperty("formattedValue", getIndicatorValue(field.getAttribute(), Indicator.VALUE));
        return result;
    }
}
