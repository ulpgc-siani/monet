package org.monet.space.explorer.control.displays.serializers.definition.entity.views;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.monet.metamodel.NodeViewProperty;

import java.lang.reflect.Type;

public class NodeViewDefinitionSerializer<T extends NodeViewProperty> extends ViewDefinitionSerializer<T> {

	public NodeViewDefinitionSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serialize(T definition, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject)super.serialize(definition, type, jsonSerializationContext);

		if (definition.getLabel() != null)
			result.addProperty("label", helper.getLanguage().getModelResource(definition.getLabel()));

		result.addProperty("default", definition.isDefault());

		return result;
	}

}
