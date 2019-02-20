CGWidgetSerial = function (extWidget) {
  this.base = CGWidget;
  this.base(extWidget);
};

CGWidgetSerial.prototype = new CGWidget;

CGWidgetSerial.prototype.createOptions = function () {
};

CGWidgetSerial.prototype.init = function () {
  this.bIsReady = true;
};

CGWidgetSerial.prototype.setTarget = function (Target) {
  this.Target = Target;
  this.createRequiredWidget();
  this.setMessageWhenEmpty(this.Target.getMessageWhenEmpty());
  this.createOptions();
  this.validate();
  //this.updateData();
};

// #############################################################################################################

CGWidgetSerial.prototype.atFocused = function (oEvent) {
  if (!this.Target) return;
  if (!this.isReady()) this.init();
  if (this.onFocused) this.onFocused();

  this.Editor.show();
  this.Editor.setConfiguration({Field: this.Target});
  this.Editor.onLoadDefaultValue = this.atLoadDefaultValue.bind(this);
  this.Editor.onAddDefaultValue = this.atAddDefaultValue.bind(this);
  this.Editor.refresh();

  if (this.isLocked()) this.Editor.lock();
  else this.Editor.unLock();

  this.extValue.addClass(CLASS_FOCUS);
  this.extValue.dom.select();

  if (this.Target.getInfo) this.Editor.setInfo(this.Target.getInfo());
  if (this.extMessageEmpty) this.extMessageEmpty.dom.style.display = "none";
};