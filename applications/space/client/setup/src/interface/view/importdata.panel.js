function ImportDataPanel(layername) {
  var html = AppTemplate.ImportDataPanel;
  this.$layer = $(layername);
  this.$layer.innerHTML = translate(html, Lang.ImportDataPanel);
  
  this.importDialogId   = 'import-data-dialog';
  this.progressDialogId = 'import-data-progress-dialog';
  this.resultDialogId   = "import-data-result-dialog";
  
  this._createWizard('import-data-wizard');          
  this.progressInterval = null;  
}

//------------------------------------------------------------------------------------
ImportDataPanel.prototype._createWizard = function(layername) {
  this.wizard = new WizardDialogImportData(layername);
  this.importDialog   = this._createImportDialog(this.importDialogId);
  this.progressDialog = this._createProgressDialog(this.progressDialogId);
  this.resultDialog   = this._createResultDialog(this.resultDialogId);
 
  this.wizard.setImportDialog(this.importDialog);
  this.wizard.setProgressDialog(this.progressDialog);
  this.wizard.setResultDialog(this.resultDialog);
  
  this.importDialog.disable();
  this.progressDialog.hide();
  this.resultDialog.hide();
};

//------------------------------------------------------------------------------------
ImportDataPanel.prototype.show = function() {
  this.$layer.show();
  this.importDialog.show();  
};

//------------------------------------------------------------------------------------
ImportDataPanel.prototype.hide = function() {
  this.$layer.hide();
};

//------------------------------------------------------------------------------------
ImportDataPanel.prototype.changeApplicationState = function(state) {  	
  switch (state){
    case Application.States.RUNNING:
	  this.importDialog.enable();	  	  
	break;	
	case Application.States.STOPPED:
	  this.importDialog.disable();
	  if (this.resultDialog.isVisible()) this.wizard.reset();
	break;	
  }		
};

//------------------------------------------------------------------------------------
ImportDataPanel.prototype.startLoadImportEvents = function() {
  this.wizard.startLoadImportEvents();	
};

//------------------------------------------------------------------------------------
ImportDataPanel.prototype.stopLoadImportEvents = function() {
  this.wizard.stopLoadImportEvents();	
};
//------------------------------------------------------------------------------------
ImportDataPanel.prototype._createProgressDialog = function(layername) {
  var dialog = new DialogImportDataProgress(layername);
  dialog.setTitle(Lang.Dialog.ImportDataProgress.title);  
  dialog.setMessage(Lang.Dialog.ImportDataProgress.message);
  dialog.setAbortProgressHandler(this.wizard, this.wizard.abortProgressHandler);
  return dialog;
};

//------------------------------------------------------------------------------------
ImportDataPanel.prototype._createImportDialog = function(layername) {
  var dialog = new DialogImportData(layername);
  dialog.setTitle(Lang.Dialog.ImportData.title);
  dialog.setMessage(Lang.Dialog.ImportData.message);  
  dialog.setImportHandler(this.wizard, this.wizard.importHandler);
  return dialog;
};

//------------------------------------------------------------------------------------
ImportDataPanel.prototype._createResultDialog = function(layername) {
  var dialog = new DialogImportDataResult(layername);
  dialog.setTitle(Lang.Dialog.ImportDataResult.title);
  dialog.setCloseHandler(this.wizard, this.wizard.closeResultDialogHandler);
  return dialog;
};

