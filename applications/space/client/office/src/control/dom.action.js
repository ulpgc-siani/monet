//----------------------------------------------------------------------
// Add observer
//----------------------------------------------------------------------
function CGActionAddObserver() {
  this.base = CGAction;
  this.base(1);
};

CGActionAddObserver.prototype = new CGAction;
CGActionAddObserver.constructor = CGActionAddObserver;
CommandFactory.register(CGActionAddObserver, {id: 0, fullname: 1, field: 2}, false);

CGActionAddObserver.prototype.step_1 = function () {
  var helper = ViewerSidebar.getHelper(Helper.OBSERVERS);
  var Observers = helper.getTarget(this.Observers);
  var DOMNode;
  var Observer = new Object();
  var bFound = false;

  if (State.CurrentView == null) {
    this.terminate();
    return;
  }

  DOMNode = State.CurrentView.getDOM();
  Observer.id = this.id;
  Observer.fullname = this.fullname;
  Observer.field = this.field;

  for (var i = 0; i < Observers.length; i++) {
    if (Observers[i].id == this.id) {
      bFound = true;
      break;
    }
  }

  if (!bFound) {
    Observers.push(Observer);
    DOMNode.addObserver(Observer, Observers.length - 1);

    var Process = new CGProcessRefreshHelperObservers();
    Process.Observers = Observers;
    Process.execute();
  }

  this.terminate();
};

//----------------------------------------------------------------------
// Refresh observer
//----------------------------------------------------------------------
function CGActionRefreshObserver() {
  this.base = CGAction;
  this.base(1);
};

CGActionRefreshObserver.prototype = new CGAction;
CGActionRefreshObserver.constructor = CGActionRefreshObserver;
CommandFactory.register(CGActionRefreshObserver, {id: 0, fullname: 1, field: 2}, false);

CGActionRefreshObserver.prototype.step_1 = function () {
  var helper = ViewerSidebar.getHelper(Helper.OBSERVERS);
  var Observers = helper.getTarget(this.Observers);
  var DOMNode;
  var Observer = new Object();
  var bFound = false;

  if (State.CurrentView == null) {
    this.terminate();
    return;
  }

  DOMNode = State.CurrentView.getDOM();
  Observer.id = this.id;
  Observer.fullname = this.fullname;
  Observer.field = this.field;

  for (var i = 0; i < Observers.length; i++) {
    if (Observers[i].id == this.id) {
      bFound = true;
      break;
    }
  }

  if (bFound) {
    DOMNode.removeObserver(Observers[i].field);
    DOMNode.addObserver(Observer, i);
    Observers[i] = Observer;
  }

  var Process = new CGProcessRefreshHelperObservers();
  Process.Observers = Observers;
  Process.execute();

  this.terminate();
};

//----------------------------------------------------------------------
// Remove observer
//----------------------------------------------------------------------
function CGActionRemoveObserver() {
  this.base = CGAction;
  this.base(1);
};

CGActionRemoveObserver.prototype = new CGAction;
CGActionRemoveObserver.constructor = CGActionRemoveObserver;
CommandFactory.register(CGActionRemoveObserver, {id: 0, fullname: 1, field: 2}, false);

CGActionRemoveObserver.prototype.step_1 = function () {
  var helper = ViewerSidebar.getHelper(Helper.OBSERVERS);
  var Observers = helper.getTarget(this.Observers);
  var HelperObservers = new Array();
  var DOMNode;

  if (State.CurrentView == null) {
    this.terminate();
    return;
  }

  DOMNode = State.CurrentView.getDOM();
  DOMNode.removeObserver(this.field);

  for (var i = 0; i < Observers.length; i++) {
    if (Observers[i].id == this.id) continue;
    HelperObservers.push(Observers[i]);
  }

  var Process = new CGProcessRefreshHelperObservers();
  Process.Observers = HelperObservers;
  Process.execute();

  this.terminate();
};

//----------------------------------------------------------------------
// Refresh view
//----------------------------------------------------------------------
function CGActionUpdateView() {
  this.base = CGAction;
  this.base(1);
  this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGActionUpdateView.prototype = new CGAction;
CGActionUpdateView.constructor = CGActionUpdateView;
CommandFactory.register(CGActionUpdateView, {type: 0, targetId: 1, data: 2, message: 3}, false);

CGActionUpdateView.prototype.step_1 = function () {
  var aViews = new Array();

  if (this.type == "node") aViews = Desktop.Main.Center.Body.getViews(VIEW_NODE, VIEW_NODE_TYPE_NODE, this.targetId);
  else aViews = Desktop.Main.Center.Body.getViews(VIEW_TASK, VIEW_TASK_TYPE_TASK, this.targetId);

  for (var i = 0; i < aViews.length; i++) {
    var DOMView = aViews[i].getDOM();
    var ControlInfo = DOMView.getControlInfo();
    var bUpdated = DOMView.update(this.data) || (this.data == null && Desktop.Main.Center.Body.isContainerView(aViews[i]));

    if (!bUpdated) {
      if (this.type == "node") {
        var Process = new CGProcessShowNode();
        Process.Id = ControlInfo.IdNode;
        Process.Mode = ControlInfo.Templates.Refresh;
        Process.ViewNode = aViews[i];
        Process.ActivateNode = true;
        Process.execute();
      }
    }

    if (this.type == "node") {
      var Node = new CGNode();
      Node.setId(ControlInfo.IdNode);
      Node.setCode(ControlInfo.Code);
      this.addRefreshTask(RefreshTaskType.References, Node);
    }
  }

  if (this.message != null)
    Desktop.reportWarning(this.message);

  this.terminate();
};

//----------------------------------------------------------------------
// Blur node
//----------------------------------------------------------------------
function CGActionBlurNode() {
  this.base = CGAction;
  this.base(1);
};

CGActionBlurNode.prototype = new CGAction;
CGActionBlurNode.constructor = CGActionBlurNode;
CommandFactory.register(CGActionBlurNode, {id: 0, fullname: 1}, false);

CGActionBlurNode.prototype.step_1 = function () {
  this.terminate();
};

//----------------------------------------------------------------------
// Blur node field
//----------------------------------------------------------------------
function CGActionBlurNodeField() {
  this.base = CGAction;
  this.base(1);
};

CGActionBlurNodeField.prototype = new CGAction;
CGActionBlurNodeField.constructor = CGActionBlurNodeField;
CommandFactory.register(CGActionBlurNodeField, {id: 0, fullname: 1, field: 2}, false);

CGActionBlurNodeField.prototype.step_1 = function () {
  var DOMNode;

  if (State.CurrentView == null) {
    this.terminate();
    return;
  }

  DOMNode = State.CurrentView.getDOM();
  DOMNode.blurField(this.field);

  this.terminate();
};

//----------------------------------------------------------------------
// Update node state
//----------------------------------------------------------------------
function CGActionUpdateNodeState() {
  this.base = CGAction;
  this.base(1);
  this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGActionUpdateNodeState.prototype = new CGAction;
CGActionUpdateNodeState.constructor = CGActionUpdateNodeState;
CommandFactory.register(CGActionUpdateNodeState, null, false);

CGActionUpdateNodeState.prototype.step_1 = function () {
  var process = new CGProcessRefreshNodeState();
  process.nodeId = this.node;
  process.state = this.state;
  process.execute();
};

//----------------------------------------------------------------------
// Update task state
//----------------------------------------------------------------------
function CGActionUpdateTaskState() {
  this.base = CGAction;
  this.base(1);
  this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGActionUpdateTaskState.prototype = new CGAction;
CGActionUpdateTaskState.constructor = CGActionUpdateTaskState;
CommandFactory.register(CGActionUpdateTaskState, null, false);

CGActionUpdateTaskState.prototype.step_1 = function () {
  var process = new CGProcessRefreshTaskState();
  process.taskId = this.task;
  process.execute();

  new CGProcessRefreshAccountPendingTasks().execute();
};

//----------------------------------------------------------------------
// Update task owner
//----------------------------------------------------------------------
function CGActionUpdateTaskOwner() {
	this.base = CGAction;
	this.base(1);
	this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGActionUpdateTaskOwner.prototype = new CGAction;
CGActionUpdateTaskOwner.constructor = CGActionUpdateTaskOwner;
CommandFactory.register(CGActionUpdateTaskOwner, null, false);

CGActionUpdateTaskOwner.prototype.step_1 = function () {
	new CGProcessRefreshAccountPendingTasks().execute();
};

//----------------------------------------------------------------------
// Refresh task order chat
//----------------------------------------------------------------------
function CGActionRefreshTaskOrderChat() {
  this.base = CGAction;
  this.base(2);
};

CGActionRefreshTaskOrderChat.prototype = new CGAction;
CGActionRefreshTaskOrderChat.constructor = CGActionRefreshTaskOrderChat;
CommandFactory.register(CGActionRefreshTaskOrderChat, null, false);

CGActionRefreshTaskOrderChat.prototype.step_1 = function () {
  var CurrentViewer = State.TaskOrderListViewer;

  if (CurrentViewer == null) {
    this.terminate();
    return;
  }

  this.activeItem = CurrentViewer.getActiveItem();
  CurrentViewer.onRefresh = window.setTimeout(CGActionRefreshTaskOrderChat.prototype.execute.bind(this), 400);
  CurrentViewer.refresh();
};

CGActionRefreshTaskOrderChat.prototype.step_2 = function () {

  if (this.activeItem == null) {
    this.terminate();
    return;
  }

  var CurrentViewer = State.TaskOrderListViewer;
  CurrentViewer.activateItem(this.activeItem.id);
  this.terminate();
};