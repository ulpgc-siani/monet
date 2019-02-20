TEXT_EDITION_NORMAL = "normal";
TEXT_EDITION_LOWERCASE = "lowercase";
TEXT_EDITION_UPPERCASE = "uppercase";
TEXT_EDITION_SENTENCE = "sentence";
TEXT_EDITION_TITLE = "title";

CGWidgetText = function (extWidget) {
  this.base = CGWidget;
  this.base(extWidget);
  this.extPattern = null;
  this.aIndicators = new Array();
  this.aPatterns = new Array();
};

CGWidgetText.Formats = new Array();
CGWidgetText.Formats["sup"] = "<sup>#{value}</sup>";
CGWidgetText.Formats["sub"] = "<sub>#{value}</sub>";
CGWidgetText.Formats["bold"] = "<b>#{value}</b>";
CGWidgetText.Formats["italic"] = "<i>#{value}</i>";

CGWidgetText.prototype = new CGWidget;

CGWidgetText.prototype.init = function () {
  this.initStores();
  this.bIsReady = true;
};

CGWidgetText.prototype.getData = function () {
  if (!this.Target) return null;
  if (!this.Target.getCode) return null;
  if (!this.extValue) return null;

  var aIndicators = this.splitIndicators();

  var Result = new Object();
  Result.code = this.Target.getCode();
  Result.order = -1;
  Result.value = new Array();
  Result.value.push({code: CGIndicator.VALUE, order: 1, value: this.extValue.dom.value});
  if (this.Target.isSuper() && (this.extSuper != null)) Result.value.push({code: CGIndicator.SUPER, order: 2, value: this.extSuper.dom.value});

  for (var iPos = 0; iPos < aIndicators.length; iPos++) {
    Result.value.push({code: aIndicators[iPos].Code, order: iPos + 1, value: aIndicators[iPos].Value});
  }

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

CGWidgetText.prototype.splitIndicators = function () {
  var iPos = 0;
  var bMatchPattern = false;
  var sValue = this.extValue.dom.value;
  var aResult = new Array();

  while ((!bMatchPattern) && (iPos < this.aPatterns.length)) {
    var Pattern = this.aPatterns[iPos];
    if (sValue.match(Pattern.Expression)) {
      var aMatching = Pattern.Expression.exec(sValue);
      for (var jPos = 0; jPos < Pattern.aCodes.length; jPos++) {
        var Indicator = new Object();
        Indicator.Code = Pattern.aCodes[jPos];
        Indicator.Value = aMatching[jPos + 1];
        aResult.push(Indicator);
      }
      bMatchPattern = true;
    }
    iPos++;
  }

  return aResult;
};

CGWidgetText.prototype.matchPatterns = function () {
  if (this.aPatterns.length <= 0) return true;

  for (var iPos = 0; iPos < this.aPatterns.length; iPos++) {
    if (this.extValue.dom.value.match(this.aPatterns[iPos].Expression)) return true;
  }

  return false;
};

CGWidgetText.prototype.getValueSelection = function () {
  if (!this.extValue) return "";
  if (document.selection) {
    return document.selection.createRange().text;
  }
  var sContent = this.extValue.dom.value;
  return sContent.substr(this.extValue.dom.selectionStart, this.extValue.dom.selectionEnd - this.extValue.dom.selectionStart);
};

CGWidgetText.prototype.validate = function () {
  var sValue = this.extValue.dom.value;
  var iMinLength = this.Target.getMinLength();

  if (this.WidgetRequired) {
    if (this.extValue.dom.value != "") this.WidgetRequired.hide();
    else this.WidgetRequired.show();
  }

  sValue = this.Target.format(sValue);
  if (this.extMessageEmpty) this.extMessageEmpty.dom.style.display = (sValue == "") ? "block" : "none";

  if ((iMinLength != 0) && (sValue.length < iMinLength)) {
    this.Editor.showValidationError(VALIDATION_ERROR_LENGTH);
    this.extValue.addClass(CLASS_WRONG);
  }
  else {
    this.Editor.hideValidationError(VALIDATION_ERROR_LENGTH);
    this.extValue.removeClass(CLASS_WRONG);
  }

  if (this.matchPatterns()) {
    this.extValue.removeClass(CLASS_WRONG);
    this.Editor.hideValidationError(VALIDATION_ERROR_FORMAT);
  }
  else {
    if (sValue != "") {
      this.extValue.addClass(CLASS_WRONG);
      this.Editor.showValidationError(VALIDATION_ERROR_FORMAT);
    }
  }

  sValue = sValue.replace(/<br\/>/g, "\n");
  if (this.extValue.dom.value != sValue)
    this.extValue.dom.value = sValue;
};

CGWidgetText.prototype.setTarget = function (Target) {
  this.Target = Target;
  this.createRequiredWidget();
  this.aPatterns = this.Target.getPatterns();
  this.setMessageWhenEmpty(this.Target.getMessageWhenEmpty());
  this.createOptions();
  this.validate();
  //this.updateData();
};

// #############################################################################################################

CGWidgetText.prototype.atFocused = function (oEvent) {
  if (!this.Target) return;
  if (!this.isReady()) this.init();
  if (this.onFocused) this.onFocused();

  this.Editor.show();
  this.Editor.setConfiguration({Field: this.Target, Dialogs: [
    {sName: HISTORY, Store: this.aStores[HISTORY]}
  ], Length: this.Target.getLength()});
  this.Editor.onSelect = this.atSelect.bind(this);
  this.Editor.onFormat = this.atFormat.bind(this);
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

CGWidgetText.prototype.atSelect = function (Data) {
  this.extValue.dom.value = (Data.value) ? Data.value : Data.code;
  this.validate();
  this.updateData();
  if (this.onSelect) this.onSelect(Data);
};

CGWidgetText.prototype.atFormat = function (CodeFormat) {
  var ValueTemplate, sValue, sSelection, sResult, iStart, iEnd;

  if (!CGWidgetText.Formats[CodeFormat]) return;

  iStart = this.extValue.dom.selectionStart;
  iEnd = this.extValue.dom.selectionEnd;

  if (iStart == iEnd) return;

  sSelection = this.getValueSelection();
  sValue = this.extValue.dom.value;
  ValueTemplate = new Template(CGWidgetText.Formats[CodeFormat]);

  sResult = sValue.substr(0, iStart);
  sResult += ValueTemplate.evaluate({'value': sSelection});
  sResult += sValue.substr(iEnd);

  this.extValue.dom.value = sResult;
  this.validate();
  this.updateData();

  if (this.onFormat) this.onFormat(this.extValue.dom.value);
};

CGWidgetText.prototype.atChange = function (oEvent) {
  if (this.extValue.dom.value != "") this.showClearValue();
  else this.hideClearValue();
  this.validate();
  this.updateData();
};

CGWidgetText.prototype.atBlur = function (oEvent) {
  if (this.extMessageEmpty) this.extMessageEmpty.dom.style.display = (this.extValue.dom.value == "") ? "block" : "none";

  if (!Ext.isIE) return;

  if (this.extValue.dom.value != "") this.showClearValue();
  else this.hideClearValue();

  this.validate();
  this.updateData();
};

CGWidgetText.prototype.fixAtChange = function () {
  this.atChange();
};

CGWidgetText.prototype.atKeyUp = function (oEvent) {
  var codeKey = oEvent.getKey();

  // Fix bug on FF 15
  if (this.geckoFix)
    window.clearTimeout(this.geckoFix);
  // End fix

  this.validate();
  this.Editor.refresh(this.extValue.dom.value);

  if ((codeKey == oEvent.ESC) && (this.onEscape)) this.onEscape();
  else if ((codeKey == oEvent.ENTER) && (!this.extWidget.hasClass(CLASS_WIDGET_LONG))) {
    var Dialog = this.Editor.getDialog(HISTORY);
    if (Dialog) {
      var Data = Dialog.getData();
      if (Data) this.extValue.dom.value = Data.value;
    }
    if (this.onEnter) this.onEnter();
  }
  else if (codeKey == oEvent.DOWN) this.Editor.moveDown(this.extValue.dom);
  else if (codeKey == oEvent.UP) this.Editor.moveUp(this.extValue.dom);
  else if (codeKey == oEvent.TAB || codeKey == oEvent.LEFT || codeKey == oEvent.RIGHT) {
  }
  else if (this.onKeyPress) this.onKeyPress(this.extValue.dom.value, codeKey);

  if (this.extMessageEmpty) this.extMessageEmpty.dom.style.display = (this.extValue.dom.value == "") ? "block" : "none";

  if (this.extValue.dom.value != "") this.showClearValue();
  else this.hideClearValue();

  // Fix bug on FF 15
  if (Ext.isGecko)
    this.geckoFix = window.setTimeout(CGWidgetText.prototype.fixAtChange.bind(this), 500);
  // End fix

  Event.stop(oEvent);
  return false;
};