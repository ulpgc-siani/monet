function CGAction (iNumStates) {
  this.iState = 0;
  this.iNumStates = iNumStates;
  this.sLoadingMessage = "";
  this.bExecuted = false;
}

CGAction.prototype = new CGProcess;
CGAction.constructor = CGAction;

CGAction.prototype.addRefreshTask = function(Type, Target, Sender) {
  RefreshTask = new CGRefreshTask(Type, Target);
  if (Sender != null) RefreshTask.setSender(Sender);
  RefreshTaskList.addRefreshTask(RefreshTask);
};

CGAction.prototype.refreshDOM = function() {
  ActionRefreshDOM = new CGActionRefreshDOM();
  ActionRefreshDOM.execute();
};

CGAction.prototype.resetState = function(){
  this.iState = 0;
  Desktop.hideReports();
};

CGAction.prototype.execute = function(){
  if (this.bTerminated) return;

  if (this.isLastState()) {
    this.bTerminated = true;
    Desktop.hideReports();
    if (this.ReturnProcess) this.ReturnProcess.execute();
    return;
  }

  var State = this.getNextState();    
  try { 
    this.method = State.NextMethod;
    this.method();
  }
  catch(e){ 
    InternalException(e,Object.inspect(this), State.sMessage);
  }
   
};

CGAction.prototype.onFailure = function(sResponse){
  sResponse = sResponse.substring(sResponse.indexOf(":")+1);
  if (this.ReturnProcess) this.ReturnProcess.onFailure(sResponse);
  else Desktop.reportError(sResponse);
};