package org.monet.space.explorer.control.displays.serializers.definition.entity.fields;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.monet.metamodel.DateFieldProperty;
import org.monet.space.explorer.control.displays.serializers.ExplorerSerializer;

import java.lang.reflect.Type;

public class DateFieldDefinitionSerializer extends FieldDefinitionSerializer<DateFieldProperty> {

	public DateFieldDefinitionSerializer(ExplorerSerializer.Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serialize(DateFieldProperty definition, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject) super.serialize(definition, type, jsonSerializationContext);

		if (definition.getPrecision() != null)
			result.addProperty("precision", definition.getPrecision().toString());

		if (definition.allowLessPrecision())
			result.addProperty("allowLessPrecision", definition.allowLessPrecision());

		if (definition.getPurpose() != null)
			result.addProperty("purpose", definition.getPurpose().toString());

		result.add("range", serializeRange(definition.getRange(), jsonSerializationContext));

		return result;
	}

	private JsonElement serializeRange(DateFieldProperty.RangeProperty rangeDefinition, JsonSerializationContext jsonSerializationContext) {
		if (rangeDefinition == null)
			return null;

		JsonObject result = new JsonObject();
		result.addProperty("min", rangeDefinition.getMin());
		result.addProperty("max", rangeDefinition.getMax());

		return result;
	}

}