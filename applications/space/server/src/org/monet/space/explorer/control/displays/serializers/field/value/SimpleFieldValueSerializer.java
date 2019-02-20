package org.monet.space.explorer.control.displays.serializers.field.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import org.monet.space.kernel.model.Field;
import org.monet.space.kernel.model.Indicator;

public class SimpleFieldValueSerializer extends FieldValueSerializer {

    public SimpleFieldValueSerializer(Helper helper) {
        super(helper);
    }

    @Override
    public JsonElement serialize(Field field, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(getIndicatorValue(field.getAttribute(), Indicator.VALUE));
    }
}
