package client.services.http.serializers;

import client.core.model.Filter;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class FilterSerializer extends AbstractSerializer<Filter> {

	@Override
	public JSONValue serialize(Filter element) {
		JSONObject result = new JSONObject();
		result.put("name", new JSONString(element.getName()));
		result.put("options", new FilterOptionSerializer().serializeList(element.getOptions()));
		return result;
	}

}
