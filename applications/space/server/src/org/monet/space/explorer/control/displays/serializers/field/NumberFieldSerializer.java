package org.monet.space.explorer.control.displays.serializers.field;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import org.monet.space.explorer.control.displays.serializers.field.value.NumberFieldValueSerializer;
import org.monet.space.kernel.model.Field;

public class NumberFieldSerializer extends SimpleFieldSerializer {

	@Override
	public JsonElement serializeValue(Field field, JsonSerializationContext jsonSerializationContext) {
		return new NumberFieldValueSerializer(helper).serialize(field, jsonSerializationContext);
	}

}
