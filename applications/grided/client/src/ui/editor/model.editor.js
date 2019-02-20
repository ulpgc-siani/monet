var ModelEditor = Editor.extend({
  init : function(model, options) {
    this._super();
    this.model = model;
    this.options = options || {};
    this.options.type = 'composed';
    
    this.labelEditor = null;
    this.imageEditor = null;
    
    this.versionEditors = {};    
    this._init();
  }
});

//------------------------------------------------------------------------------------------------
ModelEditor.prototype.setImage = function(filename, source) {
  this.imageEditor.setImage(filename, source);  
};

//------------------------------------------------------------------------------------------------
ModelEditor.prototype.hasVersionEditor = function(editorId) {
  var editor = this._findVersionEditor(editorId);
  return editor != null;
};

//------------------------------------------------------------------------------------------------
ModelEditor.prototype.getVersionEditor = function(editorId) {
  return this._findVersionEditor(editorId);
};

//------------------------------------------------------------------------------------------------
ModelEditor.prototype.addVersionEditor = function(editor) {    
  this.editors.push(editor);
};

//------------------------------------------------------------------------------------------------
ModelEditor.prototype.removeVersionEditor = function(editorId) {
  var editor = this._findVersionEditor(editorId);
  if (editor = null) return;
  
  editor.close();
  this.editors.splice(i, 1);     
};

//------------------------------------------------------------------------------------------------
ModelEditor.prototype._init = function() {
  var m = BusinessModel;
  var map = this.options.map;
  
  this.labelEditor = new TextEditor(map[m.LABEL].element, this.model, map[m.LABEL].propertyName);
  this.imageEditor = new ImageEditor(map[m.LOGO].element, this.model, map[m.LOGO].propertyName);
         
  this.editors.push(this.labelEditor);
  this.editors.push(this.imageEditor);  
};

//------------------------------------------------------------------------------------------------
ModelEditor.prototype._findVersionEditor = function(editorId) {
  for (var i=0, l = this.editors.length; i < l; i++) {
    var editor = this.editors[i];
    if (editor.id === editorId) {
      return editor;
    }
  }
  return null;
};

