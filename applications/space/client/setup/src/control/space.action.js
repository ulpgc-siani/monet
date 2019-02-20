//----------------------------------------------------------------------
// Load BusinessSpace
//----------------------------------------------------------------------

function CGActionLoadBusinessSpace () {
  this.base = CGAction;
  this.base(2);
}

CGActionLoadBusinessSpace.prototype = new CGAction;
CGActionLoadBusinessSpace.constructor = CGActionLoadBusinessSpace;
CommandFactory.register(CGActionLoadBusinessSpace, null, false);

CGActionLoadBusinessSpace.prototype.onFailure = function(sResponse){  
  Desktop.showError(Lang.Action.LoadBusinessSpace.Failure);
};

CGActionLoadBusinessSpace.prototype.step_1 = function(){    
  Desktop.reportProgress(Lang.Action.LoadBusinessSpace.Waiting);  
  Kernel.loadBusinessSpace(this);
};

CGActionLoadBusinessSpace.prototype.step_2 = function(){ 	  
  Desktop.hideReports();

  if (this.data == null) this.terminate();
  
  var serializer = new BusinessSpaceSerializer();
  BusinessSpace = serializer.unserialize(BusinessSpace, this.data);  
  Desktop.spaceLoaded(BusinessSpace);    
  this.terminate();
};


//----------------------------------------------------------------------
//Upload Business Space
//----------------------------------------------------------------------
function CGActionUploadBusinessSpace () {
  this.base = CGAction;
  this.base(2);
}

CGActionUploadBusinessSpace.prototype = new CGAction;
CGActionUploadBusinessSpace.constructor = CGActionUploadBusinessSpace;
CommandFactory.register(CGActionUploadBusinessSpace, null, false);

CGActionUploadBusinessSpace.prototype.onFailure = function(sResponse){  
  Desktop.showError(Lang.Action.UploadBusinessSpace.Failure);
};

CGActionUploadBusinessSpace.prototype.step_1 = function(){    
  Desktop.reportProgress(Lang.Action.UploadBusinessSpace.Waiting);
  Application.setState(Application.States.UPLOADING_SPACE);
  Kernel.uploadBusinessSpace(this, this.SourceForm);
};

CGActionUploadBusinessSpace.prototype.step_2 = function(){ 	  
  Desktop.hideReports();
  Application.setState(Application.States.INITIAL);
  if (this.data == null) this.terminate();
  
  var serializer = new BusinessSpaceSerializer();
  BusinessSpace = serializer.unserialize(BusinessSpace, this.data);  
//  Desktop.spaceLoaded(BusinessSpace);    
  this.terminate();
};


//----------------------------------------------------------------------
// Update Business Space
//----------------------------------------------------------------------
function CGActionUpdateBusinessSpace () {
  this.base = CGAction;
  this.base(2);
}

CGActionUpdateBusinessSpace.prototype = new CGAction;
CGActionUpdateBusinessSpace.constructor = CGActionUpdateBusinessSpace;
CommandFactory.register(CGActionUpdateBusinessSpace, null, false);

CGActionUpdateBusinessSpace.prototype.onFailure = function(sResponse){  
  Desktop.showError(Lang.Action.UpdateBusinessSpace.Failure); 
};

CGActionUpdateBusinessSpace.prototype.step_1 = function(){  
  Desktop.reportProgress(Lang.Action.UpdateBusinessSpace.Waiting);
  Application.setState(Application.States.UPLOADING_SPACE);
  Kernel.updateBusinessSpace(this, this.SourceForm);
};

CGActionUpdateBusinessSpace.prototype.step_2 = function(){  
  Desktop.hideReports();
  Application.setState(Application.States.INITIAL);
  if (this.data == null) this.terminate();
  
  var serializer = new BusinessSpaceSerializer();
  serializer.unserialize(BusinessSpace, this.data);
    
  Desktop.spaceUpdated(BusinessSpace);
  this.terminate();  
};


//----------------------------------------------------------------------
//LOAD SERVICES
//----------------------------------------------------------------------
function CGActionLoadServices() {
  this.base = CGAction;
  this.base(2);
}

CGActionLoadServices.prototype = new CGAction;
CGActionLoadServices.constructor = CGActionLoadServices;
CommandFactory.register(CGActionLoadServices, null, false);

CGActionLoadServices.prototype.onFailure = function(sResponse){
  Desktop.showError(Lang.Action.LoadServices.Failure);
};

CGActionLoadServices.prototype.step_1 = function(){  
  Desktop.reportProgress(Lang.Action.LoadServices.Waiting);
  Kernel.loadServices(this);
};

CGActionLoadServices.prototype.step_2 = function() {
  Desktop.hideReports();
  if (this.data == null) this.terminate();
  
  var serializer = new ServiceListSerializer();
  var services = serializer.unserialize(this.data);     
  BusinessSpace.setServices(services);    
};

//----------------------------------------------------------------------
//LOAD THESAURUS
//----------------------------------------------------------------------
function CGActionLoadThesaurus() {
  this.base = CGAction;
  this.base(2);
}

CGActionLoadThesaurus.prototype = new CGAction;
CGActionLoadThesaurus.constructor = CGActionLoadThesaurus;
CommandFactory.register(CGActionLoadThesaurus, null, false);

CGActionLoadThesaurus.prototype.onFailure = function() {
  Desktop.showError(Lang.Action.LoadThesaurus.Failure);
};

CGActionLoadThesaurus.prototype.step_1 = function() {
  Kernel.loadThesaurus(this);
};

CGActionLoadThesaurus.prototype.step_2 = function() {
  Desktop.hideReports();

  if (this.data == null) this.terminate();

  var serializer = new ThesaurusListSerializer();
  var thesaurusList = serializer.unserialize(this.data);
  Desktop.Bottom.thesaurusTab.setTarget(thesaurusList);    
};


//----------------------------------------------------------------------
//LOAD MAPS
//----------------------------------------------------------------------
function CGActionLoadMaps() {
  this.base = CGAction;
  this.base(2);
}

CGActionLoadMaps.prototype = new CGAction;
CGActionLoadMaps.constructor = CGActionLoadMaps;
CommandFactory.register(CGActionLoadMaps, null, false);

CGActionLoadMaps.prototype.onFailure = function() {
  Desktop.showError(Lang.Action.LoadMaps.Failure);
};

CGActionLoadMaps.prototype.step_1 = function() {
	Kernel.loadMaps(this);
};

CGActionLoadMaps.prototype.step_2 = function() {
  Desktop.hideReports();
  
  if (this.data == null) this.terminate();

  var serializer = new MapListSerializer();
  var mapsList = serializer.unserialize(this.data);  
  BusinessSpace.setMaps(mapsList);
};

//----------------------------------------------------------------------
//LOAD SERVICES PROVIDERS
//----------------------------------------------------------------------
function CGActionLoadServiceProviders () {
  this.base = CGAction;
  this.base(2);
}

CGActionLoadServiceProviders.prototype = new CGAction;
CGActionLoadServiceProviders.constructor = CGActionLoadServiceProviders;
CommandFactory.register(CGActionLoadServiceProviders, null, false);

CGActionLoadServiceProviders.prototype.onFailure = function(sResponse){
  Desktop.showError(Lang.Action.LoadServiceProviders.Failure);
};

CGActionLoadServiceProviders.prototype.step_1 = function(){  
  Desktop.reportProgress(Lang.Action.LoadServiceProviders.Waiting);
  Kernel.loadServiceProviders(this);
};

CGActionLoadServiceProviders.prototype.step_2 = function() {
  Desktop.hideReports();
  if (this.data == null) this.terminate();
  
  var serializer = new ServiceProviderListSerializer();
  var serviceProviders = serializer.unserialize(this.data);     
  BusinessSpace.setServiceProviders(serviceProviders);    
};

//----------------------------------------------------------------------
//LOAD THESAURUS PROVIDERS
//----------------------------------------------------------------------
function CGActionLoadThesaurusProviders() {
  this.base = CGAction;
  this.base(2);
}

CGActionLoadThesaurusProviders.prototype = new CGAction;
CGActionLoadThesaurusProviders.constructor = CGActionLoadThesaurusProviders;
CommandFactory.register(CGActionLoadThesaurusProviders, null, false);

CGActionLoadThesaurusProviders.prototype.onFailure = function() {
  Desktop.showError(Lang.Action.LoadThesaurusProviders.Failure);
};

CGActionLoadThesaurusProviders.prototype.step_1 = function() {
  Kernel.loadThesaurusProviders(this);
};

CGActionLoadThesaurusProviders.prototype.step_2 = function() {
  Desktop.hideReports();
  
  if (this.data == null) this.terminate();

  var serializer = new ThesaurusProvidersListSerializer();
  var thesaurusProvidersList = serializer.unserialize(this.data);  
  BusinessSpace.setThesaurusProviders(thesaurusProvidersList);
};

//----------------------------------------------------------------------
//LOAD MAP PROVIDERS
//----------------------------------------------------------------------
function CGActionLoadMapProviders() {
  this.base = CGAction;
  this.base(2);
}

CGActionLoadMapProviders.prototype = new CGAction;
CGActionLoadMapProviders.constructor = CGActionLoadMapProviders;
CommandFactory.register(CGActionLoadMapProviders, null, false);

CGActionLoadMapProviders.prototype.onFailure = function() {
  Desktop.showError(Lang.Action.LoadMapProviders.Failure);
};

CGActionLoadMapProviders.prototype.step_1 = function() {
  Kernel.loadMapProviders(this);
};

CGActionLoadMapProviders.prototype.step_2 = function() {
  Desktop.hideReports();
  
  if (this.data == null) this.terminate();

  var serializer = new MapProvidersListSerializer();
  var mapProvidersList = serializer.unserialize(this.data);  
  BusinessSpace.setMapProviders(mapProvidersList);
};


//----------------------------------------------------------------------
// RUN BUSINESS SPACE
//----------------------------------------------------------------------

function CGActionRunBusinessSpace() {
  this.base = CGAction;
  this.base(2);
}

CGActionRunBusinessSpace.prototype = new CGAction;
CGActionRunBusinessSpace.constructor = CGActionRunBusinessSpace;
CommandFactory.register(CGActionRunBusinessSpace, null, false);

CGActionRunBusinessSpace.prototype.onFailure = function() {
  Application.setState(Application.States.STOPPED);
  Desktop.showError(Lang.Action.RunBusinessSpace.Failure);
};

CGActionRunBusinessSpace.prototype.step_1 = function() {	
  Desktop.reportProgress(Lang.Action.RunBusinessSpace.Waiting);
  Kernel.runBusinessSpace(this);
};

CGActionRunBusinessSpace.prototype.step_2 = function() {	
  Desktop.hideReports();
  var context = {state: BusinessSpace.RUNNING, date: new Date()};
  BusinessSpace.setExecutionContext(context);
  Application.setState(Application.States.RUNNING);   
};

//----------------------------------------------------------------------
// STOP BUSINESS SPACE WORKERS
//----------------------------------------------------------------------

function CGActionStopBusinessSpace() {
  this.base = CGAction;
  this.base(2);
}

CGActionStopBusinessSpace.prototype = new CGAction;
CGActionStopBusinessSpace.constructor = CGActionStopBusinessSpace;
CommandFactory.register(CGActionStopBusinessSpace, null, false);

CGActionStopBusinessSpace.prototype.onFailure = function() {
  Application.setState(Application.States.RUNNING);
  Desktop.showError(Lang.Action.StopBusinessSpace.Failure);
};

CGActionStopBusinessSpace.prototype.step_1 = function() {
  switch (Application.getState()) {
    case Application.States.IMPORTING:      
      Desktop.reportProgress(Lang.Action.StopBusinessSpace.Waiting);
      
      var closeHandler = function() {dialog.hide();};
      
      var dialog = new MessageDialog();
      dialog.setTitle(Lang.Dialog.Message.title);
      dialog.setMessage(Lang.Action.StopBusinessSpace.Deny);
      dialog.setResultHandler(closeHandler, this);
      dialog.show();      
      break;
      
    case Application.States.RUNNING:
      Kernel.stopBusinessSpace(this);    	
      break;
  }	
};

CGActionStopBusinessSpace.prototype.step_2 = function() {	
  Desktop.hideReports();
  var context = {state: BusinessSpace.STOP, date: null};  
  BusinessSpace.setExecutionContext(context);
  Application.setState(Application.States.STOPPED);  
 };

//----------------------------------------------------------------------
// START IMPORT DATA 
//----------------------------------------------------------------------

function CGActionStartImportData() {
  this.base = CGAction;
  this.base(2);
}

CGActionStartImportData.prototype = new CGAction;
CGActionStartImportData.constructor = CGActionStartImportData;
CommandFactory.register(CGActionStartImportData, null, false);

CGActionStartImportData.prototype.onFailure = function() {};

CGActionStartImportData.prototype.step_1 = function() {
  Desktop.onStartImport();	
  Kernel.startImportData(this, this.SourceForm);
};

CGActionStartImportData.prototype.step_2 = function() {
  Application.setState(Application.States.IMPORTING);	  
};

//----------------------------------------------------------------------
// STOP IMPORT DATA 
//----------------------------------------------------------------------
function CGActionStopImportData() {
  this.base = CGAction;
  this.base(2);
}

CGActionStopImportData.prototype = new CGAction;
CGActionStopImportData.constructor = CGActionStopImportData;
CommandFactory.register(CGActionStopImportData, null, false);

CGActionStopImportData.prototype.onFailure = function() {
  //Desktop.showError(Lang.Action.StopBusinessSpace.Failure);  	
};

CGActionStopImportData.prototype.step_1 = function() {
  Kernel.stopImportData(this);    
};

CGActionStopImportData.prototype.step_2 = function() {
  Application.setState(Application.States.RUNNING);	
  Desktop.onStopImport();  
};









//----------------------------------------------------------------------
// Clear EventLog
//----------------------------------------------------------------------
function CGActionClearEventLog () {
  this.base = CGAction;
  this.base(2);
  this.EventLogList = new Array();
}

CGActionClearEventLog.prototype = new CGAction;
CGActionClearEventLog.constructor = CGActionClearEventLog;
CommandFactory.register(CGActionClearEventLog, null, false);

CGActionClearEventLog.prototype.onFailure = function(sResponse){
  Desktop.reportError(this.getErrorMessage(Lang.Action.ClearEventLog.Failure, sResponse));
};

CGActionClearEventLog.prototype.step_1 = function(){
  Kernel.clearEventLog(this, ViewEventLogGrid.Filter);
};

CGActionClearEventLog.prototype.step_2 = function(){
  ViewEventLogGrid.refresh();
};

//----------------------------------------------------------------------
// Refresh EventLog
//----------------------------------------------------------------------
function CGActionRefreshEventLog () {
  this.base = CGAction;
  this.base(1);
  this.EventLogList = new Array();
}

CGActionRefreshEventLog.prototype = new CGAction;
CGActionRefreshEventLog.constructor = CGActionRefreshEventLog;
CommandFactory.register(CGActionRefreshEventLog, null, false);

CGActionRefreshEventLog.prototype.onFailure = function(sResponse){
  Desktop.reportError(this.getErrorMessage(Lang.Action.RefreshEventLog.Failure, sResponse));
};

CGActionRefreshEventLog.prototype.step_1 = function(){
  ViewEventLogGrid.refresh();
};

//----------------------------------------------------------------------
// Filter EventLog
//----------------------------------------------------------------------
function CGActionFilterEventLog () {
  this.base = CGAction;
  this.base(1);
  this.EventLogList = new Array();
}

CGActionFilterEventLog.prototype = new CGAction;
CGActionFilterEventLog.constructor = CGActionFilterEventLog;
CommandFactory.register(CGActionFilterEventLog, { Filter : 0 }, false);

CGActionFilterEventLog.prototype.onFailure = function(sResponse){
  Desktop.reportError(this.getErrorMessage(Lang.Action.FilterEventLog.Failure, sResponse));
};

CGActionFilterEventLog.prototype.step_1 = function(){
  ViewEventLogGrid.setFilter(this.Filter);
  ViewEventLogGrid.refresh();
};