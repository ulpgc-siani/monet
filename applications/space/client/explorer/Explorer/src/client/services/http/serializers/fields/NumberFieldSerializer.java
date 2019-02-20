package client.services.http.serializers.fields;

import client.core.model.definition.entity.field.NumberFieldDefinition;
import client.core.model.fields.NumberField;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class NumberFieldSerializer extends AbstractFieldSerializer<NumberField, NumberFieldDefinition, client.core.model.types.Number> {
	@Override
	protected JSONValue serializeValue(NumberField field) {
		JSONObject result = new JSONObject();
		result.put("value", new JSONNumber(field.getValue().getValue().doubleValue()));
		result.put("formattedValue", new JSONString(field.getValueAsString()));
		return result;
	}
}
