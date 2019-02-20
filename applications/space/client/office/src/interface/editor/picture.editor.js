CGEditorPicture = function () {
  this.base = CGEditor;
  this.base();
};

CGEditorPicture.prototype = new CGEditor;

//private
CGEditorPicture.prototype.addBehaviours = function () {
  this.aDialogs[PICTURE_UPLOAD].onSelect = this.atSelect.bind(this);
  this.aDialogs[PICTURE_UPLOAD].onSelectMultiple = this.atSelectMultiple.bind(this);
};

//public
CGEditorPicture.prototype.init = function (DOMEditor) {
  this.extEditor = Ext.get(DOMEditor);

  var extDialogPictureUpload = this.extEditor.select(CSS_EDITOR_DIALOG_PICTURE_UPLOAD).first();

  this.aDialogs[PICTURE_UPLOAD] = new CGEditorDialogPictureUpload(extDialogPictureUpload);
  this.setDialogMain(PICTURE_UPLOAD);

  CGEditor.prototype.init.call(this, DOMEditor);

  this.extDownload = this.extEditor.select(CSS_EDITOR_DOWNLOAD).first();
  if (this.extDownload) Event.observe(this.extDownload.dom, "click", CGEditorPicture.prototype.atDownload.bind(this));

  this.addBehaviours();
};

// #############################################################################################################
CGEditorPicture.prototype.atDownload = function (oEvent) {
  this.extDownload.dom.target = "_blank";
  if (this.onDownload) this.onDownload(this.extDownload.dom);
  if (this.extDownload.dom.stop) {
    Event.stop(oEvent);
    return false;
  }
  return true;
};