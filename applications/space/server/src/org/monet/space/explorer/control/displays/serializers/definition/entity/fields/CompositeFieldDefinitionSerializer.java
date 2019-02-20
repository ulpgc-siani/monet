package org.monet.space.explorer.control.displays.serializers.definition.entity.fields;

import com.google.gson.*;
import org.monet.metamodel.CompositeFieldProperty;
import org.monet.metamodel.CompositeFieldPropertyBase;
import org.monet.metamodel.FieldProperty;
import org.monet.metamodel.internal.Ref;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

public class CompositeFieldDefinitionSerializer extends FieldDefinitionSerializer<CompositeFieldProperty> {

	public CompositeFieldDefinitionSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serialize(CompositeFieldProperty definition, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject) super.serialize(definition, type, jsonSerializationContext);

		result.addProperty("extensible", definition.isExtensible());
		result.addProperty("conditional", definition.isConditional());
		result.add("fields", serializeFields(definition, jsonSerializationContext));
		result.add("view", serializeView(definition));

		return result;
	}

	private JsonObject serializeFields(CompositeFieldProperty definition, JsonSerializationContext jsonSerializationContext) {
		JsonArray result = new JsonArray();

		for (FieldProperty fieldDefinition : definition.getAllFieldPropertyList())
			result.add(jsonSerializationContext.serialize(fieldDefinition, fieldDefinition.getClass()));

		return toListObject(result);
	}

	private JsonElement serializeView(CompositeFieldProperty definition) {
		CompositeFieldPropertyBase.ViewProperty viewDefinition = definition.getView();

		if (viewDefinition == null)
			return null;

		JsonObject result = new JsonObject();
		result.add("show", serializeViewShow(definition, viewDefinition.getShow()));
		result.add("summary", serializeViewSummary(definition, viewDefinition.getSummary()));

		return result;
	}

	private JsonElement serializeViewShow(CompositeFieldProperty definition, CompositeFieldPropertyBase.ViewProperty.ShowProperty showDefinition) {

		if (showDefinition == null)
			return null;

		JsonObject result = new JsonObject();

		String layout = getLayoutContent(showDefinition.getLayout());
		if (layout != null)
			result.addProperty("layout", layout);

		String layoutExtended = getLayoutContent(showDefinition.getLayoutExtended());
		if (layoutExtended != null)
			result.addProperty("layoutExtended", layoutExtended);

		result.add("fields", serializeViewFields(definition, showDefinition.getField()));

		return result;
	}

	private JsonElement serializeViewSummary(CompositeFieldProperty definition, CompositeFieldPropertyBase.ViewProperty.SummaryProperty summaryDefinition) {
		if (summaryDefinition == null)
			return null;

		JsonObject result = new JsonObject();
		result.add("fields", serializeViewFields(definition, summaryDefinition.getField()));

		return result;
	}

	private JsonElement serializeViewFields(CompositeFieldProperty definition, List<Ref> fields) {
		JsonArray result = new JsonArray();

		for (Ref field : fields)
			result.add(new JsonPrimitive(definition.getField(field.getValue()).getCode()));

		return result;
	}

	private String getLayoutContent(String layoutFile) {
		File file = helper.getDictionary().getLayoutDefinitionFile(layoutFile);

		if (!file.exists())
			return null;

		return helper.getAgentFilesystem().readFile(file.getAbsolutePath());
	}
}