CGEditorNumber = function () {
  this.base = CGEditor;
  this.base();
  this.extFormat = null;
  this.extIncrements = null;
  this.extRange = null;
  this.extUnit = null;
};

CGEditorNumber.prototype = new CGEditor;

//private
CGEditorNumber.prototype.init = function (DOMEditor) {
  this.extEditor = Ext.get(DOMEditor);

  this.extFormat = this.extEditor.select(CSS_EDITOR_NUMBER_FORMAT).first();
  this.extIncrements = this.extEditor.select(CSS_EDITOR_NUMBER_INCREMENTS).first();
  this.extRange = this.extEditor.select(CSS_EDITOR_NUMBER_RANGE).first();
  this.extEquivalences = this.extEditor.select(CSS_EDITOR_NUMBER_EQUIVALENCES).first();

  CGEditor.prototype.init.call(this, DOMEditor);

  this.extIncrement = this.extEditor.select(CSS_EDITOR_INCREMENT).first();
  if (this.extIncrement) Event.observe(this.extIncrement.dom, "click", CGEditorNumber.prototype.atIncrement.bind(this));

  this.extDecrement = this.extEditor.select(CSS_EDITOR_DECREMENT).first();
  if (this.extDecrement) Event.observe(this.extDecrement.dom, "click", CGEditorNumber.prototype.atDecrement.bind(this));
};

CGEditorNumber.prototype.refresh = function () {

  if (this.Configuration.Format.length > 1) {
    this.extFormat.dom.style.display = "block";
    if (this.Configuration.Format[1] != "0") this.extFormat.dom.innerHTML = Lang.Editor.Dialogs.Accepts + this.Configuration.Format[1] + Lang.Editor.Dialogs.Decimals;
    else if (this.Configuration.Format[1] == INFINITE) this.extFormat.dom.innerHTML = Lang.Editor.Dialogs.MultipleDecimals;
    else this.extFormat.dom.style.display = "none";
  }
  else {
    this.extFormat.dom.innerHTML = EMPTY;
  }

  this.extIncrements.dom.innerHTML = (this.Configuration.Increments != null) ? Lang.Editor.Dialogs.Number.Increments.replace('#count#', this.Configuration.Increments) : EMPTY;

  if (this.Configuration.Range.length > 1) {
    var sMessage = Lang.Editor.Dialogs.Number.Range;
    sMessage = sMessage.replace('#min#', this.Configuration.Range[0]);
    sMessage = sMessage.replace('#max#', this.Configuration.Range[1]);
    this.extRange.dom.innerHTML = sMessage;
  }
  else this.extRange.dom.innerHTML = EMPTY;
};

CGEditorNumber.prototype.calculateEquivalences = function (iValue) {
  var sContent = "";

  if ((this.Configuration.Metrics == null) || (this.Configuration.Metrics.length == 0)) {
    this.extEquivalences.dom.innerHTML = sContent;
    return;
  }

  sContent += "<ul>";
  for (var i = 0; i < this.Configuration.Metrics.length; i++) {
    var Metric = this.Configuration.Metrics[i];
    var Equivalence = Metric.Equivalence;
    if (Metric.IsDefault) Equivalence = 1;
    sContent += "<li>" + (iValue * Equivalence) + "&nbsp;" + Metric.Label + "</li>";
  }
  sContent += "</ul>";

  this.extEquivalences.dom.innerHTML = Lang.Editor.Dialogs.Number.Equivalences.replace("#equivalences#", sContent);
};

CGEditorNumber.prototype.setConfiguration = function (Config) {
  this.Configuration = Config;
  if (Config.Field) {
    Config.Field.onKeyPress = CGEditorNumber.prototype.atFieldKeyPress.bind(this);
    this.calculateEquivalences(Config.Field.getNumberFromFormattedValue(Config.Field.getValue()));
    this.Configuration.Field = null;
  }
  for (var iPos in Config.Dialogs) {
    if (isFunction(Config.Dialogs[iPos])) continue;
    var CurrentConfig = Config.Dialogs[iPos];
    if (this.aDialogs[CurrentConfig.sName]) {
      if (CurrentConfig.sName == HISTORY) this.bShowHISTORY = (CurrentConfig.Store != null);
      this.aDialogs[CurrentConfig.sName].setConfiguration(CurrentConfig);
    }
  }
};

// #############################################################################################################
CGEditorNumber.prototype.atIncrement = function (oEvent) {
  Event.stop(oEvent);
  if (this.onIncrement) this.onIncrement();
  return false;
};

CGEditorNumber.prototype.atDecrement = function (oEvent) {
  Event.stop(oEvent);
  if (this.onDecrement) this.onDecrement();
  return false;
};

CGEditorNumber.prototype.atFieldKeyPress = function (iValue) {
  this.calculateEquivalences(iValue);
};