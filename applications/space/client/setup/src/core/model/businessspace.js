
BusinessSpace = new Object;
BusinessSpace.STOP = 0;
BusinessSpace.RUNNING = 1;

BusinessSpace.id = "";
BusinessSpace.label = "";
BusinessSpace.organization = {name: '', server: ''};
BusinessSpace.update = "";
BusinessSpace.imageUrl = "";
BusinessSpace.image = "";

BusinessSpace.executionContext = {
   state : BusinessSpace.STOP,
   date  : null 
};

BusinessSpace.listeners = new Array();

//---------------------------------------------------------------------
BusinessSpace.setExecutionContext = function(context) {
  BusinessSpace.executionContext = context;
};

//---------------------------------------------------------------------
BusinessSpace.getExecutionContext = function(context) {
  return BusinessSpace.executionContext;
};
	
//---------------------------------------------------------------------
BusinessSpace.isRunning = function() {
  return BusinessSpace.executionContext.state === BusinessSpace.RUNNING; 
};

//---------------------------------------------------------------------
BusinessSpace.registerListener = function(listener) {
  if (! BusinessSpace.hasListener())
    BusinessSpace.listeners.push(listener);
};

//---------------------------------------------------------------------
BusinessSpace.unRegisterListener = function(listener) {
  BusinessSpace.listeners.remove(listener);
};

//---------------------------------------------------------------------
BusinessSpace.hasListener = function(listener) {
  return BusinessSpace.listeners.indexOf(listener) != -1;
};

// PRIVATE
//---------------------------------------------------------------------
BusinessSpace.notifyListeners = function() {
  for (var i=0; i < BusinessSpace.listeners.length; i++) {
  	BusinessSpace.listeners[i].notify(BusinessSpace);
  }
};
