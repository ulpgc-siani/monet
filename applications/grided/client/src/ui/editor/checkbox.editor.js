var CheckboxEditor = Editor.extend({	
  init : function(element, object, propertyName) {    	  
	this._super();
	this.element = element;
	this.object = object;
	this.propertyName = propertyName;
	
	this.oldValue = object[propertyName];
	this.newValue = this.oldValue;
	this.element.checked = this.newValue;
  }	
});

//----------------------------------------------------------------------------------
CheckboxEditor.prototype.open = function() {
  this._bind();
  this.state = Editor.OPENED;
};

//----------------------------------------------------------------------------------
CheckboxEditor.prototype.close = function() {
  this._unBind();
  this.state = Editor.CLOSED;
};

//----------------------------------------------------------------------------------
CheckboxEditor.prototype.isDirty = function() {
  return this.oldValue != this.newValue;	
};

//----------------------------------------------------------------------------------
CheckboxEditor.prototype.flush = function() {
  this.object[this.propertyName] = this.newValue;	
  this.oldValue = this.newValue;
};

//----------------------------------------------------------------------------------
CheckboxEditor.prototype.reset = function() {
  this.newValue = this.oldValue;
  this.element.checked = this.oldValue;
};

//----------------------------------------------------------------------------------
CheckboxEditor.prototype._bind = function() {
  this.extElement = Ext.get(this.element);
  this.extElement.on('change', this._changeHandler, this);  
};

//----------------------------------------------------------------------------------
CheckboxEditor.prototype._unBind = function() {
  if (! this.extElement) return;
  this.extElement.un('change', this._changeHandler);  
  this.extElement = null;
};

//----------------------------------------------------------------------------------
CheckboxEditor.prototype._changeHandler = function(event, target, options) {
  var ev = null;
  
  this.newValue = target.checked;
  if (this.newValue == this.oldValue) ev = {name: Events.RESTORED, data: {editor: this, value: this.newValue}};
  else ev = {name: Events.CHANGED, data: {editor: this, value: this.newValue}}; 
  this.fire(ev);
};
