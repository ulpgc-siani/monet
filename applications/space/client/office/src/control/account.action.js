//----------------------------------------------------------------------
// Show Federation
//----------------------------------------------------------------------
function CGActionShowFederation() {
  this.base = CGAction;
  this.base(1);
};

CGActionShowFederation.prototype = new CGAction;
CGActionShowFederation.constructor = CGActionShowFederation;
CommandFactory.register(CGActionShowFederation, null, true);

CGActionShowFederation.prototype.step_1 = function () {
  window.location.href = Context.Config.FederationUrl;
};

//----------------------------------------------------------------------
// Show Environment
//----------------------------------------------------------------------
function CGActionShowEnvironment() {
  this.base = CGAction;
  this.base(2);
};

CGActionShowEnvironment.prototype = new CGAction;
CGActionShowEnvironment.constructor = CGActionShowEnvironment;
CommandFactory.register(CGActionShowEnvironment, { Id: 0 }, true);

CGActionShowEnvironment.prototype.step_1 = function () {

  if (this.Id == Account.User.RootNode.id) {
    CommandListener.throwCommand("showhome()");
    this.terminate();
    return;
  }

  Desktop.reportProgress(Lang.Action.ShowEnvironment.Waiting);

  var aPreferences = Account.getUser().getInfo().getPreferences();
  aPreferences["rootNode"] = this.Id;
  Account.getUser().getInfo().setPreferences(aPreferences);

  var Process = new CGProcessSaveAccount();
  Process.Account = Account;
  Process.ReturnProcess = this;
  Process.execute();
};

CGActionShowEnvironment.prototype.step_2 = function () {
  window.location.href = Context.Config.Host;
};

//----------------------------------------------------------------------
// Toggle Dashboard
//----------------------------------------------------------------------
function CGActionToggleDashboard() {
  this.base = CGAction;
  this.base(2);
};

CGActionToggleDashboard.prototype = new CGAction;
CGActionToggleDashboard.constructor = CGActionToggleDashboard;
CommandFactory.register(CGActionToggleDashboard, { Id: 0 }, true);

CGActionToggleDashboard.prototype.step_1 = function () {
  Desktop.reportProgress(Lang.Action.ToggleDashboard.Waiting);

  var aPreferences = Account.getUser().getInfo().getPreferences();
  aPreferences["rootDashboard"] = this.Id;
  Account.getUser().getInfo().setPreferences(aPreferences);

  var Process = new CGProcessSaveAccount();
  Process.Account = Account;
  Process.ReturnProcess = this;
  Process.execute();
};

CGActionToggleDashboard.prototype.step_2 = function () {
  window.location.href = Context.Config.AnalyticsHost;
};

//----------------------------------------------------------------------
// Load Units
//----------------------------------------------------------------------
function CGActionLoadUnits() {
  this.base = CGAction;
  this.base(2);
};

CGActionLoadUnits.prototype = new CGAction;
CGActionLoadUnits.constructor = CGActionLoadUnits;
CommandFactory.register(CGActionLoadUnits, null, true);

CGActionLoadUnits.prototype.step_1 = function () {
  Kernel.loadUnits(this);
};

CGActionLoadUnits.prototype.step_2 = function () {
  var jsonData = Ext.util.JSON.decode(this.data);

  Account.setUnits(jsonData);
  ViewUser.addUnits(Account.Units);

  this.terminate();
};

//----------------------------------------------------------------------
// Show Unit
//----------------------------------------------------------------------
function CGActionShowUnit() {
  this.base = CGAction;
  this.base(1);
};

CGActionShowUnit.prototype = new CGAction;
CGActionShowUnit.constructor = CGActionShowUnit;
CommandFactory.register(CGActionShowUnit, { Id: 0, Url: 1 }, true);

CGActionShowUnit.prototype.step_1 = function () {

  if (this.Id == Context.Config.BusinessUnit) {
    this.terminate();
    return;
  }

  window.location.href = this.Url;
};

//----------------------------------------------------------------------
// Logout
//----------------------------------------------------------------------
function CGActionLogout() {
  this.base = CGAction;
  this.base(2);
  this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionLogout.prototype = new CGAction;
CGActionLogout.constructor = CGActionLogout;
CommandFactory.register(CGActionLogout, null, false);

CGActionLogout.prototype.step_1 = function () {
  Kernel.logout(this, Account.getInstanceId());
};

CGActionLogout.prototype.step_2 = function () {
  var sLocation = Context.Config.EnterpriseLoginUrl;
  if (sLocation == "") sLocation = Context.Config.Host;

  State.logout = true;
  this.terminate();

  reload(sLocation);
};

//----------------------------------------------------------------------
// Change role
//----------------------------------------------------------------------
function CGActionChangeRole() {
  this.base = CGAction;
  this.base(2);
  this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionChangeRole.prototype = new CGAction;
CGActionChangeRole.constructor = CGActionChangeRole;
CommandFactory.register(CGActionChangeRole, { Role: 0 }, false);

CGActionChangeRole.prototype.step_1 = function () {

  if (this.Role == null) {
    Desktop.reportError(Lang.Action.ChangeRole.Failure);
    this.terminate();
    return;
  }

  Kernel.changeRole(this, this.Role);
};

CGActionChangeRole.prototype.step_2 = function () {
  window.location.reload();
};

//----------------------------------------------------------------------
// Action Send Suggestion
//----------------------------------------------------------------------
function CGActionSendSuggestion() {
  this.base = CGAction;
  this.base(3);
  this.sMessage = "";
}

CGActionSendSuggestion.prototype = new CGAction;
CGActionSendSuggestion.constructor = CGActionSendSuggestion;
CommandFactory.register(CGActionSendSuggestion, null, false);

CGActionSendSuggestion.prototype.onFailure = function (sResponse) {
  Desktop.reportError(Lang.Action.SendSuggestion.Failure);
};

CGActionSendSuggestion.prototype.atDialogClick = function (btn, text) {
  this.sMessage = text;

  if (btn == "cancel") {
    this.terminate();
    return;
  }

  if (text == "") {
    Ext.MessageBox.show({title: Lang.Action.SendSuggestion.Label, msg: Lang.Action.SendSuggestion.DescriptionWithEmptyMessage, width: 500, height: 400, buttons: Ext.MessageBox.OKCANCEL, multiline: true, fn: CGActionSendSuggestion.prototype.atDialogClick.bind(this)});
    return false;
  }

  this.execute();
};

CGActionSendSuggestion.prototype.step_1 = function () {
  Ext.MessageBox.show({title: Lang.Action.SendSuggestion.Label, msg: Lang.Action.SendSuggestion.Description, width: 500, height: 400, buttons: Ext.MessageBox.OKCANCEL, multiline: true, fn: CGActionSendSuggestion.prototype.atDialogClick.bind(this)});
};

CGActionSendSuggestion.prototype.step_2 = function () {
  Kernel.sendSuggestion(this, this.sMessage);
};

CGActionSendSuggestion.prototype.step_3 = function () {
  Desktop.reportSuccess(Lang.Action.SendSuggestion.Done);
  this.terminate();
}; 