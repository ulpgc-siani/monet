package org.monet.space.analytics.providers.renders;

import org.monet.space.analytics.model.ChartType;
import org.monet.space.analytics.model.Language;
import org.monet.space.analytics.model.RangeList;
import org.monet.space.analytics.utils.NumberUtil;
import org.monet.space.analytics.model.Range;
import org.monet.space.kernel.library.LibraryString;
import org.sumus.asset.Asset;
import org.sumus.dictionary.Dictionary;
import org.sumus.dimension.Member;
import org.sumus.dimension.category.Category;
import org.sumus.indicator.CompositeIndicator;
import org.sumus.indicator.Indicator;
import org.sumus.indicator.IndicatorList;
import org.sumus.indicatorfigure.IndicatorFigure;
import org.sumus.measureunit.MeasureUnit;
import org.sumus.measureunit.Scale;
import org.sumus.snapshot.Snapshot;
import org.sumus.time.TimeStamp;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class GoogleChartProviderRender extends DatawareHouseProviderRender {
	private HashMap<String, Double> minValues = new HashMap<String, Double>();
	private HashMap<String, Double> maxValues = new HashMap<String, Double>();
	protected boolean isEmpty = true;

	@Override
	protected void init() {

		loadCanvas("googlechartprovider");

		this.initColumns();
		this.initRows();

		this.initChartInfo(); // this is the final step. Important !!
	}

	private String getIndicatorAxisMember(Indicator indicator, Member member, int columnPosition, int axisMemberPosition, boolean addComma) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String memberName = (member != null && member instanceof Category) ? ((Category) member).getName() : null;

		map.put("columnPosition", columnPosition + 1);
		map.put("axisMemberPosition", axisMemberPosition);
		map.put("indicator", indicator.getName() + (memberName != null ? "_" + LibraryString.cleanString(memberName) : ""));
		map.put("comma", addComma ? "true" : "");

		return block("indicatorAxisMember", map);
	}

	protected void initChartInfo() {
		StringBuilder verticalAxisLabels = new StringBuilder();
		StringBuilder indicatorsAxisMember = new StringBuilder();
		int chartType = (Integer) this.getParameter(DatawareHouseProviderRender.Parameter.CHART_TYPE);

		if (this.chartScaler == null) {
			addMark("verticalAxis", verticalAxisLabels.toString());
			addMark("indicatorsAxisMember", indicatorsAxisMember.toString());
            addMark("isEmpty", "true");
			return;
		}

		IndicatorList indicatorList = (IndicatorList) this.getParameter(DatawareHouseProviderRender.Parameter.INDICATOR_LIST);
		Asset asset = this.getAsset();
		HashMap<String, Object> map = new HashMap<String, Object>();
		Dictionary dictionary = this.getDictionary(asset);
		HashSet<String> selectedMeasureUnits = new HashSet<String>();
		int size = this.getRowSize(indicatorList);

		int pos = 0;
		int axisPosition = -1;
		for (Indicator indicator : indicatorList) {
			MeasureUnit measureUnit = indicator.getMeasureUnit();
			boolean addComma = pos != size - 1;

			if (indicator instanceof CompositeIndicator)
				continue;

			if (selectedMeasureUnits.contains(measureUnit.getName())) {
				pos = this.addIndicatorToAxisMember(indicatorsAxisMember, pos, size, axisPosition, indicator);
				continue;
			}

			axisPosition++;
			pos = this.addIndicatorToAxisMember(indicatorsAxisMember, pos, size, axisPosition, indicator);
			Scale scale = this.chartScaler.getScale(asset.getMeasureUnit(indicator.getMeasureUnit().getName()));
			String measureUnitLabel = dictionary.getLabel(measureUnit);
			String measureUnitName = measureUnit.getName();
			String scaleLabel = dictionary.getLabel(scale);
			Range defaultRange = this.getDefaultRange(measureUnitName);
			Double minValue = this.minValues.get(measureUnitName);
			Double maxValue = this.maxValues.get(measureUnitName);
			Double factor = this.getValuesCorrectorFactor(minValue, maxValue);

			if (defaultRange != null && defaultRange.getMode() == Range.RELATIVE) {
				minValue = defaultRange.getMin() != null ? defaultRange.getMin() : minValue;
				maxValue = defaultRange.getMax() != null ? defaultRange.getMax() : maxValue;
			}

			map.put("measureUnit", measureUnitName);
			map.put("measureUnitLabel", measureUnitLabel != null ? measureUnitLabel : "");
			map.put("scale", scaleLabel != null ? scaleLabel : "");
			map.put("minValue", minValue != null && !Double.isNaN(minValue) && chartType != ChartType.BAR ? (minValue == 0 ? minValue : minValue - factor) : "");
			map.put("maxValue", maxValue != null && !Double.isNaN(maxValue) && chartType != ChartType.BAR ? maxValue + factor : "");
			map.put("useBaseLine", defaultRange != null && defaultRange.getMode() == Range.ABSOLUTE ? "true" : "false");
			map.put("comma", addComma ? "true" : "");

			verticalAxisLabels.append(block("axis", map));

			selectedMeasureUnits.add(measureUnit.getName());
		}

		addMark("isEmpty", this.isEmpty ? "true" : "false");
		addMark("verticalAxis", verticalAxisLabels.toString());
		addMark("indicatorsAxisMember", indicatorsAxisMember.toString());
	}

	private Range getDefaultRange(String measureUnit) {
		RangeList rangeList = (RangeList) this.getParameter(DatawareHouseProviderRender.Parameter.RANGE_LIST);

		if (!rangeList.containsKey(measureUnit))
			return null;

		return rangeList.get(measureUnit);
	}

	private Double getValuesCorrectorFactor(Double minValue, Double maxValue) {
		if (maxValue == null || maxValue.isNaN()) return 0.0;
		if (minValue == null || minValue.isNaN()) return 0.0;
		return (maxValue - minValue) * 0.20;
	}

	private int addIndicatorToAxisMember(StringBuilder indicatorsAxisMember, int pos, int size, int axisPosition, Indicator indicator) {
		boolean addComma = pos != size - 1;

		if (this.existsDrill()) {
			for (Member member : this.chart.getDrill().getMembers()) {
				indicatorsAxisMember.append(this.getIndicatorAxisMember(indicator, member, pos, axisPosition, addComma));
				pos++;
			}
		} else {
			indicatorsAxisMember.append(this.getIndicatorAxisMember(indicator, null, pos, axisPosition, addComma));
			pos++;
		}

		return pos;
	}

	private void initColumns() {
		Asset asset = this.getAsset();
		StringBuilder columns = new StringBuilder();
		Dictionary dictionary = this.getDictionary(asset);

		if (this.chart == null || this.chart.getSnapshots().length == 0) {
			addMark("columns", columns.toString());
			return;
		}

		IndicatorList indicatorList = (IndicatorList) this.getParameter(DatawareHouseProviderRender.Parameter.INDICATOR_LIST);
		int size = this.getRowSize(indicatorList);
		boolean isDateTime = this.isDateTime();
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("comma", "true");
		if (this.includeDate())
			columns.append(block("column." + (isDateTime ? "time" : "date"), map));
		else
			columns.append(block("column.id", map));

		int pos = 0;
		for (Indicator indicator : indicatorList) {

			if (indicator instanceof CompositeIndicator)
				continue;

			MeasureUnit measureUnit = indicator.getMeasureUnit();
			String measureUnitLabel = dictionary.getLabel(measureUnit);
			Scale scale = this.chartScaler.getScale(asset.getMeasureUnit(indicator.getMeasureUnit().getName()));
			String scaleLabel = dictionary.getLabel(scale);

			if (this.existsDrill()) {
				for (Member member : this.chart.getDrill().getMembers()) {
					String id = indicator.getName() + "_" + pos;
					String label = dictionary.getLabel(indicator);

					if (member instanceof Category) {
						Category category = (Category) member;
						id = indicator.getName() + "_" + ((Category) member).getName();
						label = label + " - " + this.getCategoryLabel(dictionary, category);
					}

					map.put("id", id);
					map.put("comma", pos == size - 1 ? "" : "true");
					map.put("label", label != null ? label : "");
					map.put("metric", measureUnitLabel != null ? measureUnitLabel : "");
					map.put("scale", scaleLabel != null ? scaleLabel : "");
					columns.append(block("column", map));

					if (this.includeTooltips())
						columns.append(block("column.tooltip", map));
				}
			} else {
				String label = dictionary.getLabel(indicator);

				map.put("id", indicator.getName());
				map.put("label", label != null ? label : "");
				map.put("metric", measureUnitLabel != null ? measureUnitLabel : "");
				map.put("scale", scaleLabel != null ? scaleLabel : "");
				map.put("comma", pos == size - 1 ? "" : "true");
				columns.append(block("column", map));
				if (this.includeTooltips())
					columns.append(block("column.tooltip", map));
			}
		}

		addMark("columns", columns);
	}

	protected boolean includeDate() {
		int chartType = (Integer) this.getParameter(DatawareHouseProviderRender.Parameter.CHART_TYPE);

		if (chartType == ChartType.BUBBLE)
			return false;

		return true;
	}

	protected boolean includeTooltips() {
		int chartType = (Integer) this.getParameter(DatawareHouseProviderRender.Parameter.CHART_TYPE);

		if (chartType == ChartType.BUBBLE)
			return false;

		return true;
	}

	private void initRows() {
		IndicatorList indicatorList = (IndicatorList) this.getParameter(DatawareHouseProviderRender.Parameter.INDICATOR_LIST);
		StringBuilder rows = new StringBuilder();
		Asset asset = this.getAsset();
		Dictionary dictionary = this.getDictionary(asset);
		int pos = 0;
		int size = this.getRowSize(indicatorList);

		if (this.chart == null || this.chart.getSnapshots().length == 0) {
			addMark("nrows", 0);
			addMark("rows", rows.toString());
			return;
		}

		int spotsCount = this.chart.getSnapshots().length;

		this.isEmpty = true;
		for (Snapshot snapshot : this.chart.getSnapshots()) {
			HashMap<String, Object> spotMap = new HashMap<String, Object>();
			StringBuilder indicators = new StringBuilder();
			int indicatorPos = 0;
			TimeStamp timeStamp = snapshot.getTimeStamp();
			boolean fullDate = this.existsTimeChange(timeStamp, pos, spotsCount);
			String dateIndicator = this.getDateIndicator(timeStamp, false, fullDate);

			if (this.includeDate())
				indicators.append(dateIndicator + ",");
			else
				indicators.append(this.getIdIndicator() + ",");

			for (Indicator indicator : indicatorList) {

				if (indicator instanceof CompositeIndicator)
					continue;

				HashMap<String, Object> localMap = new HashMap<String, Object>();
				IndicatorFigure indicatorFigure = snapshot.getIndicatorFigure(indicator.getName());
				MeasureUnit measureUnit = indicator.getMeasureUnit();
				Scale scale = this.chartScaler.getScale(indicator.getMeasureUnit());
				String scaleLabel = dictionary.getLabel(scale);
				List<Double> values = this.getValues(indicatorFigure);

				if (this.isEmpty)
					this.isEmpty = this.checkIfValuesAreEmpty(values);

				for (int i = 0; i < values.size(); i++) {
					Double value = values.get(i);
					boolean isNaN = Double.isNaN(value);
					localMap.put("value", !isNaN ? value : "null");

					this.refreshMeasureUnitRange(measureUnit, value);

					if (this.includeTooltips())
						localMap.put("tooltip", this.getTooltip(this.getDateIndicator(timeStamp, false, true), value, scaleLabel != null ? scaleLabel : "", dictionary.getLabel(indicator), this.getMemberLabel(i)));
					else
						localMap.put("tooltip", "");

					localMap.put("link", ""/*snapshot.getLink()*/);
					localMap.put("primitiveValue", !isNaN ? block("row$value", localMap) : "null");
					localMap.put("formattedValue", !isNaN ? block("row$formatted", localMap) : "null");
					localMap.put("difference", 0);
					localMap.put("comma", indicatorPos == size - 1 ? "" : "true");
					indicators.append(block("row$indicator", localMap));
					indicatorPos++;
				}
			}

			spotMap.clear();
			spotMap.put("indicators", indicators.toString());
			spotMap.put("comma", pos == spotsCount - 1 ? "" : "true");
			rows.append(block("row", spotMap));

			pos++;
		}

		addMark("rows", rows.toString());
		addMark("nrows", spotsCount);
	}

	protected boolean checkIfValuesAreEmpty(List<Double> values) {
		for (Double value : values)
			if (!Double.isNaN(value))
				return false;
		return true;
	}

	protected String getMemberLabel(int pos) {
		if (!this.existsDrill()) return "";

		Member member = this.chart.getDrill().getMembers()[pos];
		if (member != null && member instanceof Category)
			return ((Category) member).getName();

		return "";
	}

	protected String getTooltip(String date, double value, String scaleLabel, String indicator, String member) {
		boolean isNaN = Double.isNaN(value);
		HashMap<String, Object> map = new HashMap<String, Object>();

		map.put("date", date.replace("\"", ""));
		map.put("value", !isNaN ? NumberUtil.formatNumber(value, Language.getCurrent()) : "null");
		map.put("scale", scaleLabel);
		map.put("indicator", indicator);
		map.put("member", member);

		return this.existsDrill() ? block("row$tooltip.drill", map) : block("row$tooltip", map);
	}

	protected void refreshMeasureUnitRange(MeasureUnit measureUnit, Double value) {
		String name = measureUnit.getName();

		if (!this.minValues.containsKey(name) || this.minValues.get(name) > value)
			this.minValues.put(name, value);

		if (!this.maxValues.containsKey(name) || this.maxValues.get(name) < value)
			this.maxValues.put(name, value);
	}

}