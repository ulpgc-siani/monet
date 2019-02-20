var DeploymentActivity = Activity.extend({
  init : function() {
    this.view = this._createView();
    this.view.setPresenter(this);
    this.service = GridedService;
    
    this.serversCollection = null;
    this.federationsCollection = null;    
    this.selectedFederation = null;
    
    this.selectedServer = null;
    this.browsingSpacesFlag = false;
  }	
});

//--------------------------------------------------------------
DeploymentActivity.prototype.start = function(data) {
  if (! this.view) this.view = this._createView();
  
  if (this.federationsCollection) this._showView(this.federationsCollection);
  
  if (! this.federationsCollection) this.service.loadAllFederations({    
    context: this,
    success: function(federationsCollection) {
      this.federationsCollection = federationsCollection;
      this.view.setFederations(this.federationsCollection.toArray());     
      this._showView(this.federationsCollection);                       
    },
    failure: function(ex) {
      throw ex; 
    }
  });
};

//--------------------------------------------------------------
DeploymentActivity.prototype.stop = function() {
  NotificationManager.hideNotification();
  
  this._unlink(this.federationsCollection);
  if (this.selectedFederation) this._unlink(this.selectedFederation);
  this.view.hide();
};

//--------------------------------------------------------------
DeploymentActivity.prototype.canStop = function(callback) {
  callback.success.call(callback.context, {});	
};

//--------------------------------------------------------------
DeploymentActivity.prototype.notify = function(event) {
  if (event.data.model instanceof Collection) {
    var federations = event.data.model.toArray();
    this.view.setFederations(federations);    
  }
  else if (event.data.model instanceof Federation) {
    var federation = event.data.model;
    
    switch (event.propertyName) {
      case Federation.SPACES: 
    	  this.view.setSpaces(federation.get(Federation.SPACES).toArray());
    	  break;
      case Federation.STATE:
    	  this.view.refreshFederation(federation);
    }    
  }
};

//--------------------------------------------------------------
DeploymentActivity.prototype._showView = function(federations) {
  this._link(federations);
  
  if (federations.size() > 0) {
    if (! this.selectedFederation) this.selectedFederation = federations.get(0);
    this.selectFederation(this.selectedFederation.id);  
  }

  this._loadData();  
  this.view.show();
};

//--------------------------------------------------------------
DeploymentActivity.prototype.selectServer = function(data) {
  var id = data.serverId;
  
  if (id != "") { 
	  this.selectedServer = this.serversCollection.getById(id);
	  this.view.showAddFederationDialog();
	  
	  if (this.selectedServer != null && this.selectedFederation != null) {
	    this.view.showAddSpaceDialog();	  
	  }
	  else {
		this.view.hideAddSpaceDialog();
	  }
	  
  } else {
	  this.selectedServer = null;
	  this.view.hideAddFederationDialog();
	  this.view.hideAddSpaceDialog();
  }
};

//--------------------------------------------------------------
DeploymentActivity.prototype._loadFederation = function(id, callback) {

  this.view.showLoadingSpaces();
 
  this.service.loadFederation(id, {
     context : this,
     success : function(federation) {
       this.view.hideLoadingSpaces();
    	
    	  var oldFederation = this.federationsCollection.getById(federation.id);
    	  this._link(oldFederation);
    	  oldFederation.set(Federation.SPACES, federation.get(Federation.SPACES));
    	  this._unlink(oldFederation);
    	
    	if (callback) { callback.success.call(callback.context, oldFederation);}
     },
     failure : function() {
   	   this.view.hideLoadingSpaces();
       alert("Error loading federation"); 
     }
   }); 	
};

//--------------------------------------------------------------
DeploymentActivity.prototype.selectFederation = function(id) {
  if (this.selectedFederation && ! this.selectedFederation.id == id) return;
	  
   this._loadFederation(id, {
    context: this,
    success: function(federation) {
      if (this.selectedFederation) this._unlink(this.selectedFederation);
      this.selectedFederation = federation;
      this.view.selectFederation(federation.id);
      this.view.showSpacesViewer();
      
  	  if (this.selectedServer) this.view.showAddSpaceDialog();    
    }, 
    failure: function(ex) {
      throw ex;
    }
   });
};

//--------------------------------------------------------------
DeploymentActivity.prototype.unSelectFederation = function() {
  if (! this.browsingSpacesFlag) {
    this.view.hideSpacesViewer();
  }
};

//--------------------------------------------------------------
DeploymentActivity.prototype.openFederation = function(id) {
  this.selectedFederation = this.federationsCollection.getById(id);
  var event = {name: Events.OPEN_FEDERATION, token: new FederationPlace(id).toString(), data: {id: id}};
  EventBus.fire(event);
};

//--------------------------------------------------------------
DeploymentActivity.prototype.addFederation = function(name, url) {

  NotificationManager.showNotification(Lang.Notifications.ADD_FEDERATION);
  
  this.service.addFederation(this.selectedServer.id, name, url, {
    context: this,
    success: function(federation) {
	    this.federationsCollection.add(federation);
	    this.selectFederation(federation.id);
	    this.view.clearAddFederationDialog();
	    this.view.focusAddFederationDialog();
	    NotificationManager.showNotification(Lang.Notifications.FEDERATION_ADDED, 1000);	  
	  },
	  failure : function() {
  	  this.view.hideAddFederationDialog();
	    NotificationManager.showNotification(Lang.Notifications.ADD_FEDERATION_ERROR, 1000);	  
	  }
  });  	  
};


//--------------------------------------------------------------
DeploymentActivity.prototype.removeFederations = function(ids) {    
  var federationIds = ids; 
  
  NotificationManager.showNotification(Lang.Notifications.REMOVING);
  
  this.service.removeFederations(ids, {
    context: this,
    success: function() {
      if (this.federationsCollection.size() > 0) {
        if (this.federationsCollection.include(this.selectedFederation)) this.selectFederation(this.federationsCollection.get(0).id); 
      }
	    this.federationsCollection.remove(federationIds);
	      
	    NotificationManager.showNotification(Lang.Notifications.REMOVED, 1000);
    },
    failure : function(ex) {	  
      NotificationManager.showNotification(Lang.Notifications.REMOVE_ERROR, 1000);
      throw ex;      
    }
  });  
};

//--------------------------------------------------------------
DeploymentActivity.prototype.startFederation = function(id) {
  NotificationManager.showNotification(Lang.Notifications.STARTING);
  
  this.service.startFederation(id, {
    context: this,
    success : function(federation) {
      NotificationManager.showNotification(Lang.Notifications.STARTED, 1000);
    	this._updateFederationState(federation);
    }, 
    failure : function(ex) {
      throw ex;
    }
  });	
};

//--------------------------------------------------------------
DeploymentActivity.prototype.stopFederation = function(id) {    
  NotificationManager.showNotification(Lang.Notifications.STOPPING);
  this.service.stopFederation(id, {
    context: this,
    success : function(federation) {
      NotificationManager.showNotification(Lang.Notifications.STOPPED, 1000);
   	  this._updateFederationState(federation);
   }, 
   failure : function(ex) {
     throw ex;
   }
 });	
};

//--------------------------------------------------------------
DeploymentActivity.prototype.addSpace = function(name, url, modelId) {
  
  NotificationManager.showNotification(Lang.Notifications.ADD_SPACE);
  
  this.service.addSpace(this.selectedServer.id, this.selectedFederation.id, name, url, modelId, {
    context: this,
    success: function(space) {      
      this.selectedFederation.get(Federation.SPACES).add(space);
      this.view.setSpaces(this.selectedFederation.get(Federation.SPACES).toArray());            
	    this.view.clearAddSpaceDialog();      
      this.view.focusAddSpaceDialog();
	    
	    NotificationManager.showNotification(Lang.Notifications.SPACE_ADDED, 1000);	    
	  },
	  failure : function() {
	    this.view.clearAddSpaceDialog();
	    NotificationManager.showNotification(Lang.Notifications.ADD_SPACE_ERROR, 1000);	    
	  }
  });  	  
	
  this.view.showAddSpaceDialog();
};

//--------------------------------------------------------------
DeploymentActivity.prototype.openSpace = function(id) {
  var event = {name: Events.OPEN_SPACE, token: new SpacePlace(id).toString(), data: {id: id}};
  EventBus.fire(event);	
};

//--------------------------------------------------------------
DeploymentActivity.prototype.removeSpaces = function(ids) {
  var spaceIds = ids;	
  
  NotificationManager.showNotification(Lang.Notifications.REMOVING);
  
  this.service.removeSpaces(ids, {
    context: this,
    success: function() {
      this.selectedFederation.get(Federation.SPACES).remove(spaceIds);
      this.view.setSpaces(this.selectedFederation.get(Federation.SPACES).toArray());
      NotificationManager.showNotification(Lang.Notifications.REMOVED, 1000);
	},
	failure : function(ex) {	  
	  NotificationManager.showNotification(Lang.Notifications.REMOVE_ERROR, 1000);
	  throw ex;
	}
  });  	  
};

//--------------------------------------------------------------
DeploymentActivity.prototype.startSpace = function(id) {
  NotificationManager.showNotification(Lang.Notifications.STARTING);
  
  this.service.startSpace(id, {
    context: this,
    success : function(space) {
      NotificationManager.showNotification(Lang.Notifications.STARTED, 1000);
      this._updateSpaceState(space);
    }, 
    failure : function(ex) {
      throw ex;
    }
  }); 
};

//--------------------------------------------------------------
DeploymentActivity.prototype.stopSpace = function(id) {    
  NotificationManager.showNotification(Lang.Notifications.STOPPING);
  
  this.service.stopSpace(id, {
    context: this,
    success : function(space) {
      NotificationManager.showNotification(Lang.Notifications.STOPPING, 1000);
      this._updateSpaceState(space);
   }, 
   failure : function(ex) {
     throw ex;
   }
 });  
};


//--------------------------------------------------------------
DeploymentActivity.prototype.onOverSpacesViewer = function(federationId) {
  this.browsingSpacesFlag = true;	
};

//--------------------------------------------------------------
DeploymentActivity.prototype.onOutSpacesViewer = function(federationId) {
  this.browsingSpacesFlag = false;	
  this.view.hideSpacesViewer();
};

//--------------------------------------------------------------
//DeploymentActivity.prototype.handle = function(event) {
//  switch (event.name) {  
//    case Events.FEDERATION_SAVED: this.federationsCollection = null; break;
//    case Events.SPACE_SAVED :  this.selectedFederation = null; break;
//    break;
//  }	
//};

//--------------------------------------------------------------
DeploymentActivity.prototype._createView = function() {
  var view = new DeploymentView(Ids.Elements.DEPLOYMENT_VIEW);	
  return view;
};

//--------------------------------------------------------------
DeploymentActivity.prototype._link = function(model) {
  model.on(Events.ADDED, this, this);  	
  model.on(Events.REMOVED, this, this); 
  model.on(Events.CHANGED, this, this);
};

//--------------------------------------------------------------
DeploymentActivity.prototype._unlink = function(model) {
  if (!model) return;  
  model.un(Events.ADDED, this, this);
  model.un(Events.REMOVED, this, this);	  
  model.un(Events.CHANGED, this, this);
};

//--------------------------------------------------------------
DeploymentActivity.prototype._updateFederationState = function(selectedFederation) {
  var federation = this.federationsCollection.getById(selectedFederation.id);
  this._link(federation);
  federation.set(Federation.STATE, selectedFederation.state);
  this._unlink(federation);
};

//--------------------------------------------------------------
DeploymentActivity.prototype._updateSpaceState = function(newSpace) {
  var spaces = this.selectedFederation.get(Federation.SPACES);  
  var space = spaces.getById(newSpace.id);
  space.set(Space.STATE, newSpace.get(Space.STATE));
  this.view.refreshFederationSpaces();  
};

//--------------------------------------------------------------
DeploymentActivity.prototype._loadData = function() {
  var queque = new TasksQueQue();
  queque.add(this._loadServers, this);
  queque.add(this._loadModels, this);
  
  queque.on(TasksQueQue.COMPLETE_EVENT, {notify : function(event) {
    this.serversCollection = event.data.results[0];
    this.modelsCollection  = event.data.results[1];
    
    this.view.setServers(this.serversCollection.toArray());
    this.view.setModels(this.modelsCollection.toArray());    
    this.view.showSelectServerDialog();    
  }}, this);
  
  queque.execute();
};

//--------------------------------------------------------------
DeploymentActivity.prototype._loadModels = function(callback) {
  
  this.service.loadModels({
    context: this,
    success: function(modelsCollection) {      
      callback.end(modelsCollection);
    },
    failure : function(ex) {
      throw ex;
    }
  });  
};

//--------------------------------------------------------------
DeploymentActivity.prototype._loadServers = function(callback) {
  //if (this.serversCollection) this.view.showSelectServerDialog(this.serversCollection.toArray());
  
  this.service.loadServers({
    context: this,
    success: function(serversCollection) {    	
    	callback.end(serversCollection);
    },
    failure : function(ex) {
      throw ex;
    }
  });
};

