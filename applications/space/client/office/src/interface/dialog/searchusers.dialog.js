CGDialogSearchUsers = function () {
  this.base = CGDialog;
  this.base("dlgSearchUsers");
  this.ListView = null;
};

//------------------------------------------------------------------
CGDialogSearchUsers.prototype = new CGDialog;

//------------------------------------------------------------------
CGDialogSearchUsers.prototype.init = function (sLayerName) {
  if (!sLayerName) return;

  this.Layer = $(sLayerName);

  var html = AppTemplate.DialogSearchUsers;
  html = translate(html, Lang.DialogSearchUsers);

  this.Layer.innerHTML = html;

  var template = AppTemplate.DialogSearchUsersItem;
  template = translate(template, Lang.DialogSearchUsersItem);

  this.ListView = new TListView();
  this.ListView.setTemplate(template);
  this.ListView.setUrl(Context.Config.Api + "?op=searchusers");
  this.ListView.setMaxPageItems(5);
  this.ListView.init($("dlgSearchUsers.Users"), Lang.DialogSearchUsers.NoResults);
  this.ListView.onLoadData = CGDialogSearchUsers.prototype.atRefreshListView.bind(this);

  Event.observe($("dlgSearchUsers.Filter"), "keypress", CGDialogSearchUsers.prototype.atFilterConditionKeyPress.bind(this));
  this.filter();
};

//------------------------------------------------------------------
CGDialogSearchUsers.prototype.show = function () {
  this.Layer.show();
};

//------------------------------------------------------------------
CGDialogSearchUsers.prototype.hide = function () {
  this.Layer.hide();
};

//------------------------------------------------------------------
CGDialogSearchUsers.prototype.destroy = function () {
};

//------------------------------------------------------------------
CGDialogSearchUsers.prototype.addListeners = function () {
  var extUsers = Ext.get("dlgSearchUsers.Users");
  aLinks = extUsers.select("a");

  aLinks.each(function (extLink) {
    Event.observe(extLink.dom, "click", CGDialogSearchUsers.prototype.atItemClick.bind(this, extLink.dom));
  }, this);

};

//------------------------------------------------------------------
CGDialogSearchUsers.prototype.refresh = function () {
};

//------------------------------------------------------------------
CGDialogSearchUsers.prototype.filter = function (Item) {
  this.ListView.filter($("dlgSearchUsers.Filter").value);
};

//------------------------------------------------------------------
CGDialogSearchUsers.prototype.check = function () {
};

//==================================================================
CGDialogSearchUsers.prototype.atFilterConditionKeyPress = function () {
  window.clearTimeout(this.idTimeoutFilter);
  this.idTimeoutFilter = window.setTimeout(this.filter.bind(this), 200);
};

//==================================================================
CGDialogSearchUsers.prototype.atItemClick = function (DOMItem, EventLaunched) {
  var CommandInfo = new CGCommandInfo(DOMItem.href);
  var aParameters = CommandInfo.getParameters();
  var User = this.Target.getUser(aParameters[0]);

  if (User != null) {
    this.Users = new Array(User);
    if (this.onAccept) this.onAccept();
  }

  Event.stop(EventLaunched);
  return false;
};

//==================================================================
CGDialogSearchUsers.prototype.atAccept = function () {
  if (this.onAccept) this.onAccept();
};

//==================================================================
CGDialogSearchUsers.prototype.atCancel = function () {
  if (this.onCancel) this.onCancel();
};

//==================================================================
CGDialogSearchUsers.prototype.atRefreshListView = function (aRecords) {
  this.ListView.removeAllElements();
  this.Target = new CGUserList();
  for (var iPos = 0; iPos < aRecords.length; iPos++) {
    var User = new CGUser(aRecords[iPos].json);
    var aUser = User.toArray();
    if (aUser.sEmail == "") aUser.sEmail = "&nbsp;";
    this.ListView.addElement(aUser);
    this.Target.addUser(User);
  }
  CommandListener.capture($("dlgSearchUsers.Users"));
  this.addListeners();
};