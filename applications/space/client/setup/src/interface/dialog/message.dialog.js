function MessageDialog() {
      
};

//------------------------------------------------------------------
MessageDialog.prototype.setTitle = function(title) {
  this.title = title;
};

//------------------------------------------------------------------
MessageDialog.prototype.setMessage = function(message) {
  this.message = message;	
};

//------------------------------------------------------------------
MessageDialog.prototype.setResultHandler = function(handler, scope) {
  this.handler = handler;
  this.scope = scope;
};

//------------------------------------------------------------------
MessageDialog.prototype.show = function() {  
  this.dialog = this._createDialog();	
  this.dialog.alert(this.title, this.message, this.handler, this.scope); 
};

//------------------------------------------------------------------
MessageDialog.prototype.hide = function() {
  this.dialog.hide();  
};

//------------------------------------------------------------------
MessageDialog.prototype._createDialog = function() {
 var dialog = Ext.MessageBox;	
  return dialog;
};
