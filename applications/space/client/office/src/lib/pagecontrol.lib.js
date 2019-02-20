PAGE_CONTROL_MAX_PAGES = 10;

function CGPageControl(Layout, sRegion, iMaxPages) {
  this.Layout = Layout;
  this.sRegion = sRegion;
  this.aPages = new Array();
  this.aPagesActivations = new Array();
  this.aPagesLocked = new Array();
  this.iMaxPages = (iMaxPages != null) ? iMaxPages : PAGE_CONTROL_MAX_PAGES;
  this.Layout.getRegion(this.sRegion).on("panelactivated", this.atPageActivated, this);
};

//---------------------------------------------------------------------
CGPageControl.prototype.existsPage = function (Id) {
  return (this.aPages[Id]) ? true : false;
};

//---------------------------------------------------------------------
CGPageControl.prototype.syncHeight = function (Page) {
  var extElement = Ext.get(Page.el.dom);
  extElement = extElement.up(".x-tabs-body.x-layout-tabs-body");
  if (extElement) {
    extElement.dom.style.height = (this.Layout.getRegion(this.sRegion).el.getHeight()) + "px";
    extElement.dom.style.overflow = "hidden";
  }
};

//---------------------------------------------------------------------
CGPageControl.prototype.addPage = function (Config) {
  var Page, Background;

  if (this.aPages[Config.Id]) return false;

  bCloseable = (this.aPages.length != 0);
  Background = (Config.Background) ? Config.Background : false;

  if (this.aPages.size() > this.iMaxPages) {
    this.deleteLesserActivatedPage();
  }

  Page = new Ext.ContentPanel(Config.Id, {autoCreate: true, closable: bCloseable, background: Background});
  Page.Id = Config.Id;
  this.Layout.add(this.sRegion, Page);
  this.aPages[Config.Id] = Config.Id;
  this.aPagesActivations[Page.Id] = 1;
  if (Config.Locked) this.aPagesLocked[Config.Id] = Config.Id;

  this.syncHeight(Page);

  if (!Background) this.showPage(Config.Id);

  return Page;
};

//---------------------------------------------------------------------
CGPageControl.prototype.deleteLesserActivatedPage = function () {
  var IdPage = null;
  var iMinActivations = 1000000;
  for (var IdCurrentPage in this.aPagesActivations) {
    if (isFunction(this.aPagesActivations[IdCurrentPage])) continue;
    if ((this.aPagesActivations[IdCurrentPage] < iMinActivations) && (this.aPagesLocked[IdCurrentPage] == null)) {
      IdPage = IdCurrentPage;
      iMinActivations = this.aPagesActivations[IdCurrentPage];
    }
  }
  if (IdPage != null) this.deletePage(IdPage);
};

//---------------------------------------------------------------------
CGPageControl.prototype.deletePage = function (Id) {
  if (!this.aPages[Id]) return false;

  if (this.aPages[Id]) delete this.aPages[Id];
  if (this.aPagesActivations[Id]) delete this.aPagesActivations[Id];
  if (this.aPagesLocked[Id]) delete this.aPagesLocked[Id];

  this.Layout.getRegion(this.sRegion).hidePanel(Id);
  this.Layout.getRegion(this.sRegion).remove(Id);
};

//---------------------------------------------------------------------
CGPageControl.prototype.setPageTitle = function (Id, sTitle) {
  if (!this.aPages[Id]) return false;
  this.Layout.getRegion(this.sRegion).getPanel(Id).setTitle(sTitle);
};

//---------------------------------------------------------------------
CGPageControl.prototype.getActivePage = function () {
  return this.Layout.getRegion(this.sRegion).getActivePanel();
};

//---------------------------------------------------------------------
CGPageControl.prototype.hidePages = function (extLayout) {
  var extBody = extLayout.el.down(".x-layout-panel-body .x-layout-tabs-body");
  if (extBody == null) return;

  var extPanels = extBody.select("> div");
  extPanels.each(function (extPanel) {
    extPanel.dom.style.display = "none";
  });
};

//---------------------------------------------------------------------
CGPageControl.prototype.showPage = function (Id) {
  var extPanel, extElement, extLayout;

  if (!this.aPages[Id]) return false;

  extLayout = this.Layout.getRegion(this.sRegion);
  this.hidePages(extLayout);

  extLayout.showPanel(Id);
  extPanel = extLayout.getPanel(Id);
  extPanel.el.dom.style.display = "block";
  extElement = Ext.get(extPanel.el.dom);
  extElement = extElement.up(".x-layout-panel-body.x-tabs-top");
  if (extElement) extElement.dom.style.position = "relative";
};

//---------------------------------------------------------------------
CGPageControl.prototype.hidePage = function (Id) {
  if (!this.aPages[Id]) return false;
  this.Layout.getRegion(this.sRegion).hidePanel(Id);
};

//---------------------------------------------------------------------
CGPageControl.prototype.atPageActivated = function () {
  var Page = this.getActivePage();
  this.aPagesActivations[Page.Id] = this.aPagesActivations[Page.Id] + 1;
};
