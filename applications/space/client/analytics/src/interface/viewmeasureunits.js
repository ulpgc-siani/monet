ViewMeasureUnits = function () {
  this.base = View;
  this.base();
};

ViewMeasureUnits.prototype = new View;

ViewMeasureUnits.prototype.init = function(DOMLayer) { 

  this.DOMLayer = DOMLayer;
  this.DOMLayer.innerHTML = translate(AppTemplate.viewmeasureunits, Lang.ViewMeasureUnits);
  
  if (!this.target) return;

  this.initTitles();
  CommandListener.capture(this.DOMLayer);
};

ViewMeasureUnits.prototype.toggleView = function() {
  var jLayer = $(this.DOMLayer);
  var jLabel = jLayer.find("label:first");
  var jList = jLayer.find("ul:first");
  var value = jLabel.html();

  if (jList.is(":visible")) {
    jLabel.html(value.replace("-", "+"));
    jList.hide();
  }
  else {
    jLabel.html(value.replace("+", "-"));
    jList.show();
  }
};

ViewMeasureUnits.prototype.refresh = function() {
  var jLayer = $(this.DOMLayer);
  var jList = jLayer.find("ul");
  var measureUnitList = this.target.dashboard.model.measureUnitList;
  var measureUnitTemplate = translate(AppTemplate.viewmeasureunitsitem, Lang.ViewMeasureUnits);
  var rangeList = this.state.rangeList;
  
  if (measureUnitList.length == 0) {
    this.hide();
    return;
  }
  
  this.show();

  jList.html("");
  for (var i=0; i<measureUnitList.length; i++) {
    var measureUnit = measureUnitList[i];
    var range = rangeList.get(measureUnit.name);
    var mode = Range.RELATIVE;
    var minValue = "", maxValue = "";
    
    if (range != null) {
      mode = range.mode;
      minValue = range.min;
      maxValue = range.max;
    }
    
    var jMeasureUnit = $.tmpl(measureUnitTemplate, { id: measureUnit.id, label: measureUnit.label, description: measureUnit.description, minValue: minValue, maxValue: maxValue });
    var DOMMeasureUnit = jMeasureUnit.get(0);
    var jInputMode = jMeasureUnit.find("input.mode");
    var jInputMin = jMeasureUnit.find("input.min");
    var jClearMin = jMeasureUnit.find(".clear.min");
    var jInputMax = jMeasureUnit.find("input.max");
    var jClearMax = jMeasureUnit.find(".clear.max");
    
    DOMMeasureUnit.id = measureUnit.id;
    
    jMeasureUnit.find("input.mode.absolute").attr("checked", (mode == Range.ABSOLUTE));
    jMeasureUnit.find("input.mode.relative").attr("checked", (mode == Range.RELATIVE));
    
    jInputMode.change(ViewMeasureUnits.prototype.atMeasureUnitModeChange.bind(this, DOMMeasureUnit));
    jInputMin.keypress(ViewMeasureUnits.prototype.atMeasureUnitRangeKeyPress.bind(this, DOMMeasureUnit));
    jInputMin.keyup(ViewMeasureUnits.prototype.atMeasureUnitRangeKeyUp.bind(this, DOMMeasureUnit));
    jClearMin.click(ViewMeasureUnits.prototype.atMeasureUnitRangeClearMin.bind(this, DOMMeasureUnit));
    jInputMax.keypress(ViewMeasureUnits.prototype.atMeasureUnitRangeKeyPress.bind(this, DOMMeasureUnit));
    jInputMax.keyup(ViewMeasureUnits.prototype.atMeasureUnitRangeKeyUp.bind(this, DOMMeasureUnit));
    jClearMax.click(ViewMeasureUnits.prototype.atMeasureUnitRangeClearMax.bind(this, DOMMeasureUnit));
    jList.append(jMeasureUnit);
    
    this.refreshMeasureInputs(DOMMeasureUnit, mode);
    this.refreshClearButtons(DOMMeasureUnit);
  }
};

ViewMeasureUnits.prototype.refreshMeasureInputs = function(DOMMeasureUnit, mode) {
  var jMeasureUnit = $(DOMMeasureUnit);
  
  jMeasureUnit.removeClass(Range.ABSOLUTE);
  jMeasureUnit.removeClass(Range.RELATIVE);
  jMeasureUnit.addClass(mode);
  
  if (mode == Range.ABSOLUTE) {
    jMeasureUnit.find("input.min").prop("disabled", true);
    jMeasureUnit.find("input.max").prop("disabled", true);
  }
  else if (mode == Range.RELATIVE) {
    jMeasureUnit.find("input.min").prop("disabled", false);
    jMeasureUnit.find("input.max").prop("disabled", false);
  }
};

ViewMeasureUnits.prototype.refreshClearButtons = function(DOMMeasureUnit) {
  var jMeasureUnit = $(DOMMeasureUnit);
  var minValue = jMeasureUnit.find("input.min").val();
  var jMinClear = jMeasureUnit.find(".clear.min");
  var maxValue = jMeasureUnit.find("input.max").val();
  var jMaxClear = jMeasureUnit.find(".clear.max");
  
  if (minValue != "") jMinClear.show();
  else jMinClear.hide();
  
  if (maxValue != "") jMaxClear.show();
  else jMaxClear.hide();
};

ViewMeasureUnits.prototype.setMode = function(DOMMeasureUnit, mode) {
  var jMeasureUnit = $(DOMMeasureUnit);
  
  Desktop.expandWestLayer($(this.DOMLayer).find("._expansible").get(0));
  this.refreshMeasureInputs(DOMMeasureUnit, mode);
  
  CommandListener.throwCommand("setmeasureunitmode(" + this.target.id + "," + DOMMeasureUnit.id + "," + mode + ")");
};

ViewMeasureUnits.prototype.setRange = function(DOMMeasureUnit) {
  var jMeasureUnit = $(DOMMeasureUnit);
  var minValue = jMeasureUnit.find("input.min").val();
  var maxValue = jMeasureUnit.find("input.max").val();
  Desktop.expandWestLayer($(this.DOMLayer).find("._expansible").get(0));
  CommandListener.throwCommand("setmeasureunitrange(" + this.target.id + "," + DOMMeasureUnit.id + "," + minValue + "," + maxValue + ")");
};

ViewMeasureUnits.prototype.atMeasureUnitModeChange = function(DOMMeasureUnit, event) {
  this.setMode(DOMMeasureUnit, $(event.currentTarget).val());
  return false;
};

ViewMeasureUnits.prototype.atMeasureUnitRangeKeyPress = function(DOMMeasureUnit, event) {
  var jMeasureUnit = $(DOMMeasureUnit);
  var number = String.fromCharCode(event.charCode);
  
  if (number == "." || number == "," || event.charCode == 0 || event.charCode == 13 || event.charCode == 9 || isNumber(number)) {
    
    if (this.rangeTimeout != null)
      window.clearTimeout(this.rangeTimeout);
    
    this.rangeTimeout = window.setTimeout(ViewMeasureUnits.prototype.setRange.bind(this, DOMMeasureUnit), 1000);
    
    return true;
  }
  
  return false;
};

ViewMeasureUnits.prototype.atMeasureUnitRangeKeyUp = function(DOMMeasureUnit, event) {
  this.refreshClearButtons(DOMMeasureUnit);
};

ViewMeasureUnits.prototype.atMeasureUnitRangeClearMin = function(DOMMeasureUnit, event) {
  var jMeasureUnit = $(DOMMeasureUnit);
  jMeasureUnit.find("input.min").val("");
  this.setRange(DOMMeasureUnit);
  this.refreshClearButtons(DOMMeasureUnit);
};

ViewMeasureUnits.prototype.atMeasureUnitRangeClearMax = function(DOMMeasureUnit, event) {
  var jMeasureUnit = $(DOMMeasureUnit);
  jMeasureUnit.find("input.max").val("");
  this.setRange(DOMMeasureUnit);
  this.refreshClearButtons(DOMMeasureUnit);
};