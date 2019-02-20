package client.services.http.serializers.fields;

import client.core.model.definition.entity.field.UriFieldDefinition;
import client.core.model.fields.UriField;
import client.core.model.types.Uri;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class UriFieldSerializer extends AbstractFieldSerializer<UriField, UriFieldDefinition, Uri> {
	@Override
	protected JSONValue serializeValue(UriField field) {
		return new JSONString(field.getValue().toString());
	}
}
