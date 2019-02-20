CGListViewerFactory = function (url, options, language, imagesPath) {
  this.url = url;
  this.options = options;
  this.language = language;
  this.imagesPath = imagesPath;
};

CGListViewerFactory.prototype.get = function (object) {
  var url = resolveParametrizedUrl(this.url, object);
  var options = clone(this.options);
  var view = new CGListViewer(options, this.language, this.imagesPath);
  //if (Object != null) View.setLabel(Object.label);
  view.setDataSourceUrls(url, null);
  if (this.onBoundItem) view.onBoundItem = this.onBoundItem;
  if (this.onDeleteItem) view.onDeleteItem = this.onDeleteItem;
  if (this.onDeleteItems) view.onDeleteItems = this.onDeleteItems;
  if (this.onBoundOperation) view.onBoundOperation = this.onBoundOperation;
  if (this.onOperationClick) view.onOperationClick = this.onOperationClick;
  return view;
};