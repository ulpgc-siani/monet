function CGAction(iNumStates) {
  this.base = CGProcess;
  this.iState = 0;
  this.iNumStates = iNumStates;
  this.sLoadingMessage = "";
  this.bExecuted = false;
  this.AvailableProcessClass = null;
};

CGAction.prototype = new CGProcess;
CGAction.constructor = CGAction;

CGAction.prototype.checkOption = function (ButtonResult) {
  if (ButtonResult == BUTTON_RESULT_YES) {
    this.execute();
  }
  else this.terminate();
};

CGAction.prototype.resetState = function () {
  this.iState = 0;
  Desktop.hideReports();
};

CGAction.prototype.terminate = function () {
  this.iState = this.iNumStates;
  this.execute();
};

CGAction.prototype.available = function () {
  if (this.AvailableProcessClass == null) return true;

  if (this.AvailableProcess) {
    if (this.AvailableProcess.success()) {
      return true;
    }
    else {
      this.terminate();
      return false;
    }
  }

  this.AvailableProcess = new this.AvailableProcessClass;
  this.AvailableProcess.ReturnProcess = this;
  this.AvailableProcess.execute();

  return false;
};

CGAction.prototype.execute = function () {
  if (this.bTerminated) return;

  if (this.isFirstStep()) {
    if (!this.available()) return;
  }

  if (this.isLastStep()) {
    this.bTerminated = true;
    this.doRefreshTaskList();
    if (this.ReturnProcess) this.ReturnProcess.execute();
    if (this.onTerminate) this.onTerminate();
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

CGAction.prototype.onFailure = function (sResponse) {
  sResponse = sResponse.substring(sResponse.indexOf(":") + 1);

  if (this.ReturnProcess) {
    this.ReturnProcess.onFailure(sResponse);
    this.terminate();
  }
  else Desktop.reportError(sResponse);

  this.terminate();
};

CGAction.prototype.getAttributeSerialization = function (attribute, path) {
  var pathArray = path.split(Context.Config.AttributePathSeparator);
  var result = null;

  if (pathArray.length <= 1) result = attribute;
  else {
    result = new CGAttribute();
    result.setCode(pathArray[0]);
    var parentAttribute = result;
    for (var i = 1; i < pathArray.length - 1; i++) {
      var childAttribute = new CGAttribute();
      childAttribute.setCode(pathArray[i]);
      parentAttribute.getAttributeList().addAttribute(childAttribute);
      parentAttribute = childAttribute;
    }
    parentAttribute.getAttributeList().addAttribute(attribute);
  }

  return result.serialize();
};

CGAction.prototype.locateAttribute = function (attribute, path) {
  var pathArray = path.split(Context.Config.AttributePathSeparator);

  if (pathArray.length == 1) return attribute;

  var currentAttribute = attribute;
  var childAttribute = null;
  for (var i = 1; i < pathArray.length; i++) {
    var attributeList = currentAttribute.getAttributeList();

    childAttribute = attributeList.getAttribute(pathArray[i]);
    if (childAttribute == null) {
      childAttribute = new CGAttribute();
      childAttribute.setCode(pathArray[i]);
      attributeList.addAttribute(childAttribute);
    }

    currentAttribute = childAttribute;
  }

  return childAttribute;
};