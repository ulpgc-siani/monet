TAG_URL_ID = "::id::";
TAG_URL_IDNODE = "::idnode::";

CGWidgetFile = function (extWidget) {
  this.base = CGWidget;
  this.base(extWidget);

  if (!extWidget) return;

  this.sServiceURL = HtmlUtil.decode(Context.Config.ApplicationFmsFilesUploadUrl);
  this.idFile = "";
  this.label = "";
  this.extOnlineMenu = null;
  this.delayBlur = true;
  this.cancelBlur = false;
};

CGWidgetFile.prototype = new CGWidget;

CGWidgetFile.prototype.init = function () {
  this.bIsReady = true;
};

CGWidgetFile.prototype.applyBehaviours = function () {
  if (!this.extWidget) return;

  this.extSuper = this.extWidget.down(CSS_WIDGET_ELEMENT_SUPER);
  this.extValue = this.extWidget.down(CSS_WIDGET_ELEMENT_COMPONENT);
  this.extRealValue = this.extWidget.down(CSS_WIDGET_ELEMENT_COMPONENT + "_hidden");

  if (this.extValue == null) return;
  if (this.atFocused) this.extValue.on("focus", this.atFocused, this);
  if (this.atBlur) this.extValue.on("blur", this.atBlur, this);
  if (this.atChange) this.extValue.on('change', this.atChange, this);
  if (this.atKeyDown) this.extValue.on("keydown", this.atKeyDown, this);
  if (this.atKeyPress) this.extValue.on("keypress", this.atKeyPress, this);
  if (this.atKeyUp) this.extValue.on("keyup", this.atKeyUp, this);

  this.setId(this.extValue.dom.id);
};

CGWidgetFile.prototype.createOptions = function () {
  new Insertion.Bottom(this.extWidget.dom, WidgetOptionsTemplate.evaluate({'ImagesPath': Context.Config.ImagesPath}));
  var extOptions = this.extWidget.select(CSS_WIDGET_ELEMENT_OPTIONS).first();
  this.extOptionClearValue = extOptions.select(CSS_WIDGET_ELEMENT_CLEAR_VALUE).first();
  this.extOptionClearValue.on("click", this.atClearValue, this);
  this.showClearValue();
};

CGWidgetFile.prototype.createOnlineMenu = function (Target) {
  new Insertion.After(this.extValue.dom, WidgetFileOnlineMenuTemplate.evaluate({'ImagesPath': Context.Config.ImagesPath}));
  this.extOnlineMenu = this.extWidget.select(CSS_WIDGET_ELEMENT_ONLINE_MENU).first();

  var extOptionDownload = this.extOnlineMenu.down(CSS_FILE_DOWNLOAD);
  extOptionDownload.on("click", this.atDownload, this);

  var extOptionClearValue = this.extOnlineMenu.down(CSS_WIDGET_ELEMENT_CLEAR_VALUE);
  extOptionClearValue.on("click", this.atClearValue, this);

  this.extOnlineMenu.hide();
  this.extOnlineMenu.on("click", this.atOnlineMenuClick, this);
};

CGWidgetFile.prototype.destroyOptions = function (Target) {
  this.extOptionClearValue.un("click", this.atClearValue, this);
  this.extOnlineMenu.un("click", this.atFocused, this);
  this.extOnlineMenu = null;
};

CGWidgetFile.prototype.setTarget = function (Target) {
  this.Target = Target;
  this.createRequiredWidget();
  this.setFile(this.extRealValue.dom.value, this.extValue.dom.value);
  this.createOnlineMenu();
  this.createOptions();
  this.setMessageWhenEmpty(this.Target.getMessageWhenEmpty());
  this.validate();
  //this.updateData();
};

CGWidgetFile.prototype.getData = function () {
  if (!this.Target) return null;
  if (!this.Target.getCode) return null;
  if (!this.extValue) return null;

  var Result = new Object();
  Result.code = this.Target.getCode();
  Result.order = -1;
  Result.value = new Array();
  Result.value.push({code: CGIndicator.VALUE, order: 1, value: this.idFile});
  Result.value.push({code: CGIndicator.FORMAT, order: 2, value: getFileExtension(this.idFile).toUpperCase()});
  Result.value.push({code: CGIndicator.DETAILS, order: 3, value: this.label});
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

CGWidgetFile.prototype.setData = function (sData) {
  if (!this.Target) return;
  if (!this.extValue) return;

  var Attribute = new CGAttribute();
  Attribute.unserialize(sData);

  var value = "";
  var IndicatorValue = Attribute.getIndicator(CGIndicator.VALUE);
  if (IndicatorValue) value = IndicatorValue.getValue();

  var details = "";
  var IndicatorDetails = Attribute.getIndicator(CGIndicator.DETAILS);
  if (IndicatorDetails) details = IndicatorDetails.getValue();

  this.setFile(value, details);

  if (this.extOptionClearValue) {
    if (this.idFile != "") this.showClearValue();
    else this.hideClearValue();
  }

  if (this.idFile != "") this.showClearValue();
  else this.hideClearValue();

  this.validate();

  if (this.onChange) this.onChange();
};

CGWidgetFile.prototype.clear = function () {
  this.setFile("", "");
  if (this.onClearValue)
    this.onClearValue(this);
  else {
    this.validate();
    this.updateData();
  }
};

CGWidgetFile.prototype.setFile = function (idFile, label) {
  if (idFile != null) this.idFile = idFile;
  this.label = label;
  if (this.idFile != "") {
    this.extValue.dom.value = this.label != "" ? this.label : HtmlUtil.decode(getFileName(this.idFile));
    this.extRealValue.dom.value = HtmlUtil.decode(this.idFile);
    this.showClearValue();
  }
  else {
    this.extValue.dom.value = "";
    this.extRealValue.dom.value = "";
    this.hideClearValue();
  }
};

CGWidgetFile.prototype.clearValue = function (oEvent) {
  if (this.isLocked()) return;
  this.focus();
  this.clear();
  this.extOnlineMenu.hide();
};

// #############################################################################################################
CGWidgetFile.prototype.atFocused = function (oEvent) {
  if (!this.Target) return false;
  if (!this.isReady()) this.init();
  if (this.onFocused) this.onFocused();

  var sURL = this.sServiceURL.replace(TAG_URL_IDNODE, this.Target.getNodeId());

  this.Editor.show();
  this.Editor.setConfiguration({Field: this.Target, Dialogs: [
    {sName: FILE_UPLOAD, Action: sURL, Label: this.label, MultipleSelection: this.Target.isMultiple(), Limit: this.Target.getLimit()}
  ]});
  this.Editor.onSelect = this.atSelect.bind(this);
  this.Editor.onLoadDefaultValue = this.atLoadDefaultValue.bind(this);
  this.Editor.onAddDefaultValue = this.atAddDefaultValue.bind(this);
  this.Editor.onClearValue = this.atClearValue.bind(this);
  this.Editor.onDownload = this.atEditorDownload.bind(this);
  this.Editor.onLabelChange = this.atEditorLabelChange.bind(this);
  this.Editor.refresh();

  if (this.isLocked()) this.Editor.lock();
  else this.Editor.unLock();

  this.extValue.addClass(CLASS_FOCUS);
  this.extValue.dom.select();

  if (this.Target.getInfo) this.Editor.setInfo(this.Target.getInfo());
  if (this.extMessageEmpty) this.extMessageEmpty.dom.style.display = "none";

  if (this.idFile != "") this.extOnlineMenu.show();

  return false;
};

CGWidgetFile.prototype.atBlur = function (oEvent) {
  if (this.delayBlur) {
    this.delayBlur = false;
    window.setTimeout(this.atBlur.bind(this), 150);
    return;
  }
  if (this.cancelBlur) {
    this.cancelBlur = false;
    return;
  }
  if ((this.extValue) && (this.extMessageEmpty)) this.extMessageEmpty.dom.style.display = (this.extValue.dom.value == "") ? "block" : "none";
  this.extOnlineMenu.hide();
  this.delayBlur = true;
};

CGWidgetFile.prototype.atSelect = function (Data) {
  if (this.isLocked()) return;
  this.setFile(Data.code, Data.value);
  this.validate();
  this.updateData();
  if (this.onSelect) this.onSelect(Data);
};

CGWidgetFile.prototype.atDownload = function (EventLaunched, DOMDownload) {
  var sSource = HtmlUtil.decode(Context.Config.ApplicationFmsFileDownloadUrl).replace(TAG_URL_ID, this.extRealValue.dom.value);
  sSource = sSource.replace(TAG_URL_IDNODE, this.Target.getNodeId());
  DOMDownload.href = sSource;

  this.extOnlineMenu.hide();
};

CGWidgetFile.prototype.atKeyDown = function (oEvent) {
  var codeKey = oEvent.getKey();

  if ((codeKey == oEvent.ESC) && (this.onEscape)) this.onEscape();
  else if (((codeKey == oEvent.TAB) || (codeKey == oEvent.ENTER)) && (this.onEnter)) this.onEnter();
  else if ((codeKey == oEvent.DELETE) || (codeKey == oEvent.BACKSPACE)) this.clear();

  if (this.extMessageEmpty) this.extMessageEmpty.dom.style.display = (this.extValue.dom.value == "") ? "block" : "none";

  if (this.idFile != "") this.showClearValue();
  else this.hideClearValue();

  Event.stop(oEvent);
  return false;
};

CGWidgetFile.prototype.atClearValue = function (oEvent) {
  this.clearValue();
  return false;
};

CGWidgetFile.prototype.atEditorDownload = function (DOMDownload) {
  DOMDownload.stop = (this.idFile == "");
  DOMDownload.href = this.extRealValue.dom.value;
};

CGWidgetFile.prototype.atEditorLabelChange = function (label) {
  this.setFile(null, label);
  this.validate();
  this.updateData();
  if (this.onSelect) this.onSelect(Data);
};

CGWidgetFile.prototype.atOnlineMenuClick = function () {
  this.cancelBlur = true;
};