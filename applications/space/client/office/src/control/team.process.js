//----------------------------------------------------------------------
// Show team
//----------------------------------------------------------------------
function CGProcessShowTeam() {
  this.base = CGProcess;
  this.base(4);
  this.ActivateTeam = true;
  this.DOMViewActiveTab = null;
};

CGProcessShowTeam.prototype = new CGProcess;
CGProcessShowTeam.constructor = CGProcessShowTeam;

CGProcessShowTeam.prototype.createViewTeam = function (Team) {
  var IdTab = Desktop.Main.Center.Body.addTab(VIEW_TEAM, {Id: Team.getId(), Background: !this.ActivateTeam});
  return Desktop.createView($(IdTab), Team, null, "default", true);
};

CGProcessShowTeam.prototype.step_1 = function () {

  if (this.ViewTeam == null) {
    var ViewTeam = Desktop.Main.Center.Body.getContainerView(VIEW_TEAM, this.Id);
    if (ViewTeam != null) {
      if (this.ActivateTeam) {
        var Process = new CGProcessActivateTeam();
        Process.Id = this.Id;
        Process.execute();
        this.terminate();
        return;
      }
      else {
        ViewTeam.destroy();
        ViewTeam = null;
      }
    }
    if (ViewTeam == null) {
      if (!this.Team) {
        this.Team = new CGTeam();
        this.Team.setId(this.Id);
      }
      this.ViewTeam = this.createView(this.Team);
    }
  }
  else {
    if ((!this.DOMViewActiveTab) && (this.ViewTeam) && (this.ViewTeam.getDOM) && (this.ViewTeam.getDOM().getActiveTab)) this.DOMViewActiveTab = this.ViewTeam.getDOM().getActiveTab();
  }

  if (!this.Team) Kernel.loadTeam(this);
  else this.gotoStep(3);
};

CGProcessShowTeam.prototype.step_2 = function () {
  this.Team = new CGTeam();
  this.Team.unserialize(this.data);
  TeamsCache.register(this.Team);

  this.ViewTeam.setTarget(this.Team);
  this.ViewTeam.refresh();

  this.execute();
};

CGProcessShowTeam.prototype.step_3 = function () {

  if (this.ActivateTeam) {
    Process = new CGProcessActivateTeam();
    Process.Id = this.Team.getId();
    Process.DOMViewActiveTab = this.DOMViewActiveTab;
    Process.ReturnProcess = this;
    Process.execute();
  }
  else this.execute();
};

CGProcessShowTeam.prototype.step_4 = function () {
  this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Activate team
//----------------------------------------------------------------------
function CGProcessActivateTeam() {
  this.base = CGProcess;
  this.base(1);
};

CGProcessActivateTeam.prototype = new CGProcess;
CGProcessActivateTeam.constructor = CGProcessActivateTeam;

CGProcessActivateTeam.prototype.step_1 = function () {
  var Team = null;
  var ViewTeam, DOMTeam;

  if (!(Team = TeamsCache.get(this.Id))) {
    this.terminateOnFailure();
    return;
  }

  if ((ViewTeam = Desktop.Main.Center.Body.getContainerView(VIEW_TEAM, Team.getId())) == null) {
    this.terminateOnFailure();
    return;
  }

  TeamsCache.setCurrent(this.Id);

  Desktop.Main.Center.Header.refresh();
  Desktop.Footer.refresh();

  Desktop.Main.Center.Body.disableNotifications();
  Desktop.Main.Center.Body.activateTab(VIEW_TEAM, this.Id);
  Desktop.Main.Center.Body.enableNotifications();

  if (this.DOMViewActiveTab) ViewTeam.getDOM().activateTab(this.DOMViewActiveTab);

  ViewTeam.show();
  DOMTeam = ViewTeam.getDOM();
  var ActiveTabId = DOMTeam.getActiveTab();
  if (ActiveTabId) DOMTeam.activateTab(ActiveTabId);
  else DOMTeam.activateDefaultTab();

  EventManager.notify(EventManager.OPEN_TEAM, {"Team": Team, "DOMTeam": ViewTeam.getDOM()});

  this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// Close team
//----------------------------------------------------------------------
function CGProcessCloseTeam() {
  this.base = CGProcess;
  this.base(1);
};

CGProcessCloseTeam.prototype = new CGProcess;
CGProcessCloseTeam.constructor = CGProcessCloseTeam;

CGProcessCloseTeam.prototype.step_1 = function () {
  var Team = TeamsCache.get(this.Id);

  if ((ViewTeam = Desktop.Main.Center.Body.getContainerView(VIEW_TEAM, this.Id)) == null) {
    this.terminateOnSuccess();
    return;
  }

  EventManager.notify(EventManager.CLOSE_TEAM, {"Team": Team, "DOMTeam": ViewTeam.getDOM()});

  ViewTeam.destroy();
  Desktop.Main.Center.Body.deleteView(VIEW_TEAM, ViewTeam.getId());
  Desktop.Main.Center.Body.deleteTab(VIEW_TEAM, this.Id);
  TeamsCache.unregister(this.Id);

  this.terminateOnSuccess();
};