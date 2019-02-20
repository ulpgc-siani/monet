ViewAnalysisboard = new Object;
ViewAnalysisboard.sLayerName = null;

//PUBLIC
ViewAnalysisboard.init = function (sLayerName) {
  ViewAnalysisboard.DOMLayer = $(sLayerName);
};

ViewAnalysisboard.getDOM = function () {
  return ViewAnalysisboard.DOMLayer;
};

ViewAnalysisboard.show = function () {
  ViewAnalysisboard.DOMLayer.show();
};

ViewAnalysisboard.hide = function () {
  ViewAnalysisboard.DOMLayer.hide();
};

ViewAnalysisboard.setContent = function (sContent) {
  this.sContent = sContent;
};

ViewAnalysisboard.refresh = function () {
  var Constructor;

  if (ViewAnalysisboard.sContent == null) return;

  ViewAnalysisboard.DOMLayer.innerHTML = ViewAnalysisboard.sContent;
  ViewAnalysisboard.sContent = "";

  Constructor = Extension.getHelperItemConstructor();
  Constructor.init(ViewAnalysisboard.DOMLayer);
  CommandListener.capture(ViewAnalysisboard.DOMLayer);

  EventManager.disableNotifications();
  ViewAnalysisboard.DOMLayer.init();
  EventManager.enableNotifications();
};