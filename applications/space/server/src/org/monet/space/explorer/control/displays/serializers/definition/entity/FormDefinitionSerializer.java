package org.monet.space.explorer.control.displays.serializers.definition.entity;

import com.google.gson.*;
import org.monet.metamodel.FieldProperty;
import org.monet.metamodel.FormDefinition;
import org.monet.space.explorer.control.displays.serializers.definition.entity.fields.FieldDefinitionSerializer;

import java.lang.reflect.Type;

public class FormDefinitionSerializer extends NodeDefinitionSerializer<FormDefinition> {

	public FormDefinitionSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serializeObject(FormDefinition definition) {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(FormDefinition.class, this);
		builder.registerTypeAdapter(FieldProperty.class, new FieldDefinitionSerializer(helper));
		return builder.create().toJsonTree(definition);
	}

	@Override
	public JsonElement serialize(FormDefinition definition, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject)super.serialize(definition, type, jsonSerializationContext);
		JsonArray fieldDefinitions = new JsonArray();

		for (FieldProperty fieldDefinition : definition.getAllFieldPropertyList())
			fieldDefinitions.add(jsonSerializationContext.serialize(fieldDefinition, fieldDefinition.getClass()));

		result.add("fields", toListObject(fieldDefinitions));

		return result;
	}

}
