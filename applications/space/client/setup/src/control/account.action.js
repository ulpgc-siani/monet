//----------------------------------------------------------------------
// Logout
//----------------------------------------------------------------------
function CGActionLogout () {
  this.base = CGAction;
  this.base(2);
}

CGActionLogout.prototype = new CGAction;
CGActionLogout.constructor = CGActionLogout;
CommandFactory.register(CGActionLogout, null, false);

CGActionLogout.prototype.step_1 = function(){
  Kernel.logout(this);
};

CGActionLogout.prototype.step_2 = function(){
  window.location = location;
};

//----------------------------------------------------------------------
// Load Account List
//----------------------------------------------------------------------
function CGActionLoadAccountList () {
  this.base = CGAction;
  this.base(2);
  this.AccountList = new Array();
}

CGActionLoadAccountList.prototype = new CGAction;
CGActionLoadAccountList.constructor = CGActionLoadAccountList;
CommandFactory.register(CGActionLoadAccountList, null, false);

CGActionLoadAccountList.prototype.onFailure = function(sResponse){
  Desktop.reportError(this.getErrorMessage(Lang.Action.LoadAccountList.Failure, sResponse));
};

CGActionLoadAccountList.prototype.step_1 = function(){
  Kernel.loadAccountList(this);
};

CGActionLoadAccountList.prototype.step_2 = function(){
  var AccountList = new CGAccountList();
  AccountList.unserialize(this.data);
  ViewAccountList.setTarget(AccountList);
  ViewAccountList.refresh();
  this.terminate();
};

//----------------------------------------------------------------------
// Create Account
//----------------------------------------------------------------------
function CGActionCreateAccount () {
  this.base = CGAction;
  this.base(4);
  this.dialog = null;
}

CGActionCreateAccount.prototype = new CGAction;
CGActionCreateAccount.constructor = CGActionCreateAccount;
CommandFactory.register(CGActionCreateAccount, null, false);

CGActionCreateAccount.prototype.onFailure = function(sResponse){
  if (this.dialog) this.dialog.destroy();
  Desktop.reportError(this.getErrorMessage(Lang.Action.CreateAccount.Failure, sResponse));
};

CGActionCreateAccount.prototype.step_1 = function(){
  Kernel.loadEnvironmentNodeDefinitions(this);
};

CGActionCreateAccount.prototype.step_2 = function(){
  var NodeDefinitionList = new CGNodeDefinitionList();
  NodeDefinitionList.unserialize(this.data);
  
  this.dialog = new CGDialogCreateAccount();
  this.dialog.setEmbedded(false);
  this.dialog.init();
  this.dialog.Target = NodeDefinitionList;
  this.dialog.onAccept = this.execute.bind(this);
  this.dialog.onCancel = this.resetState.bind(this);
  this.dialog.refresh();
  this.dialog.show();
};

CGActionCreateAccount.prototype.step_3 = function(){
  Desktop.reportProgress(Lang.Action.CreateAccount.Waiting);
  Kernel.createAccount(this, this.dialog.Username, this.dialog.Email, this.dialog.SendEmail, this.dialog.NodeDefinition, this.dialog.Type);
};

CGActionCreateAccount.prototype.step_4 = function(){
  this.dialog.destroy();

  var Action = new CGActionLoadAccountList();
  Action.execute();
  
  Desktop.hideReports();
  Desktop.reportSuccess(Lang.Action.CreateAccount.Done);
};

//----------------------------------------------------------------------
// Reset Account Password
//----------------------------------------------------------------------
function CGActionResetAccountPassword () {
  this.base = CGAction;
  this.base(3);
  this.dialog = null;
}

CGActionResetAccountPassword.prototype = new CGAction;
CGActionResetAccountPassword.constructor = CGActionResetAccountPassword;
CommandFactory.register(CGActionResetAccountPassword, { UserName : 0 }, false);

CGActionResetAccountPassword.prototype.onFailure = function(sResponse){
  Desktop.reportError(this.getErrorMessage(Lang.Action.ResetAccountPassword.Failure, sResponse));
};

CGActionResetAccountPassword.prototype.checkOption = function(ButtonResult, Password){
  if (ButtonResult == BUTTON_RESULT_OK) {
    if (! isAlphanumeric(Password)) {
      Ext.MessageBox.prompt(Lang.Action.ResetAccountPassword.Dialog.Title, Lang.Action.ResetAccountPassword.Dialog.DescriptionWithNoAlphanumeric, CGActionResetAccountPassword.prototype.checkOption.bind(this));
      return;
    }
    this.Password = Password;
    this.execute(); 
  }
};

CGActionResetAccountPassword.prototype.step_1 = function(){
  Ext.MessageBox.prompt(Lang.Action.ResetAccountPassword.Dialog.Title, Lang.Action.ResetAccountPassword.Dialog.Description, CGActionResetAccountPassword.prototype.checkOption.bind(this));
};

CGActionResetAccountPassword.prototype.step_2 = function(){
  Desktop.reportProgress(Lang.Action.ResetAccountPassword.Waiting);
  Kernel.resetAccountPassword(this, this.UserName, this.Password);
};

CGActionResetAccountPassword.prototype.step_3 = function(){
  Desktop.hideReports();
  Desktop.reportSuccess(Lang.Action.ResetAccountPassword.Done);
};

//----------------------------------------------------------------------
// Remove Accounts
//----------------------------------------------------------------------
function CGActionRemoveAccounts () {
  this.base = CGAction;
  this.base(3);
}

CGActionRemoveAccounts.prototype = new CGAction;
CGActionRemoveAccounts.constructor = CGActionRemoveAccounts;
CommandFactory.register(CGActionRemoveAccounts, null, false);

CGActionRemoveAccounts.prototype.enabled = function() {
  return (ViewAccountList.getSelected().length > 0);
};

CGActionRemoveAccounts.prototype.onFailure = function(sResponse){
  Desktop.reportError(this.getErrorMessage(Lang.Action.RemoveAccounts.Failure, sResponse));
};

CGActionRemoveAccounts.prototype.checkOption = function(ButtonResult){
  if (ButtonResult == BUTTON_RESULT_YES) { this.execute(); }
};

CGActionRemoveAccounts.prototype.step_1 = function(){
  Ext.MessageBox.confirm(Lang.Action.RemoveAccounts.Confirm.Title, Lang.Action.RemoveAccounts.Confirm.Description, CGActionRemoveAccounts.prototype.checkOption.bind(this));
};

CGActionRemoveAccounts.prototype.step_2 = function(ButtonResult) {
  Desktop.reportProgress(Lang.Action.RemoveAccounts.Waiting);
  var aAccounts = ViewAccountList.getSelected();
  Kernel.removeAccounts(this, aAccounts.toString());
};

CGActionRemoveAccounts.prototype.step_3 = function() {
  var Action = new CGActionLoadAccountList();
  Action.execute();
  
  Desktop.hideReports();
  Desktop.reportSuccess(Lang.Action.RemoveAccounts.Done);
};

//----------------------------------------------------------------------
// Toggle Account selection
//----------------------------------------------------------------------
function CGActionToggleAccountSelection () {
  this.base = CGAction;
  this.base(1);
}

CGActionToggleAccountSelection.prototype = new CGAction;
CGActionToggleAccountSelection.constructor = CGActionToggleAccountSelection;
CommandFactory.register(CGActionToggleAccountSelection, { Id : 0 }, false);

CGActionToggleAccountSelection.prototype.step_1 = function(){
  ViewAccountList.toggleSelection(this.Id);
};

//----------------------------------------------------------------------
// Select Accounts
//----------------------------------------------------------------------
function CGActionSelectAccounts () {
  this.base = CGAction;
  this.base(1);
}

CGActionSelectAccounts.prototype = new CGAction;
CGActionSelectAccounts.constructor = CGActionSelectAccounts;
CommandFactory.register(CGActionSelectAccounts, { Type : 0 }, false);

CGActionSelectAccounts.prototype.step_1 = function(){
  if (this.Type == "all") ViewAccountList.selectAll();
  else if (this.Type == "invert") ViewAccountList.selectInvert();
  else ViewAccountList.selectNone();
};