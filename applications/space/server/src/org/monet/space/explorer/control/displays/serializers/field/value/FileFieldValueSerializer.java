package org.monet.space.explorer.control.displays.serializers.field.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Field;
import org.monet.space.kernel.model.Indicator;

public class FileFieldValueSerializer extends FieldValueSerializer {

    public FileFieldValueSerializer(Helper helper) {
        super(helper);
    }

    @Override
    public JsonElement serialize(Field field, JsonSerializationContext jsonSerializationContext) {
        Attribute attribute = field.getAttribute();
        String value = getIndicatorValue(attribute, Indicator.VALUE);

        if (value.isEmpty())
            return null;

        JsonObject result = new JsonObject();
        result.addProperty("id", getIndicatorValue(attribute, Indicator.VALUE));
        result.addProperty("label", getIndicatorValue(attribute, Indicator.DETAILS));

        return result;
    }
}
