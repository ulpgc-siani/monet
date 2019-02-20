CGEditorText = function () {
  this.base = CGEditor;
  this.base();
  this.extLength = null;
};

CGEditorText.prototype = new CGEditor;

//private
CGEditorText.prototype.addBehaviours = function () {
  this.aDialogs[HISTORY].onSelect = this.atSelect.bind(this);
};

//public
CGEditorText.prototype.init = function (DOMEditor) {
  this.extEditor = Ext.get(DOMEditor);

  var extDialogHistory = this.extEditor.select(CSS_EDITOR_DIALOG_HISTORY).first();

  this.aDialogs[HISTORY] = new CGEditorDialogGrid(extDialogHistory);
  this.setDialogMain(HISTORY);

  this.extLength = this.extEditor.select(CSS_EDITOR_LENGTH).first();
  this.extFilter = this.extEditor.select(CSS_EDITOR_FILTER).first();
  this.extFilter.on("keyup", this.atFilterKeyUp, this);
  this.extFilter.on("focus", this.atFilterFocus, this);
  this.extFilter.on("blur", this.atFilterBlur, this);
  new Insertion.After(this.extFilter.dom, "<span class='empty'>" + Lang.Editor.FilterEmptyHistory + "</span>");
  this.extFilterEmpty = this.extEditor.select('.filter .empty').first();
  Event.observe(this.extFilterEmpty.dom, "click", CGEditorText.prototype.atFilterEmptyClick.bind(this));

  this.extPreview = this.extEditor.select(CSS_EDITOR_PREVIEW).first();
  this.extPreview.dom.style.display = "none";

  CGEditor.prototype.init.call(this, DOMEditor);

  this.extSupIndex = this.extEditor.select(CSS_EDITOR_SUP_INDEX).first();
  if (this.extSupIndex) Event.observe(this.extSupIndex.dom, "click", CGEditorText.prototype.atFormatClick.bind(this, "sup"));

  this.extSubIndex = this.extEditor.select(CSS_EDITOR_SUB_INDEX).first();
  if (this.extSubIndex) Event.observe(this.extSubIndex.dom, "click", CGEditorText.prototype.atFormatClick.bind(this, "sub"));

  this.extBold = this.extEditor.select(CSS_EDITOR_BOLD).first();
  if (this.extBold) Event.observe(this.extBold.dom, "click", CGEditorText.prototype.atFormatClick.bind(this, "bold"));

  this.extItalic = this.extEditor.select(CSS_EDITOR_ITALIC).first();
  if (this.extItalic) Event.observe(this.extItalic.dom, "click", CGEditorText.prototype.atFormatClick.bind(this, "italic"));

  this.addBehaviours();
};

CGEditorText.prototype.clearFilter = function () {
  this.extFilter.dom.value = "";
};

CGEditorText.prototype.filter = function () {
  var DialogMain = this.getDialogMain();
  DialogMain.setData(this.extFilter.dom.value);
  DialogMain.refresh();
};

CGEditorText.prototype.setConfiguration = function (Config) {
  this.Configuration = Config;
  if (Config.Field) {
    Config.Field.onKeyPress = CGEditorText.prototype.atFieldKeyPress.bind(this);
    Config.Field.onFormat = CGEditorText.prototype.atFieldFormat.bind(this);
    this.updatePreview(Config.Field.getValue());
    this.Configuration.Field = null;
  }
  for (var iPos in Config.Dialogs) {
    if (isFunction(Config.Dialogs[iPos])) continue;
    var CurrentConfig = Config.Dialogs[iPos];
    if (this.aDialogs[CurrentConfig.sName]) this.aDialogs[CurrentConfig.sName].setConfiguration(CurrentConfig);
  }
};

CGEditorText.prototype.updatePreview = function (sValue) {
  if (sValue != "") this.extPreview.dom.style.display = "block";
  else this.extPreview.dom.style.display = "none";
  this.extPreview.dom.innerHTML = HtmlUtil.encode(sValue).replace(/\n/g, "<br/>");
};

CGEditorText.prototype.refresh = function () {
  if (this.aDialogs[HISTORY].getConfiguration().Store == null) {
    this.extEditor.addClass(CLASS_NO_HISTORY);
    this.extFilter.dom.style.display = "none";
    this.extFilterEmpty.dom.style.display = "none";
    this.aDialogs[HISTORY].hide();
  }
  else {
    this.extEditor.removeClass(CLASS_NO_HISTORY);
    this.extFilter.dom.style.display = "block";
    this.aDialogs[HISTORY].show();
    this.extFilter.dom.value = "";
    this.extFilterEmpty.dom.style.display = "block";
    this.filter();
  }

  if ((this.Configuration.Length.min != 0) || (this.Configuration.Length.max != 0)) {
    var sMessage = Lang.Editor.Dialogs.Text.Length;
    sMessage = sMessage.replace('#min#', this.Configuration.Length.min);
    sMessage = sMessage.replace('#max#', (this.Configuration.Length.max != 0) ? this.Configuration.Length.max : Lang.Editor.Dialogs.Text.Undefined);
    this.extLength.dom.innerHTML = sMessage;
  }
  else this.extLength.dom.innerHTML = EMPTY;

};

// #############################################################################################################
CGEditorText.prototype.atSelect = function (Data) {
  Data = this.normalizeData(Data);
  this.clearFilter();
  this.filter();
  if (this.onSelect) this.onSelect(Data);
};

CGEditorText.prototype.atFieldKeyPress = function (sValue) {
  window.clearTimeout(this.idTimeoutFilter);
  this.extFilter.dom.value = sValue;
  if (!this.extEditor.hasClass(CLASS_NO_HISTORY)) this.extFilterEmpty.dom.style.display = (this.extFilter.dom.value.length <= 0) ? "block" : "none";
  this.updatePreview(sValue);
  this.idTimeoutFilter = window.setTimeout(this.filter.bind(this), 300);
};

CGEditorText.prototype.atFieldFormat = function (sValue) {
  this.updatePreview(sValue);
};

CGEditorText.prototype.atFilterKeyUp = function (oEvent) {
  var codeKey = oEvent.keyCode;
  var sFilter = this.extFilter.dom.value;

  window.clearTimeout(this.idTimeoutFilter);

  if (codeKey == oEvent.UP) {
    this.moveUp(this.extFilter);
    return;
  }
  else if (codeKey == oEvent.DOWN) {
    this.moveDown(this.extFilter);
    return;
  }
  else if ((codeKey == oEvent.ENTER) || (codeKey == oEvent.LEFT) || (codeKey == oEvent.RIGHT) || (codeKey == oEvent.SHIFT)) return;

  this.idTimeoutFilter = window.setTimeout(this.filter.bind(this), 300);
};

CGEditorText.prototype.atFilterFocus = function () {
  this.extFilterEmpty.dom.style.display = "none";
  this.extFilter.dom.select();
};

CGEditorText.prototype.atFilterBlur = function () {
  if (this.extEditor.hasClass(CLASS_NO_HISTORY)) return;
  var sFilter = this.extFilter.dom.value;
  this.extFilterEmpty.dom.style.display = (sFilter.length <= 0) ? "block" : "none";
};

CGEditorText.prototype.atFilterEmptyClick = function () {
  this.extFilter.focus();
};

CGEditorText.prototype.atFormatClick = function (sFormat, EventLaunched) {
  Event.stop(EventLaunched);
  if (this.onFormat) this.onFormat(sFormat);
  return false;
};