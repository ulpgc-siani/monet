ViewBubbleChart = function() {
  this.base = ViewChart;
  this.base(ViewChart.BUBBLE_CHART, { metrics : { min: 1, max: 4 }, indicators: { min: 2, max: 4 } });
};

ViewBubbleChart.prototype = new ViewChart;

ViewBubbleChart.prototype.init = function(DOMLayer) {
  var content = translate(AppTemplate.viewbubblechart, Lang.ViewBubbleChart);
  var content = translate(content, Lang.ViewChart);

  content = replaceAll(content, "${id}", this.id);

  this.DOMLayer = DOMLayer;
  $(this.DOMLayer).html(content);
  
  if (!this.target) return;

  var jLayer = $(this.DOMLayer);
  jLayer.addClass("view chart bubble");
  
  this.initMode();
  this.initTimeLapse();
  this.initRange();
  this.initScale();
  this.initMessages();
};

ViewBubbleChart.prototype.getColors = function() {
  return null;
}

ViewBubbleChart.prototype.refresh = function() {
  this.refreshTimeLapse();
  this.refreshRange();
  this.refreshScale();
  this.refreshCanvas();
  this.refreshMessages();
};

ViewBubbleChart.prototype.refreshCanvas = function() {
  var configuration = new Object();
  var timeLapse = this.state.timeLapse;
  var range = this.getRange(null, this.freeTimeLapseWindowSize);
  
  configuration.type = "bubblechart";
  configuration.from = range.min;
  configuration.to = range.max;
  configuration.scale = timeLapse.scale;
  configuration.options = new Object();
  configuration.options.chartType = "bubblechart";
  
  this.renderChart(configuration);
};