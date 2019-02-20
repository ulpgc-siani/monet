CGTeamConstructor = function () {
};

CGTeamConstructor.prototype.initBehaviours = function (extObject) {
  var aBehaviours = extObject.select(".behaviour");
  aBehaviours.each(function (Behaviour) {
    Behaviour = Behaviour.dom;
    Event.observe(Behaviour, 'click', CGTeamConstructor.prototype.atBehaviourClick.bind(this, Behaviour));
  }, this);
};

CGTeamConstructor.prototype.initTeams = function (extObject) {
  var Constructor = new CGTeamConstructor();
  var aExtTeams = extObject.select(CSS_TEAM);
  aExtTeams.each(function (extTeam) {
    Constructor.init(extTeam.dom);
  }, this);
};

CGTeamConstructor.prototype.init = function (DOMObject) {
  var extObject = Ext.get(DOMObject);
  var DecoratorTeam = new CGDecoratorTeam();

  if (extObject == null) return;
  if (extObject.hasClass(CLASS_TEAM)) DecoratorTeam.execute(DOMObject);

  this.initTeams(extObject);
  this.initBehaviours(extObject);
};

CGTeamConstructor.prototype.atBehaviourClick = function (DOMBehaviour, EventLaunched) {
  var BehaviourInfo = new CGCommandInfo(DOMBehaviour.href);
  Event.stop(EventLaunched);
  return false;
};