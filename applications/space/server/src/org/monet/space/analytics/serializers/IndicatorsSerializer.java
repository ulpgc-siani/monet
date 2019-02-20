package org.monet.space.analytics.serializers;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.sumus.indicator.Indicator;
import org.sumus.indicator.IndicatorList;


public class IndicatorsSerializer {

	public static IndicatorList fromJson(String content) {
		JSONObject jsonObject;
		IndicatorList indicatorList = new IndicatorList();

		if (content == null)
			return indicatorList;

		try {
			jsonObject = (JSONObject) new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(content);
			JSONArray jsonRows = (JSONArray) jsonObject.get("rows");

			for (Object row : jsonRows) {
				JSONObject jsonRow = (JSONObject) row;
				Indicator indicator = IndicatorSerializer.fromJson(jsonRow);
				indicatorList.add(indicator);
			}

		} catch (ParseException e) {
			return null;
		}

		return indicatorList;
	}

}