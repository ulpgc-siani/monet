var ServerEditor = Editor.extend({
  init : function( options) {
	this._super();  
    this.server = null;
    this.options = options || {};
    this.options.type = 'composed';
    
    this.nameEditor = null;
    this.ipEditor   = null;
  }	
});

//------------------------------------------------------------
ServerEditor.prototype.open = function(server) {
  this.server = server;
  for (var i=0; i < this.editors.length; i++) {
    var editor = this.editors[i];
	  editor.open();
  }  
  this.state = Editor.OPENED;
};

//------------------------------------------------------------
ServerEditor.prototype.close = function(server) {
  this.server = null;
  for (var i=0; i < this.editors.length; i++) {
    var editor = this.editors[i];
	editor.close();
  }  
  this.state = Editor.CLOSED;
};

//------------------------------------------------------------
ServerEditor.prototype.setServer = function(server) {
  this.server = server;
};

//------------------------------------------------------------
ServerEditor.prototype.addName = function(elt, propertyName) {
  this.nameEditor = new TextEditor(elt, this.server, propertyName);  
  this.nameEditor.on(Events.CHANGED,  {notify: function(event) { this._changeEditorHandler(event); }}, this); 
  this.nameEditor.on(Events.RESTORED, {notify: function(event) { this._changeEditorHandler(event); }}, this);
  this.editors.push(this.nameEditor);
};

//------------------------------------------------------------
ServerEditor.prototype.addIp = function(elt, propertyName) {
  this.ipEditor = new TextEditor(elt, this.server, propertyName);
  this.ipEditor.on(Events.CHANGED,  {notify: function(event) { this._changeEditorHandler(event); }}, this); 
  this.ipEditor.on(Events.RESTORED, {notify: function(event) { this._changeEditorHandler(event); }}, this);
  this.editors.push(this.ipEditor);
};

//------------------------------------------------------------
ServerEditor.prototype._changeEditorHandler = function(event) {
  var ev = {name : event.name, data : {editor : this, data : event.data.value}};
  this.fire(ev);
};