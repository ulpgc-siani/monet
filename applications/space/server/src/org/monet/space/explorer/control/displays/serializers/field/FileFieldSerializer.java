package org.monet.space.explorer.control.displays.serializers.field;

import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import org.monet.metamodel.FieldProperty;
import org.monet.space.explorer.control.displays.serializers.field.value.FileFieldValueSerializer;
import org.monet.space.kernel.model.Field;

public class FileFieldSerializer extends AbstractFieldSerializer<FieldProperty> {

	@Override
	public JsonElement serializeValue(Field field, JsonSerializationContext jsonSerializationContext) {
		return new FileFieldValueSerializer(helper).serialize(field, jsonSerializationContext);
	}

}
