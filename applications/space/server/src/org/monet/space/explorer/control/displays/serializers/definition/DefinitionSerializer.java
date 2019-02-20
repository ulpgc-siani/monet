package org.monet.space.explorer.control.displays.serializers.definition;

import com.google.gson.*;
import org.monet.metamodel.Definition;
import org.monet.metamodel.IndexDefinition;
import org.monet.space.explorer.control.displays.serializers.AbstractSerializer;
import org.monet.space.explorer.control.displays.serializers.definition.entity.IndexDefinitionSerializer;
import org.monet.space.explorer.control.displays.serializers.definition.entity.NodeDefinitionSerializer;
import org.monet.space.explorer.control.displays.serializers.definition.entity.TaskDefinitionSerializer;
import org.monet.space.explorer.control.displays.serializers.definition.entity.fields.FieldDefinitionSerializer;

import java.lang.reflect.Type;

public class DefinitionSerializer<T extends Definition> extends AbstractSerializer<T> implements JsonSerializer<T> {

	public DefinitionSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serializeObject(T definition) {
		GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
		builder.registerTypeHierarchyAdapter(Definition.class, this);
		NodeDefinitionSerializer.registerAdapters(builder, helper);
		TaskDefinitionSerializer.registerAdapters(builder, helper);
		FieldDefinitionSerializer.registerAdapters(builder, helper);
		builder.registerTypeHierarchyAdapter(IndexDefinition.class, new IndexDefinitionSerializer(helper));
		return builder.create().toJsonTree(definition);
	}

	@Override
	public JsonElement serialize(T definition, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();

		result.addProperty("code", definition.getCode());
		result.addProperty("name", definition.getName());

		if (definition.getType() != null)
			result.addProperty("type", definition.getType().toString());

		if (definition.getLabel() != null)
			result.addProperty("label", definition.getLabelString());

		if (definition.getDescription() != null)
			result.addProperty("description", definition.getDescriptionString());

		return result;
	}

}
