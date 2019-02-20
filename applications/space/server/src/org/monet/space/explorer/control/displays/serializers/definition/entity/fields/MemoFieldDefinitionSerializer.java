package org.monet.space.explorer.control.displays.serializers.definition.entity.fields;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.monet.metamodel.MemoFieldProperty;
import org.monet.space.explorer.control.displays.serializers.ExplorerSerializer;

import java.lang.reflect.Type;

public class MemoFieldDefinitionSerializer extends FieldDefinitionSerializer<MemoFieldProperty> {

	public MemoFieldDefinitionSerializer(ExplorerSerializer.Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serialize(MemoFieldProperty definition, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject) super.serialize(definition, type, jsonSerializationContext);

		result.add("allowHistory", serializeAllowHistory(definition, jsonSerializationContext));
		result.add("length", serializeLength(definition, jsonSerializationContext));
		result.add("edition", serializeEdition(definition, jsonSerializationContext));

		return result;
	}

	private JsonElement serializeAllowHistory(MemoFieldProperty definition, JsonSerializationContext jsonSerializationContext) {
		if (definition.getEnableHistory() == null)
			return null;

		JsonObject result = new JsonObject();
		result.addProperty("dataStore", definition.getEnableHistory().getDatastore());

		return result;
	}

	private JsonElement serializeLength(MemoFieldProperty definition, JsonSerializationContext jsonSerializationContext) {
		if (definition.getLength() == null)
			return null;

		JsonObject result = new JsonObject();
		result.addProperty("max", definition.getLength().getMax());

		return result;
	}

	private JsonElement serializeEdition(MemoFieldProperty definition, JsonSerializationContext jsonSerializationContext) {
		if (definition.getEdition() == null)
			return null;

		JsonObject result = new JsonObject();
		result.addProperty("mode", definition.getEdition().getMode().toString());

		return result;
	}

}