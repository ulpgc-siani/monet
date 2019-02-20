ServerSerializer = {
		
  serialize: function(server) {
	  var json = {id: + server.id, name: server.get(Server.NAME), ip: server.get(Server.IP), enabled : server.get(Server.ENABLED)};
	  jsonText = Ext.util.JSON.encode(json);		 
	  return jsonText;
  },
  
  unserialize: function(jsonServer) {
    var server = new Server();
    server.set(Server.ID, jsonServer.id, {silent: true});
    server.set(Server.NAME, jsonServer.name, {silent: true});
    server.set(Server.IP, jsonServer.ip, {silent: true});
    server.set(Server.ENABLED, jsonServer.enabled, {silent: true});
        
    var jsonFederations = jsonServer.federations;
    var federations = new Collection();
    
    if (jsonFederations) {
      for (var i=0, l = jsonFederations.length; i < l; i++) {
    	var jsonFederation = jsonFederations[i];  
        var federation = new Federation();
        federation.id = jsonFederation.id;
        federation.set(Federation.NAME, jsonFederation.name, {silent: true});
        federations.add(federation, {silent: true});
      }        
      server.set(Server.FEDERATIONS, federations, {silent: true});
    }
    
    var jsonServerState = jsonServer.server_state;  
    
    var serverState = ServerStateSerializer.unserialize(jsonServerState);
    
//    var serverState = new ServerState();
//    
//    var jsonPerformance = jsonServerState.performance;
//    var jsonTask   = jsonServerState.task;
//    var jsonMemory = jsonServerState.memory;
//    var jsonSwap   = jsonServerState.swap;
//    
//    serverState.set(ServerState.LOG, jsonServerState.log);
//    serverState.set(ServerState.PERFORMANCE, {load : jsonPerformance.load, cpu : jsonPerformance.cpu, users : jsonPerformance.users});
//    serverState.set(ServerState.TASK, {total : jsonTask.total, execution : jsonTask.execution, sleeped : jsonTask.sleeped, stopped : jsonTask.stopped, zoombies : jsonTask.zoombies});
//    serverState.set(ServerState.MEMORY, {total : jsonMemory.total, used : jsonMemory.used, available : jsonMemory.available});
//    serverState.set(ServerState.SWAP, {total : jsonSwap.total, used: jsonSwap.used, available : jsonSwap.available});
    
    server.set(Server.SERVER_STATE, serverState, {silent: true});
    
    return server;
  }
};