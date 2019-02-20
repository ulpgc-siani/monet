function CGStub(Mode, idLoadingLayer) {
  this.Mode = Mode;
  this.idLoadingLayer = idLoadingLayer;
};

CGStub.prototype.showLoading = function (sMessage) {
  $(this.idLoadingLayer).style.display = "block";
  document.body.style.cursor = "hand";
};

CGStub.prototype.hideLoading = function () {
  $(this.idLoadingLayer).style.display = "none";
  document.body.style.cursor = "auto";
};

CGStub.prototype.showConnectionFailure = function () {
  Desktop.reportProgress(Lang.Exceptions.ConnectionFailure, true);
};

CGStub.prototype.hideConnectionFailure = function () {
  Desktop.hideReports();
};