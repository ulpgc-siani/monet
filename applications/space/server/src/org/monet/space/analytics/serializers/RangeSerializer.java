package org.monet.space.analytics.serializers;

import net.minidev.json.JSONObject;
import org.monet.space.analytics.model.Range;

public class RangeSerializer {

	public static Range fromJson(JSONObject jsonObject) {
		String name = (String) jsonObject.get("measureUnit");
		String mode = (String) jsonObject.get("mode");
		String minValue = (String) jsonObject.get("min");
		String maxValue = (String) jsonObject.get("max");
		Double min = !minValue.isEmpty() ? Double.valueOf(minValue) : null;
		Double max = !maxValue.isEmpty() ? Double.valueOf(maxValue) : null;

		return new Range(name, Range.modeFromString(mode), min, max);
	}

}