package org.monet.space.kernel.model;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.util.List;

public class TaskFilters {
	public List<TaskType> types;
	public List<String> states;

	public JSONObject toJson() {
		JSONArray jsonTypes = new JSONArray();
		JSONArray jsonStates = new JSONArray();

		for (TaskType type : this.types)
			jsonTypes.add(type.toJSON());

		for (String state : this.states)
			jsonStates.add(state);

		JSONObject result = new JSONObject();
		result.put("types", jsonTypes);
		result.put("states", jsonStates);

		return result;
	}
}