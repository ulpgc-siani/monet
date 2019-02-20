TASK_TAB_ROUTE = "route";
TASK_TAB_HISTORY = "history";
TEMPLATE_TASK_TAB_ID = "#{idtask}_#{idtab}";

CGDecoratorTask = function () {
};

CGDecoratorTask.prototype = new CGDecorator;

CGDecoratorTask.prototype.execute = function (DOMTask) {

  DOMTask.CurrentTab = null;
  this.addCommonMethods(DOMTask);

  DOMTask.init = function () {
    var extTask = Ext.get(this);

    this.initToolbar(".properties .info .header .toolbar");
    this.initTabs(CSS_TASK);

    var extRouteMap = extTask.select(CSS_ROUTE_MAP).first();
    if (extRouteMap) extRouteMap.dom.init();

    this.checkTeamEmpty();
  };

  DOMTask.isStateTabActive = function () {
    var tabId = this.getActiveTab();
    if (tabId == "state") return true;
    return false;
  };

  DOMTask.destroy = function () {
  };

  DOMTask.getId = function () {
    return this.getControlInfo().IdTask;
  };

  DOMTask.getTitle = function () {
    var extTask = Ext.get(this);
    var extTitle = extTask.select(".title .descriptor").first();
    if (extTitle == null) return Context.Config.DefaultLabel;
    return extTitle.dom.innerHTML;
  };

  DOMTask.getStateTitle = function () {
    var extTask = Ext.get(this);
    var extTitle = extTask.select(".state .title").first();
    if (extTitle == null) return Context.Config.DefaultLabel;
    return extTitle.dom.innerHTML;
  };

  DOMTask.clearStateView = function () {
	  var extTask = Ext.get(this);
	  var extView = extTask.select(".state .view").first();

	  if (extView != null)
	    extView.dom.innerHTML = "";
  }

  DOMTask.getControlInfo = function () {
    var extId, extCode, extMode, extGoal, extTimeStamp;
    var aResult = new Array();
    var extTask = Ext.get(this);

    if (this.ControlInfo) return this.ControlInfo;

    this.ControlInfo = new Object();
    this.ControlInfo.IdTask = (extId = extTask.select(CSS_CONTROL_INFO + " > .idtask").first()) ? extId.dom.innerHTML : "-1";
    this.ControlInfo.Code = (extCode = extTask.select(CSS_CONTROL_INFO + " > .code").first()) ? extCode.dom.innerHTML : "-1";
    this.ControlInfo.Mode = (extMode = extTask.select(CSS_CONTROL_INFO + " > .mode").first()) ? extMode.dom.innerHTML : null;
    this.ControlInfo.TimeStamp = (extTimeStamp = extTask.select(CSS_CONTROL_INFO + " > .timestamp").first()) ? extTimeStamp.dom.innerHTML : null;
    this.ControlInfo.Templates = new Object();

    aResult = extTask.select(".tpl.refresh");
    this.ControlInfo.Templates.Refresh = (eResult = aResult.first()) ? eResult.dom.innerHTML : null;

    extGoal = extTask.select(CSS_CONTROL_INFO + " > .goal").first();
    this.ControlInfo.Goal = new Object();
    if (extGoal != null) {
      this.ControlInfo.Goal.Code = extGoal.dom.name;
      this.ControlInfo.Goal.Label = extGoal.dom.value;
    }

    return this.ControlInfo;
  };

  DOMTask.getGoal = function () {
    var ControlInfo = this.getControlInfo();
    return ControlInfo.Goal;
  };

  DOMTask.setGoal = function (Goal) {
    var extTask = Ext.get(this);
    var extGoal = extTask.select(CSS_CONTROL_INFO + " > .goal").first();
    if (extGoal != null) {
      extGoal.dom.name = Goal.Code;
      extGoal.dom.value = Goal.Label;
      this.ControlInfo.Goal = Goal;
    }
  };

  DOMTask.internalGetEmbeddedNodes = function (selector) {
    var aResult = new Array();
    var extTask = Ext.get(this);
    var extNodeList;

    // IMPORTANT. All nodes are embedded
    if (!(extNodeList = extTask.select(selector))) return false;

    extNodeList.each(function (extNode) {
      aResult.push($(extNode.dom));
    }, this);

    return aResult;
  };

  DOMTask.getEmbeddedNodes = function () {
    return this.internalGetEmbeddedNodes(CSS_NODE);
  };

  DOMTask.getEditableEmbeddedNodes = function () {
    return this.internalGetEmbeddedNodes(CSS_NODE + DOT + CLASS_EDITABLE);
  };

  DOMTask.getNode = function (Id) {
    var aResult = new Array();

    var extTask = Ext.get(this);

    if (!(extNodeList = extTask.select(CSS_NODE + "." + Id))) return false;
    if (extNodeList.getCount() == 0) return null;

    return extNodeList.first().dom;
  };

  DOMTask.getRouteMap = function () {
    var extTask = Ext.get(this);
    return extTask.select(CSS_ROUTE_MAP).first().dom;
  };

  DOMTask.isLoaded = function () {
    var extTask = Ext.get(this);
    var extBody = extTask.down(CSS_BODY);
    if (extBody == null) return true;
    return (!extBody.hasClass(CLASS_LOADING));
  };

  DOMTask.loadTab = function (Tab, DOMTab) {
    /*
     var extTab = Ext.get(DOMTab);
     var extTask = extTab.select(CSS_TASK).first();
     var bDoLoad = false;

     if ((extTask) && (!extTask.dom.isLoaded())) bDoLoad = true;
     else if (Tab == TASK_TAB_HISTORY) bDoLoad = true;

     if (bDoLoad) {
     var Process = new CGProcessLoadEmbeddedTask();
     Process.DOMItem = extTask.dom;
     Process.execute();
     }
     */
  };

  DOMTask.checkTeamEmpty = function () {
    var extTask = Ext.get(this);
    var extTeam = extTask.select(CSS_TASK_TEAM).first();

    if (extTeam == null) return;

    var extUserList = extTeam.select(CSS_TASK_TEAM_USER_LIST).first();
    var extEmpty = extTeam.select(".empty").first();
    var extUsers = extUserList.select("li");

    if (extUsers.getCount() <= 0) {
      if (extEmpty == null) new Insertion.After(extUserList.dom, "<div class='empty'>" + Lang.ViewTask.NoTeam + "</div>");
    }
    else {
      if (extEmpty != null) extEmpty.remove();
    }
  };

  DOMTask.getDOMHistory = function () {
    var extTask = Ext.get(this);
    return extTask.select(CSS_HISTORY_ITEMS).first().dom;
  };

  DOMTask.addHistoryPage = function (sPage, bHasMore) {
    var extTask = Ext.get(this);
    var extHistory = extTask.select(CSS_HISTORY).first();
    extHistory.select(CSS_HISTORY_ITEMS).first().insertHtml('beforeEnd', sPage);
    if (!bHasMore)
      extHistory.select(CSS_HISTORY_MORE).first().dom.style.display = "none";
    CommandListener.capture(this.getDOMHistory());
  };

  DOMTask.getHistoryItemsCount = function () {
    var extTask = Ext.get(this);
    return extTask.select(CSS_HISTORY_ITEMS_CHILDS).getCount();
  };

  DOMTask.switchHistory = function () {
    if (this.historyViewType == VIEW_TASK_HISTORY_EXTENDED) {

      this.historyViewType = VIEW_TASK_HISTORY_COLLAPSED;
    } else if (this.historyViewType == VIEW_TASK_HISTORY_COLLAPSED) {

      this.historyViewType = VIEW_TASK_HISTORY_EXTENDED;
    }
  };
  DOMTask.historyViewType = VIEW_TASK_HISTORY_EXTENDED;
};