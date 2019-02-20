var FederationActivity = Activity.extend({
  init : function() {
    this.view = this._createView();
    this.view.setPresenter(this);
    this.service = GridedService;
    this.federation = null;
  }	
});

//------------------------------------------------------------------------------------
FederationActivity.prototype.start = function(data) {
  if (!this.federation || this.federation.id != data.id) this._clearModel();	
	
  if (!this.view) this.view = this._createView();
  
  if (this.federation) this._showView(this.federation);
  if (!this.federation) this.service.loadFederation(data.id, {
	 context: this,
	 success: function(federation) {
	   this.federation = federation;
	   this.currentConnection = this.federation.connection;
	   this.view.setFederation(this.federation);
	   this._showView(this.federation);
	 },
	 failure : function(ex) {
		throw ex;		 
	 }
  });
};

//------------------------------------------------------------------------------------
FederationActivity.prototype.stop = function() {
  var editor = this.view.getEditor();
  if (editor) editor.close();
  this._unlink(this.federation);	
  this.view.hide();	
};

//------------------------------------------------------------------------------------
FederationActivity.prototype.canStop = function(callback) {
 var editor = this.view.getEditor();
 if (! editor.isDirty()) { callback.success.call(callback.context, {}); return; }; 
		 
 var continueCallback = {
   context: this,
   success : function() { callback.success.call(callback.context, {}); },
   failure : function() { alert("Error saving federation"); }     
 };	 
	 
 var saveCallback = {
   context: this,
   save: function() { this._saveFederation(continueCallback); },
   cancel : function() { editor.reset(); continueCallback.success.call(continueCallback.context, null); }   
 };
	 
 this._askForSaveChanges(saveCallback); 
  	  
};

//------------------------------------------------------------------------------------
FederationActivity.prototype.notify = function(event) {
  if (event.name == Events.CHANGED) {
    switch (event.propertyName) {
      case Federation.STATE :  
        this.view.refreshState();
      break;
    }       
  }    	  
};

//------------------------------------------------------------------------------------
FederationActivity.prototype.clickChangeLogo = function() {
  this.view.showUploadImageDialog();
};

//------------------------------------------------------------------------------------
FederationActivity.prototype.uploadImage = function(form, callback) {
  var params = {server_id: this.federation.server.id, federation_id: this.federation.id, model_type: ModelTypes.FEDERATION};
  
  this.service.uploadImage(form, params, {
    context: this,	  
    success: function(name, source) {           	
      this.view.hideUploadImageDialog();
      callback.fn.call(callback.scope, name, source);
    },
    failure : function() {
      this.view.hideUploadImageDialog();
    }
  });	
};

//------------------------------------------------------------------------------------
FederationActivity.prototype.openServer = function(id) {
  var event = {name: Events.OPEN_SERVER, token: new ServerPlace(id).toString(), data: {id: id}};
  EventBus.fire(event);	 
};

//--------------------------------------------------------------
FederationActivity.prototype.openSpace = function(id) {
  var event = {name: Events.OPEN_SPACE, token: new SpacePlace(id).toString(), data: {id: id}};
  EventBus.fire(event);	
};

//--------------------------------------------------------------
FederationActivity.prototype.clickBackButton = function() {
  this._goBack();
};

//--------------------------------------------------------------
FederationActivity.prototype.clickDiscardButton = function() {
  var editor = this.view.getEditor();
  editor.reset();
  this._goBack();
};

//--------------------------------------------------------------
FederationActivity.prototype.clickSaveButton = function() {
  var continueCallback = {
    context : this,
    success : function() { console.log("Federation saved"); },
    failure : function() { console.log("Error saving federation"); }	    
  };
  
  this._saveFederation(continueCallback);  
};

//------------------------------------------------------------------------------------
FederationActivity.prototype.changeConnectionType = function(type) {
  var connection = this.federation.connection;
  
  if (this.federation.connection.type != type) {
    connection = (type == ConnectionTypes.LDAP)? new LDAPConnection() : new DatabaseConnection();    
  }

  this.currentConnection = connection;
  this.view.editConnection(this.currentConnection);   
};

//------------------------------------------------------------------------------------
FederationActivity.prototype.startFederation = function() {
  NotificationManager.showNotification(Lang.Notifications.STARTING);
  
  this.service.startFederation(this.federation.id, {
    context: this,
    success : function(federation) {
      NotificationManager.showNotification(Lang.Notifications.STARTED, 1000);
      
      this.federation.set(Federation.STATE, federation.get(Federation.STATE));      
    },
    failure : function(ex) {
      throw ex;
    }
  });  
};

//------------------------------------------------------------------------------------
FederationActivity.prototype.stopFederation = function() {
  NotificationManager.showNotification(Lang.Notifications.STOPPING);
  
  this.service.stopFederation(this.federation.id, {    
    context: this,
    success : function(federation) {
      NotificationManager.showNotification(Lang.Notifications.STOPPED, 1000);
      
      this.federation.set(Federation.STATE, federation.get(Federation.STATE));
    },
    failure : function(ex) {
      throw ex;
    }
  });  
};

//------------------------------------------------------------------------------------
FederationActivity.prototype._saveFederation = function(callback) {
  var editor = this.view.getEditor();
  if (! editor.isDirty()) { callback.success.call(callback.context, null); return; };

  editor.flush();
  if (this.federation.error) {
	  editor.showError(this.federation.error);	  
	  return; 
  }
  
  this.federation.connection = this.currentConnection;
  
  this.service.saveFederation(this.federation, {
    context : this,
    success : function(federation) {    	
      var event = {name: Events.FEDERATION_SAVED, data:{federation : this.federation} };
      EventBus.fire(event);	 
      callback.success.call(callback.context, null);
      
      NotificationManager.showNotification(Lang.Notifications.FEDERATION_SAVED, 1000);
    },
    failure : function() {
      callback.failure.call(callback.context, null);
      NotificationManager.showNotification(Lang.Notifications.SAVE_FEDERATION_ERROR, 1000);
    }
  });
};

//------------------------------------------------------------------------------------
FederationActivity.prototype._goBack = function() {
  var event = {name : Events.OPEN_DEPLOYMENT, token : new DeploymentPlace().toString(), data : {}};
  EventBus.fire(event);
};

//------------------------------------------------------------------------------------
FederationActivity.prototype._showView = function(federation) {
  this.view.getEditor().open(federation);	
  this._link(federation);
  this.view.show();	
};

//------------------------------------------------------------------------------------
FederationActivity.prototype._createView = function() {
  var view = new FederationView(Ids.Elements.FEDERATION_VIEW, AppTemplate.FederationView);
  return view;
};

//------------------------------------------------------------------------------------
FederationActivity.prototype._askForSaveChanges = function(callback) {
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
FederationActivity.prototype._link = function(model) {
  model.on(Events.CHANGED, this, this);	
};

//------------------------------------------------------------------------------------
FederationActivity.prototype._unlink = function(model) {
  if (!model) return;
  model.un(Events.CHANGED, this);
};

//------------------------------------------------------------------------------------
FederationActivity.prototype._clearModel = function() {
  this.federation = null;
};


