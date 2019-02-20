var ImageEditor = Editor.extend({	
  init : function(element, object, propertyName) {    	  
	this._super();
	this.element = element;
	this.object = object;
	this.propertyName = propertyName;
	
	this.imageName = "";
	this.oldSource = element.getAttribute('src');
	this.newSource = this.oldSource;
  }	
});

//----------------------------------------------------------------------------------
ImageEditor.prototype.open = function() {
  this._bind();
  this.state = Editor.OPENED;
};

//----------------------------------------------------------------------------------
ImageEditor.prototype.close = function() {
  this._unBind();
  this.state = Editor.CLOSED;
};

//----------------------------------------------------------------------------------
ImageEditor.prototype.setImage = function(filename, source) {
  this.newSource = source;
  this.element.src = source;
  this.imageName = filename;
  this._changeHandler(source);
};

//----------------------------------------------------------------------------------
ImageEditor.prototype.isDirty = function() {
  return this.oldSource != this.newSource;	
};

//----------------------------------------------------------------------------------
ImageEditor.prototype.flush = function() {
  this.object[this.propertyName] = this.imageName;
  this.oldSource = this.newSource;
};

//----------------------------------------------------------------------------------
ImageEditor.prototype.reset = function() {
  this.newSource = this.oldSource;
  this.element.setAttribute('src', this.oldSource);
};

//----------------------------------------------------------------------------------
ImageEditor.prototype._bind = function() {
  this.extElement = Ext.get(this.element);   
};

//----------------------------------------------------------------------------------
ImageEditor.prototype._unBind = function() {
  if (! this.extElement) return;
  this.extElement = null;
};

//----------------------------------------------------------------------------------
ImageEditor.prototype._changeHandler = function(newSource) {
  var ev = null;
  this.newSource = newSource;
  
  if (this.newSource == this.oldSource) ev = {name: Events.RESTORED, data: {editor: this, value: this.newSource}};
  else ev = {name: Events.CHANGED, data: {editor: this, value: this.newSource}};
  this.fire(ev);
};

