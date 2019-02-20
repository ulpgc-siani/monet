CGEditorDialogFileUpload = function (extLayer) {
  this.base = CGEditorDialog;
  this.base(extLayer);
  this.extForm = null;
  this.Limit = "-1";
  this.init();
};

CGEditorDialogFileUpload.prototype = new CGEditorDialog;

CGEditorDialogFileUpload.prototype.showUploading = function () {
  this.extUploading.dom.innerHTML = WidgetWaitingTemplate.evaluate({'ImagesPath': Context.Config.ImagesPath, 'cls': CLASS_EDITOR_WAITING_MESSAGE, 'message': Lang.Editor.Dialogs.FileUpload.Uploading});
  this.extUploading.dom.style.display = "block";
};

CGEditorDialogFileUpload.prototype.checkFileSize = function () {
    var limit = this.Limit;
    if (limit == null || limit == "-1" || limit === "") return true;
    var files = this.extInputFile.dom.files;
    var size = 0;
    for (var i=0; i<files.length; i++) size += files[i].size;
    return size <= (limit * 1024 * 1024);
};

CGEditorDialogFileUpload.prototype.showFileSizeExceeded = function () {
    this.extFileSizeExceeded.dom.innerHTML = WidgetFileSizeExceededTemplate.evaluate({'size': this.Limit});
    this.extFileSizeExceeded.dom.style.display = "block";
};

CGEditorDialogFileUpload.prototype.hideFileSizeExceeded = function () {
    this.extFileSizeExceeded.dom.style.display = "none";
};

CGEditorDialogFileUpload.prototype.showUploadingError = function () {
  this.extUploading.dom.innerHTML = WidgetWaitingTemplate.evaluate({'ImagesPath': Context.Config.ImagesPath, 'cls': CLASS_EDITOR_WAITING_ERROR, 'message': Lang.Editor.Dialogs.FileUpload.UploadingFailed});
  setTimeout(this.hideUploadingError.bind(this), 1000, this);
};

CGEditorDialogFileUpload.prototype.hideUploadingError = function () {
  this.extUploading.dom.style.display = "none";
};

//public
CGEditorDialogFileUpload.prototype.init = function () {
  var extDialog;

  if (!this.extLayer) return;

  extDialog = (this.extLayer.down(CSS_EDITOR_DIALOG_ELEMENT_DIALOG));

  this.extLabel = extDialog.down("div " + CSS_EDITOR_DIALOG_ELEMENT_LABEL);
  this.extForm = extDialog.down("div " + CSS_EDITOR_DIALOG_ELEMENT_FORM);
  this.extInputFile = this.extForm.down(CSS_EDITOR_DIALOG_ELEMENT_INPUT_FILE);
  this.extUploading = extDialog.down(CSS_EDITOR_DIALOG_ELEMENT_UPLOADING);
  this.extFileSizeExceeded = extDialog.down(CSS_EDITOR_DIALOG_ELEMENT_FILE_SIZE_EXCEEDED);

  this.extLabel.on("change", this.atLabelChange, this);
  this.extInputFile.on("change", this.atUploadFile, this);
  this.extInputFile.dom.multiple = false;
};

CGEditorDialogFileUpload.prototype.setConfiguration = function (Config) {
  this.Limit = Config.Limit;
  this.extForm.dom.action = Config.Action;
  this.extLabel.dom.value = Config.Label;
  this.extInputFile.dom.multiple = Config.MultipleSelection;
};

CGEditorDialogFileUpload.prototype.isMultiple = function(files) {
  return files.length > 1;
};

CGEditorDialogFileUpload.prototype.notifySelect = function(files, label) {
  var event = this.isMultiple(files) ? this.onSelectMultiple : this.onSelect;
  var values = this.createValues(files, label);

  if (event == null)
    return;

  event(this.isMultiple(files) ? values : values[0]);
};

CGEditorDialogFileUpload.prototype.createValues = function(files, label) {
  var result = new Array();

  for (var i=0; i<files.length; i++) {
    var fieldId = HtmlUtil.decode(files[i]);
    result.push({code: fieldId, value: i == 0 ? label : fieldId });
  }

  return result;
};

// #############################################################################################################

CGEditorDialogFileUpload.prototype.atUploadFile = function () {
  var fileInfo = null;

  if(!this.checkFileSize()) {
      this.showFileSizeExceeded();
      return;
  }

  this.hideFileSizeExceeded();
  this.showUploading();

  Ext.Ajax.request({
    url: this.extForm.dom.action,
    method: "POST",
    success: function (response, options) {
      fileInfo = eval('(' + response.responseText + ')');
      if (fileInfo.status == 0) {
        this.extUploading.dom.style.display = 'none';
        this.extInputFile.dom.value = "";
        this.extLabel.dom.value = "";
        this.notifySelect(fileInfo.files, this.extLabel.getValue());
      }
      else {
        this.showUploadingError();
      }
    },
    failure: function (response, options) {
      this.showUploadingError();
    },
    isUpload: true,
    form: this.extForm.dom,
    scope: this
  });

};

CGEditorDialogFileUpload.prototype.atLabelChange = function () {
  if (this.onLabelChange) this.onLabelChange(this.extLabel.dom.value);
};