package org.monet.space.explorer.control.displays.serializers.definition.entity.views;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.monet.metamodel.ProcessDefinitionBase;

import java.lang.reflect.Type;

public class ProcessViewDefinitionSerializer extends ViewDefinitionSerializer<ProcessDefinitionBase.ViewProperty> {

	public ProcessViewDefinitionSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serialize(ProcessDefinitionBase.ViewProperty definition, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject)super.serialize(definition, type, jsonSerializationContext);

		if (definition.getLabel() != null)
			result.addProperty("label", helper.getLanguage().getModelResource(definition.getLabel()));

		result.add("show", serializeShow(definition, jsonSerializationContext));

		return result;
	}

	private JsonElement serializeShow(ProcessDefinitionBase.ViewProperty definition, JsonSerializationContext jsonSerializationContext) {

		if (definition.getShow() == null)
			return null;

		JsonObject result = new JsonObject();
		result.addProperty("shortcut", definition.getShow().getShortcut());
		result.addProperty("shortcutView", definition.getShow().getShortcutView());

		return result;
	}

}
