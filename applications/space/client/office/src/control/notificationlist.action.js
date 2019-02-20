//----------------------------------------------------------------------
// show notification list
//----------------------------------------------------------------------
function CGActionShowNotificationList() {
  this.base = CGAction;
  this.base(2);
};

CGActionShowNotificationList.prototype = new CGAction;
CGActionShowNotificationList.constructor = CGActionShowNotificationList;
CommandFactory.register(CGActionShowNotificationList, null, true);

CGActionShowNotificationList.prototype.step_1 = function () {
  CommandListener.throwCommand("notificationsreadall()");

  Desktop.hideBanner();
  State.isShowingPrototype = false;

  Kernel.loadSystemTemplate(this, "notificationlist");
};

CGActionShowNotificationList.prototype.step_2 = function () {
  ViewNotificationList.setContent(this.data);
  ViewNotificationList.refresh();
  ViewNotificationList.show();
  Desktop.Main.Center.Body.activateNotificationList();
};

//----------------------------------------------------------------------
// render notification list
//----------------------------------------------------------------------
function CGActionRenderNotificationList() {
  this.base = CGAction;
  this.base(2);
  this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionRenderNotificationList.prototype = new CGAction;
CGActionRenderNotificationList.constructor = CGActionRenderNotificationList;
CommandFactory.register(CGActionRenderNotificationList, { IdDOMViewerLayer: 0, IdDOMViewerLayerOptions: 1 }, false);

CGActionRenderNotificationList.prototype.atShowItem = function (Sender, Item) {
  CommandListener.dispatchCommand("shownotification(" + Item.id + "," + Item.target + ")");
};

CGActionRenderNotificationList.prototype.atBoundItem = function (Sender, Item) {
  var Dummy = Item;
  for (var index in Dummy) {
    if (isFunction(Dummy[index])) continue;
    Item[index + "_short"] = shortValue(Dummy[index]);
  }
  Item.display = (Item.icon && Item.icon != EMPTY) ? "block" : "none";
  Item.unread = Item.read ? EMPTY : "unread";
  Item.createDate = getFormattedDateTime(parseServerDate(Item.createDate), Context.Config.Language, false);
};

CGActionRenderNotificationList.prototype.destroyViewer = function () {
  if (State.NotificationListViewer == null) return;
  State.registerListViewerState("notificationlist", State.NotificationListViewer.getState());
  State.NotificationListViewer.dispose();
  $(Literals.ListViewerWizard).innerHTML = "";
};

CGActionRenderNotificationList.prototype.createViewer = function () {
  var Options;

  this.destroyViewer();

  eval($(this.IdDOMViewerLayerOptions).innerHTML);
  State.NotificationListViewer = new CGListViewer(Options, Context.Config.Language, Context.Config.ImagesPath);
  State.NotificationListViewer.setDataSourceUrls(Kernel.getLoadNotificationsLink(), null);
  State.NotificationListViewer.setWizardLayer(Literals.ListViewerWizard);
  State.NotificationListViewer.onShowItem = CGActionRenderNotificationList.prototype.atShowItem.bind(this);
  State.NotificationListViewer.onBoundItem = CGActionRenderNotificationList.prototype.atBoundItem.bind(this);
  State.NotificationListViewer.setState(State.getListViewerState("notificationlist"));
  State.NotificationListViewer.render(this.IdDOMViewerLayer);
};

CGActionRenderNotificationList.prototype.step_1 = function () {

  if ((this.IdDOMViewerLayer == null) || (this.IdDOMViewerLayerOptions == null)) {
    this.terminate();
    return;
  }

  var Process = new CGProcessLoadHelperListViewer();
  Process.ReturnProcess = this;
  Process.execute();
};

CGActionRenderNotificationList.prototype.step_2 = function () {
  this.createViewer();
  this.terminate();
};


//----------------------------------------------------------------------
// Show notification
//----------------------------------------------------------------------
function CGActionShowNotification() {
  this.base = CGAction;
  this.base(1);
  this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionShowNotification.prototype = new CGAction;
CGActionShowNotification.constructor = CGActionShowNotification;
CommandFactory.register(CGActionShowNotification, { Id: 0, Target: 1 }, true);

CGActionShowNotification.prototype.step_1 = function () {

  if (this.Target && this.Target != EMPTY)
    CommandListener.dispatchCommand(getMonetLinkAction(this.Target));

  var Action = new CGActionNotificationsReadAll();
  Action.execute();
};

//----------------------------------------------------------------------
// notifications read
//----------------------------------------------------------------------
function CGActionNotificationsRead() {
  this.base = CGAction;
  this.base(2);
};

CGActionNotificationsRead.prototype = new CGAction;
CGActionNotificationsRead.constructor = CGActionNotificationsRead;
CommandFactory.register(CGActionNotificationsRead, { Ids: 0, Mode: 1 }, false);

CGActionNotificationsRead.prototype.step_1 = function () {
  if (!this.Ids) return;
  Kernel.notificationsRead(this.Ids.replace(/#/g, ","));
  Kernel.loadNotifications(this, 0, 6);
};

CGActionNotificationsRead.prototype.step_2 = function () {
  var jsonData = Ext.util.JSON.decode(this.data);
  Account.Notifications = jsonData;
  this.refreshFurnitureSet(false);
  this.terminate();
};
//----------------------------------------------------------------------
// notifications read all
//----------------------------------------------------------------------
function CGActionNotificationsReadAll() {
  this.base = CGAction;
  this.base(2);
};

CGActionNotificationsReadAll.prototype = new CGAction;
CGActionNotificationsReadAll.constructor = CGActionNotificationsReadAll;
CommandFactory.register(CGActionNotificationsReadAll, null, false);

CGActionNotificationsReadAll.prototype.step_1 = function () {
  Kernel.notificationsReadAll();
  Kernel.loadNotifications(this, 0, 6);
};

CGActionNotificationsReadAll.prototype.step_2 = function () {
  var jsonData = Ext.util.JSON.decode(this.data);
  Account.Notifications = jsonData;
  this.refreshFurnitureSet(false);
  this.terminate();
};