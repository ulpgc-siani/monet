package client.services.serializers;

import com.google.gwt.json.client.JSONObject;

public interface JsonSerializable<T> {
	JSONObject toJson(T object);
	T fromJson(String jsonContent);
}
