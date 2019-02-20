Desktop = new Object;
Desktop.views = new Array();
Desktop.jCurrentWestView = null;

Desktop.MAIN_LOADING = "#mainLoading";
Desktop.LOADING_BOX = "#loadingBox";
Desktop.VIEWS_CONTAINER = "#viewsContainer";
Desktop.WEST_LAYER = "#westlayer";

Desktop.init = function () {
	var html = AppTemplate.desktop;
	html = translate(html, Lang.Desktop);

	document.body.innerHTML = html;

	var jMainLoading = $(Desktop.MAIN_LOADING);
	jMainLoading.get(0).innerHTML = Lang.Loading;
	jMainLoading.show();

	$(window).resize(Desktop.resize.bind(this));
};

Desktop.showLoading = function (message) {
	if (message == null) message = Lang.Loading;
	$(Desktop.LOADING_BOX).html(message).show();
};

Desktop.hideLoading = function () {
	$(Desktop.LOADING_BOX).hide();
};

Desktop.showMessageBox = function (title, summary, className, miliseconds) {
	alert(title + " - " + summary);
};

Desktop.hideMessageBox = function () {
};

Desktop.showMask = function () {
};

Desktop.hideMask = function () {
};

Desktop.reportProgress = function (message, modal) {
	this.showMask();
};

Desktop.reportInfo = function (message) {
	Desktop.showMessageBox(Lang.Information, message, 'info', 5000);
};

Desktop.reportError = function (message) {
	Desktop.showMessageBox(Lang.Error, message, 'error', 5000);
};

Desktop.reportWarning = function (message) {
	Desktop.showMessageBox(Lang.Warning, message, 'warning', 5000);
};

Desktop.reportSuccess = function (message) {
	Desktop.showMessageBox(Lang.Information, message, 'success', 5000);
};

Desktop.hideReports = function () {
	this.hideMask();
};

Desktop.hideProgress = function () {
	this.hideMask();
};

Desktop.cleanViewsContainer = function () {
	$(Desktop.VIEWS_CONTAINER).html("");
};

Desktop.createView = function () {
	var idView = generateId();
	$(Desktop.VIEWS_CONTAINER).append("<div id='" + idView + "' class='view'></div>");
	return $("#" + idView).get(0);
};

Desktop.refresh = function () {
};

Desktop.registerView = function (id, view) {
	if (this.views.length == 0) $(Desktop.MAIN_LOADING).hide();
	this.views[id] = view;
};

Desktop.getView = function (id) {
	return this.views[id];
};

Desktop.getTabTitle = function (dimension, indicator) {
	if (indicator != null) return indicator.label;
	return dimension.label;
};

Desktop.getTabSubTitle = function (period, dimension, level) {
	return (level != null) ? level.label : dimension.label;
};

Desktop.getWestViews = function () {
	return $(Desktop.WEST_LAYER).find("._expansible");
};

Desktop.hideWestLayer = function () {
	$(Desktop.WEST_LAYER).hide();
};

Desktop.expandWestLayer = function (DOMView) {
	if (DOMView == null) return;

	var jWest = $(Desktop.WEST_LAYER);
	var jViews = jWest.find("._expansible");
	var jView = DOMView != null ? $(DOMView) : Desktop.jCurrentWestView;
	var height = jWest.height();
	var numViews = jViews.length;
	var reservedPercentage = 0.20 * (numViews - 1);
	var activeViewHeight = Math.round(height * (1 - reservedPercentage - 0.1));
	var inactiveViewHeight = Math.round(height * 0.20);

	jViews.removeClass("expanded");

	if (jView.height() == activeViewHeight)
		return;

	jViews.height(inactiveViewHeight);
	jView.addClass("expanded");
	jView.animate({ height: activeViewHeight });

	Desktop.jCurrentWestView = jView;
};

Desktop.addState = function (object) {
	object.state = new Object();
	object.state.chartType = State.chartType;
	object.state.chartLayer = State.chartLayer;
	object.state.mode = State.mode;
	object.state.indicatorList = State.indicatorList;
	object.state.compareList = State.compareList;
	object.state.filterList = State.filterList;
	object.state.timeLapse = State.timeLapse;
	object.state.rangeList = State.rangeList;
};

Desktop.doResize = function () {
	for (var viewId in this.views) {
		if (isFunction(this.views[viewId])) continue;
		Desktop.addState(this.views[viewId]);
		this.views[viewId].onResize();
	}
};

Desktop.resize = function () {
	if (Desktop.resizeTimeout != null)
		window.clearTimeout(Desktop.resizeTimeout);

	Desktop.resizeTimeout = window.setTimeout(Desktop.doResize.bind(this), 500);
};