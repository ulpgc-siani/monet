package client.services.http.serializers;

import client.core.model.Filter;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class FilterOptionSerializer extends AbstractSerializer<Filter.Option> {

	@Override
	public JSONValue serialize(Filter.Option element) {
		JSONObject result = new JSONObject();
		result.put("value", new JSONString(element.getValue()));
		result.put("label", new JSONString(element.getLabel()));
		return result;
	}

}
