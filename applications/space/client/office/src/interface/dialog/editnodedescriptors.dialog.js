CGDialogEditNodeDescriptors = function () {
  this.base = CGDialog;
  this.base("dlgEditNodeDescriptors");
  this.dialog = null;
  this.Descriptors = null;
};

//------------------------------------------------------------------
CGDialogEditNodeDescriptors.prototype = new CGDialog;

//------------------------------------------------------------------
CGDialogEditNodeDescriptors.prototype.init = function () {

  var html = AppTemplate.DialogEditNodeDescriptors;
  html = translate(html, Lang.DialogEditNodeDescriptors);

  this.layer = new Insertion.Bottom(document.body, html).element.immediateDescendants().last();

  this.initRequiredFields();
  this.initDialog();
};

//------------------------------------------------------------------
CGDialogEditNodeDescriptors.prototype.setDescriptors = function (Descriptors) {
  this.Descriptors = Descriptors;
};

//------------------------------------------------------------------
CGDialogEditNodeDescriptors.prototype.check = function () {
  var sMessage = EMPTY;
  var DOMLabel = $("dlgEditNodeDescriptors.label");

  DOMLabel.removeClassName("error");
  if (DOMLabel.value == EMPTY) {
    sMessage += "<li>" + Lang.DialogEditNodeDescriptors.Error.LabelRequired + "</li>";
    DOMLabel.addClassName("error");
  }

  if (sMessage != EMPTY) {
    this.showStatus("<ul>" + sMessage + "</ul>");
  }
  return (sMessage == EMPTY);
};

//------------------------------------------------------------------
CGDialogEditNodeDescriptors.prototype.refresh = function () {
  if (!this.dialog) return false;

  $("dlgEditNodeDescriptors.label").value = this.Descriptors[DESCRIPTOR_LABEL];
  $("dlgEditNodeDescriptors.description").value = this.Descriptors[DESCRIPTOR_DESCRIPTION];
};

//==================================================================
CGDialogEditNodeDescriptors.prototype.atAccept = function () {
  if (!this.check()) return;

  this.Descriptors[DESCRIPTOR_LABEL] = $("dlgEditNodeDescriptors.label").value;
  this.Descriptors[DESCRIPTOR_DESCRIPTION] = $("dlgEditNodeDescriptors.description").value;

  this.hide();

  if (this.onAccept) this.onAccept();
};