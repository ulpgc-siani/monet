package org.monet.space.explorer.control.displays.serializers.field.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Field;
import org.monet.space.kernel.model.Indicator;

public class BooleanFieldValueSerializer extends FieldValueSerializer {

    public BooleanFieldValueSerializer(Helper helper) {
        super(helper);
    }

    @Override
    public JsonElement serialize(Field field, JsonSerializationContext jsonSerializationContext) {
        Attribute attribute = field.getAttribute();

        if (attribute == null)
            return new JsonPrimitive(false);

        Attribute optionAttribute = attribute.getAttributeList().get(Attribute.OPTION);
        if (optionAttribute != null)
            attribute = optionAttribute;

        return new JsonPrimitive(Boolean.valueOf(attribute.getIndicatorValue(Indicator.CODE)));
    }
}
