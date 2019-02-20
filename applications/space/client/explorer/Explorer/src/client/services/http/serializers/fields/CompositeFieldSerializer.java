package client.services.http.serializers.fields;

import client.core.model.definition.entity.field.CompositeFieldDefinition;
import client.core.model.fields.CompositeField;
import client.core.model.types.Composite;
import com.google.gwt.json.client.JSONBoolean;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

public class CompositeFieldSerializer extends AbstractFieldSerializer<CompositeField, CompositeFieldDefinition, Composite> {
	@Override
	protected JSONValue serializeValue(CompositeField field) {
		JSONObject value = new JSONObject();
		value.put("conditioned", JSONBoolean.getInstance(field.getConditioned()));
		return value;
	}
}
