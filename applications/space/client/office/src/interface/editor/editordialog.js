CGEditorDialog = function (extLayer) {
  this.extLayer = extLayer;
  this.Data = null;
  this.Configuration = null;
};

//private
CGEditorDialog.prototype.init = function () {
};

//public
CGEditorDialog.prototype.getLayer = function () {
  return this.extLayer;
};

CGEditorDialog.prototype.getConfiguration = function () {
  return this.Configuration;
};

CGEditorDialog.prototype.setConfiguration = function (Config) {
  this.Configuration = Config;
  if (this.Configuration.Field) this.Configuration.Field = null;
};

CGEditorDialog.prototype.showLoading = function () {
  if (this.extLoading) this.extLoading.dom.style.display = "block";
};

CGEditorDialog.prototype.hideLoading = function () {
  if (this.extLoading) this.extLoading.dom.style.display = "none";
};

CGEditorDialog.prototype.isVisible = function () {
  return (this.extLayer.dom.style.position != "absolute");
};

CGEditorDialog.prototype.show = function () {
  this.extLayer.dom.style.display = "block";
};

CGEditorDialog.prototype.hide = function () {
  this.extLayer.dom.style.display = "none";
};

CGEditorDialog.prototype.getData = function () {
  return this.Data;
};

CGEditorDialog.prototype.setData = function (Data) {
  this.Data = Data;
};

CGEditorDialog.prototype.normalizeData = function (Data, isFlatten) {
  var CodeValueColumn = "label";

  if (this.ColumnModel.CodeValueColumn != null) CodeValueColumn = this.ColumnModel.CodeValueColumn;
  else if (isFlatten) CodeValueColumn = "flatten_label";

  Data.code = HtmlUtil.decode(Data.code);
  Data.value = HtmlUtil.decode(eval("Data." + CodeValueColumn).unescapeHTML());
  Data.label = Data.value;

  return Data;
};

CGEditorDialog.prototype.moveUp = function () {
};

CGEditorDialog.prototype.moveDown = function () {
};

CGEditorDialog.prototype.focus = function () {
};

CGEditorDialog.prototype.refresh = function () {
};

// #############################################################################################################