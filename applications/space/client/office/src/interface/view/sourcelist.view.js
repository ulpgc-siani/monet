ViewSourceList = new Object;
ViewSourceList.sLayerName = null;

ViewSourceList.init = function (sLayerName) {
  ViewSourceList.DOMLayer = $(sLayerName);
};

ViewSourceList.getDOM = function () {
  return ViewSourceList.DOMLayer;
};

ViewSourceList.show = function () {
  ViewSourceList.DOMLayer.show();
};

ViewSourceList.hide = function () {
  ViewSourceList.DOMLayer.hide();
};

ViewSourceList.setContent = function (sContent) {
  this.sContent = sContent;
};

ViewSourceList.refresh = function () {
  var Constructor;

  if (ViewSourceList.sContent == null) return;

  ViewSourceList.DOMLayer.innerHTML = ViewSourceList.sContent;
  ViewSourceList.sContent = "";

  Constructor = Extension.getHelperItemConstructor();
  Constructor.init(ViewSourceList.DOMLayer);
  CommandListener.capture(ViewSourceList.DOMLayer);

  EventManager.disableNotifications();
  ViewSourceList.DOMLayer.init();
  EventManager.enableNotifications();
};