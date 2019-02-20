ViewPageNews = new Object;
ViewPageNews.sLayerName = null;

//PUBLIC
ViewPageNews.init = function (sLayerName) {
  ViewPageNews.DOMLayer = $(sLayerName);
};

ViewPageNews.getDOM = function () {
  return ViewPageNews.DOMLayer;
};

ViewPageNews.show = function () {
  ViewPageNews.DOMLayer.show();
};

ViewPageNews.hide = function () {
  ViewPageNews.DOMLayer.hide();
};

ViewPageNews.setContent = function (sContent) {
  this.sContent = sContent;
};

ViewPageNews.refresh = function () {
  var Constructor;

  if (ViewPageNews.sContent == null) return;

  ViewPageNews.DOMLayer.innerHTML = ViewPageNews.sContent;
  ViewPageNews.sContent = "";

  Constructor = Extension.getHelperItemConstructor();
  Constructor.init(ViewPageNews.DOMLayer);
  CommandListener.capture(ViewPageNews.DOMLayer);

  EventManager.disableNotifications();
  ViewPageNews.DOMLayer.init();
  EventManager.enableNotifications();
};