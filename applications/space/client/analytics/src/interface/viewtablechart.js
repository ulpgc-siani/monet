ViewTableChart = function() {
  this.base = ViewChart;
  this.base(ViewChart.TABLE_CHART, { metrics : { min: 1, max: -1 }, indicators: { min: 1, max: -1 } });
  this.freeTimeLapseWindowSize = true;
};

ViewTableChart.prototype = new ViewChart;

ViewTableChart.prototype.init = function(DOMLayer) {
  var content = translate(AppTemplate.viewtablechart, Lang.ViewTableChart);
  var content = translate(content, Lang.ViewChart);
  
  content = replaceAll(content, "${id}", this.id);

  this.DOMLayer = DOMLayer;
  $(this.DOMLayer).html(content);
  
  if (!this.target) return;

  var jLayer = $(this.DOMLayer);
  jLayer.addClass("view chart table");
  
  this.initMode();
  this.initTimeLapse();
  this.initRange();
  this.initScale();
  this.initMessages();
};

ViewTableChart.prototype.refresh = function() {
  this.refreshTimeLapse();
  this.refreshRange();
  this.refreshScale();
  this.refreshCanvas();
  this.refreshMessages();
};

ViewTableChart.prototype.refreshCanvas = function() {
  var configuration = new Object();
  var timeLapse = this.state.timeLapse;
  var range = this.getRange(null, this.freeTimeLapseWindowSize);
  
  configuration.type = "tablechart";
  configuration.from = range.min;
  configuration.to = range.max;
  configuration.scale = timeLapse.scale;
  configuration.options = new Object();
  configuration.options.chartType = "tablechart";
  
  this.renderChart(configuration);
};