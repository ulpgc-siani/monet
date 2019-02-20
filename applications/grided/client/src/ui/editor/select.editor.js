var SelectEditor = Editor.extend({	
  init : function(element, object, propertyName) {    	  
	this._super();
	this.element = element;
	this.object = object;
	this.propertyName = propertyName;
	
	this.oldSelectedIndex = element.selectedIndex;
	this.newSelectedIndex = this.oldSelectedIndex;
  }	
});

//----------------------------------------------------------------------------------
SelectEditor.prototype.open = function() {
  this._bind();
  this.state = Editor.OPENED;
};

//----------------------------------------------------------------------------------
SelectEditor.prototype.close = function() {
  this._unBind();
  this.state = Editor.CLOSED;
};

//----------------------------------------------------------------------------------
SelectEditor.prototype.isDirty = function() {
  return this.oldSelectedIndex != this.newSelectedIndex;	
};

//----------------------------------------------------------------------------------
SelectEditor.prototype.flush = function() {
  this.object[this.propertyName] = this.element.options[this.newSelectedIndex].getAttribute('value');
  this.oldSelectedIndex = this.newSelectedIndex;
};

//----------------------------------------------------------------------------------
SelectEditor.prototype.reset = function() {
  this.newSelectedIndex = this.oldSelectedIndex;
  this.element.selectedIndex = this.oldSelectedIndex;
};

//----------------------------------------------------------------------------------
SelectEditor.prototype._bind = function() {
  this.extElement = Ext.get(this.element);
  this.extElement.on('change', this._changeHandler, this);  
};

//----------------------------------------------------------------------------------
SelectEditor.prototype._unBind = function() {
  if (! this.extElement) return;
  this.extElement.un('change', this._changeHandler);  
  this.extElement = null;
};

//----------------------------------------------------------------------------------
SelectEditor.prototype._changeHandler = function(event, target, options) {
  var ev = null;  
  this.newSelectedIndex = target.selectedIndex;    

  if (this.newSelectedIndex == this.oldSelectedIndex) ev = {name: Events.RESTORED, data: {editor: this, selectedIndex: this.newSelectedIndex}};
  else ev = {name: Events.CHANGED, data: {editor : this, selectedIndex: this.newSelectedIndex}};
  this.fire(ev);
};

