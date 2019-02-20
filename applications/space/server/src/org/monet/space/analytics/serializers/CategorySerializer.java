package org.monet.space.analytics.serializers;

import net.minidev.json.JSONObject;
import org.sumus.dictionary.Dictionary;
import org.sumus.dimension.category.Category;

public class CategorySerializer {

	public static JSONObject toJson(Category category, long[] components, Dictionary dictionary) {
		String label = dictionary.getLabel(category);
		JSONObject jsonResult = new JSONObject();
		jsonResult.put("code", category.getAbsoluteName());
		jsonResult.put("name", label != null ? label : category.getName());
		jsonResult.put("leaf", category.getChildren(components).length == 0 ? true : false);
		return jsonResult;
	}

	public static Category fromJson(JSONObject jsonObject) {
		return new Category((String) jsonObject.get("id"));
	}

}