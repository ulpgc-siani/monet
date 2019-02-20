//----------------------------------------------------------------------
// Load Account
//----------------------------------------------------------------------
function CGProcessLoadAccount() {
  this.base = CGProcess;
  this.base(2);
};

CGProcessLoadAccount.prototype = new CGProcess;
CGProcessLoadAccount.constructor = CGProcessLoadAccount;

CGProcessLoadAccount.prototype.step_1 = function () {
  Kernel.loadAccount(this, Date.getCurrentTimeZone());
};

CGProcessLoadAccount.prototype.step_2 = function () {
  Account.unserialize(this.data);

  ViewUser.setTarget(Account);
  ViewUser.refresh();

  BehaviourDispatcher.apply(BehaviourViewTaskList, Literals.Views.TaskList);

  this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Save Account
//----------------------------------------------------------------------
function CGProcessSaveAccount() {
  this.base = CGProcess;
  this.base(2);
};

CGProcessSaveAccount.prototype = new CGProcess;
CGProcessSaveAccount.constructor = CGProcessSaveAccount;

CGProcessSaveAccount.prototype.step_1 = function () {

  if (this.Account == null) {
    this.terminateOnFailure();
    return;
  }

  Kernel.saveAccount(this, this.Account);
};

CGProcessSaveAccount.prototype.step_2 = function () {
  this.terminateOnSuccess();
};