package org.monet.space.explorer.control.displays.serializers.definition.entity;

import com.google.gson.*;
import org.monet.metamodel.AttributeProperty;
import org.monet.metamodel.IndexDefinition;
import org.monet.metamodel.internal.Ref;
import org.monet.space.explorer.control.displays.serializers.definition.DefinitionSerializer;
import org.monet.space.explorer.control.displays.serializers.definition.RefSerializer;
import org.monet.space.explorer.control.displays.serializers.definition.entity.views.ViewDefinitionSerializer;
import org.monet.space.explorer.model.Language;

import java.lang.reflect.Type;

public class IndexDefinitionSerializer extends DefinitionSerializer<IndexDefinition> {

	public IndexDefinitionSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serializeObject(IndexDefinition definition) {
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(IndexDefinition.class, this);
		builder.registerTypeAdapter(Ref.class, new RefSerializer(helper));
		ViewDefinitionSerializer.registerAdapters(builder, helper);
		return builder.create().toJsonTree(definition);
	}

	@Override
	public JsonElement serialize(IndexDefinition definition, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject)super.serialize(definition, type, jsonSerializationContext);

		if (definition.getReference() != null)
			result.add("reference", serializeReference(definition, jsonSerializationContext));

		if (definition.getViewList().size() > 0)
			result.add("views", serializeViews(definition, jsonSerializationContext));

		return result;
	}

	private JsonElement serializeReference(IndexDefinition definition, JsonSerializationContext jsonSerializationContext) {
		JsonArray result = new JsonArray();

		for (AttributeProperty attributeDefinition : definition.getReference().getAttributePropertyList())
			result.add(serializeAttributeDefinition(attributeDefinition, jsonSerializationContext));

		return result;
	}

	private JsonObject serializeAttributeDefinition(AttributeProperty attributeDefinition, JsonSerializationContext jsonSerializationContext) {
		Language language = helper.getLanguage();
		JsonObject result = new JsonObject();

		result.addProperty("code", attributeDefinition.getCode());
		result.addProperty("name", attributeDefinition.getName());
		result.addProperty("label", language.getModelResource(attributeDefinition.getLabel()));
		result.addProperty("description", language.getModelResource(attributeDefinition.getDescription()));
		result.addProperty("type", attributeDefinition.getType().toString());

		if (attributeDefinition.getPrecision() != null)
			result.addProperty("precision", attributeDefinition.getPrecision().toString());

		if (attributeDefinition.getSource() != null)
			result.add("source", jsonSerializationContext.serialize(attributeDefinition.getSource(), attributeDefinition.getSource().getClass()));

		return result;
	}

	private JsonObject serializeViews(IndexDefinition definition, JsonSerializationContext jsonSerializationContext) {
		JsonArray result = new JsonArray();

		for (IndexDefinition.IndexViewProperty viewDefinition : definition.getViewList())
			result.add(jsonSerializationContext.serialize(viewDefinition, viewDefinition.getClass()));

		return toListObject(result);
	}


}
