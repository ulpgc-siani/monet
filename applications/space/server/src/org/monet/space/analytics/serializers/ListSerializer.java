package org.monet.space.analytics.serializers;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

public class ListSerializer {

	public static JSONObject createListStructure() {
		JSONObject result = new JSONObject();
		result.put("nrows", 0);
		result.put("rows", new JSONArray());
		return result;
	}

}