//----------------------------------------------------------------------
// Refresh Tasks
//----------------------------------------------------------------------
function CGProcessRefreshTasks() {
  this.base = CGProcess;
  this.base(1);
};

CGProcessRefreshTasks.prototype = new CGProcess;
CGProcessRefreshTasks.constructor = CGProcessRefreshTasks;

CGProcessRefreshTasks.prototype.step_1 = function () {
  var aTasks = TasksCache.getAll();

  for (var iPos = 0; iPos < aTasks.length; iPos++) {
    var Process = new CGProcessRefreshTask();
    Process.Id = aTasks[iPos].getId();
    Process.execute();
  }

  this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Refresh account pending tasks
//----------------------------------------------------------------------
function CGProcessRefreshAccountPendingTasks() {
    this.base = CGProcess;
    this.base(2);
};

CGProcessRefreshAccountPendingTasks.prototype = new CGProcess;
CGProcessRefreshAccountPendingTasks.constructor = CGProcessRefreshAccountPendingTasks;

CGProcessRefreshAccountPendingTasks.prototype.step_1 = function () {
    Kernel.loadAccountPendingTasks(this);
}

CGProcessRefreshAccountPendingTasks.prototype.step_2 = function () {
    var jsonData = Ext.util.JSON.decode(this.data);
    Account.PendingTasks = jsonData;
    this.refreshFurnitureSet(false);
}