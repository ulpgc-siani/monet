var SpaceEditor = Editor.extend({
  init : function(space, options) {
    this._super();
    this.space = space;
    this.options = options || {};
    this.options.type = 'composed';
    
    this.labelEditor = null;
    this.datawarehouseEditor = null;
    this.imageEditor = null;
    this.servicesEditor = null;
    
    this._init();
  }	
});

//------------------------------------------------------------------------------------------------
SpaceEditor.prototype.setServicesEditor = function(servicesEditor) {
  this.servicesEditor = servicesEditor;
  this.editors.push(this.servicesEditor);
};

//------------------------------------------------------------------------------------------------
SpaceEditor.prototype.setImage = function(filename, source) {
  this.imageEditor.setImage(filename, source);	
};

//------------------------------------------------------------------------------------------------
SpaceEditor.prototype._init = function(servicesEditor) {
  var sp = Space;
  var map = this.options.map;
  
  this.labelEditor = new TextEditor(map[sp.LABEL].element, this.space, map[sp.LABEL].propertyName);
  this.imageEditor = new ImageEditor(map[sp.LOGO].element, this.space, map[sp.LOGO].propertyName);
  this.datawarehouseEditor = new CheckboxEditor(map[sp.DATAWAREHOUSE].element, this.space, map[sp.DATAWAREHOUSE].propertyName);
  
  this.labelEditor.on(Events.CHANGED,  {notify: function(event) { this._changeEditorHandler(event); }}, this); 
  this.labelEditor.on(Events.RESTORED, {notify: function(event) { this._changeEditorHandler(event); }}, this);
  this.imageEditor.on(Events.CHANGED,  {notify: function(event) { this._changeEditorHandler(event); }}, this); 
  this.imageEditor.on(Events.RESTORED, {notify: function(event) { this._changeEditorHandler(event); }}, this);
  this.datawarehouseEditor.on(Events.CHANGED,  {notify: function(event) { this._changeEditorHandler(event); }}, this); 
  this.datawarehouseEditor.on(Events.RESTORED, {notify: function(event) { this._changeEditorHandler(event); }}, this);
    
  this.editors.push(this.labelEditor);
  this.editors.push(this.datawarehouseEditor);
  this.editors.push(this.imageEditor);  
};

//------------------------------------------------------------------------------------------------
SpaceEditor.prototype._changeEditorHandler = function(event) {
  var ev = {name : event.name, data : {editor : this, data : event.data.value}};
  this.fire(ev);
};