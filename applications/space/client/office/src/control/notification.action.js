function CGActionNewNotification() {
  this.base = CGAction;
  this.base(1);
};

CGActionNewNotification.prototype = new CGAction;
CGActionNewNotification.constructor = CGActionNewNotification;
CommandFactory.register(CGActionNewNotification, null, true);

CGActionNewNotification.prototype.step_1 = function () {
  Account.addNotification(this.notification);
  this.refreshFurnitureSet(true);
};