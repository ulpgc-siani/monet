package client.services.serializers;

import client.core.model.Task;
import com.google.gwt.json.client.JSONArray;

import java.util.List;

public class TaskListSerializer implements JsonListSerializable<List<Task>> {

	@Override
	public JSONArray toJson(List<Task> tasks) {
		return null;
	}

	@Override
	public List<Task> fromJson(String jsonContent) {
		return null;
	}

}
