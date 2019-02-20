package client.services.http.serializers.fields;

import client.core.model.definition.entity.field.DateFieldDefinition;
import client.core.model.fields.DateField;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import cosmos.types.Date;

public class DateFieldSerializer extends AbstractFieldSerializer<DateField, DateFieldDefinition, Date> {
	@Override
	protected JSONValue serializeValue(DateField field) {
		JSONObject result = new JSONObject();
		result.put("value", field.getValue() == null ? new JSONString("") : new JSONNumber(field.getValue().getMilliseconds()));
		result.put("formattedValue", field.getValue() == null ? new JSONString("") : new JSONString(field.getValueAsString()));
		return result;
	}
}
