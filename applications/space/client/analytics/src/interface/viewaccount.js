ViewAccount = function() {
  this.base = View;
  this.base();
};

ViewAccount.prototype = new View;

ViewAccount.prototype.init = function(DOMLayer) {
  var jLayer = $(DOMLayer);

  this.DOMLayer = DOMLayer;
  this.DOMLayer.innerHTML = translate(AppTemplate.viewaccount, Lang.ViewAccount);
  jLayer.addClass("view account");
  
  if (!this.target) return;

  this.addBehaviours();
};

ViewAccount.prototype.addBehaviours= function() {
  var jLayer = $(this.DOMLayer);
  var jUsername = jLayer.find("span.username");
  
  jUsername.click(ViewAccount.prototype.atUsernameClick.bind(this));
  
  CommandListener.capture(this.DOMLayer);
};

ViewAccount.prototype.refresh = function() {
  $("span.username .label").html(this.target.user.info.fullname);
  this.refreshEnvironments();
  this.refreshDashboards();
  this.refreshUnits();
};

ViewAccount.prototype.refreshEnvironments = function() {
  var jLayer = $(this.DOMLayer);
  var environmentTemplate = translate(AppTemplate.viewaccountenvironment, Lang.ViewAccount);
  var jEnvironments = jLayer.find("ul.environments");
  
  jEnvironments.html("");
  for (var i=0; i<this.target.environments.length; i++) {
    var environment = this.target.environments[i];
    environment.command = "showenvironment(" + environment.id + ")";
    var jEnvironment = $.tmpl(environmentTemplate, environment);
    jEnvironments.append(jEnvironment);
  }
  
  CommandListener.capture(jEnvironments.get(0));
};

ViewAccount.prototype.refreshDashboards = function() {
  var jLayer = $(this.DOMLayer);
  var dashboardTemplate = translate(AppTemplate.viewaccountdashboard, Lang.ViewAccount);
  var jDashboards = jLayer.find("ul.dashboards");
  
  jDashboards.html("");
  for (var i=0; i<this.target.dashboards.length; i++) {
    var dashboard = this.target.dashboards[i];
    
    dashboard.fullLabel = dashboard.label + (dashboard.active?" " + Lang.ViewAccount.Current:"");
    dashboard.anchorTitle = dashboard.active?dashboard.label:Lang.ViewAccount.StartSessionWith + " " + dashboard.label;
    dashboard.disabledLabel = dashboard.disabled?"disabled":"";
    dashboard.command = dashboard.disabled?"javascript:void(null)":"toggledashboard(" + dashboard.id + ")";
    
    var jDashboard = $.tmpl(dashboardTemplate, dashboard);
    jDashboards.append(jDashboard);
  }
  
  CommandListener.capture(jDashboards.get(0));
};

ViewAccount.prototype.refreshUnits = function() {
  var jLayer = $(this.DOMLayer);
  var unitTemplate = translate(AppTemplate.viewaccountunit, Lang.ViewAccount);
  var jUnits = jLayer.find("ul.units");
  
  jUnits.html("");
  for (var i=0; i<this.target.units.length; i++) {
    var unit = this.target.units[i]; 
      
    unit.fullLabel = unit.label + (unit.active?" " + Lang.ViewAccount.Current:"");
    unit.anchorTitle = unit.active?unit.label:Lang.ViewAccount.GotoUnit + " " + unit.label;
    unit.disabledLabel = unit.disabled?"disabled":"";
    unit.command = unit.disabled?"javascript:void(null)":"showunit(" + unit.id + "," + unit.url + ")";
      
    var jUnit = $.tmpl(unitTemplate, unit);
    jUnits.append(jUnit);
  }
  
  CommandListener.capture(jUnits.get(0));
};

ViewAccount.prototype.isUsernamePanelActive = function() {
  var jLayer = $(this.DOMLayer);
  var jUsernamePanel = jLayer.find(".panel.username");
  return jUsernamePanel.hasClass("active");
};

ViewAccount.prototype.showUsernamePanel = function() {
  var jLayer = $(this.DOMLayer);
  var jUsernamePanel = jLayer.find(".panel.username");
  
  jUsernamePanel.addClass("active");
  $(document.body).bind("click", ViewAccount.prototype.hideUsernamePanel.bind(this));
};

ViewAccount.prototype.hideUsernamePanel = function() {
  var jLayer = $(this.DOMLayer);
  var jUsernamePanel = jLayer.find(".panel.username");

  jUsernamePanel.removeClass("active");
  $(document.body).unbind("click", ViewAccount.prototype.hideUsernamePanel.bind(this));
};

//************************************************************************************************************

ViewAccount.prototype.atUsernameClick = function(event) {
  event.stopPropagation();

  var jLayer = $(this.DOMLayer);
  var jUsernamePanel = jLayer.find(".panel.username");
  
  if (this.isUsernamePanelActive()) this.hideUsernamePanel();
  else this.showUsernamePanel();
};