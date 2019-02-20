function CGBPITask(Task, DOMTask) {
  this.DOMTask = DOMTask;
};

//---------------------------------------------------------------------
CGBPITask.prototype.getId = function () {
  return this.DOMTask.getIdNode();
};