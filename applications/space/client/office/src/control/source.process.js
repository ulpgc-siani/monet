//----------------------------------------------------------------------
// refresh source new terms
//----------------------------------------------------------------------
function CGProcessRefreshSourceNewTerms() {
  this.base = CGProcess;
  this.base(2);
};

CGProcessRefreshSourceNewTerms.prototype = new CGProcess;
CGProcessRefreshSourceNewTerms.constructor = CGProcessRefreshSourceNewTerms;

CGProcessRefreshSourceNewTerms.prototype.step_1 = function () {
  Kernel.loadSourceNewTerms(this, this.IdSource);
};

CGProcessRefreshSourceNewTerms.prototype.step_2 = function () {
  var helper = ViewerSidebar.getHelper(Helper.SOURCE);
  var Target = helper.getTarget();
  var JSONData = Ext.util.JSON.decode(this.data);

  Target.NewTermList = JSONData.termList;
  helper.refresh();

  this.terminateOnSuccess();
};