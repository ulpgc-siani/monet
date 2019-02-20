ViewLineChart = function() {
  this.base = ViewChart;
  this.base(ViewChart.LINE_CHART, { metrics : { min: 1, max: 2 }, indicators: { min: 1, max: -1 } });
};

ViewLineChart.prototype = new ViewChart;

ViewLineChart.prototype.init = function(DOMLayer) {
  var content = translate(AppTemplate.viewlinechart, Lang.ViewLineChart);
  var content = translate(content, Lang.ViewChart);
  
  content = replaceAll(content, "${id}", this.id);

  this.DOMLayer = DOMLayer;
  $(this.DOMLayer).html(content);
  
  if (!this.target) return;

  var jLayer = $(this.DOMLayer);
  jLayer.addClass("view chart line");
  
  this.initMode();
  this.initTimeLapse();
  this.initRange();
  this.initScale();
  this.initMessages();
};

ViewLineChart.prototype.refresh = function() {
  this.refreshTimeLapse();
  this.refreshRange();
  this.refreshScale();
  this.refreshCanvas();
  this.refreshMessages();
};

ViewLineChart.prototype.refreshCanvas = function() {
  var configuration = new Object();
  var timeLapse = this.state.timeLapse;
  var range = this.getRange(null, this.freeTimeLapseWindowSize);
  
  configuration.type = "linechart";
  configuration.from = range.min;
  configuration.to = range.max;
  configuration.scale = timeLapse.scale;
  configuration.options = new Object();
  configuration.options.chartType = "linechart";
  
  this.renderChart(configuration);
};