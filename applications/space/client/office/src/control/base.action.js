function CGActionShowBase(iNumStates) {
  this.base = CGAction;
  this.base(iNumStates);
  this.bActivate = false;
  this.Mode = "default";
};

CGActionShowBase.prototype = new CGAction;
CGActionShowBase.constructor = CGActionShowBase;
CommandFactory.register(CGActionShowBase, { Id: 0 }, true);

CGActionShowBase.prototype.onFailure = function (sResponse) {
  Desktop.reportError(sResponse);
  this.terminate();
};

CGActionShowBase.prototype.getDOMElement = function (Type, Object) {
  return null;
};

CGActionShowBase.prototype.getContainerView = function (Type, Object) {
  var View = Desktop.Main.Center.Body.getContainerView(Type, Object.getId());
  var IdTab = null;

  if (View != null && !View.NodeDependant) {
    Desktop.Main.Center.Body.deleteView(Type, View.getId());
    IdTab = Desktop.Main.Center.Body.getTabId(Type, Object.getId());
  }
  else {
    IdTab = Desktop.Main.Center.Body.addTab(Type, {Id: Object.getId()});
    this.bActivate = true;
  }

  View = Desktop.createView($(IdTab), Object, null, this.Mode, true);

  return View;
};

CGActionShowBase.prototype.getView = function (Type, Object, DOMElement) {
  var View, ViewContainer, Mode;

  if (this.DOMItem != null) DOMElement = this.getDOMElement();

  if ((!DOMElement) || (!DOMElement.IdView)) return this.getContainerView(Type, Object);

  if (!Desktop.Main.Center.Body.existsView(DOMElement.IdView)) {
    this.terminate();
    return;
  }

  View = Desktop.Main.Center.Body.getView(Type, DOMElement.IdView);
  ViewContainer = View.getContainer();
  if ((ViewContainer == null) || (ViewContainer.getTarget().getId() != this.Id)) return this.getContainerView(Type, Object);

  Mode = View.getMode();
  Type = View.getType();
  Desktop.Main.Center.Body.deleteView(Type, View.getId());
  View = Desktop.createView(DOMElement, Object, ViewContainer, this.Mode, true);
  View.setMode(Mode);
  View.setType(Type);

  return View;
};

CGActionShowBase.prototype.createView = function (Type, Object, DOMElement, ContainerView) {
  var View = Desktop.createView(DOMElement, Object, ContainerView, this.Mode, false);
  View.setMode(this.Mode);
  return View;
};