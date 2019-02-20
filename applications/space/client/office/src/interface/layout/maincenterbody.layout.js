NODE_TAB_PREFIX = "nodetab_";
TASK_TAB_PREFIX = "tasktab_";
SOURCE_TAB_PREFIX = "sourcetab_";
TEAM_TAB_PREFIX = "teamtab_";

VIEW_NODE = "vn";
VIEW_TASK = "vt";
VIEW_SOURCE = "vth";
VIEW_TEAM = "vte";

CGLayoutMainCenterBody = function () {
  this.aPanels = null;
  this.aSystemViews = new Array();

  this.aViews = new Array();
  this.aViewsOfContainer = new Array();

  this.aContainerViews = new Array();
  this.aContainerViews[VIEW_NODE] = new Array();
  this.aContainerViews[VIEW_TASK] = new Array();
  this.aContainerViews[VIEW_SOURCE] = new Array();
  this.aContainerViews[VIEW_TEAM] = new Array();

  this.bNotify = true;
  this.scrollListeners = new Object();
  this.scrollViewports = new Object();
};

CGLayoutMainCenterBody.prototype.init = function (InnerLayout) {
  this.InnerLayout = InnerLayout;

  this.PageControl = new CGPageControl(InnerLayout, 'center');

  this.InnerLayout.getRegion('center').on("beforeremove", this.atRemovePanel, this);
  this.InnerLayout.getRegion('center').on("panelactivated", this.atPanelActivated, this);

  this.ViewNotificationList = this.PageControl.addPage({Id: Literals.Views.NotificationList, Locked: true});
  this.ViewTaskList = this.PageControl.addPage({Id: Literals.Views.TaskList, Locked: true});
  this.ViewTrash = this.PageControl.addPage({Id: Literals.Views.Trash, Locked: true});
  this.ViewDashboard = this.PageControl.addPage({Id: Literals.Views.Dashboard, Locked: true});
  this.ViewAnalysisboard = this.PageControl.addPage({Id: Literals.Views.Analysisboard, Locked: true});
  this.ViewSourceList = this.PageControl.addPage({Id: Literals.Views.SourceList, Locked: true});
  this.ViewSource = this.PageControl.addPage({Id: Literals.Views.Source, Locked: true});
  this.ViewRoleList = this.PageControl.addPage({Id: Literals.Views.RoleList, Locked: true});
  this.ViewRole = this.PageControl.addPage({Id: Literals.Views.Role, Locked: true});
  this.ViewPageNews = this.PageControl.addPage({Id: Literals.Views.PageNews, Locked: true});

  ViewNotificationList.init(Literals.Views.NotificationList);
  ViewTaskList.init(Literals.Views.TaskList);
  ViewTrash.init(Literals.Views.Trash);
  ViewDashboard.init(Literals.Views.Dashboard);
  ViewAnalysisboard.init(Literals.Views.Analysisboard);
  ViewSourceList.init(Literals.Views.SourceList);
  ViewSource.init(Literals.Views.Source);
  ViewRoleList.init(Literals.Views.RoleList);
  ViewRole.init(Literals.Views.Role);
  ViewPageNews.init(Literals.Views.PageNews);
};

CGLayoutMainCenterBody.prototype.activateNotificationList = function () {
  this.PageControl.showPage(Literals.Views.NotificationList);
};

CGLayoutMainCenterBody.prototype.activateTeam = function () {
  this.PageControl.showPage(Literals.Views.Team);
};

CGLayoutMainCenterBody.prototype.activateSourceList = function () {
  this.PageControl.showPage(Literals.Views.SourceList);
};

CGLayoutMainCenterBody.prototype.activateSource = function () {
  this.PageControl.showPage(Literals.Views.Source);
};

CGLayoutMainCenterBody.prototype.activateRoleList = function () {
  this.PageControl.showPage(Literals.Views.RoleList);
};

CGLayoutMainCenterBody.prototype.activateRole = function () {
  this.PageControl.showPage(Literals.Views.Role);
};

CGLayoutMainCenterBody.prototype.activateTaskList = function () {
  this.PageControl.showPage(Literals.Views.TaskList);
};

CGLayoutMainCenterBody.prototype.activateTrash = function () {
  this.PageControl.showPage(Literals.Views.Trash);
};

CGLayoutMainCenterBody.prototype.activateDashboard = function () {
  this.PageControl.showPage(Literals.Views.Dashboard);
};

CGLayoutMainCenterBody.prototype.activateAnalysisboard = function () {
  this.PageControl.showPage(Literals.Views.Analysisboard);
};

CGLayoutMainCenterBody.prototype.activateNews = function () {
  this.PageControl.showPage(Literals.Views.PageNews);
};

CGLayoutMainCenterBody.prototype.enableNotifications = function () {
  this.bNotify = true;
};

CGLayoutMainCenterBody.prototype.disableNotifications = function () {
  this.bNotify = false;
};

CGLayoutMainCenterBody.prototype.atRemovePanel = function (Region, ContentPanel, EventManager) {

  if (!this.bNotify) {
    EventManager.cancel = true;
    return;
  }

  if (ContentPanel.Id.indexOf(NODE_TAB_PREFIX) != -1) CommandListener.throwCommand("closenode(" + ContentPanel.Id.replace(NODE_TAB_PREFIX, "") + ")");
  else if (ContentPanel.Id.indexOf(TASK_TAB_PREFIX) != -1) CommandListener.throwCommand("closetask(" + ContentPanel.Id.replace(TASK_TAB_PREFIX, "") + ")");
  else if (ContentPanel.Id.indexOf(SOURCE_TAB_PREFIX) != -1) CommandListener.throwCommand("closesource(" + ContentPanel.Id.replace(SOURCE_TAB_PREFIX, "") + ")");

  EventManager.cancel = false;
};

CGLayoutMainCenterBody.prototype.atPanelActivated = function (Region, ContentPanel) {
  if (!this.bNotify) return;

  if (ContentPanel.Id.indexOf(NODE_TAB_PREFIX) != -1) CommandListener.throwCommand("activatenode(" + ContentPanel.Id.replace(NODE_TAB_PREFIX, "") + ")");
  else if (ContentPanel.Id.indexOf(TASK_TAB_PREFIX) != -1) CommandListener.throwCommand("activatetask(" + ContentPanel.Id.replace(TASK_TAB_PREFIX, "") + ")");
  else if (ContentPanel.Id.indexOf(SOURCE_TAB_PREFIX) != -1) CommandListener.throwCommand("activatesource(" + ContentPanel.Id.replace(SOURCE_TAB_PREFIX, "") + ")");
};

CGLayoutMainCenterBody.prototype.existsView = function (IdView) {
  return (this.aViews[IdView] != null);
};

CGLayoutMainCenterBody.prototype.getView = function (Type, IdView) {
  if (this.aViews[IdView] == null) return false;
  return this.aViews[IdView];
};

CGLayoutMainCenterBody.prototype.getViews = function (Type, ViewType, IdTarget) {
  var aResult = new Array();
  var aViewsToRemove = new Array();

  if (Type == VIEW_NODE) Type = VIEW_NODE_TYPE_NODE;
  else if (Type == VIEW_TASK) Type = VIEW_TASK_TYPE_TASK;
  else if (Type == VIEW_SOURCE) Type = VIEW_SOURCE_TYPE_SOURCE;
  else if (Type == VIEW_TEAM) Type = VIEW_TEAM_TYPE_TEAM;

  for (IdView in this.aViews) {
    var View = this.aViews[IdView];
    if (isFunction(View)) continue;
    var DOMLayer = View.getDOM();
    if (DOMLayer != null && $(DOMLayer.id) == null) {
      aViewsToRemove.push(DOMLayer.id);
      continue;
    }
    if ((ViewType != null) && (ViewType != Type) && (View.getType() != ViewType)) continue;
    if ((IdTarget != null) && (View.getTarget().getId() != IdTarget)) continue;
    aResult.push(View);
  }

  for (var i = 0; i < aViewsToRemove.length; i++)
    this.deleteView(Type, aViewsToRemove[i]);

  return aResult;
};

CGLayoutMainCenterBody.prototype.isContainerView = function (View) {
  return (View.ViewContainer == null);
};

CGLayoutMainCenterBody.prototype.getContainerView = function (Type, IdTarget) {
  if (this.aContainerViews[Type][IdTarget] == null) return null;
  return this.aContainerViews[Type][IdTarget];
};

CGLayoutMainCenterBody.prototype.addView = function (Type, View) {
  var ViewContainer;

  this.aViews[View.getId()] = View;

  if ((ViewContainer = View.getContainer()) != null) {
    if (this.aViewsOfContainer[ViewContainer.getId()] == null) this.aViewsOfContainer[ViewContainer.getId()] = new Array();
    this.aViewsOfContainer[ViewContainer.getId()].push(View.getId());
  }
  else {
    var Target = View.getTarget();
    if ((Target != null) && (Target.getId)) this.aContainerViews[Type][Target.getId()] = View;
  }
};

CGLayoutMainCenterBody.prototype.deleteView = function (Type, Id) {
  var Target;

  if (this.aViews[Id] == null) return true;

  if (this.aViews[Id].getContainer() == null) {
    Target = this.aViews[Id].getTarget();
    if ((Target != null) && (Target.getId)) {
      delete this.aContainerViews[Type][Target.getId()];
    }
  }

  if (this.aViewsOfContainer[Id] != null) {
    for (var iPos = 0; iPos < this.aViewsOfContainer[Id].length; iPos++) {
      this.deleteView(this.aViewsOfContainer[Id][iPos]);
    }
    delete this.aViewsOfContainer[Id];
  }

  delete this.aViews[Id];

  return true;
};

CGLayoutMainCenterBody.prototype.deleteViewsOfContainer = function (Type, IdContainer) {
  if (this.aViews[IdContainer] == null) return true;

  if (this.aViewsOfContainer[IdContainer] != null) {
    for (var iPos = 0; iPos < this.aViewsOfContainer[IdContainer].length; iPos++) {
      this.deleteView(this.aViewsOfContainer[IdContainer][iPos]);
    }
    delete this.aViewsOfContainer[IdContainer];
  }

  return true;
};

CGLayoutMainCenterBody.prototype.getTabId = function (Type, Id) {
  var sPrefix = "";

  if (Type == VIEW_NODE) sPrefix = NODE_TAB_PREFIX;
  else if (Type == VIEW_TASK) sPrefix = TASK_TAB_PREFIX;
  else if (Type == VIEW_SOURCE) sPrefix = SOURCE_TAB_PREFIX;
  else if (Type == VIEW_TEAM) sPrefix = TEAM_TAB_PREFIX;

  return sPrefix + Id;
};

CGLayoutMainCenterBody.prototype.existsTab = function (Type, Id) {
  return this.PageControl.existsPage(this.getTabId(Type, Id));
};

CGLayoutMainCenterBody.prototype.getActiveTab = function (Type) {
  return this.getTabId(Type, this.PageControl.getActivePage().Id);
};

CGLayoutMainCenterBody.prototype.isTabActive = function (Type, Id) {
  var IdCurrentTab = this.PageControl.getActivePage().Id;
  return (IdCurrentTab == this.getTabId(Type, Id));
};

CGLayoutMainCenterBody.prototype.activateTab = function (Type, Id) {
  if (this.isTabActive(Type, Id)) return;
  return this.PageControl.showPage(this.getTabId(Type, Id));
};

CGLayoutMainCenterBody.prototype.addTab = function (Type, Config) {
  var IdPage = this.getTabId(Type, Config.Id);
  Config.Id = IdPage;
  this.PageControl.addPage(Config);
  return IdPage;
};

CGLayoutMainCenterBody.prototype.updateTab = function (Type, Id, sTitle) {
  this.PageControl.setPageTitle(this.getTabId(Type, Id), sTitle);
};

CGLayoutMainCenterBody.prototype.deleteTab = function (Type, Id) {
  this.PageControl.deletePage(this.getTabId(Type, Id));
};

CGLayoutMainCenterBody.prototype.refresh = function () {
  this.aViews.each(function (View) {
    if (!View) return;
    if (View.refresh) View.refresh();
  });
};

//---------------------------------------------------------------------
CGLayoutMainCenterBody.prototype.atScroll = function (Event, Obj) {
  var IdTab = this.getActiveTab(VIEW_NODE);
  if (this.scrollListeners[IdTab]) {
    this.scrollListeners[IdTab](Event, Obj);
  }
};

//---------------------------------------------------------------------
CGLayoutMainCenterBody.prototype.setScrollListener = function (IdView, delegate, DOMLayer) {

  if (DOMLayer != null) {
    var extViewPort = this.getViewport(DOMLayer);
    extViewPort.on('scroll', this.atScroll, this);
    this.scrollListeners[IdView] = delegate;
    this.scrollViewports[IdView] = extViewPort;
  }
  else {
    if (this.scrollViewports[IdView]) this.scrollViewports[IdView].un('scroll', this.atScroll, this);
    delete this.scrollListeners[IdView];
    delete this.scrollViewports[IdView];
  }

};

//---------------------------------------------------------------------
CGLayoutMainCenterBody.prototype.getViewport = function (DOMLayer) {
  var extResult = Ext.get(DOMLayer).up(".x-tabs-item-body");
  if (extResult == null) extResult = Ext.get(DOMLayer).up(".x-tabs-body").down(".x-tabs-item-body");
  if (extResult == null) return this.InnerLayout.el.select(".x-tabs-body.x-layout-tabs-body").first();
  return extResult;
};