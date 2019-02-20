var AddModelDialog = Dialog.extend({
  init : function(element) {
    this._super(element);
    this.html = translate(AppTemplate.AddModelDialog, Lang.AddModelDialog);
    element.innerHTML = this.html;
    
    this._bind();
  }	
});

AddModelDialog.ADD_EVENT = 'add';

//------------------------------------------------------------------
AddModelDialog.prototype.setFocus = function() {
  this.extName.dom.focus();  
};

//------------------------------------------------------------------
AddModelDialog.prototype.getData = function() {      
  var data = {    
    form : this.extAddForm.dom
  };
  return data;
};

//------------------------------------------------------------------
AddModelDialog.prototype.clear = function() {
  this.extName.dom.value = '';   
  this.extAddButton.dom.disabled = true;
};

//------------------------------------------------------------------
AddModelDialog.prototype._bind = function() {
  this.extParent = Ext.get(this.$el);
  
  this.extAddForm  = Ext.get(this.extParent.query('form[name="add-model"]').first());	  
  this.extName  = Ext.get(this.extParent.query('input[name="name"]').first());
  this.extVersion  = Ext.get(this.extParent.query('input[name="source"]').first());
    
  this.extAddButton    = Ext.get(this.extParent.query('input[name="add-button"]').first());
            
  this.extAddButton.on('click', this._addHandler, this);
  this.extName.on('keyup', this._changeHandler, this);    
//  this.extName.on('keypress', this._keyPressHandler, this);
  this.extVersion.on('change', this._changeHandler, this);    
  
  this.extAddButton.dom.disabled = true;
};
/*
//------------------------------------------------------------------
AddModelDialog.prototype._keyPressHandler = function(event, target, options) {  	
  if (event.keyCode != Ext.EventObject.ENTER) return;
	
  if (target === this.extName.dom) {
    event.preventDefault();
	  event.stopPropagation();	  
	  this._addHandler(event, target, options); 	
  }  
};
*/
//------------------------------------------------------------------
AddModelDialog.prototype._addHandler = function(event, target, options) {
  event.preventDefault();
  event.stopPropagation();	
  var evt = {name: AddModelDialog.ADD_EVENT, data: this};
  this.fire(evt);    
};

//------------------------------------------------------------------
AddModelDialog.prototype._changeHandler = function(event, target, options) {
  this.extAddButton.dom.disabled = true;
  if (this._validate()) this.extAddButton.dom.disabled = false;
};

//------------------------------------------------------------------
AddModelDialog.prototype._validate = function(event, target, options) {
  if ((this.extName.dom.value.length > 0) && (this.extVersion.dom.value != "")) return true;
  else return false;
};
