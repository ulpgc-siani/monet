//----------------------------------------------------------------------
// add role process
//----------------------------------------------------------------------
function CGProcessAddRole() {
  this.base = CGAction;
  this.base(2);
};

CGProcessAddRole.prototype = new CGAction;
CGProcessAddRole.constructor = CGProcessAddRole;

CGProcessAddRole.prototype.step_1 = function () {
  var CurrentViewer = State.RoleListViewController.getFirstView();
  CurrentViewer.onRefresh = window.setTimeout(CGProcessAddRole.prototype.execute.bind(this), 400);
  CurrentViewer.setActiveItem(this.Code);
  CurrentViewer.refresh();
};

CGProcessAddRole.prototype.step_2 = function () {
  var RolesViewer = State.RoleListViewController.getCurrentView();
  RolesViewer.activateItem(this.Role.Id);
  this.terminate();
};
