ViewTrash = new Object;
ViewTrash.sLayerName = null;

//PUBLIC
ViewTrash.init = function (sLayerName) {
  ViewTrash.DOMLayer = $(sLayerName);
};

ViewTrash.getDOM = function () {
  return ViewTrash.DOMLayer;
};

ViewTrash.show = function () {
  ViewTrash.DOMLayer.show();
};

ViewTrash.hide = function () {
  ViewTrash.DOMLayer.hide();
};

ViewTrash.setContent = function (sContent) {
  this.sContent = sContent;
};

ViewTrash.refresh = function () {
  var Constructor;

  if (ViewTrash.sContent == null) return;

  ViewTrash.DOMLayer.innerHTML = ViewTrash.sContent;
  ViewTrash.sContent = "";

  Constructor = Extension.getHelperItemConstructor();
  Constructor.init(ViewTrash.DOMLayer);
  CommandListener.capture(ViewTrash.DOMLayer);

  EventManager.disableNotifications();
  ViewTrash.DOMLayer.init();
  EventManager.enableNotifications();
};