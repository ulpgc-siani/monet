package org.monet.space.explorer.control.displays.serializers.definition.entity.fields;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.monet.metamodel.SerialFieldProperty;
import org.monet.metamodel.SerialFieldPropertyBase;
import org.monet.space.explorer.control.displays.serializers.ExplorerSerializer;

import java.lang.reflect.Type;

public class SerialFieldDefinitionSerializer extends FieldDefinitionSerializer<SerialFieldProperty> {

	public SerialFieldDefinitionSerializer(ExplorerSerializer.Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serialize(SerialFieldProperty definition, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject) super.serialize(definition, type, jsonSerializationContext);

		result.add("serial", serializeSerial(definition, jsonSerializationContext));

		return result;
	}

	private JsonElement serializeSerial(SerialFieldProperty definition, JsonSerializationContext jsonSerializationContext) {
		SerialFieldPropertyBase.SerialProperty serialDefinition = definition.getSerial();
		JsonObject result = new JsonObject();
		result.addProperty("name", serialDefinition.getName());
		result.addProperty("format", serialDefinition.getFormat());
		return result;
	}

}