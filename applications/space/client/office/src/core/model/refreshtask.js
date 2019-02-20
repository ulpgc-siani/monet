CGRefreshTask = function (Type, Target) {
  this.Type = Type;
  this.Target = Target;
  this.Sender = null;
};

CGRefreshTask.prototype.setSender = function (Sender) {
  this.Sender = Sender;
};