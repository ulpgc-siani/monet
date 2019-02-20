
DIALOG_TYPE = {};
//DIALOG_TYPE.UPLOAD_MODE = "upload";
//DIALOG_TYPE.UPDATE_MODE = "update";

UpdateBusinessModelDialog = function (layername) {
  var html = AppTemplate.DialogUpdateBusinessModel;
  this.$layer = $(layername);
  this.$layer.innerHTML = translate(html, Lang.DialogUpdateBusinessModel);
  
  this.handlers = {
    'result' : {fn:null, scope:null},
    'back': {fn:null, scope:null}
  };
  
  this.bind();  
};

//------------------------------------------------------------------
UpdateBusinessModelDialog.prototype.show = function() {
  this.$layer.show();
};

//------------------------------------------------------------------
UpdateBusinessModelDialog.prototype.hide = function() {
  this.$layer.hide();
};

//------------------------------------------------------------------
UpdateBusinessModelDialog.prototype.on = function(eventName, handler, scope) {
  this.handlers[eventName] = {fn: handler, scope: scope};	
};

//------------------------------------------------------------------
UpdateBusinessModelDialog.prototype.un = function(eventName) {
  this.handlers[eventName] = null;	
};

//------------------------------------------------------------------
UpdateBusinessModelDialog.prototype.bind = function() {
  this.extParent = Ext.get('dlgUpdateBusinessModel');
  this.extForm         = Ext.get(this.extParent.query('form').first());
  this.extUpdateButton = Ext.get(this.extParent.query('input[name="update-button"]').first());  
  this.extBackButton   = Ext.get(this.extParent.query('a[name="back-button"]').first());
  this.extFile         = Ext.get(this.extParent.query('input[type="file"]').first()); 
  
  this.extBackButton.on('click' , this._clickBackButtonHandler, this);
  this.extUpdateButton.on('click' , this._clickUpdateButtonHandler, this);  
};

//------------------------------------------------------------------
UpdateBusinessModelDialog.prototype.check = function() {
  var message = "";
  if (this.extFile.dom.value == "") {
	message = Lang.DialogUpdateBusinessModel.Error.FileRequired;
	this.showStatus(message);
  }
  return message == "";  
};

//------------------------------------------------------------------
UpdateBusinessModelDialog.prototype.showStatus = function(message) {
  Ext.MessageBox.alert(Lang.Exceptions.Title,message);
};

//------------------------------------------------------------------
UpdateBusinessModelDialog.prototype.submit = function() {
  if (! this.check()) return;
  var action = new CGActionUpdateBusinessModel();
  action.SourceForm = this.extForm.dom;
  action.execute();	  
};

//------------------------------------------------------------------
UpdateBusinessModelDialog.prototype._clickBackButtonHandler = function(event, target, options) {
  event.preventDefault();	
  var handler = this.handlers['back'];
  if (!handler) return;
  handler.fn.call(handler.scope);
};

//------------------------------------------------------------------
UpdateBusinessModelDialog.prototype._clickUpdateButtonHandler = function(event, target, options) {
  event.preventDefault();	
  var handler = this.handlers['result'];
  if (!handler) return;
  handler.fn.call(handler.scope);
};
