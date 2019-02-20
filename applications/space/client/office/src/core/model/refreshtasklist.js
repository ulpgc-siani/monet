CGRefreshTaskList = function () {
  this.aItems = new Array;
};

CGRefreshTaskList.prototype.getAll = function () {
  return this.aItems;
};

CGRefreshTaskList.prototype.addRefreshTask = function (RefreshTask) {
  this.aItems.push(RefreshTask);
};

CGRefreshTaskList.prototype.deleteRefreshTask = function (iPos) {
  delete this.aItems[iPos];
};

CGRefreshTaskList.prototype.clearAll = function () {
  this.aItems = new Array();
};
