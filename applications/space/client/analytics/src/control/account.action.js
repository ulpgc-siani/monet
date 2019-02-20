//----------------------------------------------------------------------
// Show Environment
//----------------------------------------------------------------------
function ActionShowEnvironment() {
  this.base = Action;
  this.base(2);
};

ActionShowEnvironment.prototype = new Action;
ActionShowEnvironment.constructor = ActionShowEnvironment;
CommandFactory.register(ActionShowEnvironment, { id : 0 }, false);

ActionShowEnvironment.prototype.step_1 = function(){
  var account = State.account;
  var preferences = Serializer.deserializeMap(account.user.info.preferences);
  
  preferences["rootNode"] = this.id;
  account.user.info.preferences = Serializer.serializeMap(preferences);
  
  var process = new ProcessSaveAccount();
  process.account = account;
  process.returnProcess = this;
  process.execute();
};

ActionShowEnvironment.prototype.step_2 = function() {
  window.location.href = Context.Config.OfficeUrl;
};

//----------------------------------------------------------------------
// Toggle Dashboard
//----------------------------------------------------------------------
function ActionToggleDashboard() {
  this.base = Action;
  this.base(2);
};

ActionToggleDashboard.prototype = new Action;
ActionToggleDashboard.constructor = ActionToggleDashboard;
CommandFactory.register(ActionToggleDashboard, { id : 0 }, false);

ActionToggleDashboard.prototype.step_1 = function() {
  var account = State.account;

  if (this.id == account.rootdashboard.id) {
    this.terminate();
    return;
  }

  var preferences = Serializer.deserializeMap(account.user.info.preferences);
  preferences["rootDashboard"] = this.id;
  account.user.info.preferences = Serializer.serializeMap(preferences);
  
  var process = new ProcessSaveAccount();
  process.account = account;
  process.returnProcess = this;
  process.execute();
};

ActionToggleDashboard.prototype.step_2 = function() {
  window.location.href = Context.Config.Url;
};

//----------------------------------------------------------------------
// Show Unit
//----------------------------------------------------------------------
function ActionShowUnit() {
  this.base = Action;
  this.base(1);
};

ActionShowUnit.prototype = new Action;
ActionShowUnit.constructor = ActionShowUnit;
CommandFactory.register(ActionShowUnit, { id : 0, url : 1 }, false);

ActionShowUnit.prototype.step_1 = function() {

  if (this.id == Context.Config.BusinessUnit) {
    this.terminate();
    return;
  }
  
  window.location.href = this.url;
};

//----------------------------------------------------------------------
// Logout
//----------------------------------------------------------------------
function ActionLogout() {
  this.base = Action;
  this.base(2);
};

ActionLogout.prototype = new Action;
ActionLogout.constructor = ActionLogout;
CommandFactory.register(ActionLogout, null, false);

ActionLogout.prototype.step_1 = function(){
  Kernel.logout(this, State.account.instanceId);
};

ActionLogout.prototype.step_2 = function(){
  var location = Context.Config.EnterpriseLoginUrl;
  if (location == null || location == "") location = Context.Config.Url;

  State.logout = true;
  this.terminate();

  reload(location);
};