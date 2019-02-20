package org.monet.space.explorer.control.displays.serializers.field;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.monet.metamodel.CompositeFieldProperty;
import org.monet.metamodel.FieldProperty;
import org.monet.space.explorer.control.displays.serializers.field.value.CompositeFieldValueSerializer;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Field;

public class CompositeFieldSerializer extends AbstractFieldSerializer<CompositeFieldProperty> {

	@Override
	protected JsonElement serializeValue(Field field, JsonSerializationContext jsonSerializationContext) {
		JsonObject result = (JsonObject) new CompositeFieldValueSerializer(helper).serialize(field, jsonSerializationContext);
		result.add("fields", serializeFields(field, jsonSerializationContext));
		return result;
	}

	private JsonElement serializeFields(Field compositeField, JsonSerializationContext jsonSerializationContext) {
		JsonArray fields = new JsonArray();
		CompositeFieldProperty compositeFieldDefinition = compositeField.getFieldDefinition();
		Attribute compositeAttribute = compositeField.getAttribute();

		for (FieldProperty fieldDefinition : compositeFieldDefinition.getAllFieldPropertyList()) {
			Attribute attribute = compositeAttribute!=null?compositeAttribute.getAttribute(fieldDefinition.getCode()):null;
			Field field = new Field(compositeField.getNode(), attribute, fieldDefinition);

			JsonElement fieldElement = AbstractFieldSerializer.getFieldSerializer(fieldDefinition, helper).serialize(field, jsonSerializationContext);
			fields.add(fieldElement);
		}

		return fields;
	}
}
