CGEditorLink = function (DOMEditor) {
  this.base = CGEditorSelect;
  this.base(DOMEditor);
  this.allowLocations = false;
};

CGEditorLink.prototype = new CGEditorSelect;
// #############################################################################################################
CGEditorLink.prototype.addBehaviours = function () {
  CGEditorSelect.prototype.addBehaviours.call(this);
  this.aDialogs[LOCATION].onSelect = this.atSelect.bind(this);
};

CGEditorLink.prototype.init = function (DOMEditor) {
  this.extEditor = Ext.get(DOMEditor);
  var extDialogLocation = this.extEditor.select(CSS_EDITOR_DIALOG_LOCATION).first();
  this.aDialogs[LOCATION] = new CGEditorDialogLocations(extDialogLocation);

  CGEditorSelect.prototype.init.call(this, DOMEditor);

  this.extTableView = this.extEditor.select(CSS_EDITOR_VIEW_TABLE).first();
  if (this.extTableView) Event.observe(this.extTableView.dom, "click", CGEditorLink.prototype.atViewTable.bind(this));

  this.extLocationsView = this.extEditor.select(CSS_EDITOR_VIEW_LOCATIONS).first();
  if (this.extLocationsView) Event.observe(this.extLocationsView.dom, "click", CGEditorLink.prototype.atViewLocations.bind(this));
};

CGEditorLink.prototype.refresh = function () {
  if (this.dispatchingOnSelect) return;

  CGEditorSelect.prototype.refresh.call(this);
  this.aDialogs[LOCATION].hide();
  this.refreshToolbar();
};

CGEditorLink.prototype.refreshToolbar = function() {
  if (this.allowLocations) {
    this.extTableView.show();
    this.extLocationsView.show();

    this.extTableView.addClass(CLASS_ACTIVE);
    this.extLocationsView.removeClass(CLASS_ACTIVE);
  }
  else {
    this.extTableView.hide();
    this.extLocationsView.hide();
  }
};

CGEditorLink.prototype.showHistory = function () {
  CGEditorSelect.prototype.showHistory.call(this);
  this.aDialogs[LOCATION].hide();
};

CGEditorLink.prototype.hideHistory = function () {
  CGEditorSelect.prototype.hideHistory.call(this);

  if (this.getDialogMain() != this.aDialogs[HISTORY])
    return;

  this.aDialogs[LOCATION].hide();
};

CGEditorLink.prototype.show = function () {
  CGEditorSelect.prototype.show.call(this);
  this.aDialogs[LOCATION].hide();
};

CGEditorLink.prototype.showLocations = function () {
  var data = this.getDialogMain().getData();

  this.aDialogs[HISTORY].hide();
  this.aDialogs[SOURCE_STORE].hide();
  this.aDialogs[LOCATION].show();
  this.setDialogMain(LOCATION);

  if (data != null)
    this.filter(data);

  this.extLocationsView.addClass(CLASS_ACTIVE);
  this.extTableView.removeClass(CLASS_ACTIVE);
};

CGEditorLink.prototype.showTable = function () {
  var data = this.getDialogMain().getData();

  this.aDialogs[HISTORY].hide();
  this.aDialogs[SOURCE_STORE].show();
  this.aDialogs[LOCATION].hide();
  this.setDialogMain(SOURCE_STORE);

  if (data != null)
    this.filter(data);

  this.extLocationsView.removeClass(CLASS_ACTIVE);
  this.extTableView.addClass(CLASS_ACTIVE);
};

CGEditorLink.prototype.setConfiguration = function (Config) {

  if (Config.Field)
    this.allowLocations = Config.Field.getDataLinkLocations() != null;

  CGEditorSelect.prototype.setConfiguration.call(this, Config);
};

// ------------------------------------------------------------------------------------

CGEditorLink.prototype.atViewLocations = function () {
  this.showLocations();
};

CGEditorLink.prototype.atViewTable = function () {
  this.showTable();
};