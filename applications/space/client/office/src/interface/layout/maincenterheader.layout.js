CGLayoutMainCenterHeader = function () {
  this.InnerLayout = null;
  this.Toolbar = null;
  this.DialogSearchNodes = new CGDialogSearchNodes();
};

CGLayoutMainCenterHeader.prototype.init = function (InnerLayout) {
  this.InnerLayout = InnerLayout;
  //this.InnerLayout.add("north", new Ext.ContentPanel(Literals.Layout.MainCenterHeader, {title : Lang.LayoutMainCenterHeader.Title}));
  //this.initDialogSearchNodes();
  this.hideBanner();
  //this.initToolbar();
};

CGLayoutMainCenterHeader.prototype.initDialogSearchNodes = function () {
  createLayer(Literals.Dialogs.SearchNodes, EMPTY, $(Literals.Layout.MainCenterHeader));
  this.DialogSearchNodes.init(Literals.Dialogs.SearchNodes);
  this.DialogSearchNodes.refresh();
};

CGLayoutMainCenterHeader.prototype.initToolbar = function () {

  this.Toolbar = new Ext.Toolbar(Literals.Toolbars.Main);

  this.ButtonHome = new Ext.Toolbar.Button({name: 'showhome()', disabled: true, cls: 'x-btn-text', text: ToolbarDefinition.Main.cmdShowHome.caption, handler: this.atItemClick.createDelegate(this)/*, tooltip: ToolbarDefinition.Main.cmdShowHome.hint*/});

  this.Toolbar.add(this.ButtonHome);
  this.Toolbar.addSeparator();
  this.Toolbar.add($(Literals.Dialogs.SearchNodes));
};

CGLayoutMainCenterHeader.prototype.refreshToolbar = function () {
  var ActionShowHome = new CGActionShowHome();

  if (ActionShowHome.enabled()) this.ButtonHome.enable();
  else this.ButtonHome.disable();

  CommandListener.capture($(Literals.Toolbars.Main));
};

CGLayoutMainCenterHeader.prototype.atItemClick = function (Item, EventLaunched) {
  CommandListener.dispatchCommand(Item.name);
  if (EventLaunched) Event.stop(EventLaunched);
};

CGLayoutMainCenterHeader.prototype.reportBanner = function (sMessage) {
  var extReportBanner = Ext.get(Literals.ReportBanner);
  if (extReportBanner == null) extReportBanner = Ext.get(new Insertion.Bottom(this.InnerLayout.getRegion("north").el.dom, "<div class='reportbanner' id='" + Literals.ReportBanner + "'></div>").element.immediateDescendants().last());
  extReportBanner.dom.innerHTML = sMessage;
  extReportBanner.dom.style.display = "block";
  CommandListener.capture(extReportBanner.dom);
  this.InnerLayout.getRegion("north").resizeTo(Widths.Layout.MainCenterHeader + extReportBanner.getHeight() + 2);
};

CGLayoutMainCenterHeader.prototype.showBanner = function () {
  var extReportBanner = Ext.get(Literals.ReportBanner);
  if (extReportBanner != null) {
    extReportBanner.dom.style.display = "block";
    this.InnerLayout.getRegion("north").resizeTo(Widths.Layout.MainCenterHeader + extReportBanner.getHeight() + 2);
  }
};

CGLayoutMainCenterHeader.prototype.hideBanner = function () {
  var extReportBanner = Ext.get(Literals.ReportBanner);
  if (extReportBanner != null) extReportBanner.dom.style.display = "none";
  this.InnerLayout.getRegion("north").resizeTo(Widths.Layout.MainCenterHeader);
};

CGLayoutMainCenterHeader.prototype.refresh = function () {
  //this.refreshToolbar();
  this.DialogSearchNodes.refresh();
};