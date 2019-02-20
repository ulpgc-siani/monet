ViewerHelperObservers = new Object;
ViewerHelperObservers.extLayer = null;

ViewerHelperObservers.init = function (extLayer) {
  ViewerHelperObservers.extLayer = extLayer;
  ViewerHelperObservers.extLayer.dom.innerHTML = translate(AppTemplate.ViewerHelperObservers, Lang.ViewerHelperObservers);
};

ViewerHelperObservers.getTarget = function () {
  return ViewerHelperObservers.Target;
};

ViewerHelperObservers.setTarget = function (Target) {
  ViewerHelperObservers.Target = Target;
};

ViewerHelperObservers.show = function () {
  var doShow = true;
  if (this.onBeforeShow) doShow = this.onBeforeShow();
  if (doShow) ViewerHelperObservers.extLayer.dom.style.display = "block";
};

ViewerHelperObservers.hide = function () {
  ViewerHelperObservers.extLayer.dom.style.display = "none";
};

ViewerHelperObservers.refresh = function () {
  var extObservers = ViewerHelperObservers.extLayer.select(".observers").first();
  var aObservers = ViewerHelperObservers.Target;
  var sObservers = "";
  var itemTemplate = new Template(translate(AppTemplate.ViewerHelperObserversItem, Lang.ViewerHelperObservers));

  for (var i = 0; i < aObservers.length; i++)
    sObservers += itemTemplate.evaluate({color: getColor(i), fullname: aObservers[i].fullname, field: (aObservers[i].field != null && aObservers[i].field != "") ? "&nbsp;(" + Lang.ViewerHelperObservers.Editing + ")" : ""});

  extObservers.dom.innerHTML = sObservers;
};