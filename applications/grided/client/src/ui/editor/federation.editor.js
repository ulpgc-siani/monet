var FederationEditor = Editor.extend({
  init : function(federation, userAuthenticationEditor, options) {
	this._super();  
    this.federation = federation;
    this.options = options || {};
    this.options.type = 'composed';
    this.userAuthenticationEditor = userAuthenticationEditor;    
        
    this._init();
  }	
});

//------------------------------------------------------------------------------------------------
FederationEditor.prototype.open = function(federation) {
  this.federation = federation;
  for (var i=0; i < this.editors.length; i++) {
    var editor = this.editors[i];
	editor.open();
  }  
  this.state = Editor.OPENED;
};

//------------------------------------------------------------------------------------------------
FederationEditor.prototype.close = function(server) {
  this.server = null;
  for (var i=0; i < this.editors.length; i++) {
    var editor = this.editors[i];
	editor.close();
  }  
  this.state = Editor.CLOSED;
};

//------------------------------------------------------------------------------------------------
FederationEditor.prototype.setImage = function(filename, source) {  
  this.imageEditor.setImage(filename, source);  	
};

//------------------------------------------------------------------------------------------------
FederationEditor.prototype._init = function() {
  var fe = Federation;	
  var map = this.options.map;  

  //this.nameEditor                      = new TextEditor(map[fe.NAME].element, this.federation, map[fe.NAME].propertyName);
  this.labelEditor                     = new TextEditor(map[fe.LABEL].element, this.federation, map[fe.LABEL].propertyName);
  this.imageEditor                     = new ImageEditor(map[fe.LOGO].element, this.federation, map[fe.LOGO].propertyName);
  this.certificateAuthenticationEditor = new CheckboxEditor(map[fe.CERTIFICATE_AUTH].element, this.federation, map[fe.CERTIFICATE_AUTH].propertyName);
  this.openIDAuthenticationEditor      = new CheckboxEditor(map[fe.OPENID_AUTH].element, this.federation, map[fe.OPENID_AUTH].propertyName);
  
  this.labelEditor.on(Events.CHANGED, {notify : function(event) { this._changeEditorHandler(event); }}, this);
  this.labelEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event); }}, this);
  this.imageEditor.on(Events.CHANGED, {notify : function(event) { this._changeEditorHandler(event); }}, this);
  this.imageEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event); }}, this);  
  this.certificateAuthenticationEditor.on(Events.CHANGED, {notify : function(event) { this._changeEditorHandler(event); }}, this);
  this.certificateAuthenticationEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event); }}, this);    
  this.openIDAuthenticationEditor.on(Events.CHANGED, {notify : function(event) { this._changeEditorHandler(event); }}, this);
  this.openIDAuthenticationEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event); }}, this);
  this.userAuthenticationEditor.on(Events.CHANGED, {notify : function(event) { this._changeEditorHandler(event); }}, this);
  this.userAuthenticationEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event); }}, this);  
  
  //this.editors.push(this.nameEditor);
  this.editors.push(this.labelEditor);
  this.editors.push(this.imageEditor);
  this.editors.push(this.userAuthenticationEditor);
  this.editors.push(this.certificateAuthenticationEditor);
  this.editors.push(this.openIDAuthenticationEditor);    
};

//------------------------------------------------------------------------------------------------
FederationEditor.prototype._changeEditorHandler = function(event) {
  var ev = {name : event.name, data : {editor: this}};
  this.fire(ev);
};

//------------------------------------------------------------------------------------------------
//
//  USER AUTHENTICATION EDITOR
//------------------------------------------------------------------------------------------------

var UserAuthenticationEditor = Editor.extend({
  init : function(federation, sourceEditor, connectionEditor, options) {
	this._super();  
    this.federation = federation;
    this.options = options || {};
    this.options.type = 'composed';
  
    this.checkboxEditor = null;
    this.sourceEditor = sourceEditor;
    this.connectionEditor = connectionEditor;
	        
    this._init();
  }	
});

//------------------------------------------------------------------------------------------------
UserAuthenticationEditor.prototype.setConnectionEditor = function(connectionEditor) {
  this.editors = [];
  this.connectionEditor.un(Events.CHANGED);
  this.connectionEditor.un(Events.RESTORED);
  this.connectionEditor = connectionEditor;
  
  this.connectionEditor.on(Events.CHANGED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.connectionEditor.on(Events.RESTORED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  
  this.editors.push(this.sourceEditor);
  this.editors.push(connectionEditor);
};

//------------------------------------------------------------------------------------------------
UserAuthenticationEditor.prototype._init = function() {
  var fe = Federation;	
  var map = this.options.map;
  
  this.checkboxEditor = new CheckboxEditor(map[fe.USER_AUTH].element, this.federation, map[fe.USER_AUTH].propertyName); 
  
  this.checkboxEditor.on(Events.CHANGED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.checkboxEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.sourceEditor.on(Events.CHANGED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.sourceEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.connectionEditor.on(Events.CHANGED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.connectionEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event);}}, this);  
  
  this.editors.push(this.checkboxEditor);    
  this.editors.push(this.sourceEditor);
  this.editors.push(this.connectionEditor);
};

//------------------------------------------------------------------------------------------------
UserAuthenticationEditor.prototype._changeEditorHandler = function(event) {
  var ev = {name : event.name, data : {editor: this}};
  this.fire(ev);
};

//------------------------------------------------------------------------------------------------
//
//  DATABASE CONNECTION EDITOR
//------------------------------------------------------------------------------------------------

var DatabaseConnectionEditor = Editor.extend({
  init : function(connection, options) {
	this._super();  
    this.connection = connection;
    this.options = options || {};
    this.options.type = 'composed';
	        
    this._init();
  }	
});

//------------------------------------------------------------------------------------------------
DatabaseConnectionEditor.prototype._init = function() {  
  var map = this.options.map;
  var con = DatabaseConnection;
  var config = DatabaseConnectionConfig;
  
  this.urlEditor      = new TextEditor(map[con.URL].element, this.connection, map[con.URL].propertyName);
  this.userEditor     = new TextEditor(map[con.USER].element, this.connection, map[con.USER].propertyName);
  this.passwordEditor = new TextEditor(map[con.PASSWORD].element, this.connection, map[con.PASSWORD].propertyName);
  this.engineEditor   = new SelectEditor(map[config.DATABASE_TYPE].element, this.connection.config, map[config.DATABASE_TYPE].propertyName);
  
  this.urlEditor.on(Events.CHANGED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.urlEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.userEditor.on(Events.CHANGED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.userEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.passwordEditor.on(Events.CHANGED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.passwordEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.engineEditor.on(Events.CHANGED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.engineEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event);}}, this);  
  
  this.editors.push(this.urlEditor);
  this.editors.push(this.userEditor);  
  this.editors.push(this.passwordEditor);  
  this.editors.push(this.engineEditor);    
};

//------------------------------------------------------------------------------------------------
DatabaseConnectionEditor.prototype._changeEditorHandler = function(event) {
  var ev = {name : event.name, data : {editor: this}};
  this.fire(ev);
};

//------------------------------------------------------------------------------------------------
//
//  LDAP CONNECTION EDITOR
//------------------------------------------------------------------------------------------------

var LDAPConnectionEditor = Editor.extend({
  init : function(connection, options) {
	this._super();  
    this.connection = connection;
    this.options = options || {};
    this.options.type = 'composed';
	        
    this._init();
  }	
});

//------------------------------------------------------------------------------------------------
LDAPConnectionEditor.prototype._init = function() {  
  var map = this.options.map;
  var con = LDAPConnection;
  var config = LDAPConnectionConfig;
    
  this.urlEditor      = new TextEditor(map[con.URL].element, this.connection, map[con.URL].propertyName);
  this.userEditor     = new TextEditor(map[con.USER].element, this.connection, map[con.USER].propertyName);
  this.passwordEditor = new TextEditor(map[con.PASSWORD].element, this.connection, map[con.PASSWORD].propertyName);

  this.schemaEditor     = new TextEditor(map[config.SCHEMA].element, this.connection.config, map[config.SCHEMA].propertyName);  
  this.cnFieldEditor    = new TextEditor(map[config.CN_FIELD].element, this.connection.config, map[config.CN_FIELD].propertyName);
  this.uiFieldEditor   = new TextEditor(map[config.UID_FIELD].element, this.connection.config, map[config.UID_FIELD].propertyName);
  this.emailFieldEditor = new TextEditor(map[config.EMAIL_FIELD].element, this.connection.config, map[config.EMAIL_FIELD].propertyName);
  this.langFieldEditor  = new TextEditor(map[config.LANG_FIELD].element, this.connection.config, map[config.LANG_FIELD].propertyName);
    
  this.urlEditor.on(Events.CHANGED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.urlEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.userEditor.on(Events.CHANGED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.userEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.passwordEditor.on(Events.CHANGED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.passwordEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.schemaEditor.on(Events.CHANGED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.schemaEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.cnFieldEditor.on(Events.CHANGED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.cnFieldEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.uiFieldEditor.on(Events.CHANGED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.uiFieldEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.emailFieldEditor.on(Events.CHANGED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.emailFieldEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.langFieldEditor.on(Events.CHANGED , {notify : function(event) { this._changeEditorHandler(event);}}, this);
  this.langFieldEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event);}}, this);
  
  this.editors.push(this.urlEditor);
  this.editors.push(this.userEditor);
  this.editors.push(this.passwordEditor);

  this.editors.push(this.schemaEditor);
  this.editors.push(this.cnFieldEditor);
  this.editors.push(this.uiFieldEditor);
  this.editors.push(this.emailFieldEditor);
  this.editors.push(this.langFieldEditor);
  
};

//------------------------------------------------------------------------------------------------
LDAPConnectionEditor.prototype._changeEditorHandler = function(event) {
  var ev = {name : event.name, data : {editor: this}};
  this.fire(ev);
};