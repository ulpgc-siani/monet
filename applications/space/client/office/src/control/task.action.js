//----------------------------------------------------------------------
// Show Task
//----------------------------------------------------------------------
function CGActionShowTask() {
  this.base = CGActionShowBase;
  this.base(4);
  this.AvailableProcessClass = CGProcessCleanDirty;
  this.RefreshProcessClass = CGProcessRefreshDOM;
  this.DOMViewActiveTab = null;
  this.Mode = null;
};

CGActionShowTask.prototype = new CGActionShowBase;
CGActionShowTask.constructor = CGActionShowTask;
CommandFactory.register(CGActionShowTask, { Id: 0, Mode: 1 }, true);

CGActionShowTask.prototype.getDOMElement = function () {
  return Extension.getDOMTask(this.DOMItem);
};

CGActionShowTask.prototype.step_1 = function () {
  var ViewTask;

  Desktop.hideBanner();
  State.TaskNode = null;

  ViewTask = Desktop.Main.Center.Body.getContainerView(VIEW_TASK, this.Id);
  if (ViewTask != null && ViewTask.getDOM() != null) {
    this.DOMViewActiveTab = ViewTask.getDOM().getActiveTab();
    if ((this.Mode == null) || (ViewTask.getMode() == this.Mode)) {
      var Process = new CGProcessActivateTask();
      Process.Id = this.Id;
      Process.RefreshTask = true;
      Process.execute();
      State.LastView = ViewTask;
      this.terminate();
      return;
    }
    ViewTask.destroy();
  }

  if (!this.Mode) {
    var Behaviour = Extension.getDefinitionBehaviour(DEFAULT);

    if ((!Behaviour) || (!Behaviour.ShowTask) || (!Behaviour.ShowTask.Templates) || (!Behaviour.ShowTask.Templates.Edit)) {
      Desktop.hideReports();
      this.terminate();
      return;
    }

    this.Mode = Behaviour.ShowTask.Templates.Edit;
  }

  Kernel.loadTask(this, this.Id, this.Mode);
};

CGActionShowTask.prototype.step_2 = function () {
  var Task, ViewTask;
  var ProcessShowTask;

  Task = new CGTask();
  Task.unserialize(this.data);
  TasksCache.register(Task);
  TasksCache.setCurrent(Task.getId());

  if ((ViewTask = this.getView(VIEW_TASK, Task)) == false) {
    this.terminate();
    return;
  }

  State.LastView = ViewTask;
  State.LastObject.Id = this.Id;
  State.LastObject.Mode = this.Mode;

  ProcessShowTask = new CGProcessShowTask();
  ProcessShowTask.ActivateTask = false;
  ProcessShowTask.ReturnProcess = this;
  ProcessShowTask.Task = Task;
  ProcessShowTask.ViewTask = ViewTask;
  ProcessShowTask.DOMViewActiveTab = this.DOMViewActiveTab;
  ProcessShowTask.execute();
};

CGActionShowTask.prototype.step_3 = function () {
  if (this.bActivate) this.execute();
  else this.terminate();
};

CGActionShowTask.prototype.step_4 = function () {
  var Process = new CGProcessActivateTask();
  Process.Id = this.Id;
  Process.NotifyFocus = false;
  Process.execute();
  this.terminate();
};

// ----------------------------------------------------------------------
// Show task view
// ----------------------------------------------------------------------
function CGActionShowTaskView() {
  this.base = CGAction;
  this.base(3);
  this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionShowTaskView.prototype = new CGAction;
CGActionShowTaskView.constructor = CGActionShowTaskView;
CommandFactory.register(CGActionShowTaskView, { Id: 0, View: 1 }, true);

CGActionShowTaskView.prototype.step_1 = function () {
  var action = new CGActionShowTask();
  action.Id = this.Id;
  action.ReturnProcess = this;
  action.execute();
}

CGActionShowTaskView.prototype.step_2 = function () {
  var ViewTask = Desktop.Main.Center.Body.getContainerView(VIEW_TASK, this.Id);
  var DOMView = ViewTask.getDOM();
  var viewId = this.getViewId();

  if (viewId == null) {
    this.terminate();
    return;
  }

  DOMView.activateTab(viewId);
  window.setTimeout(this.execute.bind(this), 1000);
};

CGActionShowTaskView.prototype.step_3 = function () {
  var command = this.getCommand();
  if (command != null) CommandListener.dispatchCommand(command);
  this.terminate();
};

CGActionShowTaskView.prototype.getViewId = function () {
  if (this.View.indexOf("chat.") != -1) return CGViewTask.ViewOrders;
  return null;
}

CGActionShowTaskView.prototype.getCommand = function () {
  if (this.View.indexOf("chat.") != -1) {
    var orderId = this.View.replace("chat.", "");
    return "showtaskorderchat(" + this.Id + "," + orderId + "," + false + ")";
  }
  return null;
}

//----------------------------------------------------------------------
// Refresh Task
//----------------------------------------------------------------------
function CGActionRefreshTask() {
  this.base = CGAction;
  this.base(1);
  this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGActionRefreshTask.prototype = new CGAction;
CGActionRefreshTask.constructor = CGActionRefreshTask;
CommandFactory.register(CGActionRefreshTask, { Id: 0}, false);

CGActionRefreshTask.prototype.step_1 = function () {
  Process = new CGProcessRefreshTask();
  Process.Id = this.Id;
  Process.execute();
};

//----------------------------------------------------------------------
// Action refresh task tab
//----------------------------------------------------------------------
function CGActionRefreshTaskTab() {
  this.base = CGAction;
  this.base(1);
};

CGActionRefreshTaskTab.prototype = new CGAction;
CGActionRefreshTaskTab.constructor = CGActionRefreshTaskTab;
CommandFactory.register(CGActionRefreshTaskTab, { Id: 0 }, true);

CGActionRefreshTaskTab.prototype.step_1 = function () {

  var process = new CGProcessRefreshTaskTab();
  process.Id = this.Id;
  process.DOMTask = this.DOMItem.up(CSS_TASK);
  process.execute();

  this.terminate();
};

//----------------------------------------------------------------------
// Back task
//----------------------------------------------------------------------
function CGActionBackTask() {
  this.base = CGAction;
  this.base(1);
};

CGActionBackTask.prototype = new CGAction;
CGActionBackTask.constructor = CGActionBackTask;
CommandFactory.register(CGActionBackTask, null, false);

CGActionBackTask.prototype.step_1 = function () {
  var Process = new CGProcessShowLastView();
  Process.execute();
  this.terminate();
};

//----------------------------------------------------------------------
// Show task node
//----------------------------------------------------------------------
function CGActionShowTaskNode() {
  this.base = CGAction;
  this.base(1);
};

CGActionShowTaskNode.prototype = new CGAction;
CGActionShowTaskNode.constructor = CGActionShowTaskNode;
CommandFactory.register(CGActionShowTaskNode, { Id: 0 }, false);

CGActionShowTaskNode.prototype.isRecentTaskViewOfNode = function () {
  return this.DOMItem != null && this.DOMItem.up(".recenttask") != null;
};

CGActionShowTaskNode.prototype.getRecentTaskViewInfo = function () {
  return this.DOMItem.up(".recenttask").getControlInfo();
};

CGActionShowTaskNode.prototype.step_1 = function () {
  var Task = TasksCache.getCurrent();

  if (!Task) {
    this.terminate();
    return;
  }

  var Process = new CGProcessShowTaskNode();
  Process.IdTask = Task.getId();
  Process.IdNode = this.Id;
  if (this.isRecentTaskViewOfNode()) {
    var info = this.getRecentTaskViewInfo();
    Process.TargetNode = info.IdNode;
    Process.TargetView = info.CodeView;
  }
  Process.execute();

  this.terminate();
};

//----------------------------------------------------------------------
// Show task input
//----------------------------------------------------------------------
function CGActionShowTaskInput() {
  this.base = CGAction;
  this.base(1);
};

CGActionShowTaskInput.prototype = new CGAction;
CGActionShowTaskInput.constructor = CGActionShowTaskInput;
CommandFactory.register(CGActionShowTaskInput, { Id: 0 }, false);

CGActionShowTaskInput.prototype.step_1 = function () {
  var Task = TasksCache.get(this.Id);

  if (!Task) {
    this.terminate();
    return;
  }

  var Process = new CGProcessShowTaskNode();
  Process.IdTask = this.Id;
  Process.IdNode = Task.IdInput;
  Process.ITO = "input";
  Process.execute();

  this.terminate();
};

//----------------------------------------------------------------------
// Show task target
//----------------------------------------------------------------------
function CGActionShowTaskTarget() {
  this.base = CGAction;
  this.base(1);
};

CGActionShowTaskTarget.prototype = new CGAction;
CGActionShowTaskTarget.constructor = CGActionShowTaskTarget;
CommandFactory.register(CGActionShowTaskTarget, { Id: 0 }, false);

CGActionShowTaskTarget.prototype.step_1 = function () {
  var Task = TasksCache.get(this.Id);

  if (!Task) {
    this.terminate();
    return;
  }

  var Process = new CGProcessShowTaskNode();
  Process.IdTask = this.Id;
  Process.IdNode = Task.IdTarget;
  Process.ITO = "target";
  Process.execute();

  this.terminate();
};

//----------------------------------------------------------------------
// Show task target
//----------------------------------------------------------------------
function CGActionShowTaskShortcut() {
  this.base = CGAction;
  this.base(1);
};

CGActionShowTaskShortcut.prototype = new CGAction;
CGActionShowTaskShortcut.constructor = CGActionShowTaskShortcut;
CommandFactory.register(CGActionShowTaskShortcut, { Id: 0, IdShortcut: 1 }, false);

CGActionShowTaskShortcut.prototype.step_1 = function () {
  var Task = TasksCache.get(this.Id);

  if (!Task) {
    this.terminate();
    return;
  }

  var Process = new CGProcessShowTaskNode();
  Process.IdTask = this.Id;
  Process.IdNode = this.IdShortcut;
  Process.ITO = "shortcut";
  Process.execute();

  this.terminate();
};

//----------------------------------------------------------------------
// Show task output
//----------------------------------------------------------------------
function CGActionShowTaskOutput() {
  this.base = CGAction;
  this.base(1);
};

CGActionShowTaskOutput.prototype = new CGAction;
CGActionShowTaskOutput.constructor = CGActionShowTaskOutput;
CommandFactory.register(CGActionShowTaskOutput, { Id: 0 }, false);

CGActionShowTaskOutput.prototype.step_1 = function () {
  var Task = TasksCache.get(this.Id);

  if (!Task) {
    this.terminate();
    return;
  }

  Desktop.hideBanner();
  State.isShowingPrototype = false;

  var Process = new CGProcessShowTaskNode();
  Process.IdTask = this.Id;
  Process.IdNode = Task.IdOutput;
  Process.ITO = "output";
  Process.execute();

  this.terminate();
};

//----------------------------------------------------------------------
// Activate task
//----------------------------------------------------------------------
function CGActionActivateTask() {
  this.base = CGAction;
  this.base(1);
};

CGActionActivateTask.prototype = new CGAction;
CGActionActivateTask.constructor = CGActionActivateTask;
CommandFactory.register(CGActionActivateTask, { Id: 0 }, false);

CGActionActivateTask.prototype.step_1 = function () {
  var Process = new CGProcessActivateTask();
  Process.Id = this.Id;
  Process.RefreshTask = true;
  Process.execute();
  this.terminate();
};

//----------------------------------------------------------------------
// Close task
//----------------------------------------------------------------------
function CGActionCloseTask() {
  this.base = CGAction;
  this.base(1);
  this.AvailableProcessClass = CGProcessCleanDirty;
  this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGActionCloseTask.prototype = new CGAction;
CGActionCloseTask.constructor = CGActionCloseTask;
CommandFactory.register(CGActionCloseTask, { Id: 0 }, false);

CGActionCloseTask.prototype.step_1 = function () {
  var Process = new CGProcessCloseTask();
  Process.Id = this.Id;
  Process.execute();
  this.terminate();
};

//----------------------------------------------------------------------
// Do Task
//----------------------------------------------------------------------
function CGActionDoTask() {
  this.base = CGAction;
  this.base(2);
  this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionDoTask.prototype = new CGAction;
CGActionDoTask.constructor = CGActionDoTask;
CommandFactory.register(CGActionDoTask, { Id: 0 }, false);

CGActionDoTask.prototype.onFailure = function (sResponse) {
  Desktop.reportError(Lang.Action.DoTask.Failure);
  this.terminate();
};

CGActionDoTask.prototype.step_1 = function () {
  var Task, Process = null;

  Task = Account.TaskList.getTask(this.Id);
  if (!Task) {
    this.terminate();
    return;
  }

  if (Task.Code == TASK_TYPE_ATTACH_NODE) Process = new CGProcessDoTaskAttachNode();
  else if (Task.Code == TASK_TYPE_SHARE) Process = new CGProcessDoTaskShare();
  else if (Task.Code == TASK_TYPE_REVISION) Process = new CGProcessDoTaskRevision();

  if (Process != null) {
    Process.Id = this.Id;
    Process.DOMItem = this.DOMItem;
    Process.ReturnProcess = this;
    Process.execute();
  }
};

CGActionDoTask.prototype.step_2 = function () {
  this.addRefreshTask(RefreshTaskType.TaskList, null);
  this.terminate();
};

//----------------------------------------------------------------------
// Create task
//----------------------------------------------------------------------
function CGActionCreateTask() {
  this.base = CGAction;
  this.base(4);
  this.dlgCreateTask = null;
  this.AvailableProcessClass = CGProcessCleanDirty;
  this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGActionCreateTask.prototype = new CGAction;
CGActionCreateTask.constructor = CGActionCreateTask;
CommandFactory.register(CGActionCreateTask, { CodeType: 0, Mode: 1 }, false);

CGActionCreateTask.prototype.enabled = function () {
  var Task, aDefinitions;

  if ((Task = TasksCache.getCurrent()) == null) return false;
  if ((ViewTask = Desktop.Main.Center.Body.getContainerView(VIEW_TASK, Task.getId())) == null) return false;

  aDefinitions = Extension.getDefinitions(ViewTask.getDOM(), Task.Code);

  return (aDefinitions.size() > 0);
};

CGActionCreateTask.prototype.onFailure = function (sResponse) {
  if (this.dlgCreateTask) this.dlgCreateTask.destroy();
  Desktop.reportError(this.getErrorMessage(Lang.Action.CreateTask.Failure, sResponse));
  this.terminate();
};

CGActionCreateTask.prototype.renderDialog = function () {
  var aDefinitions;

  aDefinitions = Extension.getTaskDefinitions();

  if ((aDefinitions == null) || (aDefinitions.length == 0)) {
    Desktop.reportWarning(Lang.ViewTask.DialogCreateTask.NoTasks);
    this.terminate();
    return;
  }

  this.dlgCreateTask = new CGDialogCreateTask();
  this.dlgCreateTask.init();
  this.dlgCreateTask.Target = {TaskTypes: aDefinitions};
  this.dlgCreateTask.onAccept = this.execute.bind(this);
  this.dlgCreateTask.onCancel = this.resetState.bind(this);
  this.dlgCreateTask.refresh();
  this.dlgCreateTask.show();
};

CGActionCreateTask.prototype.step_1 = function () {
  this.Title = Context.Config.DefaultLabel;
  if (!this.Code) this.renderDialog();
  else this.gotoStep(3);
};

CGActionCreateTask.prototype.step_2 = function () {
  this.Code = this.dlgCreateTask.TaskType.Code;
  this.Title = this.dlgCreateTask.Title;
  this.dlgCreateTask.destroy();
  this.execute();
};

CGActionCreateTask.prototype.step_3 = function () {

  if (!this.Mode) {
    var Behaviour = Extension.getDefinitionBehaviour(this.Code);

    if ((!Behaviour) || (!Behaviour.CreateTask) || (!Behaviour.CreateTask.Templates) || (!Behaviour.CreateTask.Templates.Edit)) {
      this.terminateOnFailure();
      return;
    }

    this.Mode = Behaviour.CreateTask.Templates.Edit;
  }

  Kernel.createTask(this, this.Code, this.Mode, this.Title);
};

CGActionCreateTask.prototype.step_4 = function () {
  var Process, Task;

  Task = new CGTask();
  Task.unserialize(this.data);
  TasksCache.register(Task);

  Process = new CGProcessShowTask();
  Process.Task = Task;
  Process.Mode = this.Mode;
  Process.execute();

  this.addRefreshTask(RefreshTaskType.TaskList, null);

  Desktop.reportSuccess(Lang.Action.CreateTask.Done);
  this.terminate();
};

//----------------------------------------------------------------------
// Abort Task
//----------------------------------------------------------------------
function CGActionAbortTask() {
  this.base = CGAction;
  this.base(3);
  this.AvailableProcessClass = CGProcessCleanDirty;
  this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGActionAbortTask.prototype = new CGAction;
CGActionAbortTask.constructor = CGActionAbortTask;
CommandFactory.register(CGActionAbortTask, { Id: 0 }, false);

CGActionAbortTask.prototype.onFailure = function (sResponse) {
  this.getErrorMessage(Lang.Action.AbortTask.Failure, sResponse);
  this.terminate();
};

CGActionAbortTask.prototype.step_1 = function () {
  var Task = TasksCache.get(this.Id), DescTemplate;

  if (!Task) {
    this.terminate();
    return;
  }

  DescTemplate = new Template(Lang.ViewTaskList.DialogAbortTask.Description);
  Ext.MessageBox.confirm(Lang.ViewTaskList.DialogAbortTask.Title, DescTemplate.evaluate({'ImagesPath': Context.Config.ImagesPath, 'task': Task.getLabel()}), CGActionAbortTask.prototype.checkOption.bind(this));
};

CGActionAbortTask.prototype.step_2 = function () {
  Kernel.abortTask(this, this.Id);
};

CGActionAbortTask.prototype.step_3 = function () {
  var Process = new CGProcessRefreshTask();
  Process.Id = this.Id;
  Process.execute();
  Desktop.reportSuccess(Lang.Action.AbortTask.Done);
  this.terminate();
};

//----------------------------------------------------------------------
// Load More Task History Task
//----------------------------------------------------------------------
function CGActionLoadMoreTaskHistory() {
  this.base = CGAction;
  this.base(2);
  this.loading = false;
};

CGActionLoadMoreTaskHistory.prototype = new CGAction;
CGActionLoadMoreTaskHistory.constructor = CGActionLoadMoreTaskHistory;
CommandFactory.register(CGActionLoadMoreTaskHistory, { Id: 0 }, false);

CGActionLoadMoreTaskHistory.loadingHistory = false;

CGActionLoadMoreTaskHistory.prototype.onFailure = function (sResponse) {
  CGActionLoadMoreTaskHistory.loadingHistory = false;
  Desktop.reportError(sResponse);
  this.terminate();
};

CGActionLoadMoreTaskHistory.prototype.step_1 = function () {
  var ViewTask = Desktop.Main.Center.Body.getContainerView(VIEW_TASK, this.Id);

  if (!ViewTask || CGActionLoadMoreTaskHistory.loadingHistory) {
    this.terminate();
    return;
  }

  CGActionLoadMoreTaskHistory.loadingHistory = true;

  var DOMTask = ViewTask.getDOM();
  var start = DOMTask.getHistoryItemsCount();
  var limit = 5;
  Kernel.loadTaskHistory(this, this.Id, start, limit);
};

CGActionLoadMoreTaskHistory.prototype.step_2 = function () {
  var DOMTask = Desktop.Main.Center.Body.getContainerView(VIEW_TASK, this.Id).getDOM();
  var jsonData = Ext.util.JSON.decode(this.data);
  var DOMHistory = DOMTask.getDOMHistory();
  var Constructor = Extension.getTaskConstructor();
  DOMTask.addHistoryPage(jsonData.content, jsonData.hasMore);
  Constructor.init(DOMHistory);
  CGActionLoadMoreTaskHistory.loadingHistory = false;
  this.terminate();
};

//----------------------------------------------------------------------
// Alert Task
//----------------------------------------------------------------------
function CGActionAlertTask() {
  this.base = CGAction;
  this.base(3);
  this.AvailableProcessClass = CGProcessCleanDirty;
  this.RefreshProcessClass = CGProcessRefreshDOM;
  this.Dialog = null;
};

CGActionAlertTask.prototype = new CGAction;
CGActionAlertTask.constructor = CGActionAlertTask;
CommandFactory.register(CGActionAlertTask, { Id: 0 }, false);

CGActionAlertTask.prototype.onFailure = function (sResponse) {
  if (this.Dialog) this.Dialog.destroy();
  Desktop.reportError(sResponse);
  this.terminate();
};

CGActionAlertTask.prototype.step_1 = function () {
  var Task = TasksCache.get(this.Id);

  if (!Task) {
    this.terminate();
    return;
  }

  this.Dialog = new CGDialogAlertEntity();
  this.Dialog.init();
  this.Dialog.onAccept = this.execute.bind(this);
  this.Dialog.onCancel = this.resetState.bind(this);
  this.Dialog.show();
};

CGActionAlertTask.prototype.step_2 = function () {
  Kernel.alertEntity(this, this.Id, this.Dialog.UserList, this.Dialog.Message, MONET_LINK_TYPE_TASK);
  this.Dialog.destroy();
};

CGActionAlertTask.prototype.step_3 = function () {
  this.addRefreshTask(RefreshTaskType.TaskList, null);
  Desktop.reportSuccess(Lang.Action.AlertTask.Done);
  this.terminate();
};

//----------------------------------------------------------------------
// Select task delegation role
//----------------------------------------------------------------------
function CGActionSelectTaskDelegationRole() {
  this.base = CGAction;
  this.base(3);
  this.AvailableProcessClass = CGProcessCleanDirty;
  this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGActionSelectTaskDelegationRole.prototype = new CGAction;
CGActionSelectTaskDelegationRole.constructor = CGActionSelectTaskDelegationRole;
CommandFactory.register(CGActionSelectTaskDelegationRole, { Id: 0, RoleId: 1, RequireConfirmation : 2 }, false);

CGActionSelectTaskDelegationRole.prototype.onFailure = function (sResponse) {
  Desktop.reportError(sResponse);
  this.terminate();
};

CGActionSelectTaskDelegationRole.prototype.step_1 = function () {

  if (this.RequireConfirmation != null && this.RequireConfirmation != "") {
    Ext.MessageBox.confirm(Lang.ViewTask.DialogConfirmAction.Title, utf8Decode(this.RequireConfirmation), CGActionSelectTaskDelegationRole.prototype.checkOption.bind(this));
    return;
  }

  this.execute();
};

CGActionSelectTaskDelegationRole.prototype.step_2 = function () {
  this.Process = new CGProcessSelectTaskDelegationRole();
  this.Process.Id = this.Id;
  this.Process.RoleId = this.RoleId;
  this.Process.DOMItem = this.DOMItem;
  this.Process.ReturnProcess = this;
  this.Process.execute();
};

CGActionSelectTaskDelegationRole.prototype.step_3 = function () {
  if (this.Process.success()) {
    if (!this.Process.Cancel) Desktop.reportSuccess(Lang.Action.SelectTaskDelegationRole.Done);
  }
  else Desktop.reportError(this.Process.getFailure());
  this.terminate();
};

//----------------------------------------------------------------------
// Setup task delegation
//----------------------------------------------------------------------
function CGActionSetupTaskDelegation() {
  this.base = CGAction;
  this.base(4);
  this.AvailableProcessClass = CGProcessCleanDirty;
  this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGActionSetupTaskDelegation.prototype = new CGAction;
CGActionSetupTaskDelegation.constructor = CGActionSetupTaskDelegation;
CommandFactory.register(CGActionSetupTaskDelegation, { Id: 0, RequireConfirmation : 1 }, false);

CGActionSetupTaskDelegation.prototype.onFailure = function (sResponse) {
  Desktop.reportError(sResponse);
  this.terminate();
};

CGActionSetupTaskDelegation.prototype.step_1 = function () {
  this.DOMItemRef = this.DOMItem.href;
  this.DOMItem.href = "";

  if (this.RequireConfirmation != null && this.RequireConfirmation != "") {
    Ext.MessageBox.confirm(Lang.ViewTask.DialogConfirmAction.Title, utf8Decode(this.RequireConfirmation), CGActionSetupTaskDelegation.prototype.checkOption.bind(this));
    return;
  }

  this.execute();
};

CGActionSetupTaskDelegation.prototype.step_2 = function () {

  var ViewTask = Desktop.Main.Center.Body.getContainerView(VIEW_TASK, this.Id);
  if (ViewTask == null) {
    this.terminate();
    return;
  }

  var Process = new CGProcessSaveEmbeddedNodes();
  Process.ViewContainer = ViewTask;
  Process.ReturnProcess = this;
  Process.execute();
};

CGActionSetupTaskDelegation.prototype.step_3 = function () {
  this.Process = new CGProcessSetupTaskDelegation();
  this.Process.Id = this.Id;
  this.Process.DOMItem = this.DOMItem;
  this.Process.ReturnProcess = this;
  this.Process.execute();
};

CGActionSetupTaskDelegation.prototype.step_4 = function () {
  if (this.Process.success()) {
    if (!this.Process.Cancel) Desktop.reportSuccess(Lang.Action.SetupTaskDelegation.Done);
  }
  else {
    Desktop.reportError(this.Process.getFailure());
    this.DOMItem.href = this.DOMItemRef;
  }
  this.terminate();
};

//----------------------------------------------------------------------
// Select task send job role
//----------------------------------------------------------------------
function CGActionSelectTaskSendJobRole() {
  this.base = CGAction;
  this.base(3);
  this.AvailableProcessClass = CGProcessCleanDirty;
  this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGActionSelectTaskSendJobRole.prototype = new CGAction;
CGActionSelectTaskSendJobRole.constructor = CGActionSelectTaskSendJobRole;
CommandFactory.register(CGActionSelectTaskSendJobRole, { Id: 0, RoleId: 1, RequireConfirmation: 2 }, false);

CGActionSelectTaskSendJobRole.prototype.onFailure = function (sResponse) {
  Desktop.reportError(sResponse);
  this.terminate();
};

CGActionSelectTaskSendJobRole.prototype.step_1 = function () {

  if (this.RequireConfirmation != null && this.RequireConfirmation != "") {
    Ext.MessageBox.confirm(Lang.ViewTask.DialogConfirmAction.Title, utf8Decode(this.RequireConfirmation), CGActionSelectTaskSendJobRole.prototype.checkOption.bind(this));
    return;
  }

  this.execute();
};

CGActionSelectTaskSendJobRole.prototype.step_2 = function () {

  if (this.RoleId == "" || this.RoleId == "null")
    this.RoleId = null;

  this.Process = new CGProcessSelectTaskSendJobRole();
  this.Process.Id = this.Id;
  this.Process.RoleId = this.RoleId;
  this.Process.DOMItem = this.DOMItem;
  this.Process.ReturnProcess = this;
  this.Process.execute();
};

CGActionSelectTaskSendJobRole.prototype.step_3 = function () {
  if (this.Process.success()) {
    if (!this.Process.Cancel) Desktop.reportSuccess(Lang.Action.SelectTaskSendJobRole.Done);
  }
  else Desktop.reportError(this.Process.getFailure());
  this.terminate();
};

//----------------------------------------------------------------------
// Setup task send job
//----------------------------------------------------------------------
function CGActionSetupTaskSendJob() {
  this.base = CGAction;
  this.base(4);
  this.AvailableProcessClass = CGProcessCleanDirty;
  this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGActionSetupTaskSendJob.prototype = new CGAction;
CGActionSetupTaskSendJob.constructor = CGActionSetupTaskSendJob;
CommandFactory.register(CGActionSetupTaskSendJob, { Id: 0, RequireConfirmation: 1 }, false);

CGActionSetupTaskSendJob.prototype.onFailure = function (sResponse) {
  Desktop.reportError(sResponse);
  this.terminate();
};

CGActionSetupTaskSendJob.prototype.step_1 = function () {

  if (this.RequireConfirmation != null && this.RequireConfirmation != "") {
    Ext.MessageBox.confirm(Lang.ViewTask.DialogConfirmAction.Title, utf8Decode(this.RequireConfirmation), CGActionSetupTaskSendJob.prototype.checkOption.bind(this));
    return;
  }

  this.execute();
};

CGActionSetupTaskSendJob.prototype.step_2 = function () {

  var ViewTask = Desktop.Main.Center.Body.getContainerView(VIEW_TASK, this.Id);
  if (ViewTask == null) {
    this.terminate();
    return;
  }

  var Process = new CGProcessSaveEmbeddedNodes();
  Process.ViewContainer = ViewTask;
  Process.ReturnProcess = this;
  Process.execute();
};

CGActionSetupTaskSendJob.prototype.step_3 = function () {
  this.Process = new CGProcessSetupTaskSendJob();
  this.Process.Id = this.Id;
  this.Process.DOMItem = this.DOMItem;
  this.Process.ReturnProcess = this;
  this.Process.execute();
};

CGActionSetupTaskSendJob.prototype.step_4 = function () {
  if (this.Process.success()) {
    if (!this.Process.Cancel) Desktop.reportSuccess(Lang.Action.SetupTaskSendJob.Done);
  }
  else Desktop.reportError(this.Process.getFailure());
  this.terminate();
};

//----------------------------------------------------------------------
// Setup task enroll
//----------------------------------------------------------------------
function CGActionSetupTaskEnroll() {
  this.base = CGAction;
  this.base(3);
  this.AvailableProcessClass = CGProcessCleanDirty;
  this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGActionSetupTaskEnroll.prototype = new CGAction;
CGActionSetupTaskEnroll.constructor = CGActionSetupTaskEnroll;
CommandFactory.register(CGActionSetupTaskEnroll, { Id: 0, ContestId: 1 }, false);

CGActionSetupTaskEnroll.prototype.onFailure = function (sResponse) {
  Desktop.reportError(sResponse);
  this.terminate();
};

CGActionSetupTaskEnroll.prototype.step_1 = function () {

  if (this.RequireConfirmation != null && this.RequireConfirmation != "") {
    Ext.MessageBox.confirm(Lang.ViewTask.DialogConfirmAction.Title, utf8Decode(this.RequireConfirmation), CGActionSetupTaskEnroll.prototype.checkOption.bind(this));
    return;
  }

  this.execute();
};

CGActionSetupTaskEnroll.prototype.step_2 = function () {
  this.Process = new CGProcessSetupTaskEnroll();
  this.Process.Id = this.Id;
  this.Process.ContestId = this.ContestId;
  this.Process.DOMItem = this.DOMItem;
  this.Process.ReturnProcess = this;
  this.Process.execute();
};

CGActionSetupTaskEnroll.prototype.step_3 = function () {
  if (this.Process.success()) {
    if (!this.Process.Cancel) Desktop.reportSuccess(Lang.Action.SetupTaskEnroll.Done);
  }
  else Desktop.reportError(this.Process.getFailure());
  this.terminate();
};

//----------------------------------------------------------------------
// Setup task wait
//----------------------------------------------------------------------
function CGActionSetupTaskWait() {
  this.base = CGAction;
  this.base(3);
  this.AvailableProcessClass = CGProcessCleanDirty;
  this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGActionSetupTaskWait.prototype = new CGAction;
CGActionSetupTaskWait.constructor = CGActionSetupTaskWait;
CommandFactory.register(CGActionSetupTaskWait, { Id: 0, DateQuantity: 1, Command: 2, RequireConfirmation: 3 }, false);

CGActionSetupTaskWait.prototype.onFailure = function (sResponse) {
  Desktop.reportError(sResponse);
  this.terminate();
};

CGActionSetupTaskWait.prototype.step_1 = function () {

  if (this.RequireConfirmation != null && this.RequireConfirmation != "") {
    Ext.MessageBox.confirm(Lang.ViewTask.DialogConfirmAction.Title, utf8Decode(this.RequireConfirmation), CGActionSetupTaskWait.prototype.checkOption.bind(this));
    return;
  }

  this.execute();
};

CGActionSetupTaskWait.prototype.step_2 = function () {
  this.Process = new CGProcessSetupTaskWait();
  this.Process.Id = this.Id;
  this.Process.DateQuantity = this.DateQuantity;
  this.Process.Command = this.Command;
  this.Process.DOMItem = this.DOMItem;
  this.Process.ReturnProcess = this;
  this.Process.execute();
};

CGActionSetupTaskWait.prototype.step_3 = function () {
  if (this.Process.success()) {
    if (!this.Process.Cancel) Desktop.reportSuccess(Lang.Action.SetupTaskWait.Done);
  }
  else Desktop.reportError(this.Process.getFailure());
  this.terminate();
};

//----------------------------------------------------------------------
// Solve task line
//----------------------------------------------------------------------
function CGActionSolveTaskLine() {
  this.base = CGAction;
  this.base(3);
  this.AvailableProcessClass = CGProcessCleanDirty;
  this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGActionSolveTaskLine.prototype = new CGAction;
CGActionSolveTaskLine.constructor = CGActionSolveTaskLine;
CommandFactory.register(CGActionSolveTaskLine, { Id: 0, PlaceCode: 1, StopCode: 2, RequireConfirmation: 3 }, false);

CGActionSolveTaskLine.prototype.onFailure = function (sResponse) {
  Desktop.reportError(sResponse);
  this.terminate();
};

CGActionSolveTaskLine.prototype.step_1 = function () {
  this.DOMItemRef = this.DOMItem.href;
  this.DOMItem.href = "";

  if (this.RequireConfirmation != null && this.RequireConfirmation != "") {
    Ext.MessageBox.confirm(Lang.ViewTask.DialogConfirmAction.Title, utf8Decode(this.RequireConfirmation), CGActionSolveTaskLine.prototype.checkOption.bind(this));
    return;
  }

  this.execute();
};

CGActionSolveTaskLine.prototype.step_2 = function () {
  this.Process = new CGProcessSolveTaskLine();
  this.Process.Id = this.Id;
  this.Process.PlaceCode = this.PlaceCode;
  this.Process.StopCode = this.StopCode;
  this.Process.DOMItem = this.DOMItem;
  this.Process.ReturnProcess = this;
  this.Process.execute();
};

CGActionSolveTaskLine.prototype.step_3 = function () {
  if (this.Process.success()) {
    if (!this.Process.Cancel) Desktop.reportSuccess(Lang.Action.SolveTaskLine.Done);
  }
  else {
    Desktop.reportError(this.Process.getFailure());
    this.DOMItem.href = this.DOMItemRef;
  }
  this.terminate();
};

//----------------------------------------------------------------------
// Solve task edition
//----------------------------------------------------------------------
function CGActionSolveTaskEdition() {
  this.base = CGAction;
  this.base(4);
};

CGActionSolveTaskEdition.prototype = new CGAction;
CGActionSolveTaskEdition.constructor = CGActionSolveTaskEdition;
CommandFactory.register(CGActionSolveTaskEdition, { Id: 0, RequireConfirmation: 1 }, false);

CGActionSolveTaskEdition.prototype.onFailure = function (sResponse) {
  Desktop.reportError(sResponse);
  this.terminate();
};

CGActionSolveTaskEdition.prototype.step_1 = function () {
  this.DOMItemRef = this.DOMItem.href;
  this.DOMItem.href = "";

  if (this.RequireConfirmation != null && this.RequireConfirmation != "") {
    Ext.MessageBox.confirm(Lang.ViewTask.DialogConfirmAction.Title, utf8Decode(this.RequireConfirmation), CGActionSolveTaskEdition.prototype.checkOption.bind(this));
    return;
  }

  this.execute();
};

CGActionSolveTaskEdition.prototype.step_2 = function () {
  if (Ext.isGecko) window.setTimeout(CGActionSolveTaskEdition.prototype.execute.bind(this), 500);
  else this.execute();
};

CGActionSolveTaskEdition.prototype.step_3 = function () {
  this.Process = new CGProcessSolveTaskEdition();
  this.Process.Id = this.Id;
  this.Process.DOMItem = this.DOMItem;
  this.Process.ReturnProcess = this;
  this.Process.execute();
};

CGActionSolveTaskEdition.prototype.step_4 = function () {
  if (this.Process.success()) {
    if (!this.Process.Cancel) Desktop.reportSuccess(Lang.Action.SolveTaskEdition.Done);
  }
  else {
    Desktop.reportError(this.Process.getFailure());
    this.DOMItem.href = this.DOMItemRef;
  }
  this.terminate();
};

//----------------------------------------------------------------------
// Set task title
//----------------------------------------------------------------------
function CGActionSetTaskTitle() {
  this.base = CGAction;
  this.base(2);
};

CGActionSetTaskTitle.prototype = new CGAction;
CGActionSetTaskTitle.constructor = CGActionSetTaskTitle;
CommandFactory.register(CGActionSetTaskTitle, { Title: 0 }, false);

CGActionSetTaskTitle.prototype.onFailure = function (sResponse) {
  Desktop.reportError(Lang.Action.SetTaskTitle.Failure);
  this.terminate();
};

CGActionSetTaskTitle.prototype.step_1 = function () {

  if (this.Title == null) {
    this.terminate();
    return;
  }

  var CurrentTask = TasksCache.getCurrent();
  var ViewTask = Desktop.Main.Center.Body.getContainerView(VIEW_TASK, CurrentTask.getId());

  Kernel.setTaskTitle(CurrentTask.getId(), ViewTask.getDOM.getTitle());
};

CGActionSetTaskTitle.prototype.step_2 = function () {
  this.terminate();
};

//----------------------------------------------------------------------
// Show Task Tab
//----------------------------------------------------------------------
function CGActionShowTaskTab() {
  this.base = CGAction;
  this.base(1);
};

CGActionShowTaskTab.prototype = new CGAction;
CGActionShowTaskTab.constructor = CGActionShowTaskTab;
CommandFactory.register(CGActionShowTaskTab, { Tab: 0 }, false);

CGActionShowTaskTab.prototype.onFailure = function (sResponse) {
  Desktop.reportError(Lang.Action.ShowTaskTab.Failure);
  this.terminate();
};

CGActionShowTaskTab.prototype.step_1 = function () {
  var ViewTask = Desktop.Main.Center.Body.getContainerView(VIEW_TASK, TasksCache.getCurrent().getId());
  var DOMTask = ViewTask.getDOM();
  DOMTask.showTab(this.Tab);
  this.terminate();
};

//----------------------------------------------------------------------
// Set task urgency
//----------------------------------------------------------------------
function CGActionToggleTaskUrgency() {
  this.base = CGAction;
  this.base(2);
};

CGActionToggleTaskUrgency.prototype = new CGAction;
CGActionToggleTaskUrgency.constructor = CGActionToggleTaskUrgency;
CommandFactory.register(CGActionToggleTaskUrgency, { Task: 0 }, false);

CGActionToggleTaskUrgency.prototype.onFailure = function (sResponse) {
  Desktop.reportError(Lang.Action.ToggleTaskUrgency.Failure);
  this.terminate();
};

CGActionToggleTaskUrgency.prototype.step_1 = function () {
  Kernel.toggleTaskUrgency(this, this.Task);
};

CGActionToggleTaskUrgency.prototype.step_2 = function () {
  var extItem = Ext.get(this.DOMItem);

  extItem.removeClass(CLASS_ACTIVE);

  if (this.data == "true")
    extItem.addClass(CLASS_ACTIVE);

  this.terminate();
};

//----------------------------------------------------------------------
// Set task owner
//----------------------------------------------------------------------
function CGActionSetTaskOwner() {
  this.base = CGAction;
  this.base(3);
};

CGActionSetTaskOwner.prototype = new CGAction;
CGActionSetTaskOwner.constructor = CGActionSetTaskOwner;
CommandFactory.register(CGActionSetTaskOwner, { Task: 0 }, false);

CGActionSetTaskOwner.prototype.saveReason = function () {
	if (this.dialog == null) return;
	State.SetTaskOwnerReason = this.dialog.Reason;
};

CGActionSetTaskOwner.prototype.destroy = function (sResponse) {
  this.resetState();
  if (this.dialogLayerId != null) $(this.dialogLayerId).remove();
};

CGActionSetTaskOwner.prototype.cancel = function() {
	this.saveReason();
	this.destroy();
};

CGActionSetTaskOwner.prototype.onFailure = function (sResponse) {
  this.saveReason();
  Desktop.reportError(Lang.Action.SetTaskOwner.Failure);
  if (this.dialogLayerId != null) $(this.dialogLayerId).remove();
  this.terminate();
};

CGActionSetTaskOwner.prototype.step_1 = function () {
  var extItem = Ext.get(this.DOMItem);
  this.dialogLayerId = Ext.id();
  new Insertion.After(extItem.dom, "<div class='dialog embedded' id='" + this.dialogLayerId + "'></div>");

  this.dialog = new CGDialogSelectTaskOwner();
  this.dialog.target = { reason: State.SetTaskOwnerReason };
  this.dialog.onAccept = this.execute.bind(this);
  this.dialog.onCancel = this.destroy.bind(this);
  this.dialog.init(this.dialogLayerId);
  this.dialog.show();
};

CGActionSetTaskOwner.prototype.step_2 = function () {
  $(this.dialogLayerId).remove();
  State.SetTaskOwnerReason = null;
  Kernel.setTasksOwner(this, this.dialog.User, this.dialog.Reason, this.Task);
};

CGActionSetTaskOwner.prototype.step_3 = function () {
  CommandListener.throwCommand("refreshtask(" + this.Task + ")");
  this.terminate();
};

//----------------------------------------------------------------------
// Unset task owner
//----------------------------------------------------------------------
function CGActionUnsetTaskOwner() {
  this.base = CGAction;
  this.base(3);
};

CGActionUnsetTaskOwner.prototype = new CGAction;
CGActionUnsetTaskOwner.constructor = CGActionUnsetTaskOwner;
CommandFactory.register(CGActionUnsetTaskOwner, { Task: 0, ParentClass: 1 }, false);

CGActionUnsetTaskOwner.prototype.onFailure = function (sResponse) {
  Desktop.reportError(Lang.Action.UnsetTaskOwner.Failure);
  this.terminate();
};

CGActionUnsetTaskOwner.prototype.step_1 = function () {
  Ext.MessageBox.confirm(Lang.ViewTask.DialogUnsetTaskOwner.Title, Lang.ViewTask.DialogUnsetTaskOwner.Description, CGActionUnsetTaskOwner.prototype.checkOption.bind(this));
};

CGActionUnsetTaskOwner.prototype.step_2 = function () {
  Kernel.unsetTaskOwner(this, this.Task);
};

CGActionUnsetTaskOwner.prototype.step_3 = function () {
  var extItem = Ext.get(this.DOMItem);

  extItem.up("." + this.ParentClass).removeClass(CLASS_ACTIVE);

  var Task = TasksCache.getCurrent();
  if (Task != null && Task.getId() == this.Task)
    CommandListener.throwCommand("refreshtask(" + this.Task + ")");

  this.terminate();
};

//----------------------------------------------------------------------
// Render Task Order List
//----------------------------------------------------------------------
function CGActionRenderTaskOrderList() {
  this.base = CGAction;
  this.base(2);
  this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionRenderTaskOrderList.prototype = new CGAction;
CGActionRenderTaskOrderList.constructor = CGActionRenderTaskOrderList;
CommandFactory.register(CGActionRenderTaskOrderList, { Id: 0, IdDOMViewerLayer: 1, IdDOMViewerLayerOptions: 2 }, false);

CGActionRenderTaskOrderList.prototype.atBoundItem = function (Sender, Item) {

  if (Item.modified == null || !Item.modified) {
    if (Item.type == "customer") Item.label = Lang.ViewTask.CustomerLabel + " " + Item.label;
    Item.createDate = getFormattedDateTime(parseServerDate(Item.createDate), Context.Config.Language, false);
    Item.modified = true;
  }

  var Dummy = Item;
  for (var index in Dummy) {
    if (isFunction(Dummy[index]))
      continue;
    Item[index + "_short"] = shortValue(Dummy[index]);
    if (Item[index] == "true") Item[index] = Item[index + "_short"] = Lang.Response.Yes;
    else if (Item[index] == "false") Item[index] = Item[index + "_short"] = Lang.Response.No;
    try {
      Item[index + "_length"] = Dummy[index].length;
    } catch (e) {
    }
  }

  Item.newMessagesClass = Item.newMessagesCount > 0 ? "active" : "";
};

CGActionRenderTaskOrderList.prototype.atShowItem = function (Sender, Item) {
  Kernel.resetTaskOrderNewMessages(Item.idTask, Item.id);
  Item.newMessagesCount = 0;
  State.TaskOrderListViewer.updateItem(Item);
  CommandListener.dispatchCommand("showtaskorderchat(" + Item.idTask + "," + Item.id + "," + Item.closed + ")");
};

CGActionRenderTaskOrderList.prototype.destroyViewer = function () {
  if (State.TaskOrderListViewer == null) return;
  State.registerListViewerState("task" + this.Id, State.TaskOrderListViewer.getState());
  State.TaskOrderListViewer.dispose();
  $(Literals.ListViewerWizard).innerHTML = "";
};

CGActionRenderTaskOrderList.prototype.createViewer = function () {
  var Options;

  this.destroyViewer();

  eval($(this.IdDOMViewerLayerOptions).innerHTML);
  State.TaskOrderListViewer = new CGListViewer(Options, Context.Config.Language, Context.Config.ImagesPath);
  State.TaskOrderListViewer.setDataSourceUrls(Kernel.getTaskOrderItemsLink(this.Id), null);
  State.TaskOrderListViewer.setWizardLayer(Literals.ListViewerWizard);
  State.TaskOrderListViewer.onBoundItem = CGActionRenderTaskOrderList.prototype.atBoundItem.bind(this);
  State.TaskOrderListViewer.onShowItem = CGActionRenderTaskOrderList.prototype.atShowItem.bind(this);
  State.TaskOrderListViewer.setState(State.getListViewerState("task" + this.Id));
  State.TaskOrderListViewer.render(this.IdDOMViewerLayer);
};

CGActionRenderTaskOrderList.prototype.step_1 = function () {

  if ((this.IdDOMViewerLayer == null) || (this.IdDOMViewerLayerOptions == null)) {
    this.terminate();
    return;
  }

  var Process = new CGProcessLoadHelperListViewer();
  Process.ReturnProcess = this;
  Process.execute();
};

CGActionRenderTaskOrderList.prototype.step_2 = function () {
  this.createViewer();
  this.terminate();
};

//----------------------------------------------------------------------
// Action show task order chat
//----------------------------------------------------------------------
function CGActionShowTaskOrderChat() {
  this.base = CGAction;
  this.base(2);
};

CGActionShowTaskOrderChat.prototype = new CGAction;
CGActionShowTaskOrderChat.constructor = CGActionShowTaskOrderChat;
CommandFactory.register(CGActionShowTaskOrderChat, { Id: 0, IdOrder: 1, closed: 2 }, false);

CGActionShowTaskOrderChat.prototype.step_1 = function () {
  var Process = new CGProcessLoadHelperChatViewer();
  Process.ReturnProcess = this;
  Process.execute();
};

CGActionShowTaskOrderChat.prototype.step_2 = function () {
  if (this.closed == "true") this.closed = true;
  else if (this.closed == "false") this.closed = false;
  ViewerHelperChat.target = { loader: Kernel.getTaskOrderChatItemsLink(this.Id, this.IdOrder), idTask: this.Id, idOrder: this.IdOrder, closed: this.closed };
  ViewerHelperChat.refresh();
};

//----------------------------------------------------------------------
// Action send task order chat message
//----------------------------------------------------------------------
function CGActionSendTaskOrderChatMessage() {
  this.base = CGAction;
  this.base(3);
};

CGActionSendTaskOrderChatMessage.prototype = new CGAction;
CGActionSendTaskOrderChatMessage.constructor = CGActionSendTaskOrderChatMessage;
CommandFactory.register(CGActionSendTaskOrderChatMessage, { Id: 0, IdOrder: 1, Message: 2 }, false);

CGActionSendTaskOrderChatMessage.prototype.failure = function () {
};

CGActionSendTaskOrderChatMessage.prototype.step_1 = function () {
  Kernel.sendTaskOrderChatMessage(this, this.Id, this.IdOrder, this.Message);
};

CGActionSendTaskOrderChatMessage.prototype.step_2 = function () {
  var Process = new CGProcessLoadHelperChatViewer();
  Process.ReturnProcess = this;
  Process.execute();
};

CGActionSendTaskOrderChatMessage.prototype.step_3 = function () {
  ViewerHelperChat.refresh();
};

//----------------------------------------------------------------------
// Action send task request
//----------------------------------------------------------------------
function CGActionSendTaskRequest() {
	this.base = CGAction;
	this.base(3);
};

CGActionSendTaskRequest.prototype = new CGAction;
CGActionSendTaskRequest.constructor = CGActionSendTaskRequest;
CommandFactory.register(CGActionSendTaskRequest, { Id: 0 }, false);

CGActionSendTaskRequest.prototype.failure = function () {
};

CGActionSendTaskRequest.prototype.step_1 = function () {
	Kernel.sendTaskRequest(this, this.Id);
	this.terminate();
};

//----------------------------------------------------------------------
// Action send task response
//----------------------------------------------------------------------
function CGActionSendTaskResponse() {
	this.base = CGAction;
	this.base(3);
};

CGActionSendTaskResponse.prototype = new CGAction;
CGActionSendTaskResponse.constructor = CGActionSendTaskResponse;
CommandFactory.register(CGActionSendTaskResponse, { Id: 0 }, false);

CGActionSendTaskResponse.prototype.failure = function () {
};

CGActionSendTaskResponse.prototype.step_1 = function () {
	Kernel.sendTaskResponse(this, this.Id);
	this.terminate();
};

//----------------------------------------------------------------------
// Action load task info
//----------------------------------------------------------------------
function CGActionLoadTaskComments() {
  this.base = CGAction;
  this.base(3);
};

CGActionLoadTaskComments.prototype = new CGAction;
CGActionLoadTaskComments.constructor = CGActionLoadTaskComments;
CommandFactory.register(CGActionLoadTaskComments, { Id: 0 }, false);

CGActionLoadTaskComments.prototype.failure = function () {
};

CGActionLoadTaskComments.prototype.hideDialog = function () {
  if (this.DOMItem.dialog != null)
    this.DOMItem.dialog.hide();
};

CGActionLoadTaskComments.prototype.step_1 = function () {
  if (this.DOMItem.dialog != null)
    this.gotoStep(3);
  else
    Kernel.loadTaskComments(this, this.Id);
};

CGActionLoadTaskComments.prototype.step_2 = function () {
  this.DOMItem.dialog = new CGDialogTaskComments();
  this.DOMItem.dialog.Target = { comments: this.data };
  this.DOMItem.dialog.init();

  Event.observe(this.DOMItem, "mouseout", CGActionLoadTaskComments.prototype.hideDialog.bind(this));

  this.execute();
};

CGActionLoadTaskComments.prototype.step_3 = function () {
  var extItem = Ext.get(this.DOMItem);
  var positionAtTop = (window.innerHeight - extItem.getTop()) < 200;
  var y = positionAtTop ? extItem.getTop() - this.DOMItem.dialog.getHeight() - 20 : extItem.getTop();

  this.DOMItem.dialog.Target.position = { x: extItem.getLeft(), y: y };
  this.DOMItem.dialog.refresh();
  this.DOMItem.dialog.show();

  this.terminate();
};