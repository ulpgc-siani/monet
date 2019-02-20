CGDialogShareNode = function () {
  this.base = CGDialog;
  this.base("dlgShareNode");
  this.dlgSearchUsers = new CGDialogSearchUsers();
  this.UserListView = null;
  this.extDate = null;
  this.UserList = new CGUserList();
};

//------------------------------------------------------------------
CGDialogShareNode.prototype = new CGDialog;

//------------------------------------------------------------------
CGDialogShareNode.prototype.init = function () {
  var html;

  html = AppTemplate.DialogShareNode;
  html = translate(html, Lang.DialogShareNode);

  this.layer = new Insertion.Bottom(document.body, html).element.immediateDescendants().last();

  this.initDialog();
  this.initUserListView();
  this.initSearchDialog();
  this.initDate();
};

//------------------------------------------------------------------
CGDialogShareNode.prototype.initDialog = function () {
  var EastRegion;

  this.dialog = new Ext.LayoutDialog(this.layer, {
    modal: true, shadow: true, minWidth: 300, minHeight: 300, closable: false,
    center: { autoScroll: true, tabPosition: 'top', alwaysShowTabs: true }
  });

  this.dialog.addKeyListener(27, this.atCancel, this);
  this.dialog.addButton(Lang.Buttons.Accept, this.atAccept, this);
  this.dialog.addButton(Lang.Buttons.Cancel, this.atCancel, this);

  var layout = this.dialog.getLayout();
  var PanelInfo = new Ext.ContentPanel('dlgShareNode.Info', {title: Lang.DialogShareNode.PanelInfo});
  var PanelUsers = new Ext.ContentPanel('dlgShareNode.Users', {title: Lang.DialogShareNode.PanelUsers});

  PanelInfo.Id = "dlgShareNode.Info";
  PanelUsers.Id = "dlgShareNode.Users";

  layout.beginUpdate();
  layout.add('center', PanelInfo);
  layout.add('center', PanelUsers);
  layout.getRegion('center').showPanel('dlgShareNode.Info');
  layout.endUpdate();
  layout.getRegion('center').on("panelactivated", this.atPanelActivated.bind(this));
};

//------------------------------------------------------------------
CGDialogShareNode.prototype.initUserListView = function () {
  var template = AppTemplate.DialogShareNodeUser;
  template = translate(template, Lang.DialogShareNodeUser);

  this.UserListView = new TListView();
  this.UserListView.init($("dlgShareNode.UserList"), Lang.DialogShareNode.NoUsers);
  this.UserListView.setTemplate(template);
  this.UserListView.removeAllElements();
};

//------------------------------------------------------------------
CGDialogShareNode.prototype.initSearchDialog = function () {
  this.dlgSearchUsers.init('dlgShareNode.DialogSearchUsers');
  this.dlgSearchUsers.onAccept = this.atAddUsers.bind(this);
};

//------------------------------------------------------------------
CGDialogShareNode.prototype.initDate = function () {
  this.extDate = new Ext.form.DateField({
    format: 'd/m/Y',
    altFormats: 'd-m-y|d/m/y|d-m-Y|Y-m-d|Y/m/d|Y-m|Y/m|m-Y|m/Y|Y',
    minLength: 4
  });

  this.extDate.applyTo($("dlgShareNode.ExpireDate"));
};

//------------------------------------------------------------------
CGDialogShareNode.prototype.destroy = function () {
  this.dlgSearchUsers.destroy();
  this.dialog.destroy(true);
};

//------------------------------------------------------------------
CGDialogShareNode.prototype.show = function () {
  var DOMStatus = $(this.sName + ".status");
  var extLayout = Ext.get(this.sName);
  var extInput;

  if (this.dialog == null) return;
  if (!DOMStatus) return;
  if (!extLayout) return;

  DOMStatus.style.display = "none";
  this.dialog.show();
};

//------------------------------------------------------------------
CGDialogShareNode.prototype.refresh = function () {
};

//------------------------------------------------------------------
CGDialogShareNode.prototype.addListeners = function (DOMElement) {
  var extElement = Ext.get(DOMElement);
  aLinks = extElement.select("a");

  aLinks.each(function (extLink) {
    Event.observe(extLink.dom, "click", CGDialogShareNode.prototype.atUserItemClick.bind(this, extLink.dom));
  }, this);
};

//==================================================================
CGDialogShareNode.prototype.atPanelActivated = function (LayoutRegion, Panel) {
  if (Panel.Id == "dlgShareNode.Info") $("dlgShareNode.Description").focus();
};

//==================================================================
CGDialogShareNode.prototype.atAccept = function () {
  var extDlgShareNode;
  var aInputs;

  if (!this.check()) return;

  this.Description = $("dlgShareNode.Description").value;
  this.ExpireDate = $("dlgShareNode.ExpireDate").value;

  this.hide();

  if (this.onAccept) this.onAccept();
};

//==================================================================
CGDialogShareNode.prototype.atUserItemClick = function (DOMItem, EventLaunched) {
  var extItem = Ext.get(DOMItem);
  var CommandInfo = new CGCommandInfo(DOMItem.href);
  var aParameters = CommandInfo.getParameters();

  if (DOMItem) {
    extLink = extItem.up("li");
    if (extLink) extLink.remove();
    this.UserList.deleteUser(aParameters[0]);
  }

  Event.stop(EventLaunched);
  return false;
};

//==================================================================
CGDialogShareNode.prototype.atAddUsers = function () {
  var UserList = this.dlgSearchUsers.Target;
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