package org.monet.space.analytics.providers.renders;

import org.monet.space.kernel.model.Dashboard;
import org.sumus.asset.Asset;
import org.sumus.dictionary.Dictionary;
import org.sumus.dimension.Member;
import org.sumus.indicator.CompositeIndicator;
import org.sumus.indicator.Indicator;
import org.sumus.indicator.IndicatorList;
import org.sumus.indicatorfigure.IndicatorFigure;
import org.sumus.query.Drill;
import org.sumus.snapshot.Snapshot;
import org.sumus.time.TimeStamp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.monet.metamodel.DashboardDefinitionBase.IndicatorProperty;
import static org.monet.metamodel.DashboardDefinitionBase.IndicatorProperty.LevelProperty.SecondaryLocationProperty;

public class GoogleMapChartProviderRender extends GoogleChartProviderRender {

	@Override
	protected void init() {
		loadCanvas("googlemapchartprovider");
		this.initRows();
		this.initChartInfo(); // this is the final step. Important !!
	}

	@Override
	protected void initChartInfo() {
		super.initChartInfo();

		HashMap<String, Object> map = new HashMap<>();
		Dashboard dashboard = (Dashboard) this.getParameter(Parameter.DASHBOARD);
		IndicatorList indicatorList = (IndicatorList) this.getParameter(DatawareHouseProviderRender.Parameter.INDICATOR_LIST);
		int countIndicators = indicatorList.size();

		if (countIndicators != 1) {
			addMark("center", block("chartInfo$center.default", map));
			return;
		}

		Indicator indicator = indicatorList.get(0);
		IndicatorProperty monetIndicator = dashboard.getDefinition().getIndicator(indicator.getName());
		IndicatorProperty.LevelProperty levelDefinition = monetIndicator.getLevel();
		if (levelDefinition == null || levelDefinition.getSecondaryLocation() == null) {
			addMark("center", block("chartInfo$center.default", map));
			return;
		}

		SecondaryLocationProperty secondaryLocation = levelDefinition.getSecondaryLocation();

		addMark("markerIcon", secondaryLocation.getMarkerIcon()!=null?secondaryLocation.getMarkerIcon().toString().toLowerCase():SecondaryLocationProperty.MarkerIconEnumeration.NONE.toString().toLowerCase());

		SecondaryLocationProperty.CenterProperty centerDefinition = secondaryLocation.getCenter();
		if (centerDefinition == null) {
			addMark("center", block("chartInfo$center.default", map));
			return;
		}

		map.put("latitude", centerDefinition.getLatitude());
		map.put("longitude", centerDefinition.getLongitude());
		map.put("zoom", centerDefinition.getZoom()!=null?centerDefinition.getZoom():block("chartInfo$zoom.default", map));

		addMark("center", block("chartInfo$center", map));
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
			HashMap<String, Object> spotMap = new HashMap<>();
			StringBuilder indicators = new StringBuilder();
			TimeStamp timeStamp = snapshot.getTimeStamp();
			int indicatorPos = 0;

			for (Indicator indicator : indicatorList) {
				HashMap<String, Object> localMap = new HashMap<>();
				IndicatorFigure indicatorFigure = snapshot.getIndicatorFigure(indicator.getName());

				if (!(indicator instanceof CompositeIndicator))
					continue;

				List<Coordinate> coordinates = this.getValuesAsCoordinate((IndicatorFigure.Composite) indicatorFigure);

				if (this.isEmpty)
					this.isEmpty = this.checkIfCoordinatesAreEmpty(coordinates);

				for (int i = 0; i < coordinates.size(); i++) {
					Coordinate value = coordinates.get(i);
					boolean isNaN = Double.isNaN(value.relevance);
					localMap.put("value", !isNaN ? value.relevance : "null");

					if (this.includeTooltips())
						localMap.put("tooltip", this.getTooltip(this.getDateIndicator(timeStamp, false, true), value.relevance, "", dictionary.getLabel(indicator), this.getMemberLabel(i)));
					else
						localMap.put("tooltip", "");

					localMap.put("x", value.x);
					localMap.put("y", value.y);
					localMap.put("comma", indicatorPos == size - 1 ? "" : "true");
					localMap.put("colorIndex", value.colorIndex);
					indicators.append(block("row$indicator.geo", localMap));
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

	private List<Coordinate> getValuesAsCoordinate(IndicatorFigure.Composite indicatorFigure) {
		Drill drill = this.chart.getDrill();
		List<Coordinate> result = new ArrayList<>();

		if (this.existsDrill()) {
			int colorIndex = 0;
			for (Member member : drill.getMembers()) {
				result.add(getCoordinate(indicatorFigure, member, colorIndex));
				colorIndex++;
			}
		} else
			result.add(getCoordinate(indicatorFigure));

		return result;
	}

	private Coordinate getCoordinate(IndicatorFigure.Composite indicatorFigure) {
		return this.getCoordinate(indicatorFigure, null, 0);
	}

	private Coordinate getCoordinate(IndicatorFigure.Composite indicatorFigure, Member member, int colorIndex) {
		CompositeIndicator compositeIndicator = (CompositeIndicator) indicatorFigure.getIndicator();
		Double x = null, y = null, relevance = null;
		int pos = 0;

		for (Indicator indicator : compositeIndicator.getIncludes()) {
			if (pos == 0)
				x = member != null ? indicatorFigure.get(indicator, member) : indicatorFigure.get(indicator);

			if (pos == 1)
				y = member != null ? indicatorFigure.get(indicator, member) : indicatorFigure.get(indicator);

			if (pos > 1)
				relevance = member != null ? indicatorFigure.get(indicator, member) : indicatorFigure.get(indicator);

			pos++;
		}

		return new Coordinate(x, y, relevance, colorIndex);
	}

	private boolean checkIfCoordinatesAreEmpty(List<Coordinate> coordinates) {
		for (Coordinate coordinate : coordinates)
			if (!Double.isNaN(coordinate.relevance))
				return false;
		return true;
	}

	private class Coordinate {
		public Double x;
		public Double y;
		public Double relevance;
		public int colorIndex;

		private Coordinate(Double x, Double y, Double relevance, int colorIndex) {
			this.x = x;
			this.y = y;
			this.relevance = relevance;
			this.colorIndex = colorIndex;
		}
	}
}