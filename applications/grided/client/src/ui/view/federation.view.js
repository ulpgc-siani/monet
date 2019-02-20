var FederationView = View.extend({
  init : function(id) {
    this._super(id);
    this.html = translate(AppTemplate.FederationView, Lang.Buttons);
    this.html = translate(AppTemplate.FederationView, Lang.FederationView);
    
    this.federation = null;
    this.presenter = null;
    this.editor = null;
    this.userAuthenticationEditor = null;
   
    this.saveDialog = null;
  }	
});

//-------------------------------------------------------------------------
FederationView.prototype.setPresenter = function(presenter) {
  this.presenter = presenter;
};

//-------------------------------------------------------------------------
FederationView.prototype.getEditor = function() {
  return this.editor;	
};

//-------------------------------------------------------------------------
FederationView.prototype.setFederation = function(federation) {
  this.federation = federation;
  this._init();
};

//-------------------------------------------------------------------------
FederationView.prototype.hide = function() {
  this.hideUploadImageDialog();
  this.$id.hide();
};

//-------------------------------------------------------------------------
FederationView.prototype.refreshState = function() {
  if (this.federation.state.running) 
    this.extStartStopButton.dom.innerHTML = Lang.FederationView.stop;
  else 
    this.extStartStopButton.dom.innerHTML = Lang.FederationView.start;
  
  var $runningTime = $(this.extRunningTime.dom);  
  
  if (this.federation.state.running) {
    $runningTime.innerHTML = "Running from " + new Date(this.federation.state.time).format('d M Y - G:i');
    $runningTime.show();
  }
  else {
    $runningTime.innerHTML = "";
    $runningTime.hide();  
  }
};

//-------------------------------------------------------------------------
FederationView.prototype.editConnection = function(connection) {
  var editor = (connection.type == ConnectionTypes.LDAP)? this._createLDAPConnectionEditor(connection) : this._createDatabaseConnectionEditor(connection);
  this.userAuthenticationEditor.setConnectionEditor(editor);   	
  this._switchToConnectionEditor(connection.type);
};

//-------------------------------------------------------------------------
FederationView.prototype.showUploadImageDialog = function() {
  if (! this.uploadDialog) {
    this.uploadDialog = new UploadDialog(this.extUploadImageDialog.dom);
    this.uploadDialog.on(UploadDialog.CLICK_CLOSE, {
      notify : function(event) {
    	this._showLogo();  
    	this.hideUploadImageDialog();  
      }
    }, this);  
  	      	
	this.uploadDialog.on(UploadDialog.CLICK_SUBMIT, {
		notify : function(event) {
		  var form = event.data.form;
		  var callback = {scope: this, fn: function(filename, source) {
		    this.editor.setImage(filename, source);  
		  }};		  
		  this.presenter.uploadImage(form, callback);
		}
    }, this);
  }
  
  this._hideImage();  
  this.uploadDialog.open();
};

//-------------------------------------------------------------------------
FederationView.prototype.hideUploadImageDialog = function() {
  if (this.uploadDialog) this.uploadDialog.close();
  this._showLogo();      
};

//-------------------------------------------------------------------------
FederationView.prototype._showLogo = function() {
  $(this.extChangeLogo.dom).show();  
  $(this.extLogo.dom).show();	
};

//-------------------------------------------------------------------------
FederationView.prototype._hideImage = function() {
  $(this.extChangeLogo.dom).hide();  
  $(this.extLogo.dom).hide();	
};

//-------------------------------------------------------------------------
FederationView.prototype._switchToConnectionEditor = function(type) {
  switch (type) {
    case ConnectionTypes.DATABASE:
      $(this.extDatabaseEditor.dom).show();
      $(this.extLDAPEditor.dom).hide();      
    break;
    case ConnectionTypes.LDAP:
      $(this.extLDAPEditor.dom).show();
      $(this.extDatabaseEditor.dom).hide();      
    break;
  }
};

//-------------------------------------------------------------------------
FederationView.prototype.openSaveDialog = function(saveHandler, discardHandler, scope) {
  if (! this.saveDialog) {
    this.saveDialog = new SaveChangesDialog(this.extSaveDialog.dom);
    this.saveDialog.addButton(Lang.Buttons.save, saveHandler, scope);
    this.saveDialog.addButton(Lang.Buttons.discard, discardHandler, scope);    
  }	  
  this.saveDialog.open();
};

//-------------------------------------------------------------------------
FederationView.prototype.closeSaveDialog = function() {
  if (this.saveDialog) this.saveDialog.close();  	
};

//-------------------------------------------------------------------------
FederationView.prototype._init = function() {
  this.extParent = this.extParent || Ext.get(this.id);
  var content = this.merge(this.html, {federation: this.federation, spaces : this.federation.get(Federation.SPACES).toArray()});
  this.extParent.dom.innerHTML = content;
  
  this.backButton     = Ext.get(this.extParent.query('a[name="back"]').first());
  this.saveButton     = Ext.get(this.extParent.query('a[name="save"]').first());
  this.discardButton  = Ext.get(this.extParent.query('a[name="discard"]').first());
  
  this.extUploadImageDialog   = Ext.get('upload-federation-image-dialog');
  this.extChangeLogo  = Ext.get(this.extParent.query('a[name="change-image"]').first());  
  this.extLogo        = Ext.get(this.extParent.query('img[name="image"]').first());
  
  this.extServerSection = Ext.get(this.extParent.query('div[name="servers"]').first());       
  this.extSpacesSection = Ext.get(this.extParent.query('div[name="spaces"]').first());  
  
  this.extLabel            = Ext.get(this.extParent.query('input[name="label"]').first());
  this.extName             = Ext.get(this.extParent.query('input[name="name"]').first());
  this.extUserCheck        = Ext.get(this.extParent.query('input[id="check-user-password"]').first());
  this.extCertificateCheck = Ext.get(this.extParent.query('input[id="check-certificate"]').first());
  this.extOpenIDCheck      = Ext.get(this.extParent.query('input[id="check-openid"]').first());
  
  this.extSource           = Ext.get(this.extParent.query('select[name="source"]').first());    
    
  this.extDatabaseEditor = Ext.get('database-editor');
  this.extDatabaseUrl      = Ext.get(this.extDatabaseEditor.query('input[name="url"]').first());
  this.extDatabaseUser     = Ext.get(this.extDatabaseEditor.query('input[name="user"]').first());
  this.extDatabasePassword = Ext.get(this.extDatabaseEditor.query('input[name="password"]').first());
  this.extDatabaseEngine   = Ext.get(this.extDatabaseEditor.query('select[name="engine"]').first());
  //this.extDatabaseEncrypt  = Ext.get(this.extDatabaseEditor.query('select[name="encrypt"]').first());
    
  this.extLDAPEditor = Ext.get('ldap-editor');
  this.extLDAPUrl        = Ext.get(this.extLDAPEditor.query('input[name="url"]').first());
  this.extLDAPSchema     = Ext.get(this.extLDAPEditor.query('input[name="schema"]').first());
  this.extLDAPUser       = Ext.get(this.extLDAPEditor.query('input[name="user"]').first());
  this.extLDAPPassword   = Ext.get(this.extLDAPEditor.query('input[name="password"]').first());
  
  this.extLDAPCNField    = Ext.get(this.extLDAPEditor.query('input[name="cn_field"]').first());
  this.extLDAPUIDField   = Ext.get(this.extLDAPEditor.query('input[name="uid_field"]').first());
  this.extLDAPEMailField = Ext.get(this.extLDAPEditor.query('input[name="email_field"]').first());
  this.extLDAPLangField  = Ext.get(this.extLDAPEditor.query('input[name="lang_field"]').first());  
  
  this.extSaveDialog = Ext.get(Ids.Elements.DIALOG); 
	  
  this.extServerSection.on('click', this._clickServerHandler, this);
  this.extSpacesSection.on('click', this._clickSpacesHandler, this);  
  
  switch (this.federation.connection.type) {
  
    case ConnectionTypes.DATABASE:
      this.extDatabaseUrl.dom.value = this.federation.connection.url;
      this.extDatabaseUser.dom.value = this.federation.connection.user;
      this.extDatabasePassword.dom.value = this.federation.connection.password;
      this.extDatabaseEngine.dom.selectedIndex = (this.federation.connection.config.databasetype == DatabaseTypes.MYSQL)? 0 : 1;      
    break;
    
    case ConnectionTypes.LDAP:
      this.extLDAPUrl.dom.value = this.federation.connection.url;
      this.extLDAPUser.dom.value = this.federation.connection.user;
      this.extLDAPSchema.dom.value = this.federation.connection.schema;
      this.extLDAPPassword.dom.value = this.federation.connection.password;    
      
      this.extLDAPCNField.dom = this.federation.connection.config.cn;
      this.extLDAPUIDField    = this.federation.connection.config.uid;
      this.extLDAPEMailField  = this.federation.connection.config.email;
      this.extLDAPLangField   = this.federation.connection.config.lang;
    break;        
  }      
  
  this.backButton.on('click', this._backButtonHandler, this);
  this.saveButton.on('click', this._saveButtonHandler, this);
  this.discardButton.on('click', this._discardButtonHandler, this);
  
  this.extChangeLogo.on('click' , this._changeLogoHandler, this);  
  this.extSource.on('change', this._changeConnectionTypeHandler, this);
  
  
  
  $(this.saveButton.dom).addClassName('disabled');
  $(this.extUploadImageDialog.dom).hide();
  
  this._initState();
  
  this.editor = this._createEditor(this.federation);  
};

//-----------------------------------------------------------------------------------------------------------
FederationView.prototype._clickServerHandler = function(event, target, options) {  	
  if (target.nodeName.toLowerCase() != 'a') return;

  event.preventDefault();
  event.stopPropagation();
  
  switch (target.getAttribute('name')) {
    case 'server': this.presenter.openServer(target.getAttribute('code')); break;
    case 'change': this.presenter.changeServer(); break;
  }
};

//-----------------------------------------------------------------------------------------------------------
FederationView.prototype._clickSpacesHandler = function(event, target, options) {  
  if (target.nodeName.toLowerCase() != 'a') return;
  
  event.preventDefault();
  event.stopPropagation();  
  this.presenter.openSpace(target.getAttribute('code'));    
};

//-----------------------------------------------------------------------------------------------------------
FederationView.prototype._backButtonHandler = function(event, target, options) {  
  event.preventDefault();
  event.stopPropagation();
  this.presenter.clickBackButton();
};

//-----------------------------------------------------------------------------------------------------------
FederationView.prototype._saveButtonHandler = function(event, target, options) {
  event.preventDefault();
  event.stopPropagation();
  this.presenter.clickSaveButton();	
};

//-----------------------------------------------------------------------------------------------------------
FederationView.prototype._discardButtonHandler = function(event, target, options) {
  event.preventDefault();
  event.stopPropagation();
  this.presenter.clickDiscardButton();	
};

//-----------------------------------------------------------------------------------------------------------
FederationView.prototype._changeLogoHandler = function(event, target, options) {
  event.preventDefault();
  event.stopPropagation();
  this.presenter.clickChangeLogo();	  	
};

//-----------------------------------------------------------------------------------------------------------
FederationView.prototype._changeConnectionTypeHandler = function(event, target, options) {
  var type = target.options[target.selectedIndex].getAttribute('value');
  if (type) this.presenter.changeConnectionType(type);
};

//-------------------------------------------------------------------------
FederationView.prototype._createEditor = function(federation) {  	
  this.userAuthenticationEditor = this._createUserAuthenticationEditor(federation);	
  var federationEditor = this._createFederationEditor(federation, this.userAuthenticationEditor);	
  federationEditor.on(Events.CHANGED, {notify : function(event) { this._changeEditorHandler(event); }}, this);
  federationEditor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event); }}, this);
  
  this._switchToConnectionEditor(federation.connection.type);
  
  return federationEditor;
};

//-------------------------------------------------------------------------
FederationView.prototype._initState = function() {
  this.extRunningTime     = this.extParent.select('.running_time').first();
  this.extStartStopButton = Ext.get(this.extParent.query('a[name="start_stop"]').first()); 
    
  this.extStartStopButton.on('click', function(event, target, options) {
    event.stopPropagation();
    event.preventDefault();
    
    if (this.federation.state.running) this.presenter.stopFederation();
    else this.presenter.startFederation();
  }, this);
   
  this.refreshState();
};

//-------------------------------------------------------------------------
FederationView.prototype._createFederationEditor = function(federation, userAuthenticationEditor) {
  var map = {};
//  map[Federation.NAME]             = {element: this.extName.dom,   propertyName: Federation.NAME};
  map[Federation.LABEL]            = {element: this.extLabel.dom,  propertyName: Federation.LABEL};
  map[Federation.LOGO]             = {element: this.extLogo.dom,  propertyName: Federation.LOGO};  
  map[Federation.USER_AUTH]        = {element: this.extUserCheck.dom,   propertyName: Federation.USER_AUTH};  
  map[Federation.CERTIFICATE_AUTH] = {element: this.extCertificateCheck.dom,   propertyName: Federation.CERTIFICATE_AUTH};
  map[Federation.OPENID_AUTH]      = {element: this.extOpenIDCheck.dom,   propertyName: Federation.OPENID_AUTH};
  	  
  var editor = new FederationEditor(federation, userAuthenticationEditor, {map: map});
  return editor;
};

//-------------------------------------------------------------------------
FederationView.prototype._createUserAuthenticationEditor = function(federation) {  
  var sourceEditor     = null;
  var connectionEditor = null;
  var editor = null;
  
  var map = {};
  map[Federation.USER_AUTH] = {element: this.extUserCheck.dom,   propertyName: Federation.USER_AUTH};  
  
  switch (federation.connection.type) {
    case ConnectionTypes.DATABASE:
      connectionEditor = this._createDatabaseConnectionEditor(federation.connection);	
    break;
    
    case ConnectionTypes.LDAP:
      connectionEditor = this._createLDAPConnectionEditor(federation.connection);	 
    break;
    case ConnectionTypes.MOCK:
    break;	 
  }
  
  sourceEditor = new SelectEditor(this.extSource.dom, federation, Federation.CONNECTION_TYPE);
  editor = new UserAuthenticationEditor(federation, sourceEditor, connectionEditor, {map: map});
  
  return editor;	
};

//-------------------------------------------------------------------------
FederationView.prototype._createDatabaseConnectionEditor = function(connection) {
  var map = {};
  map[DatabaseConnection.URL]      = {element: this.extDatabaseUrl.dom,   propertyName: DatabaseConnection.URL};
  map[DatabaseConnection.USER]     = {element: this.extDatabaseUser.dom,   propertyName: DatabaseConnection.USER};
  map[DatabaseConnection.PASSWORD] = {element: this.extDatabasePassword.dom, propertyName: DatabaseConnection.PASSWORD};
  map[DatabaseConnectionConfig.DATABASE_TYPE] = {element: this.extDatabaseEngine.dom, propertyName: DatabaseConnectionConfig.DATABASE_TYPE};
	    
  var editor = new DatabaseConnectionEditor(connection, {map: map});
  return editor;
};

//-------------------------------------------------------------------------
FederationView.prototype._createLDAPConnectionEditor = function(connection) {  
  var map = {};
  map[LDAPConnection.URL]          = {element: this.extLDAPUrl.dom,   propertyName: LDAPConnection.URL};
  map[LDAPConnection.USER]         = {element: this.extLDAPUser.dom,   propertyName: LDAPConnection.USER};
  map[LDAPConnection.PASSWORD]     = {element: this.extLDAPPassword.dom, propertyName: LDAPConnection.PASSWORD};
  
  map[LDAPConnectionConfig.SCHEMA] = {element: this.extLDAPSchema.dom, propertyName: LDAPConnectionConfig.SCHEMA};
  map[LDAPConnectionConfig.CN_FIELD]    = {element: this.extLDAPCNField.dom, propertyName: LDAPConnectionConfig.CN_FIELD};
  map[LDAPConnectionConfig.UID_FIELD]   = {element: this.extLDAPUIDField.dom, propertyName: LDAPConnectionConfig.UID_FIELD};
  map[LDAPConnectionConfig.EMAIL_FIELD] = {element: this.extLDAPEMailField.dom, propertyName: LDAPConnectionConfig.EMAIL_FIELD};
  map[LDAPConnectionConfig.LANG_FIELD]  = {element: this.extLDAPLangField.dom, propertyName: LDAPConnectionConfig.LANG_FIELD};
	  
  var editor = new LDAPConnectionEditor(connection, {map: map});
  return editor;
};

//-------------------------------------------------------------------------
FederationView.prototype._changeEditorHandler = function(event) {
  if (this.editor.isDirty()) $(this.saveButton.dom).removeClassName('disabled');
  else $(this.saveButton.dom).addClassName('disabled');  
};