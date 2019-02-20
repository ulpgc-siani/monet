ServersSerializer = {
		
  serialize: function(server) {
	  
  },
  
  unserialize: function(jsonServers) {
    var servers = new Servers();
    
	for (var i=0; i < jsonServers.length; i++)  {
	  if (_.isFunction(jsonServers[i])) continue;
	  
	  var jsonServer = jsonServers[i];
	  var server = new Server();
	  server.set(Server.ID, jsonServer.id, {silent: true});
	  server.set(Server.NAME, jsonServer.name, {silent: true});    		  		
	  servers.add(server, {silent: true});
	}  
    return servers;
  }
};