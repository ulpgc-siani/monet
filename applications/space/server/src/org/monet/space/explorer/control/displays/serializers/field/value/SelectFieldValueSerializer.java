package org.monet.space.explorer.control.displays.serializers.field.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Field;
import org.monet.space.kernel.model.Indicator;

public class SelectFieldValueSerializer extends FieldValueSerializer {

    public SelectFieldValueSerializer(Helper helper) {
        super(helper);
    }

    @Override
    public JsonElement serialize(Field field, JsonSerializationContext jsonSerializationContext) {
        JsonObject result = new JsonObject();
        Attribute attribute = field.getAttribute();
        Attribute optionAttribute = null;

        if (attribute != null)
            optionAttribute = attribute.getAttributeList().get(Attribute.OPTION);

        if (optionAttribute != null)
            attribute = optionAttribute;

        result.addProperty("value", getIndicatorValue(attribute, Indicator.CODE));
        result.addProperty("label", serializeLabel(attribute));

        return result;
    }

    private String serializeLabel(Attribute attribute) {
        String label = getIndicatorValue(attribute, Indicator.VALUE);

        if (label.isEmpty())
            label = getIndicatorValue(attribute, Indicator.OTHER);

        return label;
    }
}
