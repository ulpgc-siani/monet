//----------------------------------------------------------------------
// Process render dashboard info
//----------------------------------------------------------------------
function ProcessRenderInfo() {
    this.base = Process;
    this.base(1);
};

ProcessRenderInfo.prototype = new Process;
ProcessRenderInfo.constructor = ProcessRenderInfo;

ProcessRenderInfo.prototype.step_1 = function () {
    var viewInfo = new ViewDashboardInfo();
    var viewId = View.DASHBOARD_INFO.replace("::id::", this.target.id);

    this.layer.id = viewId;
    Desktop.registerView(viewId, viewInfo);

    viewInfo.id = viewId;
    viewInfo.target = this.target;
    Desktop.addState(viewInfo);

    viewInfo.init(this.layer);
    viewInfo.refresh();

    this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Process render indicators
//----------------------------------------------------------------------
function ProcessRenderIndicators() {
	this.base = Process;
	this.base(1);
};

ProcessRenderIndicators.prototype = new Process;
ProcessRenderIndicators.constructor = ProcessRenderIndicators;

ProcessRenderIndicators.prototype.step_1 = function () {
	var viewIndicators = new ViewIndicators();
	var viewId = View.INDICATORS.replace("::id::", this.target.id);

	this.layer.id = viewId;
	Desktop.registerView(viewId, viewIndicators);

	viewIndicators.id = viewId;
	viewIndicators.target = this.target;
	Desktop.addState(viewIndicators);

	viewIndicators.init(this.layer);
	viewIndicators.refresh();

	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Process render measure units
//----------------------------------------------------------------------
function ProcessRenderMeasureUnits() {
	this.base = Process;
	this.base(1);
};

ProcessRenderMeasureUnits.prototype = new Process;
ProcessRenderMeasureUnits.constructor = ProcessRenderMeasureUnits;

ProcessRenderMeasureUnits.prototype.step_1 = function () {
	var viewMeasureUnits = new ViewMeasureUnits();
	var viewId = View.MEASURE_UNITS.replace("::id::", this.target.id);

	this.layer.id = viewId;
	Desktop.registerView(viewId, viewMeasureUnits);

	viewMeasureUnits.id = viewId;
	viewMeasureUnits.target = this.target;
	Desktop.addState(viewMeasureUnits);

	viewMeasureUnits.init(this.layer);
	viewMeasureUnits.refresh();

	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Process refresh measure units
//----------------------------------------------------------------------
function ProcessRefreshMeasureUnits() {
	this.base = Process;
	this.base(1);
};

ProcessRefreshMeasureUnits.prototype = new Process;
ProcessRefreshMeasureUnits.constructor = ProcessRefreshMeasureUnits;

ProcessRefreshMeasureUnits.prototype.step_1 = function () {
	var viewId = View.MEASURE_UNITS.replace("::id::", this.dashboardId);
	var viewMeasureUnits = Desktop.getView(viewId);

	if (this.dashboard) viewMeasureUnits.target.dashboard = this.dashboard;
	Desktop.addState(viewMeasureUnits);
	viewMeasureUnits.refresh();

	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Process render taxonomies
//----------------------------------------------------------------------
function ProcessRenderTaxonomies() {
	this.base = Process;
	this.base(1);
};

ProcessRenderTaxonomies.prototype = new Process;
ProcessRenderTaxonomies.constructor = ProcessRenderTaxonomies;

ProcessRenderTaxonomies.prototype.step_1 = function () {
	var viewTaxonomies = new ViewTaxonomies();
	var viewId = View.TAXONOMIES.replace("::id::", this.target.id);

	this.layer.id = viewId;
	Desktop.registerView(viewId, viewTaxonomies);

	viewTaxonomies.id = viewId;
	viewTaxonomies.target = this.target;
	Desktop.addState(viewTaxonomies);

	viewTaxonomies.init(this.layer);
	viewTaxonomies.refresh();

	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Process refresh taxonomies
//----------------------------------------------------------------------
function ProcessRefreshTaxonomies() {
	this.base = Process;
	this.base(1);
};

ProcessRefreshTaxonomies.prototype = new Process;
ProcessRefreshTaxonomies.constructor = ProcessRefreshTaxonomies;

ProcessRefreshTaxonomies.prototype.step_1 = function () {
	var viewId = View.TAXONOMIES.replace("::id::", this.dashboardId);
	var viewTaxonomies = Desktop.getView(viewId);

	if (this.dashboard) viewTaxonomies.target.dashboard = this.dashboard;
	Desktop.addState(viewTaxonomies);
	viewTaxonomies.refresh();

	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Process render chart
//----------------------------------------------------------------------
function ProcessRenderChart() {
	this.base = Process;
	this.base(2);
};

ProcessRenderChart.prototype = new Process;
ProcessRenderChart.constructor = ProcessRenderChart;

ProcessRenderChart.prototype.updateMaxMetrics = function (viewIndicators) {
	var viewDashboard = Desktop.getView(View.DASHBOARD.replace("::id::", this.dashboardId));
	var viewChartId = viewDashboard.getChartLayer(this.chartType).id;
	var viewIndicatorsId = viewDashboard.getIndicatorsLayer().id;

	var viewChart = Desktop.getView(viewChartId);
	var viewIndicators = Desktop.getView(viewIndicatorsId);

	viewIndicators.setMaxMetrics(viewChart.getMaxMetrics());
	viewIndicators.setMaxIndicators(viewChart.getMaxIndicators());
	viewIndicators.checkIndicators();
};

ProcessRenderChart.prototype.checkColorsActivation = function (viewChart) {
	var viewIndicators = Desktop.getView(View.INDICATORS.replace("::id::", this.dashboardId));
	var viewTaxonomies = Desktop.getView(View.TAXONOMIES.replace("::id::", this.dashboardId));

	if (viewChart.isBubbleChart()) {
		viewIndicators.disableColors();
		viewTaxonomies.disableColors();
	}
	else {
		viewIndicators.enableColors();
		viewTaxonomies.enableColors();
	}
};

ProcessRenderChart.prototype.step_1 = function () {
	if (this.chartType == null) this.chartType = ViewChart.LINE_CHART;
	Kernel.loadChartApi(this, this.chartType);
};

ProcessRenderChart.prototype.step_2 = function () {
	var chartApi = this.getChartApi(this.data);
	var viewChart = ViewChartFactory.get(this.chartType);
	var viewId = View.CHART.replace("::id::", this.target.id).replace("::type::", this.chartType);

	this.layer.id = viewId;
	Desktop.registerView(viewId, viewChart);

	viewChart.id = viewId;
	viewChart.target = this.target;
	viewChart.target.api = chartApi;
	Desktop.addState(viewChart);

	viewChart.init(this.layer);
	viewChart.refresh();

	this.checkColorsActivation(viewChart);
	this.updateMaxMetrics();
	State.chartType = this.chartType;

	var viewDashboardInfo = Desktop.getView(View.DASHBOARD_INFO.replace("::id::", this.dashboardId));
	viewDashboardInfo.target.freeTimeLapseWindowSize = viewChart.isTableChart();
	viewDashboardInfo.refresh();

	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Process refresh chart
//----------------------------------------------------------------------
function ProcessRefreshChart() {
	this.base = Process;
	this.base(1);
};

ProcessRefreshChart.prototype = new Process;
ProcessRefreshChart.constructor = ProcessRefreshChart;

ProcessRefreshChart.prototype.step_1 = function () {
	var viewDashboard = Desktop.getView(View.DASHBOARD.replace("::id::", this.dashboardId));
	var chartLayers = this.chartType != null ? [viewDashboard.getChartLayer(this.chartType)] : viewDashboard.getChartLayers();
	var isTableChart = false;

	for (var i = 0; i < chartLayers.length; i++) {
		var viewId = chartLayers[i].id;
		var viewChart = Desktop.getView(viewId);

		if (i == 0)
			isTableChart = viewChart.isTableChart();

		if (this.dashboard) viewChart.target.dashboard = this.dashboard;
		Desktop.addState(viewChart);
		viewChart.refresh();
	}

	var viewIndicators = Desktop.getView(View.INDICATORS.replace("::id::", this.dashboardId));
	if (State.compareList.size() > 0 && State.indicatorList.size() == 1)
		viewIndicators.disableColors();
	else
		viewIndicators.enableColors();

	var viewDashboardInfo = Desktop.getView(View.DASHBOARD_INFO.replace("::id::", this.dashboardId));
	viewDashboardInfo.target.freeTimeLapseWindowSize = isTableChart;
	viewDashboardInfo.refresh();

	this.terminateOnSuccess();
};
