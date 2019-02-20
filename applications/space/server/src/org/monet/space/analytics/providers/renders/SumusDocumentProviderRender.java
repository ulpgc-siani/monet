package org.monet.space.analytics.providers.renders;

import org.monet.space.analytics.model.Language;
import org.monet.space.analytics.utils.NumberUtil;
import org.monet.space.kernel.library.LibraryDate;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Dashboard;
import org.sumus.asset.Asset;
import org.sumus.dictionary.Dictionary;
import org.sumus.dimension.Member;
import org.sumus.dimension.category.Category;
import org.sumus.dimension.category.CategoryList;
import org.sumus.indicator.Indicator;
import org.sumus.indicator.IndicatorList;
import org.sumus.indicatorfigure.IndicatorFigure;
import org.sumus.measureunit.MeasureUnit;
import org.sumus.measureunit.Scale;
import org.sumus.snapshot.Snapshot;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class SumusDocumentProviderRender extends DatawareHouseProviderRender {

	@Override
	protected void init() {
		loadCanvas("sumusdocumentprovider");
		Dashboard dashboard = (Dashboard) this.getParameter(DatawareHouseProviderRender.Parameter.DASHBOARD);

		addMark("company", BusinessUnit.getInstance().getLabel());
		addMark("label", dashboard.getLabel());
		addMark("date", LibraryDate.getDateAndTimeString(new Date(), Language.getCurrent(), BusinessUnit.getTimeZone(), LibraryDate.Format.TEXT, true, "/"));

		this.initFilters();
		this.initColumns();
		this.initRows();
	}

	private void initFilters() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		StringBuilder filtersBuilder = new StringBuilder();
		HashMap<String, CategoryList> filtersMap = (HashMap<String, CategoryList>) this.getParameter(Parameter.FILTERS);
		int i = 0;

		for (CategoryList categoryList : filtersMap.values()) {
			for (Category category : categoryList) {
				HashMap<String, Object> localMap = new HashMap<String, Object>();
				localMap.put("name", category.getName());
				localMap.put("comma", i == 0 ? "" : "comma");
				filtersBuilder.append(block("filter", localMap));
				i++;
			}
		}

		addMark("filters", filtersBuilder.toString());
	}

	private void initColumns() {
		StringBuilder columns = new StringBuilder();
		boolean isDateTime = this.isDateTime();

		if (this.chart == null || this.chart.getSnapshots().length == 0) {
			addMark("columns", columns.toString());
			addMark("columnsCount", 0);
			return;
		}

		IndicatorList indicatorList = (IndicatorList) this.getParameter(Parameter.INDICATOR_LIST);
		Asset asset = this.getAsset();
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("comma", "true");
		columns.append(block("column." + (isDateTime ? "time" : "date"), map));

		int pos = 0;
		int size = this.getRowSize(indicatorList);
		for (Indicator indicator : indicatorList) {
			HashMap<String, Object> resultMap = this.initColumn(asset, indicator, pos, size);
			columns.append(resultMap.get("content"));
			pos = (Integer) resultMap.get("pos");
		}

		addMark("columns", columns);
		addMark("columnsCount", pos + 1 + Integer.valueOf(block("additionalColumns", map)));
	}

	private HashMap<String, Object> initColumn(Asset asset, Indicator indicator, int pos, int size) {
		Dictionary dictionary = this.getDictionary(asset);
		MeasureUnit measureUnit = indicator.getMeasureUnit();
		String metricLabel = dictionary.getLabel(measureUnit);
		Scale scale = this.chartScaler.getScale(asset.getMeasureUnit(indicator.getMeasureUnit().getName()));
		String scaleLabel = dictionary.getLabel(scale);
		HashMap<String, Object> map = new HashMap<String, Object>();
		StringBuilder result = new StringBuilder();

		if (this.existsDrill()) {
			Member[] members = this.chart.getDrill().getMembers();
			for (Member member : members) {
				String id = indicator.getName() + "_" + pos;
				String label = dictionary.getLabel(indicator);

				if (member instanceof Category) {
					Category category = (Category) member;
					id = indicator.getName() + "_" + category.getName();
					label = label + " - " + this.getCategoryLabel(dictionary, category);
				}

				map.put("id", id);
				map.put("comma", pos == size - 1 ? "" : "true");
				map.put("label", label != null ? label : indicator.getName());
				map.put("metric", metricLabel != null ? metricLabel : measureUnit.getName());
				map.put("scale", scaleLabel != null ? scaleLabel : scale.getName());
				result.append(block("column", map));

				pos++;
			}
		} else {
			String label = dictionary.getLabel(indicator);

			map.put("id", indicator.getName());
			map.put("comma", pos == size - 1 ? "" : "true");
			map.put("label", label != null ? label : indicator.getName());
			map.put("metric", metricLabel != null ? metricLabel : measureUnit.getName());
			map.put("scale", scaleLabel != null ? scaleLabel : scale.getName());
			result.append(block("column", map));

			pos++;
		}

		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("content", result.toString());
		resultMap.put("pos", pos);

		return resultMap;
	}

	private void initRows() {
		IndicatorList indicatorList = (IndicatorList) this.getParameter(DatawareHouseProviderRender.Parameter.INDICATOR_LIST);
		StringBuilder rows = new StringBuilder();
		int pos = 0;
		int rowCount = 0;

		if (this.chart == null || this.chart.getSnapshots().length == 0) {
			addMark("rows", rows.toString());
			addMark("rowsCount", 0);
			return;
		}

		int spotsCount = this.chart.getSnapshots().length;
		int size = this.getRowSize(indicatorList);

		for (Snapshot snapshot : this.chart.getSnapshots()) {
			HashMap<String, Object> spotMap = new HashMap<String, Object>();
			StringBuilder indicators = new StringBuilder();
			int indicatorPos = 0;
			String dateIndicator = this.getDateIndicator(snapshot.getTimeStamp(), false, true);
			boolean addRow = false;

			indicators.append(dateIndicator);

			for (Indicator indicator : indicatorList) {
				HashMap<String, Object> localMap = new HashMap<String, Object>();
				IndicatorFigure indicatorFigure = snapshot.getIndicatorFigure(indicator.getName());
				List<Double> values = this.getValues(indicatorFigure);

				for (Double value : values) {
					boolean isNaN = Double.isNaN(value);
					localMap.put("value", !isNaN ? value : "null");
					localMap.put("formattedValue", !isNaN ? NumberUtil.formatNumber(value, Language.getCurrent()) : null);
					localMap.put("link", ""/*snapshot.getLink()*/);
					localMap.put("primitiveValue", !isNaN ? block("row$value", localMap) : "null");
					localMap.put("difference", 0);
					localMap.put("comma", indicatorPos == size - 1 ? "" : "true");
					localMap.put("type", "Number");
					indicators.append(block("row$indicator", localMap));
					indicatorPos++;
					if (!isNaN)
						addRow = true;
				}
			}

			if (addRow) {
				spotMap.clear();
				spotMap.put("indicators", indicators.toString());
				spotMap.put("comma", pos == spotsCount - 1 ? "" : "true");
				rowCount++;
				rows.append(block("row", spotMap));
			}

			pos++;
		}

		addMark("rows", rows.toString());
		addMark("rowsCount", rowCount + Integer.valueOf(block("additionalRows", new HashMap<String, Object>())));
	}

}