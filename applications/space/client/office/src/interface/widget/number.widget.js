CGWidgetNumber = function (extWidget) {
  this.base = CGWidget;
  this.base(extWidget);
  this.aFormat = null;
  this.iIncrements = 1;
  this.aRange = new Array();
  this.Metrics = null;
  this.extMetrics = this.extWidget.down(CSS_WIDGET_ELEMENT_METRICS);
  if (this.extMetrics) this.extMetrics.on("change", this.atMetricChange, this);
  if (!extWidget) return;
};

CGWidgetNumber.prototype = new CGWidget;

CGWidgetNumber.prototype.init = function () {
  this.bIsReady = true;
};

CGWidgetNumber.prototype.validate = function () {
  var sValue = this.extValue.dom.value.replace(/\./g, EMPTY);
  var iValue = parseFloat(sValue.replace(COMMA, DOT));
  var bValidRange;

  if (this.WidgetRequired) {
    if (this.extValue.dom.value != "0") this.WidgetRequired.hide();
    else this.WidgetRequired.show();
  }

  sValue = this.Target.format(this.extValue.dom.value);
  if (sValue == false || isNaN(iValue)) sValue = 0;
  bValidRange = this.Target.isValidRange(iValue);

  if ((bValidRange) || (sValue == 0)) this.extValue.removeClass(CLASS_WRONG);
  else this.extValue.addClass(CLASS_WRONG);

  this.extValue.dom.value = sValue;
  this.Editor.hideValidationError(VALIDATION_ERROR_FORMAT);

  if (bValidRange) this.Editor.hideValidationError(VALIDATION_ERROR_RANGE);
  else this.Editor.showValidationError(VALIDATION_ERROR_RANGE);

  if (this.extMessageEmpty) this.extMessageEmpty.dom.style.display = (this.extValue.dom.value == "0") ? "block" : "none";
};

CGWidgetNumber.prototype.setTarget = function (Target) {
  this.Target = Target;

  this.createRequiredWidget();
  this.Metrics = this.Target.getMetrics();
  this.aFormat = this.Target.getFormat();
  this.aRange = this.Target.getRange();
  this.iIncrements = this.Target.getIncrements();

  if (this.extMetrics) this.extMetrics.dom.style.display = (this.Metrics.length > 0) ? "block" : "none";

  this.createOptions();
  this.setMessageWhenEmpty(this.Target.getMessageWhenEmpty());

  this.validate();
  //this.updateData();
};

CGWidgetNumber.prototype.getData = function () {
  if (!this.Target) return null;
  if (!this.Target.getCode) return null;
  if (!this.extValue) return null;

  var Result = new Object();
  Result.code = this.Target.getCode();
  Result.order = -1;
  Result.value = new Array();
  Result.value.push({code: CGIndicator.VALUE, order: 1, value: this.extValue.dom.value});
  Result.value.push({code: CGIndicator.INTERNAL, order: 2, value: this.Target.getNumberFromFormattedValue(this.extValue.dom.value)});
  if (this.extMetrics && this.extMetrics.dom.selectedIndex != -1) Result.value.push({code: CGIndicator.METRIC, order: 3, value: this.extMetrics.dom.options[this.extMetrics.dom.selectedIndex].value});
  if (this.Target.isSuper() && (this.extSuper != null)) Result.value.push({code: CGIndicator.SUPER, order: 4, value: this.extSuper.dom.value});

  Result.toXml = function () {
    var Attribute = new CGAttribute();
    Attribute.code = this.code;
    Attribute.iOrder = this.order;
    for (var iPos = 0; iPos < this.value.length; iPos++) {
      Attribute.addIndicatorByValue(this.value[iPos].code, this.value[iPos].order, this.value[iPos].value);
    }
    return Attribute.serialize();
  };

  return Result;
};

CGWidgetNumber.prototype.add = function () {
  var iValue = this.extValue.dom.value.replace(/\./g, EMPTY);
  if (iValue == "") iValue = "0";
  iValue = parseFloat(iValue.replace(COMMA, DOT));
  iValue = this.Target.roundDecimals(iValue + this.iIncrements);
  this.extValue.dom.value = iValue.toString().replace(DOT, COMMA);
  this.validate();
  this.updateData();
  if (this.extValue.dom.value != "") this.showClearValue();
  else this.hideClearValue();
};

CGWidgetNumber.prototype.subtract = function () {
  var iValue = this.extValue.dom.value.replace(/\./g, EMPTY);
  if (iValue == "") iValue = "0";
  iValue = parseFloat(iValue.replace(COMMA, DOT));
  iValue = this.Target.roundDecimals(iValue - this.iIncrements);
  this.extValue.dom.value = iValue.toString().replace(DOT, COMMA);
  this.validate();
  this.updateData();
  if (this.extValue.dom.value != "") this.showClearValue();
  else this.hideClearValue();
};

CGWidgetNumber.prototype.clearValue = function (oEvent) {
  this.focus();
  this.extValue.dom.value = "0";
  this.validate();
  this.updateData();
  this.hideClearValue();
  if (this.onClearValue) this.onClearValue(this);
};

// #############################################################################################################

CGWidgetNumber.prototype.atFocused = function (oEvent) {
  if (!this.Target) return;
  if (!this.isReady()) this.init();
  if (this.onFocused) this.onFocused();

  this.Editor.show();
  this.Editor.setConfiguration({Field: this.Target, Format: this.aFormat, Increments: this.iIncrements, Range: this.aRange, Metrics: this.Metrics});
  this.Editor.onSelect = this.atSelect.bind(this);
  this.Editor.onIncrement = this.atIncrement.bind(this);
  this.Editor.onDecrement = this.atDecrement.bind(this);
  this.Editor.onLoadDefaultValue = this.atLoadDefaultValue.bind(this);
  this.Editor.onAddDefaultValue = this.atAddDefaultValue.bind(this);
  this.Editor.onClearValue = this.atClearValue.bind(this);
  this.Editor.refresh();

  if (this.isLocked()) this.Editor.lock();
  else this.Editor.unLock();

  this.validate();
  this.extValue.addClass(CLASS_FOCUS);
  this.extValue.dom.select();

  if (this.Target.getInfo) this.Editor.setInfo(this.Target.getInfo());
  if (this.extMessageEmpty) this.extMessageEmpty.dom.style.display = "none";
};

CGWidgetNumber.prototype.atSelect = function (Data) {
  this.extValue.dom.value = (Data.value) ? Data.value : Data.code;
  this.validate();
  this.updateData();
  if (this.onSelect) this.onSelect(Data);
};

CGWidgetNumber.prototype.atIncrement = function (Data) {
  this.add();
};

CGWidgetNumber.prototype.atDecrement = function (Data) {
  this.subtract();
};

CGWidgetNumber.prototype.atChange = function (oEvent) {
  var codeKey = oEvent.getKey();

  this.validate();
  this.updateData();

  if ((codeKey == oEvent.ESC) && (this.onEscape)) this.onEscape();
  if ((codeKey == oEvent.ENTER) && (this.onEnter)) this.onEnter();
};

CGWidgetNumber.prototype.atKeyUp = function (oEvent) {
  var codeKey = oEvent.getKey(), dummy;
  var sValue = this.extValue.dom.value;

  if ((codeKey == oEvent.ESC) && (this.onEscape)) this.onEscape();
  else if ((codeKey == oEvent.ENTER) && (this.onEnter)) this.onEnter();
  else if (codeKey == oEvent.TAB) dummy = 1;
  else if (this.onKeyPress) this.onKeyPress(this.Target.getNumberFromFormattedValue(sValue), codeKey);

  if (this.extMessageEmpty) this.extMessageEmpty.dom.style.display = (this.extValue.dom.value == "0") ? "block" : "none";

  if (this.extValue.dom.value != "0") this.showClearValue();
  else this.hideClearValue();

  Event.stop(oEvent);
  return false;
};

CGWidgetNumber.prototype.atKeyDown = function (oEvent) {
  var codeKey = oEvent.getKey();

  if (codeKey == oEvent.DOWN) this.subtract();
  else if (codeKey == oEvent.UP) this.add();
  else if ((codeKey == oEvent.TAB || codeKey == oEvent.ENTER) && this.onKeyPress) {
    var sValue = this.extValue.dom.value;
    this.onKeyPress(this.Target.getNumberFromFormattedValue(sValue), codeKey);
  }
};

CGWidgetNumber.prototype.atClearValue = function (oEvent) {
  this.clearValue();
  return true;
};

CGWidgetNumber.prototype.atMetricChange = function (oEvent) {
  this.focus();
  this.validate();
  this.updateData();
};