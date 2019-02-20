DialogScale = function() {
  this.base = Dialog;
  this.base();
};

DialogScale.prototype = new Dialog;

DialogScale.prototype.render = function(DOMLayer) {
  var content = translate(AppTemplate.dialogscale, Lang.DialogScale);

  content = replaceAll(content, "${id}", this.id);
  
  this.DOMLayer = DOMLayer;
  this.DOMLayer.innerHTML = content;
  
  if (!this.target) return;

  CommandListener.capture(this.DOMLayer);
  
  this.init();
  this.refresh();
};

DialogScale.prototype.isResolutionAvailable = function(resolutions) {
  if (size(this.target.availableResolutions) == 0)
    return true;

  for (var i=0; i<resolutions.length; i++) {
    var resolution = resolutions[i];
    if (this.target.availableResolutions[resolution] != null)
      return true;
  }

  return false;
}

DialogScale.prototype.isSecondScaleVisible = function() {
  return (this.target.resolution == Scale.SECOND && this.isResolutionAvailable([Resolution.SECONDS]));
};

DialogScale.prototype.isMinuteScaleVisible = function() {
  var isAvailable = this.isResolutionAvailable([Resolution.SECONDS, Resolution.MINUTES]);
  if (this.target.resolution == Scale.SECOND && isAvailable) return true;
  if (this.target.resolution == Scale.MINUTE && isAvailable) return true;
  return false;
};

DialogScale.prototype.isHourScaleVisible = function() {
  var isAvailable = this.isResolutionAvailable([Resolution.SECONDS, Resolution.MINUTES, Resolution.HOURS]);
  if (this.target.resolution == Scale.SECOND && isAvailable) return true;
  if (this.target.resolution == Scale.MINUTE && isAvailable) return true;
  if (this.target.resolution == Scale.HOUR && isAvailable) return true;
  return false;
};

DialogScale.prototype.isDayScaleVisible = function() {
  var isAvailable = this.isResolutionAvailable([Resolution.SECONDS, Resolution.MINUTES, Resolution.HOURS, Resolution.DAYS]);
  if (this.target.resolution == Scale.SECOND && isAvailable) return true;
  if (this.target.resolution == Scale.MINUTE && isAvailable) return true;
  if (this.target.resolution == Scale.HOUR && isAvailable) return true;
  if (this.target.resolution == Scale.DAY && isAvailable) return true;
  return false;
};

DialogScale.prototype.isMonthScaleVisible = function() {
  var isAvailable = this.isResolutionAvailable([Resolution.SECONDS, Resolution.MINUTES, Resolution.HOURS, Resolution.DAYS, Resolution.MONTHS]);
  if (this.target.resolution == Scale.SECOND && isAvailable) return true;
  if (this.target.resolution == Scale.MINUTE && isAvailable) return true;
  if (this.target.resolution == Scale.HOUR && isAvailable) return true;
  if (this.target.resolution == Scale.DAY && isAvailable) return true;
  if (this.target.resolution == Scale.MONTH && isAvailable) return true;
  return false;
};

DialogScale.prototype.getAvailableScale = function (scale) {

  while (!this.isResolutionAvailable([scale]) && scale != Scale.YEARS && scale >= 0)
    scale--;

  return scale;
};

DialogScale.prototype.init = function() {
  var jLayer = $(this.DOMLayer);
  
  if (this.state.mode != null)
    jLayer.find(".dialog.scale").addClass(this.state.mode);

  var scale = this.getAvailableScale(this.target.resolution);
  jLayer.find(".scalebar").addClass(Scale.toString(scale));

  this.initBehaviours();
};

DialogScale.prototype.initBehaviours = function() {
  var jLayer = $(this.DOMLayer);
  
  var jScaleSecond = jLayer.find(".scale.second a");
  jScaleSecond.click(DialogScale.prototype.atScaleClick.bind(this, "second"));

  var jScaleMinute = jLayer.find(".scale.minute a");
  jScaleMinute.click(DialogScale.prototype.atScaleClick.bind(this, "minute"));

  var jScaleHour = jLayer.find(".scale.hour a");
  jScaleHour.click(DialogScale.prototype.atScaleClick.bind(this, "hour"));

  var jScaleDay = jLayer.find(".scale.day a");
  jScaleDay.click(DialogScale.prototype.atScaleClick.bind(this, "day"));

  var jScaleMonth = jLayer.find(".scale.month a");
  jScaleMonth.click(DialogScale.prototype.atScaleClick.bind(this, "month"));

  var jScaleYear = jLayer.find(".scale.year a");
  jScaleYear.click(DialogScale.prototype.atScaleClick.bind(this, "year"));
};

DialogScale.prototype.refresh = function(jsonData) {
  var jLayer = $(this.DOMLayer);
  
  jLayer.find(".scale").removeClass("active");

  jLayer.find(".scale.second").attr("style", "display: " + (this.isSecondScaleVisible()?"block":"none"));
  jLayer.find(".scale.minute").attr("style", "display: " + (this.isMinuteScaleVisible()?"block":"none"));
  jLayer.find(".scale.hour").attr("style", "display: " + (this.isHourScaleVisible()?"block":"none"));
  jLayer.find(".scale.day").attr("style", "display: " + (this.isDayScaleVisible()?"block":"none"));
  jLayer.find(".scale.month").attr("style", "display: " + (this.isMonthScaleVisible()?"block":"none"));
  jLayer.find(".scale.year").attr("style", "display: block");

  var jActiveScale = jLayer.find(".scale." + Scale.toString(this.target.scale));
  jActiveScale.addClass("active");
};

DialogScale.prototype.disable = function() {
  var jLayer = $(this.DOMLayer);
  jLayer.find(".scalebar").addClass("disabled");
};

DialogScale.prototype.enable = function() {
  var jLayer = $(this.DOMLayer);
  jLayer.find(".scalebar").removeClass("disabled");
};

//************************************************************************************************************

DialogScale.prototype.atScaleClick = function(scale) {
  var jLayer = $(this.DOMLayer);
  if (jLayer.find(".scalebar").hasClass("disabled")) return;
  if (this.onChange) this.onChange(Scale.fromString(scale));
};