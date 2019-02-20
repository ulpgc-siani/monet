//----------------------------------------------------------------------
// Action init
//----------------------------------------------------------------------
function ActionInit() {
  this.base = Action;
  this.base(2);
};

ActionInit.prototype = new Action;
ActionInit.constructor = ActionInit;
CommandFactory.register(ActionInit, null, false);

ActionInit.prototype.onFailure = function(response){
  var action = new ActionLogout();
  action.execute();

  Desktop.hideLoading();
  alert(Lang.InitApplication);

  this.terminate();
};

ActionInit.prototype.step_1 = function() {
  Kernel.loadAccount(this);
};

ActionInit.prototype.step_2 = function() {
  State.account = jQuery.parseJSON(this.data);
  
  Desktop.hideLoading();
  Desktop.refresh();
  CommandListener.throwCommand(Context.Config.Command);
};

//----------------------------------------------------------------------
// Action show home
//----------------------------------------------------------------------
function ActionShowHome() {
  this.base = Action;
  this.base(1);
};

ActionShowHome.prototype = new Action;
ActionShowHome.constructor = ActionShowHome;
CommandFactory.register(ActionShowHome, null, false);

ActionShowHome.prototype.step_1 = function() {
  var rootDashboard = State.account.rootdashboard;
  CommandListener.throwCommand("showdashboard(" + rootDashboard.id + ")");
};
