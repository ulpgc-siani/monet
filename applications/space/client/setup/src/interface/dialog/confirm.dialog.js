DialogConfirm = function() {  
  
};

//------------------------------------------------------------------
DialogConfirm.prototype.setTitle = function(title) {  
  this.title = title;	
};

//------------------------------------------------------------------
DialogConfirm.prototype.setMessage = function(message) {
  this.message = message;	  	
};

//------------------------------------------------------------------
DialogConfirm.prototype.setResultHandler = function(handler, scope) {
  this.handler = handler;
  this.scope = scope;
};

//------------------------------------------------------------------
DialogConfirm.prototype.show = function() {
  this.dialog = this._createDialog();
  this.dialog.confirm(this.title, this.message, this.handler, this.scope);      	   
};

//------------------------------------------------------------------
DialogConfirm.prototype.hide = function() {
  this.dialog.hide();
};

//------------------------------------------------------------------
DialogConfirm.prototype._createDialog = function() {	
  var dialog = Ext.MessageBox;	
  return dialog;
 };
  
