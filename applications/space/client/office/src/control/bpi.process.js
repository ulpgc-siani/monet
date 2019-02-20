//----------------------------------------------------------------------
// BPI Process
//----------------------------------------------------------------------
function CGBPIProcess(iNumSteps) {
  this.base = CGProcess;
  this.base(iNumSteps);
};

CGBPIProcess.prototype = new CGProcess;
CGBPIProcess.constructor = CGBPIProcess;

//----------------------------------------------------------------------
// Process Dispatch
//----------------------------------------------------------------------
function CGProcessBPIDispatch() {
  this.base = CGProcess;
  this.base(2);
};

CGProcessBPIDispatch.prototype = new CGProcess;
CGProcessBPIDispatch.constructor = CGProcessBPIDispatch;

CGProcessBPIDispatch.prototype.onFailure = function (sResponse) {
  this.terminateOnFailure(sResponse);
};

CGProcessBPIDispatch.prototype.step_1 = function () {
  if (this.Process == null) return;

  this.Process.ReturnProcess = this;
  this.Process.execute();
};

CGProcessBPIDispatch.prototype.step_2 = function () {
  this.Callback(this.Process.Result);
  this.terminateOnSuccess();
};