
var AddSpaceDialog = Dialog.extend({
  init : function(element, models) {
    this._super(element);	    
    this.models = models;
    this.html = translate(AppTemplate.AddSpaceDialog, Lang.AddSpaceDialog);
    element.innerHTML = this.html;

    this.defaultOption = new Option(Lang.AddSpaceDialog.select_model, 0);
    this.defaultOption.selected = true;
    
    this._bind();
  }	
});

AddSpaceDialog.ADD_EVENT = "add";

//------------------------------------------------------------------
AddSpaceDialog.prototype.setFocus = function() {
  this.extName.dom.focus();
};

//------------------------------------------------------------------
AddSpaceDialog.prototype.getAddFormData = function() {      
  var data = {    
    form : this.extAddForm.dom,
    name : this.extName.dom.value,
    url  : this.extURL.dom.value,
    modelId : this.extModelsCombo.dom.options[this.extModelsCombo.dom.selectedIndex].value
  };
  return data;
};

//------------------------------------------------------------------
AddSpaceDialog.prototype.clear = function() {
  this.extName.dom.value = "";
  this.extURL.dom.value = "";  
  this.extAddButton.dom.disabled = true;
  this.extModelsCombo.dom.selectedIndex = 0;
};

//------------------------------------------------------------------
AddSpaceDialog.prototype._bind = function() {
  this.extParent = Ext.get(this.$el);
  
  this.extAddForm  = Ext.get(this.extParent.query('form[name="add-space"]').first());	  
  this.extName  = Ext.get(this.extParent.query('input[name="name"]').first());
  this.extURL   = Ext.get(this.extParent.query('input[name="url"]').first());
    
  this.extAddButton   = Ext.get(this.extParent.query('input[name="add-button"]').first());
  this.extModelsCombo = Ext.get(this.extParent.query('select').first());
            
  this.extAddButton.on('click', this._addHandler, this);
  this.extName.on('keyup', this._changeHandler, this);
  this.extURL.on('keyup', this._changeHandler, this);
  this.extModelsCombo.on('change', this._changeHandler, this);
  this.extURL.on('keypress', this._keyPressHandler, this);
  this.extAddButton.on('keypress', this._keyPressHandler, this);
    
  this._createModelsCombo();
  this.extAddButton.dom.disabled = true;
};

//------------------------------------------------------------------
AddSpaceDialog.prototype._keyPressHandler = function(event, target, options) {  	
  if (event.keyCode != Ext.EventObject.ENTER) return;
	
  if (target === this.extURL.dom) {
   event.preventDefault();
   event.stopPropagation();
	 this._addHandler(event, target, options); 
	return;
  }  
};

//------------------------------------------------------------------
AddSpaceDialog.prototype._addHandler = function(event, target, options) {
  event.preventDefault();
  event.stopPropagation();	
  var evt = {name: AddSpaceDialog.ADD_EVENT, data: this};
  this.fire(evt);    
};

//------------------------------------------------------------------
AddSpaceDialog.prototype._changeHandler = function(event, target, options) {
  this.extAddButton.dom.disabled = true;  
  if (this._validate()) this.extAddButton.dom.disabled = false;   
};

//------------------------------------------------------------------
AddSpaceDialog.prototype._validate = function(event, target, options) {
  if ((this.extName.dom.value.length > 0) && (this.extURL.dom.value.length > 0) && (this.extModelsCombo.dom.selectedIndex != 0)) return true;
  else return false;
};

//------------------------------------------------------------------
AddSpaceDialog.prototype._createModelsCombo = function() {  
  this.extModelsCombo.dom.options[0] = this.defaultOption;
     
  for (var i=0, l = this.models.length; i < l; i++) {
    var model = this.models[i];
    var option = new Option(model.name, model.id);
    this.extModelsCombo.dom.options[this.extModelsCombo.dom.options.length] = option;
  }  
};