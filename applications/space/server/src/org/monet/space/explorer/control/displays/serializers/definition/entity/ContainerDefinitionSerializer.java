package org.monet.space.explorer.control.displays.serializers.definition.entity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.monet.metamodel.ContainerDefinition;

import java.lang.reflect.Type;

public class ContainerDefinitionSerializer extends NodeDefinitionSerializer<ContainerDefinition> {

	public ContainerDefinitionSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serialize(ContainerDefinition definition, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject) super.serialize(definition, type, jsonSerializationContext);

		result.addProperty("environment", definition.isEnvironment());

		return result;
	}
}
