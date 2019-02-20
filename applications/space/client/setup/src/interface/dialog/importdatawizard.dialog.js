function WizardDialogImportData(layername) {    
  this.layername = layername;
  this.$layer = $(layername);  
  var html = AppTemplate.WizardDialogImportData;
  this.$layer.innerHTML = translate(html, {});
	  
  this.importDialog = null;
  this.progressDialog = null;
  this.resultDialog = null;
  this.progressInterval = null;         
};

WizardDialogImportData.prototype = new WizardDialogImport;
WizardDialogImportData.prototype.constructor = WizardDialogImportData; 

//--------------------------------------------------------------------------------
WizardDialogImportData.prototype.importHandler = function() {
  this.importDialog.submit();
};

//--------------------------------------------------------------------------------
WizardDialogImportData.prototype.abortProgressHandler = function() {
  var action = new CGActionStopImportData();
  action.execute();
  this._renderResult(Lang.Dialog.ImportDataResult.import_aborted);    
};

//--------------------------------------------------------------------------------
WizardDialogImportData.prototype.closeResultDialogHandler = function() {
  var action = new CGActionStopImportData();
  action.execute();	
  this.resultDialog.hide();
  this.importDialog.show();
}; 
