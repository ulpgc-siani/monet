package org.monet.space.explorer.control.displays.serializers.definition.entity.views;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.monet.metamodel.FormDefinition;
import org.monet.metamodel.FormDefinitionBase;
import org.monet.metamodel.internal.LayoutDefinition;
import org.monet.metamodel.internal.Ref;

import java.io.File;
import java.lang.reflect.Type;

public class FormViewDefinitionSerializer extends NodeViewDefinitionSerializer<FormDefinition.FormViewProperty> {

	public FormViewDefinitionSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serialize(FormDefinition.FormViewProperty definition, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject)super.serialize(definition, type, jsonSerializationContext);

		result.add("show", serializeShow(definition, jsonSerializationContext));

		return result;
	}

	private JsonElement serializeShow(FormDefinition.FormViewProperty definition, JsonSerializationContext jsonSerializationContext) {
		FormDefinitionBase.FormViewProperty.ShowProperty showDefinition = definition.getShow();
		JsonObject result = new JsonObject();
		JsonArray fields = new JsonArray();

		for (Ref linkRef : showDefinition.getField())
			fields.add(jsonSerializationContext.serialize(linkRef, linkRef.getClass()));

		String layout = getLayoutContent(showDefinition.getLayout());
		if (layout != null)
			result.addProperty("layout", layout);

		String layoutExtended = getLayoutContent(showDefinition.getLayoutExtended());
		if (layoutExtended != null)
			result.addProperty("layoutExtended", layoutExtended);

		result.add("fields", toListObject(fields));

		return result;
	}

	private String getLayoutContent(String layoutFile) {
		File file = helper.getDictionary().getLayoutDefinitionFile(layoutFile);

		if (!file.exists())
			return null;

		return helper.getAgentFilesystem().readFile(file.getAbsolutePath());
	}

}
