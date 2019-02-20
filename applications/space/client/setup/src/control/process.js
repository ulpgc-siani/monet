function CGProcess(iNumStates) {
  this.iState = 0;
  this.iNumStates = iNumStates;
  this.bTerminated = false;
}

CGProcess.prototype.isFirstState = function(){
  return this.iState == 0;
};

CGProcess.prototype.isLastState = function(){
  return this.iState == this.iNumStates;
};

CGProcess.prototype.stepState = function(steps){
  if (steps == null) steps = 1;
  this.iState += steps;
  this.execute();
};

CGProcess.prototype.gotoState = function(iState){
  if (iState > this.iNumStates) return false;
  this.iState = iState - 1;
  this.execute();
};

CGProcess.prototype.resetState = function(){
  this.iState = 0;
};

CGProcess.prototype.restart = function(){
  this.iState = 0;
  this.execute();
};

CGProcess.prototype.terminate = function(){
  this.iState = this.iNumStates;
  this.execute();
};

CGProcess.prototype.execute = function(){
  if (this.bTerminated) return;

  if (this.isLastState()) {
    this.bTerminated = true;
    if (this.ReturnTask) this.ReturnTask.execute();
    return;
  }

  var State = this.getNextState();
    
  try { 
    this.method = State.NextMethod;
    this.method();
  }
  catch(e){ 
    InternalException(e,Object.inspect(this),State.sMessage);
  }
   
};

CGProcess.prototype.getNextState = function(){
  if (this.iState > this.iNumStates) return false;

  var iState = this.sState;
  this.iState++;


  var State = {
    iProgress: Math.round((iState/this.iNumStates)*100)/100,
    NextMethod: this["step_" + this.iState]
  };

  return State;
};

CGProcess.prototype.onSuccess = function(){
  this.execute();
};

CGProcess.prototype.getErrorMessage = function(sMessage, sFailure){
  return sMessage.replace("#response#", sFailure.substr(sFailure.indexOf(":")+1));
};

CGProcess.prototype.onFailure = function(sFailure){
  if (this.ReturnTask) this.ReturnTask.onFailure(sFailure);
  else Ext.MessageBox.alert(Lang.Exceptions.Title, sFailure);
};
