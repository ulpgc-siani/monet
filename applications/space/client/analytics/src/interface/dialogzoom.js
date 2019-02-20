DialogZoom = function() {
  this.base = Dialog;
  this.base();
};

DialogZoom.prototype = new Dialog;

DialogZoom.prototype.render = function(DOMLayer) {
  var content = translate(AppTemplate.dialogzoom, Lang.DialogZoom);

  content = replaceAll(content, "${id}", this.id);
  
  this.DOMLayer = DOMLayer;
  this.DOMLayer.innerHTML = content;
  
  if (!this.target) return;

  CommandListener.capture(this.DOMLayer);
  
  this.init();
  this.refresh();
};

DialogZoom.prototype.init = function() {
  this.initBehaviours();
};

DialogZoom.prototype.initBehaviours = function() {
  var jLayer = $(this.DOMLayer);
  
  var jZoomIn = jLayer.find(".zoomin a");
  jZoomIn.click(DialogZoom.prototype.atZoomInClick.bind(this));

  var jRestoreZoom = jLayer.find(".restorezoom a");
  jRestoreZoom.click(DialogZoom.prototype.atRestoreZoomClick.bind(this));
};

DialogZoom.prototype.refresh = function(jsonData) {
};

//************************************************************************************************************

DialogZoom.prototype.atZoomInClick = function() {
  if (this.onZoomIn) this.onZoomIn();
};

DialogZoom.prototype.atRestoreZoomClick = function() {
  if (this.onRestoreZoom) this.onRestoreZoom();
};