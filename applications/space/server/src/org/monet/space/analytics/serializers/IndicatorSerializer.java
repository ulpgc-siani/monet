package org.monet.space.analytics.serializers;

import net.minidev.json.JSONObject;
import org.sumus.indicator.CubeIndicator;
import org.sumus.indicator.Indicator;
import org.sumus.indicator.operator.OperatorFactory;
import org.sumus.metric.Metric;

public class IndicatorSerializer {

	public static Indicator fromJson(JSONObject jsonObject) {
		String name = (String) jsonObject.get("id");
		Metric metric = new Metric("", null, null);
		metric.setIndex(1);
		return new CubeIndicator(name, null, metric, OperatorFactory.createOperator(OperatorFactory.SUM, metric));
	}

}