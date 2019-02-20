ViewerHelperRevisionList = new Object;
ViewerHelperRevisionList.extLayer = null;

ViewerHelperRevisionList.init = function (extLayer) {
  ViewerHelperRevisionList.extLayer = extLayer;
  ViewerHelperRevisionList.extLayer.dom.innerHTML = translate(AppTemplate.ViewerHelperRevisionList, Lang.ViewerHelperRevisionList);
};

ViewerHelperRevisionList.getListViewerLayer = function () {
  return $("RevisionListViewer");
};

ViewerHelperRevisionList.show = function () {
  ViewerHelperRevisionList.extLayer.dom.style.display = "block";
};

ViewerHelperRevisionList.hide = function () {
  ViewerHelperRevisionList.extLayer.dom.style.display = "none";
};

ViewerHelperRevisionList.refresh = function () {
};