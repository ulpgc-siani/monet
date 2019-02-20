ViewerHelperPreview = new Object;

ViewerHelperPreview.init = function (extLayer) {
  ViewerHelperPreview.extLayer = extLayer;
  ViewerHelperPreview.extLayer.dom.innerHTML = translate(AppTemplate.ViewerHelperPreview, Lang.ViewerHelperPreview);
};

ViewerHelperPreview.show = function () {
  ViewerHelperPreview.extLayer.dom.style.display = "block";
};

ViewerHelperPreview.hide = function () {
  ViewerHelperPreview.extLayer.dom.style.display = "none";
};

ViewerHelperPreview.refresh = function () {
};