FederationSerializer = {
		
  serialize : function(federation) {
    var json = {};
    json.id    = federation.id;
    json.name  = federation.get(Federation.NAME);
    json.label = federation.get(Federation.LABEL);
    json.logo  = federation.get(Federation.LOGO);
    json.url   = federation.get(Federation.URL);

    var state = federation.get(Federation.STATE);
    var jsonState = {};
    jsonState.running = state.get(State.RUNNING);
    jsonState.time    = state.get(State.TIME);
    
    json.state = jsonState;       
        
    var server = federation.get(Federation.SERVER);
    var jsonServer = {};
    jsonServer.id = server.id;
    jsonServer.name = server.get(Server.NAME);
    jsonServer.ip = server.get(Server.IP);
    
    json.server = jsonServer;
    
    json.user_auth        = federation.get(Federation.USER_AUTH);
    json.certificate_auth = federation.get(Federation.CERTIFICATE_AUTH);
    json.openid_auth      = federation.get(Federation.OPENID_AUTH);
    
    var connection = null;
    
    switch (federation.get(Federation.CONNECTION).get(Federation.CONNECTION_TYPE)) {
      case ConnectionTypes.DATABASE: connection = this.serializeDatabaseConnection(federation.get(Federation.CONNECTION));  break;
      case ConnectionTypes.LDAP: connection     = this.serializeLDAPConnection(federation.get(Federation.CONNECTION));  break;
      case ConnectionTypes.DATABASE: connection = this.serializeMockConnection(federation.get(Federation.CONNECTION));  break;    
    } 
    
    json.connection = connection;
    
    var jsonSpaces = SpacesSerializer.serialize(federation.get(Federation.SPACES));			
	json.spaces = jsonSpaces;
	
	var text = jsonText = Ext.util.JSON.encode(json);
	return text;
  },
  
  serializeDatabaseConnection : function(connection) {
    var jsonConnection = {};
    
    jsonConnection.type = connection.get(Federation.CONNECTION_TYPE);	
    jsonConnection.url = connection.get(DatabaseConnection.URL);	
    jsonConnection.user = connection.get(DatabaseConnection.USER);	
    jsonConnection.password = connection.get(DatabaseConnection.PASSWORD);
    
    var config = connection.get(DatabaseConnection.CONFIG);
    
    jsonConnection.config = {};
    jsonConnection.config.database_type = config.get(DatabaseConnectionConfig.DATABASE_TYPE);    
    
    return jsonConnection;
  },
  
  serializerLDAPConnection : function(connection) {
    var jsonConnection = {};
    jsonConnection.type = connection.get(Federation.CONNECTION_TYPE);	
    jsonConnection.url = connection.get(LDAPConnection.URL);	
    jsonConnection.user = connection.get(LDAPConnection.USER);	
    jsonConnection.password = connection.get(LDAPConnection.PASSWORD);
    
    var config = connection.get(LDAPConnection.CONFIG);
    
    jsonConnection.config = {};
    jsonConnection.config.schema = config.get(LDAPConnectionConfig.SCHEMA);
    
    var cnFieldParameter    = config.get(LDAPConnectionConfig.CN);
    var uidFieldParameter   = config.get(LDAPConnectionConfig.UID);
    var emailFieldParameter = config.get(LDAPConnectionConfig.EMAIL);
    var langFieldParameter  = config.get(LDAPConnectionConfig.LANG);
            
    jsonConnection.config.parameters = [cnFieldParameter, uidFieldParameter, emailFieldParameter, langFieldParameter];
     
    return jsonConnection;
  },
  
  serializerMockConnection : function(connection) {
	var jsonConnection = {};  
    jsonConnection.type = connection.get(Federation.CONNECTION_TYPE);	
    jsonConnection.url = connection.get(Federation.URL);	
    jsonConnection.user = connection.get(Federation.USER);	
    jsonConnection.password = connection.get(Federation.PASSWORD);
      
    return jsonConnection;    
  },
  
  
  unserialize : function(jsonFederation) {
	var federation = new Federation();
	
	federation.id = jsonFederation.id;
	federation.set(Federation.NAME, jsonFederation.name, {silent: true});
	federation.set(Federation.LABEL, jsonFederation.label, {silent: true});
	federation.set(Federation.LOGO, jsonFederation.logo, {silent: true});
	federation.set(Federation.URL, jsonFederation.url, {silent: true});
	
  var state = new State();
  state.set(State.RUNNING, jsonFederation.state.running, {silent : true});
  state.set(State.TIME, jsonFederation.state.time, {silent : true});	
  federation.set(Federation.STATE, jsonFederation.state, {silent: true});
  
	if (jsonFederation.server) {
	  var jsonServer = jsonFederation.server;
	  
	  var server = new Server();
	  server.id = jsonServer.id;
	  server.set(Server.NAME, jsonServer.name);
	  server.set(Server.IP, jsonServer.ip);    	  
	  federation.set(Federation.SERVER, server, {silent: true});
	}	  
  
	if (jsonFederation.authentication) {	  
	  federation.set(Federation.USER_AUTH, jsonFederation.authentication.user_auth, {silent: true});
	  federation.set(Federation.CERTIFICATE_AUTH, jsonFederation.authentication.certificate_auth, {silent: true});
	  federation.set(Federation.OPENID_AUTH, jsonFederation.authentication.openid_auth, {silent: true});	  
	}
	
	if (jsonFederation.connection) {
		var connection = null;
		
		switch (jsonFederation.connection.type) {
		  case 'database':
		    connection = this.unSerializeDatabaseConnection(jsonFederation.connection);    	  
		  break;
		  case 'ldap':
		    connection = this.unSerializeLDAPConnection(jsonFederation.connection);	   
		  break;
		  case 'mock':
            connection = this.unSerializeMockConnection(jsonFederation.connection);			  
		  break;
		}
		federation.set(Federation.CONNECTION, connection, {silent: true});
	}
	   	
	if (jsonFederation.spaces) {
		var spaces = SpacesSerializer.unserialize(jsonFederation.spaces);			
		federation.set(Federation.SPACES, spaces, {silent: true});
	}
	
	return federation;
  },
  
  unSerializeDatabaseConnection : function(jsonConnection) {
    var connection = new DatabaseConnection();
    connection.set(Federation.CONNECTION_TYPE, jsonConnection.type);
    connection.set(DatabaseConnection.URL, jsonConnection.url);
    connection.set(DatabaseConnection.USER, jsonConnection.user);
    connection.set(DatabaseConnection.PASSWORD, jsonConnection.password);
    
    if (jsonConnection.config) {
    	var jsonConfig = jsonConnection.config;
    	var config = new DatabaseConnectionConfig();
    	config.set(DatabaseConnectionConfig.DATABASE_TYPE, jsonConfig.database_type);    	
    	connection.set(DatabaseConnection.CONFIG, config, {silent: true});    	
    } 
    return connection;
  },
  
  unSerializeLDAPConnection : function(jsonConnection) {
    var connection = new LDAPConnection();
    connection.set(Federation.CONNECTION_TYPE, jsonConnection.type, {silent: true});
    connection.set(LDAPConnection.URL, jsonConnection.url, {silent: true});    
    connection.set(LDAPConnection.USER, jsonConnection.user, {silent: true});
    connection.set(LDAPConnection.PASSWORD, jsonConnection.password, {silent: true});
    
    if (jsConnection.config) {
    	var jsonConfig = jsonConnection.config;
    	var config = new LDAPConnectionConfig();
    	
    	config.set(LDAPConnectionConfig.SCHEMA, jsonConfig.schema, {silent: true});
    	        
        for (var i=0; i < jsonConfig.parameters.length; i++) {
    	  var parameter = jsonConfig.parameters[i];
    	  
    	  switch (parameter.name) {
    	    case LDAPAlias.CN:    config.set(LDAPConnectionConfig.CN, parameter, {silent: true}); break;
    	    case LDAPAlias.UID:   config.set(LDAPConnectionConfig.UID, parameter, {silent: true}); break;
    	    case LDAPAlias.EMAIL: config.set(LDAPConnectionConfig.EMAIL, parameter, {silent: true}); break;
    	    case LDAPAlias.LANG:  config.set(LDAPConnectionConfig.LANG, parameter, {silent: true}); break;
    	  };    	
        }
        
        connection.set(LDAPConnection.CONFIG, config, {silent: true});
    }
        
    return connection;    
  },
  
  unSerializeMockConnection : function() {
    var connection = new MockConnection();
    connection.set(Federation.CONNECTION_TYPE, jsonConnection.type);
    connection.set(LDAPConnection.URL, jsonConnection.url);    
    connection.set(LDAPConnection.USER, jsonConnection.user);
    connection.set(LDAPConnection.PASSWORD, jsonConnection.password);
    
    return connection;
  }
};