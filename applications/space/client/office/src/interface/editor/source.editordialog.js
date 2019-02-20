CGEditorDialogSource = function (extLayer) {
  this.base = CGEditorDialog;
  this.base(extLayer);
  this.selectedOption = null;
  this.init();
};

CGEditorDialogSource.prototype = new CGEditorDialog;

//private
CGEditorDialogSource.prototype.init = function () {
  this.SelectedSourceId = null;
  this.Sources = new Array();
  this.SourcesMap = new Array();

  this.extSelect = this.extLayer.select(CSS_EDITOR_DIALOG_ELEMENT_SELECT).first();

  Event.observe(this.extSelect.dom, "change", CGEditorDialogSource.prototype.atChangeSource.bind(this));
};

CGEditorDialogSource.prototype.setConfiguration = function (Config) {
  if ((Config.Sources != null) && (Config.Sources == this.Sources) &&
      (Config.SelectedSourceId != null) && (Config.SelectedSourceId == this.SelectedSourceId)) {
    return;
  }
  this.SelectedSourceId = Config.SelectedSourceId;
  this.Sources = Config.Sources;
  this.refreshSourcesMap();
};

CGEditorDialogSource.prototype.refreshSourcesMap = function () {
  for (var i = 0; i < this.Sources.length; i++) {
    var Source = this.Sources[i];
    this.SourcesMap[Source.id] = Source;
  }
};

//public
CGEditorDialogSource.prototype.hide = function () {
  this.hideLoading();
  this.extLayer.dom.style.display = "none";
};

CGEditorDialogSource.prototype.getData = function () {
  return this.SelectedSourceId;
};

CGEditorDialogSource.prototype.setData = function (SourceId) {
  this.SelectedSourceId = SourceId;
};

CGEditorDialogSource.prototype.refresh = function () {

  if (this.Sources.length <= 1) this.hide();
  else this.show();

  this.extSelect.dom.innerHTML = "";
  for (var i = 0; i < this.Sources.length; i++) {
    var Source = this.Sources[i];
    var selected = false;
    if (this.SelectedSourceId == null && i == 0) selected = true;
    else if (Source.id == this.SelectedSourceId) selected = true;
    addSelectOption(this.extSelect.dom, Source.id, Source.label, selected);
  }
  ;
};

// #############################################################################################################

CGEditorDialogSource.prototype.atChangeSource = function () {
  var id = this.extSelect.dom.options[this.extSelect.dom.selectedIndex].value;
  var Source = this.SourcesMap[id];
  this.SelectedSourceId = Source.id;
  if (this.onSelect) this.onSelect(Source.id);
};