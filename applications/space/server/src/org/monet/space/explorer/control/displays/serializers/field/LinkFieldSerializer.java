package org.monet.space.explorer.control.displays.serializers.field;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import org.monet.metamodel.LinkFieldProperty;
import org.monet.space.explorer.control.displays.serializers.field.value.LinkFieldValueSerializer;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Field;

public class LinkFieldSerializer extends AbstractFieldSerializer<LinkFieldProperty> {

	@Override
	public JsonElement serializeValue(Field field, JsonSerializationContext jsonSerializationContext) {
        final JsonObject result = (JsonObject) new LinkFieldValueSerializer(helper).serialize(field, jsonSerializationContext);
		result.add("index", serializeValueIndex(field.<LinkFieldProperty>getFieldDefinition(), field.getAttribute()));
		return result;
	}

	private JsonElement serializeValueIndex(LinkFieldProperty fieldDefinition, Attribute attribute) {
		JsonObject result = new JsonObject();
		result.addProperty("id", helper.getDictionary().getDefinitionCode(fieldDefinition.getSource().getIndex().getValue()));
		//result.add("filters", );
		return result;
	}

}
