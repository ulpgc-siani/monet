
var Controller = {};
Controller.defaultLocation = new ServersPlace().toString();

Controller.init = function() {
  Controller._initHistory();
  Controller._initRouter();
  Controller._initActivityMapper();
  Controller._initEventBus();
  Controller._initNotificationManager();
    
  var data = {};  
  var location = (History.getLocation() != "")? History.getLocation() : Controller.defaultLocation;
  data.id = LocationParser.parse(location);      

  var event = {token: location, data: data};
  ChangePlaceEventHandlers.handle(event);  
};

//-----------------------------------------------------------------------------------
Controller.onChangeHistory = function(token ,data) {
  var place = Router.get(token);
  var activity = ActivityMapper.get(place);
  ActivityManager.start(activity, data);  
  EventBus.fire({name: Events.CHANGE_PLACE, data: place});
};

//-----------------------------------------------------------------------------------
Controller._initHistory = function() {
  History.init();
  History.addListener(Controller.onChangeHistory);   
};

//-----------------------------------------------------------------------------------
Controller._initRouter = function() {
  Router.addRoute(/home\/?$/, HomePlace);  
  Router.addRoute(/servers\/\d+$/, ServerPlace);
  Router.addRoute(/servers\/?$/, ServersPlace);
  Router.addRoute(/\\*models\/?$/, ModelsPlace);
  Router.addRoute(/\\*models\/\d+$/, ModelPlace);
  Router.addRoute(/deployment\/?$/, DeploymentPlace);
  Router.addRoute(/\\*federations\/\d+$/, FederationPlace);  
  Router.addRoute(/\\*spaces\/\d+$/, SpacePlace);    
};

//-----------------------------------------------------------------------------------
Controller._initActivityMapper = function() {
  ActivityMapper.register(HomePlace, HomeActivity);
  ActivityMapper.register(ServersPlace, ServersActivity);
  ActivityMapper.register(ServerPlace, ServerActivity);
  ActivityMapper.register(DeploymentPlace, DeploymentActivity);
  ActivityMapper.register(FederationPlace, FederationActivity);
  ActivityMapper.register(SpacePlace, SpaceActivity);  
  ActivityMapper.register(ModelsPlace, ModelsActivity);
  ActivityMapper.register(ModelPlace, ModelActivity);
};

//-----------------------------------------------------------------------------------
Controller._initEventBus = function() {  
  EventBus.on(Events.OPEN_HOME, ChangePlaceEventHandlers.handle);
  EventBus.on(Events.OPEN_SERVERS, ChangePlaceEventHandlers.handle);
  EventBus.on(Events.OPEN_SERVER, ChangePlaceEventHandlers.handle);
  EventBus.on(Events.OPEN_MODELS, ChangePlaceEventHandlers.handle);
  EventBus.on(Events.OPEN_MODEL, ChangePlaceEventHandlers.handle);
  EventBus.on(Events.OPEN_DEPLOYMENT, ChangePlaceEventHandlers.handle);
  EventBus.on(Events.OPEN_FEDERATION, ChangePlaceEventHandlers.handle);  
  EventBus.on(Events.OPEN_SPACE, ChangePlaceEventHandlers.handle);
  
  EventBus.on(Events.CHANGE_PLACE, Desktop.handle);
      
  EventBus.on(Events.SERVER_SAVED, ServerSavedHandler.handle);
};

//-----------------------------------------------------------------------------------
Controller._initNotificationManager = function() {  
  NotificationManager.init(Ids.Elements.NOTIFICATIONS_BAR);
  NotificationManager.hideNotification();
};