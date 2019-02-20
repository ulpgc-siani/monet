package client.services.http.serializers.fields;

import client.core.model.definition.entity.field.LinkFieldDefinition;
import client.core.model.fields.LinkField;
import client.core.model.types.Link;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class LinkFieldSerializer extends AbstractFieldSerializer<LinkField, LinkFieldDefinition, Link> {
	@Override
	protected JSONValue serializeValue(LinkField field) {
		JSONObject result = new JSONObject();
		Link link = field.getValue();

		if (link != null) {
			result.put("id", new JSONString(link.getId()));
			result.put("label", new JSONString(link.getLabel()));
		} else {
			result.put("id", new JSONString(""));
			result.put("label", new JSONString(""));
		}

		return result;
	}
}
