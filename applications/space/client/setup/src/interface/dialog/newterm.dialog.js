function DialogNewTerm(layername) {
  this.html = AppTemplate.DialogNewTerm;	
  this.layername = layername;
  this.$layer = $(this.layername);
  this.$layer.innerHTML = "";
  
  this.NEW_BUTTON   = Lang.Dialog.NewTerm.newTerm;
  this.SAVE_BUTTON  = Lang.Dialog.NewTerm.save;
  this.CLOSE_BUTTON = Lang.Dialog.NewTerm.close;   
  this.bind();
}  

//--------------------------------------------------------------------------------
DialogNewTerm.prototype.setTitle = function(title) {
  this.extDialog.header.dom.innerHTML = (title)? title : "";  	
};

//--------------------------------------------------------------------------------
DialogNewTerm.prototype.setMessage = function(message) {
  this.extDialog.header.dom.innerHTML = (message)? message : "";
};

//--------------------------------------------------------------------------------
DialogNewTerm.prototype.show = function() {
  this.extDialog.show();	
  var extInput = Ext.get(this.extDialog.getEl().query('input').first().id);
  this.extDialog.buttons[1].disable();
  extInput.focus();
};

//--------------------------------------------------------------------------------
DialogNewTerm.prototype.hide = function() {
  this.extDialog.hide();  	
};

//--------------------------------------------------------------------------------
DialogNewTerm.prototype.clear = function() {
  this.extCode.dom.value = '';
  this.extValue.dom.value = '';   
  this.extParent.dom.value = '';  
  this.extDialog.buttons[1].disable();
  var extInput = Ext.get(this.extDialog.getEl().query('input').first().id);
  extInput.focus();
};

//--------------------------------------------------------------------------------
DialogNewTerm.prototype.getRecord = function() {
  var RecordClass = Ext.data.Record.create([
    {name: 'code', type: 'string'},
    {name: 'value', type: 'string'},
    {name: 'parent', type: 'string'},
    {name: 'enabled', type: 'boolean'}]);
  
  var record = new RecordClass({
    code: this.extCode.dom.value, 
	value: this.extValue.dom.value, 
	parent: this.extParent.dom.value, 
	enabled: (this.extEnabled.dom.selectedIndex == 0)? true:false
  });
  return record;
};

//--------------------------------------------------------------------------------
DialogNewTerm.prototype.addButton = function(buttonName, handler, scope) {
  this.extDialog.addButton(buttonName, handler, scope);    	
};

//--------------------------------------------------------------------------------
DialogNewTerm.prototype.bind = function() {
  this.extDialog = this._createDialog();
  this.extDialog.header.dom.innerHTML = Lang.Dialog.NewTerm.title;
  this.extDialog.body.dom.innerHTML = translate(this.html, Lang.Dialog.NewTerm);  
  
  this.extError   = this.extDialog.body.select('.error').first();  
  this.extMessage = this.extDialog.body.select('.message').first();    
  this.extCode    = this.extDialog.body.select('.code').first();
  this.extValue   = this.extDialog.body.select('.value').first();
  this.extParent  = this.extDialog.body.select('.parent').first();
  this.extEnabled = this.extDialog.body.select('.enabled').first();
  
  this.extCode.on('change', this._checkHandler, this);
  this.extValue.on('change', this._checkHandler, this);
  this.extParent.on('change', this._checkHandler, this);
  this.extEnabled.on('change', this._checkHandler, this); 
};

//--------------------------------------------------------------------------------
DialogNewTerm.prototype._checkHandler = function(event, target, options) {    
  if (this.extCode.dom.value.trim().length == 0 || this.extValue.dom.value.trim().length  == 0 ||
      this.extParent.dom.value.trim().length == 0) {
	  this.extDialog.buttons[1].disable();
	  return;
  }
  this.extDialog.buttons[1].enable();  
};

//--------------------------------------------------------------------------------
DialogNewTerm.prototype._createDialog = function() {
  var dialog = new Ext.BasicDialog(this.layername, {
    modal: true,
    shadow: false,
    width: 435,
    height: 236,
    minWidth: 430,
    minHeight: 230,
    minimizable: false
  });
    
  return dialog;
};

//--------------------------------------------------------------------------------
DialogNewTerm.prototype._showError = function(message) {
  this.extError.dom.innerHTML = message;	
  this.extError.fadeIn();
  var scope = this;
  setTimeout(function(scope) {scope.extError.fadeOut();}, 2000);
};
