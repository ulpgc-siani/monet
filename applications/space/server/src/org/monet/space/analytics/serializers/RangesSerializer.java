package org.monet.space.analytics.serializers;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.monet.space.analytics.model.RangeList;
import org.monet.space.analytics.model.Range;


public class RangesSerializer {

	public static RangeList fromJson(String content) {
		JSONObject jsonObject;
		RangeList rangeList = new RangeList();

		if (content == null)
			return rangeList;

		try {
			jsonObject = (JSONObject) new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(content);
			JSONArray jsonRows = (JSONArray) jsonObject.get("rows");

			for (Object row : jsonRows) {
				JSONObject jsonRow = (JSONObject) row;
				Range range = RangeSerializer.fromJson(jsonRow);
				rangeList.put(range.getMeasureUnit(), range);
			}

		} catch (ParseException e) {
			return null;
		}

		return rangeList;
	}

}