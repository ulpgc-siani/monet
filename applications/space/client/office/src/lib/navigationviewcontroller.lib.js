var aNavigationViewControllerTemplates = new Array();
aNavigationViewControllerTemplates["es"] = new Array();
aNavigationViewControllerTemplates["es"]["NVC_TEMPLATE"] = "<table height='100%'><tr class='panels'></tr></table>";
aNavigationViewControllerTemplates["es"]["NVC_PANEL_TEMPLATE"] = "<div class='box' id='#{boxId}'></div>";
aNavigationViewControllerTemplates["es"]["NVC_PANEL_TEMPLATE_ARROW"] = "<img style='display:none;' src='#{imagesPath}/s.gif'/>";

CLASS_NVC = "nvc";
CSS_NCV_PANELS = ".panels";
CSS_NCV_PANEL_BOX = ".box";
ID_NCV_PANEL_ARROW = "panel_arrow_::id::";
CSS_NCV_ARROW = "img";

// IMPORTANT: escape and utf8Encode functions are needed by this library

function CGNavigationViewController(sLayer, ViewsFactory, imagesPath) {
  if (sLayer == null) return;

  this.imagesPath = imagesPath;

  this.extLayer = Ext.get(sLayer);
  this.extLayer.dom.innerHTML = aNavigationViewControllerTemplates["es"]["NVC_TEMPLATE"];
  this.extLayer.addClass(CLASS_NVC);

  this.extPanels = this.extLayer.select(CSS_NCV_PANELS).first();
  this.ViewsFactory = ViewsFactory;
  this.aIdViews = new Array();
  this.aViews = new Array();
};

CGNavigationViewController.prototype.getViewPos = function (View) {
  for (var i = 0; i < this.aIdViews.length; i++) {
    if (this.aIdViews[i] == View.id) return i;
  }
  return -1;
};

CGNavigationViewController.prototype.getViewLabel = function (iPos) {
  if (iPos < 0) return "";
  if (!this.aIdViews[iPos]) return "";
  if (!this.aViews[this.aIdViews[iPos]].getLabel) return "";
  return this.aViews[this.aIdViews[iPos]].getLabel();
};

CGNavigationViewController.prototype.pushView = function (View) {
  var id = Ext.id(), idBox = Ext.id();
  var PanelTemplate = new Template(aNavigationViewControllerTemplates["es"]["NVC_PANEL_TEMPLATE"]);
  var PanelArrowTemplate = new Template(aNavigationViewControllerTemplates["es"]["NVC_PANEL_TEMPLATE_ARROW"]);
  var iHeight = this.extLayer.getHeight();

  var DOMPanel = document.createElement("td");
  DOMPanel.id = id;
  DOMPanel.className = "panel depth" + this.aViews.size();
  DOMPanel.innerHTML = PanelTemplate.evaluate({id: id, boxId: idBox, "imagesPath": this.imagesPath});
  this.extPanels.dom.appendChild(DOMPanel);

  var DOMPanelArrow = document.createElement("td");
  DOMPanelArrow.id = "panel_arrow_" + id;
  DOMPanelArrow.className = "panel arrow";
  DOMPanelArrow.innerHTML = PanelArrowTemplate.evaluate({id: id, boxId: idBox, "imagesPath": this.imagesPath});
  this.extPanels.dom.appendChild(DOMPanelArrow);

  var extPanel = Ext.get(id);
  var extPanelBox = extPanel.down(CSS_NCV_PANEL_BOX);

  if (iHeight > 0) extPanelBox.dom.style.height = (iHeight - 40) + "px";
  Ext.get(id).scrollIntoView(this.extLayer);

  View.id = id;
  View.onShowItem = CGNavigationViewController.prototype.atShowItem.bind(this, View, extPanel.dom);
  View.render(extPanelBox.id);

  this.aIdViews.push(View.id);
  this.aViews[View.id] = View;

  if (this.onPushView) this.onPushView(View);
};

CGNavigationViewController.prototype.pushViewForObject = function (Object) {
  if (this.onBeforePushView && !this.onBeforePushView(Object)) return;
  var NewView = this.ViewsFactory.get(Object);
  this.pushView(NewView);
};

CGNavigationViewController.prototype.popView = function (View) {
  var sIdViews = this.aIdViews.join(",");
  var sNewIdViews = "", sViewsToPop = "";
  var iPos = sIdViews.indexOf(View.id);
  var aViewsToPop;

  if (iPos != -1) {
    if (iPos > 0) iPos = iPos - 1;
    sNewIdViews = sIdViews.substring(0, iPos);
    sViewsToPop = sIdViews.substring(iPos != 0 ? iPos + 1 : iPos);
  }

  this.aIdViews = (sNewIdViews.length > 0) ? sNewIdViews.split(",") : new Array();
  aViewsToPop = (sViewsToPop.length > 0) ? sViewsToPop.split(",") : new Array();

  for (var i = 0; i < aViewsToPop.length; i++) {
    var idView = aViewsToPop[i];
    var DOMPanel = $(idView);
    var DOMPanelArrow = $(ID_NCV_PANEL_ARROW.replace("::id::", idView));

    this.aViews[idView].dispose();
    DOMPanel.remove();
    DOMPanelArrow.remove();

    delete this.aViews[idView];
  }

};

CGNavigationViewController.prototype.getFirstView = function () {
  if (this.aIdViews.length <= 0) return null;
  return this.aViews[this.aIdViews[0]];
};

CGNavigationViewController.prototype.getCurrentView = function () {
  var iPos = this.aIdViews.length - 1;
  if (iPos < 0) return null;
  return this.aViews[this.aIdViews[iPos]];
};

CGNavigationViewController.prototype.getParentViewOfCurrentView = function () {
  var iPos = this.aIdViews.length - 2;
  if (iPos < 0) return null;
  return this.aViews[this.aIdViews[iPos]];
};

CGNavigationViewController.prototype.dispose = function () {
  var View = this.aViews[this.aIdViews[0]];
  if (View != null) this.popView(View);
};

CGNavigationViewController.prototype.getCountViews = function () {
  return this.aIdViews.length;
};

//#############################################################################################################

CGNavigationViewController.prototype.atShowItem = function (View, DOMPanel, Sender, Object) {
  var iViewPos = this.getViewPos(View);
  var CurrentView = this.aViews[this.aIdViews[iViewPos]];
  var ViewToPop = this.aViews[this.aIdViews[iViewPos + 1]];
  var IdArrow = ID_NCV_PANEL_ARROW.replace("::id::", DOMPanel.id);
  var extArrow = Ext.get(IdArrow).down(CSS_NCV_ARROW);

  extArrow.dom.style.display = "none";

  if (ViewToPop) this.popView(ViewToPop);

  if (this.onBeforePushView && !this.onBeforePushView(Object)) return;

  if (CurrentView.getActiveItem) {
    var ActiveItem = CurrentView.getActiveItem();
    if (ActiveItem && CurrentView.getDOMItem) {
      var extPanel = Ext.get(DOMPanel);
      var extItem = Ext.get(CurrentView.getDOMItem(ActiveItem.id));
      extArrow.dom.style.display = "block";
      extArrow.setTop(extItem.getTop() - extPanel.getTop() - 4);
    }
  }

  var NewView = this.ViewsFactory.get(Object);
  this.pushView(NewView);
};