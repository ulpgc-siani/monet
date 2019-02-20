ViewerHelperList = new Object;
ViewerHelperList.extLayer = null;

ViewerHelperList.init = function (extLayer) {
  ViewerHelperList.extLayer = extLayer;
  ViewerHelperList.extLayer.dom.innerHTML = translate(AppTemplate.ViewerHelperList, Lang.ViewerHelperList);
};

ViewerHelperList.show = function () {
  ViewerHelperList.extLayer.dom.style.display = "block";
};

ViewerHelperList.hide = function () {
  ViewerHelperList.extLayer.dom.style.display = "none";
};

ViewerHelperList.refresh = function () {
};