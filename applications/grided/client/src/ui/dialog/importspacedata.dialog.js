var ImportSpaceDataDialog = Dialog.extend({
  init : function(element) {
    this._super(element);
    this.html = translate(AppTemplate.ImportSpaceDataDialog, Lang.ImportSpaceDataDialog);
    element.innerHTML = this.html;
    
    this._bind();
  }
});

ImportSpaceDataDialog.CLICK_IMPORT = 'click_import';

//------------------------------------------------------------------
ImportSpaceDataDialog.prototype.getData = function() {      
  var data = {
    form : this.extForm.dom,
    importerTypeId : this.extSelect.dom.options[this.extSelect.dom.selectedIndex].value
  };
  return data;
};

//------------------------------------------------------------------
ImportSpaceDataDialog.prototype.setImporterTypes = function(importerTypes) {      
  this.importerTypes = importerTypes;
  
  for (var i=0, l = importerTypes.length; i < l; i++) {
    var type = importerTypes[i];
    this.extSelect.dom.options[this.extSelect.dom.options.length] = new Option(type.label, type.id);
  }  
};

//------------------------------------------------------------------
ImportSpaceDataDialog.prototype._bind = function() {
  this.extParent = Ext.get(this.$el); 
  
  this.extForm   = Ext.get(this.extParent.query('form').first());
  this.extSelect = Ext.get(this.extParent.query('select[name="importer_type"]').first());
  this.extFile   = Ext.get(this.extParent.query('input[type="file"]').first());
  this.extImportButton = Ext.get(this.extParent.query('input[name="import-button"]').first());
  this.extFilename = Ext.get(this.extParent.query('div[name="filename"]').first());
  
  this.extImportButton.on('click', this._importButtonHandler, this);  
  this.extFile.on('change', this._changeFileHandler, this);
    
  this.extImportButton.dom.disabled = true;
};

//------------------------------------------------------------------
ImportSpaceDataDialog.prototype._importButtonHandler = function(event, target, options) {
  var ev = {name : ImportSpaceDataDialog.CLICK_IMPORT, data: this.getData()};
  this.fire(ev);  
};

//------------------------------------------------------------------
ImportSpaceDataDialog.prototype._changeFileHandler = function(event, target, options) {
  this.extFilename.dom.innerHTML = this.extFile.dom.value;
  if (this.extFilename != "") this.extImportButton.dom.disabled = false;
};