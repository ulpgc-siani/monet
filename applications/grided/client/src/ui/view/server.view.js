var ServerView = View.extend({
  init : function(id) {
	this._super(id);
	this.html = translate(AppTemplate.ServerView, Lang.ServerView);     
    this.server = null;
    this.editor = null;
    this.saveDialog = null;
    
    this.serverInitialized = false;
    this.stateInitialized  = false;
  }
});

//-----------------------------------------------------------------------------------------------------------
ServerView.prototype.setPresenter = function(presenter) {
  this.presenter = presenter;
};

//-----------------------------------------------------------------------------------------------------------
ServerView.prototype.getEditor = function() {
  return this.editor;	
};

//-----------------------------------------------------------------------------------------------------------
ServerView.prototype.destroy = function() {
  this.extTabs.destroy();
};

//-----------------------------------------------------------------------------------------------------------
ServerView.prototype.hide = function() {
  this.pooler.stop();
  this.$id.hide();
};

//-----------------------------------------------------------------------------------------------------------
ServerView.prototype.setServer = function(server) {
  this.server = server;
  this.state = this.server.get(Server.SERVER_STATE);
    
  this._initServer();
  this._initState();  
};

//-----------------------------------------------------------------------------------------------------------
ServerView.prototype.setState = function(state) {
  this.state = state;
  
  if (! this.stateInitialized) this._initState();
  this._refreshState();
};

//-----------------------------------------------------------------------------------------------------------
ServerView.prototype.setSpaces = function(spaces) {
  this.spaces = spaces;
  this._refreshSpaces();
};

//-----------------------------------------------------------------------------------------------------------
ServerView.prototype.openSaveDialog = function(saveHandler, discardHandler, scope) {
  if (! this.saveDialog) {
    this.saveDialog = new SaveChangesDialog(this.extSaveDialog.dom);
    this.saveDialog.addButton(Lang.Buttons.save, saveHandler, scope);
    this.saveDialog.addButton(Lang.Buttons.discard, discardHandler, scope);
  }
  this.saveDialog.open();
};

//-----------------------------------------------------------------------------------------------------------
ServerView.prototype.closeSaveDialog = function() {
  if (this.saveDialog) this.saveDialog.close();	
};

//-----------------------------------------------------------------------------------------------------------
ServerView.prototype._refreshServer = function() {
  this.extName.dom.value = this.server.name;
  this.extIP.dom.value   = this.server.ip;	
  this._refreshTabs();
};

//-----------------------------------------------------------------------------------------------------------
ServerView.prototype._refreshState = function() {  		
  this.logsTab.setContent(this.state.log);  
  if (this.server.enabled) this._showTabs();
};

//-----------------------------------------------------------------------------------------------------------
ServerView.prototype._refreshSpaces = function() {  
  var section = this.merge(AppTemplate.SpacesSection, {spaces : this.spaces.toArray()});  
  section = translate(section, Lang.ServerView);
  this.extSpacesSection.dom.innerHTML = section;  	
};

//-----------------------------------------------------------------------------------------------------------
ServerView.prototype._backButtonHandler = function(event, target, options) {  
  event.preventDefault();
  event.stopPropagation();
  this.presenter.clickBackButton();
};

//-----------------------------------------------------------------------------------------------------------
ServerView.prototype._saveButtonHandler = function(event, target, options) {
  event.preventDefault();
  event.stopPropagation();
  this.presenter.clickSaveButton();	
};

//-----------------------------------------------------------------------------------------------------------
ServerView.prototype._discardButtonHandler = function(event, target, options) {
  event.preventDefault();
  event.stopPropagation();
  this.presenter.clickDiscardButton();	
};

//-----------------------------------------------------------------------------------------------------------
ServerView.prototype._initServer = function() {
  this.extParent = this.extParent || Ext.get(this.id);       
  var content = this.merge(this.html, {server: this.server, federations: this.server.get(Server.FEDERATIONS).toArray()}); 	
  this.extParent.dom.innerHTML = content;
      	
  this.backButton    = Ext.get(this.extParent.query('a[name="back"]').first());
  this.saveButton    = Ext.get(this.extParent.query('a[name="save"]').first());
  this.discardButton = Ext.get(this.extParent.query('a[name="discard"]').first());
  
  this.extName       = Ext.get(this.extParent.query('input[name="name"]').first());
  this.extIP         = Ext.get(this.extParent.query('input[name="ip"]').first());
  
  this.extFederationsSection = Ext.get(this.extParent.query('div[name="federations"]').first());
  this.extSpacesSection = Ext.get(this.extParent.query('div[name="spaces"]').first());
  
  this.extTabs      = new Ext.TabPanel("tabs");
  this.stateTab     = this.extTabs.addTab(Ids.Elements.STATE_TAB, Lang.ServerView.state);  
  this.logsTab      = this.extTabs.addTab(Ids.Elements.LOGS_TAB, Lang.ServerView.log);
  this.extMessage   = this.extParent.select('.message').first();
      
  this.extSaveDialog = Ext.get(Ids.Elements.DIALOG); 
	  
  this.editor = this._createEditor(this.server); 
  
  this.extTabs.on('tabchange', function(tabPanel) {
    var tab = tabPanel.getActiveTab();
    if (!tab) return;
    tab.activate();
  }, this);
    
  this.extFederationsSection.on('click', this._clickFederationsHandler, this);
  this.extSpacesSection.on('click', this._clickSpacesHandler, this);
    	  
  this.backButton.on('click', this._backButtonHandler, this);
  this.saveButton.on('click', this._saveButtonHandler, this);
  this.discardButton.on('click', this._discardButtonHandler, this);  
  
  $(this.saveButton.dom).addClassName('disabled');  
  
  this._refreshTabs();
    
  this.pooler = new Pooler({poolTime: 3000, url: Context.Config.Api + "?op=loadserverstate", params : {id : this.server.id}});
  this.pooler.start({
    context : this,
    success : function() { 
      this.presenter.loadServerState(this.server.id);
    }
  });
  
  this.serverInitialized = true;
};

//-----------------------------------------------------------------------------------------------------------
ServerView.prototype._initState = function() {
  var template = translate(AppTemplate.ServerState, Lang.ServerView);
  var html = this.merge(template, {state : this.server.get(Server.SERVER_STATE)});
  this.stateTab.setContent(html);
    
  this.logsTab.setContent(this.state.log);
  if (this.server.enabled) this._showTabs();
    
  this.stateInitialized = true;
};

//-----------------------------------------------------------------------------------------------------------
ServerView.prototype._createEditor = function(server) {
  var editor = new ServerEditor({type: 'composed'});
  var nameElement = this.extParent.query('input[name="name"]').first();
  var ipElement   = this.extParent.query('input[name="ip"]').first();
  
  editor.setServer(server);
  editor.addName(nameElement, Server.NAME);
  editor.addIp(ipElement, Server.IP);  
  editor.on(Events.CHANGED, {notify : function(event) { this._changeEditorHandler(event); }}, this);
  editor.on(Events.RESTORED, {notify : function(event) { this._changeEditorHandler(event); }}, this);
  
  return editor;    
};

//-----------------------------------------------------------------------------------------------------------
ServerView.prototype._refreshTabs = function() {
  if (this.server.enabled) {
	  this._showTabs();
    this.stateTab.activate();
    this.stateTab.show();
  }
  else { 
	this.extTabs.getActiveTab().hide(); //bug in ext
	this._showMessage();
  }     	
};

//-----------------------------------------------------------------------------------------------------------
ServerView.prototype._showTabs = function() {
  $(this.extTabs.el).show();
  this.extTabs.endUpdate();
  this.extMessage.dom.hide();
};

//-----------------------------------------------------------------------------------------------------------
ServerView.prototype._showMessage = function() {
  $(this.extTabs.el).hide();
  this.extMessage.dom.show();	
};

//-----------------------------------------------------------------------------------------------------------
ServerView.prototype._clickFederationsHandler = function(event, target, options) {
  if (target.nodeName.toLowerCase() != "a") return;
  
  event.preventDefault();
  event.stopPropagation();
  this.presenter.openFederation(target.getAttribute('code'));
};

//-----------------------------------------------------------------------------------------------------------
ServerView.prototype._clickSpacesHandler = function(event, target, options) {
  if (target.nodeName.toLowerCase() != "a") return;
 
  event.preventDefault();
  event.stopPropagation();
  this.presenter.openSpace(target.getAttribute('code'));  	
};

//-----------------------------------------------------------------------------------------------------------
ServerView.prototype._changeEditorHandler = function(event) {
  if (this.editor.isDirty()) $(this.saveButton.dom).removeClassName('disabled');
  else $(this.saveButton.dom).addClassName('disabled');
};
