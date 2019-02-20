package org.monet.space.explorer.control.displays.serializers.field;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.monet.metamodel.FieldProperty;
import org.monet.space.explorer.control.displays.serializers.SourceSerializer;
import org.monet.space.explorer.control.displays.serializers.field.value.SelectFieldValueSerializer;
import org.monet.space.kernel.model.Field;
import org.monet.space.kernel.model.Source;

public class SelectFieldSerializer extends AbstractFieldSerializer<FieldProperty> {

	@Override
	public JsonElement serializeValue(Field field, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject) new SelectFieldValueSerializer(helper).serialize(field, jsonSerializationContext);

		Source source = loadSource(field.getAttribute());
		if (source != null)
			result.add("source", new SourceSerializer(helper).serialize(source, source.getClass(), jsonSerializationContext));

		return result;
	}
}
