CGDialogSelectTaskOwner = function () {
  this.base = CGDialog;
  this.base("dlgSelectTaskOwner");
  this.doHide = true;
  this.User = null;
  this.Reason = "";
};

//------------------------------------------------------------------
CGDialogSelectTaskOwner.prototype = new CGDialog;

//------------------------------------------------------------------
CGDialogSelectTaskOwner.prototype.init = function (sLayerName) {
  if (!sLayerName) return;

  this.Layer = $(sLayerName);

  var html = AppTemplate.DialogSelectTaskOwner;
  html = translate(html, Lang.DialogSelectTaskOwner);

  this.Layer.innerHTML = html;

  var extLayer = Ext.get(this.Layer);

  this.initTabs();

  var extContainer = extLayer.select(".dialog.selecttaskowner").first();
  var extComponent = extLayer.select(".dialog.selecttaskowner .component").first();

  this.extClearValue = extLayer.select(".dialog.selecttaskowner .clearvalue").first();
  this.extClearValue.on("click", this.atClearValue, this);

  extComponent.on("focus", this.atUserComponentFocus, this);
  extComponent.on("keyup", this.atUserComponentKeyUp, this);

  var extReason = extLayer.select(".dialog.selecttaskowner .reason");
  extReason.on("focus", this.atReasonFocus, this);
  extReason.on("click", this.atReasonFocus, this);
  extReason.on("dblclick", this.atReasonFocus, this);
  extReason.on("change", this.atReasonChange, this);

  var extDataStore = new Ext.data.Store({
    proxy: new Ext.data.HttpProxy({ url: Kernel.getLoadUsersLink() }),
    reader: new Ext.data.JsonReader({root: 'rows', totalProperty: 'nrows'}, [
      {name: 'id'},
      {name: 'label'},
      {name: 'email'}
    ]),
    remoteSort: true
  });
  extDataStore.isRemote = function () {
    return false;
  };

  var extColumnModel = new Ext.grid.ColumnModel([
    {header: Lang.DialogSelectTaskOwner.Users, dataIndex: 'code', hidden: true},
    {header: Lang.DialogSelectTaskOwner.User, dataIndex: 'label'},
    {header: Lang.DialogSelectTaskOwner.Email, dataIndex: 'email'}
  ]);

  this.dialog = new CGEditorDialogGrid(extContainer);
  this.dialog.setRenderFooter(true);
  this.dialog.setConfiguration({Store: extDataStore, ColumnModel: extColumnModel});
  this.dialog.onSelect = CGDialogSelectTaskOwner.prototype.atSelect.bind(this);

  var extAccept = extLayer.select(".dialog.selecttaskowner .accept");
  extAccept.on("click", this.atAccept, this);

  this.filter();
  this.checkAcceptEnable();
};

CGDialogSelectTaskOwner.prototype.initTabs = function () {
	var extLayer = Ext.get(this.Layer);
	var extTabPanel = extLayer.select(".dialog.selecttaskowner .tabs").first();
	var extTabUsername = extLayer.select(".dialog.selecttaskowner .tab_username").first();
	var extTabReason = extLayer.select(".dialog.selecttaskowner .tab_reason").first();

	extTabUsername.dom.id = Ext.id();
	extTabReason.dom.id = Ext.id();

	this.extTabPanel = new Ext.TabPanel(extTabPanel);
	this.extTabPanel.on("click", this.atTabPanelFocus, this);
	this.extTabPanel.on("focus", this.atTabPanelFocus, this);
	this.extTabPanel.on("tabchange", this.atTabPanelFocus, this);
	this.extTabPanel.addTab(extTabUsername.dom.id, Lang.DialogSelectTaskOwner.Username);
	this.extTabPanel.addTab(extTabReason.dom.id, Lang.DialogSelectTaskOwner.Reason);

	this.extTabPanel.activate(extTabUsername.dom.id);
};

CGDialogSelectTaskOwner.prototype.checkAcceptEnable = function () {
  var extLayer = Ext.get(this.Layer);
  var extAccept = extLayer.select(".dialog.selecttaskowner .accept").first();

  if (this.User != null) extAccept.removeClass("disabled");
  else extAccept.addClass("disabled");

  return !extAccept.hasClass("disabled");
};

CGDialogSelectTaskOwner.prototype.filter = function () {
  var extLayer = Ext.get(this.Layer);
  var extUsername = extLayer.select(".dialog.selecttaskowner .component").first();
  var sUsername = extUsername.dom.value;
  this.extClearValue.dom.style.display = (sUsername != "") ? "block" : "none";
  this.dialog.setData(sUsername);
  this.dialog.refresh();
};

CGDialogSelectTaskOwner.prototype.hideDialogOnTimer = function () {
  if (!this.doHide) {
    this.doHide = true;
    return;
  }
  this.hideDialogTimer = window.setTimeout(this.atCancel.bind(this), 100);
};

CGDialogSelectTaskOwner.prototype.clearHideDialogTimer = function () {
  window.clearTimeout(this.hideDialogTimer);
  return false;
};

CGDialogSelectTaskOwner.prototype.show = function () {
  var extLayer = Ext.get(this.Layer);

  Ext.get(document.body).on("click", this.hideDialogOnTimer, this);
  extLayer.on("click", this.clearHideDialogTimer, this);

  if (this.target != null && this.target.reason != null) {
	  var extReason = extLayer.select(".dialog.selecttaskowner .reason").first();
	  extReason.dom.value = this.target.reason;
	  this.Reason = this.target.reason;
  }

  extLayer.show();
};

CGDialogSelectTaskOwner.prototype.hide = function () {
  var extLayer = Ext.get(this.Layer);
  Ext.get(document.body).un("click", this.hideDialogOnTimer, this);
  extLayer.hide();
  this.dialog.hide();
};

//==================================================================
CGDialogSelectTaskOwner.prototype.atAccept = function () {
  if (this.checkAcceptEnable() == false) {
    this.doHide = false;
    return;
  }
  this.doHide = false;
  this.hide();
  if (this.onAccept) this.onAccept();
};

CGDialogSelectTaskOwner.prototype.atSelect = function (Data) {
  var extLayer = Ext.get(this.Layer);
  var extUsername = extLayer.select(".dialog.selecttaskowner .component").first();
  this.User = Data.value;
  extUsername.dom.value = Data.value;
  this.extClearValue.dom.style.display = "block";
  this.checkAcceptEnable();
  this.doHide = false;
  return false;
};

CGDialogSelectTaskOwner.prototype.atClearValue = function () {
  var extLayer = Ext.get(this.Layer);
  var extUsername = extLayer.select(".dialog.selecttaskowner .component").first();
  extUsername.dom.value = "";
  extUsername.dom.focus();
  this.extClearValue.dom.style.display = "none";
  this.filter();
  this.checkAcceptEnable();
  this.doHide = false;
};

CGDialogSelectTaskOwner.prototype.atUserComponentFocus = function (event) {
  this.doHide = false;
};

CGDialogSelectTaskOwner.prototype.atTabPanelFocus = function (event) {
	this.doHide = false;

	var extLayer = Ext.get(this.Layer);
	var extGrid = extLayer.select(".dialog.selecttaskowner .grid").first();
	extGrid.dom.style.width = "100%";
};

CGDialogSelectTaskOwner.prototype.atUserComponentKeyUp = function (event) {
  var codeKey = event.keyCode;
  var extLayer = Ext.get(this.Layer);
  var extUsername = extLayer.select(".dialog.selecttaskowner .component").first();

  this.doHide = false;

  window.clearTimeout(this.idTimeoutFilter);

  if (codeKey == event.UP) {
    this.dialog.moveUp(extUsername);
    return;
  }
  else if (codeKey == event.DOWN) {
    this.dialog.moveDown(extUsername);
    return;
  }
  else if ((codeKey == event.ENTER) || (codeKey == event.LEFT) || (codeKey == event.RIGHT) || (codeKey == event.SHIFT)) return;

  this.idTimeoutFilter = window.setTimeout(this.filter.bind(this), 200);
};

CGDialogSelectTaskOwner.prototype.atReasonFocus = function (event) {
  this.doHide = false;
};

CGDialogSelectTaskOwner.prototype.atReasonChange = function (event) {
  var extLayer = Ext.get(this.Layer);
  var extReason = extLayer.select(".dialog.selecttaskowner .reason").first();
  this.doHide = false;
  this.Reason = extReason.dom.value;
};
