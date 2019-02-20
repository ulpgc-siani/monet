//----------------------------------------------------------------------
// Show Team
//----------------------------------------------------------------------
function CGActionShowTeam() {
  this.base = CGActionShowBase;
  this.base(2);
  this.AvailableProcessClass = CGProcessCleanDirty;
  this.RefreshProcessClass = CGProcessRefreshDOM;
  this.DOMViewActiveTab = null;
};

CGActionShowTeam.prototype = new CGActionShowBase;
CGActionShowTeam.constructor = CGActionShowTeam;
CommandFactory.register(CGActionShowTeam, { Id: 0 }, true);

CGActionShowTeam.prototype.getDOMElement = function () {
  return Extension.getDOMTeam(this.DOMItem);
};

CGActionShowTeam.prototype.step_1 = function () {
  var ViewTeam;

  Desktop.hideBanner();

  ViewTeam = Desktop.Main.Center.Body.getContainerView(VIEW_TEAM, this.Id);
  if (ViewTeam != null) {
    this.DOMViewActiveTab = ViewTeam.getDOM().getActiveTab();
    var Process = new CGProcessActivateTeam();
    Process.Id = this.Id;
    Process.execute();
    State.LastView = ViewTeam;
    this.terminate();
    return;
  }

  Kernel.loadTeam(this);
};

CGActionShowTeam.prototype.step_2 = function () {
  var Team, ViewTeam;
  var ProcessShowTeam;

  Team = new CGTeam();
  Team.setId(TeamsCache.getCount() + 1);
  Team.unserialize(this.data);
  TeamsCache.register(Team);
  TeamsCache.setCurrent(Team.getId());

  if ((ViewTeam = this.getView(VIEW_TEAM, Team)) == false) {
    this.terminate();
    return;
  }

  State.LastView = ViewTeam;
  State.LastObject.Id = this.Id;
  State.LastObject.Mode = "default";

  ProcessShowTeam = new CGProcessShowTeam();
  ProcessShowTeam.Team = Team;
  ProcessShowTeam.ViewTeam = ViewTeam;
  ProcessShowTeam.DOMViewActiveTab = this.DOMViewActiveTab;
  ProcessShowTeam.execute();

  EventManager.notify(EventManager.OPEN_Team, {"Team": Team, "DOMTeam": ViewTeam.getDOM()});
  this.terminate();
};

//----------------------------------------------------------------------
// Activate Team
//----------------------------------------------------------------------
function CGActionActivateTeam() {
  this.base = CGAction;
  this.base(1);
};

CGActionActivateTeam.prototype = new CGAction;
CGActionActivateTeam.constructor = CGActionActivateTeam;
CommandFactory.register(CGActionActivateTeam, { Id: 0 }, false);

CGActionActivateTeam.prototype.step_1 = function () {
  var Process = new CGProcessActivateTeam();
  Process.Id = this.Id;
  Process.execute();
  this.terminate();
};

//----------------------------------------------------------------------
// Close Team
//----------------------------------------------------------------------
function CGActionCloseTeam() {
  this.base = CGAction;
  this.base(1);
  this.AvailableProcessClass = CGProcessCleanDirty;
  this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGActionCloseTeam.prototype = new CGAction;
CGActionCloseTeam.constructor = CGActionCloseTeam;
CommandFactory.register(CGActionCloseTeam, { Id: 0 }, false);

CGActionCloseTeam.prototype.step_1 = function () {
  var Process = new CGProcessCloseTeam();
  Process.Id = this.Id;
  Process.execute();
  this.terminate();
};