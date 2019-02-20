
GridedService = {
  		
};


//----------------------------------------------------------------------------------
GridedService.loadAccount = function(callback) {
  var request = new Request({
    url: Context.Config.Api + "?op=loadaccount",
	  method: 'get',
	  params: {},
	  callback : callback  
  });	
  
  request.send();	
};

//----------------------------------------------------------------------------------
GridedService.loadServers = function(callback) {
  var serviceCallback = {
	success : function(jsonServer) {
	  var servers = ServersSerializer.unserialize(jsonServer);
	  callback.success.call(callback.context, servers);
	},
	failure : function() { callback.failure.apply(callback.context, arguments); }
  };		
	
  var request = new Request({
    url: Context.Config.Api + "?op=loadservers",
	  method: 'get',
	  params: {},
	  callback : serviceCallback  
  });	
  
  request.send();
};

//----------------------------------------------------------------------------------
GridedService.loadServer = function(id, callback) {
  var serviceCallback = {
    success: function(jsonServer) {
      var server = ServerSerializer.unserialize(jsonServer);
      callback.success.call(callback.context, server);
    },
    failure: function() { callback.failure.apply(callback.context, arguments); }
  };
  
  var request = new Request({
    url: Context.Config.Api + "?op=loadserver",
    params: {id: id},
	  method: 'get',	
	  callback : serviceCallback
  });	
  
  request.send();
};

//----------------------------------------------------------------------------------
GridedService.removeServers = function(ids, callback) {
  var json = Ext.util.JSON.encode(ids);
  
  var request = new Request({
    url: Context.Config.Api + "?op=removeservers",
    params: {ids: json},
	  method: 'get',	
	  callback : callback
  });	
  
  request.send();
};

//----------------------------------------------------------------------------------
GridedService.addServer = function(server, callback) {
  var serviceCallback = {
    success: function(jsonServer) {
	  var server = ServerSerializer.unserialize(jsonServer);
      callback.success.call(callback.context, server);
    },
	failure: function() { callback.failure.apply(callback.context, arguments); }
  };
	
  var request = new Request({
	  url: Context.Config.Api + "?op=addserver",
	  params: {name: server.get(Server.NAME), ip: server.get(Server.IP)},
	  method: 'get',	
	  callback : serviceCallback
  });
  request.send();
};

//----------------------------------------------------------------------------------
GridedService.saveServer = function(server, callback) {
	
  jsonServer = ServerSerializer.serialize(server);
  
  var request = new Request({
	  url: Context.Config.Api + "?op=saveserver",
	  params: {server: jsonServer},
	  method: 'get',	
	  callback : callback
  });
  request.send();
};

//----------------------------------------------------------------------------------
GridedService.loadServerSpaces = function(id, callback) {
  var serviceCallback = {
    success: function(jsonSpaces) {
    var spacesCollection = SpacesSerializer.unserialize(jsonSpaces);
	  callback.success.call(callback.context, spacesCollection);
	},
	failure: function() { callback.failure.apply(callback.context, arguments); } 
  };
  
  var request = new Request({
    url: Context.Config.Api + "?op=loadserverspaces",
    params: {id: id},                
	  method: 'get',	  
	  callback : serviceCallback  
  });	
		  
  request.send(); 	
};

//----------------------------------------------------------------------------------
GridedService.loadServerState = function(id, callback) {
  var serviceCallback = {
    success: function(jsonServerState) {
      var serverState = ServerStateSerializer.unserialize(jsonServerState);
      callback.success.call(callback.context, serverState);
    },
    failure: function() { callback.failure.apply(callback.context, arguments); } 
  };   
  
  var request = new Request({
    url: Context.Config.Api + "?op=loadserverstate",
    params: {id: id},
	  method: 'get',	  
	  callback : serviceCallback  
  });	
  
  request.send();
};

//----------------------------------------------------------------------------------
GridedService.loadAllFederations = function(callback) {
  var serviceCallback = {
    success: function(jsonFederations) {
	  var federationsCollection = FederationsSerializer.unserialize(jsonFederations);
	  callback.success.call(callback.context, federationsCollection);
	},
	failure: function() { callback.failure.apply(callback.context, arguments); }
 };
	
  var request = new Request({
    url: Context.Config.Api + "?op=loadfederations",    
	  method: 'get',
	  params: {},
	  callback : serviceCallback  
  });	
	  
  request.send();	
};

//----------------------------------------------------------------------------------
GridedService.loadFederation = function(id, callback) {

  var serviceCallback = {
   success: function(jsonFederation) {
     var federation = FederationSerializer.unserialize(jsonFederation);
	   callback.success.call(callback.context, federation);
	},
    failure: function() { callback.failure.apply(callback.context, arguments); }
 };
	
  var request = new Request({
    url: Context.Config.Api + "?op=loadfederation",
    method: 'get',	
    params: {id: id},
	callback : serviceCallback  
  });	
  
  request.send();
};


//----------------------------------------------------------------------------------
GridedService.addFederation = function(serverId, name, url, callback) {
  var serviceCallback = {
    success: function(jsonFederation) {
    var federation = FederationSerializer.unserialize(jsonFederation);
	  callback.success.call(callback.context, federation);
	},
    failure: function() { callback.failure.apply(callback.context, arguments); }
  };
				
  var request = new Request({
    url: Context.Config.Api + "?op=addfederation",
	  method: 'get',	
	  params: {server_id: serverId, name: name, url: url},
	  callback : serviceCallback  
  });	
			  
  request.send();    
};

//----------------------------------------------------------------------------------
GridedService.saveFederation = function(federation, callback) {
	
  jsonFederation = FederationSerializer.serialize(federation);
  
  var request = new Request({
	  url: Context.Config.Api + "?op=savefederation",
	  params: {federation: jsonFederation},
	  method: 'get',	
	  callback : callback
  });
  request.send();
};

//----------------------------------------------------------------------------------
GridedService.removeFederations = function(ids, callback) {
  var json = Ext.util.JSON.encode(ids);
  
  var request = new Request({
    url: Context.Config.Api + "?op=removefederations",
    params: {ids: json},
	  method: 'get',	
	  callback : callback
  });	
			  
  request.send();	
};

//----------------------------------------------------------------------------------
GridedService.startFederation = function(id, callback) {    
  var serviceCallback = {
    success: function(jsonFederation) {
	    var federation = FederationSerializer.unserialize(jsonFederation);
      callback.success.call(callback.context, federation);
	},
    failure: function() { callback.failure.apply(callback.context, arguments); }
 };
	
  var request = new Request({
    url: Context.Config.Api + "?op=startfederation",
    params: {id: id},
	  method: 'get',	  
	  callback : serviceCallback
  });	
			  
  request.send();	
};

//----------------------------------------------------------------------------------
GridedService.stopFederation = function(id, callback) {    
  var serviceCallback = {
    success: function(jsonFederation) {
	    var federation = FederationSerializer.unserialize(jsonFederation);
      callback.success.call(callback.context, federation);
	},
    failure: function() { callback.failure.apply(callback.context, arguments); }
 };
	
  var request = new Request({
    url: Context.Config.Api + "?op=stopfederation",
    params: {id: id},
	  method: 'get',	
	  callback : serviceCallback
  });	
			  
  request.send();	
};



//----------------------------------------------------------------------------------
GridedService.loadSpace = function(id, callback) {
  var serviceCallback = {
    success: function(jsonSpace) {
      var space = SpaceSerializer.unserialize(jsonSpace);
      callback.success.call(callback.context, space);
    },
    failure: function(ex) { callback.failure.apply(callback.context, ex); }
 };
				
  var request = new Request({
    url: Context.Config.Api + "?op=loadspace",
    method: 'get',	
    params: {id: id},
	  callback : serviceCallback  
  });	
			  
  request.send();  	
};

//----------------------------------------------------------------------------------
GridedService.loadSpacesWithModel = function(id, version, callback) {
  var serviceCallback = {
    success : function(jsonSpaces) {
      var spaces = SpacesSerializer.unserialize(jsonSpaces);
      callback.success.call(callback.context, spaces, version);
    },
    failure : function(ex) { callback.failure.apply(callback.context, ex); }
  };
  
  var request = new Request({
    url: Context.Config.Api + "?op=loadspaceswithmodel",
    method: 'get',  
    params: {model_id: id, version_id: version.id},
    callback : serviceCallback  
  }); 
        
  request.send();
};

//----------------------------------------------------------------------------------
GridedService.loadSpacesByModel = function(modelId, callback) {
  var serviceCallback = {
    success : function(jsonSpaces) {
      var spaces = SpacesSerializer.unserialize(jsonSpaces);
      callback.success.call(callback.context, spaces);
    },
    failure : function(ex) { callback.failure.apply(callback.context, ex); }
  };
  
  var request = new Request({
    url: Context.Config.Api + "?op=loadspacesbymodel",
    method: 'get',  
    params: {model_id: modelId},
    callback : serviceCallback  
  }); 
        
  request.send();
};


//----------------------------------------------------------------------------------
GridedService.loadServices = function(spaceId, callback) {
  var serviceCallback = {
    success: function(jsonServices) {
      var services = PublicationServicesSerializer.unserialize(jsonServices);
      callback.success.call(callback.context, services);
    },
    failure: function(ex) { callback.failure.apply(callback.context, ex); }
  };
							
  var request = new Request({
    url: Context.Config.Api + "?op=loadservices",
    method: 'get',	
    params: {space_id: spaceId},
	  callback : serviceCallback
  });	
						  
  request.send();
};

//----------------------------------------------------------------------------------
GridedService.loadImporterTypes = function(spaceId, callback) {              
  var request = new Request({
    url: Context.Config.Api + "?op=loadimportertypes",
    method: 'get',  
    params: {space_id: spaceId},
    callback : callback
  }); 
              
  request.send();
};

//----------------------------------------------------------------------------------
GridedService.startImport = function(form, spaceId, importerTypeId, callback) {
  var params = {
    space_id : spaceId,
    importer_type_id : importerTypeId
  };
  
  this.upload(form, "startimport", params, callback);    
};

//----------------------------------------------------------------------------------
GridedService.saveSpace = function(space, callback) {
  
  jsonSpace = SpaceSerializer.serialize(space);
										
  var request = new Request({
    url: Context.Config.Api + "?op=savespace",
    method: 'get',	
    params: {space: jsonSpace},
	callback : callback
  });	
									  
  request.send();	
};

//----------------------------------------------------------------------------------
GridedService.addSpace = function(serverId, federationId, name, url, modelId, callback) {
  var serviceCallback = {
    success: function(jsonSpace) {
      var space = SpaceSerializer.unserialize(jsonSpace);
	    callback.success.call(callback.context, space);
	  },
    failure: function() { callback.failure.apply(callback.context, arguments); }
  };
				
  var request = new Request({
    url: Context.Config.Api + "?op=addspace",
	  method: 'get',	
	  params: {server_id: serverId, federation_id: federationId, name: name, url : url, model_id : modelId},
	  callback : serviceCallback  
  });	
			  
  request.send();    
};

//----------------------------------------------------------------------------------
GridedService.removeSpaces = function(ids, callback) {
  var json = Ext.util.JSON.encode(ids);
  
  var request = new Request({
    url: Context.Config.Api + "?op=removespaces",
    params: {ids: json},
	  method: 'get',	
	  callback : callback
  });	
			  
  request.send();	
};

//----------------------------------------------------------------------------------
GridedService.upgradeSpaces = function(spaceIds, modelId, versionId, callback) {
  var json = Ext.util.JSON.encode(spaceIds);

  var serviceCallback = {
    success : function() {      
      callback.success.call(callback.context, spaceIds, modelId, versionId);
    },
    failure : function() { callback.failure.apply(callback.context, arguments); }
  };
  
  var request = new Request({
    url: Context.Config.Api + "?op=upgradespaces",
    params: {ids: json, model_id: modelId, version_id : versionId},
    method: 'get',    
    callback : serviceCallback
  }); 
        
  request.send();
};

//----------------------------------------------------------------------------------
GridedService.startSpace = function(id, callback) {    
  var serviceCallback = {
    success: function(jsonSpace) {
      var space = SpaceSerializer.unserialize(jsonSpace);
      callback.success.call(callback.context, space);
  },
    failure: function() { callback.failure.apply(callback.context, arguments); }
 };
  
  var request = new Request({
    url: Context.Config.Api + "?op=startspace",
    params: {id: id},
    method: 'get',
    callback : serviceCallback
  }); 
        
  request.send(); 
};

//----------------------------------------------------------------------------------
GridedService.stopSpace = function(id, callback) {    
  var serviceCallback = {
    success: function(jsonSpace) {
      var space = SpaceSerializer.unserialize(jsonSpace);
      callback.success.call(callback.context, space);
   },
   failure: function() { callback.failure.apply(callback.context, arguments); }
 };
  
  var request = new Request({
    url: Context.Config.Api + "?op=stopspace",
    params: {id: id},
    method: 'get',    
    callback : serviceCallback
  }); 
        
  request.send(); 
};

//----------------------------------------------------------------------------------
GridedService.loadModels = function(callback) {
  var serviceCallback = {
	success : function(jsonModel) {
	  var models = ModelsSerializer.unserialize(jsonModel);
	  callback.success.call(callback.context, models);
	},
	failure : function() { callback.failure.apply(callback.context, arguments); }
  };		
	
  var request = new Request({
    url: Context.Config.Api + "?op=loadmodels",
	  method: 'get',
	  params: {},
	  callback : serviceCallback
  });	
  
  request.send();
};

//----------------------------------------------------------------------------------
GridedService.loadModel = function(id, callback) {
  var serviceCallback = {
    success: function(jsonModel) {
      var model = ModelSerializer.unserialize(jsonModel);
      callback.success.call(callback.context, model);
    },
    failure: function() { callback.failure.apply(callback.context, arguments); }
  };
  
  var request = new Request({
    url: Context.Config.Api + "?op=loadmodel",
    params: {id: id},
	  method: 'get',	  
	  callback : serviceCallback
  });	
  
  request.send();
};

//----------------------------------------------------------------------------------
GridedService.uploadModelVersion = function(form, params, callback) {
  var serviceCallback = {
    success: function(jsonModel) {         
      var model = ModelVersionSerializer.unserialize(jsonModel);
      callback.success.call(callback.context, model);
    },
    failure: function() { callback.failure.apply(callback.context, arguments); }
  };

  this.upload(form, "uploadmodelversion", params, serviceCallback);
};

//----------------------------------------------------------------------------------
GridedService.saveModel = function(model, callback) {
  var jsonModel = ModelSerializer.serialize(model);
  
  var request = new Request({
    url: Context.Config.Api + "?op=savemodel",
    method: 'get',  
    params: {model: jsonModel},
    callback : callback
  }); 
                    
  request.send();   
};

//----------------------------------------------------------------------------------
GridedService.addModel = function(form, name, callback) {
  var serviceCallback = {
    success: function(jsonModel) {
      var model = ModelSerializer.unserialize(jsonModel);
      callback.success.call(callback.context, model);
    },
    failure: function() { callback.failure.apply(callback.context, arguments); }      
  };
/*    
  var request = new Request({
    url: Context.Config.Api + "?op=addmodel",
    params: {form: form, name : name},
    method: 'get',    
    callback : serviceCallback
  });
*/        
//  request.send();
  
  this.upload(form, "addmodel", [], serviceCallback);

};


//----------------------------------------------------------------------------------
GridedService.removeModels = function(ids, callback) {
  var json = Ext.util.JSON.encode(ids);
  
  var request = new Request({
    url: Context.Config.Api + "?op=removemodels",
    params: {ids: json},
    method: 'get',    
    callback : callback
  }); 
        
  request.send(); 
};

//----------------------------------------------------------------------------------
GridedService.uploadImage = function(form, params, callback) {
  var serviceCallback = {
    success: function(jsonImage) {      	 
      var name   = URLEncoder.decode(jsonImage.name);
      var source = URLEncoder.decode(jsonImage.source);
      callback.success.call(callback.context, name, source);
    },
    failure: function() { callback.failure.apply(callback.context, arguments); }
  };
	
  this.upload(form, "uploadimage", params, serviceCallback);
};

//----------------------------------------------------------------------------------
GridedService.downloadImage = function(jsonIds, filename, callback) {
  	
  var request = new Request({
    url: Context.Config.Api + "?op=downloadImage",
    params: {'server-id': jsonIds.serverId, 'federation-id': jsonIds.federationId, 'space-id': jsonIds.spaceId, filename: filename},
	  method: 'get',	
	  callback : callback
  });						  
  request.send();	
};


//----------------------------------------------------------------------------------
GridedService.upload = function(form, operation, parameters, callback) {
  form.action = Context.Config.Api;
  addInput(form, "op", operation);
  
  if (parameters != null) {
    for (var name in parameters) {
      if (isFunction(parameters[name])) continue;
      addInput(form, name, parameters[name]);
    }
  }
  
  var request = new Request({
	  url :  Context.Config.Api + "?op=" + operation,  
	  method: 'post',	
	  form: form,
	  isUpload: true,
	  callback : callback  
  });
      
  request.send();
};
