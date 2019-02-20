package org.monet.space.explorer.control.displays.serializers.field.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import org.monet.space.kernel.model.Field;
import org.monet.space.kernel.model.Indicator;

public class NumberFieldValueSerializer extends FieldValueSerializer {

    public NumberFieldValueSerializer(Helper helper) {
        super(helper);
    }

    @Override
    public JsonElement serialize(Field field, JsonSerializationContext jsonSerializationContext) {
        String indicatorValue = getIndicatorValue(field.getAttribute(), Indicator.INTERNAL);

        if (indicatorValue.isEmpty())
            indicatorValue = "0";

        return new JsonPrimitive(indicatorValue);
    }
}
