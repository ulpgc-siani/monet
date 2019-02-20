package client.services.http.serializers.fields;

import client.core.model.definition.entity.field.PictureFieldDefinition;
import client.core.model.fields.PictureField;
import client.core.model.types.Picture;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class PictureFieldSerializer extends AbstractFieldSerializer<PictureField, PictureFieldDefinition, Picture> {
	@Override
	protected JSONValue serializeValue(PictureField field) {
		JSONObject result = new JSONObject();
		result.put("id", field.getValue() == null ? new JSONString("") : new JSONString(field.getValue().getId()));
		result.put("label", field.getValue() == null ? new JSONString("") : new JSONString(field.getValue().getLabel()));
		return result;
	}
}
