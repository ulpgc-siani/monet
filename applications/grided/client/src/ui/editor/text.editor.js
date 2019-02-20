var TextEditor = Editor.extend({	
  init : function(element, object, propertyName) {    	  
	this._super();
	this.element = element;
	this.object = object;
	this.propertyName = propertyName;
	
	this.oldValue = element.value;
	this.newValue = this.oldValue;
  }	
});

//----------------------------------------------------------------------------------
TextEditor.prototype.open = function() {
  this._bind();
  this.state = Editor.OPENED;
};

//----------------------------------------------------------------------------------
TextEditor.prototype.close = function() {
  this._unBind();
  this.state = Editor.CLOSED;
};

//----------------------------------------------------------------------------------
TextEditor.prototype.isDirty = function() {
  return this.oldValue != this.newValue;	
};

//----------------------------------------------------------------------------------
TextEditor.prototype.flush = function() {
  if (this.object.set(this.propertyName, this.newValue)) {
	  this.oldValue = this.newValue; 	  
  }
};

//----------------------------------------------------------------------------------
TextEditor.prototype.reset = function() {
  this.newValue = this.oldValue;
  this.element.value = this.oldValue;
};

//----------------------------------------------------------------------------------
TextEditor.prototype.showError = function(error) {
	
};

//----------------------------------------------------------------------------------
TextEditor.prototype._bind = function() {
  this.extElement = Ext.get(this.element);
  this.extElement.on('keyup', this._changeHandler, this);
};

//----------------------------------------------------------------------------------
TextEditor.prototype._unBind = function() {
  if (! this.extElement) return;
  this.extElement.un('keyup', this._changeHandler);
  this.extElement = null;
};

//----------------------------------------------------------------------------------
TextEditor.prototype._changeHandler = function(event, target, options) {
  this.newValue = target.value;
  
  if (this.newValue == this.oldValue) ev = {name: Events.RESTORED, data: {editor: this, value: this.newValue}};  
  else ev = {name: Events.CHANGED, data: {editor: this, value: this.newValue}};  
  this.fire(ev);
};
