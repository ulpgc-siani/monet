package org.monet.space.explorer.control.displays.serializers.definition.entity.views;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.monet.metamodel.IndexDefinition;
import org.monet.metamodel.internal.Ref;

import java.lang.reflect.Type;
import java.util.List;

public class IndexViewDefinitionSerializer extends ViewDefinitionSerializer<IndexDefinition.IndexViewProperty> {

	public IndexViewDefinitionSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serialize(IndexDefinition.IndexViewProperty definition, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject)super.serialize(definition, type, jsonSerializationContext);

		result.add("show", serializeShow(definition, jsonSerializationContext));

		return result;
	}

	private JsonElement serializeShow(IndexDefinition.IndexViewProperty definition, JsonSerializationContext jsonSerializationContext) {
		IndexDefinition.IndexViewProperty.ShowProperty showDefinition = definition.getShow();
		JsonObject result = new JsonObject();

		if (showDefinition.getTitle() != null)
			result.add("title", jsonSerializationContext.serialize(showDefinition.getTitle(), showDefinition.getTitle().getClass()));

		result.add("lines", serializeRefList(showDefinition.getLine(), jsonSerializationContext));
		result.add("linesBelow", serializeRefList(showDefinition.getLineBelow(), jsonSerializationContext));
		result.add("highlight", serializeRefList(showDefinition.getHighlight(), jsonSerializationContext));
		result.add("footer", serializeRefList(showDefinition.getFooter(), jsonSerializationContext));

		if (showDefinition.getIcon() != null)
			result.add("icon", jsonSerializationContext.serialize(showDefinition.getIcon(), showDefinition.getIcon().getClass()));

		if (showDefinition.getPicture() != null)
			result.add("picture", jsonSerializationContext.serialize(showDefinition.getPicture(), showDefinition.getPicture().getClass()));

		return result;
	}

	private JsonObject serializeRefList(List<Ref> elements, JsonSerializationContext jsonSerializationContext) {
		JsonArray result = new JsonArray();

		for (Ref element : elements)
			result.add(jsonSerializationContext.serialize(element, element.getClass()));

		return toListObject(result);
	}

}
