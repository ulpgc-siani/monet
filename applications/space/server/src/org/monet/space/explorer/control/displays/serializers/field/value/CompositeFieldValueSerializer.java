package org.monet.space.explorer.control.displays.serializers.field.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.monet.space.kernel.model.Field;
import org.monet.space.kernel.model.Indicator;

public class CompositeFieldValueSerializer extends FieldValueSerializer {

    public CompositeFieldValueSerializer(Helper helper) {
        super(helper);
    }

    @Override
    public JsonElement serialize(Field field, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        String conditioned = field.getAttribute().getIndicatorValue(Indicator.CONDITIONED);
        result.addProperty("conditioned", (conditioned != null && !conditioned.isEmpty()) ? Boolean.valueOf(conditioned) : false);
        return result;
    }
}
