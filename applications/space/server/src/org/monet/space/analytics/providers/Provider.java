package org.monet.space.analytics.providers;

import org.monet.space.analytics.model.RangeList;
import org.monet.space.kernel.model.Dashboard;
import org.sumus.dimension.category.CategoryList;
import org.sumus.indicator.IndicatorList;
import org.sumus.query.Chart;

import java.util.HashMap;


public interface Provider {

	public String getChartApi();

	public String getChartData(Dashboard dashboard, int chartType, Chart chart, IndicatorList indicatorList, RangeList rangeList);

	public String getDocumentData(Dashboard dashboard, int format, Chart chart, IndicatorList indicatorList, RangeList rangeList, HashMap<String, CategoryList> filters);

}