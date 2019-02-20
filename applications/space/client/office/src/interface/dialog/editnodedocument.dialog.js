CGDialogEditNodeDocument = function () {
  this.base = CGDialog;
  this.base("dlgEditNodeDocument");
  this.bDownloaded = false;
};

//------------------------------------------------------------------
CGDialogEditNodeDocument.prototype = new CGDialog;

//------------------------------------------------------------------
CGDialogEditNodeDocument.prototype.init = function (DOMLayer) {
  this.extLayer = Ext.get(DOMLayer);
  this.initWizard(DOMLayer);

  var extDownloadDocumentList = this.extLayer.select("a.download");
  extDownloadDocumentList.each(function (extDownloadDocument) {
    extDownloadDocument.on("click", this.atDownloadDocument, this);
  }, this);

  var extContinueEdition = this.extLayer.select("a.continue").first();
  extContinueEdition.on("click", this.atContinueEdition, this);

  var extReplaceDocument = this.extLayer.select("a.replace").first();
  extReplaceDocument.on("click", this.atReplaceDocument, this);

  var extFinishEditing = this.extLayer.select("a.finish").first();
  extFinishEditing.on("click", this.atFinishEditing, this);

  var extCancelList = this.extLayer.select("a.cancel");
  extCancelList.each(function (extCancel) {
    extCancel.on("click", this.atCancel, this);
  }, this);

  var extFile = Ext.get("DialogEditNodeDocument.form").select(".file").first();
  extFile.on("change", this.atFileChange, this);
};

//------------------------------------------------------------------
CGDialogEditNodeDocument.prototype.initWizard = function (DOMLayer) {
  var html = AppTemplate.DialogEditNodeDocument;

  this.Wizard = new CGWizard();
  this.Wizard.init(translate(html, Lang.DialogEditNodeDocument), DOMLayer, false);
  this.Wizard.onCancel = this.atCancel.bind(this);
  this.Wizard.onAccept = this.atAccept.bind(this);
  this.Wizard.onNextStep = this.atNextStep.bind(this);
};

//------------------------------------------------------------------
CGDialogEditNodeDocument.prototype.show = function () {
  var extStatus = Ext.get("dlgEditNodeDocument.status");
  extStatus.hide();
};

//------------------------------------------------------------------
CGDialogEditNodeDocument.prototype.refresh = function () {
};

//------------------------------------------------------------------
CGDialogEditNodeDocument.prototype.check = function () {
  var sMessage = EMPTY;
  var extForm = Ext.get("DialogEditNodeDocument.form");
  var extFile = extForm.select(".file").first();

  extFile.removeClass("error");
  if (extFile.dom.value == "") {
    sMessage += "<li>" + Lang.DialogEditNodeDocument.Error.FileRequired + "</li>";
    extFile.addClass("error");
  }

  if (sMessage != EMPTY) {
    this.showStatus("<ul>" + sMessage + "</ul>");
  }
  else this.hideStatus();

  return (sMessage == EMPTY);
};

CGDialogEditNodeDocument.prototype.nextStep = function () {
  this.Wizard.nextStep();
};

//==================================================================
CGDialogEditNodeDocument.prototype.atAccept = function () {
  if (!this.check()) return;

  this.DownloadPreference = false;
  this.EditPreference = false;
  this.FileForm = $("DialogEditNodeDocument.form");

  this.hide();

  if (this.onAccept) this.onAccept();
};

CGDialogEditNodeDocument.prototype.atCancel = function () {
  this.hide();
  this.destroy();
  if (this.onCancel) this.onCancel();
};

CGDialogEditNodeDocument.prototype.atNextStep = function () {
  if (this.Wizard.getActiveStep().id == 2 && !this.bDownloaded) {
    CommandListener.throwCommand("downloadnode(" + this.Target.IdNode + ")");
    this.bDownloaded = true;
  }
};

CGDialogEditNodeDocument.prototype.atDownloadDocument = function () {
  CommandListener.throwCommand("downloadnode(" + this.Target.IdNode + ")");

  if (this.autoStepTimeout)
    window.clearTimeout(this.autoStepTimeout);

  this.autoStepTimeout = window.setTimeout(this.nextStep.bind(this), 5000);

  if (this.bDownloaded)
    return;

  this.bDownloaded = true;
  window.setTimeout(this.nextStep.bind(this), 800);
};

CGDialogEditNodeDocument.prototype.atContinueEdition = function () {
  this.bDownloaded = true;
  this.Wizard.showStep(4);
};

CGDialogEditNodeDocument.prototype.atReplaceDocument = function () {
  this.Wizard.nextStep();
};

CGDialogEditNodeDocument.prototype.atFinishEditing = function () {
  this.atAccept();
};

CGDialogEditNodeDocument.prototype.atFileChange = function () {
  this.check();
};