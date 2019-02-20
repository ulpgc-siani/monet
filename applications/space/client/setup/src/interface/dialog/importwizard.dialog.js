var ImportationState = {
	started : 0,
	stopped : 1
};

function WizardDialogImport() {
  this.importationState = ImportationState.stopped; 
}

//------------------------------------------------------------------------------------
WizardDialogImport.prototype.setImportDialog = function(importDialog) {
  this.importDialog = importDialog;
};

//------------------------------------------------------------------------------------
WizardDialogImport.prototype.setProgressDialog = function(progressDialog) {
  this.progressDialog = progressDialog;
};

//------------------------------------------------------------------------------------
WizardDialogImport.prototype.setResultDialog = function(resultDialog) {
  this.resultDialog = resultDialog;
};

//--------------------------------------------------------------------------------
WizardDialogImport.prototype.show = function() {
  this.$layer.show();
  this.importDialog.show();
};

//--------------------------------------------------------------------------------
WizardDialogImport.prototype.hide = function() {
  this.$layer.hide();
};

//--------------------------------------------------------------------------------
WizardDialogImport.prototype.reset = function() {
  this.importDialog.show();
  this.importDialog.disable();
  this.progressDialog.hide();
  this.resultDialog.hide();
};

//------------------------------------------------------------------------------------
WizardDialogImport.prototype.changeApplicationState = function(state) {
  switch (state){
    case Application.States.RUNNING:
	  this.importDialog.enable();      
	break;	
	case Application.States.STOPPED:
	  this.importDialog.disable();
	break;
  }
};

//--------------------------------------------------------------------------------
WizardDialogImport.prototype.startLoadImportEvents = function() {
  this._doStartLoad();
};

//--------------------------------------------------------------------------------
WizardDialogImport.prototype.stopLoadImportEvents = function() {
  this._doStopLoad();
};


WizardDialogImport.prototype.importHandler = function(){alert('abstract');}; 
WizardDialogImport.prototype.abortProgressHandler = function(){alert('abstract');}; 
WizardDialogImport.prototype.closeResultDialogHandler = function(){alert('abstract');}; 

//------------------------------------------------------------------------------------
WizardDialogImport.prototype._loadImportEvents = function() {
  if (this.importationState == ImportationState.stopped) return;
  
  var url = Context.Config.Api + "?op=loadimportevents";
  new Ajax.Request(url, {
	method: 'get',
	  onSuccess: function(transport, json) {
		if (transport.responseText == "") return;  
	    this.importResult = Ext.util.JSON.decode(transport.responseText);
	     
	    switch (this.importResult.type) {
	      case 'progress':
	    	if (this.importResult.progress.value === 100) {
	    	  var summary = {count: this.importResult.progress.currentCount, time: this._formatTime(this.importResult.progress.time)};	
	    	  this._renderResult(Lang.Dialog.ImportWizard.import_complete, summary);
	    	  this._doStopLoad();
	    	} else {  
	    	  this._renderProgress(this.importResult.progress);
	    	}
	        break;
	      case 'error':
	    	this._doStopLoad(); 
	    	this._renderError(Lang.Dialog.ImportWizard.import_failure, this.importResult.error); 	    	  	    	  
	        break;	  	    	  
	    } 
	  }.bind(this)
  });	    		
};

//--------------------------------------------------------------------------------
WizardDialogImport.prototype._doStartLoad = function() {
  var scope = this;
  this.importationState = ImportationState.started;
  
  var progress = {currentCount: 0, value: 0 ,time: 0, estimatedTime : 0}; 
  this._renderProgress(progress);
  
  this._loadImportEvents();  
  this.progressInterval = window.setInterval(function() { scope._loadImportEvents(); }, parseInt(Context.Config.PoolInterval)); 
};

//--------------------------------------------------------------------------------
WizardDialogImport.prototype._doStopLoad = function() {
  this.importationState = ImportationState.stopped;
  window.clearInterval(this.progressInterval);
};

//------------------------------------------------------------------------------------
WizardDialogImport.prototype._renderProgress = function(progress) {
  this.importDialog.hide();
  this.progressDialog.clear();
  this.progressDialog.setProgress(progress);
  this.progressDialog.show();	  
};

//--------------------------------------------------------------------------------
WizardDialogImport.prototype._renderResult = function(message, summary) {
  if (this.importDialog) this.importDialog.hide();	
  if (this.progressDialog) this.progressDialog.hide();
  
  this.resultDialog.clear();
  if (message) this.resultDialog.setMessage(message);
  if (summary) this.resultDialog.setSummary(summary);
  this.resultDialog.show();	
};

//--------------------------------------------------------------------------------
WizardDialogImport.prototype._renderError = function(message, error) {
  if (this.importDialog) this.importDialog.hide();	
  if (this.progressDialog) this.progressDialog.hide();
  
  this.resultDialog.clear();
  this.resultDialog.setMessage(message);
  if (error) this.resultDialog.setError(error);
  this.resultDialog.show();	
};

//--------------------------------------------------------------------------------
WizardDialogImport.prototype._formatTime = function(milliseconds) {
  var time = new Time(milliseconds);
  var tuple = time.format();
  var msg = '';
  
  msg += (tuple.hours)? ((tuple.hours == 1)? tuple.hours + ' ' + Lang.HOUR + ' ' : tuple.hours + ' ' + Lang.HOURS + ', ') : '';
  msg += (tuple.minutes)? ((tuple.minutes == 1)? tuple.minutes + ' ' + Lang.MINUTE + ' y ' : tuple.minutes + ' ' + Lang.MINUTES + ' y ') : '';
  msg += (tuple.seconds)? ((tuple.seconds == 1)? tuple.seconds + ' ' + Lang.SECOND: tuple.seconds + ' ' + Lang.SECONDS) : '';
  
  return msg;
};
