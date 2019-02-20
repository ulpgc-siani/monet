CGEditorSelect = function () {
  this.base = CGEditor;
  this.base();
  this.Configuration = null;
  this.bShowHistory = true;
  this.dispatchingOnSelect = false;
};

CGEditorSelect.prototype = new CGEditor;

//private
CGEditorSelect.prototype.addBehaviours = function () {
  this.aDialogs[HISTORY].onSelect = this.atSelect.bind(this);
  this.aDialogs[SOURCE].onSelect = this.atSelectSource.bind(this);
  this.aDialogs[SOURCE_STORE].onSelect = this.atSelect.bind(this);
};

//public
CGEditorSelect.prototype.init = function (DOMEditor) {
  this.extEditor = Ext.get(DOMEditor);

  var extDialogSource = this.extEditor.select(CSS_EDITOR_DIALOG_SOURCE).first();
  var extDialogSourceStore = this.extEditor.select(CSS_EDITOR_DIALOG_SOURCE_STORE).first();
  var extDialogHistory = this.extEditor.select(CSS_EDITOR_DIALOG_HISTORY).first();

  this.aDialogs[SOURCE] = new CGEditorDialogSource(extDialogSource);
  this.aDialogs[SOURCE_STORE] = new CGEditorDialogGrid(extDialogSourceStore);
  this.aDialogs[HISTORY] = new CGEditorDialogGrid(extDialogHistory);
  this.setDialogMain(SOURCE_STORE);

  this.extToggleHistory = this.extEditor.select(CSS_EDITOR_TOGGLE_HISTORY).first();
  if (this.extToggleHistory) {
    this.extToggleHistory.dom.innerHTML = Lang.Editor.ShowHistory;
    Event.observe(this.extToggleHistory.dom, "click", CGEditorSelect.prototype.atToggleHistoryClick.bind(this));
  }

  this.extSelectOther = this.extEditor.select(CSS_EDITOR_SELECT_OTHER).first();
  if (this.extSelectOther) Event.observe(this.extSelectOther.dom, "click", CGEditorSelect.prototype.showOtherDialog.bind(this));

  this.extFilter = this.extEditor.select(CSS_EDITOR_FILTER).first();
  this.extFilter.on("keyup", this.atFilterKeyUp, this);
  this.extFilter.on("focus", this.atFilterFocus, this);
  this.extFilter.on("blur", this.atFilterBlur, this);
  new Insertion.After(this.extFilter.dom, "<span class='empty'>" + Lang.Editor.FilterEmpty + "</span>");
  this.extFilterEmpty = this.extEditor.select('.filter .empty').first();
  Event.observe(this.extFilterEmpty.dom, "click", CGEditorSelect.prototype.atFilterEmptyClick.bind(this));

  CGEditor.prototype.init.call(this, DOMEditor);

  this.addBehaviours();
};

CGEditorSelect.prototype.showHistory = function () {
  this.aDialogs[HISTORY].show();
  this.aDialogs[SOURCE_STORE].hide();
  this.setDialogMain(HISTORY);
  this.extToggleHistory.dom.innerHTML = Lang.Editor.HideHistory;
};

CGEditorSelect.prototype.hideHistory = function () {

  if (this.getDialogMain() != this.aDialogs[HISTORY])
    return;

  this.aDialogs[HISTORY].hide();
  this.aDialogs[SOURCE_STORE].show();
  this.setDialogMain(SOURCE_STORE);
  this.extToggleHistory.dom.innerHTML = Lang.Editor.ShowHistory;
};

CGEditorSelect.prototype.clearFilter = function () {
  var sFilter = this.extFilter.dom.value;
  sFilter = (sFilter.substring(0, 1) == "+") ? "+" : "";
  this.extFilter.dom.value = sFilter;
};

CGEditorSelect.prototype.filter = function () {
  var sFilter = this.extFilter.dom.value;

  if (sFilter.substring(0, 1) == "+") {
    sFilter = sFilter.substring(1, sFilter.length);
    this.showHistory();
  }
  else {
    this.hideHistory();
  }

  var DialogMain = this.getDialogMain();
  DialogMain.setData(sFilter);

  if (DialogMain.setStoreSourceId)
    DialogMain.setStoreSourceId(this.getSourceId());

  DialogMain.refresh();
};

CGEditorSelect.prototype.show = function () {
  this.extEditor.dom.style.display = "block";
  this.aDialogs[HISTORY].hide();
  this.aDialogs[SOURCE_STORE].show();
  if (this.onShow) this.onShow(this);
};

CGEditorSelect.prototype.refresh = function () {
  if (this.dispatchingOnSelect) return;

  if (this.Configuration.allowOthers) this.extSelectOther.dom.style.display = "inline";
  else this.extSelectOther.dom.style.display = "none";

  if (this.extToggleHistory) this.extToggleHistory.dom.style.display = this.bShowHistory ? "inline" : "none";

  this.extFilter.dom.value = "";
  this.extFilterEmpty.dom.style.display = "block";

  this.aDialogs[SOURCE].refresh();
  this.aDialogs[HISTORY].hide();
  this.aDialogs[SOURCE_STORE].show();

  this.filter();
};

CGEditorSelect.prototype.setConfiguration = function (Config) {
  this.Configuration = Config;
  if (Config.Field) {
    Config.Field.onKeyPress = CGEditorSelect.prototype.atFieldKeyPress.bind(this);
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

CGEditorSelect.prototype.getSourceId = function () {
  var Dialog = this.getDialog(SOURCE);
  return Dialog.getData();
};

CGEditorSelect.prototype.getData = function () {
  var dialog = this.getDialog(SOURCE_STORE);
  if (!dialog.isVisible()) dialog = this.getDialog(HISTORY);
  return dialog.getData();
};

CGEditorSelect.prototype.setDialogStoreSourceId = function (dialogName, SourceId) {
  this.getDialog(dialogName).setStoreSourceId(SourceId);
};

// #############################################################################################################
CGEditorSelect.prototype.atSelectSource = function (SourceId) {
  this.setDialogStoreSourceId(SOURCE_STORE, SourceId);
  this.clearFilter();
  this.filter();
};

CGEditorSelect.prototype.atSelect = function (Data) {

  Data = this.normalizeData(Data);
  var DialogSource = this.getDialog(SOURCE);
  //this.clearFilter();
  //this.filter();

  this.dispatchingOnSelect = true;
  if (this.onSelect) this.onSelect(Data, DialogSource != null ? DialogSource.getData() : null);
  this.dispatchingOnSelect = false;
};

CGEditorSelect.prototype.atFieldKeyPress = function (sValue) {
  window.clearTimeout(this.idTimeoutFilter);
  this.extFilter.dom.value = sValue;
  this.extFilterEmpty.dom.style.display = (this.extFilter.dom.value.length <= 0) ? "block" : "none";
  this.idTimeoutFilter = window.setTimeout(this.filter.bind(this), 200);
};

CGEditorSelect.prototype.atToggleHistoryClick = function (oEvent) {
  var sFilter = this.extFilter.dom.value;

  Event.stop(oEvent);

  if (sFilter.substring(0, 1) == "+") {
    this.extFilter.dom.value = sFilter.substring(1, sFilter.length);
  }
  else this.extFilter.dom.value = "+" + sFilter;

  this.extFilterEmpty.dom.style.display = (this.extFilter.dom.value.length <= 0) ? "block" : "none";

  this.filter();

  return false;
};

CGEditorSelect.prototype.atFilterKeyUp = function (oEvent) {
  var codeKey = oEvent.keyCode;

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

  this.idTimeoutFilter = window.setTimeout(this.filter.bind(this), 200);
};

CGEditorSelect.prototype.atFilterFocus = function () {
  this.extFilterEmpty.dom.style.display = "none";
  this.extFilter.dom.select();
};

CGEditorSelect.prototype.atFilterBlur = function () {
  var sFilter = this.extFilter.dom.value;
  this.extFilterEmpty.dom.style.display = (sFilter.length <= 0) ? "block" : "none";
};

CGEditorSelect.prototype.atFilterEmptyClick = function () {
  this.extFilter.focus();
};