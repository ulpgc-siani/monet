
UpdateBusinessSpaceDialog = function (layername) {
  var html = AppTemplate.DialogUpdateBusinessSpace;
  this.$layer = $(layername);
  this.$layer.innerHTML =  translate(html, Lang.DialogUpdateBusinessSpace);
  
  this.handlers = {
	'result' : {fn:null, scope:null},
    'back': {fn:null, scope:null}
  };
  
  this.bind();  
};

//------------------------------------------------------------------
UpdateBusinessSpaceDialog.prototype.show = function() {
  this.$layer.show();
};

//------------------------------------------------------------------
UpdateBusinessSpaceDialog.prototype.hide = function() {
  this.$layer.hide();	
};

//------------------------------------------------------------------
UpdateBusinessSpaceDialog.prototype.on = function(eventName, handler, scope) {
  this.handlers[eventName] = {fn: handler, scope: scope};	
};

//------------------------------------------------------------------
UpdateBusinessSpaceDialog.prototype.un = function(eventName) {
  this.handlers[eventName] = null;	
};

//--------------------------------------------------------------------------------------------------
UpdateBusinessSpaceDialog.prototype.bind = function() {	
  this.extParent = Ext.get('dlgUpdateBusinessSpace');
  this.extForm         = Ext.get(this.extParent.query('form').first());
  this.extBackButton   = Ext.get(this.extParent.query('a[name="back-button"]').first());
  this.extUpdateButton = Ext.get(this.extParent.query('input[name="update-button"]').first());
  this.extFile         = Ext.get(this.extParent.query('input[type="file"]').first()); 
  
  this.extBackButton.on('click' , this._clickBackButtonHandler, this);
  this.extUpdateButton.on('click' , this._clickUpdateButtonHandler, this);  
};

//--------------------------------------------------------------------------------------------------
UpdateBusinessSpaceDialog.prototype.check = function() {
  var sMessage = EMPTY;  
  if (this.extFile.dom.value == "") sMessage += "<li>" + Lang.DialogUpdateBusinessSpace.Error.FileRequired + "</li>"; 
  if (sMessage != EMPTY) { this.showStatus(sMessage); }
  return (sMessage == EMPTY);
};

//--------------------------------------------------------------------------------------------------
UpdateBusinessSpaceDialog.prototype.submit = function() {   
  if (!this.check()) return;
  var action = new CGActionUpdateBusinessSpace();
  action.SourceForm = this.extForm.dom;
  action.execute();  	 
};

//--------------------------------------------------------------------------------------------------
UpdateBusinessSpaceDialog.prototype._clickBackButtonHandler = function(event, target, options) {
  event.preventDefault();	
  var handler = this.handlers['back'];
  if (!handler) return;
  handler.fn.call(handler.scope);
};

//--------------------------------------------------------------------------------------------------
UpdateBusinessSpaceDialog.prototype._clickUpdateButtonHandler = function(event, target, options) {
  event.preventDefault();
  var handler = this.handlers['result'];
  if (!handler) return;
  handler.fn.call(handler.scope);
};
