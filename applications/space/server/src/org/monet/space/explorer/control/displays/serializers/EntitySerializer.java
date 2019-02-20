package org.monet.space.explorer.control.displays.serializers;

import com.google.gson.*;
import org.monet.metamodel.Definition;
import org.monet.space.kernel.model.DefinitionType;
import org.monet.space.kernel.model.Entity;

import java.lang.reflect.Type;

public class EntitySerializer<T extends Entity, D extends Definition> extends AbstractSerializer<T> implements JsonSerializer<T> {

	public EntitySerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serializeObject(T entity) {
		GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
		builder.registerTypeHierarchyAdapter(Entity.class, this);
		return builder.create().toJsonTree(entity);
	}

	@Override
	public JsonElement serialize(T entity, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();
		DefinitionType definitionType = ((D)entity.getDefinition()).getType();

		result.addProperty("id", entity.getId());
		result.addProperty("label", entity.getLabel());
		result.addProperty("type", definitionType.toString());

		String description = entity.getDescription();
		if (description != null && !description.isEmpty())
			result.addProperty("description", description);

		result.add("definition", serializeDefinition((D)entity.getDefinition()));

		return result;
	}

	protected static JsonObject serializeDefinition(Definition definition) {
		JsonObject result = new JsonObject();
		DefinitionType definitionType = definition.getType();

		result.addProperty("code", definition.getCode());
		result.addProperty("name", definition.getName());
		result.addProperty("type", definitionType.toString());

		return result;
	}

}
