package org.monet.space.explorer.control.displays.serializers.definition.entity.fields;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.monet.metamodel.IndexDefinition;
import org.monet.metamodel.LinkFieldProperty;
import org.monet.space.explorer.control.displays.serializers.ExplorerSerializer;

import java.lang.reflect.Type;

public class LinkFieldDefinitionSerializer extends FieldDefinitionSerializer<LinkFieldProperty> {

	public LinkFieldDefinitionSerializer(ExplorerSerializer.Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serialize(LinkFieldProperty definition, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject) super.serialize(definition, type, jsonSerializationContext);

		result.add("source", serializeSource(definition, jsonSerializationContext));
		result.add("allowHistory", serializeAllowHistory(definition));
		result.addProperty("allowAdd", definition.allowAdd());
		result.addProperty("allowSearch", definition.allowSearch());
		result.addProperty("allowEdit", definition.allowEdit());

		return result;
	}

	private JsonElement serializeSource(LinkFieldProperty definition, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();
		LinkFieldProperty.SourceProperty sourceDefinition = definition.getSource();
		IndexDefinition indexDefinition = helper.getDictionary().getIndexDefinition(sourceDefinition.getIndex().getValue());

		result.addProperty("index", indexDefinition.getCode());
		result.add("filters", serializeFilters(indexDefinition, sourceDefinition, jsonSerializationContext));

		if (sourceDefinition.getCollection() != null)
			result.addProperty("collection", helper.getDictionary().getDefinitionCode(sourceDefinition.getCollection().getValue()));

		if (sourceDefinition.getView() != null)
			result.addProperty("view", indexDefinition.getViewMap().get(sourceDefinition.getView().getValue()).getCode());

		return result;
	}

	private JsonElement serializeAllowHistory(LinkFieldProperty definition) {

		if (definition.getEnableHistory() == null)
			return null;

		JsonObject result = new JsonObject();
		result.addProperty("dataStore", definition.getEnableHistory().getDatastore());

		return result;
	}

	private JsonElement serializeFilters(IndexDefinition indexDefinition, LinkFieldProperty.SourceProperty sourceDefinition, JsonSerializationContext jsonSerializationContext) {
		JsonArray result = new JsonArray();

		for (LinkFieldProperty.SourceProperty.FilterProperty filterDefinition : sourceDefinition.getFilterList())
			result.add(serializeFilter(indexDefinition, filterDefinition, jsonSerializationContext));

		return result;
	}

	private JsonElement serializeFilter(IndexDefinition indexDefinition, LinkFieldProperty.SourceProperty.FilterProperty filterDefinition, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();

		result.addProperty("attribute", indexDefinition.getAttribute(filterDefinition.getAttribute().getValue()).getCode());
		result.add("value", serializeObject(filterDefinition.getValue(), jsonSerializationContext));

		return result;
	}
}