package org.monet.space.explorer.control.displays.serializers.field;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import org.monet.metamodel.BooleanFieldProperty;
import org.monet.space.explorer.control.displays.serializers.field.value.BooleanFieldValueSerializer;
import org.monet.space.kernel.model.Field;

public class BooleanFieldSerializer extends AbstractFieldSerializer<BooleanFieldProperty> {

	@Override
	public JsonElement serializeValue(Field field, JsonSerializationContext jsonSerializationContext) {
        return new BooleanFieldValueSerializer(helper).serialize(field, jsonSerializationContext);
	}
}
