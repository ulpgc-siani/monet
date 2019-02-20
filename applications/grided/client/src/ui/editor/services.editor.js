var ServicesEditor = Editor.extend({
  init : function(services, options) {
	this._super();  
    this.services = services;
    this.options = options || {};
    this.options.type = 'composed';
    
    this._init();
  }	
});

//------------------------------------------------------------------------------------------------
ServicesEditor.prototype.setServices = function(services) {
  this.editors = [];
  this._setServices(services);  
};

//------------------------------------------------------------------------------------------------
ServicesEditor.prototype.addEditor = function(editor) {
  editor.on(Events.CHANGED,  {notify: function(event) { this._changeEditorHandler(event); }}, this); 
  editor.on(Events.RESTORED, {notify: function(event) { this._changeEditorHandler(event); }}, this);

  this.editors.push(editor);  	
};

//------------------------------------------------------------------------------------------------
ServicesEditor.prototype._init = function() {   
  this.editors = [];  
};

//------------------------------------------------------------------------------------------------
ServicesEditor.prototype._setServices = function(services) {
  this.services = services;
  
  var el = PublicationService;	
  var map = this.options.map;  
  
  this.services.each(function(service, index) {
    var editor = new CheckboxEditor(map[el.PUBLISHED].element, service, map[el.PUBLISHED].propertyName);
    editor.on(Events.CHANGED,  {notify: function(event) { this._changeEditorHandler(event); }}, this); 
    editor.on(Events.RESTORED, {notify: function(event) { this._changeEditorHandler(event); }}, this);
    this.editors.push(editor);
  }, this);        	
};

//------------------------------------------------------------------------------------------------
ServicesEditor.prototype._changeEditorHandler = function(event) {
  var ev = {name : event.name, data : {editor : this, data : event.data.value}};
  this.fire(ev);  
};