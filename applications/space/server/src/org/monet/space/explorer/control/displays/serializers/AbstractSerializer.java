package org.monet.space.explorer.control.displays.serializers;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.monet.space.explorer.model.List;

public abstract class AbstractSerializer<T> extends ExplorerSerializer implements Serializer<T, JsonElement> {

	public AbstractSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public final String serialize(T entity) {
		return serializeObject(entity).toString();
	}

	@Override
	public final String serializeList(List list) {
		return listToJsonObject(list).toString();
	}

	protected final JsonObject listToJsonObject(List list) {
		JsonObject result = new JsonObject();

		result.addProperty("totalCount", list.getTotalCount());
		result.add("items", itemsToJsonArray(list));

		return result;
	}

	private JsonArray itemsToJsonArray(List list) {
		JsonArray result = new JsonArray();

		for (Object item : list)
			result.add(serializeObject((T) item));

		return result;
	}

}