SpaceSerializer = {
  
  serialize: function(space) {
    var json = {};
    json.id = space.id;
    json.name = space.get(Space.NAME);
    json.label = space.get(Space.LABEL);
    json.logo = space.get(Space.LOGO);
    json.url = space.get(Space.URL);
    json.datawarehouse = space.get(Space.DATAWAREHOUSE);
    json.model_id = space.get(Space.MODEL_ID);
    
    var modelVersion = space.get(Space.MODEL_VERSION);
    if (modelVersion) {
      json.model_version = ModelVersionSerializer.serialize(modelVersion);
    }
    
    var state = space.get(Space.STATE);
    var jsonState = {};
    jsonState.running = state.get(State.RUNNING);
    jsonState.time    = state.get(State.TIME);
    
    json.state = jsonState;       
    
    var jsonFederation = {};
    var federation = space.get(Space.FEDERATION);    
    jsonFederation.id   = federation.id;
    jsonFederation.name = federation.get(Federation.NAME);
        
    var jsonServer = {};
    var server = federation.get(Federation.SERVER);
    jsonServer.id    = server.id;
    jsonServer.name = server.get(Server.NAME);
    jsonServer.ip    = server.get(Server.IP);
    
    jsonFederation.server = jsonServer;    
    json.federation = jsonFederation;
        
    var services = space.get(Space.SERVICES);
    var jsonServices = [];
    services.each(function(index, item) {
      var jsonService = {name: item.get(PublicationService.NAME), type: item.get(PublicationService.TYPE), published : item.get(PublicationService.PUBLISHED)};
      jsonServices.push(jsonService);
    });
    json.services = jsonServices;    
        
	var text = Ext.util.JSON.encode(json);
	return text;
  },
  
  unserialize: function(jsonSpace) {
    var space = new Space();
    space.set(Space.ID, jsonSpace.id, {silent: true});
    space.set(Space.NAME, jsonSpace.name, {silent: true});
    space.set(Space.LABEL, jsonSpace.label, {silent: true});    
    space.set(Space.LOGO, jsonSpace.logo, {silent: true});
    space.set(Space.URL, jsonSpace.url, {silent: true});
    space.set(Space.DATAWAREHOUSE, jsonSpace.datawarehouse, {silent: true});    
    
    if (jsonSpace.model_version) {
      var modelVersion = ModelVersionSerializer.unserialize(jsonSpace.model_version);    
      if (! modelVersion) {
        space.set(Space.MODEL_VERSION, modelVersion, {silent: true});
      }
    }
    
    var state = new State();
    state.set(State.RUNNING, jsonSpace.state.running, {silent : true});
    state.set(State.TIME, jsonSpace.state.time, {silent : true});

    space.set(Space.STATE, state, {silent: true});
    
    if (jsonSpace.federation) {
      var jsonFederation = jsonSpace.federation;
      var jsonServer = jsonFederation.server;
      
      var server = new Server();
      server.id = jsonServer.id;
      server.set(Server.NAME, jsonServer.name, {silent: true});
      server.set(Server.IP, jsonServer.ip, {silent: true});
      
      var federation = new Federation();
      federation.id = jsonFederation.id;
      federation.set(Federation.NAME, jsonFederation.name, {silent: true});
      federation.set(Federation.SERVER, server, {silent: true});
      space.set(Space.FEDERATION, federation, {silent: true});
    }
    
    if (jsonSpace.services) {
      var services = new Collection();
      
      for (var i=0; i < jsonSpace.services.length; i++) {
     	var jsonService = jsonSpace.services[i];
        var service = new PublicationService();        
        service.set(PublicationService.NAME, jsonService.name, {silent: true});
        service.set(PublicationService.TYPE, jsonService.type, {silent: true});
        service.set(PublicationService.PUBLISHED, jsonService.published, {silent: true});
        
        services.add(service, {silent: true});    	  
      }
      space.set(Space.SERVICES, services, {silent: true});
    }
    
    return space;
  }
};