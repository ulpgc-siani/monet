package client.services.http.serializers;

import client.core.model.Order;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public class OrderSerializer extends AbstractSerializer<Order> {

	@Override
	public JSONValue serialize(Order element) {
		JSONObject result = new JSONObject();
		result.put("name", new JSONString(element.getName()));
		result.put("mode", new JSONString(element.getMode().toString()));
		return result;
	}

}
