//----------------------------------------------------------------------
// Show dashboard
//----------------------------------------------------------------------
function CGActionShowDashboard() {
  this.base = CGAction;
  this.base(2);
};

CGActionShowDashboard.prototype = new CGAction;
CGActionShowDashboard.constructor = CGActionShowDashboard;
CommandFactory.register(CGActionShowDashboard, { Code: 0, View: 1 }, true);

CGActionShowDashboard.prototype.step_1 = function () {

  Desktop.hideBanner();
  State.isShowingPrototype = false;

  Kernel.loadDashboardTemplate(this, this.Code, this.View);
};

CGActionShowDashboard.prototype.step_2 = function () {

  var process = new CGProcessCloseRightPanel();
  process.execute();

  ViewDashboard.setContent(this.data);
  ViewDashboard.refresh();
  ViewDashboard.show();
  this.setActiveFurniture(Furniture.DASHBOARD, this.Code);
  Desktop.Main.Center.Body.activateDashboard();
};