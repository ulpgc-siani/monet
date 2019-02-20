CGEditorCheck = function () {
  this.base = CGEditor;
  this.base();
};

CGEditorCheck.prototype = new CGEditor;

//private
CGEditorCheck.prototype.addBehaviours = function () {
  this.aDialogs[HISTORY].onSelect = this.atSelect.bind(this);
  this.aDialogs[SOURCE].onSelect = this.atSelectSource.bind(this);
};

//public
CGEditorCheck.prototype.init = function (DOMEditor) {
  this.extEditor = Ext.get(DOMEditor);

  var extDialogSource = this.extEditor.select(CSS_EDITOR_DIALOG_SOURCE).first();
  var extDialogHistory = this.extEditor.select(CSS_EDITOR_DIALOG_HISTORY).first();

  this.aDialogs[SOURCE] = new CGEditorDialogSource(extDialogSource);
  this.aDialogs[HISTORY] = new CGEditorDialogGrid(extDialogHistory);
  this.setDialogMain(HISTORY);

  this.extFilter = this.extEditor.select(CSS_EDITOR_FILTER).first();
  this.extFilter.on("keyup", this.atFilterKeyUp, this);
  this.extFilter.on("focus", this.atFilterFocus, this);
  this.extFilter.on("blur", this.atFilterBlur, this);
  new Insertion.After(this.extFilter.dom, "<span class='empty'>" + Lang.Editor.FilterEmptyHistory + "</span>");
  this.extFilterEmpty = this.extEditor.select('.filter .empty').first();
  Event.observe(this.extFilterEmpty.dom, "click", CGEditorCheck.prototype.atFilterEmptyClick.bind(this));

  CGEditor.prototype.init.call(this, DOMEditor);

  this.extToggleCheck = this.extEditor.select(CSS_EDITOR_TOGGLE_CHECK).first();
  if (this.extToggleCheck) Event.observe(this.extToggleCheck.dom, "click", CGEditorCheck.prototype.atToggleCheck.bind(this));

  this.addBehaviours();
};

CGEditorCheck.prototype.clearFilter = function () {
  this.extFilter.dom.value = "";
};

CGEditorCheck.prototype.filter = function () {
  var DialogMain = this.getDialogMain();
  DialogMain.setData(this.extFilter.dom.value);
  DialogMain.setStoreSourceId(this.getSourceId());
  DialogMain.refresh();
};

CGEditorCheck.prototype.setConfiguration = function (Config) {
  this.Configuration = Config;
  if (Config.Field) {
    Config.Field.onKeyPress = CGEditorCheck.prototype.atFieldKeyPress.bind(this);
    this.Configuration.Field = null;
  }
  for (var iPos in Config.Dialogs) {
    if (isFunction(Config.Dialogs[iPos])) continue;
    var CurrentConfig = Config.Dialogs[iPos];
    if (this.aDialogs[CurrentConfig.sName]) this.aDialogs[CurrentConfig.sName].setConfiguration(CurrentConfig);
  }
};

CGEditorCheck.prototype.refresh = function () {
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
  if (this.extToggleCheck) this.extToggleCheck.dom.innerHTML = (this.Configuration.Checked) ? Lang.Editor.Uncheck : Lang.Editor.Check;
  this.aDialogs[SOURCE].refresh();
};

CGEditorCheck.prototype.getSourceId = function () {
  var Dialog = this.getDialog(SOURCE);
  return Dialog.getData();
};

// #############################################################################################################
CGEditorCheck.prototype.atSelectSource = function (SourceId) {
  this.clearFilter();
  this.filter();
  if (this.onSelectSource) this.onSelectSource(SourceId);
};

CGEditorCheck.prototype.atSelect = function (Data) {
  Data = this.normalizeData(Data);
  var DialogSource = this.getDialog(SOURCE);
  this.clearFilter();
  this.filter();
  if (this.onSelect) this.onSelect(Data, DialogSource != null ? DialogSource.getData() : null);
};

CGEditorCheck.prototype.atFieldKeyPress = function (sValue) {
  window.clearTimeout(this.idTimeoutFilter);
  this.extFilter.dom.value = sValue;
  if (!this.extEditor.hasClass(CLASS_NO_HISTORY)) this.extFilterEmpty.dom.style.display = (this.extFilter.dom.value.length <= 0) ? "block" : "none";
  this.idTimeoutFilter = window.setTimeout(this.filter.bind(this), 300);
};

CGEditorCheck.prototype.atFilterKeyUp = function (oEvent) {
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

  this.idTimeoutFilter = window.setTimeout(this.filter.bind(this), 300);
};

CGEditorCheck.prototype.atFilterFocus = function () {
  this.extFilterEmpty.dom.style.display = "none";
  this.extFilter.dom.select();
};

CGEditorCheck.prototype.atFilterBlur = function () {
  if (this.extEditor.hasClass(CLASS_NO_HISTORY)) return;
  var sFilter = this.extFilter.dom.value;
  this.extFilterEmpty.dom.style.display = (sFilter.length <= 0) ? "block" : "none";
};

CGEditorCheck.prototype.atFilterEmptyClick = function () {
  this.extFilter.focus();
};

CGEditorCheck.prototype.atToggleCheck = function (oEvent) {
  Event.stop(oEvent);
  if (this.onToggleCheck) this.onToggleCheck();
  return false;
};