var ServerActivity = Activity.extend({
  init : function() {
	this.view = this._createView();
	this.view.setPresenter(this);
	this.service = GridedService;	
	this.server = null; 	  
  }
});

//------------------------------------------------------------------------------------
ServerActivity.prototype.start = function(data) {
  if (!this.server || this.server.get(Server.ID) != data.id) this._clearModel();	
	
  if (!this.view) this.view = this._createView();
    
  if (this.server) this._showView(this.server);
  if (!this.server) this.service.loadServer(data.id, {
	 context: this,
	 success: function(server) {
	   this.server = server;
	   this.view.setServer(this.server);	
	   this._showView(this.server);
	   
	   this._loadSpaces(server.id);	   
	 },
	 failure : function(ex) {
		alert("Error loading Server"); 
	 }
  });
};

//------------------------------------------------------------------------------------
ServerActivity.prototype.stop = function() {
  var editor = this.view.getEditor();
  if (editor) editor.close();
  this._unlink(this.server);	
         
  this.view.hide();  
};

//------------------------------------------------------------------------------------
ServerActivity.prototype.canStop = function(callback) {
 var editor = this.view.getEditor();
 if (! editor.isDirty()) { callback.success.call(callback.context, {}); return;}; 
	 
 var continueCallback = {
   context: this,
   success : function() { callback.success.call(callback.context, {}); },
   failure : function() { alert("Error saving server"); }     
 };	 
 
 var saveCallback = {
   context: this,
   save: function() { this._saveServer(continueCallback); },
   cancel : function() { editor.reset(); continueCallback.success.call(continueCallback.context, null); }   
 };
 
 this._askForSaveChanges(saveCallback); 
};

//------------------------------------------------------------------------------------
ServerActivity.prototype.loadServerState = function(id, callback) {
  this.service.loadServerState(id, {
    context : this,
    success : function(serverState) {
      this.server.set(Server.SERVER_STATE, serverState);
    },
    failure : function(ex) {
      throw ex;
    }
  });
};

//------------------------------------------------------------------------------------
ServerActivity.prototype.notify = function(event) {
  if (event.propertyName == Server.SERVER_STATE) {
    var server = event.data.model;    
    this.view.setState(server.get(Server.SERVER_STATE));
  }
};

//------------------------------------------------------------------------------------
ServerActivity.prototype._showView = function(server) {
  this.view.getEditor().open(server);
  this._link(server);
  this.view.show();	
};

//------------------------------------------------------------------------------------
ServerActivity.prototype.clickBackButton = function() {
  this._goBack();	
};

//------------------------------------------------------------------------------------
ServerActivity.prototype.clickSaveButton = function() {
  var continueCallback = {
	context: this,
	success : function() { console.log("Server saved");  },
	failure : function() { alert("Error saving server"); }
  };	
     
  this._saveServer(continueCallback);  	
};

//------------------------------------------------------------------------------------
ServerActivity.prototype.clickDiscardButton = function() {
  var editor = this.view.getEditor();
  editor.reset();
  this._goBack();  
};

//------------------------------------------------------------------------------------
ServerActivity.prototype.openFederation = function(id) {
  var event = {name: Events.OPEN_FEDERATION, token: new FederationPlace(id).toString(), data: {id: id}};
  EventBus.fire(event);	
};

//------------------------------------------------------------------------------------
ServerActivity.prototype.openSpace = function(id) {
  var event = {name: Events.OPEN_SPACE, token: new SpacePlace(id).toString(), data: {id: id}};
  EventBus.fire(event);		
};

//------------------------------------------------------------------------------------
ServerActivity.prototype._loadSpaces = function(id) {
  this.service.loadServerSpaces(id, {
    context: this,
    success: function(spaces) {
      this.view.setSpaces(spaces);	
    },
    failure : function(ex) { throw ex; } 	
  });	
};

//------------------------------------------------------------------------------------
ServerActivity.prototype._saveServer = function(callback) { 	
  var editor = this.view.getEditor();
  if (! editor.isDirty()) return;

  editor.flush();
      
  var server = this.server;
  this.service.saveServer(this.server, {
    context : this,
    success : function() {      
      var event = {name: Events.SERVER_SAVED, data:{server : server} };
      EventBus.fire(event);	 
      callback.success.call(callback.context, null);
      
      NotificationManager.showNotification(Lang.Notifications.SERVER_SAVED, 1000);
    },
    failure : function() {
      callback.failure.call(callback.context, null);
      NotificationManager.showNotification(Lang.Notifications.SAVE_SERVER_ERROR, 1000);
    }
  });
};

//------------------------------------------------------------------------------------
ServerActivity.prototype._link = function(model) {
  model.on(Events.CHANGED, this, this);  
};

//------------------------------------------------------------------------------------
ServerActivity.prototype._unlink = function(model) {
  if (!model) return;  
  model.un(Events.CHANGED, this);
};

//------------------------------------------------------------------------------------
ServerActivity.prototype._goBack = function() {
  var event = {name : Events.OPEN_SERVERS, token : new ServersPlace().toString(), data: {}};
  EventBus.fire(event);
};

//------------------------------------------------------------------------------------
ServerActivity.prototype._createView = function() {
  var view = new ServerView(Ids.Elements.SERVER_VIEW, AppTemplate.ServerView);  
  return view;
};

//------------------------------------------------------------------------------------
ServerActivity.prototype._askForSaveChanges = function(callback) {    
  var saveHandler = function() { 
    this.view.closeSaveDialog();
    callback.save.call(callback.context, null);	  
  };   

  var discardHandler  = function() { 
    this.view.closeSaveDialog();    
    callback.cancel.call(callback.context, null);    
  };
		    	    
  this.view.openSaveDialog(saveHandler, discardHandler, this);	
};

//------------------------------------------------------------------------------------
ServerActivity.prototype._clearModel = function() {
  this.server = null;  
  this.environments = null;
};
