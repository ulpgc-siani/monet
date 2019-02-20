CGView = function () {
  this.Id = Ext.id();
  this.DOMLayer = null;
  this.Target = null;
  this.Mode = null;
  this.ViewContainer = null;
  this.Type = null;
};

// private
CGView.prototype.getType = function () {
  return this.Type;
};

CGView.prototype.setType = function (Type) {
  this.Type = Type;
};

CGView.prototype.setDOMLayer = function (DOMLayer) {
  if (DOMLayer == null) return;
  this.DOMLayer = DOMLayer;
  this.DOMLayer.IdView = this.Id;
};

// public
CGView.prototype.init = function (DOMLayer) {
  this.setDOMLayer(DOMLayer);
};

CGView.prototype.getId = function () {
  return this.Id;
};

CGView.prototype.getDOM = function () {
  return $(this.DOMLayer);
};

CGView.prototype.getTarget = function () {
  return this.Target;
};

CGView.prototype.setTarget = function (Target) {
  this.Target = Target;
};

CGView.prototype.getMode = function () {
  return this.Mode;
};

CGView.prototype.setMode = function (Mode) {
  this.Mode = Mode;
};

CGView.prototype.getContainer = function () {
  return this.ViewContainer;
};

CGView.prototype.setContainer = function (ViewContainer) {
  this.ViewContainer = ViewContainer;
};

CGView.prototype.show = function () {
  if (!this.DOMLayer) return;
  this.DOMLayer.show();
};

CGView.prototype.hide = function () {
  if (!this.DOMLayer) return;
  this.DOMLayer.hide();
};

CGView.prototype.refresh = function () {
};

CGView.prototype.destroy = function () {
  EventManager.disableNotifications();
  this.DOMLayer.destroy();
  EventManager.enableNotifications();
};