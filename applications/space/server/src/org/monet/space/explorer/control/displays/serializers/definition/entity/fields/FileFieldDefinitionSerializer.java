package org.monet.space.explorer.control.displays.serializers.definition.entity.fields;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.monet.metamodel.FileFieldProperty;
import org.monet.space.explorer.control.displays.serializers.ExplorerSerializer;

import java.lang.reflect.Type;

public class FileFieldDefinitionSerializer extends FieldDefinitionSerializer<FileFieldProperty> {

	public FileFieldDefinitionSerializer(ExplorerSerializer.Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serialize(FileFieldProperty definition, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject) super.serialize(definition, type, jsonSerializationContext);

		if (definition.getLimit() != null)
			result.addProperty("limit", definition.getLimit());

		return result;
	}

}