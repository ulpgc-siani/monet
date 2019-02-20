CGDialogSearchRoles = function () {
  this.base = CGDialog;
  this.base("dlgSearchRoles");
  this.ListView = null;
};

//------------------------------------------------------------------
CGDialogSearchRoles.prototype = new CGDialog;

//------------------------------------------------------------------
CGDialogSearchRoles.prototype.init = function (sLayerName) {
  if (!sLayerName) return;

  this.Layer = $(sLayerName);

  var html = AppTemplate.DialogSearchRoles;
  html = translate(html, Lang.DialogSearchRoles);

  this.Layer.innerHTML = html;

  var template = AppTemplate.DialogSearchRolesItem;
  template = translate(template, Lang.DialogSearchRolesItem);

  this.ListView = new TListView();
  this.ListView.setTemplate(template);
  this.ListView.setUrl(Context.Config.Api + "?op=searchroles");
  this.ListView.setMaxPageItems(-1);
  this.ListView.init($("dlgSearchRoles.Roles"), Lang.DialogSearchRoles.NoResults);
  this.ListView.onLoadData = CGDialogSearchRoles.prototype.atRefreshListView.bind(this);

  Event.observe($("dlgSearchRoles.Filter"), "keypress", CGDialogSearchRoles.prototype.atFilterConditionKeyPress.bind(this));
  this.filter();
};

//------------------------------------------------------------------
CGDialogSearchRoles.prototype.show = function () {
  this.Layer.show();
};

//------------------------------------------------------------------
CGDialogSearchRoles.prototype.hide = function () {
  this.Layer.hide();
};

//------------------------------------------------------------------
CGDialogSearchRoles.prototype.destroy = function () {
};

//------------------------------------------------------------------
CGDialogSearchRoles.prototype.addListeners = function () {
  var extRoles = Ext.get("dlgSearchRoles.Roles");
  aLinks = extRoles.select("a");

  aLinks.each(function (extLink) {
    Event.observe(extLink.dom, "click", CGDialogSearchRoles.prototype.atItemClick.bind(this, extLink.dom));
  }, this);

};

//------------------------------------------------------------------
CGDialogSearchRoles.prototype.refresh = function () {
};

//------------------------------------------------------------------
CGDialogSearchRoles.prototype.filter = function (Item) {
  this.ListView.filter($("dlgSearchRoles.Filter").value);
};

//------------------------------------------------------------------
CGDialogSearchRoles.prototype.check = function () {
};

//==================================================================
CGDialogSearchRoles.prototype.atFilterConditionKeyPress = function () {
  window.clearTimeout(this.idTimeoutFilter);
  this.idTimeoutFilter = window.setTimeout(this.filter.bind(this), 200);
};

//==================================================================
CGDialogSearchRoles.prototype.atItemClick = function (DOMItem, EventLaunched) {
  var CommandInfo = new CGCommandInfo(DOMItem.href);
  var aParameters = CommandInfo.getParameters();
  var User = this.Target.getUser(aParameters[0]);

  if (User != null) {
    this.Roles = new Array(User);
    if (this.onAccept) this.onAccept();
  }

  Event.stop(EventLaunched);
  return false;
};

//==================================================================
CGDialogSearchRoles.prototype.atAccept = function () {
  if (this.onAccept) this.onAccept();
};

//==================================================================
CGDialogSearchRoles.prototype.atCancel = function () {
  if (this.onCancel) this.onCancel();
};

//==================================================================
CGDialogSearchRoles.prototype.atRefreshListView = function (aRecords) {
  this.ListView.removeAllElements();
  this.Target = new CGUserList();
  for (var iPos = 0; iPos < aRecords.length; iPos++) {
    var User = new CGUser(aRecords[iPos].json);
    var aUser = User.toArray();
    if (aUser.sEmail == "") aUser.sEmail = "&nbsp;";
    this.ListView.addElement(aUser);
    this.Target.addUser(User);
  }
  CommandListener.capture($("dlgSearchRoles.Roles"));
  this.addListeners();
};