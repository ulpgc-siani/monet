package client.services.http.serializers.fields;

import client.core.model.Field;
import client.core.model.definition.entity.FieldDefinition;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class SimpleFieldSerializer<F extends Field<FD, String>, FD extends FieldDefinition> extends AbstractFieldSerializer<F, FD, String> {
	@Override
	protected JSONValue serializeValue(Field field) {
		return new JSONString(field.getValue() == null ? "" : (String) field.getValue());
	}
}
