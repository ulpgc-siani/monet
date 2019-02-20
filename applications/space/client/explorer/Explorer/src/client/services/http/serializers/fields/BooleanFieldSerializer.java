package client.services.http.serializers.fields;

import client.core.model.definition.entity.field.BooleanFieldDefinition;
import client.core.model.fields.BooleanField;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONValue;

public class BooleanFieldSerializer extends AbstractFieldSerializer<BooleanField, BooleanFieldDefinition, Boolean> {

	@Override
	protected JSONValue serializeValue(BooleanField field) {
		return JSONBoolean.getInstance(field.getValue());
	}

}
