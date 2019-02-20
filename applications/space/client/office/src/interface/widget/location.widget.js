CGWidgetLocation = function (extWidget) {
  this.base = CGWidget;
  this.base(extWidget);
};

CGWidgetLocation.prototype = new CGWidget;

CGWidgetLocation.prototype.init = function () {
  this.initStores();
  this.bIsReady = true;
};

CGWidgetLocation.prototype.getData = function () {
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

CGWidgetLocation.prototype.getLocation = function () {/*
 var sValue = this.extValue.dom.value;
 return sValue != null ? Ext.util.JSON.decode(sValue) : null;*/
};

// #############################################################################################################

CGWidgetLocation.prototype.atFocused = function (oEvent) {
  if (!this.Target) return;
  if (!this.isReady()) this.init();
  if (this.onFocused) this.onFocused();

  this.Editor.show();
  this.Editor.setConfiguration({Field: this.Target, Dialogs: [
    {sName: LOCATION, Location: this.getLocation()}
  ]});
  this.Editor.onSelect = this.atSelect.bind(this);
  this.Editor.onLoadDefaultValue = this.atLoadDefaultValue.bind(this);
  this.Editor.onAddDefaultValue = this.atAddDefaultValue.bind(this);
  this.Editor.onClearValue = this.atClearValue.bind(this);
  this.Editor.refresh();

  if (this.isLocked()) this.Editor.lock();
  else this.Editor.unLock();

  this.validate();
  this.extValue.addClass(CLASS_FOCUS);
  this.extValue.dom.select();

  if (this.Target.getInfo) this.Editor.setInfo(this.Target.getInfo());
  if (this.extMessageEmpty) this.extMessageEmpty.dom.style.display = "none";
};

CGWidgetLocation.prototype.atSelect = function (sData) {
  this.extValue.dom.value = sData;
  this.validate();
  this.updateData();
  if (this.onSelect) this.onSelect(sData);
};

CGWidgetLocation.prototype.atChange = function (oEvent) {
  if (this.extValue.dom.value != "") this.showClearValue();
  else this.hideClearValue();
  this.validate();
  this.updateData();
};