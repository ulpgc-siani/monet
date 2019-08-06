CGWidgetLink = function (extWidget) {
  this.base = CGWidget;
  this.base(extWidget);
  this.Item = new Object();
  this.Item.code = EMPTY;
  this.Item.value = EMPTY;

  if (!extWidget) return;

  this.extWidget.dom.blur = CGWidgetLink.prototype.blur.bind(this);
  this.extNodeBox = this.extWidget.down(CSS_WIDGET_ELEMENT_NODE_BOX);
  this.extOnlineMenu = null;
  this.extNodeContainer = null;
  this.bSelectingNode = false;
  this.delayBlur = true;
  this.cancelBlur = false;
};

CGWidgetLink.prototype = new CGWidget;

CGWidgetLink.prototype.createOptions = function () {
  new Insertion.Bottom(this.extWidget.dom, WidgetOptionsTemplate.evaluate({'ImagesPath': Context.Config.ImagesPath}));
  var extOptions = this.extWidget.select(CSS_WIDGET_ELEMENT_OPTIONS).first();

  this.extOptionClearValue = extOptions.select(CSS_WIDGET_ELEMENT_CLEAR_VALUE).first();
  this.extOptionClearValue.on("click", this.atClearValue, this);

  if (this.extValue.dom.value != "") this.showClearValue();
  else this.hideClearValue();

  new Insertion.Bottom(this.extNodeBox.dom, WidgetLinkNodeBoxTemplate.evaluate({'ImagesPath': Context.Config.ImagesPath}));

  this.extNodeContainer = this.extNodeBox.down(CSS_WIDGET_ELEMENT_NODE_CONTAINER);
  this.extNodeContainer.dom.innerHTML = WidgetLoadingTemplate.evaluate({'ImagesPath': Context.Config.ImagesPath});
};

CGWidgetLink.prototype.createOnlineMenu = function () {
  new Insertion.After(this.extValue.dom, WidgetLinkOnlineMenuTemplate.evaluate({'ImagesPath': Context.Config.ImagesPath}));
  this.extOnlineMenu = this.extWidget.select(CSS_WIDGET_ELEMENT_ONLINE_MENU).first();

  this.extOptionHiperLink = this.extOnlineMenu.down(CSS_WIDGET_ELEMENT_HIPERLINK);
  this.extOptionHiperLink.on("click", this.atHiperLinkClick, this);

  var extOptionClearValue = this.extOnlineMenu.down(CSS_WIDGET_ELEMENT_CLEAR_VALUE);
  extOptionClearValue.on("click", this.atClearValue, this);

  this.extOnlineMenu.hide();
  this.extOnlineMenu.on("click", this.atOnlineMenuClick, this);
};

CGWidgetLink.prototype.destroyOptions = function () {
  this.extOptionClearValue.un("click", this.atClearValue, this);
  this.extOptionHiperLink.un("click", this.atHiperLinkClick, this);
  this.extOptionHiperLink = null;
  this.extOnlineMenu.un("click", this.atFocused, this);
  this.extOnlineMenu = null;
};

CGWidgetLink.prototype.init = function (code, value) {
  this.aCodeNodeTypes = this.Target.getNodeTypes();
  this.initStores();
  this.initColumnModel();
  if (!this.isCustomColumnModel && this.aStores[DATA_LINK]) this.aStores[DATA_LINK].ShowCode = true;
  this.bIsReady = true;
};

CGWidgetLink.prototype.initStores = function () {
  CGWidget.prototype.initStores.call(this);

  if (!this.Target) return;

  var DataLinkLocations = this.Target.getDataLinkLocations();
  if (DataLinkLocations != null)
    this.initDataLinkLocations(DataLinkLocations);
};

CGWidgetLink.prototype.initDataLinkLocations = function (DataLinkLocations) {
  var locationsCountSourceUrl = WidgetTemplateDataLinkLocationsCountUrl.evaluate({'ImagesPath': Context.Config.ImagesPath, 'api': Context.Config.Api, 'domain': DataLinkLocations.Domain, 'code': DataLinkLocations.Code, 'list': LOCATION});
  var locationsSourceUrl = WidgetTemplateDataLinkLocationsUrl.evaluate({'ImagesPath': Context.Config.ImagesPath, 'api': Context.Config.Api, 'domain': DataLinkLocations.Domain, 'code': DataLinkLocations.Code, 'list': LOCATION});
  this.aStores[LOCATION] = {
    "sourceCountUrl" : locationsCountSourceUrl,
    "sourceUrl" : locationsSourceUrl,
    "list" : LOCATION,
    "isRemote" : true
  };
};

CGWidgetLink.prototype.setItem = function (code, value) {
  this.Item.code = code;
  this.Item.value = value;
};

CGWidgetLink.prototype.getData = function () {
  if (!this.Target) return null;
  if (!this.Target.getCode) return null;

  var Result = new Object();
  Result.code = this.Target.getCode();
  Result.order = -1;
  Result.value = new Array();
  Result.value.push({code: CGIndicator.CODE, order: 1, value: this.Item.code});
  Result.value.push({code: CGIndicator.VALUE, order: 2, value: this.Item.value});
  Result.value.push({code: CGIndicator.NODE_LINK, order: 3, value: this.Item.code});
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

CGWidgetLink.prototype.setData = function (sData) {
  if (!this.Target) return;
  if (!this.extValue) return;

  var Attribute = new CGAttribute();
  Attribute.unserialize(sData);

  var IndicatorCode = Attribute.getIndicator(CGIndicator.CODE);
  var sValue = Attribute.getIndicatorValue(CGIndicator.VALUE);

  var code = null;
  if (IndicatorCode) code = IndicatorCode.getValue();
  if (code == null) {
    code = Attribute.getIndicatorValue(CGIndicator.NODE_LINK);
  }

  this.extValue.dom.name = code;
  this.extValue.dom.value = sValue;

  this.setItem(code, sValue);

  if (this.extValue.dom.value != "") this.showClearValue();
  else this.hideClearValue();

  this.validate();

  if (this.onChange) this.onChange();
};

CGWidgetLink.prototype.setTarget = function (Target) {
  this.Target = Target;

  this.createRequiredWidget();
  this.createOptions();
  this.createOnlineMenu();
  this.setMessageWhenEmpty(this.Target.getMessageWhenEmpty());
  this.setItem(this.extValue.dom.name, this.extValue.dom.value);
  this.addListeners();
  //this.updateData();
};

CGWidgetLink.prototype.addNodeLink = function (sLabel) {
  if (!this.Target.getLinkTemplates) return;

  var Process = new CGProcessAddFieldNodeLink();
  var Templates = this.Target.getLinkTemplates();

  if (Templates.Edit == null) return;

  this.NewItem = new Object();
  this.NewItem.sLabel = sLabel;

  Process.Code = this.aCodeNodeTypes[0];
  Process.Label = sLabel;
  Process.Mode = Templates.Edit;
  Process.IdParent = this.Target.getCollection();
  Process.Container = this.extNodeContainer.dom;
  Process.onComplete = this.onNewItemComplete.bind(this);
  Process.execute();
};

CGWidgetLink.prototype.onNewItemComplete = function (IdNode) {
  var extForm = this.extNodeBox.select(CSS_FORM).first();

  if (extForm) {
    extForm.dom.onFieldFocus = this.atFieldFocus.bind(this);
    extForm.dom.onFieldBlur = this.atFieldBlur.bind(this);
    extForm.dom.onFieldBeforeChange = this.atFieldBeforeChange.bind(this);
    extForm.dom.onFieldChange = this.atFieldChange.bind(this);
  }

  this.lastNode = this.Item.code;
  this.select({code: IdNode, value: this.NewItem.sLabel});
};

CGWidgetLink.prototype.select = function (Data) {
  this.setItem(Data.code, Data.value);
  this.extValue.dom.value = this.Item.value;
  this.validate();
  this.updateData();
  this.Editor.refresh();
  this.showClearValue();

  if (this.onSelect) this.onSelect(Data);
};

CGWidgetLink.prototype.selectOther = function (Data) {
  this.extNodeContainer.dom.innerHTML = WidgetLoadingTemplate.evaluate({'ImagesPath': Context.Config.ImagesPath});
  this.addNodeLink(Data.value);
  this.showClearValue();
};

CGWidgetLink.prototype.unSelect = function () {
  this.setItem(EMPTY, EMPTY);
  this.extValue.dom.value = this.Item.value;
  this.hideClearValue();
  this.validate();
  this.updateData();
};

CGWidgetLink.prototype.blur = function () {
  this.extValue.removeClass(CLASS_FOCUS);
  this.extNodeBox.removeClass(CLASS_FOCUS);
  //var extForm = this.extNodeBox.select(CSS_FORM).first();
  //if (extForm) extForm.dom.blur();
};

CGWidgetLink.prototype.showOnlineMenu = function () {
  this.extOptionHiperLink.dom.innerHTML = this.Item.value;
  this.extOptionHiperLink.dom.alt = Lang.Goto + this.Item.value;
  this.extOptionHiperLink.dom.title = Lang.Goto + this.Item.value;
  this.extOnlineMenu.show();
};

CGWidgetLink.prototype.getValue = function () {
  return this.Item.value;
};

CGWidgetLink.prototype.getValueCode = function () {
  return this.Item.code;
};

CGWidgetLink.prototype.clearValue = function () {
  this.focus();
  this.unSelect();
  this.extOnlineMenu.hide();
  if (this.onClearValue) this.onClearValue(this);
};

// #############################################################################################################

CGWidgetLink.prototype.atFocused = function () {
  if (!this.Target) return;
  if (!this.isReady()) this.init();
  if (this.onFocused) this.onFocused();

  this.refreshStoreFilters();

  this.Editor.show();
  this.Editor.setConfiguration({Field: this.Target, allowOthers: true, Dialogs: [
    {sName: HISTORY, Store: this.aStores[HISTORY]},
    {sName: SOURCE_STORE, Store: this.aStores[DATA_LINK], ColumnModel: this.ColumnModel, Parameters: this.Target.getParameters()},
    {sName: LOCATION, Store: this.aStores[LOCATION], Parameters: this.Target.getParameters()}
  ]});
  this.Editor.onSelect = this.atSelect.bind(this);
  this.Editor.onSelectOther = this.atSelectOther.bind(this);
  this.Editor.onLoadDefaultValue = this.atLoadDefaultValue.bind(this);
  this.Editor.onAddDefaultValue = this.atAddDefaultValue.bind(this);
  this.Editor.onClearValue = this.atClearValue.bind(this);
  this.Editor.refresh();

  if (this.isLocked()) this.Editor.lock();
  else this.Editor.unLock();

  this.extValue.addClass(CLASS_FOCUS);
  this.extValue.dom.select();
  this.extNodeBox.addClass(CLASS_FOCUS);
  this.bSelectingNode = false;

  if (this.Target.getInfo) this.Editor.setInfo(this.Target.getInfo());
  if (this.extMessageEmpty) this.extMessageEmpty.dom.style.display = "none";

  if (this.Item.code != "") this.showOnlineMenu();
};

CGWidgetLink.prototype.atBlur = function (oEvent) {
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
  if (this.extOnlineMenu) this.extOnlineMenu.hide();
  this.delayBlur = true;
};

CGWidgetLink.prototype.atSelect = function (Data, Source) {
  this.select(Data);
  this.bSelectingNode = true;
};

CGWidgetLink.prototype.atSelectOther = function (Data, Source) {
  this.selectOther(Data);
};

CGWidgetLink.prototype.atChange = function (oEvent) {
  this.unSelect(EMPTY, EMPTY);
};

CGWidgetLink.prototype.atClearValue = function () {
  this.clearValue();
};

CGWidgetLink.prototype.atHiperLinkClick = function () {
  CommandListener.dispatchCommand("showlinknode(" + this.Target.getNodeId() + "," + this.Item.code + ")");
  this.extOnlineMenu.hide();
};

CGWidgetLink.prototype.atKeyUp = function (oEvent) {
  var codeKey = oEvent.getKey();

  if ((codeKey == oEvent.ESC) && (this.onEscape)) this.onEscape();
  else if ((codeKey == oEvent.ENTER) || (codeKey == oEvent.TAB)) {
    var Dialog = this.Editor.getDialog(SOURCE_STORE);
    if (Dialog) {
      var Data = Dialog.getData();
      if (Data) this.select(Data);
    }
    if ((codeKey == oEvent.ENTER) && (this.onEnter)) this.onEnter();
  }
  else if (codeKey == oEvent.DOWN) this.Editor.moveDown(this.extValue.dom);
  else if (codeKey == oEvent.UP) this.Editor.moveUp(this.extValue.dom);
  else if (this.onKeyPress) this.onKeyPress(this.extValue.dom.value, codeKey);

  if (this.extMessageEmpty) this.extMessageEmpty.dom.style.display = (this.extValue.dom.value == "") ? "block" : "none";

  if (this.extValue.dom.value != "") this.showClearValue();
  else this.hideClearValue();

  Event.stop(oEvent);
  return false;
};

CGWidgetLink.prototype.atFieldFocus = function (DOMField) {
  DOMField.blur();
  this.Target.gotoField(DOMField.getPath());
};

CGWidgetLink.prototype.atFieldBlur = function () {
  //if (this.onBlur) this.onBlur();
};

CGWidgetLink.prototype.atFieldBeforeChange = function (DOMField) {
  if (this.onBeforeChange) this.onBeforeChange(DOMField);
};

CGWidgetLink.prototype.atFieldChange = function (DOMField) {
  if (this.onChange) this.onChange(DOMField);
};

CGWidgetLink.prototype.atOnlineMenuClick = function () {
  this.cancelBlur = true;
};