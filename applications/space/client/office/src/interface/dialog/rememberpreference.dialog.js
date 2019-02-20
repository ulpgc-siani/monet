CGDialogRememberPreference = function () {
  this.base = CGDialog;
  this.base("dlgRememberPreference");
  this.dialog = null;
  this.Target = null;
};

//------------------------------------------------------------------
CGDialogRememberPreference.prototype = new CGDialog;

//------------------------------------------------------------------
CGDialogRememberPreference.prototype.init = function () {

  var html = AppTemplate.DialogRememberPreference;
  html = translate(html, Lang.DialogRememberPreference);

  this.layer = new Insertion.Bottom(document.body, html).element.immediateDescendants().last();

  this.initRequiredFields();
  this.initDialog();
};

//------------------------------------------------------------------
CGDialogRememberPreference.prototype.initDialog = function () {
  if (!this.layer) return;

  this.dialog = new Ext.LayoutDialog(this.layer, {
    modal: true, shadow: true, minWidth: 300, minHeight: 300, closable: false,
    center: { autoScroll: true }
  });

  this.dialog.addKeyListener(27, this.atCancel, this);
  this.dialog.addButton(Lang.Buttons.Yes, this.atYes, this);
  this.dialog.addButton(Lang.Buttons.No, this.atNo, this);
  this.dialog.addButton(Lang.Buttons.Cancel, this.atCancel, this);

  var layout = this.dialog.getLayout();
  layout.beginUpdate();
  layout.add('center', new Ext.ContentPanel(this.sName + ".center"));
  layout.endUpdate();
};

//------------------------------------------------------------------
CGDialogRememberPreference.prototype.refresh = function () {
  $("dlgRememberPreference.title").innerHTML = Lang.DialogRememberPreference.Title[this.Target];
  $("dlgRememberPreference.description").innerHTML = Lang.DialogRememberPreference.Description[this.Target];
  $("dlgRememberPreference.remember").checked = true;
};

//==================================================================
CGDialogRememberPreference.prototype.atYes = function () {
  if (!this.check()) return;

  this.RememberResult = $("dlgRememberPreference.remember").checked;
  this.hide();

  if (this.onYes) this.onYes();
};

//==================================================================
CGDialogRememberPreference.prototype.atNo = function () {
  if (!this.check()) return;

  this.RememberResult = ($("dlgRememberPreference.remember").value == "true") ? true : false;
  this.hide();

  if (this.onNo) this.onNo();
};