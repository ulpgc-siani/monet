var SECOND = 1000;
var MINUTE = SECOND*60;
var HOUR = MINUTE*60;
var DAY = HOUR*24;
var WEEK = DAY*7;
var MONTH = DAY*30;
var YEAR = MONTH*12;

Dialog = function() {
  this.DOMLayer = null;
};

Dialog.prototype.init = function(DOMLayer) {
  this.DOMLayer = DOMLayer;
};

Dialog.prototype.show = function() {
  if (!this.DOMLayer) return;
  this.DOMLayer.show();
};

Dialog.prototype.hide = function() {
  if (!this.DOMLayer) return;
  this.DOMLayer.hide();
};

Dialog.prototype.refresh = function() {
};

Dialog.prototype.destroy = function() {
  $(this.DOMLayer).remove();
};

Dialog.prototype.atAccept = function() {
  if (this.onAccept) this.onAccept();
};

Dialog.prototype.atCancel = function() {
  this.hide();
  this.destroy();
  if (this.onCancel) this.onCancel();
};