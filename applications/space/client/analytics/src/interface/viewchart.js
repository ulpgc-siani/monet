ViewChart = function (type, configuration) {
	this.base = View;
	this.base();
	this.dialogTimeLapse = null;
	this.dialogRange = null;
	this.dialogScale = null;
	this.type = type;
	this.configuration = configuration;
	this.freeTimeLapseWindowSize = false;
};

ViewChart.LINE_CHART = "linechart";
ViewChart.BAR_CHART = "barchart";
ViewChart.MAP_CHART = "mapchart";
ViewChart.BUBBLE_CHART = "bubblechart";
ViewChart.TABLE_CHART = "tablechart";

ViewChart.prototype = new View;

ViewChart.prototype.init = function (DOMLayer) {
	this.initMode();
	this.initTimeLapse();
	this.initRange();
	this.initScale();
	this.initMessages();
};

ViewChart.prototype.initMode = function () {
	if (this.state.mode != null)
		$(this.DOMLayer).addClass(this.state.mode);
};

ViewChart.prototype.initTimeLapse = function () {
	if ($(this.DOMLayer).find(".dialogtimelapse").length == 0)
		return;

	this.dialogTimeLapse = new DialogTimeLapse();
	this.dialogTimeLapse.onChange = ViewChart.prototype.atTimeLapseChange.bind(this);
};

ViewChart.prototype.initRange = function () {
	if ($(this.DOMLayer).find(".dialogrange").length == 0)
		return;

	this.dialogRange = new DialogRange();
	this.dialogRange.onChange = ViewChart.prototype.atRangeChange.bind(this);
};

ViewChart.prototype.initScale = function () {
	if ($(this.DOMLayer).find(".dialogscale").length == 0)
		return;

	this.dialogScale = new DialogScale();
	this.dialogScale.onChange = ViewChart.prototype.atScaleChange.bind(this);
};

ViewChart.prototype.initMessages = function () {
};

ViewChart.prototype.hideMessages = function (indicatorList) {
	var jLayer = $(this.DOMLayer);
	jLayer.find(".message.selectindicator").hide();
	jLayer.find(".message.notenoughselectedindicators").hide();
	jLayer.find(".message.toomuchselectedindicators").hide();
	jLayer.find(".message.toomuchselectedmetrics").hide();
	jLayer.find(".message.comingsoon").hide();
	jLayer.find(".message.emptychart").hide();
};

ViewChart.prototype.validateIndicators = function (indicatorList) {
	var jLayer = $(this.DOMLayer);
	var indicatorsCount = indicatorList.size();
	var metricsCount = size(this.getMetrics(indicatorList));

	if (indicatorsCount == 0) {
		jLayer.find(".message.selectindicator").show();
		return false;
	}

	if (this.configuration.indicators.min != -1 && indicatorsCount < this.configuration.indicators.min) {
		jLayer.find(".message.notenoughselectedindicators").show();
		return false;
	}

	if (this.configuration.indicators.max != -1 && indicatorsCount > this.configuration.indicators.max) {
		var jMessage = jLayer.find(".message.toomuchselectedindicators");
		jMessage.html(Lang.ViewChart.TooMuchSelectedIndicators.replace("%d", this.configuration.indicators.max));
		jMessage.show();
		return false;
	}

	if (this.configuration.metrics.max != -1 && metricsCount > this.configuration.metrics.max) {
		var jMessage = jLayer.find(".message.toomuchselectedmetrics");
		jMessage.html(Lang.ViewChart.TooMuchSelectedMetrics.replace("%d", this.configuration.metrics.max));
		jMessage.show();
		return false;
	}

	if (this.configuration.comingSoon) {
		this.hideMessages();
		$(this.DOMLayer).find(".message.comingsoon").show();
	}

	return true;
};

ViewChart.prototype.getMetrics = function (indicatorList) {
	var indicatorKeys = indicatorList.getKeys();
	var usedMetrics = new Object();

	for (var i = 0; i < indicatorKeys.length; i++) {
		var indicator = this.target.dashboard.model.indicatorMap[indicatorKeys[i]];
		var metric = indicator.metric;
		if (usedMetrics[metric] != null) continue;
		usedMetrics[metric] = metric;
	}

	return usedMetrics;
};

ViewChart.prototype.getColors = function () {
	return State.colors;
};

ViewChart.prototype.renderChart = function (configuration) {
	var jLayer = $(this.DOMLayer);
	var jCanvas = jLayer.find(".canvas");
	var indicatorList = this.state.indicatorList;
	var compareList = this.state.compareList;
	var filterList = this.state.filterList;
	var rangeList = this.state.rangeList;
	var type = configuration.type;
	var datasourceUrl = Kernel.getChartUrl(this.target.id, type, indicatorList, compareList, filterList, rangeList, configuration.from, configuration.to, configuration.scale);

	if (configuration.from > configuration.to) {
		jLayer.find(".evolutionloading").hide();
		Desktop.hideLoading();
		return;
	}

	jLayer.find(".evolutionloading").show();
	this.hideMessages();
	Desktop.showLoading(Lang.ViewChart.Loading);

	jCanvas.html("");
	jCanvas.show();

	if (!this.validateIndicators(indicatorList)) {
		jLayer.find(".evolutionloading").hide();
		Desktop.hideLoading();
		return;
	}

	var jChart = $("<div></div>").attr("id", generateId());
	jCanvas.append(jChart);

	configuration.options.width = jCanvas.width();
	configuration.options.height = jCanvas.height();
	configuration.options.colors = this.getColors();
	configuration.options.imagesUrl = Context.Config.ImagesUrl;
	configuration.options.language = Context.Config.Language;
	configuration.options.metricsCount = size(this.getMetrics(indicatorList));

	this.target.api.createChart(type, jChart.get(0), datasourceUrl, configuration.options, ViewChart.prototype.atChartCreated.bind(this));
};

ViewChart.prototype.isBubbleChart = function () {
	return this.type == ViewChart.BUBBLE_CHART;
};

ViewChart.prototype.isTableChart = function () {
	return this.type == ViewChart.TABLE_CHART;
};

ViewChart.prototype.refresh = function () {

	this.refreshTimeLapse();
	this.refreshRange();
	this.refreshScale();
	this.refreshMessages();

	var stateTimeLapse = this.state.timeLapse;
	this.timeLapse = { from: stateTimeLapse.from, to: stateTimeLapse.to, scale: stateTimeLapse.scale };

	var range = this.target.dashboard.model.range;
	this.configuration = { min: range.min, max: range.max };
};

ViewChart.prototype.refreshTimeLapse = function () {
	var jLayer = $(this.DOMLayer);
	var jDialogTimeLapse = jLayer.find(".dialogtimelapse");
	var stateTimeLapse = this.state.timeLapse;
	var jCanvas = jLayer.find(".canvas");

	if (jDialogTimeLapse.length == 0)
		return;

	if (!this.validateIndicators(this.state.indicatorList)) {
		jDialogTimeLapse.hide();
		return;
	}

	jDialogTimeLapse.show();

	if (!this.requireRefreshTimeLapse() && !this.requireRefreshRange())
		return;

	this.dialogTimeLapse.state = this.state;
	this.dialogTimeLapse.target = { resolution: this.target.dashboard.model.resolution, range: this.target.dashboard.model.range, timeLapse: stateTimeLapse, width: jCanvas.width(), left: jCanvas.get(0).offsetLeft, freeWindowSize: this.freeTimeLapseWindowSize };
	this.dialogTimeLapse.render(jDialogTimeLapse.get(0));
};

ViewChart.prototype.refreshRange = function () {
	var jLayer = $(this.DOMLayer);
	var jDialogRange = jLayer.find(".dialogrange");
	var jCanvas = jLayer.find(".canvas");

	if (jDialogRange.length == 0)
		return;

	if (!this.validateIndicators(this.state.indicatorList)) {
		jDialogRange.hide();
		return;
	}

	jDialogRange.show();

	if (!this.requireRefreshRange() && !this.requireRefreshTimeLapse())
		return;

	this.dialogRange.state = this.state;
	this.dialogRange.target = { resolution: this.target.dashboard.model.resolution, range: this.target.dashboard.model.range, timeLapse: this.state.timeLapse, width: jCanvas.width() };
	this.dialogRange.render(jDialogRange.get(0));
};

ViewChart.prototype.refreshScale = function () {
	var jLayer = $(this.DOMLayer);
	var jDialogScale = jLayer.find(".dialogscale");
	var jCanvas = jLayer.find(".canvas");

	if (jDialogScale.length == 0)
		return;

	if (!this.validateIndicators(this.state.indicatorList)) {
		jDialogScale.hide();
		return;
	}

	jDialogScale.show();

	this.dialogScale.state = this.state;
	this.dialogScale.target = { resolution: this.target.dashboard.model.resolution, scale: this.state.timeLapse.scale, availableResolutions: this.getAvailableResolutions() };
	this.dialogScale.render(jDialogScale.get(0));
};

ViewChart.prototype.getAvailableResolutions = function () {
	var stateIndicatorList = this.state.indicatorList;
	var availableResolutions = {};

	for (var i = 0; i < this.target.dashboard.model.indicatorList.length; i++) {
		var indicator = this.target.dashboard.model.indicatorList[i];
		if (stateIndicatorList.get(indicator.id, "") == null) continue;
		for (var j = 0; j < indicator.resolutions.length; j++) {
			var resolution = Resolution.fromString(indicator.resolutions[j]);
			availableResolutions[resolution] = resolution;
		}
	}

	return availableResolutions;
};

ViewChart.prototype.refreshMessages = function () {
	var jMessages = $(this.DOMLayer).find(".canvasmessages");
	var stateIndicatorList = this.state.indicatorList;
	var invisibleIndicators = new Array();

	jMessages.html("");

	for (var i = 0; i < this.target.dashboard.model.indicatorList.length; i++) {
		var indicator = this.target.dashboard.model.indicatorList[i];

		if (stateIndicatorList.get(indicator.id, "") != null) {
			if (indicator.scale < this.state.timeLapse.scale)
				invisibleIndicators.push(indicator);
		}
	}

	if (invisibleIndicators.length <= 0) return;

	var messageTemplate = translate(invisibleIndicators.length == 1 ? AppTemplate.viewchartmessage : AppTemplate.viewchartmessagemultiple, Lang.ViewChart);
	var labels = this.getLabels(invisibleIndicators);
	var scale = this.calculateMaxCommonScale(invisibleIndicators);
	var jMessage = $.tmpl(messageTemplate, { labels: labels, scale: scale, dashboard: this.target.dashboard.code });
	jMessages.append(jMessage);

	CommandListener.capture(jMessages.get(0));
};

ViewChart.prototype.getMinMetrics = function () {
	return this.configuration.metrics.min;
};

ViewChart.prototype.getMaxMetrics = function () {
	return this.configuration.metrics.max;
};

ViewChart.prototype.getMinIndicators = function () {
	return this.configuration.indicators.min;
};

ViewChart.prototype.getMaxIndicators = function () {
	return this.configuration.indicators.max;
};

ViewChart.prototype.requireRefreshTimeLapse = function () {
	var stateTimeLapse = this.state.timeLapse;

	if (this.timeLapse == null) return true;
	if (this.timeLapse.scale != stateTimeLapse.scale) return true;
	if (this.timeLapse.from != null && this.timeLapse.from != stateTimeLapse.from) return true;
	if (this.timeLapse.to != null && this.timeLapse.to != stateTimeLapse.to) return true;

	return false;
};

ViewChart.prototype.requireRefreshRange = function () {
	var range = this.target.dashboard.model.range;

	if (this.configuration == null) return true;
	if (this.configuration.min != range.min) return true;
	if (this.configuration.max != range.max) return true;

	return false;
};

ViewChart.prototype.getLabels = function (invisibleIndicators) {
	var labels = "";
	for (var i = 0; i < invisibleIndicators.length; i++) {
		var indicator = invisibleIndicators[i];
		labels += i > 0 ? ",&nbsp;" : "";
		labels += indicator.label;
	}
	return labels;
}

ViewChart.prototype.calculateMaxCommonScale = function (invisibleIndicators) {
	var maxScale = Scale.YEAR;

	while (maxScale < Scale.SECOND) {
		for (var i = 0; i < invisibleIndicators.length; i++) {
			var indicator = invisibleIndicators[i];
			if (maxScale > indicator.scale)
				return maxScale - 1;
		}
		maxScale++;
	}

	return maxScale;
};

ViewChart.prototype.onResize = function () {
	if (this.state.chartType != this.type) return;
	var timeLapse = this.state.timeLapse;
	CommandListener.throwCommand("refreshchart(" + this.target.dashboard.code + "," + this.type + "," + timeLapse.from + "," + timeLapse.to + ")");
};

//************************************************************************************************************

ViewChart.prototype.atChartCreated = function (chart) {
	var jLayer = $(this.DOMLayer);

	jLayer.find(".evolutionloading").hide();
	Desktop.hideLoading();

	jLayer.find(".message.emptychart").hide();
	if (chart == null)
		return;

	if (chart.info.isEmpty)
		jLayer.find(".message.emptychart").show();

	this.chart = chart;
	this.chart.onSelect = ViewChart.prototype.atChartSelectClick.bind(this);
	this.chart.onUnselect = ViewChart.prototype.atChartUnselectClick.bind(this);

//  if (this.target.center) {
//    this.target.api.select(this.chart, this.target.center);
//  }
//  else {
//    this.target.api.unselect(this.chart);
//  }
};

ViewChart.prototype.atChartSelectClick = function (chart, selection, measureUnit) {
};

ViewChart.prototype.atChartUnselectClick = function () {
};

ViewChart.prototype.atTimeLapseChange = function (timeLapse) {
	this.refreshMessages();
	CommandListener.throwCommand("refreshchart(" + this.target.dashboard.code + "," + this.type + "," + timeLapse.from + "," + timeLapse.to + ")");
};

ViewChart.prototype.atScaleChange = function (scale, event) {
	var timeLapse = TimeLapse.toScale(this.state.timeLapse, scale);
	var values = this.getRange(timeLapse, this.freeTimeLapseWindowSize);
	CommandListener.throwCommand("changescale(" + this.target.dashboard.code + "," + scale + "," + values.min + "," + values.max + ")");
};

ViewChart.prototype.atRangeChange = function (timeStamp) {
	this.refreshMessages();
	var bounds = TimeLapse.getBounds(this.state.timeLapse, this.target.dashboard.model.range);
	var windowSize = TimeLapse.getWindowSize(this.state.timeLapse, bounds, this.freeTimeLapseWindowSize);
	var timeLapse = { from: timeStamp.value, to: timeStamp.value + windowSize.milliseconds, scale: timeStamp.scale };
	CommandListener.throwCommand("refreshchart(" + this.target.dashboard.code + "," + this.type + "," + timeLapse.from + "," + timeLapse.to + ")");
};