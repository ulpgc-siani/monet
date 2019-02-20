var Editor = EventsSource.extend({
  init : function() {    
	this._super();
    this.state = Editor.CLOSED;	
    this.editors = [];
  }	
});

Editor.CLOSED = 0;
Editor.OPENED = 1;
Editor.CHANGED = 2;

//------------------------------------------------------------
Editor.prototype.open = function() {	  
  if (this.options.type == 'single') { this.state = Editor.OPENED; return; }
	  
  for (var i=0; i < this.editors.length; i++) {
    var editor = this.editors[i];
	editor.open();
  }
	  
  this.state = Editor.OPENED; 
};

//------------------------------------------------------------
Editor.prototype.close = function() {
  if (this.options.type == 'single') { this.state = Editor.STOPPED; return; }
  
  for (var i=0; i < this.editors.length; i++) {
    var editor = this.editors[i];
	editor.close();
  }
  
  this.state = Editor.CLOSED;
};

//------------------------------------------------------------
Editor.prototype.isClosed = function() {
  return this.state === Editor.CLOSED;  	
};

//------------------------------------------------------------
Editor.prototype.isDirty = function() {
  if (this.options.type == 'single') { throw new Error('isDirty method has to be overrided in editor'); return; }
  
  for (var i=0; i < this.editors.length; i++) {
    var editor = this.editors[i];
    if (editor.isDirty()) return true;
  }
  return false;
};

//------------------------------------------------------------
Editor.prototype.flush = function() {
  if (this.options.type == 'single') { throw new Error('flush method has to be overrided in editor'); return; }

  for (var i=0; i < this.editors.length; i++) {
    var editor = this.editors[i];
    if (editor.isDirty()) editor.flush();
  }
};

//------------------------------------------------------------
Editor.prototype.reset = function() {
  if (this.options.type == 'single')  { throw new Error('reset method has to be overrided in editor'); return; }
  
  for (var i=0; i < this.editors.length; i++) {
    var editor = this.editors[i];
	editor.reset();
  }	   
};

//------------------------------------------------------------
Editor.prototype.showError = function(error) {
  if (this.options.type == 'single')  { throw new Error('reset method has to be overrided in editor'); return; }    
  
  var editor = this.getEditor(error.propertyName);
  if (editor) editor.showError(error);    
};

//------------------------------------------------------------
Editor.prototype.getEditor = function(propertyName) {
  if (this.options.type == 'single')  return null;
  
  for (var i=0, l = this.editors.length; i < l; i++) {
	  var editor = this.editors[i];
	  if (editor.propertyName === propertyName) return editor;	
  }    
  return null;
};