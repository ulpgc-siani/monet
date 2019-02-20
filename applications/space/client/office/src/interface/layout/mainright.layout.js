CGLayoutMainRight = function () {
  this.Layout = null;
  this.bCollapsed = false;
  this.bClosed = false;
  this.onCollapseList = new Array();
};

CGLayoutMainRight.EVENT = "_event_";

CGLayoutMainRight.prototype.init = function (InnerLayout) {
  InnerLayout.add("east", new Ext.ContentPanel(Literals.Layout.MainRight, {title: Lang.LayoutMainRight.HelperTitle}));

  this.Layout = InnerLayout;

  this.TabPanel = new Ext.TabPanel(Literals.TabPanels.MainRight);
  this.TabPanel.addTab(Literals.TabPanels.MainRightHelper, Lang.LayoutMainRight.HelperTitle, $(Literals.TabPanels.MainRightHelper).innerHTML);
  this.TabPanel.activate(Literals.TabPanels.MainRightHelper);

  var Region = this.Layout.getRegion("east");
  Region.on("collapsed", this.atCollapsed, this);
  Region.on("expanded", this.atExpanded, this);

  ViewerSidebar.init(Ext.get(Literals.Viewers.HelperSideBar));
  var helper = ViewerSidebar.getHelper(Helper.EDITORS);
  helper.onLock = this.atLock.bind(this);
  helper.onUnLock = this.atUnLock.bind(this);

  $(Literals.Layout.MainRight).style.height = "";
};

CGLayoutMainRight.prototype.loadStateFromCookies = function () {
    var value = getCookie(this.collapseCookie());
    if (value != null && value == "true") this.collapse();
    else this.expand();
};

CGLayoutMainRight.prototype.setPanelTitle = function (sName, sTitle) {
  this.TabPanel.getTab(sName).setText(sTitle);
  this.Layout.getRegion("east").getActivePanel().setTitle(sTitle);
};

CGLayoutMainRight.prototype.activateTab = function (Id) {
  this.TabPanel.activate(Id);
  this.unLock();
};

CGLayoutMainRight.prototype.collapse = function () {
  this.Layout.getRegion('east').collapse();
};

CGLayoutMainRight.prototype.isExpanded = function () {
  if (this.bClosed) return false;
  if (this.bCollapsed) return false;
  return true;
};

CGLayoutMainRight.prototype.expand = function () {
  if (this.bCollapsed) return;
  this.Layout.getRegion('east').show();
  this.Layout.getRegion('east').expand();
  this.bClosed = false;
};

CGLayoutMainRight.prototype.close = function () {
  this.Layout.getRegion('east').hide();
  this.bClosed = true;
};

CGLayoutMainRight.prototype.refresh = function () {
};

CGLayoutMainRight.prototype.lock = function () {
  var extLayer = this.TabPanel.bodyEl.up(".x-layout-panel-body");
  var extLockLayer = extLayer.select(CSS_LOCKED).first();
  if (extLockLayer == null) new Insertion.Bottom(extLayer.dom, "<div class='" + CLASS_LOCKED + "'></div>").element.immediateDescendants().last();
};

CGLayoutMainRight.prototype.unLock = function () {
  var extLayer = this.TabPanel.bodyEl.up(".x-layout-panel-body");
  var extLockLayer = extLayer.select(CSS_LOCKED).first();
  if (extLockLayer != null) extLockLayer.remove();
};

CGLayoutMainRight.prototype.registerCollapse = function (key, onCollapse) {
  this.onCollapseList[CGLayoutMainRight.EVENT + key] = onCollapse;
};

CGLayoutMainRight.prototype.unRegisterCollapse = function (key) {
  delete this.onCollapseList[CGLayoutMainRight.EVENT + key];
};

CGLayoutMainRight.prototype.notifyCollapse = function () {
  for (var key in this.onCollapseList) {
    if (key.indexOf(CGLayoutMainRight.EVENT) != -1)
      this.onCollapseList[key]();
  }
};

CGLayoutMainRight.prototype.atCollapsed = function () {
  this.bCollapsed = true;
  setCookie(this.collapseCookie(), "true", 30);
  this.notifyCollapse();
};

CGLayoutMainRight.prototype.atExpanded = function () {
  this.bCollapsed = false;
  setCookie(this.collapseCookie(), "false", 30);
};

CGLayoutMainRight.prototype.atLock = function () {
  this.lock();
};

CGLayoutMainRight.prototype.atUnLock = function () {
  this.unLock();
};

CGLayoutMainRight.prototype.getHeight = function () {
  return this.Layout.getRegion('east').bodyEl.getHeight();
};

CGLayoutMainRight.prototype.collapseCookie = function () {
    return "monet-mainright-collapse-" + Account.Id;
};