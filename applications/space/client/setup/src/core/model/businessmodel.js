
BusinessModel = new Object;
BusinessModel.init = function() {
  BusinessModel.code  = "";	  
  BusinessModel.label = "";
  BusinessModel.version = "";
  BusinessModel.images = {};
};
BusinessModel.init();
BusinessModel.listeners = new Array();

//---------------------------------------------------------------------
BusinessModel.registerListener = function(listener) {
  if (! BusinessModel.hasListener(listener))	
    BusinessModel.listeners.push(listener);
};

//---------------------------------------------------------------------
BusinessModel.unRegisterListener = function(listener) {
	BusinessModel.listeners.remove(listener);
};

//---------------------------------------------------------------------
BusinessModel.hasListener = function(listener) {
  return BusinessModel.listeners.indexOf(listener) != -1;
};
  
// PRIVATE
//---------------------------------------------------------------------
BusinessModel.notifyListeners = function() {
  for (var i=0; i < BusinessModel.listeners.length; i++) {
	  BusinessModel.listeners[i].notify(BusinessModel);
  }
};

