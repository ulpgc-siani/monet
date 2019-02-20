function Stub(mode) {
  this.mode = mode;
};

Stub.prototype.showConnectionFailure = function() {
  Desktop.reportProgress(Lang.ConnectionFailure, true);
};

Stub.prototype.hideConnectionFailure = function() {
  Desktop.hideReports();
};