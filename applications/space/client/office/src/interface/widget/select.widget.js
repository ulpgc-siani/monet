CGWidgetSelect = function (extWidget) {
  this.base = CGWidget;
  this.base(extWidget);
  this.Item = new Object();
  this.Item.code = EMPTY;
  this.Item.value = EMPTY;
  this.Item.other = EMPTY;
  this.bIsValueOtherActive = false;
  this.sources = null;

  if (!extWidget) return;

  if (this.extValue) {
    this.Item.code = this.extValue.dom.name;
    this.Item.value = this.extValue.dom.value;
  }

  this.extRadioList = this.extWidget.down(CSS_WIDGET_ELEMENT_RADIO_LIST);
  this.registerRadios();
};

CGWidgetSelect.prototype = new CGWidget;

CGWidgetSelect.prototype.showClearValue = function () {
  if (this.extRadioList != null) this.extOptionClearValue.dom.style.display = "none";
  else if ((this.extOptionClearValue) && (!this.extOptionClearValue.locked)) this.extOptionClearValue.dom.style.display = "block";
};

CGWidgetSelect.prototype.focus = function () {
  if (this.extRadioList == null) this.extValue.dom.focus();
  else {
    this.bFocused = true;
    this.extWidget.addClass(CLASS_FOCUS);
    if (this.onFocused) this.onFocused();
  }
};

CGWidgetSelect.prototype.blur = function () {
  if (this.extRadioList == null) this.extValue.removeClass(CLASS_FOCUS);
  else {
    this.bFocused = false;
    this.extWidget.removeClass(CLASS_FOCUS);
  }
  if (this.onBlur) this.onBlur();
};

CGWidgetSelect.prototype.registerRadio = function (extRadio) {
  var idRadio = Ext.id();
  var extChecker = extRadio.down("input");
  var extLabel = extRadio.down("label");

  if (extChecker == null) return;

  extChecker.dom.id = idRadio;
  extLabel.dom.htmlFor = idRadio;
  Event.observe(extChecker.dom, "click", CGWidgetSelect.prototype.atRadioClick.bind(this, extRadio.dom, extChecker.dom));
  Event.observe(extChecker.dom, "blur", CGWidgetSelect.prototype.blur.bind(this));
};

CGWidgetSelect.prototype.registerRadios = function () {
  if (!this.extRadioList) return;
  var extRadioItems = this.extRadioList.select(CSS_WIDGET_ELEMENT_RADIO);
  extRadioItems.each(function (extRadio) {
    this.registerRadio(extRadio);
  }, this);
};

CGWidgetSelect.prototype.init = function () {
  this.initStores();
  this.bIsReady = true;
};

CGWidgetSelect.prototype.getValue = function () {
  return this.Item.value;
};

CGWidgetSelect.prototype.getValueCode = function () {
  return this.Item.code;
};

CGWidgetSelect.prototype.getData = function () {
  if (!this.Target) return null;
  if (!this.Target.getCode) return null;

  var Result = new Object();
  Result.code = this.Target.getCode();
  Result.order = -1;
  Result.value = new Array();
  Result.value.push({code: CGIndicator.CODE, order: 1, value: this.Item.code});
  Result.value.push({code: CGIndicator.SOURCE, order: 1, value: this.SourceId});
  if (this.isValueOtherActive()) {
    Result.value.push({code: CGIndicator.OTHER, order: 2, value: this.Item.other});
  }
  else Result.value.push({code: CGIndicator.VALUE, order: 2, value: this.Item.value});

  if (this.Target.isSuper() && (this.extSuper != null))
    Result.value.push({code: CGIndicator.SUPER, order: 3, value: this.extSuper.dom.value});

  Result.toXml = function () {
    var Attribute = new CGAttribute();
    var OptionAttribute = new CGAttribute();

    Attribute.code = this.code;
    Attribute.iOrder = this.order;
    OptionAttribute.code = CGAttribute.OPTION;
    OptionAttribute.iOrder = 0;

    for (var iPos = 0; iPos < this.value.length; iPos++) {
      OptionAttribute.addIndicatorByValue(this.value[iPos].code, this.value[iPos].order, this.value[iPos].value);
    }
    Attribute.getAttributeList().addAttribute(OptionAttribute);

    return Attribute.serialize();
  };

  return Result;
};

CGWidgetSelect.prototype.setData = function (sData) {
  if (!this.Target) return;

  var Attribute = new CGAttribute();
  Attribute.unserialize(sData);

  var OptionAttribute = Attribute.getAttributeList().getAttribute(CGAttribute.OPTION);
  if (OptionAttribute != null) Attribute = OptionAttribute;

  var code = Attribute.getIndicatorValue(CGIndicator.CODE);
  var sValue = Attribute.getIndicatorValue(CGIndicator.VALUE);
  var sOther = Attribute.getIndicatorValue(CGIndicator.OTHER);
  var sSource = Attribute.getIndicatorValue(CGIndicator.SOURCE);

  if (sData != "" && sSource != "" && this.SourceId != sSource)
    this.SourceId = sSource;

  if (sOther) {
    this.setItem(this.Target.getCodeOnOthers(), EMPTY, sOther);
    this.extValue.dom.value = "";
    this.activateValueOther();
  }
  else {
    this.deactivateValueOther();
    this.setItem(code, sValue, EMPTY);
    this.extValue.dom.value = sValue;

    if (this.extRadioList && code != "") {
      var extRadio = this.extRadioList.select(CSS_WIDGET_ELEMENT_RADIO + " ." + code).first();
      if (extRadio) extRadio.dom.checked = true;
    }
  }

  if (this.extValue.dom.value != "") this.showClearValue();
  else this.hideClearValue();

  this.validate();

  if (this.onChange) this.onChange();
};

CGWidgetSelect.prototype.setItem = function (code, value, other) {
  this.Item.code = code;
  this.Item.value = value;
  this.Item.other = other;
};

CGWidgetSelect.prototype.setTarget = function (Target) {
  this.Target = Target;

  this.createRequiredWidget();
  this.setItem(this.extValue.dom.name, this.extValue.dom.value, EMPTY);

  if (this.extValue.hasClass(CLASS_WIDGET_OTHER)) {
    this.setItem(this.Target.getCodeOnOthers(), EMPTY, this.extValue.dom.value);
    this.extValue.dom.value = "";
    this.activateValueOther();
  }

  this.setMessageWhenEmpty(this.Target.getMessageWhenEmpty());
  this.SourceId = this.extValue.dom.alt;
  this.sources = this.Target.getSources();

  this.createOptions();
  this.validate();
  //this.updateData();
  if (this.extRadioList != null) this.hideClearValue();
};

CGWidgetSelect.prototype.validate = function () {

  if (this.WidgetRequired) {
    if (this.Item.code != "") this.WidgetRequired.hide();
    else this.WidgetRequired.show();
  }

  if (this.extMessageEmpty) this.extMessageEmpty.dom.style.display = (this.extValue.dom.value == "") ? "block" : "none";
};

CGWidgetSelect.prototype.isValueOtherActive = function () {
  return this.bIsValueOtherActive;
};

CGWidgetSelect.prototype.activateValueOther = function () {
  this.bIsValueOtherActive = true;
  this.extValue.dom.value = this.Item.other + BLANK + LEFT_BRACKET + this.Item.code + RIGHT_BRACKET;
};

CGWidgetSelect.prototype.deactivateValueOther = function () {
  this.bIsValueOtherActive = false;
  this.extValue.dom.value = "";
};

CGWidgetSelect.prototype.select = function (SourceId, Data) {
  this.deactivateValueOther();
  this.SourceId = SourceId;
  this.setItem(Data.code, Data.value, EMPTY);
  this.extValue.dom.value = this.Item.value;
  this.showClearValue();
  this.validate();
  this.updateData();
};

CGWidgetSelect.prototype.selectOther = function (SourceId, sValue) {
  this.setItem(this.Target.getCodeOnOthers(), EMPTY, sValue);
  this.activateValueOther();
  this.SourceId = SourceId;
  this.showClearValue();
  this.validate();
  this.updateData();
};

CGWidgetSelect.prototype.lock = function () {
  if (this.extWidget) {
    if (this.extWidget.hasClass(CLASS_FOCUS)) this.extWidget.dom.blur();
    this.extWidget.addClass(CLASS_LOCKED);
  }
  if (this.extValue) {
    this.extValue.dom.disabled = true;
    this.extValue.dom.readOnly = true;
    this.extValue.addClass(CLASS_READONLY);
  }
  if (this.extRadioList) {
    var extRadioItems = this.extRadioList.select(CSS_WIDGET_ELEMENT_RADIO);
    extRadioItems.each(function (extRadio) {
      extRadio.down("input").dom.disabled = true;
    }, this);
  }
};

CGWidgetSelect.prototype.unLock = function () {
  if (this.Target.isLockedByDefinition()) return;
  if (this.extWidget) this.extWidget.removeClass(CLASS_LOCKED);
  if (this.extValue) {
    this.extValue.dom.disabled = false;
    this.extValue.dom.readOnly = false;
    this.extValue.removeClass(CLASS_READONLY);
  }
  if (this.extRadioList) {
    var extRadioItems = this.extRadioList.select(CSS_WIDGET_ELEMENT_RADIO);
    extRadioItems.each(function (extRadio) {
      extRadio.down("input").dom.disabled = false;
    }, this);
  }
  if (this.onUnLock) this.onUnLock();
};

CGWidgetSelect.prototype.clearValue = function (oEvent) {
  this.focus();
  this.setItem(EMPTY, EMPTY, EMPTY);
  this.extValue.dom.value = "";
  this.hideClearValue();
  this.validate();
  this.updateData();
  if (this.onClearValue) this.onClearValue(this);
};

CGWidgetSelect.prototype.refreshStoreSource = function (callback) {

  if (!this.Target || !this.Target.getStorePartnerContextValue || !this.Target.getSources) {
    callback();
    return;
  }

  var value = this.Target.getStorePartnerContextValue();
  if (value == "") {
    callback();
    return;
  }

  var Process = new CGProcessLoadNodeContext();
  Process.Id = value;
  Process.onComplete = this.onContextLoadedComplete.bind(this, callback);
  Process.execute();
};

CGWidgetSelect.prototype.onContextLoadedComplete = function (callback, context) {
  var sources = this.Target.getSources();

  for (var i = 0; i < sources.length; i++) {
    if (sources[i].partner == context)
      this.SourceId = sources[i].id;
  }

  if (this.aStores[SOURCE_STORE]) this.aStores[SOURCE_STORE].SourceId = this.SourceId;
  if (this.aStores[INDEX]) this.aStores[INDEX].SourceId = this.SourceId;

  callback();
};

// #############################################################################################################
CGWidgetSelect.prototype.atFocused = function () {
  if (!this.Target) return;
  if (!this.isReady()) this.init();
  if (this.onFocused) this.onFocused();

  this.refreshStoreFilters();

  this.refreshStoreSource(CGWidgetSelect.prototype.atFocusedCallback.bind(this));
};

CGWidgetSelect.prototype.atFocusedCallback = function () {
  var sendSources = !this.Target.isStorePartnerContextLinked();

  this.refreshStoreFrom();

  this.Editor.show();
  this.Editor.setConfiguration({Field: this.Target, allowOthers: this.Target.allowOthers(), Dialogs: [
    {sName: HISTORY, Store: this.aStores[HISTORY]},
    {sName: SOURCE, SelectedSourceId: this.SourceId, Sources: sendSources ? this.sources : new Array()},
    {sName: SOURCE_STORE, Store: this.aStores[SOURCE_STORE]}
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

  if (this.Target.getInfo) this.Editor.setInfo(this.Target.getInfo());
  if (this.extMessageEmpty) this.extMessageEmpty.dom.style.display = "none";
};

CGWidgetSelect.prototype.atSelect = function (Data, Source) {
  this.select(Source, Data);
  if (this.onSelect) this.onSelect(Data);
};

CGWidgetSelect.prototype.atSelectOther = function (Data, Source) {
  this.selectOther(Source, Data.value);
  if (this.onSelect) this.onSelect(Data);
};

CGWidgetSelect.prototype.atChange = function (oEvent) {
  this.setItem(EMPTY, EMPTY, EMPTY);
  this.extValue.dom.value = "";
  this.hideClearValue();
  this.validate();
  this.updateData();
};

CGWidgetSelect.prototype.atClearValue = function (oEvent) {
  this.clearValue();
  return false;
};

CGWidgetSelect.prototype.atKeyUp = function (oEvent) {
  var codeKey = oEvent.getKey();

  if ((codeKey == oEvent.ESC) && (this.onEscape)) this.onEscape();
  else if ((codeKey == oEvent.ENTER) || (codeKey == oEvent.TAB)) {
    var Source = this.Editor.getSourceId();
    var Data = this.Editor.getData();
    if (Data) this.select(Source, Data);
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

CGWidgetSelect.prototype.atRadioClick = function (DOMCheck, DOMChecker) {
  this.focus();
  this.setItem(DOMChecker.className, DOMChecker.value, EMPTY);
  this.validate();
  this.updateData();
};