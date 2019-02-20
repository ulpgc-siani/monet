ViewSource = new Object;
ViewSource.sLayerName = null;

//PUBLIC
ViewSource.init = function (sLayerName) {
  ViewSource.DOMLayer = $(sLayerName);
};

ViewSource.getDOM = function () {
  return ViewSource.DOMLayer;
};

ViewSource.show = function () {
  ViewSource.DOMLayer.show();
};

ViewSource.hide = function () {
  ViewSource.DOMLayer.hide();
};

ViewSource.setContent = function (sContent) {
  this.sContent = sContent;
};

ViewSource.refresh = function () {
  var Constructor;

  if (ViewSource.sContent == null) return;

  ViewSource.DOMLayer.innerHTML = ViewSource.sContent;
  ViewSource.sContent = "";

  Constructor = Extension.getHelperItemConstructor();
  Constructor.init(ViewSource.DOMLayer);
  CommandListener.capture(ViewSource.DOMLayer);

  EventManager.disableNotifications();
  ViewSource.DOMLayer.init();
  EventManager.enableNotifications();
};