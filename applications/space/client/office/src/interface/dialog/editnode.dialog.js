CGDialogEditNode = function () {
  this.base = CGDialog;
  this.base("dlgEditNode");
  this.dialog = null;
  this.Node = null;
};

//------------------------------------------------------------------
CGDialogEditNode.prototype = new CGDialog;

//------------------------------------------------------------------
CGDialogEditNode.prototype.init = function () {

  var html = AppTemplate.DialogEditNode;
  html = translate(html, Lang.DialogEditNode);

  this.layer = new Insertion.Bottom(document.body, html).element.immediateDescendants().last();

  this.initRequiredFields();
  this.initDialog();
};

//------------------------------------------------------------------
CGDialogEditNode.prototype.setNode = function (Node) {
  this.Node = Node;
};

//------------------------------------------------------------------
CGDialogEditNode.prototype.refresh = function () {
  if (!this.dialog) return false;

  if (this.Node.dom.getHtmlDialog) {
    var sContent = this.Node.dom.getHtmlDialog();
    $("dlgEditNode.dialogbox").innerHTML = (sContent != false) ? sContent : Lang.DialogEditNode.NoFields;
  }
  else $("dlgEditNode.dialogbox").innerHTML = Lang.DialogEditNode.NoFields;

  CommandListener.capture($("dlgEditNode.dialogbox"));
  Constructor = Extension.getNodeConstructor();
  Constructor.init($("dlgEditNode.dialogbox"));

  this.dialog.buttons[0].enable();
};

//==================================================================
CGDialogEditNode.prototype.atAccept = function () {
  if (!this.check()) return;

  this.Fields = new Array();

  var extDialogBox = Ext.get("dlgEditNode.dialogbox");
  aFields = extDialogBox.select('.nodeFields input');
  aFields.each(function (Field) {
    if (!Field.dom.name) return;
    this.Fields.push({code: Field.dom.name, id: Field.dom.name, name: Field.dom.name, value: Field.dom.value});
  }, this);

  this.hide();

  if (this.onAccept) this.onAccept();
};