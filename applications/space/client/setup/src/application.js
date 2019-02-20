
var Context = new Object;
Context.Config = new Object;
Context.Config.Layer = new Object;
Context.Pages = new Object;

var Application = new Object;
Application.listeners = [];
Application.Dialogs = new Array();
Application.States = {
  INITIAL: 0,		
  STOPPED : 1,
  RUNNING: 2,
  IMPORTING: 3,
  UPLOADING_MODEL: 4,
  UPLOADING_SPACE: 5
};


Application.Dialogs = {};
Application.setState = function(state) {Application.state = state; Application.notifyListeners();};
Application.getState = function() { return Application.state; };
Application.hasListener = function(listener) {return Application.listeners.indexOf(listener) != -1;};

Application.registerListener = function(listener) {
  if (! Application.hasListener(listener)) {
    Application.listeners.push(listener);
  }
};
Application.unRegisterListener = function(listener) {
  Application.listeners.remove(listener);
};

Application.notifyListeners = function(listener) {
  for (var i=0; i < Application.listeners.length; i++) {
    Application.listeners[i].changeApplicationState(Application.state);
  }
};

Application.init = function() {
  readData(Context, Ext.get(Literals.Data));
  Ext.BLANK_IMAGE_URL = Context.Config.ImagesPath + "/s.gif";
        
  CommandListener.start(CommandDispatcher);    
  Application.setState(Application.States.STOPPED);  
  Desktop.init(Context.Config.Layer.Name);  
  Application.initialLoad();
  
  this.isRunning = true;
};

//--------------------------------------------------------------------------
Application.executeInitAction = function() {
  Desktop.Bottom.init();
  Desktop.hideLoading();
  
  Application.registerListener(Desktop.Bottom);
    
  var action = new CGActionInit();
  action.execute();      
};

//--------------------------------------------------------------------------
Application.initialLoad = function() {
  	  
  var successHandler = function(response, request) {
    if (response.responseText.indexOf("ERR_") != -1) { Desktop.hideLoading(); dialog.show(); }
    Desktop.hideLoading();
    if ((Kernel.isSessionExpired(response.responseText)) || (Kernel.isServiceStopped(response.responseText))) {
      if (Kernel.isSessionExpired(response.responseText)) alert(Lang.Exceptions.SessionExpired);
      if (Kernel.isServiceStopped(response.responseText)) alert(Lang.Exceptions.ServiceStopped);
      Kernel.logout(); //window.location = location;
      return;
    }
    var resp = Ext.util.JSON.decode(response.responseText);
    if (resp.type == "error") dialog.show();
    else {
      Application.executeInitAction();
      dialog.hide();
      
    }
  };
  
  var failureHandler = function(response, request) {
   	if (! dialog.isVisible()) dialog.show();  
  };	
	  
  var resultHandler = function() {	          	
    dialog.submit();		
  };

  var dialog = new WizardDialogUpdateSpace(Literals.Dialogs.UpdateSpaceWizard);
  dialog.on('result', resultHandler, Application);
  Application.Dialogs[Literals.Dialogs.UpdateSpaceWizard] = dialog;
    
  Ext.Ajax.request({
    url: Context.Config.Api,
    method: 'get',
    params : {op: 'existsbusinessspace'},
    success : successHandler.bind(this),
    failure : failureHandler.bind(this),
  });
};

//--------------------------------------------------------------------------
Application.loadMonetVersion = function() {
  return "2";
};

Ext.onReady(
  function(){
    Ext.QuickTips.init();
    Ext.enableListenerCollection = true;
    Application.init();
  }
);