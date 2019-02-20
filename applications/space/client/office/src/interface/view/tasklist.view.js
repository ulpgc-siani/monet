ViewTaskList = new Object;
ViewTaskList.sLayerName = null;

ViewTaskList.init = function (sLayerName) {
  ViewTaskList.DOMLayer = $(sLayerName);
};

ViewTaskList.getDOM = function () {
  return ViewTaskList.DOMLayer;
};

ViewTaskList.show = function () {
  ViewTaskList.DOMLayer.show();
};

ViewTaskList.hide = function () {
  ViewTaskList.DOMLayer.hide();
};

ViewTaskList.setContent = function (sContent) {
  this.sContent = sContent;
};

ViewTaskList.refresh = function () {
  var Constructor;

  if (ViewTaskList.sContent == null) return;

  var extLayer = Ext.get(ViewTaskList.DOMLayer);
  var IdDOMLayer = (ViewTaskList.DOMLayer.id) ? ViewTaskList.DOMLayer.id : Ext.id();

  var sContent = ViewTaskList.sContent;
  sContent = setIdToElementContent(IdDOMLayer, sContent);

  extLayer.dom = replaceDOMElement(extLayer.dom, sContent);
  var DOMLayer = $(IdDOMLayer);

  if (DOMLayer == null) return;
  ViewTaskList.DOMLayer = DOMLayer;

  extLayer = Ext.get(ViewTaskList.DOMLayer);
  ViewTaskList.sContent = "";

  Constructor = Extension.getHelperItemConstructor();
  Constructor.init(ViewTaskList.DOMLayer);
  CommandListener.capture(ViewTaskList.DOMLayer);

  EventManager.disableNotifications();
  ViewTaskList.DOMLayer.init();
  EventManager.enableNotifications();
};