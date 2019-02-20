package org.monet.space.explorer.control.displays.serializers.definition.entity.fields;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.monet.metamodel.CheckFieldProperty;
import org.monet.metamodel.TermProperty;
import org.monet.metamodel.internal.Ref;

import java.lang.reflect.Type;

public class CheckFieldDefinitionSerializer extends FieldDefinitionSerializer<CheckFieldProperty> {

	public CheckFieldDefinitionSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serialize(CheckFieldProperty definition, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject) super.serialize(definition, type, jsonSerializationContext);
		Ref source = definition.getSource();

		result.addProperty("allowKey", definition.allowKey());
		result.addProperty("source", source != null ? helper.getDictionary().getDefinitionCode(source.getValue()) : null);
		result.add("terms", serializeTerms(definition, jsonSerializationContext));
		result.add("select", serializeSelect(definition, jsonSerializationContext));

		return result;
	}

	private JsonObject serializeTerms(CheckFieldProperty definition, JsonSerializationContext jsonSerializationContext) {
		CheckFieldProperty.TermsProperty termsDefinition = definition.getTerms();

		if (termsDefinition == null)
			return null;


		JsonArray items = new JsonArray();
		for (TermProperty termDefinition : termsDefinition.getTermPropertyList()) {
			items.add(serializeTerm(termDefinition, jsonSerializationContext));
		}

		return toListObject(items);
	}

	private JsonElement serializeTerm(TermProperty termDefinition, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();

		result.addProperty("key", termDefinition.getKey());
		result.addProperty("label", helper.getLanguage().getModelResource(termDefinition.getLabel()));

		if (termDefinition.isCategory())
			result.addProperty("category", termDefinition.isCategory());

		JsonArray childrenDefinitions = new JsonArray();
		for (TermProperty childDefinition : termDefinition.getTermPropertyList())
			childrenDefinitions.add(serializeTerm(childDefinition, jsonSerializationContext));

		result.add("terms", toListObject(childrenDefinitions));

		return result;
	}

	private JsonElement serializeSelect(CheckFieldProperty definition, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();
		CheckFieldProperty.SelectProperty selectDefinition = definition.getSelect();

		if (selectDefinition == null)
			return null;

		result.addProperty("depth", selectDefinition.getDepth());
		result.add("filter", serializeSelectFilter(selectDefinition.getFilter(), jsonSerializationContext));

		if (selectDefinition.getFlatten() != null)
			result.addProperty("flatten", selectDefinition.getFlatten().toString());

		result.add("root", serializeObject(selectDefinition.getRoot(), jsonSerializationContext));

		return result;
	}

	private JsonElement serializeSelectFilter(CheckFieldProperty.SelectProperty.FilterProperty filterDefinition, JsonSerializationContext jsonSerializationContext) {

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