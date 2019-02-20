
var ServersActivity = Activity.extend({
  init : function() {
	this.view = this._createView();	
	this.view.setPresenter(this);
	this.service = GridedService;
	this.serversCollection = null;	  
  }
});
	
//--------------------------------------------------------------
ServersActivity.prototype.start = function(data) {
  if (! this.view) this.view = this._createView();
  
  if (this.serversCollection) {
    this._link(this.serversCollection);
    this._showView();    
  }
  
  if (! this.serversCollection) this.service.loadServers({
    context: this,
    success: function(serversCollection) {
      this.serversCollection = serversCollection;
      this.view.setServers(this.serversCollection.toArray());
      this._link(this.serversCollection);
      this._showView();
    },
    failure: function(ex) {
      throw ex; 
    }
  });
};

//--------------------------------------------------------------
ServersActivity.prototype.stop = function() {
  this._unlink(this.serversCollection);	  
  this.view.hide();
};

//--------------------------------------------------------------
ServersActivity.prototype.canStop = function(callback) {
  callback.success.call(callback.context, {});
};

//--------------------------------------------------------------
ServersActivity.prototype.notify = function(event) {
  var serversCollection = event.data.model;  
  this.view.setServers(serversCollection.toArray());   
};

//--------------------------------------------------------------
ServersActivity.prototype.openServer = function(id) {
  var event = {name: Events.OPEN_SERVER, token: new ServerPlace(id).toString(), data: {id:id}};
  EventBus.fire(event);
};

//--------------------------------------------------------------
ServersActivity.prototype.addServer = function(data) {
  var server = new Server();
  server.set(Server.NAME, data.name);
  server.set(Server.IP, data.ip);
  
  NotificationManager.showNotification(Lang.Notifications.ADD_SERVER);
  
  this.service.addServer(server, {
	  context: this,
	  success : function(server) {
	    this.serversCollection.add(server);	
	    this.view.clearAddServerDialog();
	    NotificationManager.showNotification(Lang.Notifications.SERVER_ADDED, 1000);
	  },
	  failure : function(ex) {
	    NotificationManager.showNotification(Lang.Notifications.ADD_SPACE_ERROR, 1000);
	    throw ex;
	  }
  });  
};

//--------------------------------------------------------------
ServersActivity.prototype.removeServers = function(ids) {    
  NotificationManager.showNotification(Lang.Notifications.REMOVING, 1000);
  
  var serverIds = ids; 	
  this.service.removeServers(serverIds, {
    context: this,
    success: function(ids) {    	
       this.serversCollection.remove(serverIds);
       NotificationManager.showNotification(Lang.Notifications.REMOVED, 1000);
    },
    failure : function(ex) {
      NotificationManager.showNotification(Lang.Notifications.REMOVE_ERROR, 1000);
      throw ex;
    }
  });	
};

//--------------------------------------------------------------
ServersActivity.prototype.handle = function(event) {
  switch (event.name) {
    case Events.SERVER_SAVED: this.serversCollection = null; break;
  }
};

//------------------------------------------------------------------------------------
ServersActivity.prototype._link = function(model) {
  model.on(Events.ADDED, this, this);  
  model.on(Events.REMOVED, this, this);
};

//------------------------------------------------------------------------------------
ServersActivity.prototype._unlink = function(model) {
  if (!model) return;
  model.un(Events.REMOVED, this);
  model.un(Events.ADDED, this);
};

//--------------------------------------------------------------
ServersActivity.prototype._createView = function(id, html) {
  var view = new ServersView(Ids.Elements.SERVERS_VIEW);
  return view;
};

//--------------------------------------------------------------
ServersActivity.prototype._showView = function() {
  this.view.show();  
};
