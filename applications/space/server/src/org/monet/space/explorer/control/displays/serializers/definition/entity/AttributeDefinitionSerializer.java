package org.monet.space.explorer.control.displays.serializers.definition.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.monet.metamodel.AttributeProperty;
import org.monet.space.explorer.control.displays.serializers.ExplorerSerializer;
import org.monet.space.explorer.control.displays.serializers.definition.PropertySerializer;

import java.lang.reflect.Type;

public class AttributeDefinitionSerializer extends PropertySerializer<AttributeProperty> {

	public AttributeDefinitionSerializer(ExplorerSerializer.Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serialize(AttributeProperty definition, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject) super.serialize(definition, type,  jsonSerializationContext);

		result.addProperty("type", definition.getType().toString());

		return result;
	}

}
