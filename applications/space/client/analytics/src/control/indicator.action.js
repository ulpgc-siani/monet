//----------------------------------------------------------------------
// Action show indicator
//----------------------------------------------------------------------
function ActionShowIndicator() {
  this.base = Action;
  this.base(3);
};

ActionShowIndicator.prototype = new Action;
ActionShowIndicator.constructor = ActionShowIndicator;
CommandFactory.register(ActionShowIndicator, { dashboard: 0 }, false);

ActionShowIndicator.prototype.step_1 = function() {
  CommandListener.throwCommand("showdashboard(" + this.dashboard + ")");
};