//----------------------------------------------------------------------
// Save Account
//----------------------------------------------------------------------
function ProcessSaveAccount () {
  this.base = Process;
  this.base(2);
};

ProcessSaveAccount.prototype = new Process;
ProcessSaveAccount.constructor = ProcessSaveAccount;

ProcessSaveAccount.prototype.step_1 = function() {
  Kernel.saveAccount(this, this.account);
};

ProcessSaveAccount.prototype.step_2 = function() {
  this.terminateOnSuccess();
};