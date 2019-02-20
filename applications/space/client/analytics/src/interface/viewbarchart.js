ViewBarChart = function() {
  this.base = ViewChart;
  this.base(ViewChart.BAR_CHART, { metrics : { min: 1, max: 2 }, indicators: { min: 1, max: -1 } });
};

ViewBarChart.prototype = new ViewChart;

ViewBarChart.prototype.init = function(DOMLayer) {
  var content = translate(AppTemplate.viewbarchart, Lang.ViewBarChart);
  var content = translate(content, Lang.ViewChart);
  
  content = replaceAll(content, "${id}", this.id);

  this.DOMLayer = DOMLayer;
  $(this.DOMLayer).html(content);
  
  if (!this.target) return;

  var jLayer = $(this.DOMLayer);
  jLayer.addClass("view chart bar");
  
  this.initMode();
  this.initTimeLapse();
  this.initRange();
  this.initScale();
  this.initMessages();
};

ViewBarChart.prototype.refresh = function() {
  this.refreshTimeLapse();
  this.refreshRange();
  this.refreshScale();
  this.refreshCanvas();
  this.refreshMessages();
};

ViewBarChart.prototype.refreshCanvas = function() {
  var configuration = new Object();
  var timeLapse = this.state.timeLapse;
  var range = this.getRange(null, this.freeTimeLapseWindowSize);
  
  configuration.type = "barchart";
  configuration.from = range.min;
  configuration.to = range.max;
  configuration.scale = timeLapse.scale;
  configuration.options = new Object();
  configuration.options.chartType = "barchart";
  
  this.renderChart(configuration);
};