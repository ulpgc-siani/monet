SpacesSerializer = {
    
    serialize: function(spaces) {
      var jsonSpaces = [];
      
      for (var i=0; i < spaces.size(); i++) {
        var jsonSpace = {};
        var space = spaces.get(i);
        jsonSpace.id = space.id;
        jsonSpace.name = space.get(Space.NAME);
                
        var state = space.get(Space.STATE);
        var jsonState = {};
        jsonState.running = state.get(State.RUNNING);
        jsonState.time = state.get(State.TIME);
        
        jsonSpace.state = jsonState;
        
        if (space.get(Space.MODEL_VERSION)) {
          var modelVersion = space.get(Space.MODEL_VERSION);        
          var jsonModelVersion = {};
          jsonModelVersion.id = modelVersion.get(MODEL_VERSION.ID);
          jsonModelVersion.label = modelVersion.get(MODEL_VERSION.LABEL);
         
          jsonSpace.model_version = jsonModelVersion;         
        }
        
        jsonSpaces.push(jsonSpace);
      }

      var text = Ext.util.JSON.encode(jsonSpaces);
      return text;
    },

    unserialize: function(jsonSpaces) {
      var spaces = new Spaces();

      for (var i=0; i < jsonSpaces.length; i++)  {
        if (_.isFunction(jsonSpaces[i])) continue;

        var jsonSpace = jsonSpaces[i];

        var space = new Space();
        space.set(Space.ID, jsonSpace.id, {silent: true});
        space.set(Space.NAME, jsonSpace.name, {silent: true});
        
        if (jsonSpace.model_version) {
          var jsonModelVersion = jsonSpace.model_version;
          var modelVersion = new ModelVersion();
          modelVersion.id = jsonModelVersion.id;
          modelVersion.set(ModelVersion.LABEL, jsonModelVersion.label, {silent: true});
          modelVersion.set(ModelVersion.METAMODEL, jsonModelVersion.metamodel, {silent: true});
        
          space.set(Space.MODEL_VERSION, modelVersion, {silent : true});
        }
        
        var jsonState = jsonSpace.state;
        var state = new State();
        state.set(State.RUNNING, jsonState.running, {silent : true});
        state.set(State.TIME, jsonState.time, {silent : true});    
        space.set(Space.STATE, state, {silent: true});    

        if (jsonSpace.federation) {
          var jsonServer = jsonSpace.federation.server;
          var server = new Server();
          server.id = jsonServer.id;
          server.set(Server.NAME, jsonServer.name, {silent: true});

          var jsonFederation = jsonSpace.federation;
          var federation = new Federation();
          federation.id = jsonFederation.id;
          federation.set(Federation.NAME, jsonFederation.name, {silent: true});
          federation.set(Federation.SERVER, server, {silent: true});

          space.set(Space.FEDERATION, federation, {silent: true});
        }

        spaces.add(space, {silent: true});
      }  
      return spaces;
    }
};