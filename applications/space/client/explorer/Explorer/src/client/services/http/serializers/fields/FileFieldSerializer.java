package client.services.http.serializers.fields;

import client.core.model.definition.entity.field.FileFieldDefinition;
import client.core.model.fields.FileField;
import client.core.model.types.File;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class FileFieldSerializer extends AbstractFieldSerializer<FileField, FileFieldDefinition, File> {
	@Override
	protected JSONValue serializeValue(FileField field) {
		JSONObject result = new JSONObject();
		result.put("id", field.getValue() == null ? new JSONString("") : new JSONString(field.getValue().getId()));
		result.put("label", field.getValue() == null ? new JSONString("") : new JSONString(field.getValue().getLabel()));
		return result;
	}
}
