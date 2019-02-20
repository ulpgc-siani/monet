var AddFederationDialog = Dialog.extend({
  init : function(element) {
    this._super(element);	      
    this.html = translate(AppTemplate.AddFederationDialog, Lang.AddFederationDialog);
    element.innerHTML = this.html;

    this._bind();
  }	
});

AddFederationDialog.ADD_EVENT = "add";

//------------------------------------------------------------------
AddFederationDialog.prototype.setFocus = function() {
  this.extName.dom.focus();
};

//------------------------------------------------------------------
AddFederationDialog.prototype.getAddFormData = function() {      
  var data = {
    form : this.extAddForm.dom,		  
    name : this.extName.dom.value,
    url  : this.extURL.dom.value
  };
  return data;
};

//------------------------------------------------------------------
AddFederationDialog.prototype.clear = function() {
  this.extName.dom.value = "";	 
  this.extURL.dom.value = "";
  this.extAddButton.dom.disabled = true;
};

//------------------------------------------------------------------
AddFederationDialog.prototype._bind = function() {
  this.extParent = Ext.get(this.$el);
  this.extAddForm  = Ext.get(this.extParent.query('form[name="add-federation"]').first());
  
  this.extName = Ext.get(this.extParent.query('input[name="name"]').first());
  this.extURL  = Ext.get(this.extParent.query('input[name="url"]').first());
  this.extAddButton    = Ext.get(this.extParent.query('input[name="add-button"]').first());
            
  this.extAddButton.on('click', this._addHandler, this);
  
  this.extName.on('keyup', this._changeHandler, this);
  this.extURL.on('keyup', this._changeHandler, this);  
  this.extURL.on('keypress', this._keyPressHandler, this);
    
  this.extAddButton.dom.disabled = true;
};

//------------------------------------------------------------------
AddFederationDialog.prototype._keyPressHandler = function(event, target, options) {  
  if (event.keyCode != Ext.EventObject.ENTER) return;  
  if (target === this.extURL.dom) {
	  event.preventDefault();
	  event.stopPropagation();	  
    this._addHandler(event, target, options); return; 
  }     
};

//------------------------------------------------------------------
AddFederationDialog.prototype._addHandler = function(event, target, options) {
  event.preventDefault();
  event.stopPropagation();	
  var evt = {name: AddFederationDialog.ADD_EVENT, data: this};
  this.fire(evt);    
};

//------------------------------------------------------------------
AddFederationDialog.prototype._changeHandler = function(event, target, options) {    
  this.extAddButton.dom.disabled = true;
  if (this._validate()) this.extAddButton.dom.disabled = false;  
};

//------------------------------------------------------------------
AddFederationDialog.prototype._validate = function(event, target, options) {
  if (this.extName.dom.value.length > 0 && this.extURL.dom.value.length > 0) return true;
  else return false;
};