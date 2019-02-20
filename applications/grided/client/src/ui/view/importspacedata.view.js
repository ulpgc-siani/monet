var ImportSpaceDataView = View.extend({
  init : function(id) {
    this._super(id);
    this.html = AppTemplate.ImportSpaceDataView;
    this.importerTypes = null; 
    this.progress = null;
    
    this.importWizard = null;
    this._init();
  }
}); 

//-------------------------------------------------------------------------
ImportSpaceDataView.prototype.setPresenter = function(presenter) {
  this.presenter = presenter;
};

//-------------------------------------------------------------------------
ImportSpaceDataView.prototype.setImporterTypes = function(importerTypes) {    
  this.importWizard.setImporterTypes(importerTypes);
};

//-------------------------------------------------------------------------
ImportSpaceDataView.prototype.setProgress = function(progress) {  
  this.importWizard.setProgress(progress);
};

//-------------------------------------------------------------------------
ImportSpaceDataView.prototype.setResult = function(result) {  
  this.importWizard.setResult(result);
};

//-------------------------------------------------------------------------
ImportSpaceDataView.prototype.startImportation = function() {
  this.isImportationStarted = true;  
};

//-------------------------------------------------------------------------
ImportSpaceDataView.prototype.stopImportation = function() {
  this.isImportationStarted = false;
};

//-------------------------------------------------------------------------
ImportSpaceDataView.prototype._init = function() {
  this.extParent = this.extParent || Ext.get(this.id);
  var content = this.merge(this.html, {importerTypes : this.importerTypes});
  this.extParent.dom.innerHTML = content;      
  
  this.extImportWizard = Ext.get('import-wizard');
  this.extImportDialog = this.extParent.select('.import-dialog').first();
  this.extProgressComposite = Ext.get('import-progress');
  this.extResultComposite = Ext.get('import-result');
  
  var elements = {
    import_dialog : this.extImportDialog.dom,
    progress : this.extProgressComposite.dom,
    result : this.extResultComposite.dom
  };
  
  this.importWizard = new ImportSpaceDataWizardDialog(this.extImportWizard.dom, elements);     
  this.importWizard.on(ImportSpaceDataWizardDialog.CLICK_IMPORT, { notify: function(event) { this._clickImportButtonHandler(event);}}, this);
  this.importWizard.on(ImportSpaceDataWizardDialog.CLICK_STOP, { notify: function(event) { this._clickCancelButtonHandler(event);}}, this);
  this.importWizard.on(ImportSpaceDataWizardDialog.CLICK_CLOSE, { notify: function(event) { this._clickCloseButtonHandler(event);}}, this);   
};

//-------------------------------------------------------------------------
ImportSpaceDataView.prototype._clickImportButtonHandler = function(event) {
  var form = event.data.form;
  var importerTypeId = event.data.importerTypeId;
  this.presenter.startImportation(form, importerTypeId);
};

//-------------------------------------------------------------------------
ImportSpaceDataView.prototype._clickCancelButtonHandler = function(event) {
  var result = {message : Lang.importation_canceled, duration : event.data.progress.duration};
  this.importWizard.setResult(result);
  this.presenter.stopImportation();
};

//-------------------------------------------------------------------------
ImportSpaceDataView.prototype._clickCloseButtonHandler = function(event) {
  this.presenter.stopImportation();
};
