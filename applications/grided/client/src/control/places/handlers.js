ChangePlaceEventHandlers = {};
ChangePlaceEventHandlers.handle = function(event) {
  History.newLocation(event.token, event.data);
  
};

//------------------------------------------------------------------------------------
//FederationSavedHandler = {};
//FederationSavedHandler.handle = function(event) {
//   var activity = ActivityMapper.get(DeploymentPlace);
//   activity.handle(event);	
//};

//------------------------------------------------------------------------------------
ServerSavedHandler = {};
ServerSavedHandler.handle = function(event) {  
   var activity = ActivityMapper.get(ServersPlace);
   activity.handle(event);	
};

//------------------------------------------------------------------------------------
//SpaceSavedHandler = {};
//SpaceSavedHandler.handle = function(event) {
//  var activity = ActivityMapper.get(DeploymentPlace);  
//  activity.handle(event);	
//};
