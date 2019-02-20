CGEditorFile = function () {
  this.base = CGEditor;
  this.base();
};

CGEditorFile.prototype = new CGEditor;

//private
CGEditorFile.prototype.addBehaviours = function () {
  this.aDialogs[FILE_UPLOAD].onSelect = this.atSelect.bind(this);
  this.aDialogs[FILE_UPLOAD].onSelectMultiple = this.atSelectMultiple.bind(this);
  this.aDialogs[FILE_UPLOAD].onLabelChange = this.atLabelChange.bind(this);
};

//public
CGEditorFile.prototype.init = function (DOMEditor) {
  this.extEditor = Ext.get(DOMEditor);

  var extDialogFileUpload = this.extEditor.select(CSS_EDITOR_DIALOG_FILE_UPLOAD).first();

  this.aDialogs[FILE_UPLOAD] = new CGEditorDialogFileUpload(extDialogFileUpload);
  this.setDialogMain(FILE_UPLOAD);

  CGEditor.prototype.init.call(this, DOMEditor);

  this.extDownload = this.extEditor.select(CSS_EDITOR_DOWNLOAD).first();
  if (this.extDownload) Event.observe(this.extDownload.dom, "click", CGEditorFile.prototype.atDownload.bind(this));

  this.addBehaviours();
};

// #############################################################################################################
CGEditorFile.prototype.atDownload = function (oEvent) {
  this.extDownload.dom.target = "_blank";
  if (this.onDownload) this.onDownload(this.extDownload.dom);
  if (this.extDownload.dom.stop) {
    Event.stop(oEvent);
    return false;
  }
  return true;
};

CGEditorFile.prototype.atLabelChange = function (label) {
  if (this.onLabelChange) this.onLabelChange(label);
};