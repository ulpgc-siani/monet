
function CGActionInit () {
  this.base = CGAction;
  this.base(3);
}

CGActionInit.prototype = new CGAction;
CGActionInit.constructor = CGActionInit;
CommandFactory.register(CGActionInit, null, false);

//PUBLIC
CGActionInit.prototype.onFailure = function(sResponse){
  var ActionLogout = new CGActionLogout();
  ActionLogout.execute();

  Ext.MessageBox.hide();  
  Ext.MessageBox.alert(Lang.Exceptions.Title, Lang.Exceptions.InitApplication);  
};

CGActionInit.prototype.step_1 = function() {
  var action = new CGActionLoadBusinessModel();
  action.ReturnProcess = this;
  action.execute();   		  
};

CGActionInit.prototype.step_2 = function() {	
  var action = new CGActionLoadBusinessSpace();
  action.ReturnProcess = this;
  action.execute();  
};

CGActionInit.prototype.step_3 = function() {
  Desktop.showViewsContainer();	  
  Desktop.show();    	
};

//-----------------------------------------------------------------------------------
function CGActionUploadSpaceAndModel() {
  this.base = CGAction;
  this.base(3);
}

CGActionUploadSpaceAndModel.prototype = new CGAction;
CGActionUploadSpaceAndModel.constructor = CGActionUploadSpaceAndModel;
CommandFactory.register(CGActionUploadSpaceAndModel, null, false);

CGActionUploadSpaceAndModel.prototype.onFailure = function(sResponse){
  Ext.MessageBox.hide();  
  Ext.MessageBox.alert(Lang.Exceptions.Title, Lang.Exceptions.InitApplication);  
};

CGActionUploadSpaceAndModel.prototype.step_1 = function() {
  var action = new CGActionUploadBusinessModel();
  action.SourceForm = this.forms[0];
  action.ReturnProcess = this;
  action.execute();   		  
};

CGActionUploadSpaceAndModel.prototype.step_2 = function() {
  var action = new CGActionUploadBusinessSpace();
  action.SourceForm = this.forms[1];
  action.ReturnProcess = this;
  action.execute();   		  
};

CGActionUploadSpaceAndModel.prototype.step_3 = function() {
  Desktop.Bottom.init();
  Application.registerListener(Desktop.Bottom);
  var action = new CGActionInit();
  action.ReturnProcess = this;
  action.execute();
  Application.Dialogs[Literals.Dialogs.UpdateSpaceWizard].hide();
};

//-----------------------------------------------------------------------------------
function CGActionLogout() {
  this.base = CGAction;
  this.base(2);
}

CGActionLogout.prototype = new CGAction;
CGActionLogout.constructor = CGActionLogout;
CommandFactory.register(CGActionLogout, null, false);

CGActionLogout.prototype.onFailure = function(sResponse){  
  Ext.MessageBox.alert(Lang.Exceptions.Title, Lang.Action.Logout.Failure);  
};

CGActionLogout.prototype.step_1 = function() {
  Kernel.logout(this);
};

CGActionLogout.prototype.step_2 = function() {
  var url = this.data;
  if (url.trim().length > 0) {
    window.location = url;
  }
};



