ViewRoleList = new Object;
ViewRoleList.sLayerName = null;

ViewRoleList.init = function (sLayerName) {
  ViewRoleList.DOMLayer = $(sLayerName);
};

ViewRoleList.getDOM = function () {
  return ViewRoleList.DOMLayer;
};

ViewRoleList.show = function () {
  ViewRoleList.DOMLayer.show();
};

ViewRoleList.hide = function () {
  ViewRoleList.DOMLayer.hide();
};

ViewRoleList.setContent = function (sContent) {
  this.sContent = sContent;
};

ViewRoleList.refresh = function () {
  var Constructor;

  if (ViewRoleList.sContent == null) return;

  ViewRoleList.DOMLayer.innerHTML = ViewRoleList.sContent;
  ViewRoleList.sContent = "";

  Constructor = Extension.getHelperItemConstructor();
  Constructor.init(ViewRoleList.DOMLayer);
  CommandListener.capture(ViewRoleList.DOMLayer);

  EventManager.disableNotifications();
  ViewRoleList.DOMLayer.init();
  EventManager.enableNotifications();
};