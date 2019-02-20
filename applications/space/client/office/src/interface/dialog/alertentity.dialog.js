CGDialogAlertEntity = function () {
  this.base = CGDialog;
  this.base("dlgAlertEntity");
  this.UserListView = null;
  this.dlgSearchUsers = new CGDialogSearchUsers();
  this.dlgSearchRoles = new CGDialogSearchRoles();
  this.UserList = new CGUserList();
};

//------------------------------------------------------------------
CGDialogAlertEntity.prototype = new CGDialog;

//------------------------------------------------------------------
CGDialogAlertEntity.prototype.init = function () {
  var html;

  html = AppTemplate.DialogAlertEntity;
  html = translate(html, Lang.DialogAlertEntity);

  this.layer = new Insertion.Bottom(document.body, html).element.immediateDescendants().last();

  this.initDialog();
  this.initTabs();
  this.initUserListView();
  this.initSearchUsersDialog();
  this.initSearchRolesDialog();
};

//------------------------------------------------------------------
CGDialogAlertEntity.prototype.initTabs = function () {
  var tabs = new Ext.TabPanel("dlgAlertEntity.RoleUserTabs");
  tabs.addTab("dlgAlertEntity.UserTabs", "Usuarios");
  tabs.addTab("dlgAlertEntity.RoleTabs", "Roles");
  tabs.activate("dlgAlertEntity.UserTabs");
};

//------------------------------------------------------------------
CGDialogAlertEntity.prototype.initUserListView = function () {
  var template = AppTemplate.DialogAlertEntityUser;
  template = translate(template, Lang.DialogAlertEntityUser);

  this.UserListView = new TListView();
  this.UserListView.init($("dlgAlertEntity.UserList"), Lang.DialogAlertEntity.NoUsers);
  this.UserListView.setTemplate(template);
  this.UserListView.removeAllElements();
};

//------------------------------------------------------------------
CGDialogAlertEntity.prototype.initSearchUsersDialog = function () {
  this.dlgSearchUsers.init('dlgAlertEntity.DialogSearchUsers');
  this.dlgSearchUsers.onAccept = this.atAddUsers.bind(this);
};

//------------------------------------------------------------------
CGDialogAlertEntity.prototype.initSearchRolesDialog = function () {
  this.dlgSearchRoles.init('dlgAlertEntity.DialogSearchRoles');
  this.dlgSearchRoles.onAccept = this.atAddRoles.bind(this);
};

//------------------------------------------------------------------
CGDialogAlertEntity.prototype.destroy = function () {
  this.dlgSearchUsers.destroy();
  this.dlgSearchRoles.destroy();
  this.dialog.destroy(true);
};

//------------------------------------------------------------------
CGDialogAlertEntity.prototype.show = function () {
  var DOMStatus = $(this.sName + ".status");
  var extLayout = Ext.get(this.sName);

  if (this.dialog == null) return;
  if (!DOMStatus) return;
  if (!extLayout) return;

  DOMStatus.style.display = "none";
  this.dialog.show();
};

//------------------------------------------------------------------
CGDialogAlertEntity.prototype.refresh = function () {
};

//------------------------------------------------------------------
CGDialogAlertEntity.prototype.addListeners = function (DOMElement) {
  var extElement = Ext.get(DOMElement);
  aLinks = extElement.select("a");

  aLinks.each(function (extLink) {
    Event.observe(extLink.dom, "click", CGDialogAlertEntity.prototype.atUserItemClick.bind(this, extLink.dom));
  }, this);
};

//==================================================================
CGDialogAlertEntity.prototype.check = function () {
  var sMessage = EMPTY;

  if (this.UserList.getUsers().size() == 0) {
    sMessage += "<li>" + Lang.DialogAlertEntity.Error.NoUsers + "</li>";
  }

  if (sMessage != EMPTY) {
    this.showStatus("<ul>" + sMessage + "</ul>");
  }

  return (sMessage == EMPTY);
};

//==================================================================
CGDialogAlertEntity.prototype.atAccept = function () {
  if (!this.check()) return;

  this.Message = $("dlgAlertEntity.Message").value;

  this.hide();

  if (this.onAccept) this.onAccept();
};

//==================================================================
CGDialogAlertEntity.prototype.atUserItemClick = function (DOMItem, EventLaunched) {
  var extItem = Ext.get(DOMItem);
  var CommandInfo = new CGCommandInfo(DOMItem.href);
  var aParameters = CommandInfo.getParameters();

  if (DOMItem) {
    this.UserListView.removeElement(extItem.up("li").dom);
    delete this.UserList.deleteUser(aParameters[0]);
  }

  Event.stop(EventLaunched);
  return false;
};

//==================================================================
CGDialogAlertEntity.prototype.atAddUser = function (Record) {
  var aUser, User = new CGUser();
  var Data = Record.data;
  var Info = User.getInfo();
  var DOMElement;

  User.setId(Record.id);
  Info.setFullname(Data.fullname);
  Info.setEmail(Data.email);
  Info.setPhoto(Data.photo);
  User.setInfo(Info);

  aUser = User.toArray();
  DOMElement = this.UserListView.addElement(aUser);

  this.UserList.addUser(User);
  this.addListeners(DOMElement);
};

//==================================================================
CGDialogAlertEntity.prototype.atAddUsers = function () {
  var aSelectedUsers = this.dlgSearchUsers.Users;

  for (var iPos in aSelectedUsers) {
    if (isFunction(aSelectedUsers[iPos])) continue;
    var User = aSelectedUsers[iPos];

    if (this.UserList.getUser(User.getId()) != null) continue;

    var aUser = User.toArray();
    var DOMElement = this.UserListView.addElement(aUser);

    this.UserList.addUser(User);
    this.addListeners(DOMElement);
  }

  return false;
};

//==================================================================
CGDialogAlertEntity.prototype.atAddRoles = function () {
  var aSelectedRoles = this.dlgSearchRoles.Roles;

  for (var iPos in aSelectedRoles) {
    if (isFunction(aSelectedRoles[iPos])) continue;
    var Role = aSelectedRoles[iPos];

    if (this.UserList.getUser(Role.getId()) != null) continue;

    var aRole = Role.toArray();
    var DOMElement = this.UserListView.addElement(aRole);

    this.UserList.addUser(Role);
    this.addListeners(DOMElement);
  }

  return false;
};