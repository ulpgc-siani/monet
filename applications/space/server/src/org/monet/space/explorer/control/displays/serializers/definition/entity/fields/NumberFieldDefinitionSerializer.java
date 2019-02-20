package org.monet.space.explorer.control.displays.serializers.definition.entity.fields;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.monet.metamodel.NumberFieldProperty;
import org.monet.space.explorer.control.displays.serializers.ExplorerSerializer;

import java.lang.reflect.Type;

public class NumberFieldDefinitionSerializer extends FieldDefinitionSerializer<NumberFieldProperty> {

	public NumberFieldDefinitionSerializer(ExplorerSerializer.Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serialize(NumberFieldProperty definition, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject) super.serialize(definition, type, jsonSerializationContext);

		result.addProperty("format", definition.getFormat());
		result.add("range", serializeRange(definition, jsonSerializationContext));
		result.addProperty("edition", definition.getEdition() != null ? definition.getEdition().toString() : null);

		return result;
	}

	private JsonElement serializeRange(NumberFieldProperty definition, JsonSerializationContext jsonSerializationContext) {
		NumberFieldProperty.RangeProperty rangeDefinition = definition.getRange();

		if (rangeDefinition == null)
			return null;

		JsonObject result = new JsonObject();
		result.addProperty("min", rangeDefinition.getMin());
		result.addProperty("max", rangeDefinition.getMax());

		return result;
	}

}