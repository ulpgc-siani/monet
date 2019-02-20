//----------------------------------------------------------------------
// load node helper page
//----------------------------------------------------------------------
function CGActionLoadNodeHelperPage() {
  this.base = CGAction;
};

CGActionLoadNodeHelperPage.prototype = new CGAction;
CGActionLoadNodeHelperPage.constructor = CGActionLoadNodeHelperPage;
CommandFactory.register(CGActionLoadNodeHelperPage, { Code: 0 }, false);

CGActionLoadNodeHelperPage.prototype.execute = function () {
  var Process = new CGProcessLoadNodeHelperPage();
  Process.Code = this.Code;
  Process.execute();
};

//----------------------------------------------------------------------
// load helper page
//----------------------------------------------------------------------
function CGActionLoadHelperPage() {
  this.base = CGAction;
};

CGActionLoadHelperPage.prototype = new CGAction;
CGActionLoadHelperPage.constructor = CGActionLoadHelperPage;
CommandFactory.register(CGActionLoadHelperPage, { Path: 0 }, false);

CGActionLoadHelperPage.prototype.execute = function () {
  var Process = new CGProcessLoadHelperPage();
  Process.Path = this.Path;
  Process.execute();
};

//----------------------------------------------------------------------
// load helper editors
//----------------------------------------------------------------------
function CGActionLoadHelperEditors() {
  this.base = CGAction;
};

CGActionLoadHelperEditors.prototype = new CGAction;
CGActionLoadHelperEditors.constructor = CGActionLoadHelperEditors;
CommandFactory.register(CGActionLoadHelperEditors, null, false);

CGActionLoadHelperEditors.prototype.execute = function () {
  var Process = new CGProcessLoadHelperEditors();
  Process.execute();
};

//----------------------------------------------------------------------
// Toggle right panel
//----------------------------------------------------------------------
function CGActionToggleRightPanel() {
  this.base = CGAction;
  this.base(1);
};

CGActionToggleRightPanel.prototype = new CGAction;
CGActionToggleRightPanel.constructor = CGActionToggleRightPanel;
CommandFactory.register(CGActionToggleRightPanel, null, false);

CGActionToggleRightPanel.prototype.enabled = function () {
  return true;
};

CGActionToggleRightPanel.prototype.refresh = function (MenuOption) {
  MenuOption.setChecked(Desktop.Main.Right.isExpanded());
};

CGActionToggleRightPanel.prototype.step_1 = function () {
  if (Desktop.Main.Right.isExpanded()) Desktop.Main.Right.collapse();
  else Desktop.Main.Right.expand();
};

//----------------------------------------------------------------------
// Activate right panel tab
//----------------------------------------------------------------------
function CGActionActivateRightPanelTab() {
  this.base = CGAction;
  this.base(2);
};

CGActionActivateRightPanelTab.prototype = new CGAction;
CGActionActivateRightPanelTab.constructor = CGActionActivateRightPanelTab;
CommandFactory.register(CGActionActivateRightPanelTab, { Tab: 0 }, false);

CGActionActivateRightPanelTab.prototype.enabled = function () {
  return true;
};

CGActionActivateRightPanelTab.prototype.step_1 = function () {
  var ProcessCheckRightPanelExpanded = new CGProcessCheckRightPanelExpanded();
  ProcessCheckRightPanelExpanded.ReturnProcess = this;
  ProcessCheckRightPanelExpanded.execute();
};

CGActionActivateRightPanelTab.prototype.step_2 = function () {
  if (this.Tab == Literals.TabPanels.MainRightHelper) Desktop.Main.Right.setPanelTitle(Literals.TabPanels.MainRightHelper, Lang.LayoutMainRight.HelperTitle);
  Desktop.Main.Right.activateTab(this.Tab);
};

//----------------------------------------------------------------------
// Download Business Model file
//----------------------------------------------------------------------
function CGActionDownloadBusinessModelFile() {
  this.base = CGAction;
};

CGActionDownloadBusinessModelFile.prototype = new CGAction;
CGActionDownloadBusinessModelFile.constructor = CGActionDownloadBusinessModelFile;
CommandFactory.register(CGActionDownloadBusinessModelFile, { Path: 0 }, false);

CGActionDownloadBusinessModelFile.prototype.execute = function () {
  window.location.href = Kernel.getBusinessModelFileLink() + "&path=" + this.Path;
};
