package org.monet.space.explorer.control.displays.serializers.definition.entity.views;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.monet.metamodel.ContainerDefinition;
import org.monet.metamodel.internal.Ref;

import java.lang.reflect.Type;

public class ContainerViewDefinitionSerializer extends NodeViewDefinitionSerializer<ContainerDefinition.ViewProperty> {

	public ContainerViewDefinitionSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serialize(ContainerDefinition.ViewProperty definition, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject)super.serialize(definition, type, jsonSerializationContext);

		result.add("show", serializeShow(definition, jsonSerializationContext));

		return result;
	}

	private JsonElement serializeShow(ContainerDefinition.ViewProperty definition, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();
		JsonArray components = new JsonArray();

		for (Ref linkRef : definition.getShow().getComponent())
			components.add(jsonSerializationContext.serialize(linkRef, linkRef.getClass()));

		result.add("components", toListObject(components));

		return result;
	}

}
