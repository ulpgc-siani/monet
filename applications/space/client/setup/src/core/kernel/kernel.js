Kernel = new Object;

Kernel.init = function() {
};

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
Kernel.isSessionExpired = function(sMessage) {
  var sInitialChars;
    
  sInitialChars = sMessage.substr(0,SERVER_USER_NOT_LOGGED.length);
  if (sInitialChars.toLowerCase() == SERVER_USER_NOT_LOGGED) return true;

  sInitialChars = sMessage.substr(0,SERVER_SESSION_EXPIRES.length);
  if (sInitialChars.toLowerCase() == SERVER_SESSION_EXPIRES) return true;

  return false;
};

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
Kernel.isServiceStopped = function(sMessage) {
  var sInitialChars;
    
  sInitialChars = sMessage.substr(0,SERVER_SERVICE_STOPPED.length);
  if (sInitialChars.toLowerCase() == SERVER_SERVICE_STOPPED) return true;

  return false;
};

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
Kernel.existServerError = function(sMessage){
  var sInitialChars = sMessage.substr(0,SERVER_ERROR_PREFIX.length);
  return (sInitialChars.toLowerCase() == SERVER_ERROR_PREFIX);
};

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
Kernel.prepareRequest = function (Action, sOperation, sParameters) {
  Desktop.showLoading();
  
  Ext.Ajax.method = "POST";
  Ext.Ajax.url = Context.Config.Api;
  Ext.Ajax.request({
    params: "op=" + sOperation + "&sender=ajax" + ((sParameters != null)?"&" + sParameters:""),
    success: function(response,options) {
       Desktop.hideLoading();	
    	
      if ((Kernel.isSessionExpired(response.responseText)) || (Kernel.isServiceStopped(response.responseText))) {
        if (Kernel.isSessionExpired(response.responseText)) alert(Lang.Exceptions.SessionExpired);
        if (Kernel.isServiceStopped(response.responseText)) alert(Lang.Exceptions.ServiceStopped);
        window.location = location;
        return;
      }
      
      Action.data = response.responseText;
      try {
        if (Action.onFailure && Kernel.existServerError(response.responseText)) Action.onFailure(response.responseText);
        else if (Action.onSuccess) Action.onSuccess();
      }
      catch(e) { 
        Ext.MessageBox.alert(Lang.Exceptions.Title, e.message); 
      }
    },
    
    failure: function(response,options){
      Desktop.hideLoading();
      RequestException(sOperation, response, options);
    },
    scope: Action
  });
};

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
Kernel.prepareUpload = function (Action, sOperation, aParameters, DOMForm) {
  DOMForm.action = Context.Config.Api;
  addInput(DOMForm, "op", sOperation);

  for (var sName in aParameters) {
    if (isFunction(aParameters[sName])) continue;
    addInput(DOMForm, sName, aParameters[sName]);
  }

  Ext.Ajax.method = "POST";
  Ext.Ajax.url = Context.Config.Api + "?op=" + sOperation;
  Ext.Ajax.on("requestexception", RequestException);
  
  Desktop.showLoading();
  
  Ext.Ajax.request({
    success: function(response,options){
      if ((Kernel.isSessionExpired(response.responseText)) || (Kernel.isServiceStopped(response.responseText))) {
      if (Kernel.isSessionExpired(response.responseText)) alert(Lang.Exceptions.SessionExpired);
      if (Kernel.isServiceStopped(response.responseText)) alert(Lang.Exceptions.ServiceStopped);
        window.location = location;
      return;
      }
      Desktop.hideLoading();
      Action.data = response.responseText;
      try {
        if (Action.onFailure && Kernel.existServerError(response.responseText)) Action.onFailure(response.responseText);
        else if (Action.onSuccess) Action.onSuccess();
      }
      catch(e){ 
        Ext.MessageBox.alert(Lang.Exceptions.Title, e.message); 
      }
    },
    failure: function(response,options){
      Desktop.hideLoading();	
      RequestException(sOperation, response, options);
    },
    form : DOMForm,
    isUpload : true,
    scope: Action
  });
};

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
Kernel.loadBusinessModel = function(Action) {
  Kernel.prepareRequest(Action, "loadbusinessmodel");
};

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
Kernel.uploadBusinessModel = function(Action, DOMForm) {
  DOMForm.enctype = "multipart/form-data";  
  Kernel.prepareUpload(Action, "updatebusinessmodel", [], DOMForm);
};

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
Kernel.updateBusinessModel = function(Action, DOMForm) {
  DOMForm.enctype = "multipart/form-data";
  Kernel.prepareUpload(Action, "updatebusinessmodel", [], DOMForm);
};

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
Kernel.loadBusinessSpace = function(Action) {
  Kernel.prepareRequest(Action, "loadbusinessspace");
};

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
Kernel.uploadBusinessSpace = function(Action, DOMForm) {
  DOMForm.enctype = "multipart/form-data";
  Kernel.prepareUpload(Action, "updatebusinessspace", [], DOMForm);
};
//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
Kernel.updateBusinessSpace = function(Action, DOMForm) {
  DOMForm.enctype = "multipart/form-data";
  Kernel.prepareUpload(Action, "updatebusinessspace", [], DOMForm);
};

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
Kernel.loadServices = function(Action) {
  Kernel.prepareRequest(Action, "loadservices");
};

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
Kernel.loadThesaurus = function(Action) {
  Kernel.prepareRequest(Action, "loadthesaurus");
};

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
Kernel.loadThesaurusList = function(Action) {
  Kernel.prepareRequest(Action, "loadthesauruslist");
};

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
Kernel.loadThesaurusProviders = function(Action) {  
  Kernel.prepareRequest(Action, "loadthesaurusproviders");
};

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
Kernel.loadMaps = function(Action) {
  Kernel.prepareRequest(Action, "loadmaps");
};

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
Kernel.loadServiceProviders = function(Action) {  
  Kernel.prepareRequest(Action, "loadserviceproviders");
};

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
Kernel.loadMapProviders = function(Action) {  
  Kernel.prepareRequest(Action, "loadmapproviders");
};

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
Kernel.runBusinessSpace = function(Action) {
  Kernel.prepareRequest(Action, "runbusinessspace");  
};

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
Kernel.stopBusinessSpace = function(Action) {
  Kernel.prepareRequest(Action, "stopbusinessspace");
};

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
Kernel.getLoadThesaurusUrl = function() {
  return Context.Config.Api + "?op=loadthesaurus";
};

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
Kernel.startImportData = function(Action, DOMForm) {
  DOMForm.enctype = "multipart/form-data";
  Kernel.prepareUpload(Action, "startimportdata", [], DOMForm);
};

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
Kernel.stopImportData = function(Action) {  
  Kernel.prepareRequest(Action, "stopimportdata");
};

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
Kernel.getLoadEventLogListUrl = function(Action) {
  return Context.Config.Api + "?op=loadeventloglist";
};

//-------------------------------------------------------------------------------------------------------------------------------------------------------------------
Kernel.logout = function(Action) {
  Kernel.prepareRequest(Action, "logout");
};