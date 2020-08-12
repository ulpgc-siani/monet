//----------------------------------------------------------------------
// Show task
//----------------------------------------------------------------------
function CGProcessShowTask() {
	this.base = CGProcess;
	this.base(3);
	this.ActivateTask = true;
	this.DOMViewActiveTab = null;
};

CGProcessShowTask.prototype = new CGProcess;
CGProcessShowTask.constructor = CGProcessShowTask;

CGProcessShowTask.prototype.createViewTask = function (Task) {
	var IdTab = Desktop.Main.Center.Body.addTab(VIEW_TASK, {Id: Task.getId(), Background: !this.ActivateTask});
	return Desktop.createView($(IdTab), Task, null, this.Mode, true);
};

CGProcessShowTask.prototype.loadEmbeddedNodes = function () {
	var Process = new CGProcessLoadEmbeddedNodes();
	Process.ViewContainer = this.ViewTask;
	Process.LoadHelperPage = true;
	Process.execute();
};

CGProcessShowTask.prototype.reload = function () {
	var sLocation = Context.Config.EnterpriseLoginUrl;
	if (sLocation == "") sLocation = Context.Config.Host;
	reload(sLocation);
};

CGProcessShowTask.prototype.step_1 = function () {

	if (this.ViewTask == null) {
		var ViewTask = Desktop.Main.Center.Body.getContainerView(VIEW_TASK, this.Id);
		if (ViewTask != null) {
			if (((this.Mode == null) || (ViewTask.getMode() == this.Mode)) && (this.ActivateTask)) {
				var Process = new CGProcessActivateTask();
				Process.Id = this.Id;
				Process.execute();
				this.terminate();
				return;
			}
			else {
				ViewTask.destroy();
				ViewTask = null;
			}
		}
		if (ViewTask == null) {
			if (!this.Task) {
				this.Task = new CGTask();
				this.Task.setId(this.Id);
			}
			this.ViewTask = this.createView(this.Task);
		}
	}
	else {
		if ((!this.DOMViewActiveTab) && (this.ViewTask) && (this.ViewTask.getDOM) && (this.ViewTask.getDOM().getActiveTab)) this.DOMViewActiveTab = this.ViewTask.getDOM().getActiveTab();
	}

	if (!this.Task) Kernel.loadTask(this, this.Id, this.Mode);
	else this.gotoStep(3);
};

CGProcessShowTask.prototype.step_2 = function () {

	this.Task = new CGTask();
	this.Task.unserialize(this.data);
	TasksCache.register(this.Task);

	this.ViewTask.setTarget(this.Task);
	this.ViewTask.refresh();

	this.execute();
};

CGProcessShowTask.prototype.step_3 = function () {

	if (this.Task.isInitializer() && this.Task.State == TASK_STATE_FINISHED) {
		Desktop.reportSuccess(Lang.Information.Updated);
		window.setTimeout(CGProcessShowTask.prototype.reload.bind(this), 3000);
		return;
	}

	Desktop.Main.Center.Header.refresh();
	if (Desktop.Main.Center.Body.existsTab(VIEW_TASK, this.Task.getId())) {
		Desktop.Main.Center.Body.updateTab(VIEW_TASK, this.Task.getId(), this.Task.getLabel());
	}

	if (this.ActivateTask) {
		var Process = new CGProcessActivateTask();
		Process.Id = this.Task.getId();
		Process.DOMViewActiveTab = this.DOMViewActiveTab;
		Process.execute();
	}

	this.loadEmbeddedNodes();

	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Load embedded task
//----------------------------------------------------------------------
function CGProcessLoadEmbeddedTask() {
	this.base = CGProcess;
	this.base(2);
};

CGProcessLoadEmbeddedTask.prototype = new CGProcess;
CGProcessLoadEmbeddedTask.constructor = CGProcessLoadEmbeddedTask;

CGProcessLoadEmbeddedTask.prototype.step_1 = function () {
	var ControlInfo, Task = null;
	var DOMTask = this.DOMTask;

	if (DOMTask == null) DOMTask = this.DOMItem;

	if (!DOMTask.getControlInfo) {
		this.execute();
		return;
	}

	ControlInfo = DOMTask.getControlInfo();
	this.ViewTask = Desktop.Main.Center.Body.getView(VIEW_TASK, DOMTask.IdView);

	if (this.ViewContainer == null) this.ViewContainer = Desktop.Main.Center.Body.getContainerView(VIEW_TASK, TasksCache.getCurrent().getId());

	if (!this.ViewTask) {
		Task = new CGTask();
		Task.setId(ControlInfo.IdTask);
		this.ViewTask = Desktop.createView(DOMTask, Task, this.ViewContainer, null, true);
	}

	if (this.ViewTask.getDOM().isLoaded()) {
		this.execute();
		return;
	}

	var Process = new CGProcessShowTask();
	Process.Id = Task.getId();
	Process.Mode = ControlInfo.Templates.Refresh;
	Process.ViewTask = this.ViewTask;
	Process.ActivateTask = false;
	Process.ReturnProcess = this;
	Process.execute();
};

CGProcessLoadEmbeddedTask.prototype.step_2 = function () {
	if (this.onFinish) this.onFinish();
	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Activate task
//----------------------------------------------------------------------
function CGProcessActivateTask() {
	this.base = CGProcess;
	this.base(2);
	this.RefreshTask = false;
};

CGProcessActivateTask.prototype = new CGProcess;
CGProcessActivateTask.constructor = CGProcessActivateTask;

CGProcessActivateTask.prototype.step_1 = function () {
	var Task = null;
	var ViewTask;

	if (!(Task = TasksCache.get(this.Id))) {
		this.terminateOnFailure();
		return;
	}

	if ((ViewTask = Desktop.Main.Center.Body.getContainerView(VIEW_TASK, Task.getId())) == null) {
		this.terminateOnFailure();
		return;
	}

	TasksCache.setCurrent(this.Id);

	Desktop.Main.Center.Header.refresh();
	Desktop.Footer.refresh();

	Desktop.Main.Center.Body.disableNotifications();
	Desktop.Main.Center.Body.activateTab(VIEW_TASK, this.Id);
	Desktop.Main.Center.Body.enableNotifications();

	this.DOMViewActiveTab = ViewTask.getDOM().getActiveTab();

	if (this.RefreshTask) {
		var Process = new CGProcessRefreshTask();
		Process.ActivateTab = false;
		Process.ReturnProcess = this;
		Process.execute();
	}
	else this.execute();

};

CGProcessActivateTask.prototype.step_2 = function () {

	if (!(Task = TasksCache.get(this.Id))) {
		this.terminateOnFailure();
		return;
	}

	if ((ViewTask = Desktop.Main.Center.Body.getContainerView(VIEW_TASK, Task.getId())) == null) {
		this.terminateOnFailure();
		return;
	}

	ViewTask.show();
	DOMTask = ViewTask.getDOM();
	if (this.DOMViewActiveTab) DOMTask.activateTab(this.DOMViewActiveTab);
	else DOMTask.activateDefaultTab();

	EventManager.notify(EventManager.OPEN_TASK, {"Task": Task, "DOMTask": ViewTask.getDOM()});

	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Refresh Task
//----------------------------------------------------------------------
function CGProcessRefreshTask() {
	this.base = CGProcess;
	this.base(2);
	this.ActivateTab = true;
	this.DOMViewActiveTab = null;
};

CGProcessRefreshTask.prototype = new CGProcess;
CGProcessRefreshTask.constructor = CGProcessRefreshTask;

CGProcessRefreshTask.prototype.step_1 = function () {
	var ViewTask, Process, Task = TasksCache.get(this.Id);

	if (!Task) Task = TasksCache.getCurrent();
	if (!Task) {
		this.terminate();
		return;
	}

	this.Id = Task.getId();

	if ((State.aRefreshingTasks[this.Id] != null) && (State.aRefreshingTasks[this.Id])) {
		this.terminate();
		return;
	}

	if (this.View == null) this.View = Desktop.Main.Center.Body.getContainerView(VIEW_TASK, Task.getId());
	if (!this.View) {
		this.terminate();
		return;
	}

	if ((!this.DOMViewActiveTab) && (this.View) && (this.View.getDOM) && (this.View.getDOM().getActiveTab)) this.DOMViewActiveTab = this.View.getDOM().getActiveTab();

	Process = new CGProcessShowTask();
	Process.Id = this.Id;
	Process.Mode = this.View.getMode();
	Process.ViewTask = this.View;
	Process.ActivateTask = false;
	Process.ReturnProcess = this;
	Process.execute();
};

CGProcessRefreshTask.prototype.step_2 = function () {
	var ViewTask = Desktop.Main.Center.Body.getContainerView(VIEW_TASK, this.Id);

	if (!ViewTask) {
		this.terminate();
		return;
	}

	if (this.ActivateTab && this.DOMViewActiveTab) ViewTask.getDOM().activateTab(this.DOMViewActiveTab);
	this.addRefreshTask(RefreshTaskType.TaskList, null);
	State.aRefreshingTasks[this.Id] = false;
	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Refresh Task State
//----------------------------------------------------------------------
function CGProcessRefreshTaskState() {
	this.base = CGProcess;
	this.base(1);
};

CGProcessRefreshTaskState.prototype = new CGProcess;
CGProcessRefreshTaskState.constructor = CGProcessRefreshTaskState;

CGProcessRefreshTaskState.prototype.step_1 = function () {

	var task = TasksCache.get(this.taskId);
	if (!task) {
		this.terminate();
		return;
	}

	this.refreshView(Desktop.Main.Center.Body.getContainerView(VIEW_TASK, task.getId()), true);

	var aViews = Desktop.Main.Center.Body.getViews(VIEW_TASK, VIEW_TASK_TYPE_TASK, task.getId());
	for (var i=0; i<aViews.length; i++) this.refreshView(aViews[i], false);

	this.terminate();
};

CGProcessRefreshTaskState.prototype.refreshView = function(viewTask, checkStateTab) {
	if (!viewTask) {
		this.terminate();
		return;
	}

	if (checkStateTab && !viewTask.getDOM().isStateTabActive()) {
		this.terminate();
		return;
	}

	var process = new CGProcessRefreshTask();
	process.Id = this.taskId;
	process.View = viewTask;
	process.execute();
};

//----------------------------------------------------------------------
// Process refresh task tab
//----------------------------------------------------------------------
function CGProcessRefreshTaskTab() {
	this.base = CGAction;
	this.base(1);
};

CGProcessRefreshTaskTab.prototype = new CGProcess;
CGProcessRefreshTaskTab.constructor = CGProcessRefreshTaskTab;

CGProcessRefreshTaskTab.prototype.step_1 = function () {

	var viewTask = Desktop.Main.Center.Body.getView(VIEW_TASK, this.DOMTask.IdView);
	if (viewTask == null) {
		this.terminate();
		return;
	}

	var process = new CGProcessShowTask();
	process.Id = this.Id;
	process.Mode = this.DOMTask.getControlInfo().Templates.Refresh;
	process.ViewTask = viewTask;
	process.ActivateNode = false;
	process.execute();
};

//----------------------------------------------------------------------
// Close task
//----------------------------------------------------------------------
function CGProcessCloseTask() {
	this.base = CGProcess;
	this.base(1);
};

CGProcessCloseTask.prototype = new CGProcess;
CGProcessCloseTask.constructor = CGProcessCloseTask;

CGProcessCloseTask.prototype.step_1 = function () {
	var Task = TasksCache.get(this.Id);

	if ((ViewTask = Desktop.Main.Center.Body.getContainerView(VIEW_TASK, this.Id)) == null) {
		this.terminateOnSuccess();
		return;
	}

	EventManager.notify(EventManager.CLOSE_TASK, {"Task": Task, "DOMTask": ViewTask.getDOM()});

	ViewTask.destroy();
	Desktop.Main.Center.Body.deleteView(VIEW_TASK, ViewTask.getId());
	Desktop.Main.Center.Body.deleteTab(VIEW_TASK, this.Id);
	TasksCache.unregister(this.Id);

	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Do Task AttachNode
//----------------------------------------------------------------------
function CGProcessDoTaskAttachNode() {
	this.base = CGProcess;
	this.base(1);
};

CGProcessDoTaskAttachNode.prototype = new CGProcess;
CGProcessDoTaskAttachNode.constructor = CGProcessDoTaskAttachNode;

CGProcessDoTaskAttachNode.prototype.onFailure = function (sResponse) {
	Desktop.reportError(Lang.Process.DoTaskAttachNode.Failure);
	this.terminateOnFailure(sResponse);
};

CGProcessDoTaskAttachNode.prototype.step_1 = function () {
	var IdInput, DOMElement, Action;
	var Task = Account.TaskList.getTask(this.Id);

	if (!Task) {
		this.terminate();
		return;
	}

	var ViewTask = Desktop.Main.Center.Body.getContainerView(VIEW_TASK, this.Id);
	if (ViewTask == null) {
		this.terminate();
		return false;
	}

	DOMElement = Extension.getDOMNodeForm(this.DOMItem);
	if (DOMElement != null) {
		Action = new CGActionCopyNode();
		var ControlInfo = DOMElement.getControlInfo();
		Action.Id = ControlInfo.IdNode;
		Action.Mode = ControlInfo.Templates.Refresh;
	}
	else {
		DOMElement = ViewTask.getDOM().getNode(Task.IdTarget);
		if (DOMElement == null) {
			this.terminate();
			return;
		}
		Action = new CGActionCopyNodes();
		Action.DOMElement = DOMElement;
	}

	IdInput = Task.IdInput;
	if (IdInput == -1) {
		var ViewRoot = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, Account.getUser().getRootNode().id);
		if (ViewRoot != null) IdInput = Extension.getTaskIdInput(ViewRoot.getDOM());
		else IdInput = null;
	}

	if (IdInput == null) {
		this.terminateOnFailure();
		return;
	}

	Action.IdParent = IdInput;
	Action.execute();

	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Do Task Share
//----------------------------------------------------------------------
function CGProcessDoTaskShare() {
	this.base = CGProcess;
	this.base(1);
};

CGProcessDoTaskShare.prototype = new CGProcess;
CGProcessDoTaskShare.constructor = CGProcessDoTaskShare;

CGProcessDoTaskShare.prototype.onFailure = function (sResponse) {
	Desktop.reportError(Lang.Process.DoTaskShare.Failure);
	this.terminateOnFailure(sResponse);
};

CGProcessDoTaskShare.prototype.step_1 = function () {
	var IdInput, DOMElement, Action;
	var Task = Account.TaskList.getTask(this.Id);

	if (!Task) {
		this.terminate();
		return;
	}

	var ViewTask = Desktop.Main.Center.Body.getContainerView(VIEW_TASK, this.Id);
	if (ViewTask == null) {
		this.terminate();
		return false;
	}

	Action = new CGActionCopyNode();

	DOMElement = Extension.getDOMNodeForm(this.DOMItem);
	if (DOMElement == null) {
		DOMElement = ViewTask.getDOM().getNode(Task.IdTarget);
		if (DOMElement == null) {
			this.terminate();
			return;
		}
	}

	var ControlInfo = DOMElement.getControlInfo();
	Action.Id = ControlInfo.IdNode;
	Action.Mode = ControlInfo.Templates.Refresh;

	IdInput = Task.IdInput;
	if (IdInput == -1) {
		var ViewRoot = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, Account.getUser().getRootNode().id);
		if (ViewRoot != null) IdInput = Extension.getTaskIdInput(ViewRoot.getDOM());
		else IdInput = null;
	}

	if (IdInput == null) {
		this.terminateOnFailure();
		return;
	}

	Action.IdParent = IdInput;
	Action.execute();

	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Do Task Revision
//----------------------------------------------------------------------
function CGProcessDoTaskRevision() {
	this.base = CGProcess;
	this.base(2);
};

CGProcessDoTaskRevision.prototype = new CGProcess;
CGProcessDoTaskRevision.constructor = CGProcessDoTaskRevision;

CGProcessDoTaskRevision.prototype.onFailure = function (sResponse) {
	Desktop.reportError(Lang.Process.DoTaskRevision.Failure);
	this.terminateOnFailure(sResponse);
};

CGProcessDoTaskRevision.prototype.step_1 = function () {
	var Task = Account.TaskList.getTask(this.Id);

	if (!Task) {
		this.terminate();
		return;
	}

	var ViewTask = Desktop.Main.Center.Body.getContainerView(VIEW_TASK, this.Id);
	if (ViewTask == null) {
		this.terminate();
		return false;
	}

	var aDOMEmbeddedNodes = ViewTask.getDOM().getEmbeddedNodes();
	var DOMEmbeddedNode = aDOMEmbeddedNodes[0];

	if (DOMEmbeddedNode == null) {
		this.execute();
		return;
	}

	if (DOMEmbeddedNode.IdView == null) {
		this.execute();
		return;
	}

	var ViewNode = Desktop.Main.Center.Body.getView(VIEW_NODE, DOMEmbeddedNode.IdView);
	var Process = new CGProcessSaveNode();
	Process.Id = ViewNode.getTarget().Id;
	Process.ReturnProcess = this;
	Process.execute();
};

CGProcessDoTaskRevision.prototype.step_2 = function () {
	Desktop.reportSuccess(Lang.Process.DoTaskRevision.Done);
	var Process = new CGProcessCloseTask();
	Process.Id = this.Id;
	Process.execute();
	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Show task node
//----------------------------------------------------------------------
function CGProcessShowTaskNode() {
	this.base = CGProcess;
	this.base(2);
};

CGProcessShowTaskNode.prototype = new CGProcess;
CGProcessShowTaskNode.constructor = CGProcessShowTaskNode;

CGProcessShowTaskNode.prototype.step_1 = function () {
	var ActionShowNode = new CGActionShowNode();
	ActionShowNode.Id = this.IdNode;
	ActionShowNode.ReturnProcess = this;
	ActionShowNode.execute();
};

CGProcessShowTaskNode.prototype.step_2 = function () {
	var ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, this.IdNode);

	State.TaskNode = {IdNode: this.IdNode, TargetNode: this.TargetNode, TargetView: this.TargetView, IdTask: this.IdTask};
	ViewNode.getDOM().showBackTaskCommand(this.IdTask, this.TargetNode, this.TargetView);

	this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Select task delegation role
//----------------------------------------------------------------------
function CGProcessSelectTaskDelegationRole() {
	this.base = CGProcess;
	this.base(2);
};

CGProcessSelectTaskDelegationRole.prototype = new CGProcess;
CGProcessSelectTaskDelegationRole.constructor = CGProcessSelectTaskDelegationRole;

CGProcessSelectTaskDelegationRole.prototype.onFailure = function (sResponse) {
	Desktop.hideProgress();
	this.terminateOnFailure(sResponse);
};

CGProcessSelectTaskDelegationRole.prototype.step_1 = function () {
	Desktop.reportProgress(Lang.Desktop.Starting, true);
	Kernel.selectTaskDelegationRole(this, this.Id, this.RoleId);
};

CGProcessSelectTaskDelegationRole.prototype.step_2 = function () {
	var ViewTask = Desktop.Main.Center.Body.getContainerView(VIEW_TASK, this.Id);

	var Process = new CGProcessShowTask();
	Process.Id = this.Id;
	Process.Mode = ViewTask.getMode();
	Process.ViewTask = ViewTask;
	Process.execute();

	this.addRefreshTask(RefreshTaskType.TaskList, null);
	this.terminateOnSuccess();
	Desktop.hideProgress();
};

//----------------------------------------------------------------------
// Setup task delegation
//----------------------------------------------------------------------
function CGProcessSetupTaskDelegation() {
	this.base = CGProcess;
	this.base(2);
};

CGProcessSetupTaskDelegation.prototype = new CGProcess;
CGProcessSetupTaskDelegation.constructor = CGProcessSetupTaskDelegation;

CGProcessSetupTaskDelegation.prototype.onFailure = function (sResponse) {
	Desktop.hideProgress();
	this.terminateOnFailure(sResponse);
};

CGProcessSetupTaskDelegation.prototype.step_1 = function () {
	Desktop.reportProgress(Lang.Desktop.Starting, true);
	Kernel.setupTaskDelegation(this, this.Id);
};

CGProcessSetupTaskDelegation.prototype.step_2 = function () {
	var ViewTask = Desktop.Main.Center.Body.getContainerView(VIEW_TASK, this.Id);

	var Process = new CGProcessShowTask();
	Process.Id = this.Id;
	Process.Mode = ViewTask.getMode();
	Process.ViewTask = ViewTask;
	Process.execute();

	this.addRefreshTask(RefreshTaskType.TaskList, null);
	this.terminateOnSuccess();
	Desktop.hideProgress();
};

//----------------------------------------------------------------------
// Select task send job role
//----------------------------------------------------------------------
function CGProcessSelectTaskSendJobRole() {
	this.base = CGProcess;
	this.base(2);
};

CGProcessSelectTaskSendJobRole.prototype = new CGProcess;
CGProcessSelectTaskSendJobRole.constructor = CGProcessSelectTaskSendJobRole;

CGProcessSelectTaskSendJobRole.prototype.onFailure = function (sResponse) {
	Desktop.hideProgress();
	this.terminateOnFailure(sResponse);
};

CGProcessSelectTaskSendJobRole.prototype.step_1 = function () {
	Desktop.reportProgress(Lang.Desktop.Starting, true);
	Kernel.selectTaskSendJobRole(this, this.Id, this.RoleId);
};

CGProcessSelectTaskSendJobRole.prototype.step_2 = function () {
	var ViewTask = Desktop.Main.Center.Body.getContainerView(VIEW_TASK, this.Id);

	var Process = new CGProcessShowTask();
	Process.Id = this.Id;
	Process.Mode = ViewTask.getMode();
	Process.ViewTask = ViewTask;
	Process.execute();

	this.addRefreshTask(RefreshTaskType.TaskList, null);
	this.terminateOnSuccess();
	Desktop.hideProgress();
};

//----------------------------------------------------------------------
// Setup task send job
//----------------------------------------------------------------------
function CGProcessSetupTaskSendJob() {
	this.base = CGProcess;
	this.base(2);
};

CGProcessSetupTaskSendJob.prototype = new CGProcess;
CGProcessSetupTaskSendJob.constructor = CGProcessSetupTaskSendJob;

CGProcessSetupTaskSendJob.prototype.onFailure = function (sResponse) {
	Desktop.hideProgress();
	this.terminateOnFailure(sResponse);
};

CGProcessSetupTaskSendJob.prototype.step_1 = function () {
	Desktop.reportProgress(Lang.Desktop.Starting, true);
	Kernel.setupTaskSendJob(this, this.Id);
};

CGProcessSetupTaskSendJob.prototype.step_2 = function () {
	var ViewTask = Desktop.Main.Center.Body.getContainerView(VIEW_TASK, this.Id);

	var Process = new CGProcessShowTask();
	Process.Id = this.Id;
	Process.Mode = ViewTask.getMode();
	Process.ViewTask = ViewTask;
	Process.execute();

	this.addRefreshTask(RefreshTaskType.TaskList, null);
	this.terminateOnSuccess();
	Desktop.hideProgress();
};

//----------------------------------------------------------------------
// Setup task enroll
//----------------------------------------------------------------------
function CGProcessSetupTaskEnroll() {
	this.base = CGProcess;
	this.base(2);
};

CGProcessSetupTaskEnroll.prototype = new CGProcess;
CGProcessSetupTaskEnroll.constructor = CGProcessSetupTaskEnroll;

CGProcessSetupTaskEnroll.prototype.onFailure = function (sResponse) {
	Desktop.hideProgress();
	this.terminateOnFailure(sResponse);
};

CGProcessSetupTaskEnroll.prototype.step_1 = function () {
	Desktop.reportProgress(Lang.Desktop.Starting, true);
	Kernel.setupTaskEnroll(this, this.Id, this.ContestId);
};

CGProcessSetupTaskEnroll.prototype.step_2 = function () {
	var ViewTask = Desktop.Main.Center.Body.getContainerView(VIEW_TASK, this.Id);

	var Process = new CGProcessShowTask();
	Process.Id = this.Id;
	Process.Mode = ViewTask.getMode();
	Process.ViewTask = ViewTask;
	Process.execute();

	this.addRefreshTask(RefreshTaskType.TaskList, null);
	this.terminateOnSuccess();
	Desktop.hideProgress();
};

//----------------------------------------------------------------------
// Setup task wait
//----------------------------------------------------------------------
function CGProcessSetupTaskWait() {
	this.base = CGProcess;
	this.base(2);
};

CGProcessSetupTaskWait.prototype = new CGProcess;
CGProcessSetupTaskWait.constructor = CGProcessSetupTaskWait;

CGProcessSetupTaskWait.prototype.onFailure = function (sResponse) {
	Desktop.hideProgress();
	this.terminateOnFailure(sResponse);
};

CGProcessSetupTaskWait.prototype.step_1 = function () {
	Desktop.reportProgress(Lang.Desktop.Starting, true);
	Kernel.setupTaskWait(this, this.Id, this.DateQuantity, this.Command);
};

CGProcessSetupTaskWait.prototype.step_2 = function () {
	var ViewTask = Desktop.Main.Center.Body.getContainerView(VIEW_TASK, this.Id);

	var Process = new CGProcessShowTask();
	Process.Id = this.Id;
	Process.Mode = ViewTask.getMode();
	Process.ViewTask = ViewTask;
	Process.execute();

	this.addRefreshTask(RefreshTaskType.TaskList, null);
	this.terminateOnSuccess();
	Desktop.hideProgress();
};

//----------------------------------------------------------------------
// Solve task line
//----------------------------------------------------------------------
function CGProcessSolveTaskLine() {
	this.base = CGProcess;
	this.base(2);
};

CGProcessSolveTaskLine.prototype = new CGProcess;
CGProcessSolveTaskLine.constructor = CGProcessSolveTaskLine;

CGProcessSolveTaskLine.prototype.onFailure = function (sResponse) {
	Desktop.hideProgress();
	this.terminateOnFailure(sResponse);
};

CGProcessSolveTaskLine.prototype.step_1 = function () {
	Desktop.reportProgress(Lang.Desktop.Starting, true);
	Kernel.solveTaskLine(this, this.Id, this.PlaceCode, this.StopCode);
};

CGProcessSolveTaskLine.prototype.step_2 = function () {
	var ViewTask = Desktop.Main.Center.Body.getContainerView(VIEW_TASK, this.Id);

	if (ViewTask != null && ViewTask.getDOM() != null && ViewTask.getDOM().clearStateView)
		ViewTask.getDOM().clearStateView();

	this.terminateOnSuccess();
	Desktop.hideProgress();
};

//----------------------------------------------------------------------
// Solve task edition
//----------------------------------------------------------------------
function CGProcessSolveTaskEdition() {
	this.base = CGProcess;
	this.base(2);
};

CGProcessSolveTaskEdition.prototype = new CGProcess;
CGProcessSolveTaskEdition.constructor = CGProcessSolveTaskEdition;

CGProcessSolveTaskEdition.prototype.onFailure = function (sResponse) {
	Desktop.hideProgress();
	this.terminateOnFailure(sResponse);
};

CGProcessSolveTaskEdition.prototype.step_1 = function () {
	Desktop.reportProgress(Lang.Desktop.Starting, true);
	Kernel.solveTaskEdition(this, this.Id);
};

CGProcessSolveTaskEdition.prototype.step_2 = function () {
	var ViewTask = Desktop.Main.Center.Body.getContainerView(VIEW_TASK, this.Id);

	var Process = new CGProcessShowTask();
	Process.Id = this.Id;
	Process.Mode = ViewTask.getMode();
	Process.ViewTask = ViewTask;
	Process.execute();

	this.addRefreshTask(RefreshTaskType.TaskList, null);
	this.terminateOnSuccess();
	Desktop.hideProgress();
};

//----------------------------------------------------------------------
// Set Task Owner
//----------------------------------------------------------------------
function CGProcessSetTaskOwner() {
	this.base = CGProcess;
	this.base(3);
};

CGProcessSetTaskOwner.prototype = new CGProcess;
CGProcessSetTaskOwner.constructor = CGProcessSetTaskOwner;

CGProcessSetTaskOwner.prototype.saveReason = function () {
	if (this.dialog == null) return;
	State.SetTaskOwnerReason = this.dialog.Reason;
};

CGProcessSetTaskOwner.prototype.destroy = function (sResponse) {
  this.resetState();
  if (this.dialogLayerId != null) $(this.dialogLayerId).remove();
};

CGProcessSetTaskOwner.prototype.cancel = function() {
	this.saveReason();
	this.destroy();
};

CGProcessSetTaskOwner.prototype.onFailure = function (sResponse) {
  this.saveReason();
  Desktop.reportError(Lang.Action.SetTaskOwner.Failure);
  if (this.dialogLayerId != null) $(this.dialogLayerId).remove();
  this.terminateOnFailure();
};

CGProcessSetTaskOwner.prototype.step_1 = function () {
  var extItem = Ext.get(this.DOMItem);
  this.dialogLayerId = Ext.id();
  new Insertion.After(extItem.dom, "<div class='dialog embedded' id='" + this.dialogLayerId + "'></div>");

  this.dialog = new CGDialogSelectTaskOwner();
  this.dialog.target = { reason: State.SetTaskOwnerReason };
  this.dialog.onAccept = this.execute.bind(this);
  this.dialog.onCancel = this.destroy.bind(this);
  this.dialog.init(this.dialogLayerId);
  this.dialog.show();
}

CGProcessSetTaskOwner.prototype.step_2 = function () {
  $(this.dialogLayerId).remove();
  State.SetTaskOwnerReason = null;
  Kernel.setTasksOwner(this, this.dialog.User, this.dialog.Reason, this.Task);
};

CGProcessSetTaskOwner.prototype.step_3 = function () {
  this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Unset task owner
//----------------------------------------------------------------------
function CGProcessUnsetTaskOwner() {
  this.base = CGProcess;
  this.base(3);
};

CGProcessUnsetTaskOwner.prototype = new CGProcess;
CGProcessUnsetTaskOwner.constructor = CGProcessUnsetTaskOwner;

CGProcessUnsetTaskOwner.prototype.onFailure = function (sResponse) {
  Desktop.reportError(Lang.Action.UnsetTaskOwner.Failure);
  this.terminateOnFailure();
};

CGProcessUnsetTaskOwner.prototype.step_1 = function () {
  Ext.MessageBox.confirm(Lang.ViewTask.DialogUnsetTaskOwner.Title, Lang.ViewTask.DialogUnsetTaskOwner.Description, CGProcessUnsetTaskOwner.prototype.checkOption.bind(this));
};

CGProcessUnsetTaskOwner.prototype.step_2 = function () {
  Kernel.unsetTaskOwner(this, this.Task);
};

CGProcessUnsetTaskOwner.prototype.step_3 = function () {
  this.terminateOnSuccess();
};
