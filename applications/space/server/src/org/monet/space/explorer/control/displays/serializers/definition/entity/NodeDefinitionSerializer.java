package org.monet.space.explorer.control.displays.serializers.definition.entity;

import com.google.gson.*;
import org.monet.metamodel.*;
import org.monet.space.explorer.control.displays.serializers.definition.DefinitionSerializer;
import org.monet.space.explorer.control.displays.serializers.definition.entity.views.ViewDefinitionSerializer;

import java.lang.reflect.Type;

public class NodeDefinitionSerializer<T extends NodeDefinition> extends DefinitionSerializer<T> {

	public NodeDefinitionSerializer(Helper helper) {
		super(helper);
	}

	public static void registerAdapters(GsonBuilder builder, Helper helper) {
		builder.registerTypeHierarchyAdapter(DesktopDefinition.class, new DesktopDefinitionSerializer(helper));
		builder.registerTypeHierarchyAdapter(ContainerDefinition.class, new ContainerDefinitionSerializer(helper));
		builder.registerTypeHierarchyAdapter(CollectionDefinition.class, new CollectionDefinitionSerializer(helper));
		builder.registerTypeHierarchyAdapter(CatalogDefinition.class, new CatalogDefinitionSerializer(helper));
		builder.registerTypeHierarchyAdapter(FormDefinition.class, new FormDefinitionSerializer(helper));
		builder.registerTypeHierarchyAdapter(DocumentDefinition.class, new DocumentDefinitionSerializer(helper));
		builder.registerTypeHierarchyAdapter(IndexDefinition.class, new IndexDefinitionSerializer(helper));
		ViewDefinitionSerializer.registerAdapters(builder, helper);
	}

	@Override
	public JsonElement serializeObject(T definition) {
		GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
		NodeDefinitionSerializer.registerAdapters(builder, helper);
		return builder.create().toJsonTree(definition, definition.getClass());
	}

	@Override
	public JsonElement serialize(T definition, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject) super.serialize(definition, type, jsonSerializationContext);

		if (definition.getOperationList().size() > 0)
			result.add("operations", serializeOperations(definition, jsonSerializationContext));

		if (definition.getViewDefinitionList().size() > 0)
			result.add("views", serializeViews(definition, jsonSerializationContext));

		if (definition.isReadonly())
			result.addProperty("readonly", definition.isReadonly());

		return result;
	}

	private JsonArray serializeOperations(T definition, JsonSerializationContext jsonSerializationContext) {
		JsonArray result = new JsonArray();

		for (NodeDefinitionBase.OperationProperty operationDefinition : definition.getOperationList())
			result.add(serializeOperation(operationDefinition, jsonSerializationContext));

		return result;
	}

	private JsonObject serializeOperation(NodeDefinitionBase.OperationProperty operationDefinition, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();

		result.addProperty("code", operationDefinition.getCode());
		result.addProperty("name", operationDefinition.getName());
		result.addProperty("label", helper.getLanguage().getModelResource(operationDefinition.getLabel()));
		result.addProperty("description", helper.getLanguage().getModelResource(operationDefinition.getDescription()));
		result.addProperty("enabled", true);
		result.addProperty("visible", true);

		return result;
	}

	private JsonObject serializeViews(T definition, JsonSerializationContext jsonSerializationContext) {
		JsonArray result = new JsonArray();

		for (NodeViewProperty viewDefinition : definition.getViewDefinitionList())
			result.add(jsonSerializationContext.serialize(viewDefinition, viewDefinition.getClass()));

		return toListObject(result);
	}

}
