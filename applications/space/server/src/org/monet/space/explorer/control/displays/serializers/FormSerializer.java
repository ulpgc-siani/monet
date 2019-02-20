package org.monet.space.explorer.control.displays.serializers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.monet.metamodel.FieldProperty;
import org.monet.metamodel.FormDefinition;
import org.monet.space.explorer.control.displays.serializers.field.AbstractFieldSerializer;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Field;
import org.monet.space.kernel.model.Node;

import java.lang.reflect.Type;

public class FormSerializer extends NodeSerializer<FormDefinition> {

	public FormSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serialize(Node node, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject)super.serialize(node, type, jsonSerializationContext);

		result.add("fields", serializeFields(node, jsonSerializationContext));

		return result;
	}

	private JsonObject serializeFields(Node node, JsonSerializationContext jsonSerializationContext) {
		JsonArray result = new JsonArray();
		FormDefinition definition = (FormDefinition)node.getDefinition();

		for (FieldProperty fieldDefinition : definition.getAllFieldPropertyList()) {
			Attribute attribute = node.getAttribute(fieldDefinition.getCode());

			if (attribute == null)
				attribute = new Attribute(fieldDefinition);

			Field field = new Field(node, attribute, fieldDefinition);
			JsonElement fieldElement = AbstractFieldSerializer.getFieldSerializer(fieldDefinition, helper).serialize(field, jsonSerializationContext);
			result.add(fieldElement);
		}

		return toListObject(result);
	}

}
