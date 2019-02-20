package org.monet.space.explorer.control.displays.serializers.definition.entity.fields;

import com.google.gson.*;
import org.monet.metamodel.TextFieldProperty;
import org.monet.space.explorer.control.displays.serializers.ExplorerSerializer;

import java.lang.reflect.Type;

public class TextFieldDefinitionSerializer extends FieldDefinitionSerializer<TextFieldProperty> {

	public TextFieldDefinitionSerializer(ExplorerSerializer.Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serialize(TextFieldProperty definition, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject) super.serialize(definition, type, jsonSerializationContext);

		result.add("allowHistory", serializeAllowHistory(definition, jsonSerializationContext));
		result.add("length", serializeLength(definition, jsonSerializationContext));
		result.add("edition", serializeEdition(definition, jsonSerializationContext));
		result.add("patterns", serializePatterns(definition, jsonSerializationContext));

		if (definition.isEmail())
			result.addProperty("mail", definition.isEmail());

		return result;
	}

	private JsonElement serializeAllowHistory(TextFieldProperty definition, JsonSerializationContext jsonSerializationContext) {
		if (definition.getEnableHistory() == null)
			return null;

		JsonObject result = new JsonObject();
		result.addProperty("dataStore", definition.getEnableHistory().getDatastore());

		return result;
	}

	private JsonElement serializeLength(TextFieldProperty definition, JsonSerializationContext jsonSerializationContext) {
		if (definition.getLength() == null)
			return null;

		JsonObject result = new JsonObject();
		result.addProperty("min", definition.getLength().getMin());
		result.addProperty("max", definition.getLength().getMax());

		return result;
	}

	private JsonElement serializeEdition(TextFieldProperty definition, JsonSerializationContext jsonSerializationContext) {
		if (definition.getEdition() == null)
			return null;

		JsonObject result = new JsonObject();
		result.addProperty("mode", definition.getEdition().getMode().toString());

		return result;
	}

	private JsonElement serializePatterns(TextFieldProperty definition, JsonSerializationContext jsonSerializationContext) {
		JsonArray result = new JsonArray();

		for (TextFieldProperty.PatternProperty patternDefinition : definition.getPatternList())
			result.add(serializePattern(patternDefinition, jsonSerializationContext));

		return result;
	}

	private JsonElement serializePattern(TextFieldProperty.PatternProperty patternDefinition, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();

		result.addProperty("regExp", helper.getLanguage().getModelResource(patternDefinition.getRegexp()));
		result.add("metas", serializePatternMetaList(patternDefinition, jsonSerializationContext));

		return result;
	}

	private JsonElement serializePatternMetaList(TextFieldProperty.PatternProperty patternDefinition, JsonSerializationContext jsonSerializationContext) {
		JsonArray result = new JsonArray();

		for (TextFieldProperty.PatternProperty.MetaProperty metaDefinition : patternDefinition.getMetaList())
			result.add(serializePatternMeta(metaDefinition, jsonSerializationContext));

		return result;
	}

	private JsonElement serializePatternMeta(TextFieldProperty.PatternProperty.MetaProperty metaDefinition, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = new JsonObject();

		result.addProperty("indicator", metaDefinition.getIndicator());
		result.addProperty("position", metaDefinition.getPosition());

		return result;
	}

}