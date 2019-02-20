ChartApi = new Object();
ChartApi.onChartClick = null;

ChartApi.chartInstance = function (type, DOMContainer) {
	if (type == "linechart") return new google.visualization.LineChart(DOMContainer);
	else if (type == "barchart") return new google.visualization.ColumnChart(DOMContainer);
	else if (type == "tablechart") return new google.visualization.Table(DOMContainer);
	else if (type == "bubblechart") return new google.visualization.BubbleChart(DOMContainer);
	else if (type == "mapchart") return new google.maps.Map(DOMContainer);
}

ChartApi.createChart = function (type, DOMContainer, datasourceUrl, options, onCreateCallback) {
	$.ajax({url: datasourceUrl, async: true, type: 'POST'}).done(this.createChartCallback.bind(this, type, DOMContainer, options, onCreateCallback));
};

ChartApi.createChartCallback = function (type, DOMContainer, options, onCreateCallback, serializedData) {
	if (type == "mapchart") return this.createMapChartCallback(type, DOMContainer, options, onCreateCallback, serializedData);
	else return this.createVisualizationChartCallback(type, DOMContainer, options, onCreateCallback, serializedData);
};

ChartApi.getMinValue = function (chart, measureUnit) {
	for (var i = 0; i < chart.info.VerticalAxis.length; i++) {
		var verticalAxis = chart.info.VerticalAxis[i];
		if (verticalAxis.measureUnit == measureUnit)
			return verticalAxis.minValue;
	}
	return null;
};

ChartApi.getMaxValue = function (chart, measureUnit) {
	for (var i = 0; i < chart.info.VerticalAxis.length; i++) {
		var verticalAxis = chart.info.VerticalAxis[i];
		if (verticalAxis.measureUnit == measureUnit)
			return verticalAxis.maxValue;
	}
	return null;
};

// EVENTS

ChartApi.atChartReady = function (chart, onCreateCallback) {
	if (onCreateCallback) onCreateCallback(chart);
};

ChartApi.atChartClick = function (chart) {
	var selection = chart.getSelection();

	if (selection.length == 0) {
		if (chart.onUnselect) chart.onUnselect(chart);
		return;
	}

	var data = chart.data;
	var value = data.getValue(selection[0].row, selection[0].column);
	var measureUnit = this.getMeasureUnit(chart, selection[0].column);

	if (chart.onSelect) chart.onSelect(chart, { row: selection[0].row, column: selection[0].column, value: value }, measureUnit);
};

// PRIVATE

ChartApi.select = function (chart, selection) {
	chart.setSelection([
		{row: selection.row, column: selection.column}
	]);
};

ChartApi.getMeasureUnit = function (chart, columnPos) {
	var info = chart.info;
	for (var i = 0; i < info.IndicatorsAxisMember.length; i++) {
		if (info.IndicatorsAxisMember[i].columnPosition == columnPos) {
			var axisPosition = info.IndicatorsAxisMember[i].axisPosition;
			return info.VerticalAxis[axisPosition].measureUnit;
		}
	}
	return null;
};

ChartApi.addVerticalAxis = function (verticalAxisInfo) {
	var result = { title: verticalAxisInfo.label, textPosition: 'in', textStyle: { color: '#666', fontSize: 12 }, gridlines: { color: '#EEE' }};

	var minValue = verticalAxisInfo.minValue;
	var maxValue = verticalAxisInfo.maxValue;
	if (minValue != null && maxValue != null && minValue < maxValue) {
		result.viewWindowMode = "explicit";
		result.viewWindow = { min: minValue, max: maxValue };
	}

	var useBaseLine = verticalAxisInfo.useBaseLine;
	if (useBaseLine) {
		result.baseline = -100;
		result.baselineColor = "black";
	}

	return result;
};

ChartApi.createVisualizationChartCallback = function(type, DOMContainer, options, onCreateCallback, serializedData) {
	var chart = null;
	var chartInfo;
	var dataTable;

	if (serializedData != "")
		eval(serializedData);

	if (chart == null)
		chart = this.chartInstance(type, DOMContainer);

	chart.data = dataTable;
	chart.info = chartInfo;

	if (chartInfo.isEmpty) {
		if (onCreateCallback) onCreateCallback(chart);
		return;
	}

	google.visualization.events.addListener(chart, 'select', this.atChartClick.bind(this, chart));
	google.visualization.events.addListener(chart, 'ready', this.atChartReady.bind(this, chart, onCreateCallback));

	options.curveType = "none";
	options.axisTitlesPosition = 'out';
	options.legend = { position: 'bottom' };
	options.pointSize = 5;
	options.tooltip = { isHtml: true };
	options.hAxis = { slantedText: true, textPosition: 'in' };
	options.hAxis.textStyle = { color: '#666', fontSize: 12 };
	options.hAxis.gridlines = { color: '#EEE' };

	if (options.metricsCount != null) {
		if (options.metricsCount == 1) {
			options.chartArea = { left: 30, top: 0, right: 0, bottom: 0, width: "100%", height: "90%"};
			options.vAxis = this.addVerticalAxis(chartInfo.VerticalAxis[0]);
		}
		else {
			options.chartArea = { left: 30, top: 0, right: 0, bottom: 0, width: "95%", height: "90%"};
			options.vAxes = {};
			for (var i = 0; i < chartInfo.VerticalAxis.length; i++) {
				options.vAxes[i] = this.addVerticalAxis(chartInfo.VerticalAxis[i]);
			}
			options.series = {};
			for (var i = 0; i < chartInfo.IndicatorsAxisMember.length; i++) {
				var axisMemberInfo = chartInfo.IndicatorsAxisMember[i];
				options.series[i] = { targetAxisIndex: axisMemberInfo.axisPosition };
			}
		}
	}
	else {
		options.chartArea = { left: 30, top: 0, right: 0, bottom: 0, width: "100%", height: "90%"};
		options.vAxis = this.addVerticalAxis({ label: 'label', minValue: 0, maxValue: 0 });
	}

	chart.draw(dataTable, options);

	if (serializedData == "" || type == "mapchart")
		this.atChartReady(null, onCreateCallback);

	return chart;
};

ChartApi.createMapChartCallback = function(type, DOMContainer, options, onCreateCallback, serializedData) {
	var chart = null;
	var chartInfo;
	var dataTable;

	if (serializedData != "")
		eval(serializedData);

	DOMContainer.style.height = "100%";

	if (chart == null)
		chart = this.chartInstance(type, DOMContainer);

	chart.data = dataTable;
	chart.info = chartInfo;

	if (chartInfo.isEmpty) {
		if (onCreateCallback) onCreateCallback(chart);
		return;
	}

	this.addLayerToMap(chart, options);
	this.atChartReady(chart, onCreateCallback);

	return chart;
};

ChartApi.addLayerToMap = function(chart, options) {
	if (options.layerType == null)
		return this.addDefaultLayerToMap(chart, options);

	if (options.layerType == "heatlayer") return this.addHeatLayerToMap(chart, options);

	return this.addDefaultLayerToMap(chart, options);
};

ChartApi.addHeatLayerToMap = function(chart, options) {
	var center = chart.info.center;

	var mapOptions = {
		zoom: center.zoom,
		center: new google.maps.LatLng(center.latitude, center.longitude)
	};

	chart.setOptions(mapOptions);

	var googleList = new Array();
	for (var i=0; i<chart.data.length; i++)
		googleList.push(chart.data[i].position);

	var pointArray = new google.maps.MVCArray(googleList);
	var heatMap = new google.maps.visualization.HeatmapLayer({data: pointArray});
	heatMap.setMap(chart);
};

ChartApi.addDefaultLayerToMap = function(chart, options) {
	var center = chart.info.center;

	var mapOptions = {
		zoom: center.zoom,
		center: new google.maps.LatLng(center.latitude, center.longitude)
	};

	chart.setOptions(mapOptions);

	for (var i=0; i<chart.data.length; i++) {
		var chartData = chart.data[i];

		new Marker({
			map: chart,
			title: chartData.label,
			label: '<i class="' + this.getIconStyle(chart.info.markerIcon) + '"></i>',
			position: chartData.position,
			zIndex: 9,
			icon: {
				path: MAP_PIN,
				fillColor: options.colors[chartData.colorIndex],
				fillOpacity: 1,
				strokeColor: '',
				strokeWeight: 0,
				scale: 1/4
			}
		});
	}

};

ChartApi.getIconStyle = function(markerIcon) {
	if (markerIcon == "human") return "map-icon-walking";
	else if (markerIcon == "bicycle") return "map-icon-bicycling";
	else if (markerIcon == "car") return "map-icon-taxi-stand";
	else if (markerIcon == "bus") return "map-icon-bus-station";
	else if (markerIcon == "truck") return "map-icon-moving-company";
	else if (markerIcon == "water_vehicle") return "map-icon-boat-tour";
	else if (markerIcon == "airplane") return "map-icon-airport";
	else if (markerIcon == "train") return "map-icon-train-station";
	return "map-icon-circle";
};