ViewerHelperMap = new Object;
ViewerHelperMap.extLayer = null;

ViewerHelperMap.init = function (extLayer) {
  ViewerHelperMap.extLayer = extLayer;

  var html = AppTemplate.ViewerHelperMap;
  html = translate(html, Lang.ViewerHelperMap);

  ViewerHelperMap.extLayer.dom.innerHTML = html;
};

ViewerHelperMap.getEditionToolbarLayer = function () {
  return ViewerHelperMap.extLayer.select(".edition .toolbar").first().dom;
};

ViewerHelperMap.getInfoLayer = function () {
  return ViewerHelperMap.extLayer.select(".edition .info").first().dom;
};

ViewerHelperMap.getNavigationToolbarLayer = function () {
  return ViewerHelperMap.extLayer.select(".navigation .toolbar").first().dom;
};

ViewerHelperMap.getSearchLayer = function () {
  return ViewerHelperMap.extLayer.select(".navigation .search").first().dom;
};

ViewerHelperMap.showEditionLayer = function () {
  var extEditionLayer = ViewerHelperMap.extLayer.select(".edition").first();
  extEditionLayer.dom.style.display = "block";
};

ViewerHelperMap.hideEditionLayer = function () {
  var extEditionLayer = ViewerHelperMap.extLayer.select(".edition").first();
  extEditionLayer.dom.style.display = "none";
};

ViewerHelperMap.show = function () {
  ViewerHelperMap.extLayer.dom.style.display = "block";
};

ViewerHelperMap.hide = function () {
  ViewerHelperMap.extLayer.dom.style.display = "none";
};

ViewerHelperMap.refresh = function () {
};