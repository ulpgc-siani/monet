package org.monet.space.analytics.serializers;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.sumus.dictionary.Dictionary;
import org.sumus.dimension.category.Category;
import org.sumus.dimension.category.CategoryList;

import java.util.HashMap;

public class CategoriesSerializer {

	public static JSONObject toJson(Category[] categories, long[] components, Dictionary dictionary) {
		JSONObject jsonResult = ListSerializer.createListStructure();
		JSONArray jsonArray = (JSONArray) jsonResult.get("rows");

		for (Category category : categories)
			jsonArray.add(CategorySerializer.toJson(category, components, dictionary));

		jsonResult.put("nrows", categories.length);

		return jsonResult;
	}

	public static HashMap<String, CategoryList> fromJson(String content) {
		JSONObject jsonObject;
		HashMap<String, CategoryList> result = new HashMap<String, CategoryList>();

		if (content == null)
			return result;

		try {
			jsonObject = (JSONObject) new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(content);
			JSONArray jsonRows = (JSONArray) jsonObject.get("rows");

			for (Object row : jsonRows) {
				JSONObject jsonRow = (JSONObject) row;
				Category category = CategorySerializer.fromJson(jsonRow);
				String taxonomy = (String) jsonRow.get("taxonomy");

				if (!result.containsKey(taxonomy))
					result.put(taxonomy, new CategoryList());

				result.get(taxonomy).add(category);
			}

		} catch (ParseException e) {
			return null;
		}

		return result;
	}

}