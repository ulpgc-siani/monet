package client.services.http.serializers;

import client.core.model.List;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONValue;

public abstract class AbstractSerializer<T> implements Serializer<T, JSONValue, JSONArray> {

	@Override
	public JSONArray serializeList(List<T> elements) {
		JSONArray result = new JSONArray();
		int pos = 0;

		for (T element : elements)
			result.set(pos++, serialize(element));

		return result;
	}

}
