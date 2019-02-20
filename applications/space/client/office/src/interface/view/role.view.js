ViewRole = new Object;
ViewRole.sLayerName = null;

//PUBLIC
ViewRole.init = function (sLayerName) {
  ViewRole.DOMLayer = $(sLayerName);
};

ViewRole.getDOM = function () {
  return ViewRole.DOMLayer;
};

ViewRole.show = function () {
  ViewRole.DOMLayer.show();
};

ViewRole.hide = function () {
  ViewRole.DOMLayer.hide();
};

ViewRole.setContent = function (sContent) {
  this.sContent = sContent;
};

ViewRole.refresh = function () {
  var Constructor;

  if (ViewRole.sContent == null) return;

  ViewRole.DOMLayer.innerHTML = ViewRole.sContent;
  ViewRole.sContent = "";

  Constructor = Extension.getHelperItemConstructor();
  Constructor.init(ViewRole.DOMLayer);
  CommandListener.capture(ViewRole.DOMLayer);

  EventManager.disableNotifications();
  ViewRole.DOMLayer.init();
  EventManager.enableNotifications();
};