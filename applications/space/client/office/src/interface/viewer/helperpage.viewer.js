ViewerHelperPage = new Object;

ViewerHelperPage.extLayer = null;
ViewerHelperPage.Page = null;

ViewerHelperPage.init = function (extLayer) {
  ViewerHelperPage.extLayer = extLayer;
  ViewerHelperPage.extLayer.dom.innerHTML = translate(AppTemplate.ViewerHelperPage, Lang.ViewerHelperPage);
};

ViewerHelperPage.getTarget = function () {
  return ViewerHelperPage.Page;
};

ViewerHelperPage.setTarget = function (Page) {
  ViewerHelperPage.Page = Page;
};

ViewerHelperPage.show = function () {
  ViewerHelperPage.extLayer.dom.style.display = "block";
};

ViewerHelperPage.hide = function () {
  ViewerHelperPage.extLayer.dom.style.display = "none";
};

ViewerHelperPage.refresh = function () {
  if (ViewerHelperPage.Page == null) return;
  var extPage = ViewerHelperPage.extLayer.select(".page").first();
  extPage.dom.innerHTML = ViewerHelperPage.Page.getContent();
  CommandListener.capture(extPage.dom);
};