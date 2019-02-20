EDITOR_MAX_PAGE_ITEMS = 20;

CGEditor = function () {
  this.extEditor = null;
  this.Configuration = null;
  this.aDialogs = new Array();
  this.sDialogMain = null;
  this.onShow = null;
  this.onHide = null;
  this.IdField = null;
  this.bShowHelp = false;
};

//private
CGEditor.prototype.init = function (DOMEditor) {
  this.extEditor = Ext.get(DOMEditor);

  this.extLoadDefaultValue = this.extEditor.select(CSS_EDITOR_LOAD_DEFAULT_VALUE).first();
  if (this.extLoadDefaultValue) Event.observe(this.extLoadDefaultValue.dom, "click", CGEditor.prototype.atLoadDefaultValue.bind(this));

  this.extAddDefaultValue = this.extEditor.select(CSS_EDITOR_ADD_DEFAULT_VALUE).first();
  if (this.extAddDefaultValue) Event.observe(this.extAddDefaultValue.dom, "click", CGEditor.prototype.atAddDefaultValue.bind(this));

  this.extClearField = this.extEditor.select(CSS_EDITOR_CLEAR_FIELD).first();
  if (this.extClearField) Event.observe(this.extClearField.dom, "click", CGEditor.prototype.atClearValue.bind(this));

  this.extMessage = this.extEditor.select(CSS_EDITOR_EMESSAGE).first();

  var extHelp = this.extEditor.select(CSS_EDITOR_HELP).first();
  if (extHelp) Event.observe(extHelp.dom, "click", CGEditor.prototype.atHelp.bind(this));
};

//public
CGEditor.prototype.getDOM = function () {
  return this.extEditor.dom;
};

CGEditor.prototype.getFocus = function () {
  this.extFilter.dom.focus();
};

CGEditor.prototype.getDialogMain = function () {
  return this.aDialogs[this.sDialogMain];
};

CGEditor.prototype.setDialogMain = function (sName) {
  this.sDialogMain = sName;
};

CGEditor.prototype.getContextContent = function (Context) {
  var sContent;

  if ((Context == null) || (Context.length <= 0)) return "";

  sContent = "<li><a class='command' href='firstfield()'>" + Context[Context.length - 1].Title + "</a></li>";
  for (var iPos = Context.length - 2; iPos >= 0; iPos--) {
    sContent += "<li style='color:green;'>/</li>";
    sContent += "<li><a class='command' href='gotofield(" + Context[iPos].Path + ")'>" + Context[iPos].Title + "</a></li>";
  }

  return "<ul>" + sContent + "</ul>";
};

CGEditor.prototype.setInfo = function (Info) {
  var extDescription = this.extEditor.select(CSS_EDITOR_INFO_DESCRIPTION).first();
  var extEHelp = this.extEditor.select(CSS_EDITOR_INFO_EHELP).first();

  this.IdField = Info.Id;

  if (extDescription) {
    if (Info.Description != null) extDescription.dom.innerHTML = Info.Description;
    else extDescription.dom.innerHTML = EMPTY;
  }

  if (extEHelp) {
    if (Info.eHelp != null) {
      var sCommand = "loadhelperpage(" + Info.eHelp + ")";
      extEHelp.dom.innerHTML = "<a class='command' href='" + sCommand + "'>" + Lang.Editor.MoreInfo + "</a>";
      CommandListener.capture(extEHelp.dom);
    }
    else extEHelp.dom.innerHTML = EMPTY;
  }

};

CGEditor.prototype.getDialog = function (sName) {
  return this.aDialogs[sName];
};

CGEditor.prototype.show = function () {
  this.extEditor.dom.style.display = "block";
  for (var iPos in this.aDialogs) {
    if (isFunction(this.aDialogs[iPos])) continue;
    this.aDialogs[iPos].show();
  }
  if (this.onShow) this.onShow(this);
};

CGEditor.prototype.hide = function () {
  this.extEditor.dom.style.display = "none";
  for (var iPos in this.aDialogs) {
    if (isFunction(this.aDialogs[iPos])) continue;
    this.aDialogs[iPos].hide();
  }
  if (this.onHide) this.onHide(this);
};

CGEditor.prototype.showValidationError = function (codeError) {
  var extValidationError = this.extEditor.select(CSS_EDITOR_VALIDATION_ERROR + DOT + codeError).first();
  if (extValidationError) {
    extValidationError.dom.style.display = "block";
  }
};

CGEditor.prototype.hideValidationError = function (codeError) {
  var extValidationError = this.extEditor.select(CSS_EDITOR_VALIDATION_ERROR + DOT + codeError).first();
  if (extValidationError) {
    extValidationError.dom.style.display = "none";
  }
};

CGEditor.prototype.setConfiguration = function (Config) {
  this.Configuration = Config;
  if (this.Configuration.Field) this.Configuration.Field = null;
  for (var iPos in Config.Dialogs) {
    if (isFunction(Config.Dialogs[iPos])) continue;
    var CurrentConfig = Config.Dialogs[iPos];
    if (this.aDialogs[CurrentConfig.sName]) this.aDialogs[CurrentConfig.sName].setConfiguration(CurrentConfig);
  }
};

CGEditor.prototype.moveUp = function (Sender) {
  if (this.aDialogs[this.sDialogMain]) this.aDialogs[this.sDialogMain].moveUp(Sender);
};

CGEditor.prototype.moveDown = function (Sender) {
  if (this.aDialogs[this.sDialogMain]) this.aDialogs[this.sDialogMain].moveDown(Sender);
};

CGEditor.prototype.getData = function () {
  if (this.aDialogs[this.sDialogMain]) this.aDialogs[this.sDialogMain].getData();
};

CGEditor.prototype.setData = function (Data) {
  for (var iPos in this.aDialogs) {
    if (isFunction(this.aDialogs[iPos])) continue;
    this.aDialogs[iPos].setData(Data);
  }
};

CGEditor.prototype.setItemsPerPage = function (iItemsPerPage) {
  for (var iPos in this.aDialogs) {
    if (isFunction(this.aDialogs[iPos])) continue;
    this.aDialogs[iPos].setItemsPerPage(iItemsPerPage);
  }
};

CGEditor.prototype.showOtherDialog = function (oEvent) {
  Event.stop(oEvent);
  Ext.Msg.show({
    title: Lang.Editor.Dialogs.Other.Title,
    msg: Lang.Editor.Dialogs.Other.Description,
    width: 300,
    buttons: Ext.MessageBox.OKCANCEL,
    prompt: true,
    fn: CGEditor.prototype.atSelectOther.bind(this),
    value: (this.extFilter) ? this.extFilter.dom.value : ""
  });
  return false;
};

CGEditor.prototype.refresh = function () {
  for (var iPos in this.aDialogs) {
    if (isFunction(this.aDialogs[iPos])) continue;
    this.aDialogs[iPos].refresh();
  }
};

CGEditor.prototype.normalizeDataList = function (dataList) {
  for (var i=0; i<dataList.length; i++)
    dataList[i] = this.normalizeData(dataList[i]);
  return dataList;
};

CGEditor.prototype.normalizeData = function (data) {
  data.code = HtmlUtil.decode(data.code);
  if (data.value.unescapeHTML) data.value = data.value.unescapeHTML();
  data.value = HtmlUtil.decode(data.value);
  data.label = data.value;
  return data;
};

CGEditor.prototype.lock = function () {
  if (this.onLock) this.onLock();
};

CGEditor.prototype.unLock = function () {
  if (this.onUnLock) this.onUnLock();
};

// #############################################################################################################
CGEditor.prototype.atSelect = function (Data) {
  Data = this.normalizeData(Data);
  var DialogSource = this.getDialog(SOURCE);
  if (this.onSelect) this.onSelect(Data, DialogSource != null ? DialogSource.getData() : null);
};

CGEditor.prototype.atSelectMultiple = function (dataList) {
  dataList = this.normalizeDataList(dataList);
  var DialogSource = this.getDialog(SOURCE);
  if (this.onSelectMultiple) this.onSelectMultiple(dataList, DialogSource != null ? DialogSource.getData() : null);
};

CGEditor.prototype.atSelectOther = function (ButtonResult, sValue) {
  if (ButtonResult == BUTTON_RESULT_OK) {
    var Data = { code: "", value: sValue };
    var DialogSource = this.getDialog(SOURCE);
    if (this.onSelectOther) this.onSelectOther(Data, DialogSource != null ? DialogSource.getData() : null);
  }
};

CGEditor.prototype.atFilter = function (sValue) {
  if (this.onFilter) this.onFilter(sValue);
};

CGEditor.prototype.atLoadDefaultValue = function (oEvent) {
  Event.stop(oEvent);
  if (this.onLoadDefaultValue) this.onLoadDefaultValue();
  return false;
};

CGEditor.prototype.atAddDefaultValue = function (oEvent) {
  Event.stop(oEvent);
  if (this.onAddDefaultValue) this.onAddDefaultValue();
  return false;
};

CGEditor.prototype.atClearValue = function (oEvent) {
  Event.stop(oEvent);
  if (this.onClearValue) this.onClearValue(this);
  return false;
};

CGEditor.prototype.atHelp = function (oEvent) {
  Event.stop(oEvent);
  var sStyle = this.extMessage.dom.style.display;
  this.extMessage.dom.style.display = (sStyle == "block") ? "none" : "block";
  bShowHelp = (this.extMessage.dom.style.display == "block");
  return false;
};
