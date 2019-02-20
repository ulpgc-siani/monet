ViewDashboard = new Object;
ViewDashboard.sLayerName = null;

//PUBLIC
ViewDashboard.init = function (sLayerName) {
  ViewDashboard.DOMLayer = $(sLayerName);
};

ViewDashboard.getDOM = function () {
  return ViewDashboard.DOMLayer;
};

ViewDashboard.show = function () {
  ViewDashboard.DOMLayer.show();
};

ViewDashboard.hide = function () {
  ViewDashboard.DOMLayer.hide();
};

ViewDashboard.setContent = function (sContent) {
  this.sContent = sContent;
};

ViewDashboard.refresh = function () {
  var Constructor;

  if (ViewDashboard.sContent == null) return;

  ViewDashboard.DOMLayer.innerHTML = ViewDashboard.sContent;
  ViewDashboard.sContent = "";

  Constructor = Extension.getHelperItemConstructor();
  Constructor.init(ViewDashboard.DOMLayer);
  CommandListener.capture(ViewDashboard.DOMLayer);

  EventManager.disableNotifications();
  ViewDashboard.DOMLayer.init();
  EventManager.enableNotifications();
};