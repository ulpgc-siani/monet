CGEditorDialogGrid = function (extLayer) {
  this.base = CGEditorDialog;
  this.base(extLayer);
  this.extGrid = null;
  this.extEmpty = null;
  this.sFilter = EMPTY;
  this.Store = null;
  this.ColumnModel = null;
  this.sLastQuery = EMPTY;
  this.iItemsPerPage = EDITOR_MAX_PAGE_ITEMS;
  this.bGridRendered = false;
  this.bRenderFooter = true;
  this.init();
};

CGEditorDialogGrid.prototype = new CGEditorDialog;

//private
CGEditorDialogGrid.prototype.init = function () {
  var DOMGrid;

  this.Store = new Ext.data.SimpleStore({fields: ['label', 'code'],remoteSort:true});
  this.ColumnModel = new Ext.grid.ColumnModel([
    {header: Lang.Editor.Code, dataIndex: 'code', hidden: true},
    {header: Lang.Editor.Option, dataIndex: 'label', sortable: true, width: this.extLayer.getWidth() - 10}
  ]);
  this.ColumnModel.defaultSortable = true;
  this.ColumnModel.sortable = true;

  if (!(DOMGrid = (this.extLayer.select(CSS_EDITOR_DIALOG_ELEMENT_GRID).first()).dom)) return;
  this.extGrid = new Ext.grid.Grid(DOMGrid, {
    ds: this.Store,
    cm: this.ColumnModel,
    selModel: new Ext.grid.RowSelectionModel({singleSelect: true}),
    autoSizeColumns: true,
    loadMask: true
  });

  this.extGrid.on("keypress", this.atGridKeyPress, this);
  this.extGrid.on("click", this.atSelect, this);

  this.extLoading = Ext.get($(new Insertion.Bottom(this.extLayer.dom, "<div class='loading'></div>").element.descendants().last()));
  this.extEmpty = Ext.get($(new Insertion.Bottom(this.extLayer.dom, "<div class='empty'>" + Lang.Editor.Empty + "</div>").element.descendants().last()));
};

CGEditorDialogGrid.prototype.renderGrid = function () {
  var extGridFooter;

  if (this.bGridRendered) return;

  this.extGrid.render();

  if (this.bRenderFooter) {
    extGridFooter = this.extGrid.getView().getFooterPanel(true);
    this.extPaging = new Ext.PagingToolbar(extGridFooter, this.Store, {
      pageSize: EDITOR_MAX_PAGE_ITEMS,
      displayInfo: true
    });
  }

  this.bGridRendered = true;
};

CGEditorDialogGrid.prototype.doRefreshColumns = function () {
  this.ColumnModel.setHidden(0, !this.Store.ShowCode);

  var offset = this.Store.ShowCode ? 0 : 1;
  var numColumns = this.ColumnModel.getColumnCount();
  var columnsWidth = (this.extLayer.getWidth() - 5) / (numColumns - offset);

  for (var i = offset; i < numColumns; i++)
    this.ColumnModel.setColumnWidth(i, columnsWidth);
};

CGEditorDialogGrid.prototype.doRefresh = function () {

  if (this.Configuration.Store == null) {
    this.hideLoading();
    this.idTimeoutRefresh = null;
    return;
  }

  if (this.Configuration.Store == this.Store) {
    this.hideLoading();
    this.doQuery(this.sFilter);
    this.idTimeoutRefresh = null;
    return;
  }

  if (this.Configuration.Store != null) this.Store = this.Configuration.Store;

  if (this.Configuration.ColumnModel != null) this.ColumnModel = this.Configuration.ColumnModel;
  else {
    if (this.Store.isFlatten) this.ColumnModel = new Ext.grid.ColumnModel([
      {header: Lang.Editor.Code, dataIndex: 'code', hidden: true},
      {header: Lang.Editor.Option, dataIndex: 'flatten_label', width: this.extLayer.getWidth() - 10}
    ]);
    else this.ColumnModel = new Ext.grid.ColumnModel([
      {header: Lang.Editor.Code, dataIndex: 'code', hidden: true},
      {header: Lang.Editor.Option, dataIndex: 'label', width: this.extLayer.getWidth() - 10}
    ]);
  }

  this.loadSorting();
  this.doRefreshColumns();
  this.renderGrid();

  this.Store.un("beforeload", this.atDataBeforeLoad, this);
  this.Store.on("beforeload", this.atDataBeforeLoad, this);

  this.Store.un("load", this.atDataLoad, this);
  this.Store.on("load", this.atDataLoad, this);

  this.extGrid.reconfigure(this.Store, this.ColumnModel);
  if (this.extPaging) this.extPaging.bind(this.Store);

  this.idTimeoutRefresh = null;
  this.hideLoading();
  this.doQuery(this.sFilter);
};

CGEditorDialogGrid.prototype.doQuery = function (sQuery) {
  var DataSource = this.extGrid.getDataSource();
  if (sQuery == null) sQuery = this.sLastQuery;
  this.sLastQuery = sQuery;
  DataSource.load({params: {query: sQuery, start: 0}});
  if (!DataSource.isRemote()) DataSource.filter("label", sQuery, true);
};

//public
CGEditorDialogGrid.prototype.focus = function () {
};

CGEditorDialogGrid.prototype.show = function (bShowPageControl) {
  this.extLayer.dom.style.display = "block";

  var extElement = this.extGrid.getView().el;
  if (extElement) extElement.dom.style.display = "block";

  if (this.Store.getCount() > 0) {
    this.extEmpty.dom.style.display = "none";
  }
  else {
    this.extEmpty.dom.style.display = "block";
  }
};

CGEditorDialogGrid.prototype.hide = function () {

  if (this.idTimeoutRefresh) {
    window.clearTimeout(this.idTimeoutRefresh);
    this.idTimeoutRefresh = null;
  }

  this.hideLoading();
  this.extLayer.dom.style.display = "none";
  var extElement = this.extGrid.getView().el;
  if (extElement) extElement.dom.style.display = "none";
};

CGEditorDialogGrid.prototype.moveUp = function (Sender) {
  this.extGrid.container.focus();
  if (Sender != null) this.Sender = Sender;
  var SelectionModel = this.extGrid.getSelectionModel();
  if (this.Store.getTotalCount() > 0) {
    if (SelectionModel.getSelected() != null) SelectionModel.selectPrevious();
    else SelectionModel.selectLastRow();
  }
};

CGEditorDialogGrid.prototype.moveDown = function (Sender) {
  this.extGrid.container.focus();
  if (Sender != null) this.Sender = Sender;
  var SelectionModel = this.extGrid.getSelectionModel();
  if (this.Store.getTotalCount() > 0) {
    if (SelectionModel.getSelected() != null) SelectionModel.selectNext();
    else SelectionModel.selectFirstRow();
  }
};

CGEditorDialogGrid.prototype.getData = function () {
  var Record = this.extGrid.getSelectionModel().getSelected();
  var DataSource = this.extGrid.getDataSource();
  if (Record) return this.normalizeData(Record.data, DataSource.isFlatten);
  return null;
};

CGEditorDialogGrid.prototype.setData = function (Data) {
  this.sFilter = Data;
};

CGEditorDialogGrid.prototype.setItemsPerPage = function (iItemsPerPage) {
  this.iItemsPerPage = iItemsPerPage;
};

CGEditorDialogGrid.prototype.setRenderFooter = function (bValue) {
  this.bRenderFooter = bValue;
};

CGEditorDialogGrid.prototype.setStoreSourceId = function (SourceId) {
  if (this.extGrid.getDataSource().setSourceId) {
    this.extGrid.getDataSource().setSourceId(SourceId);
    this.Configuration.Store.SourceId = SourceId;
  }
};

CGEditorDialogGrid.prototype.refresh = function () {

  if (this.idTimeoutRefresh != null)
    window.clearTimeout(this.idTimeoutRefresh);

  this.showLoading();
  this.Store.removeAll();
  this.idTimeoutRefresh = window.setTimeout(this.doRefresh.bind(this), 300);
};

// #############################################################################################################
CGEditorDialogGrid.prototype.atSelect = function (oEvent) {
  var Record = this.extGrid.getSelectionModel().getSelected();
  var DataSource = this.extGrid.getDataSource();
  if (!Record) return false;
  var Data = this.normalizeData(Record.data, DataSource.isFlatten);
  if (this.onSelect) this.onSelect(Data);
  return true;
};

CGEditorDialogGrid.prototype.atDataBeforeLoad = function (Store, Options) {
  this.showLoading();
  if (Options) {
    if (!Options.params) Options.params = new Object();
    if (!Options.params.query) Options.params.query = this.sLastQuery;
    if (Store.SourceId != null) Options.params.id = Store.SourceId;
    if (Store.From != null) Options.params.from = Store.From;
    if (Store.Filters != null) Options.params.filters = Store.Filters;
    Options.params.list = this.extGrid.getDataSource().list;
    Options.params.limit = this.iItemsPerPage;
  }
  if (this.Configuration.Parameters) {
    for (var code in this.Configuration.Parameters) {
      if (isFunction(this.Configuration.Parameters[code])) continue;
      eval("Options.params." + code + "='" + this.Configuration.Parameters[code] + "';");
    }
  }
};

CGEditorDialogGrid.prototype.atDataLoad = function (e) {
  this.hideLoading();
  this.saveSorting(e.sortInfo);
  if (this.extGrid.getDataSource().getCount() > 0) {
    this.extEmpty.dom.style.display = "none";
  }
  else {
    this.extEmpty.dom.style.display = "block";
  }
};

CGEditorDialogGrid.prototype.atGridKeyPress = function (oEvent) {
  var codeKey = oEvent.keyCode;
  var Sender = this.Sender;

  if (Sender == null) return true;

  if (codeKey == oEvent.ENTER) {
    this.atSelect();
    Sender.focus();
    this.Sender = null;
  }
  else if (codeKey == oEvent.ESCAPE) {
    Sender.focus();
    this.Sender = null;
  }

  if (this.onKeyPress) this.onKeyPress(oEvent, codeKey);

  Event.stop(oEvent);
  return false;
};

CGEditorDialogGrid.prototype.saveSorting = function(sortInfo) {
    if (sortInfo == null) return;
    var name = this.ColumnModel.name;
    if (name == null || name === "") return;
    var businessUnit = Context.Config.BusinessUnit;
    setCookie(businessUnit + "grid_editordialog_sorting_" + name, sortInfo.field);
    setCookie(businessUnit + "grid_editordialog_sorting_dir_" + name, sortInfo.direction);
};

CGEditorDialogGrid.prototype.loadSorting = function () {
    var name = this.ColumnModel.name;
    if (name == null || name === "") return;
    var businessUnit = Context.Config.BusinessUnit;
    var sorting = getCookie(businessUnit + "grid_editordialog_sorting_" + name);
    if (sorting == null || sorting === "" || sorting === "undefined") return;
    this.Store.sortInfo = { field: sorting, direction: getCookie(businessUnit + "grid_editordialog_sorting_dir_" + name) };
};