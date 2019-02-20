function CGProcess(iNumStates) {
  this.iState = 0;
  this.iNumStates = iNumStates;
  this.bTerminated = false;
  this.bSuccess = false;
  this.sFailure = "";
  this.RefreshTaskList = null;
};

CGProcess.prototype.checkOption = function (ButtonResult) {
  if (ButtonResult == BUTTON_RESULT_YES) {
    this.execute();
  }
  else this.terminate();
};

CGProcess.prototype.isFirstStep = function () {
  return this.iState == 0;
};

CGProcess.prototype.isLastStep = function () {
  return this.iState == this.iNumStates;
};

CGProcess.prototype.gotoStep = function (iState) {
  if (iState > this.iNumStates) return;
  this.iState = iState - 1;
  this.execute();
};

CGProcess.prototype.resetState = function () {
  this.iState = 0;
};

CGProcess.prototype.restart = function () {
  this.iState = 0;
  this.execute();
};

CGProcess.prototype.terminate = function () {
  this.iState = this.iNumStates;
  this.execute();
};

CGProcess.prototype.terminateOnSuccess = function () {
  this.bSuccess = true;
  this.terminate();
};

CGProcess.prototype.terminateOnFailure = function (sFailure) {
  if (sFailure != null) this.sFailure = sFailure;
  this.bSuccess = false;
  this.terminate();
};

CGProcess.prototype.getFailure = function () {
  return this.sFailure;
};

CGProcess.prototype.success = function () {
  return this.bSuccess;
};

CGProcess.prototype.addRefreshTask = function (Type, Target, Sender) {
  if (this.RefreshTaskList == null) this.RefreshTaskList = new CGRefreshTaskList();
  RefreshTask = new CGRefreshTask(Type, Target);
  if (Sender != null) RefreshTask.setSender(Sender);
  this.RefreshTaskList.addRefreshTask(RefreshTask);
};

CGProcess.prototype.doRefreshTaskList = function () {
  if (this.RefreshProcessClass == null) return true;
  if (this.RefreshTaskList == null) return true;
  this.RefreshProcess = new this.RefreshProcessClass;
  this.RefreshProcess.RefreshTaskList = this.RefreshTaskList;
  this.RefreshProcess.execute();
};

CGProcess.prototype.execute = function () {
  if (this.bTerminated) return;

  if (this.isLastStep()) {
    this.bTerminated = true;
    this.doRefreshTaskList();
    if (this.ReturnProcess) this.ReturnProcess.execute();
    return;
  }

  var State = this.getNextState();
  try {
    this.method = State.NextMethod;
    this.method();
  }
  catch (e) {
    Kernel.registerException(e);
  }

};

CGProcess.prototype.getNextState = function () {
  if (this.iState > this.iNumStates) return false;

  var iState = this.sState;
  this.iState++;


  var State = {
    iProgress: Math.round((iState / this.iNumStates) * 100) / 100,
    NextMethod: this["step_" + this.iState]
  };

  return State;
};

CGProcess.prototype.onSuccess = function () {
  this.execute();
};

CGProcess.prototype.generateMessage = function (sMessage, Variables) {
  var template = new Template(sMessage);
  return template.evaluate(Variables);
};

CGProcess.prototype.getErrorMessage = function (sMessage, sFailure) {
  sFailure = (sFailure != null) ? sFailure.substr(sFailure.indexOf(":") + 1) : "";
  return sMessage.replace("#response#", sFailure);
};

CGProcess.prototype.onFailure = function (sFailure) {
  alert(sFailure);
  this.terminate();
};

CGProcess.prototype.setActiveFurniture = function (type, id) {
  State.ActiveFurniture = { type: type, id: id };
  this.refreshFurnitureSet(false);
};

CGProcess.prototype.refreshFurnitureSet = function (banner) {
  ViewFurnitureSet.setTarget({ account: Account, banner: banner});
  ViewFurnitureSet.refresh();
};

CGProcess.prototype.loadMainNode = function (id) {
  if (State.MainNodes[id] != null) {
    this.data = State.MainNodes[id];
    this.execute();
  }
  else Kernel.loadMainNode(this, id);
};

CGProcess.prototype.setMainNode = function (id, mainNode) {
  State.MainNodes[id] = mainNode;
};