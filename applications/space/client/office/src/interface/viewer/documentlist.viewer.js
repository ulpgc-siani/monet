function CGViewerDocumentList(documentViewerFactory) {
  this.documentViewerFactory = documentViewerFactory;
  this.baseUrl = null;
  this.loading = false;
  this.initialized = false;
  this.documentPosition = 0;
  this.currentViewer = null;
};

CGViewerDocumentList.prototype.init = function (layerId) {

  if (this.initialized) return;
  if (layerId == null) return;

  this.extLayer = Ext.get(layerId);
  this.extLayer.dom.innerHTML = translate(AppTemplate.ViewerDocumentList, Lang.ViewerDocumentList);
  this.extLayer.addClass("documentlistviewer");

  this.addBehaviours();

  this.loading = false;
  this.initialized = true;
};

CGViewerDocumentList.prototype.addBehaviours = function () {
  var extDocumentsSelector = this.extLayer.select("select.documents").first();
  extDocumentsSelector.on("change", CGViewerDocumentList.prototype.atDocumentsSelectorChange, this);

  var extLabels = this.extLayer.select(".dialog .label");
  extLabels.each(function (extLabel) {
    extLabel.on("keyup", CGViewerDocumentList.prototype.atLabelKeyUp, this);
  }, this);

  var extAddDialogAccept = this.extLayer.select(".dialog.add .accept").first();
  extAddDialogAccept.on("click", CGViewerDocumentList.prototype.atAddDialogAccept, this);

  var extAddDialogCancel = this.extLayer.select(".dialog.add .cancel").first();
  extAddDialogCancel.on("click", CGViewerDocumentList.prototype.atAddDialogCancel, this);

  var extRenameDialogAccept = this.extLayer.select(".dialog.rename .accept").first();
  extRenameDialogAccept.on("click", CGViewerDocumentList.prototype.atRenameDialogAccept, this);

  var extRenameDialogCancel = this.extLayer.select(".dialog.rename .cancel").first();
  extRenameDialogCancel.on("click", CGViewerDocumentList.prototype.atRenameDialogCancel, this);

  var extDialogUpload = this.extLayer.select(".dialog .input.file").first();
  extDialogUpload.on("change", CGViewerDocumentList.prototype.atUploadFile, this);

  var extPrevious = this.extLayer.select(".previous").first();
  extPrevious.on("click", CGViewerDocumentList.prototype.atPreviousClick, this);

  var extNext = this.extLayer.select(".next").first();
  extNext.on("click", CGViewerDocumentList.prototype.atNextClick, this);
};

CGViewerDocumentList.prototype.setBaseUrl = function (baseUrl) {
  this.baseUrl = baseUrl;
};

CGViewerDocumentList.prototype.showLoading = function () {
  if (this.extLayer == null) return;
  this.extLayer.select(".header").first().dom.style.display = "none";
  this.extLayer.select(".loading").first().dom.style.display = "block";
  this.loading = true;
};

CGViewerDocumentList.prototype.hideLoading = function () {
  if (this.extLayer == null) return;
  this.extLayer.select(".header").first().dom.style.display = "block";
  this.extLayer.select(".loading").first().dom.style.display = "none";
  this.loading = false;
};

CGViewerDocumentList.prototype.render = function (layerId) {
  if (this.baseUrl == null) return;

  this.init(layerId);
  this.showLoading();

  Ext.Ajax.request({
    url: this.baseUrl,
    params: "",
    method: "GET",
    callback: function (sOptions, bSuccess, Response) {
      eval("var data = " + Response.responseText);
      this.hideLoading();
      this.update(data);
    },
    scope: this
  });
};

CGViewerDocumentList.prototype.update = function (data) {
  this.data = data;

  this.extLayer.select(".empty").first().dom.style.display = (this.data.documents.length <= 0) ? "block" : "none";
  this.extLayer.select(".header").first().dom.style.display = (this.data.documents.length <= 0) ? "none" : "block";

  if (this.data.documents.length <= 0)
    return;

  if (this.target.idDocument != null) this.gotoDocument(this.target.idDocument);
  else this.first();
};

CGViewerDocumentList.prototype.refresh = function () {

  this.refreshHeader();
  this.refreshContainer();

  if (this.onSelect) this.onSelect(this.data.documents[this.documentPosition].id);
};

CGViewerDocumentList.prototype.refreshHeader = function () {
  var extDocumentsSelector = this.extLayer.select("select.documents").first();
  var extPrevious = this.extLayer.select(".previous").first();
  var extNext = this.extLayer.select(".next").first();

  if (this.documentPosition == 0) extPrevious.addClass("disabled");
  else extPrevious.removeClass("disabled");

  if (this.documentPosition == this.data.documents.length - 1) extNext.addClass("disabled");
  else extNext.removeClass("disabled");

  var selectedDocument = this.data.documents[this.documentPosition];

  extDocumentsSelector.dom.innerHTML = "";
  for (var i = 0; i < this.data.categories.length; i++) {
    var currentCategory = this.data.categories[i];
    var DOMCategoryOption = null;

    if (currentCategory.isMultiple) {
      DOMCategoryOption = document.createElement("option");
      DOMCategoryOption.selected = (selectedDocument.targetCode == currentCategory.code);
      DOMCategoryOption.text = currentCategory.label;
      extDocumentsSelector.dom.appendChild(DOMCategoryOption);
    }

    var firstDocument = null;
    for (var j = 0; j < this.data.documents.length; j++) {
      var currentDocument = this.data.documents[j];
      if (currentDocument.targetCode != currentCategory.code) continue;
      var DOMOption = document.createElement("option");
      DOMOption.value = j;
      DOMOption.selected = (this.documentPosition == j);
      DOMOption.text = ((currentDocument.label != "") ? currentDocument.label : Lang.ViewerDocumentList.NoLabel);
      DOMOption.className = "document";
      extDocumentsSelector.dom.appendChild(DOMOption);
      if (firstDocument == null) firstDocument = j;
    }

    if (DOMCategoryOption != null) DOMCategoryOption.value = firstDocument;
  }

};

CGViewerDocumentList.prototype.refreshContainer = function () {
  var document = this.data.documents[this.documentPosition];
  var extContainer = this.extLayer.select(".container").first();

  extContainer.dom.innerHTML = "";
  $(Literals.PreviewThumbnails).innerHTML = "";
  $(Literals.PreviewButtons).innerHTML = "";

  if (this.currentViewer != null) this.currentViewer.dispose();

  var viewer = this.documentViewerFactory.get(document, extContainer.dom);
  viewer.load();

  this.currentViewer = viewer;
};

CGViewerDocumentList.prototype.getDocument = function () {
  if (this.data.documents.length <= 0) return null;
  var document = this.data.documents[this.documentPosition];
  document.position = this.documentPosition;
  return document;
};

CGViewerDocumentList.prototype.setLabel = function (label) {
  var extLabelList = this.extLayer.select(".dialog .label");
  extLabelList.each(function (extLabel) {
    extLabel.dom.value = label;
  }, this);
};

CGViewerDocumentList.prototype.getLabel = function () {
  var extDialog = this.extLayer.select(".dialog.add").first();
  if (extDialog.dom.style.display == "none") extDialog = this.extLayer.select(".dialog.rename").first();
  var extLabel = extDialog.select(".label").first();
  return extLabel.dom.value;
};

CGViewerDocumentList.prototype.getFilename = function () {
  return this.filename;
};

CGViewerDocumentList.prototype.hasDocuments = function () {
  return this.data.documents.length > 0;
};

CGViewerDocumentList.prototype.getDownloadDocumentUrl = function () {
  var document = this.getDocument();
  if (document == null) return null;
  var url = HtmlUtil.decode(Context.Config.ApplicationFmsFileDownloadUrl).replace("::id::", document.id);
  url = url.replace("::idnode::", this.target.idNode);
  return url;
};

CGViewerDocumentList.prototype.clear = function () {
  var extAddLabel = this.extLayer.select(".dialog.add .label").first();
  var extRenameLabel = this.extLayer.select(".dialog.rename .label").first();
  extAddLabel.dom.value = "";
  extRenameLabel.dom.value = "";
  this.filename = "";
};

CGViewerDocumentList.prototype.first = function () {
  this.documentPosition = 0;
  this.refresh();
};

CGViewerDocumentList.prototype.selectCategory = function (idCategory) {
  var pos = 0;

  for (var i = 0; i < this.data.categories.length; i++) {
    var currentCategory = this.data.categories[i];
    if (currentCategory.code == idCategory) {
      for (var j = 0; j < this.data.documents.length; j++) {
        var currentDocument = this.data.documents[j];
        if (currentDocument.targetCode == currentCategory.code) {
          pos = j;
          break;
        }
      }
      break;
    }
  }

  if (pos >= this.data.documents.length) pos = 0;

  this.documentPosition = pos;
  this.refresh();
};

CGViewerDocumentList.prototype.gotoDocument = function (idDocument) {

  for (var i = 0; i < this.data.documents.length; i++) {
    var currentDocument = this.data.documents[i];
    if (currentDocument.id == idDocument) break;
  }

  if (i >= this.data.documents.length) i = 0;

  this.documentPosition = i;
  this.refresh();
};

CGViewerDocumentList.prototype.previous = function () {
  this.documentPosition--;

  if (this.documentPosition < 0) {
    this.documentPosition = 0;
    return;
  }

  this.refresh();
};

CGViewerDocumentList.prototype.next = function () {
  this.documentPosition++;

  if (this.documentPosition >= this.data.documents.length) {
    this.documentPosition = this.data.documents.length - 1;
    return;
  }

  this.refresh();
};

CGViewerDocumentList.prototype.hideDialogs = function () {
  var extDialogList = this.extLayer.select(".dialog");
  extDialogList.each(function (extDialog) {
    extDialog.dom.style.display = "none";
  }, this);
};

CGViewerDocumentList.prototype.showAddDialog = function (type, acceptCallback, cancelCallback) {
  this.hideDialogs();
  var extDialog = this.extLayer.select(".dialog.add").first();
  extDialog.removeClass("node");
  extDialog.removeClass("file");
  extDialog.addClass(type);
  extDialog.dom.style.display = "block";
  var extLabel = extDialog.select(".label").first();
  extLabel.dom.focus();
  this.acceptCallback = acceptCallback;
  this.cancelCallback = cancelCallback;
};

CGViewerDocumentList.prototype.showRenameDialog = function (acceptCallback, cancelCallback) {
  this.hideDialogs();
  var extDialog = this.extLayer.select(".dialog.rename").first();
  extDialog.dom.style.display = "block";
  var extLabel = extDialog.select(".label").first();
  extLabel.dom.focus();
  this.acceptCallback = acceptCallback;
  this.cancelCallback = cancelCallback;
};

CGViewerDocumentList.prototype.dispose = function () {
  if (this.currentViewer) this.currentViewer.dispose();
};

CGViewerDocumentList.prototype.showUploadError = function () {
  var extError = this.extLayer.select(".dialog .error");
  extError.dom.style.display = "block";
  this.hideUploading();
};

CGViewerDocumentList.prototype.showUploading = function () {
  var extUploading = this.extLayer.select(".dialog .uploading").first();
  extUploading.dom.style.display = "block";
};

CGViewerDocumentList.prototype.hideUploading = function () {
  var extUploading = this.extLayer.select(".dialog .uploading").first();
  extUploading.dom.style.display = "none";
};

CGViewerDocumentList.prototype.atDocumentsSelectorChange = function () {
  var extDocumentsSelector = this.extLayer.select("select.documents").first();
  var index = extDocumentsSelector.dom.options[extDocumentsSelector.dom.selectedIndex].value;
  this.gotoDocument(this.data.documents[index].id);
};

CGViewerDocumentList.prototype.atPreviousClick = function () {
  this.previous();
};

CGViewerDocumentList.prototype.atNextClick = function () {
  this.next();
};

CGViewerDocumentList.prototype.atUploadFile = function () {
  var fileInfo = null;

  this.showUploading();

  var action = HtmlUtil.decode(Context.Config.ApplicationFmsFilesUploadUrl);
  action = action.replace("::idnode::", this.target.idNode);

  var extForm = this.extLayer.select(".form").first();

  Ext.Ajax.request({
    url: action,
    method: "POST",
    success: function (response, options) {
      fileInfo = eval('(' + response.responseText + ')');
      this.hideUploading();
      if (fileInfo.status == 0) this.filename = HtmlUtil.decode(fileInfo.files[0]);
      else {
        this.showUploadError();
      }
    },
    failure: function (response, options) {
      this.showUploadError();
    },
    isUpload: true,
    form: extForm.dom,
    scope: this
  });

};

CGViewerDocumentList.prototype.atAddDialogAccept = function () {
  var extDialog = this.extLayer.select(".dialog.add").first();
  var extLabel = extDialog.select(".label").first();
  if (extLabel.dom.value == "") return;
  if (extDialog.hasClass("file") && this.filename == null) return;
  if (this.acceptCallback) this.acceptCallback();
};

CGViewerDocumentList.prototype.atAddDialogCancel = function () {
  var extDialog = this.extLayer.select(".dialog.add").first();
  extDialog.dom.style.display = "none";
  this.clear();
  if (this.cancelCallback) {
    this.refreshContainer();
    this.cancelCallback();
  }
};

CGViewerDocumentList.prototype.atRenameDialogAccept = function () {
  var extDialog = this.extLayer.select(".dialog.rename").first();
  var extLabel = extDialog.select(".label").first();
  if (extLabel.dom.value == "") return;
  if (this.acceptCallback) this.acceptCallback();
};

CGViewerDocumentList.prototype.atRenameDialogCancel = function () {
  var extDialog = this.extLayer.select(".dialog.rename").first();
  extDialog.dom.style.display = "none";
  this.clear();
  if (this.cancelCallback) {
    this.refreshContainer();
    this.cancelCallback();
  }
};

CGViewerDocumentList.prototype.atLabelKeyUp = function (event) {
  var codeKey = event.getKey();
  if (codeKey == event.ENTER) {
    var extDialog = Ext.get(event.target).up(".dialog");
    if (extDialog.hasClass("add")) this.atAddDialogAccept();
    else if (extDialog.hasClass("rename")) this.atRenameDialogAccept();
  }
};