package org.monet.space.explorer.control.displays.serializers.field.value;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import org.monet.space.explorer.control.displays.serializers.ExplorerSerializer;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Field;

import java.util.HashMap;
import java.util.Map;

public abstract class FieldValueSerializer extends ExplorerSerializer {

    private static Map<String, FieldValueSerializer> serializers;

    public FieldValueSerializer(Helper helper) {
        super(helper);
    }

    public static String serialize(Helper helper, Field field, JsonSerializationContext context, String type) {
        if (serializers == null)
            addSerializers(helper);
        if (!serializers.containsKey(type))
            return "";
        return serializers.get(type).serialize(field, context).toString();
    }

    public abstract JsonElement serialize(Field field, JsonSerializationContext context);

    protected String getIndicatorValue(Attribute attribute, String code) {
        if (attribute == null)
            return "";
        return attribute.getIndicatorValue(code);
    }

    private static void addSerializers(Helper helper) {
        serializers = new HashMap<>();
        serializers.put("boolean", new BooleanFieldValueSerializer(helper));
        serializers.put("check", new CheckFieldValueSerializer(helper));
        serializers.put("composite", new CompositeFieldValueSerializer(helper));
        serializers.put("date", new DateFieldValueSerializer(helper));
        serializers.put("file", new FileFieldValueSerializer(helper));
        serializers.put("link", new LinkFieldValueSerializer(helper));
        serializers.put("memo", new SimpleFieldValueSerializer(helper));
        serializers.put("number", new NumberFieldValueSerializer(helper));
        serializers.put("picture", new FileFieldValueSerializer(helper));
        serializers.put("select", new SelectFieldValueSerializer(helper));
        serializers.put("serial", new SimpleFieldValueSerializer(helper));
        serializers.put("text", new SimpleFieldValueSerializer(helper));
    }
}
