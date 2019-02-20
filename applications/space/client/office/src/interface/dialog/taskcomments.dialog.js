CGDialogTaskComments = function () {
  this.base = CGDialog;
  this.base("dlgTaskComments");
};

CGDialogTaskComments.hideTimeout = null;

//------------------------------------------------------------------
CGDialogTaskComments.prototype = new CGDialog;

//------------------------------------------------------------------
CGDialogTaskComments.prototype.init = function() {

  var html = AppTemplate.DialogTaskComments;
  html = translate(html, Lang.DialogTaskComments);

  var layer = Ext.get("dlgTaskComments");
  this.layer = (layer != null) ? layer.dom : new Insertion.Bottom(document.body, html).element.immediateDescendants().last();

  this.addBehaviours();
};

//------------------------------------------------------------------
CGDialogTaskComments.prototype.addBehaviours = function () {
  Event.observe(this.layer, "mouseover", CGDialogTaskComments.prototype.atMouseOver.bind(this));
  Event.observe(this.layer, "mouseout", CGDialogTaskComments.prototype.atMouseOut.bind(this));
};

//------------------------------------------------------------------
CGDialogTaskComments.prototype.refreshPosition = function () {
  var position = this.Target.position;

  this.layer.style.left = (position.x-this.layer.getWidth()) + "px";
  this.layer.style.top = (position.y+20) + "px";
};

//------------------------------------------------------------------
CGDialogTaskComments.prototype.refreshComments = function () {
  Ext.get(this.layer).select(".body").first().dom.innerHTML = this.Target.comments.replace(/\\n/g, "<br/>");
};

//------------------------------------------------------------------
CGDialogTaskComments.prototype.show = function () {
  this.cancelHide();
  this.layer.show();
};

//------------------------------------------------------------------
CGDialogTaskComments.prototype.doHide = function () {
  this.layer.hide();
};

//------------------------------------------------------------------
CGDialogTaskComments.prototype.getHeight = function () {
  return this.layer.getHeight();
};

//------------------------------------------------------------------
CGDialogTaskComments.prototype.cancelHide = function () {
  if (CGDialogTaskComments.hideTimeout != null)
    window.clearTimeout(CGDialogTaskComments.hideTimeout);
};

//------------------------------------------------------------------
CGDialogTaskComments.prototype.hide = function () {
  if (CGDialogTaskComments.hideTimeout != null)
    window.clearTimeout(CGDialogTaskComments.hideTimeout);

  CGDialogTaskComments.hideTimeout = window.setTimeout(CGDialogTaskComments.prototype.doHide.bind(this), 400);
};

//------------------------------------------------------------------
CGDialogTaskComments.prototype.refresh = function () {
  this.refreshPosition();
  this.refreshComments();
};

//------------------------------------------------------------------
CGDialogTaskComments.prototype.destroy = function () {
  this.layer.remove();
}

//==================================================================
CGDialogTaskComments.prototype.atMouseOver = function () {
  this.cancelHide();
};

CGDialogTaskComments.prototype.atMouseOut = function () {
  this.hide();
};