CGViewTask = function () {
  this.base = CGView;
  this.base();
  this.Type = VIEW_TASK;
  this.lastTab = null;
};

CGViewTask.ViewOrders = "orders";

CGViewTask.prototype = new CGView;

CGViewTask.prototype.refreshTask = function () {

  EventManager.disableNotifications();

  this.DOMLayer.init();
  this.DOMLayer.onTabFocus = CGViewTask.prototype.atTaskTabFocus.bind(this);

  EventManager.enableNotifications();
};

CGViewTask.prototype.refresh = function () {
  var extLayer, IdDOMLayer;
  var Styles;
  var sContent = this.Target.getContent();

  if (!this.Target) return;

  if (sContent != null && sContent != "") {
    this.Target.setContent("");

    extLayer = Ext.get(this.DOMLayer);
    IdDOMLayer = (this.DOMLayer.id) ? this.DOMLayer.id : Ext.id();
    Styles = extLayer.getStyles("position", "visibility", "left", "top");

    sContent = setIdToElementContent(IdDOMLayer, sContent);
    extLayer.dom = replaceDOMElement(extLayer.dom, sContent);
    this.setDOMLayer($(IdDOMLayer));

    extLayer = Ext.get(this.DOMLayer);
    extLayer.applyStyles(Styles);
    extLayer.dom.IdView = this.Id;
  }

  if (this.Mode != null) {
    Constructor = Extension.getTaskConstructor();
    Constructor.init(this.DOMLayer);
    CommandListener.capture(this.DOMLayer);
    BehaviourDispatcher.apply(BehaviourViewTask, this.DOMLayer);
  }

  this.refreshTask();
};

CGViewTask.prototype.atTaskTabFocus = function (DOMTask, DOMTab, DOMView) {
  ViewerSidebar.hide();
  var DOMTaskTabList = Ext.get(DOMTab).select(".task");
  if (DOMTaskTabList.elements.length > 0) {
    var DOMTaskTab = DOMTaskTabList.elements[0];
    DOMTaskTab.style.display = "block";
    DOMTaskTab.style.height = "calc(100% - 63px)";
    this.resizeNodes();
  }
  if (DOMTask != null) EventManager.notify(EventManager.FOCUS_TASK_VIEW, {"Task": this.Target, "DOMTask": DOMTask});
};

CGViewTask.prototype.resizeNodes = function() {
  var nodes = Ext.get(this.DOMLayer).select(".node").elements;
  for (var i=0; i<nodes.length; i++) {
    nodes[i].style.height = "100%";
    nodes[i].style.display = "block";
  }
};