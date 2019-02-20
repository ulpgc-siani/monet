package org.monet.space.explorer.control.displays.serializers.definition.entity.fields;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.monet.metamodel.SummationFieldProperty;
import org.monet.metamodel.SummationItemProperty;
import org.monet.space.explorer.control.displays.serializers.ExplorerSerializer;

import java.lang.reflect.Type;

public class SummationFieldDefinitionSerializer extends FieldDefinitionSerializer<SummationFieldProperty> {

	public SummationFieldDefinitionSerializer(ExplorerSerializer.Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serialize(SummationFieldProperty definition, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject) super.serialize(definition, type, jsonSerializationContext);

//SUMMATION NOT USED		result.add("terms", serializeTerms(definition, jsonSerializationContext));
//
//		if (definition.getSource() != null)
//			result.addProperty("source", helper.getDictionary().getDefinitionCode(definition.getSource().getValue()));
//
//		result.add("select", serializeSelect(definition, jsonSerializationContext));
//		result.addProperty("format", definition.getFormat());
//		result.add("range", serializeRange(definition, jsonSerializationContext));
//
		return result;
	}

	private JsonElement serializeTerms(SummationFieldProperty definition, JsonSerializationContext jsonSerializationContext) {
//SUMMATION NOT USED		SummationFieldProperty.TermsProperty termsDefinition = definition.getTerms();

//SUMMATION NOT USED		if (termsDefinition == null)
			return null;

//SUMMATION NOT USED		JsonArray result = new JsonArray();

//SUMMATION NOT USED		for (SummationItemProperty itemProperty : termsDefinition.getSummationItemPropertyList())
//			result.add(serializeTerm(itemProperty, jsonSerializationContext));

//		return toListObject(result);
	}

	private JsonElement serializeTerm(SummationItemProperty termProperty, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();

		result.addProperty("key", termProperty.getKey());
		result.addProperty("label", helper.getLanguage().getModelResource(termProperty.getLabel()));
		result.addProperty("type", termProperty.getType().toString());
		result.addProperty("multiple", termProperty.isMultiple());
		result.addProperty("negative", termProperty.isNegative());

		JsonArray children = new JsonArray();
		for (SummationItemProperty childTermDefinition : termProperty.getSummationItemPropertyList())
			children.add(serializeTerm(childTermDefinition, jsonSerializationContext));

		result.add("terms", toListObject(children));

		return result;
	}

	private JsonElement serializeSelect(SummationFieldProperty definition, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();
//SUMMATION NOT USED		SummationFieldProperty.SelectProperty selectDefinition = definition.getSelect();
//
//		result.addProperty("depth", selectDefinition.getDepth());
//		result.addProperty("flatten", selectDefinition.getFlatten().toString());

		return result;
	}


	private JsonElement serializeRange(SummationFieldProperty definition, JsonSerializationContext jsonSerializationContext) {
//SUMMATION NOT USED		SummationFieldPropertyBase.RangeProperty rangeDefinition = definition.getRange();
//
//		if (rangeDefinition == null)
			return null;

//SUMMATION NOT USED		JsonObject result = new JsonObject();
//		result.addProperty("min", rangeDefinition.getMin());
//		result.addProperty("max", rangeDefinition.getMax());
//
//		return result;
	}

}