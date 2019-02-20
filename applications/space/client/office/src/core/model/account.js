Account = new Object;

/*********************************************************************/
/*  Variables                                                        */
/*********************************************************************/
Account.Id = null;
Account.User = null;
Account.Units = null;

/*********************************************************************/
/*  Properties                                                       */
/*********************************************************************/
Account.getInstanceId = function () {
  return Account.InstanceId;
};

Account.getId = function () {
  return Account.Id;
};

Account.setId = function (Id) {
  Account.Id = Id;
};

Account.getUser = function () {
  return Account.User;
};

Account.setUser = function (User) {
  Account.User = User;
};

Account.getEnvironments = function () {
  return Account.Environments;
};

Account.getDashboards = function () {
  return Account.Dashboards;
};

Account.getUnits = function () {
  return Account.Units;
};

Account.setUnits = function (Units) {
  Account.Units = Units;
};

Account.getLastNotifications = function () {
  return Account.Notifications;
};

Account.addNotification = function (notification) {
  Account.Notifications.nrows++;
  Account.Notifications.unread++;
  Account.Notifications.rows.push(notification);
};

Account.getPendingTasks = function (type) {
    return Account.PendingTasks[type];
};

Account.clean = function () {
  Account.bDirty = false;
};

Account.isDirty = function () {
  return Account.bDirty;
};

Account.serialize = function () {
  var sResult = "\"id\":\"" + this.Id + "\",";
  sResult += "\"user\":" + this.User.serialize();
  return "{" + sResult + "}";
};

Account.unserialize = function (serialized) {
  var jsonData = Ext.util.JSON.decode(serialized);

  Account.InstanceId = jsonData.instanceId;
  Account.Id = jsonData.id;
  Account.User = new CGUser(jsonData.user);
  Account.User.InitializerTask = jsonData.initializertask;
  Account.User.RootNode = jsonData.rootnode;
  Account.User.RootDashboard = jsonData.rootdashboard;
  Account.aRoles = jsonData.roles;

  Account.Notifications = jsonData.notifications;
  Account.Environments = jsonData.environments;
  Account.Dashboards = jsonData.dashboards;
  Account.PendingTasks = jsonData.pendingTasks;
  Account.Links = jsonData.links;
};