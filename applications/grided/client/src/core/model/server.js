var Server = Model.extend({
  init: function() {
    this._super();
    this.federations = new Federations();
  }
});

// Property names
Server.ID = 'id';
Server.NAME  = 'name';
Server.IP = 'ip';
Server.FEDERATIONS = 'federations',
Server.STATE = 'state';
Server.ENABLED = 'enabled';
Server.SERVER_STATE = "server_state";



var ServerState = Model.extend({
  init : function() {
    this._super();
  }
});

ServerState.PERFORMANCE = 'performance';
ServerState.TASK   = 'task';
ServerState.MEMORY = 'memory';
ServerState.SWAP   = 'swap',
ServerState.LOG    = 'log';