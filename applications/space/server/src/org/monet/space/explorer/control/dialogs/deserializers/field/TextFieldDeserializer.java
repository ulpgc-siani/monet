package org.monet.space.explorer.control.dialogs.deserializers.field;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Indicator;

import java.util.HashMap;
import java.util.Map;

public class TextFieldDeserializer extends AbstractFieldDeserializer {

    public TextFieldDeserializer() {
    }

    @Override
    public Attribute deserialize(JsonObject object) {
        final Attribute attribute = super.deserialize(object);
        int index = 0;
        for (Map.Entry<String, String> entry : getMetas(object).entrySet())
            attribute.addOrSetIndicatorValue(entry.getKey(), index++, entry.getValue());
        return attribute;
    }

    @Override
    public void deserializeValue(Attribute attribute, JsonElement value) {
        attribute.addOrSetIndicatorValue(Indicator.VALUE, 0, value.getAsString());
    }

    private Map<String, String> getMetas(JsonObject object) {
        if (object.getAsJsonObject("metas") == null)
            return new HashMap<>();
        return new Gson().fromJson(object.getAsJsonObject("metas"), new TypeToken<Map<String, String>>(){}.getType());
    }

}
