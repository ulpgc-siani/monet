var SECOND = 1000;
var MINUTE = SECOND*60;
var HOUR = MINUTE*60;
var DAY = HOUR*24;
var WEEK = DAY*7;
var MONTH = DAY*30;
var YEAR = MONTH*12;

DialogRange = function() {
  this.base = Dialog;
  this.base();
  this.lastTimeStamp = null;
};

DialogRange.prototype = new Dialog;

DialogRange.prototype.render = function(DOMLayer) {
  var content = translate(AppTemplate.dialogrange, Lang.DialogRange);

  content = replaceAll(content, "${id}", this.id);
  
  this.DOMLayer = DOMLayer;
  this.DOMLayer.innerHTML = content;
  
  if (!this.target) return;
  
  if (this.target.width) {
    var jLayer = $(this.DOMLayer);
    jLayer.width(this.target.width-parseInt(jLayer.css("margin-left").replace("px",""))+5);
  }

  CommandListener.capture(this.DOMLayer);
  
  this.init();
  this.refresh();
};

DialogRange.prototype.init = function() {
  var jLayer = $(this.DOMLayer);
  var range = this.getRange();

  this.jSlider = jLayer.find(".slider");
  
  this.jSlider.slider({
    min: range.min,
    max: range.max,
    step: 1,
    change: DialogRange.prototype.atChange.bind(this),
    slide: DialogRange.prototype.atSlide.bind(this)
  });
  
  this.initBehaviours();
};

DialogRange.prototype.initBehaviours = function() {
  var jLayer = $(this.DOMLayer);
  
  var jPlay = jLayer.find(".controls .play");
  jPlay.click(DialogRange.prototype.atPlayClick.bind(this));

  var jPause = jLayer.find(".controls .pause");
  jPause.click(DialogRange.prototype.atPauseClick.bind(this));
};

DialogRange.prototype.refresh = function() {
  var timeStamp = this.getTimeStamp();
  
  if (this.target.timeLapse.from != null)
    timeStamp = this.target.timeLapse.from;
  
  this.refreshTitle(timeStamp);
  this.refreshSlider();
  this.refreshControls();
};

DialogRange.prototype.refreshTitle = function(timeStamp) {
  var jLayer = $(this.DOMLayer);
  var jTitle = jLayer.find(".title span");
  jTitle.html(this.getFormattedDate(new Date(timeStamp)));
};

DialogRange.prototype.refreshSlider = function() {
  var range = this.getRange();
  var value = this.getValue();
  
  this.jSlider.slider("option", "min", range.min);
  this.jSlider.slider("option", "max", range.max);
  
  if (value > range.max) value = range.max;
  this.eventsDisabled = true;
  this.jSlider.slider("option", "value", value);
  this.eventsDisabled = false;
  
  this.jSlider.slider("option", "disabled", range.max <= 1);
};

DialogRange.prototype.refreshControls = function() {
  var jLayer = $(this.DOMLayer);
  var jPlay = jLayer.find(".controls .play");
  var jPause = jLayer.find(".controls .pause");
  var value = this.jSlider.slider("option", "value");

  jPlay.css("display", (!this.playing)?"block":"none");
  jPause.css("display", (this.playing)?"block":"none");
  
  jPlay.removeClass("disabled");
  
  var max = this.jSlider.slider("option", "max");
  if (max <= 1)
    jPlay.addClass("disabled");
  else if (!this.playing) {
    if (value >= max)
      jPlay.addClass("disabled");
  }
};

DialogRange.prototype.notifyChange = function(value) {
  var timeStamp = { value: this.toTimeStamp(value), scale: this.target.timeLapse.scale };
  
  if (this.lastTimeStamp != null) {
    var time = timeStamp.value;
    var lastTime = this.lastTimeStamp.value;
    if (time == lastTime && timeStamp.scale == this.lastTimeStamp.scale) return;
  }
  
  this.refreshTitle(this.getTimeStamp(value));
  this.refreshControls();
  
  if (this.onChange) this.onChange(timeStamp);
  this.lastTimeStamp = { value: timeStamp.value, scale: timeStamp.scale };
};

DialogRange.prototype.play = function() {
  
  this.playing = true;

  if (this.canContinuePlaying && !this.canContinuePlaying()) {
    this.playTimeout = window.setTimeout(DialogRange.prototype.play.bind(this), 3000);
    return;
  }
  
  var value = this.jSlider.slider('option', 'value');
  var max = this.jSlider.slider('option', 'max');
  
  value++;
  if (value >= max) {
    this.jSlider.slider('option', 'value', max);
    this.pause();
    return;
  }
  
  this.jSlider.slider('option', 'value', value);
  this.playTimeout = window.setTimeout(DialogRange.prototype.play.bind(this), 1200);
};

DialogRange.prototype.pause = function() {
  window.clearTimeout(this.playTimeout);
  this.playing = false;
  this.refreshControls();
};

DialogRange.prototype.getFormattedDate = function(date) {
  var scale = this.target.timeLapse.scale;
  return date.format(Lang.DateFormats[scale], Context.Config.Language);
};

DialogRange.prototype.getValue = function() {
  var value = this.jSlider.slider("option", "value");
  
  if (this.target.timeLapse.from != null) {
    var factor = this.getTimeStampFactor();
    value = Math.round((this.target.timeLapse.from-this.target.range.min)/factor);
  }
  
  return value;
};

DialogRange.prototype.getTimeStamp = function(value) {
  value = value!=null?value:this.jSlider.slider("option", "value");
  return this.toTimeStamp(value);
};

DialogRange.prototype.getTimeStampFactor = function() {
  var scale = this.target.timeLapse.scale;
  var factor = 1;
  
  if (scale == Scale.YEAR) factor = YEAR;
  else if (scale == Scale.MONTH) factor = MONTH;
  else if (scale == Scale.DAY) factor = DAY;
  else if (scale == Scale.HOUR) factor = HOUR;
  else if (scale == Scale.MINUTE) factor = MINUTE;
  else if (scale == Scale.SECOND) factor = SECOND;

  return factor;
};

DialogRange.prototype.getRange = function() {
  var range = this.target.range;
  var count = range.max-range.min;
  var factor = this.getTimeStampFactor();
  return { min: 0, max: Math.round(count/factor) };
};  

DialogRange.prototype.toTimeStamp = function(value) {
  var factor = this.getTimeStampFactor();
  return this.target.range.min+(value*factor);
};

//************************************************************************************************************

DialogRange.prototype.atChange = function(event, ui) {
  if (this.eventsDisabled) return true;
  this.notifyChange(ui.value);
};

DialogRange.prototype.atSlide = function(event, ui) {
  if (this.eventsDisabled) return true;
  this.refreshTitle(this.getTimeStamp(ui.value));
};

DialogRange.prototype.atPlayClick = function() {
  var value = this.jSlider.slider('option','value');
  var max = this.jSlider.slider('option', 'max');
  if (value >= max) return;
  window.setTimeout(DialogRange.prototype.play.bind(this), 2000);
  this.playing = true;
  this.refreshControls();
};

DialogRange.prototype.atPauseClick = function() {
  this.pause();
};