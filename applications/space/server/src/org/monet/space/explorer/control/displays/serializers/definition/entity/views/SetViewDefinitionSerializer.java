package org.monet.space.explorer.control.displays.serializers.definition.entity.views;

import com.google.gson.*;
import org.monet.metamodel.IndexDefinition;
import org.monet.metamodel.SetDefinition;
import org.monet.metamodel.SetDefinitionBase;
import org.monet.metamodel.internal.Ref;

import java.lang.reflect.Type;

public class SetViewDefinitionSerializer extends NodeViewDefinitionSerializer<SetDefinition.SetViewProperty> {

	public SetViewDefinitionSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serialize(SetDefinition.SetViewProperty definition, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject)super.serialize(definition, type, jsonSerializationContext);

		if (definition.getDesign() != null)
			result.addProperty("design", definition.getDesign().toString());

		result.add("show", serializeShow(definition, jsonSerializationContext));
		result.add("analyze", serializeAnalyze(definition, jsonSerializationContext));
		result.add("select", serializeSelect(definition, jsonSerializationContext));

		return result;
	}

	private JsonElement serializeShow(SetDefinition.SetViewProperty definition, JsonSerializationContext jsonSerializationContext) {
		SetDefinition.SetViewProperty.ShowProperty showDefinition = definition.getShow();
		JsonObject result = new JsonObject();

		result.add("index", serializeIndex(showDefinition, jsonSerializationContext));
		result.add("items", showDefinition.getItems() != null ? new JsonObject() : null);

		return result;
	}

	private JsonElement serializeIndex(SetDefinitionBase.SetViewPropertyBase.ShowProperty showDefinition, JsonSerializationContext jsonSerializationContext) {
		SetDefinitionBase.SetViewPropertyBase.ShowProperty.IndexProperty showIndexDefinition = showDefinition.getIndex();

		if (showIndexDefinition == null)
			return null;

		String indexDefinitionKey = showIndexDefinition.getSortBy().getDefinition();
		IndexDefinition indexDefinition = helper.getDictionary().getIndexDefinition(indexDefinitionKey);

		if (indexDefinition == null)
			return null;

		JsonObject result = new JsonObject();
		result.addProperty("sortBy", indexDefinition.getAttribute(showIndexDefinition.getSortBy().getValue()).getCode());

		if (showIndexDefinition.getSortMode() != null)
			result.addProperty("mode", showIndexDefinition.getSortMode().toString());

		result.addProperty("withView", indexDefinition.getViewMap().get(showIndexDefinition.getWithView().getValue()).getCode());

		return result;
	}

	private JsonElement serializeAnalyze(SetDefinition.SetViewProperty definition, JsonSerializationContext jsonSerializationContext) {
		SetDefinition.SetViewProperty.AnalyzeProperty analyzeDefinition = definition.getAnalyze();

		if (analyzeDefinition == null)
			return null;

		JsonObject result = new JsonObject();
		result.add("dimension", serializeAnalyzeDimension(analyzeDefinition, jsonSerializationContext));
		result.add("sorting", serializeAnalyzeSorting(analyzeDefinition, jsonSerializationContext));

		return result;
	}

	private JsonElement serializeAnalyzeDimension(SetDefinition.SetViewProperty.AnalyzeProperty definition, JsonSerializationContext jsonSerializationContext) {

		if (definition.getDimension() == null)
			return null;

		JsonObject result = new JsonObject();
		JsonArray attributes = new JsonArray();

		for (Ref ref : definition.getDimension().getAttribute())
			attributes.add(jsonSerializationContext.serialize(ref, ref.getClass()));

		result.add("attributes", toListObject(attributes));

		return result;
	}

	private JsonElement serializeAnalyzeSorting(SetDefinition.SetViewProperty.AnalyzeProperty definition, JsonSerializationContext jsonSerializationContext) {

		if (definition.getSorting() == null)
			return null;

		JsonObject result = new JsonObject();
		JsonArray attributes = new JsonArray();

		for (Ref ref : definition.getSorting().getAttribute())
			attributes.add(jsonSerializationContext.serialize(ref, ref.getClass()));

		result.add("attributes", toListObject(attributes));

		return result;
	}

	private JsonElement serializeSelect(SetDefinition.SetViewProperty definition, JsonSerializationContext jsonSerializationContext) {

		if (definition.getSelect() == null)
			return null;

		JsonObject result = new JsonObject();
		JsonArray nodes = new JsonArray();

		for (Ref ref : definition.getSelect().getNode())
			nodes.add(jsonSerializationContext.serialize(ref, ref.getClass()));

		result.add("nodes", toListObject(nodes));

		return result;
	}

}
