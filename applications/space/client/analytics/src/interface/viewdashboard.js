CLASS_VIEW_DASHBOARD = "dashboard";
CSS_VIEW_DASHBOARD = ".view.dashboard";

ViewDashboard = function () {
  this.base = View;
  this.base();
};

ViewDashboard.prototype = new View;

ViewDashboard.prototype.init = function(DOMLayer) {
  var jLayer = $(DOMLayer);

  this.DOMLayer = DOMLayer;
  this.DOMLayer.innerHTML = translate(AppTemplate.viewdashboard, Lang.ViewDashboard);
  jLayer.addClass(CLASS_VIEW_DASHBOARD);
  
  if (!this.target) return;
  
  CommandListener.capture(this.DOMLayer);
  jLayer.find(".dialog").hide();
  
  if (this.state.mode != null)
    jLayer.addClass(this.state.mode);
  
  this.initLayer();
};

ViewDashboard.prototype.initLayer = function() {
  var jLayer = $(this.DOMLayer);
  var eastWidth = screen.width*.40;
  
  if (this.state.mode != State.Mode.PRINT) {
    var layout = jLayer.find(".layout").layout({
      north__size: 60,
      north__spacing_open: -1,
      north__initHidden: this.state.mode == State.Mode.EMBED,
      west__minSize: eastWidth-(eastWidth*0.60),
      west__size: eastWidth-(eastWidth*0.40),
      west__maxSize: eastWidth,
      west__spacing_open: 4,
      west__spacing_closed: 10,
      west__onresize: function (pane, $pane, paneState, paneOptions) {
        Desktop.expandWestLayer(null);
      }
    });
  }

  this.initHeader();
};

ViewDashboard.prototype.getChartType = function(jChart) {
  var className = jChart.attr("class");
  var type = className.replace("chart", "");
  
  type = type.replace("active", "");
  type = trim(type);
  
  if (type == "line") return ViewChart.LINE_CHART;
  else if (type == "bar") return ViewChart.BAR_CHART;
  else if (type == "map") return ViewChart.MAP_CHART;
  else if (type == "bubble") return ViewChart.BUBBLE_CHART;
  else if (type == "table") return ViewChart.TABLE_CHART;

  return "";
};

ViewDashboard.prototype.addBehaviours = function() {
  var jLayer = $(this.DOMLayer);
  var jContainer = jLayer.find(".ui-layout-center");
  
  jLayer.find(".toolbar > .chart a").click(ViewDashboard.prototype.atChartClick.bind(this));
  jLayer.find(".toolbar > .print a").click(ViewDashboard.prototype.atPrintClick.bind(this));
  jLayer.find(".toolbar > .download a").click(ViewDashboard.prototype.atDownloadClick.bind(this));
  jLayer.find(".reload").click(ViewDashboard.prototype.atReloadClick.bind(this));

  CommandListener.capture(jContainer);
};

ViewDashboard.prototype.refresh = function() {
  var dashboardContent = translate(AppTemplate.viewdashboardcontent, Lang.ViewDashboard);
  var jLayer = $(this.DOMLayer);
  var jContainer = jLayer.find(".ui-layout-center");
  
  this.refreshHeader();
  
  jContainer.html(dashboardContent);
  
  this.addBehaviours();
};

ViewDashboard.prototype.showNotLoaded = function() {
  $(this.DOMLayer).find(".layer.notLoaded").get(0).style.display = "block";
  $(this.DOMLayer).find(".layout").get(0).style.display = "none";
};

ViewDashboard.prototype.getIndicatorsLayer = function() {
  return $(this.DOMLayer).find(".layer.indicators").get(0);
};

ViewDashboard.prototype.getMeasureUnitsLayer = function() {
  return $(this.DOMLayer).find(".layer.measureunits").get(0);
};

ViewDashboard.prototype.getCompareLayer = function() {
  return $(this.DOMLayer).find(".layer.compare").get(0);
};

ViewDashboard.prototype.getTaxonomiesLayer = function() {
  return $(this.DOMLayer).find(".layer.taxonomies").get(0);
};

ViewDashboard.prototype.getInfoLayer = function() {
  return $(this.DOMLayer).find(".layer.info").get(0);
};

ViewDashboard.prototype.getChartLayer = function(type) {
  var jChartsContainer = $(this.DOMLayer).find(".layer.chart");
  
  if (this.state.mode != State.Mode.PRINT)
    return jChartsContainer.get(0);
  
  var jChart = jChartsContainer.find("." + type + ".chartlayeritem");
  if (jChart.length == 0) {
    var jChart = $("<div class=\"" + type + " chartlayeritem\"></div>");
    jChartsContainer.append(jChart);
  }
    
  return jChart.get(0);
};

ViewDashboard.prototype.getChartLayers = function() {
  var jChartsContainer = $(this.DOMLayer).find(".layer.chart");
  
  if (this.state.mode != State.Mode.PRINT) 
    return [jChartsContainer.get(0)];
  
  var jCharts = jChartsContainer.find(".chartlayeritem");
  var result = new Array();
  for (var i=0; i<jCharts.length; i++)
    result.push(jCharts[i].get(0));
  
  return result;
};

ViewDashboard.prototype.getUrl = function(operation) {
  var dashboard = this.target.dashboard.code;
  var chartType = State.chartType;
  var indicatorList = State.indicatorList;
  var compareList = State.compareList;
  var filterList = State.filterList;
  var rangeList = State.rangeList;
  var timeLapse = State.timeLapse;
  var range = this.getRange(null, false);
  var url = null;
  
  if (operation == "print")
    url = Kernel.getPrintDashboardUrl(dashboard, chartType, indicatorList, compareList, filterList, rangeList, range.min, range.max, timeLapse.scale, View.colors);
  else if (operation = "download")
    url = Kernel.getDownloadDashboardUrl(dashboard, "xlsformat", indicatorList, compareList, filterList, rangeList, range.min, range.max, timeLapse.scale);

  return url;
};

//************************************************************************************************************
ViewDashboard.prototype.atChartClick = function(event) {
  var jChartLink = $(event.target);
  var jChart = jChartLink.parents("li");
  var jLayer = $(this.DOMLayer);
  var timeLapse = this.state.timeLapse;

  jLayer.find(".toolbar > .chart").removeClass("active");
  jChart.addClass("active");
  
  CommandListener.throwCommand("refreshchart(" + this.target.dashboard.code + "," + this.getChartType(jChart) + "," + State.timeLapse.from + "," + State.timeLapse.to + ")");
};

ViewDashboard.prototype.atPrintClick = function(event) {
  window.open(this.getUrl("print"), "_blank", "menubar=1,scrollbars=1,width=1024");
};

ViewDashboard.prototype.atDownloadClick = function(event) {
  var downloadUrl = this.getUrl("download");
  var jLayer = $(this.DOMLayer);
  var jLink = jLayer.find(".toolbar > .download a").attr("href", downloadUrl); 
  return true;
};

ViewDashboard.prototype.atReloadClick = function(event) {
    window.location.reload(true);
};