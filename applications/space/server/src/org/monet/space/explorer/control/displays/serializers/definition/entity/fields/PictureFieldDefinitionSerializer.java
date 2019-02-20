package org.monet.space.explorer.control.displays.serializers.definition.entity.fields;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.monet.metamodel.PictureFieldProperty;
import org.monet.space.explorer.control.displays.serializers.ExplorerSerializer;

import java.lang.reflect.Type;

public class PictureFieldDefinitionSerializer extends FieldDefinitionSerializer<PictureFieldProperty> {

	public PictureFieldDefinitionSerializer(ExplorerSerializer.Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serialize(PictureFieldProperty definition, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject) super.serialize(definition, type, jsonSerializationContext);

		result.addProperty("defaultValue", definition.getDefault());

		if (definition.getLimit() != null)
			result.addProperty("limit", definition.getLimit());

		if (definition.isProfilePhoto())
			result.addProperty("isProfilePhoto", definition.isProfilePhoto());

		result.add("size", serializeSize(definition, jsonSerializationContext));

		return result;
	}

	private JsonElement serializeSize(PictureFieldProperty definition, JsonSerializationContext jsonSerializationContext) {
		PictureFieldProperty.SizeProperty sizeDefinition = definition.getSize();

		if (sizeDefinition == null)
			return null;

		JsonObject result = new JsonObject();
		result.addProperty("width", sizeDefinition.getWidth());
		result.addProperty("height", sizeDefinition.getHeight());

		return result;
	}
}