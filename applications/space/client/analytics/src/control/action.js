function Action(numStates) {
  this.base = Process;
  this.state = 0;
  this.numStates = numStates;
  this.loadingMessage = "";
  this.executed = false;
  this.availableProcessClass = null;
};

Action.prototype = new Process;
Action.constructor = Action;

Action.prototype.resetState = function(){
  this.state = 0;
  Desktop.hideReports();
};

Action.prototype.terminate = function(){
  this.state = this.numStates;
  this.execute();
};

Action.prototype.available = function(){
  if (this.availableProcessClass == null) return true;

  if (this.availableProcess) {
    if (this.availableProcess.success()) { return true; }
    else {
      this.terminate();
      return false;
    }
  }

  this.availableProcess = new this.availableProcessClass;
  this.availableProcess.returnProcess = this;
  this.availableProcess.execute();

  return false;
};

Action.prototype.execute = function(){
  if (this.terminated) return;

  if (this.isFirstStep()) {
    if (! this.available()) return;
  }

  if (this.isLastStep()) {
    this.terminated = true;
    this.doRefreshTaskList();
    if (this.returnProcess) this.returnProcess.execute();
    if (this.onTerminate) this.onTerminate();
    return;
  }

  var State = this.getNextState();    
  try { 
    this.method = State.nextMethod;
    this.method();
  }
  catch(e){ 
    Kernel.registerException(e);
  }
   
};

Action.prototype.onFailure = function(response){
  response = response.substring(response.indexOf(":")+1);

  if (this.returnProcess) {
    this.returnProcess.onFailure(response);
    this.terminate();
  }
  else Desktop.reportError(response);

  this.terminate();
};