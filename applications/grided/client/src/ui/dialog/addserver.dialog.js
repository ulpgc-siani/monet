var AddServerDialog = Dialog.extend({
  init : function(element) {
    this._super(element);
    this.html = translate(AppTemplate.AddServerDialog, Lang.AddServerDialog);
    element.innerHTML = this.html;
    
    this._bind();
  }
});
AddServerDialog.CLICK_ADD_EVENT = 'click-add';

//------------------------------------------------------------------
AddServerDialog.prototype.getData = function() {      
  var data = {
    name : this.extName.dom.value,
    ip : this.extIP.dom.value
  };
  return data;    	
};

//------------------------------------------------------------------
AddServerDialog.prototype.check = function() {
  if (this.extName.dom.value.length == 0 || this.extIP.dom.value.length == 0) {
    this.error = Lang.AddServerDialog.have_to_complete_info || "poner el mensaje a mostrar en AddSeverDialog";
    return false;
  }
  this.error = null;
  return true;
};

//------------------------------------------------------------------
AddServerDialog.prototype.showError = function() {
  if (this.error) alert(this.error); 
  this.error = null;
};

//------------------------------------------------------------------
AddServerDialog.prototype.clear = function() {
  this.extName.dom.value = "";
  this.extIP.dom.value = "";
  this.extAddButton.dom.disabled = true;
};

//------------------------------------------------------------------
AddServerDialog.prototype._bind = function() {
  this.extParent = Ext.get(this.$el); 	
  this.extName = Ext.get(this.extParent.query('input[name="name"]').first());
  this.extIP   = Ext.get(this.extParent.query('input[name="ip"]').first());
  this.extAddButton = Ext.get(this.extParent.select('input[name="add-button"]').first()); 
  
  this.extName.on('keyup', this._changeHandler, this);
  this.extIP.on('keyup', this._changeHandler, this);
  this.extAddButton.on('click', this._addButtonHandler, this);
  this.extParent.on('keypress', this._keyPressHandler, this);
  
  this.extAddButton.dom.disabled = true;
};

//------------------------------------------------------------------
AddServerDialog.prototype._addButtonHandler = function() {
  var event = {name: AddServerDialog.CLICK_ADD_EVENT, data: this};	
  this.fire(event);
  this.clear();
};

//------------------------------------------------------------------
AddServerDialog.prototype._changeHandler = function(event, target, options) {
  this.extAddButton.dom.disabled = true;
  if (this._validate()) this.extAddButton.dom.disabled = false;    
};

//------------------------------------------------------------------
AddServerDialog.prototype._keyPressHandler = function(event, target, options) {
  if (event.keyCode != Ext.EventObject.ENTER) return;
  this._addButtonHandler();
};

//------------------------------------------------------------------
AddServerDialog.prototype._validate = function(event, target, options) {
  if (this.extName.dom.value.length > 0 && this.extIP.dom.value.length > 0) return true;
  else return false;
};

