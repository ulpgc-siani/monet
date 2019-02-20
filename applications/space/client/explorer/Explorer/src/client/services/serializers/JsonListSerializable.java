package client.services.serializers;

import com.google.gwt.json.client.JSONArray;

public interface JsonListSerializable<T> {
	JSONArray toJson(T object);
	T fromJson(String jsonContent);
}
