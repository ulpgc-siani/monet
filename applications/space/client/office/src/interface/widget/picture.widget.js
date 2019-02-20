CGWidgetPicture = function (extWidget) {
  this.base = CGWidgetFile;
  this.base(extWidget);

  if (!extWidget) return;

  this.extWidget.dom.blur = CGWidgetPicture.prototype.blur.bind(this);
  this.extValue.dom.readOnly = true;
  this.extLink = null;
  this.sServiceURL = HtmlUtil.decode(Context.Config.ApplicationFmsImagesUploadUrl);

  this.extThumbnail = this.extWidget.down(CSS_WIDGET_ELEMENT_THUMBNAIL);
  Event.observe(this.extThumbnail.dom, 'click', this.atThumbnailClick.bind(this));
};

CGWidgetPicture.prototype = new CGWidgetFile;

CGWidgetPicture.prototype.init = function () {
  this.preserveAspectRatio = true;
  this.Size = this.Target.getSize();

  if (this.Size == null) {
	  this.Size = { Width: 100, Height: 100 };
	  this.preserveAspectRatio = false;
  }

  this.bIsReady = true;
};

CGWidgetPicture.prototype.applyBehaviours = function () {
  if (!this.extWidget) return;

  this.extSuper = this.extWidget.down(CSS_WIDGET_ELEMENT_SUPER);
  this.extValue = this.extWidget.down(CSS_WIDGET_ELEMENT_COMPONENT);

  if (this.extValue == null) return;
  if (this.atFocused) this.extValue.on("focus", this.atFocused, this);
  if (this.atBlur) this.extValue.on("blur", this.atBlur, this);
  if (this.atChange) this.extValue.on('change', this.atChange, this);
  if (this.atKeyDown) this.extValue.on("keydown", this.atKeyDown, this);
  if (this.atKeyPress) this.extValue.on("keypress", this.atKeyPress, this);
  if (this.atKeyUp) this.extValue.on("keyup", this.atKeyUp, this);

  this.setId(this.extValue.dom.id);
};

CGWidgetPicture.prototype.createOptions = function () {
  new Insertion.Bottom(this.extWidget.dom, WidgetOptionsTemplate.evaluate({'ImagesPath': Context.Config.ImagesPath}));
  var extOptions = this.extWidget.select(CSS_WIDGET_ELEMENT_OPTIONS).first();
  this.extOptionClearValue = extOptions.select(CSS_WIDGET_ELEMENT_CLEAR_VALUE).first();
  this.extOptionClearValue.on("click", this.atClearValue, this);
  this.showClearValue();
};

CGWidgetPicture.prototype.createOnlineMenu = function (Target) {
  new Insertion.After(this.extValue.dom, WidgetImageOnlineMenuTemplate.evaluate({'ImagesPath': Context.Config.ImagesPath}));
  this.extOnlineMenu = this.extWidget.select(CSS_WIDGET_ELEMENT_ONLINE_MENU).first();

  var extOptionDownload = this.extOnlineMenu.down(CSS_FILE_DOWNLOAD);
  extOptionDownload.on("click", this.atDownload, this);

  var extOptionClearValue = this.extOnlineMenu.down(CSS_WIDGET_ELEMENT_CLEAR_VALUE);
  extOptionClearValue.on("click", this.atClearValue, this);

  this.extOnlineMenu.hide();
  this.extOnlineMenu.on("click", this.atOnlineMenuClick, this);
};

CGWidgetPicture.prototype.setTarget = function (Target) {
  this.Target = Target;
  this.createRequiredWidget();
  this.createOnlineMenu();
  this.createOptions();
  this.setFile(this.extValue.dom.value);
  this.setMessageWhenEmpty(this.Target.getMessageWhenEmpty());
  this.validate();
  //this.updateData();
};

CGWidgetPicture.prototype.getData = function () {
  if (!this.Target) return null;
  if (!this.Target.getCode) return null;
  if (!this.extValue) return null;

  var Result = new Object();
  Result.code = this.Target.getCode();
  Result.order = -1;
  Result.value = new Array();
  Result.value.push({code: CGIndicator.VALUE, order: 1, value: this.idFile});
  Result.value.push({code: CGIndicator.FORMAT, order: 2, value: getFileExtension(this.idFile).toUpperCase()});
  if (this.Target.isSuper() && (this.extSuper != null)) Result.value.push({code: CGIndicator.SUPER, order: 3, value: this.extSuper.dom.value});

  Result.toXml = function () {
    var Attribute = new CGAttribute();
    Attribute.code = this.code;
    Attribute.iOrder = this.order;
    for (var iPos = 0; iPos < this.value.length; iPos++) {
      Attribute.addIndicatorByValue(this.value[iPos].code, this.value[iPos].order, this.value[iPos].value);
    }
    return Attribute.serialize();
  };

  return Result;
};

CGWidgetPicture.prototype.setData = function (sData) {
  if (!this.Target) return;
  if (!this.extValue) return;

  var Attribute = new CGAttribute();
  Attribute.unserialize(sData);

  var IndicatorValue = Attribute.getIndicator(CGIndicator.VALUE);
  if (IndicatorValue) {
    this.setFile(IndicatorValue.getValue());
  }

  if (this.extOptionClearValue) {
    if (this.idFile != "") this.showClearValue();
    else this.hideClearValue();
  }

  this.validate();

  if (this.onChange) this.onChange();
};

CGWidgetPicture.prototype.clear = function () {
  this.setFile("");
  this.hideClearValue();
  if (this.onClearValue)
    this.onClearValue(this);
  else {
    this.validate();
    this.updateData();
  }
};

CGWidgetPicture.prototype.blur = function () {
  this.extThumbnail.removeClass(CLASS_FOCUS);
  this.extValue.removeClass(CLASS_FOCUS);
  if (this.onBlur) this.onBlur();
};

CGWidgetPicture.prototype.getUrl = function (idFile) {
  var sSource = Context.Config.ApplicationFmsImageDownloadUrl.replace(TAG_URL_ID, idFile);
  sSource = sSource.replace(TAG_URL_IDNODE, this.Target.getNodeId());
  sSource = HtmlUtil.decode(sSource);
  return sSource + "&rand=" + (Math.random() * 10000000);
};

CGWidgetPicture.prototype.setFile = function (idFile) {

  this.idFile = idFile;

  if (idFile) {
    this.extThumbnail.dom.src = this.getUrl(idFile) + "&thumb=1";
    this.extValue.dom.value = idFile;
  }
  else {
    var defaultPicture = this.Target.getDefaultPicture();
    if (defaultPicture)
      this.extThumbnail.dom.src = Context.Config.Api + "?op=loadbusinessunitfile&path=images/" + this.Target.getDefaultPicture();
    else
      this.extThumbnail.dom.src = Context.Config.ImagesPath + "/no-picture.jpg";
    this.extValue.dom.value = "";
  }
};

CGWidgetPicture.prototype.clearValue = function (oEvent) {
  this.focus();
  this.clear();
};

// #############################################################################################################
CGWidgetPicture.prototype.atFocused = function (oEvent) {
  if (!this.Target) return false;
  if (!this.isReady()) this.init();
  if (this.onFocused) this.onFocused();

  var nodeId = this.Target.getNodeId();
  var action = this.sServiceURL.replace(TAG_URL_IDNODE, nodeId);
  var downloadActionTemplate = Context.Config.ApplicationFmsImageDownloadUrl.replace(TAG_URL_IDNODE, nodeId);

  this.Editor.show();
  this.Editor.setConfiguration({Field: this.Target, Dialogs: [
    {sName: PICTURE_UPLOAD, Action: action, DownloadActionTemplate: downloadActionTemplate, Size: this.Size, Limit: this.Target.getLimit(), PreserveAspectRatio: this.preserveAspectRatio, MultipleSelection: this.Target.isMultiple()}
  ]});
  this.Editor.onSelect = this.atSelect.bind(this);
  this.Editor.onLoadDefaultValue = this.atLoadDefaultValue.bind(this);
  this.Editor.onAddDefaultValue = this.atAddDefaultValue.bind(this);
  this.Editor.onClearValue = this.atClearValue.bind(this);
  this.Editor.onDownload = this.atEditorDownload.bind(this);
  this.Editor.refresh();

  if (this.isLocked()) this.Editor.lock();
  else this.Editor.unLock();

  this.extThumbnail.addClass(CLASS_FOCUS);
  this.extValue.addClass(CLASS_FOCUS);
  this.extValue.dom.select();

  if (this.Target.getInfo) this.Editor.setInfo(this.Target.getInfo());
  if (this.extMessageEmpty) this.extMessageEmpty.dom.style.display = "none";

  if (this.idFile != "") this.extOnlineMenu.show();

  return false;
};

CGWidgetPicture.prototype.atThumbnailClick = function (oEvent) {
  this.focus();
  Event.stop(oEvent);
  return false;
};

CGWidgetPicture.prototype.atSelect = function (Data, Source) {
  this.setFile((Data.value) ? Data.value : Data.code);
  this.showClearValue();
  this.validate();
  this.updateData();
  if (this.onSelect) this.onSelect(Data);
  new Effect.Appear(this.extThumbnail.dom);
};

CGWidgetPicture.prototype.atDownload = function (EventLaunched, DOMDownload) {
  DOMDownload.href = this.getUrl(this.extValue.dom.value);
  this.extOnlineMenu.hide();
};

CGWidgetPicture.prototype.atClearValue = function (oEvent) {
  this.clearValue();
  return false;
};

CGWidgetPicture.prototype.atEditorDownload = function (DOMDownload) {
  DOMDownload.stop = (this.idFile == "");
  DOMDownload.href = this.extValue.dom.value;
};