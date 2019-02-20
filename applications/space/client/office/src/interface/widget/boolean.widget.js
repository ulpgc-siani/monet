CGWidgetBoolean = function (extWidget) {
  this.base = CGWidgetSelect;
  this.base(extWidget);

  if (!extWidget) return;
};

CGWidgetBoolean.prototype = new CGWidgetSelect;

CGWidgetBoolean.prototype.init = function () {
  this.initStore();
  this.bIsReady = true;
};

CGWidgetBoolean.prototype.initStore = function () {
  sData = [
    ["true", Lang.Widget.Boolean.Yes],
    ["false", Lang.Widget.Boolean.No]
  ];
  this.aStores[SOURCE_STORE] = new Ext.data.SimpleStore({
    fields: ['code', 'label'],
    data: sData
  });
  this.aStores[SOURCE_STORE].isRemote = function () {
    return false;
  };
};

CGWidgetBoolean.prototype.createOptions = function () {
};

CGWidgetBoolean.prototype.destroyOptions = function () {
};

CGWidgetBoolean.prototype.setStateAndUpdate = function (bValue) {
    this.setState(bValue);
    this.updateData();
};

CGWidgetBoolean.prototype.setState = function (bValue) {

  if (bValue) {
    this.Item.code = "true";
    this.Item.value = "X";
    this.extValue.dom.checked = true;
  }
  else {
    this.Item.code = "false";
    this.Item.value = "";
    this.extValue.dom.checked = false;
  }

  this.validate();
};

CGWidgetBoolean.prototype.setData = function (sData) {
  if (!this.Target) return;
  if (!this.extValue) return;

  var Attribute = new CGAttribute();
  Attribute.unserialize(sData);

  var OptionAttribute = Attribute.getAttributeList().getAttribute(CGAttribute.OPTION);
  if (OptionAttribute != null) Attribute = OptionAttribute;

  var code = Attribute.getIndicatorValue(CGIndicator.CODE);
  var sValue = Attribute.getIndicatorValue(CGIndicator.VALUE);

  this.setItem(code, sValue, EMPTY);
  this.extValue.dom.value = sValue;
  this.extValue.dom.checked = code!="" && code=="true";
  this.setStateAndUpdate(this.extValue.dom.checked);

  this.validate();

  if (this.onChange) this.onChange();
};

CGWidgetBoolean.prototype.setTarget = function (Target) {
  this.Target = Target;

  this.createRequiredWidget();
  this.setItem(this.extValue.dom.name, this.extValue.dom.value, EMPTY);
  this.createOptions();
  this.setState(this.extValue.dom.checked);
};

CGWidgetBoolean.prototype.toggle = function () {
  this.setStateAndUpdate(!this.extValue.dom.checked);
};

CGWidgetBoolean.prototype.clearValue = function () {
  this.focus();
  this.setStateAndUpdate(false);
  if (this.onClearValue) this.onClearValue(this);
};

// #############################################################################################################

CGWidgetBoolean.prototype.atFocused = function () {
  if (!this.Target) return;
  if (!this.isReady()) this.init();
  if (this.onFocused) this.onFocused();

  this.Editor.show();
  this.Editor.setConfiguration({Field: this.Target, allowOthers: false, Dialogs: [
    {sName: SOURCE_STORE, Store: this.aStores[SOURCE_STORE]}
  ]});
  this.Editor.onSelect = this.atSelect.bind(this);
  this.Editor.onLoadDefaultValue = this.atLoadDefaultValue.bind(this);
  this.Editor.onAddDefaultValue = this.atAddDefaultValue.bind(this);
  this.Editor.onClearValue = this.atClearValue.bind(this);
  this.Editor.refresh();

  if (this.isLocked()) this.Editor.lock();
  else this.Editor.unLock();

  if (this.Target.getInfo) this.Editor.setInfo(this.Target.getInfo());

  this.extValue.addClass(CLASS_FOCUS);
};

CGWidgetBoolean.prototype.atSelect = function (Data) {
  if (Data.code == "true") this.setStateAndUpdate(true);
  else this.setStateAndUpdate(false);
  if (this.onSelect) this.onSelect(this.Item);
};

CGWidgetBoolean.prototype.atChange = function (oEvent) {
  this.setStateAndUpdate(this.extValue.dom.checked);
  if (this.onSelect) this.onSelect(this.Item);
};

CGWidgetBoolean.prototype.atClearValue = function (oEvent) {
  this.clearValue();
  return false;
};