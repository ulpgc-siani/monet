//----------------------------------------------------------------------
// Show TaskList
//----------------------------------------------------------------------
function CGActionShowTaskList() {
  this.base = CGAction;
  this.base(2);
};

CGActionShowTaskList.prototype = new CGAction;
CGActionShowTaskList.constructor = CGActionShowTaskList;
CommandFactory.register(CGActionShowTaskList, { View: 0, Situation: 1 }, true);

CGActionShowTaskList.prototype.step_1 = function () {

  Desktop.hideBanner();
  State.isShowingPrototype = false;

  Kernel.loadSystemTemplate(this, "tasklist", this.View, this.Situation);
};

CGActionShowTaskList.prototype.step_2 = function () {
  ViewTaskList.setContent(this.data);
  ViewTaskList.refresh();
  ViewTaskList.show();
  ViewTaskList.getDOM().activateDefaultTab();

  if (this.View == "taskboard")
    this.setActiveFurniture(Furniture.TASKBOARD);
  else if (this.View == "tasktray")
    this.setActiveFurniture(Furniture.TASKTRAY);

  Desktop.Main.Center.Body.activateTaskList();
};

//----------------------------------------------------------------------
// Render Task List
//----------------------------------------------------------------------
function CGActionRenderTaskList() {
  this.base = CGAction;
  this.base(2);
  this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionRenderTaskList.prototype = new CGAction;
CGActionRenderTaskList.constructor = CGActionRenderTaskList;
CommandFactory.register(CGActionRenderTaskList, { Inbox: 0, IdDOMViewerLayer: 1, IdDOMViewerLayerOptions: 2 }, false);

CGActionRenderTaskList.prototype.atAddItem = function (Sender, Item) {
  CommandListener.throwCommand("createtask(" + Item.Code + ")");
};

CGActionRenderTaskList.prototype.atBoundItem = function (Sender, Item) {
  var idUser = Account.getUser().getId();

  var Dummy = Item;
  for (var index in Dummy) {
    if (isFunction(Dummy[index])) continue;
    Item[index + "_short"] = shortValue(Dummy[index]);
  }

  if (!Item.state) return;

  Item.ownerStyle = Item.senderStyle = Item.senderEmptyStyle = "";
  if (Item.idOwner != null) {
    if (Item.idOwner == idUser) {
      if (Item.idSender != null)
        Item.senderStyle = "active";
      else
        Item.senderEmptyStyle = "active";
    }
    else
      Item.ownerStyle = "active";
  }

  if (Item.urgent == true) Item.urgentStyle = "active";
  else Item.urgentStyle = "";

  if (Item.state == "new") Item.stateLabel = Lang.ViewTaskList.State.New;
  else if (Item.state == "pending") Item.stateLabel = Lang.ViewTaskList.State.Pending;
  else if (Item.state == "waiting") Item.stateLabel = Lang.ViewTaskList.State.Waiting;
  else if (Item.state == "expired") Item.stateLabel = Lang.ViewTaskList.State.Expired;
  else if (Item.state == "finished") Item.stateLabel = Lang.ViewTaskList.State.Finished;
  else if (Item.state == "aborted") Item.stateLabel = Lang.ViewTaskList.State.Aborted;
  else if (Item.state == "failure") Item.stateLabel = Lang.ViewTaskList.State.Failure;
  else Item.stateLabel = Lang.ViewTaskList.State.Undefined;

  if (Item.type == "service") Item.typeLabel = Lang.ViewTaskList.Type.Service;
  else if (Item.type == "activity") Item.typeLabel = Lang.ViewTaskList.Type.Activity;
  else if (Item.type == "job") Item.typeLabel = Lang.ViewTaskList.Type.Job;

  Item.newMessagesClass = Item.newMessagesCount > 0 ? "active" : "";

  if (Item.startDate != null) {
    var date = parseServerDate(Item.startDate);
    var message = Lang.ViewTaskList.StartedAt;
    if (date.getTime() > new Date()) message = Lang.ViewTaskList.StartAt;
    Item.startDate = message + getFormattedDateTime(date, Context.Config.Language, false);
  }

  Item.createDate = getFormattedDateTime(parseServerDate(Item.createDate), Context.Config.Language, false);
  Item.updateDate = getFormattedDateTime(parseServerDate(Item.updateDate), Context.Config.Language, false);
  Item.random = Math.random();
};

CGActionRenderTaskList.prototype.atRenderItem = function (Sender, Item, DOMItem) {
  CommandListener.capture(DOMItem);
};

CGActionRenderTaskList.prototype.atUpdateState = function (NewState) {
  State.registerListViewerState("tasklist_" + this.Inbox, NewState);
};

CGActionRenderTaskList.prototype.destroyViewer = function () {
  if (State.TaskListViewer == null) return;
  State.TaskListViewer.dispose();
  $(Literals.ListViewerWizard).innerHTML = "";
};

CGActionRenderTaskList.prototype.createViewer = function () {
  var Options;

  this.destroyViewer();

  eval($(this.IdDOMViewerLayerOptions).innerHTML);
  State.TaskListViewer = new CGListViewer(Options, Context.Config.Language, Context.Config.ImagesPath);
  State.TaskListViewer.setDataSourceUrls(Kernel.getLoadTasksLink(this.Inbox), null);
  State.TaskListViewer.setWizardLayer(Literals.ListViewerWizard);
  State.TaskListViewer.onAddItem = CGActionRenderTaskList.prototype.atAddItem.bind(this);
  State.TaskListViewer.onBoundItem = CGActionRenderTaskList.prototype.atBoundItem.bind(this);
  State.TaskListViewer.onRenderItem = CGActionRenderTaskList.prototype.atRenderItem.bind(this);
  State.TaskListViewer.onUpdateState = CGActionRenderTaskList.prototype.atUpdateState.bind(this);
  State.TaskListViewer.setState(State.getListViewerState("tasklist_" + this.Inbox));
  State.TaskListViewer.render(this.IdDOMViewerLayer);
};

CGActionRenderTaskList.prototype.step_1 = function () {

  if ((this.IdDOMViewerLayer == null) || (this.IdDOMViewerLayerOptions == null)) {
    this.terminate();
    return;
  }

  var Process = new CGProcessLoadHelperListViewer();
  Process.ReturnProcess = this;
  Process.execute();
};

CGActionRenderTaskList.prototype.step_2 = function () {
  this.createViewer();
  this.terminate();
};

//----------------------------------------------------------------------
// Refresh task list
//----------------------------------------------------------------------
function CGActionRefreshTaskList() {
  this.base = CGAction;
  this.base(1);
  this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionRefreshTaskList.prototype = new CGAction;
CGActionRefreshTaskList.constructor = CGActionRefreshTaskList;
CommandFactory.register(CGActionRefreshTaskList, null, false);

CGActionRefreshTaskList.prototype.step_1 = function () {
  if (State.TaskListViewer) State.TaskListViewer.refresh();
  var Process = new CGProcessLoadHelperListViewer();
  Process.ReturnProcess = this;
  Process.execute();
};

//----------------------------------------------------------------------
// Set tasks owner
//----------------------------------------------------------------------
function CGActionSetTasksOwner() {
  this.base = CGAction;
  this.base(3);
  this.dialogLayerId = null;
};

CGActionSetTasksOwner.prototype = new CGAction;
CGActionSetTasksOwner.constructor = CGActionSetTasksOwner;
CommandFactory.register(CGActionSetTasksOwner, null, false);

CGActionSetTasksOwner.prototype.saveReason = function () {
	if (this.dialog == null) return;
	State.SetTaskOwnerReason = this.dialog.Reason;
};

CGActionSetTasksOwner.prototype.destroy = function (sResponse) {
  this.resetState();
  if (this.dialogLayerId != null) $(this.dialogLayerId).remove();
};

CGActionSetTasksOwner.prototype.cancel = function() {
	this.saveReason();
	this.destroy();
};

CGActionSetTasksOwner.prototype.onFailure = function (sResponse) {
  this.saveReason();
  Desktop.reportError(Lang.Action.SetTasksOwner.Failure);
  if (this.dialogLayerId != null) $(this.dialogLayerId).remove();
  this.terminate();
};

CGActionSetTasksOwner.prototype.step_1 = function () {
  var aTasks = State.TaskListViewer.getSelectedItems();

  if (aTasks.length <= 0) {
    Desktop.reportWarning(Lang.ViewTask.DialogSetTasksOwner.NoSelection);
    this.terminate();
    return;
  }

  var extItem = Ext.get(this.DOMItem);
  this.dialogLayerId = Ext.id();
  new Insertion.After(extItem.dom, "<div class='dialog embedded' id='" + this.dialogLayerId + "'></div>");

  this.dialog = new CGDialogSelectTaskOwner();
  this.dialog.target = { reason: State.SetTaskOwnerReason };
  this.dialog.onAccept = this.execute.bind(this);
  this.dialog.onCancel = this.cancel.bind(this);
  this.dialog.init(this.dialogLayerId);
  this.dialog.show();
};

CGActionSetTasksOwner.prototype.step_2 = function () {
  var aTasks = State.TaskListViewer.getSelectedItems();

  $(this.dialogLayerId).remove();
  State.SetTaskOwnerReason = null;
  Kernel.setTasksOwner(this, this.dialog.User, this.dialog.Reason, aTasks.toString());
};

CGActionSetTasksOwner.prototype.step_3 = function () {
  State.TaskListViewer.clearSelection();
  State.TaskListViewer.refresh();
  this.terminate();
};

//----------------------------------------------------------------------
// Unset tasks owner
//----------------------------------------------------------------------
function CGActionUnsetTasksOwner() {
  this.base = CGAction;
  this.base(3);
};

CGActionUnsetTasksOwner.prototype = new CGAction;
CGActionUnsetTasksOwner.constructor = CGActionUnsetTasksOwner;
CommandFactory.register(CGActionUnsetTasksOwner, null, false);

CGActionUnsetTasksOwner.prototype.onFailure = function (sResponse) {
  Desktop.reportError(Lang.Action.UnsetTasksOwner.Failure);
  this.terminate();
};

CGActionUnsetTasksOwner.prototype.step_1 = function () {
  Ext.MessageBox.confirm(Lang.ViewTask.DialogUnsetTasksOwner.Title, Lang.ViewTask.DialogUnsetTasksOwner.Description, CGActionUnsetTasksOwner.prototype.checkOption.bind(this));
};

CGActionUnsetTasksOwner.prototype.step_2 = function () {
  var aTasks = State.TaskListViewer.getSelectedItems();

  if (aTasks.length <= 0) {
    Desktop.reportWarning(Lang.ViewTask.DialogUnsetTasksOwner.NoSelection);
    this.terminate();
    return;
  }

  Kernel.unsetTasksOwner(this, aTasks.toString());
};

CGActionUnsetTasksOwner.prototype.step_3 = function () {
  State.TaskListViewer.clearSelection();
  State.TaskListViewer.refresh();
  this.terminate();
};

// ----------------------------------------------------------------------
// Print task list
// ----------------------------------------------------------------------
function CGActionPrintTaskList() {
	this.base = CGAction;
	this.base(3);
    this.allAttributes = new Array();
};

CGActionPrintTaskList.prototype = new CGAction;
CGActionPrintTaskList.constructor = CGActionPrintTaskList;
CommandFactory.register(CGActionPrintTaskList, { Mode: 0, Inbox: 1 }, false);

CGActionPrintTaskList.prototype.destroy = function (sResponse) {
  if (this.dialog) this.dialog.destroy();
  this.terminate();
};

CGActionPrintTaskList.prototype.saveDialogResult = function () {
  CGActionPrintTaskList.Attributes = this.dialog.Attributes;
  CGActionPrintTaskList.DateAttribute = this.dialog.DateAttribute;
  CGActionPrintTaskList.FromDate = this.dialog.FromDate;
  CGActionPrintTaskList.ToDate = this.dialog.ToDate;
};

CGActionPrintTaskList.prototype.getAttributes = function () {
  if (this.Mode == "pdf")
    return CGActionPrintTaskList.Attributes;

  var result = new Array();
  for (var i=0; i<this.allAttributes.length; i++)
    result.push(this.allAttributes[i].code);

  return result;
};

CGActionPrintTaskList.prototype.step_1 = function () {
  Kernel.loadTaskListPrintAttributes(this);
};

CGActionPrintTaskList.prototype.step_2 = function () {
  var extItem = Ext.get(this.DOMItem);
  this.allAttributes = Ext.util.JSON.decode(this.data);

  this.dialogLayerId = Ext.id();
  new Insertion.After(extItem.dom, "<div class='dialog embedded' id='" + this.dialogLayerId + "'></div>");

  this.dialog = new CGDialogPrintEntity();
  this.dialog.onAccept = this.execute.bind(this);
  this.dialog.onCancel = this.destroy.bind(this);
  this.dialog.Attributes = CGActionPrintTaskList.Attributes != null ? CGActionPrintTaskList.Attributes : new Array();
  this.dialog.DateAttribute = CGActionPrintTaskList.DateAttribute;
  this.dialog.FromDate = CGActionPrintTaskList.FromDate;
  this.dialog.ToDate = CGActionPrintTaskList.ToDate;
  this.dialog.target = { Attributes: this.allAttributes, Mode : this.Mode, DisableRange : true };
  this.dialog.init(this.dialogLayerId);
  this.dialog.show();
};

CGActionPrintTaskList.prototype.step_3 = function () {
	var Filters = new Object();
	var ListState = State.TaskListViewer.getState();

    $(this.dialogLayerId).remove();
    this.saveDialogResult();

    if (ListState != null) {
		Filters["query"] = ListState.Filter;
		Filters["sortsby"] = "";
		Filters["groupsby"] = "";

		for (var i = 0; i < ListState.Sorts.length; i++) {
			Filters["sortsby"] += ListState.Sorts[i].Code + MONET_FILTER_SEPARATOR + ListState.Sorts[i].Mode + MONET_FILTERS_SEPARATOR;
		}

		for (var i = 0; i < ListState.Groups.length; i++) {
			Filters["groupsby"] += ListState.Groups[i].Code + MONET_FILTER_SEPARATOR + ListState.Groups[i].Value + MONET_FILTERS_SEPARATOR;
		}

		if (Filters["sortsby"].length > 0)
			Filters["sortsby"] = Filters["sortsby"].substring(0, Filters["sortsby"].length - MONET_FILTERS_SEPARATOR.length);

		if (Filters["groupsby"].length > 0)
			Filters["groupsby"] = Filters["groupsby"].substring(0, Filters["groupsby"].length - MONET_FILTERS_SEPARATOR.length);
	}

	window.location = Kernel.getPrintTaskListLink(this.Mode, this.Inbox, (ListState!=null)?ListState.Folder:"", Filters, this.getAttributes(), CGActionPrintTaskList.DateAttribute, CGActionPrintTaskList.FromDate, CGActionPrintTaskList.ToDate);
};