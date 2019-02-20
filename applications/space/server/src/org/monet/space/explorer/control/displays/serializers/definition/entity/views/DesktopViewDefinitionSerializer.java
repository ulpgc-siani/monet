package org.monet.space.explorer.control.displays.serializers.definition.entity.views;

import com.google.gson.*;
import org.monet.metamodel.DesktopDefinition;
import org.monet.metamodel.internal.Ref;

import java.lang.reflect.Type;

public class DesktopViewDefinitionSerializer extends NodeViewDefinitionSerializer<DesktopDefinition.ViewProperty> {

	public DesktopViewDefinitionSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serialize(DesktopDefinition.ViewProperty definition, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject)super.serialize(definition, type, jsonSerializationContext);

		result.add("show", serializeShow(definition, jsonSerializationContext));

		return result;
	}

	private JsonElement serializeShow(DesktopDefinition.ViewProperty definition, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();
		JsonArray links = new JsonArray();

		for (Ref linkRef : definition.getShow().getLink())
			links.add(jsonSerializationContext.serialize(linkRef, linkRef.getClass()));

		result.add("links", toListObject(links));

		return result;
	}

}
