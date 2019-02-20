//----------------------------------------------------------------------
// Load BusinessModel
//----------------------------------------------------------------------
function CGActionLoadBusinessModel() {
  this.base = CGAction;
  this.base(2);
}

CGActionLoadBusinessModel.prototype = new CGAction;
CGActionLoadBusinessModel.constructor = CGActionLoadBusinessModel;
CommandFactory.register(CGActionLoadBusinessModel, null, false);

CGActionLoadBusinessModel.prototype.onFailure = function(sResponse) {  
  Desktop.showError(Lang.Action.LoadBusinessModel.Failure);  
};

CGActionLoadBusinessModel.prototype.step_1 = function(sResponse) {  
  Desktop.reportProgress(Lang.Action.LoadBusinessModel.Waiting);  
  Kernel.loadBusinessModel(this, this.SourceForm);
};

CGActionLoadBusinessModel.prototype.step_2 = function(sResponse) {	
  Desktop.hideReports();  
  if (this.data == null) this.terminate();
	  
  var serializer = new BusinessModelSerializer();
  BusinessModel = serializer.unserialize(BusinessModel, this.data);	  
  
  Desktop.modelLoaded(BusinessModel);  
  this.terminate();  
};

//----------------------------------------------------------------------
// Upload BusinessModel
//----------------------------------------------------------------------
function CGActionUploadBusinessModel() {
  this.base = CGAction;
  this.base(2);
}

CGActionUploadBusinessModel.prototype = new CGAction;
CGActionUploadBusinessModel.constructor = CGActionUploadBusinessModel;
CommandFactory.register(CGActionUploadBusinessModel, null, false);

CGActionUploadBusinessModel.prototype.onFailure = function(sResponse) {  
  Desktop.showError(Lang.Action.UploadBusinessModel.Failure);  
};

CGActionUploadBusinessModel.prototype.step_1 = function(sResponse) {  
  Desktop.reportProgress(Lang.Action.UploadBusinessModel.Waiting);
  Application.setState(Application.States.UPLOADING_MODEL);
  Kernel.uploadBusinessModel(this, this.SourceForm);
};

CGActionUploadBusinessModel.prototype.step_2 = function(sResponse) {	
  Desktop.hideReports();  
  if (this.data == null) this.terminate();
  Application.setState(Application.States.INITIAL);	  
  var serializer = new BusinessModelSerializer();
  BusinessModel = serializer.unserialize(BusinessModel, this.data);	  
  
  this.terminate();  
};


//----------------------------------------------------------------------
//Update BusinessModel
//----------------------------------------------------------------------
function CGActionUpdateBusinessModel() {
  this.base = CGAction;
  this.base(2);
}

CGActionUpdateBusinessModel.prototype = new CGAction;
CGActionUpdateBusinessModel.constructor = CGActionUpdateBusinessModel;
CommandFactory.register(CGActionUpdateBusinessModel, null, false);

CGActionUpdateBusinessModel.prototype.onFailure = function(sResponse) {	
  Desktop.showError(Lang.Action.UpdateBusinessModel.Failure);
};

CGActionUpdateBusinessModel.prototype.step_1 = function(sResponse) {	
  Desktop.reportProgress(Lang.Action.UpdateBusinessModel.Waiting);
  Application.setState(Application.States.UPLOADING_MODEL);
  Kernel.updateBusinessModel(this, this.SourceForm);  
};

CGActionUpdateBusinessModel.prototype.step_2 = function(sResponse) {
  Desktop.hideReports();
  Application.setState(Application.States.INITIAL);
  if (this.data == null) this.terminate();
  
  var serializer = new BusinessModelSerializer();
  BusinessModel = serializer.unserialize(BusinessModel, this.data);
  
  Desktop.modelLoaded(BusinessModel);
  this.terminate();  
};