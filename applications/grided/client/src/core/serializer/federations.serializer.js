FederationsSerializer = {
  serialize : function() {},
  
  unserialize : function(jsonFederations) {
	var federations = new Federations();
	
	  for (var i in jsonFederations) {
	    if (_.isFunction(jsonFederations[i])) continue;
	    federation = new Federation();
	    var jsonFederation = jsonFederations[i];
	  
	    federation.id = jsonFederation.id;	  
	    federation.set(Federation.NAME, jsonFederation.name, {silent: true});
	    federation.set(Federation.LABEL, jsonFederation.label, {silent: true});
	  
	    var jsonState = jsonFederation.state;
	    var state = new State();
	    state.set(State.RUNNING, jsonState.running, {silent : true});
	    state.set(State.TIME, jsonState.time, {silent : true});    
	    federation.set(Federation.STATE, state, {silent: true});	  
	    federations.add(federation, {silent: true});
	  }	
	return federations;
  }
};