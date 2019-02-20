//----------------------------------------------------------------------
// Action show dashboard
//----------------------------------------------------------------------
function ActionShowDashboard() {
	this.base = Action;
	this.base(2);
};

ActionShowDashboard.prototype = new Action;
ActionShowDashboard.constructor = ActionShowDashboard;
CommandFactory.register(ActionShowDashboard, { dashboard: 0, view: 1, mode: 2, chartType: 3, scale: 4 }, false);

ActionShowDashboard.prototype.renderInfo = function (view) {
	var process = new ProcessRenderInfo();
	process.target = view.target;
	process.layer = view.getInfoLayer();
	process.execute();
};

ActionShowDashboard.prototype.renderIndicators = function (view) {
	var process = new ProcessRenderIndicators();
	process.target = view.target;
	process.layer = view.getIndicatorsLayer();
	process.execute();
};

ActionShowDashboard.prototype.renderMeasureUnits = function (view) {
	var process = new ProcessRenderMeasureUnits();
	process.target = view.target;
	process.layer = view.getMeasureUnitsLayer();
	process.execute();
};

ActionShowDashboard.prototype.renderTaxonomies = function (view) {
	var process = new ProcessRenderTaxonomies();
	process.target = view.target;
	process.layer = view.getTaxonomiesLayer();
	process.execute();
};

ActionShowDashboard.prototype.renderChart = function (view) {
	var process = new ProcessRenderChart();
	process.dashboardId = this.dashboard;
	process.target = view.target;

	if (this.chartType != null && this.chartType != "" && this.chartType != "null")
		process.chartType = this.chartType;

	process.layer = view.getChartLayer(State.chartType);
	process.execute();
};

ActionShowDashboard.prototype.step_1 = function () {
	var id = View.DASHBOARD.replace("::id::", this.dashboard);
	var view = Desktop.getView(id);

	if (State.chartType == null)
		State.chartType = ViewChart.LINE_CHART;

	if (view != null)
		return;

	Kernel.loadDashboard(this, this.dashboard, State.indicatorList, this.view);
};

ActionShowDashboard.prototype.step_2 = function () {
	var dashboard = (this.data != null && this.data !== "") ? this.getDashboard(this.data) : null;
	var dashboardScale = dashboard != null ? dashboard.model.scale : null;

	State.timeLapse.scale = (this.scale != null && this.scale != "" && this.scale != "null") ? this.scale : dashboardScale;

	if (this.mode != null)
		State.mode = this.mode;

	var view = new ViewDashboard();
	view.id = View.DASHBOARD.replace("::id::", this.dashboard);
	view.target = { id: this.dashboard, dashboard: dashboard };
	Desktop.addState(view);

	Desktop.cleanViewsContainer();
	view.init(Desktop.createView());
	view.refresh();

	Desktop.registerView(view.id, view);
	Desktop.hideLoading();

	if (dashboard == null) {
		view.showNotLoaded();
		return;
    }

	this.renderInfo(view);
	this.renderIndicators(view);
	this.renderMeasureUnits(view);
	this.renderTaxonomies(view);
	this.renderChart(view);

	Desktop.expandWestLayer(Desktop.getWestViews().get(0));

	this.terminate();
};

//----------------------------------------------------------------------
// Action refresh dashboard
//----------------------------------------------------------------------
function ActionRefreshDashboard() {
	this.base = Action;
	this.base(1);
};

ActionRefreshDashboard.prototype = new Action;
ActionRefreshDashboard.constructor = ActionRefreshDashboard;
CommandFactory.register(ActionRefreshDashboard, { dashboard: 0 }, false);

ActionRefreshDashboard.prototype.step_1 = function () {
	this.updateColors();

	var process = new ProcessRefreshChart();
	process.dashboardId = this.dashboard;
	process.execute();
};

//----------------------------------------------------------------------
// Action refresh chart
//----------------------------------------------------------------------
function ActionRefreshChart() {
	this.base = Action;
	this.base(1);
};

ActionRefreshChart.prototype = new Action;
ActionRefreshChart.constructor = ActionRefreshChart;
CommandFactory.register(ActionRefreshChart, { dashboard: 0, chartType: 1, from: 2, to: 3 }, false);

ActionRefreshChart.prototype.step_1 = function () {
	var viewDashboard = Desktop.getView(View.DASHBOARD.replace("::id::", this.dashboard));
	var process;

	if (this.from != "" && this.from != "null") State.timeLapse.from = parseInt(this.from);
	if (this.to != "" && this.to != "null") State.timeLapse.to = parseInt(this.to);

	if (State.chartType == null || State.chartType != this.chartType)
		process = new ProcessRenderChart();
	else
		process = new ProcessRefreshChart();

	process.dashboardId = this.dashboard;
	process.chartType = this.chartType;
	process.target = viewDashboard.target;
	process.layer = viewDashboard.getChartLayer(this.chartType);
	process.execute();
};

//----------------------------------------------------------------------
// Action toggle indicator
//----------------------------------------------------------------------
function ActionToggleIndicator() {
	this.base = Action;
	this.base(2);
};

ActionToggleIndicator.prototype = new Action;
ActionToggleIndicator.constructor = ActionToggleIndicator;
CommandFactory.register(ActionToggleIndicator, { dashboard: 0, indicator: 1, view: 2 }, false);

ActionToggleIndicator.prototype.launchRefreshProcess = function (process, dashboard) {
	process.dashboardId = dashboard.code;
	process.dashboard = dashboard;
	process.execute();
};

ActionToggleIndicator.prototype.updateMaxMetrics = function (viewIndicators) {
	var viewDashboard = Desktop.getView(View.DASHBOARD.replace("::id::", this.dashboard));
	var viewId = viewDashboard.getChartLayers()[0].id;
	var viewChart = Desktop.getView(viewId);
	viewIndicators.setMaxMetrics(viewChart.getMaxMetrics());
	viewIndicators.setMaxIndicators(viewChart.getMaxIndicators());
};

ActionToggleIndicator.prototype.getMinScale = function (dashboard) {
	var result = Scale.SECOND;
	var stateIndicatorList = State.indicatorList;

	for (var i = 0; i < dashboard.model.indicatorList.length; i++) {
		var indicator = dashboard.model.indicatorList[i];
		if (stateIndicatorList.get(indicator.id, "") == null) continue;
		for (var j = 0; j < indicator.resolutions.length; j++) {
			var resolution = Resolution.fromString(indicator.resolutions[j]);
			if (resolution < result)
				result = resolution;
		}
	}

	return result;
};

ActionToggleIndicator.prototype.step_1 = function () {
	var viewId = View.INDICATORS.replace("::id::", this.dashboard);
	var view = Desktop.getView(viewId);

	this.updateMaxMetrics(view);

	if (State.indicatorList.get(this.indicator, "")) {
		view.unselectIndicator(this.indicator);
		State.indicatorList.deleteItem(this.indicator, "");
	}
	else {
		view.selectIndicator(this.indicator);
		State.indicatorList.add(this.indicator);
	}

	Kernel.loadDashboard(this, this.dashboard, State.indicatorList, this.view);
};

ActionToggleIndicator.prototype.step_2 = function () {
	var dashboard = this.getDashboard(this.data);
	var minScale = this.getMinScale(dashboard);

	if (State.timeLapse.scale > minScale)
		State.timeLapse.scale = minScale;

	State.compareList.clear();
	this.updateColors();

	this.launchRefreshProcess(new ProcessRefreshChart(), dashboard);
	this.launchRefreshProcess(new ProcessRefreshTaxonomies(), dashboard);
	this.launchRefreshProcess(new ProcessRefreshMeasureUnits(), dashboard);
};

//----------------------------------------------------------------------
// Action unselect indicators
//----------------------------------------------------------------------
function ActionUnselectIndicators() {
	this.base = Action;
	this.base(2);
};

ActionUnselectIndicators.prototype = new Action;
ActionUnselectIndicators.constructor = ActionUnselectIndicators;
CommandFactory.register(ActionUnselectIndicators, { dashboard: 0, view: 1 }, false);

ActionUnselectIndicators.prototype.launchRefreshProcess = function (process, dashboard) {
	process.dashboardId = dashboard.code;
	process.dashboard = dashboard;
	process.execute();
};

ActionUnselectIndicators.prototype.updateMaxMetrics = function (viewIndicators) {
	var viewDashboard = Desktop.getView(View.DASHBOARD.replace("::id::", this.dashboard));
	var viewId = viewDashboard.getChartLayers()[0].id;
	var viewChart = Desktop.getView(viewId);
	viewIndicators.setMaxMetrics(viewChart.getMaxMetrics());
	viewIndicators.setMaxIndicators(viewChart.getMaxIndicators());
};

ActionUnselectIndicators.prototype.step_1 = function () {
	var viewId = View.INDICATORS.replace("::id::", this.dashboard);
	var view = Desktop.getView(viewId);

	this.updateMaxMetrics(view);
	State.indicatorList.clear();
	view.unselectIndicators();

	Kernel.loadDashboard(this, this.dashboard, State.indicatorList, this.view);
};

ActionUnselectIndicators.prototype.step_2 = function () {
	var dashboard = this.getDashboard(this.data);

	this.updateColors();

	this.launchRefreshProcess(new ProcessRefreshChart(), dashboard);
	this.launchRefreshProcess(new ProcessRefreshTaxonomies(), dashboard);
	this.launchRefreshProcess(new ProcessRefreshMeasureUnits(), dashboard);
};

//----------------------------------------------------------------------
// Action change scale
//----------------------------------------------------------------------
function ActionChangeScale() {
	this.base = Action;
	this.base(1);
};

ActionChangeScale.prototype = new Action;
ActionChangeScale.constructor = ActionChangeScale;
CommandFactory.register(ActionChangeScale, { dashboard: 0, scale: 1, from: 2, to: 3 }, false);

ActionChangeScale.prototype.step_1 = function () {

	State.timeLapse.scale = parseInt(this.scale);
	if (this.from != null && this.from != "" && this.from != "null") State.timeLapse.from = parseInt(this.from);
	if (this.to != null && this.to != "" && this.to != "null") State.timeLapse.to = parseInt(this.to);

	var process = new ProcessRefreshChart();
	process.dashboardId = this.dashboard;
	process.execute();
};

//----------------------------------------------------------------------
// Action change map layer
//----------------------------------------------------------------------
function ActionChangeMapLayer() {
	this.base = Action;
	this.base(1);
};

ActionChangeMapLayer.prototype = new Action;
ActionChangeMapLayer.constructor = ActionChangeMapLayer;
CommandFactory.register(ActionChangeMapLayer, { dashboard: 0, layer: 1 }, false);

ActionChangeMapLayer.prototype.step_1 = function () {

	State.chartLayer = this.layer;

	var process = new ProcessRefreshChart();
	process.dashboardId = this.dashboard;
	process.execute();
};

//----------------------------------------------------------------------
// Action delete filter
//----------------------------------------------------------------------
function ActionDeleteFilter() {
	this.base = Action;
	this.base(2);
};

ActionDeleteFilter.prototype = new Action;
ActionDeleteFilter.constructor = ActionDeleteFilter;
CommandFactory.register(ActionDeleteFilter, { dashboard: 0, filterId: 1 }, false);

ActionDeleteFilter.prototype.step_1 = function () {

	State.filterList.deleteItem(this.filterId);

	var process = new ProcessRefreshChart();
	process.dashboardId = this.dashboard;
	process.returnProcess = this;
	process.execute();
};

ActionDeleteFilter.prototype.step_2 = function () {
	var process = new ProcessRefreshTaxonomies();
	process.dashboardId = this.dashboard;
	process.execute();
};

//----------------------------------------------------------------------
// Action unselect filters
//----------------------------------------------------------------------
function ActionUnselectFilters() {
	this.base = Action;
	this.base(2);
};

ActionUnselectFilters.prototype = new Action;
ActionUnselectFilters.constructor = ActionUnselectFilters;
CommandFactory.register(ActionUnselectFilters, { dashboard: 0 }, false);

ActionUnselectFilters.prototype.step_1 = function () {

	State.filterList.clear();

	var process = new ProcessRefreshChart();
	process.dashboardId = this.dashboard;
	process.returnProcess = this;
	process.execute();
};

ActionUnselectFilters.prototype.step_2 = function () {
	var process = new ProcessRefreshTaxonomies();
	process.dashboardId = this.dashboard;
	process.execute();
};

//----------------------------------------------------------------------
//Action unselect categories
//----------------------------------------------------------------------
function ActionUnselectCategories() {
	this.base = Action;
	this.base(2);
};

ActionUnselectCategories.prototype = new Action;
ActionUnselectCategories.constructor = ActionUnselectCategories;
CommandFactory.register(ActionUnselectCategories, { dashboard: 0 }, false);

ActionUnselectCategories.prototype.step_1 = function () {

	State.compareList.clear();
	this.updateColors();

	var process = new ProcessRefreshChart();
	process.dashboardId = this.dashboard;
	process.returnProcess = this;
	process.execute();
};

ActionUnselectCategories.prototype.step_2 = function () {
	var process = new ProcessRefreshTaxonomies();
	process.dashboardId = this.dashboard;
	process.execute();
};

//----------------------------------------------------------------------
// Action set color
//----------------------------------------------------------------------
function ActionSetColor() {
	this.base = Action;
	this.base(1);
};

ActionSetColor.prototype = new Action;
ActionSetColor.constructor = ActionSetColor;
CommandFactory.register(ActionSetColor, { dashboard: 0, context: 1, id: 2, color: 3 }, false);

ActionSetColor.prototype.step_1 = function () {
	View.setColor(this.context, this.id, this.color);

	this.updateColors();

	var process = new ProcessRefreshChart();
	process.dashboardId = this.dashboard;
	process.execute();
};

//----------------------------------------------------------------------
// Action set measure unit mode
//----------------------------------------------------------------------
function ActionSetMeasureUnitMode() {
	this.base = Action;
	this.base(1);
};

ActionSetMeasureUnitMode.prototype = new Action;
ActionSetMeasureUnitMode.constructor = ActionSetMeasureUnitMode;
CommandFactory.register(ActionSetMeasureUnitMode, { dashboard: 0, measureUnit: 1, mode: 2 }, false);

ActionSetMeasureUnitMode.prototype.step_1 = function () {
	var range = State.rangeList.get(this.measureUnit);

	if (range == null)
		State.rangeList.add(this.measureUnit, this.mode, null, null);
	else
		range.mode = this.mode;

	var process = new ProcessRefreshChart();
	process.dashboardId = this.dashboard;
	process.execute();
};

//----------------------------------------------------------------------
// Action set measure unit range
//----------------------------------------------------------------------
function ActionSetMeasureUnitRange() {
	this.base = Action;
	this.base(1);
};

ActionSetMeasureUnitRange.prototype = new Action;
ActionSetMeasureUnitRange.constructor = ActionSetMeasureUnitRange;
CommandFactory.register(ActionSetMeasureUnitRange, { dashboard: 0, measureUnit: 1, minValue: 2, maxValue: 3 }, false);

ActionSetMeasureUnitRange.prototype.step_1 = function () {
	var range = State.rangeList.get(this.measureUnit);

	if (range == null)
		State.rangeList.add(this.measureUnit, this.mode, this.minValue, this.maxValue);
	else {
		range.min = this.minValue;
		range.max = this.maxValue;
	}

	var process = new ProcessRefreshChart();
	process.dashboardId = this.dashboard;
	process.execute();
};

//----------------------------------------------------------------------
// Action print dashboard
//----------------------------------------------------------------------
function ActionPrintDashboard() {
	this.base = Action;
	this.base(2);
};

ActionPrintDashboard.prototype = new Action;
ActionPrintDashboard.constructor = ActionPrintDashboard;
CommandFactory.register(ActionPrintDashboard, { dashboard: 0, view: 1, chartType: 2, indicators: 3, compare: 4, filters: 5, ranges: 6, from: 7, to: 8, scale: 9, colors: 10 }, false);

ActionPrintDashboard.prototype.step_1 = function () {

	if (this.chartType != "" && this.chartType != "null") State.chartType = this.chartType;
	if (this.indicators != "" && this.indicators != "null") State.indicatorList.fromJson(jQuery.parseJSON(this.indicators));
	if (this.compare != "" && this.compare != "null") State.compareList.fromJson(jQuery.parseJSON(this.compare));
	if (this.filters != "" && this.filters != "null") State.filterList.fromJson(jQuery.parseJSON(this.filters));
	if (this.from != "" && this.from != "null") State.timeLapse.from = this.from;
	if (this.to != "" && this.to != "null") State.timeLapse.to = this.to;
	if (this.scale != "" && this.scale != "null") State.timeLapse.scale = this.scale;
	if (this.ranges != "" && this.ranges != "null") State.rangeList.fromJson(jQuery.parseJSON(this.ranges));

	if (this.colors != "" && this.colors != "null") {
		View.setColors(jQuery.parseJSON(this.colors));
		this.updateColors();
	}

	var action = new ActionShowDashboard();
	action.dashboard = this.dashboard;
	action.view = this.view;
	action.mode = State.Mode.PRINT;
	action.chartType = this.chartType;
	action.scale = this.scale;
	action.returnProcess = this;
	action.execute();
};

ActionPrintDashboard.prototype.step_2 = function () {

	if (this.chartType == ViewChart.TABLE_CHART) {
		this.terminate();
		return;
	}

	var view = Desktop.getView(View.DASHBOARD.replace("::id::", this.dashboard));
	var process = new ProcessRenderChart();
	process.dashboardId = this.dashboard;
	process.target = view.target;
	process.chartType = ViewChart.TABLE_CHART;
	process.layer = view.getChartLayer(ViewChart.TABLE_CHART);
	process.execute();

	this.terminate();
};