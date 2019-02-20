CGDocumentViewerFactory = function (url, editable) {
  this.url = url;
  this.editable = editable;
};

CGDocumentViewerFactory.prototype.get = function (document, layerId) {
  var url = resolveParametrizedUrl(this.url, document);
  var DOMLayer = $(layerId);

  var documentViewer = new DocumentViewer(Literals.PreviewThumbnails, layerId, Literals.PreviewButtons, Account.getUser().Language);
  documentViewer.setDocumentId(document.id);
  documentViewer.setDocumentLabel(document.label, this.editable);
  documentViewer.setViewport(Desktop.Main.Center.Body.getViewport(DOMLayer));
  documentViewer.setBaseUrl(url);
  documentViewer.onDocumentLabelChange = this.onDocumentLabelChange;
  documentViewer.onDocumentClick = this.onDocumentClick;

  if (this.onBeforeGet) this.onBeforeGet(document, documentViewer);

  Desktop.Main.Center.Body.setScrollListener(Desktop.Main.Center.Body.getActiveTab(VIEW_NODE), documentViewer.onScrollPageView.createDelegate(documentViewer), DOMLayer);

  return documentViewer;
};