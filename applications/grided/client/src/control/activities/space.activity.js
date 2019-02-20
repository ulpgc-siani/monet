var SpaceActivity = Activity.extend({
  init : function() {
    this.view = this._createView();
    this.view.setPresenter(this);
    this.service = GridedService;
    
    this.space = null;
    this.services = null;    
  }	
});

//------------------------------------------------------------------------------------
SpaceActivity.prototype.start = function(data) {
  if (!this.space || this.space.id != data.id) this._clearModel();	
	
  if (!this.view) this.view = this._createView();
  
  if (this.space) this._showView(this.space);
  if (! this.space) this.service.loadSpace(data.id, {
	 context: this,
	 success: function(space) {
	   this.space = space;	   
	   this.view.setSpace(this.space);
	   this._showView(this.space);
	 },
	 failure : function(ex) {
		 throw ex; 
	 }
  });
};

//------------------------------------------------------------------------------------
SpaceActivity.prototype.stop = function() {  
  var editor = this.view.getEditor();
  if (editor) editor.close();  
  if (this.pooler) this.pooler.stop();
  this._unlink(this.space);	
  this.space = null;
  this.view.hide();	
};

//------------------------------------------------------------------------------------
SpaceActivity.prototype.canStop = function(callback) {
 var editor = this.view.getEditor();
 if (! editor.isDirty()) { callback.success.call(callback.context, {}); return;}; 
		 
 var continueCallback = {
   context: this,
   success : function() { callback.success.call(callback.context, {}); },
   failure : function() { alert("Error saving space"); }     
 };	 
	 
 var saveCallback = {
   context: this,
   save: function() { this._saveSpace(continueCallback); },
   cancel : function() { editor.reset(); continueCallback.success.call(continueCallback.context, null); }   
 };
	 
 this._askForSaveChanges(saveCallback); 
  	  
};

//------------------------------------------------------------------------------------
SpaceActivity.prototype.notify = function(event) {   
  if (event.name = Events.CHANGED) {
    switch (event.propertyName) {
      case Space.SERVICES:
        this.view.setServices(this.space.get(Space.SERVICES).toArray());
        this.view.showPublishTab();
      break;
      case Space.STATE: 
        this.view.refreshState();
      break;
    }	  
  }
};

//--------------------------------------------------------------
SpaceActivity.prototype.clickBackButton = function() {
  this._goBack();
};

//--------------------------------------------------------------
SpaceActivity.prototype.clickDiscardButton = function() {
  var editor = this.view.getEditor();
  editor.reset();
  this._goBack();
};

//--------------------------------------------------------------
SpaceActivity.prototype.clickSaveButton = function() {
  var continueCallback = {
    context : this,
    success : function() { console.log("Space saved"); },
    failure : function() { console.log("Error saving space"); }	    
  };
  
  this._saveSpace(continueCallback);  
};

//------------------------------------------------------------------------------------
SpaceActivity.prototype.clickChangeLogo = function() {
  this.view.showUploadImageDialog();  	  
};

//------------------------------------------------------------------------------------
SpaceActivity.prototype.publishService = function(id) {
  var service = this.space.getService(id);
  service.set(PublicationService.PUBLISHED, true);
};

//------------------------------------------------------------------------------------
SpaceActivity.prototype.unPublishService = function(id) {
  var service = this.space.getService(id);
  service.set(PublicationService.PUBLISHED, false);
};

//------------------------------------------------------------------------------------
SpaceActivity.prototype.changeServiceType = function(type) {
  this.view.filterServiceByType(type);	
};

//------------------------------------------------------------------------------------
SpaceActivity.prototype.uploadImage = function(form, callback) {
  var federation   = this.space.get(Space.FEDERATION);
  var federationId = federation.id;
  var serverId     = federation.get(Federation.SERVER).id;
	  
  var params = {server_id: serverId, federation_id: federationId, space_id: this.space.id, model_type: ModelTypes.SPACE};
  
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
SpaceActivity.prototype.openServer = function(id) {
  var event = {name: Events.OPEN_SERVER, token: new ServerPlace(id).toString(), data: {id: id}};
  EventBus.fire(event);	   	
};

//------------------------------------------------------------------------------------
SpaceActivity.prototype.openFederation = function(id) {
  var event = {name: Events.OPEN_FEDERATION, token: new FederationPlace(id).toString(), data: {id: id}};
  EventBus.fire(event);	   	
};

//------------------------------------------------------------------------------------
SpaceActivity.prototype.openModel = function(id) {
  var event = {name: Events.OPEN_MODEL, token: new ModelPlace(id).toString(), data: {id: id}};
  EventBus.fire(event);	   	
};

//------------------------------------------------------------------------------------
SpaceActivity.prototype.clickPublishTab = function(id) {
  if (this.services) {
    this.view.showPublishTab();
    return;
  }
  
  this.service.loadServices(this.space.id, {
    context: this,
    success : function(services) {       	
      this.space.set(Space.SERVICES, services);
      this.view.showPublishTab();
    },
    failure : function(ex) {
      throw ex;	
    }
  });
};

//------------------------------------------------------------------------------------
SpaceActivity.prototype.clickImportTab = function(id) {
  if (this.importerTypes) {
    this.view.showImportTab();
    return;
  }
  
  this.service.loadImporterTypes(this.space.id, {
    context : this,
    success : function(importerTypes) {
      this.view.setImporterTypes(importerTypes);
      this.view.showImportTab(); 
    },
    failure : function(ex) {
      throw ex;
    }
  });    
};

//------------------------------------------------------------------------------------
SpaceActivity.prototype.startImportation = function(form, importerTypeId) {  
      
  this.pooler = new Pooler({poolTime: 3000, url: Context.Config.Api + "?op=loadimportprogress", params : {id : this.space.id}});
  var pooler = this.pooler;
  
  this.service.startImport(form, this.space.id, importerTypeId, {
    context: this,
    success : function() {      
      pooler.start({context : this, success : function(progress) {
        if (progress.value < 100) this.view.setProgress(progress);          
        else {
          pooler.stop();
          this.view.finishImportation();
        }
      }});      
    },
    failure : function(ex) {
      throw ex;
    }
  });  
};

//------------------------------------------------------------------------------------
SpaceActivity.prototype.stopImportation = function() {
  this.pooler.stop();
};

//------------------------------------------------------------------------------------
SpaceActivity.prototype.startSpace = function() {
  NotificationManager.showNotification(Lang.Notifications.STARTING);
  
  this.service.startSpace(this.space.id, {
    context: this,
    success : function(space) {
      NotificationManager.showNotification(Lang.Notifications.STARTED, 1000);
      
      this.space.set(Space.STATE, space.get(Space.STATE));      
    },
    failure : function(ex) {      
      throw ex;
    }
  });  
};

//------------------------------------------------------------------------------------
SpaceActivity.prototype.stopSpace = function() {
  NotificationManager.showNotification(Lang.Notifications.STOPPING);
  
  this.service.stopSpace(this.space.id, {
    context: this,
    success : function(space) {
      NotificationManager.showNotification(Lang.Notifications.STOPPED, 1000);
      
      this.space.set(Space.STATE, space.get(Space.STATE));      
    },
    failure : function(ex) {
      throw ex;
    }
  });  
};

//------------------------------------------------------------------------------------
SpaceActivity.prototype._askForSaveChanges = function(callback) {
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
SpaceActivity.prototype._showView = function(space) {
  this.view.getEditor().open(space);
  this._link(space);
  this.view.show();
};

//------------------------------------------------------------------------------------
SpaceActivity.prototype._goBack = function() {  
  var event = {name : Events.OPEN_DEPLOYMENT, token : new DeploymentPlace().toString(), data : {}};
  EventBus.fire(event);
};

//------------------------------------------------------------------------------------
SpaceActivity.prototype._link = function(model) {
  model.on(Events.CHANGED, this, this);  
};

//------------------------------------------------------------------------------------
SpaceActivity.prototype._unlink = function(model) {
  if (!model) return;  
  model.un(Events.CHANGED, this);
};

//------------------------------------------------------------------------------------
SpaceActivity.prototype._saveSpace = function(callback) {
  var editor = this.view.getEditor();
  if (! editor.isDirty()) { callback.success.call(callback.context, null); return; };

  editor.flush();
  if (this.space.error) {
    editor.showError(this.space.error);
    return;
  }
      
  var space = this.space;
  this.service.saveSpace(this.space, {
    context : this,
    success : function() {    	
      var event = {name: Events.SPACE_SAVED, data:{space : space} };
      EventBus.fire(event);	 
      callback.success.call(callback.context, null);
      NotificationManager.showNotification(Lang.Notifications.SPACE_SAVED, 1000);
    },
    failure : function() {
      callback.failure.call(callback.context, null);
      NotificationManager.showNotification(Lang.Notifications.SAVE_SPACE_ERROR, 1000);
    }
  });
};

//------------------------------------------------------------------------------------
SpaceActivity.prototype._createView = function(space) {  
  var view = new SpaceView(Ids.Elements.SPACE_VIEW, AppTemplate.SpaceView);
  return view;
};

//------------------------------------------------------------------------------------
SpaceActivity.prototype._clearModel = function() {
  this.space = null;  
};
