ViewNotificationList = new Object;

ViewNotificationList.sLayerName = null;

//PUBLIC
ViewNotificationList.init = function (sLayerName) {
  ViewNotificationList.DOMLayer = $(sLayerName);
};

ViewNotificationList.getDOM = function () {
  return ViewNotificationList.DOMLayer;
};

ViewNotificationList.show = function () {
  ViewNotificationList.DOMLayer.show();
};

ViewNotificationList.hide = function () {
  ViewNotificationList.DOMLayer.hide();
};

ViewNotificationList.setContent = function (sContent) {
  this.sContent = sContent;
};

ViewNotificationList.refresh = function () {
  var Constructor;

  if (ViewNotificationList.sContent == null) return;

  ViewNotificationList.DOMLayer.innerHTML = ViewNotificationList.sContent;
  ViewNotificationList.sContent = "";

  Constructor = Extension.getHelperItemConstructor();
  Constructor.init(ViewNotificationList.DOMLayer);
  CommandListener.capture(ViewNotificationList.DOMLayer);

  EventManager.disableNotifications();
  ViewNotificationList.DOMLayer.init();
  EventManager.enableNotifications();
};