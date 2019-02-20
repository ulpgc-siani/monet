function CGBehaviour(Config) {
  this.eLayer = null;
  this.CurrentObject = null;
  this.iTimeLeft = 500;
  if (Config) {
    this.sClassName = Config.cls;
  }
};

/*********************************************************************/
/*  Methods                                                          */
/*********************************************************************/
CGBehaviour.prototype.destroyLayer = function () {
  if (this.eLayer) this.eLayer.remove();
};

CGBehaviour.prototype.initLayer = function () {

  this.destroyLayer();
  this.eLayer = new Ext.Layer({shim: true, constrain: true, shadowOffset: 4});
  this.eLayer.activated = false;
  this.eLayer.addClass(this.sClassName);

  Event.observe(this.eLayer.dom, 'mouseover', CGBehaviour.prototype.activateContextualPanel.bind(this));
  Event.observe(this.eLayer.dom, 'mouseout', CGBehaviour.prototype.deactivateContextualPanel.bind(this));

  return true;
};

CGBehaviour.prototype.addCommandObservers = function () {
  if (!this.eLayer) return;
  aCommands = this.eLayer.select(".command");
  aCommands.each(function (Command) {
    Event.observe(Command.dom, "click", CGBehaviour.prototype.atCommandClick.bind(this));
  }, this);
};

CGBehaviour.prototype.atCommandClick = function () {
  this.eLayer.hide();
};

CGBehaviour.prototype.locateLayer = function (eContextualPanel, extObject) {
  if (eContextualPanel.hasClass(Literals.FloatLayer.LocationTop)) this.eLayer.setXY([extObject.getLeft(), extObject.getTop() - extObject.getHeight()]);
  else if (eContextualPanel.hasClass(Literals.FloatLayer.LocationRight)) this.eLayer.setXY([extObject.getRight() + 5, extObject.getTop() - 1]);
  else if (eContextualPanel.hasClass(Literals.FloatLayer.LocationBottom)) this.eLayer.setXY([extObject.getLeft(), extObject.getTop() + extObject.getHeight()]);
  else if (eContextualPanel.hasClass(Literals.FloatLayer.LocationLeft)) this.eLayer.setXY([extObject.getLeft() - this.eLayer.getWidth() - 5, extObject.getTop() - 1]);

  return true;
};

CGBehaviour.prototype.showDelayedContextualPanel = function (Object, EventSent) {
  if (this.P) {
    window.setTimeout(this.showDelayedContextualPanel.bind(this, Object, EventSent), 10);
    return;
  }

  this.P = true;
  if (this.idTimeoutHide) window.clearTimeout(this.idTimeoutHide);
  this.idTimeoutHide = null;

  if (this.idTimeoutShow) window.clearTimeout(this.idTimeoutShow);
  this.idTimeoutShow = Object.idTimeoutShow = window.setTimeout(this.showContextualPanel.bind(this, Object, EventSent), this.iTimeLeft);
  this.LastObject = Object;
  this.P = false;
};

CGBehaviour.prototype.showContextualPanel = function (Object, EventSent) {
  this.idTimeoutShow = Object.idTimeoutShow = null;

  if (!(extObject = Ext.get(Object))) return;
  if (!(extParent = Ext.get(Object.parentNode))) return;
  if (!(eContextualPanel = $(extParent).select(".contextual").first())) return;

  this.eLayer.update(eContextualPanel.dom.innerHTML);
  CommandListener.capture(this.eLayer.dom);

  if (eContextualPanel.hasClass(Literals.FloatLayer.LocationCursor)) {
    this.eLayer.setXY([extObject.getX() + 35, extObject.getTop() + extObject.getHeight() + 10]);
  }
  else {
    this.locateLayer(eContextualPanel, extObject);
  }

//  if (this.idTimeoutHide) window.clearTimeout(this.idTimeoutHide);
//  this.idTimeoutHide = window.setTimeout(this.hideContextualPanel.bind(this), 1500);

  this.eLayer.show();
  this.addCommandObservers();
  this.CurrentObject = Object;
};

CGBehaviour.prototype.hideDelayedContextualPanel = function () {
  if (this.P) {
    window.setTimeout(this.hideDelayedContextualPanel.bind(this), 10);
    return;
  }

  this.P = true;

  if (this.eLayer.isVisible()) {
    if (this.idTimeoutHide) window.clearTimeout(this.idTimeoutHide);
    this.idTimeoutHide = window.setTimeout(this.hideContextualPanel.bind(this), this.iTimeLeft);
  }

  if (this.LastObject && this.LastObject.idTimeoutShow) {
    window.clearTimeout(this.LastObject.idTimeoutShow);
    if (this.idTimeoutShow == this.LastObject.idTimeoutShow) this.idTimeoutShow = null;
    this.LastObject.idTimeoutShow = null;
    this.LastObject = null;
  }

  this.P = false;
};

CGBehaviour.prototype.hideContextualPanel = function () {
  if (!this.eLayer) return;
  if (this.eLayer.activated) return;
  if (this.idTimeoutShow) window.clearTimeout(this.idTimeoutShow);
  this.idTimeoutShow = null;
  this.idTimeoutHide = null;
  this.eLayer.hide();
  return false;
};

CGBehaviour.prototype.activateContextualPanel = function (Sent) {
  if (!this.eLayer) return;
  if (this.eLayer.activated) return;

  if (this.P) {
    window.setTimeout(this.activateContextualPanel.bind(this, Sent), 5);
    return;
  }

  this.P = true;
  this.eLayer.activated = true;
  if (this.idTimeoutHide) {
    window.clearTimeout(this.idTimeoutHide);
    this.idTimeoutHide = null;
  }
  this.LastObject = null;
  this.P = false;
};

CGBehaviour.prototype.deactivateContextualPanel = function (Sent) {
  if (!this.eLayer) return;
  if (!this.eLayer.activated) return;

  if (this.P) {
    window.setTimeout(this.activateContextualPanel.bind(this, Sent), 5);
    return;
  }

  this.P = true;
  this.eLayer.activated = false;
  this.LastObject = null;
  this.P = false;
  this.hideDelayedContextualPanel();
};
