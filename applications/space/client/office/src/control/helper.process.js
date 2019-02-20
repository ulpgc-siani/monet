//----------------------------------------------------------------------
// Check right panel expanded
//----------------------------------------------------------------------
function CGProcessCheckRightPanelExpanded() {
  this.base = CGProcess;
  this.base(3);
  this.Dialog = null;
};

CGProcessCheckRightPanelExpanded.prototype = new CGProcess;
CGProcessCheckRightPanelExpanded.constructor = CGProcessCheckRightPanelExpanded;

CGProcessCheckRightPanelExpanded.prototype.onFailure = function (sResponse) {
  if (this.Dialog) this.Dialog.destroy();
  this.terminateOnFailure(sResponse);
};

CGProcessCheckRightPanelExpanded.prototype.atYes = function () {
  Desktop.Main.Right.expand();
  this.UserOption = true;
  this.execute();
};

CGProcessCheckRightPanelExpanded.prototype.atNo = function () {
  this.UserOption = false;
  this.execute();
};

CGProcessCheckRightPanelExpanded.prototype.step_1 = function () {

  if (Desktop.Main.Right.isExpanded()) {
    this.terminateOnSuccess();
    return;
  }

  Desktop.Main.Right.expand();
  this.terminateOnSuccess();
};

CGProcessCheckRightPanelExpanded.prototype.step_2 = function () {
  var aPreferences, Process, bRememberResult;

  bRememberResult = this.Dialog.RememberResult;
  this.Dialog.destroy();

  if (bRememberResult) {
    aPreferences = Account.getUser().getInfo().getPreferences();
    aPreferences["LayoutMainRightExpand"] = this.UserOption;

    Account.getUser().getInfo().setPreferences(aPreferences);

    Process = new CGProcessSaveAccount();
    Process.Account = Account;
    Process.ReturnProcess = this;
    Process.execute();
  }
  else this.terminateOnSuccess();
};

CGProcessCheckRightPanelExpanded.prototype.step_3 = function () {
  this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Load node helper page
//----------------------------------------------------------------------
function CGProcessLoadNodeHelperPage() {
  this.base = CGProcess;
  this.base(3);
};

CGProcessLoadNodeHelperPage.prototype = new CGProcess;
CGProcessLoadNodeHelperPage.constructor = CGProcessLoadNodeHelperPage;

CGProcessLoadNodeHelperPage.prototype.step_1 = function () {
  var Code = (this.Code) ? this.Code : null;

  if (Code == null) {
    this.terminateOnSuccess();
    return;
  }

  Kernel.loadNodeHelperPage(this, Code);
};

CGProcessLoadNodeHelperPage.prototype.step_2 = function () {
  var Process = new CGProcessCheckRightPanelExpanded();
  Process.ReturnProcess = this;
  Process.execute();
};

CGProcessLoadNodeHelperPage.prototype.step_3 = function () {
  Desktop.Main.Right.activateTab(Literals.TabPanels.MainRightHelper);
  Desktop.Main.Right.setPanelTitle(Literals.TabPanels.MainRightHelper, Lang.LayoutMainRight.HelperTitle);

  var Page = new CGPage();
  Page.unserialize(this.data);

  if (Page.getContent() == "") return;

  var helper = ViewerSidebar.getHelper(Helper.PAGE);

  helper.setTarget(Page);
  helper.refresh();
  helper.show();

  this.terminate();
};

//----------------------------------------------------------------------
// Load helper page
//----------------------------------------------------------------------
function CGProcessLoadHelperPage() {
  this.base = CGProcess;
  this.base(3);
};

CGProcessLoadHelperPage.prototype = new CGProcess;
CGProcessLoadHelperPage.constructor = CGProcessLoadHelperPage;

CGProcessLoadHelperPage.prototype.step_1 = function () {

  if (!this.Path) {
    this.terminateOnFailure();
    return;
  }

  Kernel.loadHelperPage(this, this.Path);
};

CGProcessLoadHelperPage.prototype.step_2 = function () {
  var Process = new CGProcessCheckRightPanelExpanded();
  Process.ReturnProcess = this;
  Process.execute();
};

CGProcessLoadHelperPage.prototype.step_3 = function () {
  Desktop.Main.Right.activateTab(Literals.TabPanels.MainRightHelper);
  Desktop.Main.Right.setPanelTitle(Literals.TabPanels.MainRightHelper, Lang.LayoutMainRight.HelperTitle);

  var Page = new CGPage();
  Page.unserialize(this.data);

  if (Page.getContent() == "") return;

  var helper = ViewerSidebar.getHelper(Helper.PAGE);

  helper.setTarget(Page);
  helper.refresh();
  helper.show();

  this.terminate();
};

//----------------------------------------------------------------------
// Load helper editors
//----------------------------------------------------------------------
function CGProcessLoadHelperEditors() {
  this.base = CGProcess;
  this.base(2);
};

CGProcessLoadHelperEditors.prototype = new CGProcess;
CGProcessLoadHelperEditors.constructor = CGProcessLoadHelperEditors;

CGProcessLoadHelperEditors.prototype.step_1 = function () {
  var Process = new CGProcessCheckRightPanelExpanded();
  Process.ReturnProcess = this;
  Process.execute();
};

CGProcessLoadHelperEditors.prototype.step_2 = function () {

  Desktop.Main.Right.activateTab(Literals.TabPanels.MainRightHelper);
  Desktop.Main.Right.setPanelTitle(Literals.TabPanels.MainRightHelper, Lang.LayoutMainRight.HelperTitle);

  ViewerSidebar.activateHelper(Helper.EDITORS);

  this.terminate();
};

//----------------------------------------------------------------------
// Load helper document preview
//----------------------------------------------------------------------
function CGProcessLoadHelperPreview() {
  this.base = CGProcess;
  this.base(2);
};

CGProcessLoadHelperPreview.prototype = new CGProcess;
CGProcessLoadHelperPreview.constructor = CGProcessLoadHelperPreview;

CGProcessLoadHelperPreview.prototype.step_1 = function () {
  var Process = new CGProcessCheckRightPanelExpanded();
  Process.ReturnProcess = this;
  Process.execute();
};

CGProcessLoadHelperPreview.prototype.step_2 = function () {
  Desktop.Main.Right.activateTab(Literals.TabPanels.MainRightHelper);
  Desktop.Main.Right.setPanelTitle(Literals.TabPanels.MainRightHelper, Lang.LayoutMainRight.HelperTitle);

  ViewerSidebar.activateHelper(Helper.PREVIEW);

  this.terminate();
};

//----------------------------------------------------------------------
// Load helper list viewer
//----------------------------------------------------------------------
function CGProcessLoadHelperListViewer() {
  this.base = CGProcess;
  this.base(2);
};

CGProcessLoadHelperListViewer.prototype = new CGProcess;
CGProcessLoadHelperListViewer.constructor = CGProcessLoadHelperListViewer;

CGProcessLoadHelperListViewer.prototype.step_1 = function () {
  var Process = new CGProcessCheckRightPanelExpanded();
  Process.ReturnProcess = this;
  Process.execute();
};

CGProcessLoadHelperListViewer.prototype.step_2 = function () {
  Desktop.Main.Right.activateTab(Literals.TabPanels.MainRightHelper);
  Desktop.Main.Right.setPanelTitle(Literals.TabPanels.MainRightHelper, Lang.LayoutMainRight.HelperTitle);

  ViewerSidebar.activateHelper(Helper.LIST);

  this.terminate();
};

//----------------------------------------------------------------------
// Load helper map viewer
//----------------------------------------------------------------------
function CGProcessLoadHelperMapViewer() {
  this.base = CGProcess;
  this.base(2);
};

CGProcessLoadHelperMapViewer.prototype = new CGProcess;
CGProcessLoadHelperMapViewer.constructor = CGProcessLoadHelperMapViewer;

CGProcessLoadHelperMapViewer.prototype.step_1 = function () {
  var Process = new CGProcessCheckRightPanelExpanded();
  Process.ReturnProcess = this;
  Process.execute();
};

CGProcessLoadHelperMapViewer.prototype.step_2 = function () {
  Desktop.Main.Right.activateTab(Literals.TabPanels.MainRightHelper);
  Desktop.Main.Right.setPanelTitle(Literals.TabPanels.MainRightHelper, Lang.LayoutMainRight.HelperTitle);

  ViewerSidebar.activateHelper(Helper.LIST);
  ViewerSidebar.activateHelper(Helper.MAP);

  this.terminate();
};

//----------------------------------------------------------------------
// Load helper source viewer
//----------------------------------------------------------------------
function CGProcessLoadHelperSourceViewer() {
  this.base = CGProcess;
  this.base(3);
};

CGProcessLoadHelperSourceViewer.prototype = new CGProcess;
CGProcessLoadHelperSourceViewer.constructor = CGProcessLoadHelperSourceViewer;

CGProcessLoadHelperSourceViewer.prototype.step_1 = function () {
  var Process = new CGProcessCheckRightPanelExpanded();
  Process.ReturnProcess = this;
  Process.execute();
};

CGProcessLoadHelperSourceViewer.prototype.step_2 = function () {
  if (this.IdSource != null)
    Kernel.loadSourceNewTerms(this, this.IdSource);
  else
    this.execute();
};

CGProcessLoadHelperSourceViewer.prototype.step_3 = function () {
  Desktop.Main.Right.activateTab(Literals.TabPanels.MainRightHelper);
  Desktop.Main.Right.setPanelTitle(Literals.TabPanels.MainRightHelper, Lang.LayoutMainRight.HelperTitle);
  var JSONData = (this.data != null) ? Ext.util.JSON.decode(this.data) : null;
  var source = (JSONData != null) ? JSONData.source : null;
  var termList = (JSONData != null) ? JSONData.termList : null;
  var Source = new CGSource(source);
  var helper = ViewerSidebar.getHelper(Helper.SOURCE);

  helper.setTarget({Source: Source, NewTermList: termList});
  ViewerSidebar.activateHelper(Helper.SOURCE);

  this.terminate();
};

//----------------------------------------------------------------------
// Load helper role viewer
//----------------------------------------------------------------------
function CGProcessLoadHelperRoleViewer() {
  this.base = CGProcess;
  this.base(4);
};

CGProcessLoadHelperRoleViewer.prototype = new CGProcess;
CGProcessLoadHelperRoleViewer.constructor = CGProcessLoadHelperRoleViewer;

CGProcessLoadHelperRoleViewer.prototype.step_1 = function () {
  var Process = new CGProcessCheckRightPanelExpanded();
  Process.ReturnProcess = this;
  Process.execute();
};

CGProcessLoadHelperRoleViewer.prototype.step_2 = function () {
  if (!State.RoleDefinitionList) Kernel.loadRoleDefinitionList(this);
  else this.gotoStep(4);
};

CGProcessLoadHelperRoleViewer.prototype.step_3 = function () {
  State.RoleDefinitionList = Ext.util.JSON.decode(this.data);
  this.execute();
};

CGProcessLoadHelperRoleViewer.prototype.step_4 = function () {
  Desktop.Main.Right.activateTab(Literals.TabPanels.MainRightHelper);
  Desktop.Main.Right.setPanelTitle(Literals.TabPanels.MainRightHelper, Lang.LayoutMainRight.HelperTitle);
  var helper = ViewerSidebar.getHelper(Helper.SOURCE);

  helper.setTarget(null);
  ViewerSidebar.activateHelper(Helper.SOURCE);

  this.terminate();
};

//----------------------------------------------------------------------
// Load helper revision list viewer
//----------------------------------------------------------------------
function CGProcessLoadHelperRevisionListViewer() {
  this.base = CGProcess;
  this.base(2);
};

CGProcessLoadHelperRevisionListViewer.prototype = new CGProcess;
CGProcessLoadHelperRevisionListViewer.constructor = CGProcessLoadHelperRevisionListViewer;

CGProcessLoadHelperRevisionListViewer.prototype.step_1 = function () {
  var Process = new CGProcessCheckRightPanelExpanded();
  Process.ReturnProcess = this;
  Process.execute();
};

CGProcessLoadHelperRevisionListViewer.prototype.step_2 = function () {
  Desktop.Main.Right.activateTab(Literals.TabPanels.MainRightHelper);
  Desktop.Main.Right.setPanelTitle(Literals.TabPanels.MainRightHelper, Lang.LayoutMainRight.HelperTitle);
  var helper = ViewerSidebar.getHelper(Helper.REVISIONLIST);

  helper.show();
  helper.refresh();

  this.terminate();
};

//----------------------------------------------------------------------
// Load helper chat
//----------------------------------------------------------------------
function CGProcessLoadHelperChatViewer() {
  this.base = CGProcess;
  this.base(2);
  this.refresh = false;
};

CGProcessLoadHelperChatViewer.prototype = new CGProcess;
CGProcessLoadHelperChatViewer.constructor = CGProcessLoadHelperChatViewer;

CGProcessLoadHelperChatViewer.prototype.step_1 = function () {
  var Process = new CGProcessCheckRightPanelExpanded();
  Process.ReturnProcess = this;
  Process.execute();
};

CGProcessLoadHelperChatViewer.prototype.step_2 = function () {
  Desktop.Main.Right.activateTab(Literals.TabPanels.MainRightHelper);
  Desktop.Main.Right.setPanelTitle(Literals.TabPanels.MainRightHelper, Lang.LayoutMainRight.HelperTitle);
  var helper = ViewerSidebar.getHelper(Helper.CHAT);

  helper.show();
  if (this.refresh) helper.refresh();

  this.terminate();
};

//----------------------------------------------------------------------
// Load helper observers
//----------------------------------------------------------------------
function CGProcessRefreshHelperObservers() {
  this.base = CGProcess;
  this.base(2);
};

CGProcessRefreshHelperObservers.prototype = new CGProcess;
CGProcessRefreshHelperObservers.constructor = CGProcessRefreshHelperObservers;

CGProcessRefreshHelperObservers.prototype.step_1 = function () {
  var Process = new CGProcessCheckRightPanelExpanded();
  Process.ReturnProcess = this;
  Process.execute();
};

CGProcessRefreshHelperObservers.prototype.step_2 = function () {
  Desktop.Main.Right.activateTab(Literals.TabPanels.MainRightHelper);
  Desktop.Main.Right.setPanelTitle(Literals.TabPanels.MainRightHelper, Lang.LayoutMainRight.HelperTitle);
  var helper = ViewerSidebar.getHelper(Helper.OBSERVERS);

  helper.setTarget(this.Observers);
  helper.refresh();

  if (this.Observers.length > 0) helper.show();
  else helper.hide();

  this.terminate();
};

//----------------------------------------------------------------------
// Close right panel
//----------------------------------------------------------------------
function CGProcessCloseRightPanel() {
  this.base = CGProcess;
  this.base(1);
};

CGProcessCloseRightPanel.prototype = new CGAction;
CGProcessCloseRightPanel.constructor = CGProcessCloseRightPanel;

CGProcessCloseRightPanel.prototype.step_1 = function () {
  Desktop.Main.Right.close();
};