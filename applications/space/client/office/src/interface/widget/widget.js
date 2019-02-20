CGWidget = function (extWidget) {
  this.extWidget = extWidget;
  if (this.extWidget) {
    this.extWidget.dom.focus = CGWidget.prototype.focus.bind(this);
    this.extWidget.dom.blur = CGWidget.prototype.blur.bind(this);
    this.extWidget.setVisibilityMode(Element.DISPLAY);
  }
  this.Target = null;
  this.Editor = null;
  this.extMessageEmpty = null;
  this.WidgetRequired = null;
  this.aStores = new Array();
  this.bIsReady = false;
  this.ViewMode = null;
  this.isCustomColumnModel = false;
  this.applyBehaviours();
};

CGWidget.prototype.createOptions = function () {
  new Insertion.Bottom(this.extWidget.dom, WidgetOptionsTemplate.evaluate({'ImagesPath': Context.Config.ImagesPath}));
  var extOptions = this.extWidget.select(CSS_WIDGET_ELEMENT_OPTIONS).first();
  this.extOptionClearValue = extOptions.select(CSS_WIDGET_ELEMENT_CLEAR_VALUE).first();
  this.extOptionClearValue.on("click", this.atClearValue, this);
  if (this.extValue.dom.value != "") this.showClearValue();
  else this.hideClearValue();
};

CGWidget.prototype.applyBehaviours = function () {
  if (!this.extWidget) return;

  this.extSuper = this.extWidget.down(CSS_WIDGET_ELEMENT_SUPER);
  this.extValue = this.extWidget.down(CSS_WIDGET_ELEMENT_COMPONENT);

  if (this.extValue == null) {
    this.setId(this.extWidget.dom.id);
    return;
  }

  if (this.atFocused) this.extValue.on("focus", this.atFocused, this);
  if (this.atBlur) this.extValue.on("blur", this.atBlur, this);
  if (this.atChange) this.extValue.on('change', this.atChange, this);
  if (this.atKeyDown) this.extValue.on("keydown", this.atKeyDown, this);
  if (this.atKeyPress) this.extValue.on("keypress", this.atKeyPress, this);
  if (this.atKeyUp) this.extValue.on("keyup", this.atKeyUp, this);

  this.setId(this.extValue.dom.id);
};

CGWidget.prototype.destroyOptions = function () {
  if (this.extOptionClearValue) this.extOptionClearValue.un("click", this.atClearValue, this);
};

CGWidget.prototype.destroyBehaviours = function () {
  if (!this.extValue) return;
  if (this.atFocused) this.extValue.un("focus", this.atFocused, this);
  if (this.atBlur) this.extValue.un("blur", this.atBlur, this);
  if (this.atChange) this.extValue.un('change', this.atChange, this);
  if (this.atKeyDown) this.extValue.un("keydown", this.atKeyDown, this);
  if (this.atKeyPress) this.extValue.un("keypress", this.atKeyPress, this);
  if (this.atKeyUp) this.extValue.un("keyup", this.atKeyUp, this);
};

CGWidget.prototype.isReady = function () {
  return this.bIsReady;
};

CGWidget.prototype.init = function () {
  this.bIsReady = true;
};

CGWidget.prototype.destroy = function () {
  this.destroyOptions();
  this.destroyBehaviours();
};

CGWidget.prototype.initStores = function () {
  var HistoryStore, SourceStore, IndexStore, DataLink;

  if (!this.Target) return;

  if ((this.Target.getHistoryStore) && ((HistoryStore = this.Target.getHistoryStore()) != null)) {
    this.initRemoteHistoryStore(HistoryStore);
  }

  if ((this.Target.getSourceStore) && ((SourceStore = this.Target.getSourceStore()) != null)) {
    if (SourceStore.IsRemote) this.initRemoteSourceStore(SourceStore);
    else this.initLocalSourceStore(SourceStore);
  }

  if ((this.Target.getIndexStore) && ((IndexStore = this.Target.getIndexStore()) != null)) {
    this.initRemoteIndexStore(IndexStore);
  }

  if ((this.Target.getDataLink) && ((DataLink = this.Target.getDataLink()) != null)) {
    this.initRemoteDataLink(DataLink);
  }
};

CGWidget.prototype.initRemoteHistoryStore = function (HistoryStore) {
  var sHistoryStoreUrl = WidgetTemplateHistoryStoreUrl.evaluate({'ImagesPath': Context.Config.ImagesPath, 'api': Context.Config.Api, 'code': HistoryStore.Code, 'list': HISTORY});
  this.aStores[HISTORY] = new Ext.data.Store({
    proxy: new Ext.data.HttpProxy({ url: sHistoryStoreUrl }),
    reader: new Ext.data.JsonReader({root: 'rows', totalProperty: 'nrows'}, [
      {name: 'code'},
      {name: 'label'}
    ]),
    remoteSort: true
  });
  this.aStores[HISTORY].isRemote = function () {
    return true;
  };
  this.aStores[HISTORY].list = HISTORY;
  this.aStores[HISTORY].ShowCode = HistoryStore.ShowCode;
};

CGWidget.prototype.refreshStoreFrom = function () {
  if (!this.Target || !this.Target.getStoreFromValue)
    return;

  var value = this.Target.getStoreFromValue();

  if (this.aStores[SOURCE_STORE]) this.aStores[SOURCE_STORE].From = value;
  if (this.aStores[INDEX]) this.aStores[INDEX].From = value;
};

CGWidget.prototype.refreshStoreFilters = function () {
  if (!this.Target || !this.Target.getStoreFiltersValues)
    return;

  var filtersValues = this.Target.getStoreFiltersValues();

  if (this.aStores[INDEX]) this.aStores[INDEX].Filters = filtersValues;
  if (this.aStores[SOURCE_STORE]) this.aStores[SOURCE_STORE].Filters = filtersValues;
  if (this.aStores[DATA_LINK]) this.aStores[DATA_LINK].Filters = filtersValues;
};

CGWidget.prototype.isFlatten = function (SourceStore) {
  return SourceStore.Flatten != null && SourceStore.Flatten != "" && SourceStore.Flatten != "none";
};

CGWidget.prototype.initRemoteSourceStore = function (SourceStore) {
  var isFlatten = this.isFlatten(SourceStore);
  var sSourceURL = WidgetTemplateSourceUrl.evaluate({'ImagesPath': Context.Config.ImagesPath, 'api': Context.Config.Api, 'list': SOURCE_STORE,
    'flatten': SourceStore.Flatten, 'depth': SourceStore.Depth});
  this.aStores[SOURCE_STORE] = new Ext.data.Store({
    proxy: new Ext.data.HttpProxy({ url: sSourceURL }),
    reader: new Ext.data.JsonReader({root: 'rows', totalProperty: 'nrows'}, [
      {name: 'code'},
      {name: isFlatten ? 'flatten_label' : 'label'}
    ]),
    remoteSort: true
  });
  this.aStores[SOURCE_STORE].isRemote = function () {
    return true;
  };
  this.aStores[SOURCE_STORE].getSourceId = function () {
    return this.SourceId;
  };
  this.aStores[SOURCE_STORE].setSourceId = function (SourceId) {
    this.SourceId = SourceId;
  };
  this.aStores[SOURCE_STORE].list = SOURCE_STORE;
  this.aStores[SOURCE_STORE].ShowCode = SourceStore.ShowCode;
  this.aStores[SOURCE_STORE].From = SourceStore.From;
  this.aStores[SOURCE_STORE].isFlatten = isFlatten;
  this.aStores[SOURCE_STORE].setSourceId(SourceStore.Id);
};

CGWidget.prototype.initLocalSourceStore = function (SourceStore) {
  var isFlatten = this.isFlatten(SourceStore);
  this.aStores[SOURCE_STORE] = new Ext.data.Store({
    //fields: ['code','value'],
    proxy: new Ext.data.MemoryProxy(SourceStore.Items),
    reader: new Ext.data.ArrayReader({}, [
      {name: 'code'},
      {name: isFlatten ? 'flatten_label' : 'label'}
    ]),
    remoteSort: false
    //data: SourceStore.Items
  });
  this.aStores[SOURCE_STORE].isRemote = function () {
    return false;
  };
  this.aStores[SOURCE_STORE].getSourceId = function () {
    return this.SourceId;
  };
  this.aStores[SOURCE_STORE].setSourceId = function (SourceId) {
    this.SourceId = SourceId;
  };
  this.aStores[SOURCE_STORE].list = SOURCE_STORE;
  this.aStores[SOURCE_STORE].ShowCode = SourceStore.ShowCode;
  this.aStores[SOURCE_STORE].isFlatten = isFlatten;
  this.aStores[SOURCE_STORE].setSourceId(null);
};

CGWidget.prototype.initRemoteIndexStore = function (IndexStore) {
  var isFlatten = this.isFlatten(IndexStore);
  var sIndexUrl = WidgetTemplateIndexUrl.evaluate({'ImagesPath': Context.Config.ImagesPath, 'api': Context.Config.Api, 'list': INDEX,
    'flatten': SourceStore.Flatten, 'depth': SourceStore.Depth });
  this.aStores[INDEX] = new Ext.data.Store({
    proxy: new Ext.data.HttpProxy({ url: sIndexUrl }),
    reader: new Ext.data.JsonReader({root: 'rows', totalProperty: 'nrows'}, [
      {name: 'code'},
      {name: isFlatten ? 'flatten_label' : 'label'}
    ]),
    remoteSort: true
  });
  this.aStores[INDEX].isRemote = function () {
    return true;
  };
  this.aStores[INDEX].getSourceId = function () {
    return this.SourceId;
  };
  this.aStores[INDEX].setSourceId = function (SourceId) {
    this.SourceId = SourceId;
  };
  this.aStores[INDEX].list = INDEX;
  this.aStores[INDEX].ShowCode = IndexStore.ShowCode;
  this.aStores[INDEX].From = SourceStore.From;
  this.aStores[INDEX].Filters = SourceStore.Filters;
  this.aStores[INDEX].isFlatten = isFlatten;
  this.aStores[INDEX].setSourceId(IndexStore.Id);
};

CGWidget.prototype.initRemoteDataLink = function (DataLink) {
  var sDataLinkURL = WidgetTemplateDataLinkUrl.evaluate({'ImagesPath': Context.Config.ImagesPath, 'api': Context.Config.Api, 'domain': DataLink.Domain, 'code': DataLink.Code, 'list': DATA_LINK});
  this.aStores[DATA_LINK] = new Ext.data.Store({
    proxy: new Ext.data.HttpProxy({ url: sDataLinkURL }),
    reader: this.getReader(),
    remoteSort: true
  });
  this.aStores[DATA_LINK].isRemote = function () {
    return true;
  };
  this.aStores[DATA_LINK].list = DATA_LINK;
  this.aStores[DATA_LINK].Filters = DataLink.Filters;
  this.aStores[DATA_LINK].ShowCode = DataLink.ShowCode;
};

CGWidget.prototype.getReader = function () {
  var sDefaultReader = "new Ext.data.JsonReader({root: 'rows', totalProperty: 'nrows'}, [{name:'code'},{name:'label'},{name:'body'}])";

  if (!this.Target) return eval(sDefaultReader);
  if (!this.Target.getHeader) return eval(sDefaultReader);

  var Header = this.Target.getHeader();
  if (Header.Attributes.length <= 0) return eval(sDefaultReader);

  var sHeaders = "{name:'code'},{name:'label'},";
  for (var i = 0; i < Header.Attributes.length; i++) {
    var Attribute = Header.Attributes[i];
    sHeaders += "{name:'" + Attribute[0] + "'},";
  }
  sHeaders = sHeaders.substring(0, sHeaders.length - 1);

  return eval("new Ext.data.JsonReader({root: 'rows', totalProperty: 'nrows'}, [" + sHeaders + "])");
};

CGWidget.prototype.initColumnModel = function () {
  if (!this.Target) return;
  if (!this.Target.getHeader) return;

  var Header = this.Target.getHeader();
  this.ColumnModel = new Ext.grid.ColumnModel([
    {header: Lang.Editor.Code, dataIndex: 'code'},
    {header: Lang.Editor.Option, sortable: true, dataIndex: 'label'}
  ]);

  if (Header.Attributes.length > 0) {
    var sHeaders = "{header:'" + Lang.Editor.Code + "',dataIndex:'code'},";
    for (var i = 0; i < Header.Attributes.length; i++) {
      var Attribute = Header.Attributes[i];
      sHeaders += "{header:'" + Attribute[1] + "',sortable:true,dataIndex:'" + Attribute[0] + "'},";
    }
    sHeaders = sHeaders.substring(0, sHeaders.length - 1);
    this.ColumnModel = eval("new Ext.grid.ColumnModel([" + sHeaders + "])");
    this.ColumnModel.CodeValueColumn = Header.CodeValueColumn;
    this.isCustomColumnModel = true;
  }

  this.ColumnModel.name = this.Target.getCode();
};

CGWidget.prototype.setId = function (Id) {
  if (this.extValue) this.extValue.dom.id = Id;
  else this.extWidget.dom.id = Id;
  this.Id = Id;
};

CGWidget.prototype.getId = function () {
  return this.Id;
};

CGWidget.prototype.getPath = function () {
  if (this.extValue) return this.extValue.dom.id;
  else return this.extWidget != null ? this.extWidget.dom.id : "";
};

CGWidget.prototype.getCode = function () {
  return this.extWidget.dom.id;
};

CGWidget.prototype.getOrder = function () {
  return (this.extWidget.dom.iOrder != null) ? this.extWidget.dom.iOrder : "-1";
};

CGWidget.prototype.getDOM = function () {
  if (this.extWidget == null) return null;
  return this.extWidget.dom;
};

CGWidget.prototype.getTarget = function () {
  return this.Target;
};

CGWidget.prototype.createRequiredWidget = function () {
  if ((this.Target.isRequired()) && (!this.WidgetRequired)) {
    this.WidgetRequired = WidgetFactory.get(WIDGET_REQUIRED, this.extWidget);
    this.WidgetRequired.setTarget(this.Target);
    this.WidgetRequired.init();
  }
};

CGWidget.prototype.setTarget = function (Target) {
  this.Target = Target;
  this.createRequiredWidget();
  this.createOptions();
  this.setMessageWhenEmpty(this.Target.getMessageWhenEmpty());
  this.validate();
  //this.updateData();
};

CGWidget.prototype.getViewMode = function () {
  return this.ViewMode;
};

CGWidget.prototype.setViewMode = function (ViewMode) {
  var aExtViews = this.extWidget.select(CSS_WIDGET_ELEMENT_VIEW_MODE);
  if (this.ViewMode) this.extWidget.removeClass(this.ViewMode);
  this.extWidget.addClass(ViewMode);
  this.ViewMode = ViewMode;
  aExtViews.each(function (extView) {
    var extParent = extView.up(CSS_WIDGET);
    if (extParent.dom != this.extWidget.dom) return;
    if (extView.hasClass(ViewMode)) extView.dom.style.display = "block";
    else extView.dom.style.display = "none";
  }, this);
};

CGWidget.prototype.setEditor = function (Editor) {
  this.Editor = Editor;
};

CGWidget.prototype.setReadonly = function (bValue) {
  if (!this.extValue) return;
  this.extValue.dom.readOnly = bValue;
};

CGWidget.prototype.setWidgetRequired = function (WidgetRequired) {
  this.WidgetRequired = WidgetRequired;
};

CGWidget.prototype.setValue = function (sValue) {
  if (!this.extValue) return;
  this.extValue.dom.value = sValue;
};

CGWidget.prototype.getValue = function () {
  if (!this.extValue) return "";
  return this.extValue.dom.value;
};

CGWidget.prototype.getValueCode = function () {
	if (!this.extValue) return "";
	return this.extValue.dom.value;
};

CGWidget.prototype.getSuperId = function () {
  if (!this.extSuper) return "";
  return this.extSuper.dom.value;
};

CGWidget.prototype.getData = function () {
  if (!this.Target) return null;
  if (!this.Target.getCode) return null;
  if (!this.extValue) return null;

  var Result = new Object();
  Result.code = this.Target.getCode();
  Result.order = -1;
  Result.value = new Array();
  Result.value.push({code: CGIndicator.VALUE, order: 1, value: this.extValue.dom.value});
  if (this.Target.isSuper() && (this.extSuper != null)) Result.value.push({code: CGIndicator.SUPER, order: 2, value: this.extSuper.dom.value});

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

CGWidget.prototype.updateData = function () {
  if (this.onChange) this.onChange();
};

CGWidget.prototype.setData = function (sData) {
  if (!this.Target) return;
  if (!this.extValue) return;

  var Attribute = new CGAttribute();
  Attribute.unserialize(sData);

  this.extValue.dom.value = "";

  var IndicatorValue = Attribute.getIndicator(CGIndicator.VALUE);
  if (IndicatorValue) this.extValue.dom.value = IndicatorValue.getValue().replace(/<br\/>/g, "\n");

  if (this.extOptionClearValue) {
    if (this.extValue.dom.value != "") this.showClearValue();
    else this.hideClearValue();
  }

  this.validate();

  if (this.onChange) this.onChange();
};

CGWidget.prototype.setDefault = function (sData) {
  this.setData(sData);
};

CGWidget.prototype.validate = function () {
  if (!this.extValue) return;

  if (this.WidgetRequired) {
    if (this.extValue.dom.value != "") this.WidgetRequired.hide();
    else this.WidgetRequired.show();
  }

  if (this.extMessageEmpty) this.extMessageEmpty.dom.style.display = (this.extValue.dom.value == "") ? "block" : "none";
};

CGWidget.prototype.isFocused = function () {
  if (!this.extValue) return this.bFocused;
  return this.extValue.hasClass(CLASS_FOCUS);
};

CGWidget.prototype.focus = function () {
  if (this.extValue) this.extValue.dom.focus();
  else {
    this.bFocused = true;
    this.extWidget.addClass(CLASS_FOCUS);
    if (this.onFocused) this.onFocused();
  }
};

CGWidget.prototype.blur = function () {
  if (this.extValue) this.extValue.removeClass(CLASS_FOCUS);
  else {
    this.bFocused = false;
    this.extWidget.removeClass(CLASS_FOCUS);
  }
  if (this.onBlur) this.onBlur();
};

CGWidget.prototype.show = function () {
  if (this.extWidget) this.extWidget.show();
};

CGWidget.prototype.lockClearValue = function () {
  if (this.extOptionClearValue) this.extOptionClearValue.locked = true;
};

CGWidget.prototype.showClearValue = function () {
  if ((this.extOptionClearValue) && (!this.extOptionClearValue.locked)) this.extOptionClearValue.dom.style.display = "block";
};

CGWidget.prototype.hideClearValue = function () {
  if ((this.extOptionClearValue) && (!this.extOptionClearValue.locked)) this.extOptionClearValue.dom.style.display = "none";
};

CGWidget.prototype.hide = function () {
  if (this.extWidget) this.extWidget.hide();
};

CGWidget.prototype.setMessageWhenEmpty = function (sMessage) {
  if (!this.Target) return;
  if (!this.extValue) return;

  if ((sMessage == null) || (sMessage == "")) return;

  var id = Ext.id();
  new Insertion.After(this.extValue.dom, "<div id='" + id + "' class='empty'>" + sMessage + "</div>");
  this.extMessageEmpty = Ext.get(id);
  Event.observe(this.extMessageEmpty.dom, "click", CGWidget.prototype.atMessageEmptyClick.bind(this));
};

CGWidget.prototype.isLocked = function () {

  if (this.extWidget.hasClass(CLASS_LOCKED)) return true;

  var extParentWidget = this.extWidget.up(CSS_WIDGET);
  while (extParentWidget != null) {
    if (extParentWidget.hasClass(CLASS_LOCKED)) return true;
    extParentWidget = extParentWidget.up(CSS_WIDGET);
  }

  return false;
};

CGWidget.prototype.lock = function () {
  if (this.extWidget) {
    if (this.extWidget.hasClass(CLASS_FOCUS)) this.extWidget.dom.blur();
    this.extWidget.addClass(CLASS_LOCKED);
  }
  if (this.extValue) this.extValue.dom.disabled = true;
};

CGWidget.prototype.unLock = function () {
  if (!this.Target) return;
  if (this.Target.isLockedByDefinition()) return;
  if (this.extWidget) this.extWidget.removeClass(CLASS_LOCKED);
  if (this.extValue) this.extValue.dom.disabled = false;
  if (this.onUnLock) this.onUnLock();
};

CGWidget.prototype.setObserver = function (Observer, iPos) {
  if (!this.extValue) return;
  var DOMFullname, DOMObserver = this.extValue.getNextSibling();

  if (DOMObserver != null && !DOMObserver.hasClassName(CLASS_OBSERVER)) DOMObserver = null;

  if (!DOMObserver) {
    new Insertion.After(this.extValue.dom, WidgetObserverTemplate.evaluate());
    DOMObserver = this.extValue.getNextSibling();
    DOMFullname = DOMObserver.down("span");
    CommandListener.capture(DOMObserver);
  }
  else DOMFullname = DOMObserver.down("span");

  if (Observer == null) {
    DOMObserver.hide();
    DOMObserver.style.backgroundColor = "";
    this.extValue.dom.style.borderColor = "";
    this.extWidget.dom.style.borderColor = "";
    this.unLock();
  }
  else {
    DOMObserver.show();
    DOMObserver.style.backgroundColor = getColor(iPos);
    DOMFullname.innerHTML = Observer.fullname;
    this.extValue.dom.style.borderColor = getColor(iPos);
    this.extWidget.dom.style.borderColor = getColor(iPos);
    this.lock();
  }
};

CGWidget.prototype.clearValue = function (oEvent) {
  this.focus();
  this.extValue.dom.value = "";
  this.hideClearValue();
  this.validate();
  this.updateData();
  if (this.onClearValue) this.onClearValue(this);
};

CGWidget.prototype.reset = function () {
  this.setData("");
};

// #############################################################################################################

CGWidget.prototype.atBlur = function (oEvent) {
  if ((this.extValue) && (this.extMessageEmpty)) this.extMessageEmpty.dom.style.display = (this.extValue.dom.value == "") ? "block" : "none";
};

CGWidget.prototype.atKeyUp = function (oEvent) {
  var codeKey = oEvent.getKey();

  if ((codeKey == oEvent.ESC) && (this.onEscape)) this.onEscape();
  if ((codeKey == oEvent.ENTER) && (this.onEnter)) this.onEnter();

  if ((this.extValue) && (this.extMessageEmpty)) this.extMessageEmpty.dom.style.display = (this.extValue.dom.value == "") ? "block" : "none";

  if ((this.extValue) && (this.extValue.dom.value != "")) this.showClearValue();
  else this.hideClearValue();

  return false;
};

CGWidget.prototype.atLoadDefaultValue = function (oEvent) {
  if (this.onLoadDefaultValue) this.onLoadDefaultValue();
};

CGWidget.prototype.atAddDefaultValue = function (oEvent) {
  if (this.onAddDefaultValue) this.onAddDefaultValue();
};

CGWidget.prototype.atClearValue = function (oEvent) {
  this.clearValue();
  return false;
};

CGWidget.prototype.atMessageEmptyClick = function (oEvent) {
  this.extMessageEmpty.dom.style.display = "none";
  if (this.extValue) this.extValue.focus();
  Event.stop(oEvent);
  return false;
};