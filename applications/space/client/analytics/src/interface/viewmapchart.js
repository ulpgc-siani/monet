ViewMapChart = function () {
	this.base = ViewChart;
	this.base(ViewChart.MAP_CHART, { metrics: { min: 1, max: 1 }, indicators: { min: 1, max: 1 }, comingSoon: false });
};

ViewMapChart.prototype = new ViewChart;

ViewMapChart.prototype.init = function (DOMLayer) {
	var content = translate(AppTemplate.viewmapchart, Lang.ViewMapChart);
	var content = translate(content, Lang.ViewChart);

	content = replaceAll(content, "${id}", this.id);

	this.DOMLayer = DOMLayer;
	$(this.DOMLayer).html(content);

	if (!this.target) return;

	var jLayer = $(this.DOMLayer);
	jLayer.addClass("view chart map");

	this.initMode();
	this.initTimeLapse();
	this.initRange();
	this.initScale();
	this.initMapLayer();
	this.initMessages();
};

ViewMapChart.prototype.initMapLayer = function () {
	if ($(this.DOMLayer).find(".dialoglayer").length == 0)
		return;

	this.dialogMapLayer = new DialogMapLayer();
	this.dialogMapLayer.onChange = ViewChart.prototype.atLayerChange.bind(this);
};

ViewMapChart.prototype.refresh = function () {
	this.refreshTimeLapse();
	this.refreshRange();
	this.refreshScale();
	this.refreshMapLayer();
	this.refreshCanvas();
	this.refreshMessages();
};

ViewMapChart.prototype.refreshMapLayer = function () {
	var jLayer = $(this.DOMLayer);
	var jDialogMapLayer = jLayer.find(".dialoglayer");
	var jCanvas = jLayer.find(".canvas");

	if (jDialogMapLayer.length == 0)
		return;

	if (!this.validateIndicators(this.state.indicatorList)) {
		jDialogMapLayer.hide();
		return;
	}

	jDialogMapLayer.show();
	jDialogMapLayer.get(0).style.right = (jLayer.width()-jCanvas.width())/2 + "px";

	this.dialogMapLayer.state = this.state;
	this.dialogMapLayer.target = { selected: this.getChartLayer() };
	this.dialogMapLayer.render(jDialogMapLayer.get(0));
};

ViewMapChart.prototype.refreshCanvas = function () {
	var configuration = new Object();
	var timeLapse = this.state.timeLapse;
	var range = this.getRange(null, this.freeTimeLapseWindowSize);

	configuration.type = "mapchart";
	configuration.from = range.min;
	configuration.to = range.max;
	configuration.scale = timeLapse.scale;
	configuration.options = new Object();
	configuration.options.layerType = this.getChartLayer();

	this.renderChart(configuration);
};

ViewMapChart.prototype.getChartLayer = function () {
	return this.state.chartLayer!=null?this.state.chartLayer:Map.POINT;
};

//************************************************************************************************************

ViewChart.prototype.atLayerChange = function (layer) {
	CommandListener.throwCommand("changemaplayer(" + this.target.dashboard.code + "," + layer + ")");
};
