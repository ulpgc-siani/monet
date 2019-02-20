package org.monet.space.explorer.control.displays.serializers.definition.entity.fields;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.monet.metamodel.SelectFieldProperty;
import org.monet.metamodel.TermProperty;
import org.monet.space.explorer.control.displays.serializers.ExplorerSerializer;

import java.lang.reflect.Type;

public class SelectFieldDefinitionSerializer extends FieldDefinitionSerializer<SelectFieldProperty> {

	public SelectFieldDefinitionSerializer(ExplorerSerializer.Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serialize(SelectFieldProperty definition, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject) super.serialize(definition, type, jsonSerializationContext);

		result.add("terms", serializeTerms(definition, jsonSerializationContext));

		if (definition.getSource() != null)
			result.addProperty("source", helper.getDictionary().getDefinitionCode(definition.getSource().getValue()));

		result.add("allowHistory", serializeAllowHistory(definition, jsonSerializationContext));
		result.addProperty("allowOther", definition.allowOther());
		result.addProperty("allowKey", definition.allowKey());
		result.addProperty("allowSearch", definition.allowSearch());
		result.add("select", serializeSelect(definition, jsonSerializationContext));

		return result;
	}

	private JsonElement serializeTerms(SelectFieldProperty definition, JsonSerializationContext jsonSerializationContext) {
		SelectFieldProperty.TermsProperty termsDefinition = definition.getTerms();

		if (termsDefinition == null)
			return null;

		JsonArray result = new JsonArray();

		for (TermProperty termProperty : termsDefinition.getTermPropertyList())
			result.add(serializeTerm(termProperty, jsonSerializationContext));

		return toListObject(result);
	}

	private JsonElement serializeTerm(TermProperty termProperty, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();

		result.addProperty("key", termProperty.getKey());
		result.addProperty("label", helper.getLanguage().getModelResource(termProperty.getLabel()));

		if (termProperty.isCategory())
			result.addProperty("category", termProperty.isCategory());

		JsonArray children = new JsonArray();
		for (TermProperty childTermDefinition : termProperty.getTermPropertyList())
			children.add(serializeTerm(childTermDefinition, jsonSerializationContext));

		result.add("terms", toListObject(children));

		return result;
	}

	private JsonElement serializeAllowHistory(SelectFieldProperty definition, JsonSerializationContext jsonSerializationContext) {
		if (definition.getEnableHistory() == null)
			return null;

		JsonObject result = new JsonObject();
		result.addProperty("dataStore", definition.getEnableHistory().getDatastore());

		return result;
	}

	private JsonElement serializeSelect(SelectFieldProperty definition, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();
		SelectFieldProperty.SelectProperty selectDefinition = definition.getSelect();

		if (selectDefinition == null)
			return null;

		result.addProperty("depth", selectDefinition.getDepth());
		result.add("filter", serializeSelectFilter(selectDefinition.getFilter(), jsonSerializationContext));

		if (selectDefinition.getFlatten() != null)
			result.addProperty("flatten", selectDefinition.getFlatten().toString());

		if (selectDefinition.getRoot() != null)
			result.add("root", serializeObject(selectDefinition.getRoot(), jsonSerializationContext));

		if (selectDefinition.getContext() != null)
			result.addProperty("context", String.valueOf(selectDefinition.getContext()));

		result.addProperty("embedded", selectDefinition.isEmbedded());

		return result;
	}

	private JsonElement serializeSelectFilter(SelectFieldProperty.SelectProperty.FilterProperty filterDefinition, JsonSerializationContext jsonSerializationContext) {

		if (filterDefinition == null)
			return null;

		JsonObject result = new JsonObject();
		JsonArray tags = new JsonArray();

		for (Object tagDefinition : filterDefinition.getTag())
			tags.add(serializeObject(tagDefinition, jsonSerializationContext));

		result.add("tags", tags);

		return result;
	}

}