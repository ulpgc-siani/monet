CGTaskList = function () {
  this.aTasks = new Array();
  this.bDirty = false;
  this.iCount = 0;
};

CGTaskList.prototype.newTasks = function (aJsonItems) {

  for (var i = 0; i < aJsonItems.length; i++) {
    var Item = aJsonItems[i];
    var Task = new CGTask();
    Task.unserializeFromJSON(Item);
    this.aTasks[Task.getId()] = Task;
  }

  return this.aTasks;
};

CGTaskList.prototype.addTask = function (Task) {
  this.aTasks[Task.getId()] = Task;
};

CGTaskList.prototype.deleteTask = function (Id) {
  if (this.aTasks[Id]) delete this.aTasks[Id];
};

CGTaskList.prototype.getTask = function (Id) {
  return this.aTasks[Id];
};

CGTaskList.prototype.getTasks = function () {
  return this.aTasks;
};

CGTaskList.prototype.setTasks = function (aTasks) {
  this.aTasks = aTasks;
};

CGTaskList.prototype.clean = function () {
  this.bDirty = false;
};

CGTaskList.prototype.isDirty = function () {
  return this.bDirty;
};

CGTaskList.prototype.getCount = function () {
  return this.iCount;
};

CGTaskList.prototype.setCount = function (iCount) {
  this.iCount = iCount;
};

CGTaskList.prototype.unserialize = function (serialized) {
  var jsonData = Ext.util.JSON.decode(serialized);

  this.aTasks = new Array();
  this.newTasks(jsonData.items);
  this.iCount = jsonData.count;
};