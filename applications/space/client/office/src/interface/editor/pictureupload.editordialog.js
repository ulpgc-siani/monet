CGEditorDialogPictureUpload = function (extLayer) {
  this.base = CGEditorDialog;
  this.base(extLayer);
  this.extToolbarUpload = null;
  this.extForm = null;
  this.init();
  this.bCropInitalized = false;
  this.CropBox = new Object();
  this.Limit = "-1";
  this.downloadActionTemplate = null;
};

CGEditorDialogPictureUpload.prototype = new CGEditorDialog;

//public
CGEditorDialogPictureUpload.prototype.init = function () {
  var extDialog;

  extDialog = (this.extLayer.down(CSS_EDITOR_DIALOG_ELEMENT_DIALOG));

  this.extInputFile = extDialog.select(CSS_EDITOR_DIALOG_ELEMENT_INPUT_FILE).first();
  this.extCropLayer = extDialog.select(CSS_EDITOR_DIALOG_ELEMENT_CROP_LAYER).first();
  this.extForm = extDialog.down(CSS_EDITOR_DIALOG_ELEMENT_FORM);
  this.extUploading = this.extLayer.select(CSS_EDITOR_DIALOG_ELEMENT_UPLOADING).first();
  this.extFileSizeExceeded = this.extLayer.select(CSS_EDITOR_DIALOG_ELEMENT_FILE_SIZE_EXCEEDED).first();

  this.extButtonUpload = extDialog.select(CSS_EDITOR_DIALOG_ELEMENT_BUTTON_UPLOAD).first();
  this.extButtonUpload.on("click", this.atButtonUploadClick, this);

  this.extPreview = this.extLayer.select(CSS_EDITOR_DIALOG_ELEMENT_PREVIEW).first();
  this.extPreview.on("load", CGEditorDialogPictureUpload.prototype.atLoadPicture.bind(this));
  this.extPreview.dom.style.maxHeight = 330 + "px";
  this.extPreview.dom.style.maxSize = 550 + "px";

  this.extSliceX = this.extLayer.select(CSS_EDITOR_DIALOG_ELEMENT_SLICE_X).first();
  this.extSliceY = this.extLayer.select(CSS_EDITOR_DIALOG_ELEMENT_SLICE_Y).first();
  this.extSliceWidth = this.extLayer.select(CSS_EDITOR_DIALOG_ELEMENT_SLICE_WIDTH).first();
  this.extSliceHeight = this.extLayer.select(CSS_EDITOR_DIALOG_ELEMENT_SLICE_HEIGHT).first();

  this.extSliceX.dom.onChange = CGEditorDialogPictureUpload.prototype.atCoordinatesChange.bind(this);
  this.extSliceX.on("change", this.atCoordinatesChange, this);

  this.extInputFile.on("change", this.atUploadFile, this);
  this.extInputFile.dom.multiple = false;
};

CGEditorDialogPictureUpload.prototype.setCropSize = function () {
  crop_imageWidth = this.extPreview.getWidth();
  crop_imageHeight = this.extPreview.getHeight();
  crop_originalImageWidth = this.extPreview.getWidth();
  crop_originalImageHeight = this.extPreview.getHeight();
  crop_script_alwaysPreserveAspectRatio = this.PreserveAspectRatio;
  this.extSliceX.dom.value = 0;
  this.extSliceY.dom.value = 0;
  this.extSliceWidth.dom.value = this.CropSize.Width;
  this.extSliceHeight.dom.value = this.CropSize.Height;
};

CGEditorDialogPictureUpload.prototype.initCropLayer = function () {
  if (this.bCropInitialized) return;

  this.setCropSize();
  init_imageCrop(this.extLayer.dom);
  cropScript_setCropSizeByInput();

  this.bCropInitialized = true;
};

CGEditorDialogPictureUpload.prototype.refreshCropLayer = function (FileInfo) {
  if (!this.bCropInitialized) this.initCropLayer();
  else {
    this.setCropSize();
    cropScript_setCropSizeByInput();
  }
};

CGEditorDialogPictureUpload.prototype.showCropLayer = function (Config) {
  this.extCropLayer.dom.style.display = "block";
  this.extPreview.dom.style.display = "block";
};

CGEditorDialogPictureUpload.prototype.hideCropLayer = function (Config) {
  this.extCropLayer.dom.style.display = "none";
  this.extPreview.dom.style.display = "none";
};

CGEditorDialogPictureUpload.prototype.setConfiguration = function (Config) {
  this.Limit = Config.Limit;
  if (this.extForm.dom.action == Config.Action) return;
  this.CropSize = Config.Size;
  this.PreserveAspectRatio = Config.PreserveAspectRatio;
  this.extForm.dom.action = Config.Action;
  this.downloadActionTemplate = Config.DownloadActionTemplate;

  this.extInputFile.dom.multiple = Config.MultipleSelection;
};

CGEditorDialogPictureUpload.prototype.getPicture = function (sSource) {
  return {Source: (sSource) ? sSource : this.extPreview.dom.src, Position: {X: this.extSliceX.dom.value, Y: this.extSliceY.dom.value, Width: this.extSliceWidth.dom.value, Height: this.extSliceHeight.dom.value}};
};

CGEditorDialogPictureUpload.prototype.updateForm = function (x, y, iWidth, iHeight) {
  this.extSliceX.dom.value = x;
  this.extSliceY.dom.value = y;
  this.extSliceWidth.dom.value = iWidth;
  this.extSliceHeight.dom.value = iHeight;
};

CGEditorDialogPictureUpload.prototype.checkFileSize = function () {
    var limit = this.Limit;
    if (limit == null || limit == "-1" || limit === "") return true;
    var files = this.extInputFile.dom.files;
    var size = 0;
    for (var i=0; i<files.length; i++) size += files[i].size;
    return size <= (limit * 1024 * 1024);
};

CGEditorDialogPictureUpload.prototype.showFileSizeExceeded = function () {
    this.extFileSizeExceeded.dom.innerHTML = WidgetFileSizeExceededTemplate.evaluate({'size': this.Limit});
    this.extFileSizeExceeded.dom.style.display = "block";
};

CGEditorDialogPictureUpload.prototype.hideFileSizeExceeded = function () {
    this.extFileSizeExceeded.dom.style.display = "none";
};

CGEditorDialogPictureUpload.prototype.showUploading = function (sMessage) {
  this.extUploading.dom.innerHTML = WidgetWaitingTemplate.evaluate({'ImagesPath': Context.Config.ImagesPath, 'cls': CLASS_EDITOR_WAITING_MESSAGE, 'message': sMessage});
  this.extUploading.dom.style.display = "block";
  this.hideCropLayer();
};

CGEditorDialogPictureUpload.prototype.showUploadingError = function () {
  this.extUploading.dom.innerHTML = WidgetWaitingTemplate.evaluate({'ImagesPath': Context.Config.ImagesPath, 'cls': CLASS_EDITOR_WAITING_ERROR, 'message': Lang.Editor.Dialogs.PictureUpload.UploadingFailed});
  this.hideCropLayer();
};

CGEditorDialogPictureUpload.prototype.hideUploadingError = function () {
  this.extUploading.dom.style.display = "none";
};

CGEditorDialogPictureUpload.prototype.setPicture = function (Source, iWidth, iHeight) {
  this.Picture = new Object();
  this.Picture.Source = Source;
  this.Picture.Width = iWidth;
  this.Picture.Height = iHeight;
  this.Picture.Slice = new Object();
  this.Picture.Slice.X = this.extSliceX.dom.value;
  this.Picture.Slice.Y = this.extSliceY.dom.value;
  this.Picture.Slice.Width = this.extSliceWidth.dom.value;
  this.Picture.Slice.Height = this.extSliceHeight.dom.value;
};

CGEditorDialogPictureUpload.prototype.calculateSelection = function (Source, iWidth, iHeight) {
  var Result = new Object();
  var iPreviewWidth = parseInt(this.extPreview.getWidth());
  var iPreviewHeight = parseInt(this.extPreview.getHeight());

  Result.X = Math.floor((this.Picture.Slice.X * this.Picture.Width) / iPreviewWidth);
  Result.Y = Math.floor((this.Picture.Slice.Y * this.Picture.Height) / iPreviewHeight);
  Result.Width = Math.floor((this.Picture.Slice.Width * this.Picture.Width) / iPreviewWidth);
  Result.Height = Math.floor((this.Picture.Slice.Height * this.Picture.Height) / iPreviewHeight);

  return Result;
};

CGEditorDialogPictureUpload.prototype.refresh = function () {
  this.extInputFile.dom.value = "";
  this.hideCropLayer();
};

CGEditorDialogPictureUpload.prototype.isMultiple = function(images) {
  return images.length > 1;
};

CGEditorDialogPictureUpload.prototype.notifySelect = function(image) {
  if (this.onSelect == null)
    return;

  this.onSelect(this.createValue(image));
};

CGEditorDialogPictureUpload.prototype.notifyMultipleSelect = function(images) {
  if (this.onSelectMultiple == null)
    return;

  var values = this.createValues(images);
  this.onSelectMultiple(values);
};

CGEditorDialogPictureUpload.prototype.createValue = function(image) {
  return {code: null, value: HtmlUtil.decode(image)};
};

CGEditorDialogPictureUpload.prototype.createValues = function(images) {
  var result = new Array();

  for (var i=0; i<images.length; i++)
    result.push(this.createValue(images[i]));

  return result;
};

// #############################################################################################################
CGEditorDialogPictureUpload.prototype.atUploadFile = function () {
    if (this.extInputFile.dom.value === "") // FIX IE BUG
        return;

  var fileInfo = null;

  if(!this.checkFileSize()) {
    this.showFileSizeExceeded();
    return;
  }

  this.hideFileSizeExceeded();
  this.hideUploadingError();
  this.showUploading(Lang.Editor.Dialogs.PictureUpload.Processing);
  this.updateForm(0, 0, -1, -1);

  var cropSizeWidth = this.PreserveAspectRatio?this.CropSize.Width:-1;
  var cropSizeHeight = this.PreserveAspectRatio?this.CropSize.Height:-1;
  var sActionUrl = this.extForm.dom.action + ((this.extForm.dom.action.indexOf("?") == -1) ? "?" : "&") + "width=" + cropSizeWidth + "&height=" + cropSizeHeight;

  Ext.Ajax.request({
    url: sActionUrl,
    method: "POST",
    success: function (response, options) {
      fileInfo = eval('(' + response.responseText + ')');
      if (fileInfo.status == 0) {
        this.extUploading.dom.style.display = 'none';

        if (this.isMultiple(fileInfo.images)) {
          this.notifyMultipleSelect(fileInfo.images);
          return;
        }

        var sSource = this.downloadActionTemplate.replace(TAG_URL_ID, fileInfo.images[0]);
        sSource = HtmlUtil.decode(sSource);
        this.setPicture(sSource, fileInfo.width, fileInfo.height);
        this.extPreview.dom.src = sSource;
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

CGEditorDialogPictureUpload.prototype.atLoadPicture = function () {
  this.showCropLayer();
  this.refreshCropLayer();
  this.extCropLayer.dom.style.display = "block";
  if (this.onChange) this.onChange(this.Picture);
};

CGEditorDialogPictureUpload.prototype.atCoordinatesChange = function () {
  this.Picture.Slice.X = this.extSliceX.dom.value;
  this.Picture.Slice.Y = this.extSliceY.dom.value;
  this.Picture.Slice.Width = this.extSliceWidth.dom.value;
  this.Picture.Slice.Height = this.extSliceHeight.dom.value;
  if (this.onChange) this.onChange(this.Picture);
};

CGEditorDialogPictureUpload.prototype.atButtonUploadClick = function (oEvent) {
  Event.stop(oEvent);

  var oFileInfo = null;
  var Selection = null;
  var X = this.extSliceX.dom.value;
  var Y = this.extSliceY.dom.value;
  var Width = this.extSliceWidth.dom.value;
  var Height = this.extSliceHeight.dom.value;

  Selection = this.calculateSelection();
  this.extSliceX.dom.value = Selection.X;
  this.extSliceY.dom.value = Selection.Y;
  this.extSliceWidth.dom.value = Selection.Width;
  this.extSliceHeight.dom.value = Selection.Height;

  this.showUploading(Lang.Editor.Dialogs.PictureUpload.Uploading);

  var cropSizeWidth = this.PreserveAspectRatio?this.CropSize.Width:-1;
  var cropSizeHeight = this.PreserveAspectRatio?this.CropSize.Height:-1;

  Ext.Ajax.request({
    url: this.extForm.dom.action + ((this.extForm.dom.action.indexOf("?") == -1) ? "?" : "&") + "width=" + cropSizeWidth + "&height=" + cropSizeHeight,
    method: "POST",
    success: function (response, options) {
      fileInfo = eval('(' + response.responseText + ')');
      if (fileInfo.status == 0) {
        this.notifySelect(fileInfo.images[0]);
        this.extUploading.dom.style.display = 'none';
        this.extInputFile.dom.value = "";
        this.extPreview.dom.src = "";
        this.hideCropLayer();
      }
      else {
        this.showUploadingError();
      }
      this.extSliceX.dom.value = X;
      this.extSliceY.dom.value = Y;
      this.extSliceWidth.dom.value = Width;
      this.extSliceHeight.dom.value = Height;
    },
    failure: function (response, options) {
      this.showUploadingError();
      this.extSliceX.dom.value = X;
      this.extSliceY.dom.value = Y;
      this.extSliceWidth.dom.value = Width;
      this.extSliceHeight.dom.value = Height;
    },
    isUpload: true,
    form: this.extForm.dom,
    scope: this
  });

  return false;
};