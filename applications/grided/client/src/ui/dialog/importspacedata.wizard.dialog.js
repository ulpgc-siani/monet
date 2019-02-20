var ImportSpaceDataWizardDialog = Dialog.extend({
  init : function(element, elements) {
    this._super(element);
    this.elements = elements;
    this.wizard = new Wizard();
    this._bind();
  }
});
ImportSpaceDataWizardDialog.CLICK_IMPORT = 'click_import';
ImportSpaceDataWizardDialog.CLICK_STOP   = 'click_stop';
ImportSpaceDataWizardDialog.CLICK_CLOSE  = 'click_close';

//-------------------------------------------------------------------------------------------
ImportSpaceDataWizardDialog.prototype.setImporterTypes = function(importerTypes) {
  this.importDialog.setImporterTypes(importerTypes);
};

//-------------------------------------------------------------------------------------------
ImportSpaceDataWizardDialog.prototype.setProgress = function(progress) {
  this.progressComposite.setProgress(progress);
};

//-------------------------------------------------------------------------------------------
ImportSpaceDataWizardDialog.prototype.setResult = function(result) {
  this.resultComposite.setResult(result);  
};

//-------------------------------------------------------------------------------------------
ImportSpaceDataWizardDialog.prototype.open = function() {
  this.$element.show();
  this.wizard.start();
};

//-------------------------------------------------------------------------------------------
ImportSpaceDataWizardDialog.prototype._init = function() {
  this.progressComposite = null;
  this.importDialog = null;
  this.resultComposite = null;
};

//-------------------------------------------------------------------------------------------
ImportSpaceDataWizardDialog.prototype._bind = function() {
  this.progressComposite = this._getProgressComposite();
  this.importDialog      = this._getImportDialog();
  this.resultComposite   = this._getResultComposite();
  
  this.importDialog.on(ImportSpaceDataDialog.CLICK_IMPORT, {notify : function(event) {this._clickImportButtonHandler(event);}}, this);
  this.progressComposite.on(ImportProgressComposite.CLICK_STOP, {notify : function(event) {this._clickStopButtonHandler(event);}}, this);
  this.resultComposite.on(ImportResultComposite.CLICK_CLOSE, {notify : function(event) {this._clickCloseButtonHandler(event);}}, this);
    
  var firstPage  = new Page(this.importDialog); 
  var secondPage = new Page(this.progressComposite); 
  var thirdPage  = new Page(this.resultComposite); 
  
  var dialog = this.importDialog;
  firstPage.show = function() { dialog.open(); };
  firstPage.hide = function() { dialog.close(); };
    
  this.wizard.addPage(firstPage);
  this.wizard.addPage(secondPage);
  this.wizard.addPage(thirdPage);    
};

//-------------------------------------------------------------------------------------------
ImportSpaceDataWizardDialog.prototype._clickImportButtonHandler = function(event) {
  this.wizard.next();
  this.fire(event);  
};

//-------------------------------------------------------------------------------------------
ImportSpaceDataWizardDialog.prototype._clickStopButtonHandler = function(event) {
  this.wizard.next();
  this.fire(event);
};

//-------------------------------------------------------------------------------------------
ImportSpaceDataWizardDialog.prototype._clickCloseButtonHandler = function(event) {
  this._init();
  this.wizard.start();
  this.fire(event);
};


//-------------------------------------------------------------------------------------------
ImportSpaceDataWizardDialog.prototype._getImportDialog = function() {
  var importDialog = this.importDialog;
  
  if (importDialog == null) {
    var importDialog = new ImportSpaceDataDialog(this.elements.import_dialog);    
    importDialog.on(ImportSpaceDataDialog.CLICK_IMPORT_EVENT, {notify: function(event) {
      var form = event.data.form;
      var importerTypeId = event.data.importerTypeId;
      
      this.presenter.startImportation(form, importerTypeId);
    }}, this);    
  }
  return importDialog; 
};

//-------------------------------------------------------------------------------------------
ImportSpaceDataWizardDialog.prototype._getProgressComposite = function() {
  var resultComposite = this.resultComposite;
  if (resultComposite == null) {
    resultComposite = new ImportProgressComposite(this.elements.progress);
    resultComposite.hide();
  }
  return resultComposite;
};

//-------------------------------------------------------------------------------------------
ImportSpaceDataWizardDialog.prototype._getResultComposite = function() {
  var resultComposite = this.resultComposite;
  if (resultComposite == null) {
    resultComposite = new ImportResultComposite(this.elements.result);
    resultComposite.hide();
  }
  return resultComposite;
};

