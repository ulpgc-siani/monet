DialogTimeLapse = function() {
  this.base = Dialog;
  this.base();
  this.lastTimeLapse = null;
  this.dialogScale = null;
  this.page = 0;
};

DialogTimeLapse.prototype = new Dialog;

DialogTimeLapse.prototype.render = function(DOMLayer) {
  var content = translate(AppTemplate.dialogtimelapse, Lang.DialogTimeLapse);

  content = replaceAll(content, "${id}", this.id);
  
  this.DOMLayer = DOMLayer;
  this.DOMLayer.innerHTML = content;
  
  if (!this.target) return;
  
  if (this.target.width) {
    var jLayer = $(this.DOMLayer);
    jLayer.width(this.target.width);
    jLayer.css("margin-left", this.target.left);
  }

  CommandListener.capture(this.DOMLayer);
  
  this.init();
  this.refresh();
};

DialogTimeLapse.prototype.init = function() {
  var jLayer = $(this.DOMLayer);
  var bounds = TimeLapse.getBounds(this.target.timeLapse, this.target.range);
  var windowSize = TimeLapse.getWindowSize(this.target.timeLapse, bounds, this.isFreeWindowSize());
  var values = TimeLapse.getValues(this.target.timeLapse, windowSize, bounds);
  
  if (this.state.mode != null)
    jLayer.find(".dialog.timelapse").addClass(this.state.mode);

  this.jSlider = jLayer.find(".slider");
  
  this.jSlider.dateRangeSlider({
    bounds: { min: new Date(bounds.min), max: new Date(bounds.max) },
    range: windowSize,
    arrows: false,
    defaultValues: { min: new Date(values.min), max: new Date(values.max) },
    formatter: DialogTimeLapse.prototype.getFormattedDate.bind(this),
    step: 1,
    wheelMode: "zoom"
  });
  
  this.jSlider.on("userValuesChanged", DialogTimeLapse.prototype.atChange.bind(this));
  this.jSlider.on("userValuesChanging", DialogTimeLapse.prototype.atChanging.bind(this));
};

DialogTimeLapse.prototype.refresh = function(jsonData) {
  var jLayer = $(this.DOMLayer);
  var bounds = TimeLapse.getBounds(this.target.timeLapse, this.target.range);
  var windowSize = TimeLapse.getWindowSize(this.target.timeLapse, bounds, this.isFreeWindowSize());
  var values = TimeLapse.getValues(this.target.timeLapse, windowSize, bounds);
  
  jLayer.find(".bounds.min").html(this.getFormattedDate(new Date(bounds.min)));
  jLayer.find(".bounds.max").html(this.getFormattedDate(new Date(bounds.max)));

  this.jSlider = jLayer.find(".slider");
  this.jSlider.dateRangeSlider("option", "bounds", { min: new Date(bounds.min), max: new Date(bounds.max) });
  this.jSlider.dateRangeSlider("option", "range", windowSize);
  this.jSlider.dateRangeSlider('values', new Date(values.min), new Date(values.max));
};

DialogTimeLapse.prototype.notifyChange = function(timeLapse) {
  
  if (this.lastTimeLapse != null) {
    var fromTime = timeLapse.from;
    var lastFromTime = this.lastTimeLapse.from;
    var toTime = timeLapse.to;
    var lastToTime = this.lastTimeLapse.to;
    if (fromTime == lastFromTime && toTime == lastToTime && timeLapse.scale == this.lastTimeLapse.scale) return;
  }
  
  if (this.onChange) this.onChange(timeLapse);
  
  this.lastTimeLapse = timeLapse;
};

DialogTimeLapse.prototype.getFormattedDate = function(date) {
  var scale = this.target.timeLapse.scale;
  return date.format(Lang.DateFormats[scale], Context.Config.Language);
};

DialogTimeLapse.prototype.isFreeWindowSize = function() {
  return this.target.freeWindowSize?this.target.freeWindowSize:false;
};

//************************************************************************************************************

DialogTimeLapse.prototype.atChange = function(event, data) {
  if (this.eventsDisabled) return true;
  if (data != null) {
    this.notifyChange({ from: data.values.min.getTime(), to: data.values.max.getTime(), scale: this.target.timeLapse.scale });
    this.refresh();
  }
};

DialogTimeLapse.prototype.atChanging = function(event, data) {
};