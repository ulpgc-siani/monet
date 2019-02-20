SERVER_ERROR_PREFIX = "err_";
SERVER_SESSION_EXPIRES = "err_session_expires";
SERVER_USER_NOT_LOGGED = "err_user_not_logged";
SERVER_BUSINESSUNIT_STOPPED = "err_businessunit_stopped";

function StubAjax(Mode, idMessageLayer) {
  this.base = Stub;
  this.base(Mode, idMessageLayer);
  this.title = parent.document.title;
};

StubAjax.prototype = new Stub;

StubAjax.prototype.isSessionExpired = function(message) {
  var initialChars;
    
  initialChars = message.substr(0,SERVER_USER_NOT_LOGGED.length);
  if (initialChars.toLowerCase() == SERVER_USER_NOT_LOGGED) return true;

  initialChars = message.substr(0,SERVER_SESSION_EXPIRES.length);
  if (initialChars.toLowerCase() == SERVER_SESSION_EXPIRES) return true;

  return false;
};

StubAjax.prototype.isBusinessUnitStopped = function(message) {
  var initialChars;
    
  initialChars = message.substr(0,SERVER_BUSINESSUNIT_STOPPED.length);
  if (initialChars.toLowerCase() == SERVER_BUSINESSUNIT_STOPPED) return true;

  return false;
};

StubAjax.prototype.existServerError = function(message){
  var initialChars = message.substr(0,SERVER_ERROR_PREFIX.length);
  return (initialChars.toLowerCase() == SERVER_ERROR_PREFIX);
};

StubAjax.prototype.getErrorMessage = function(message){
  var pos = message.indexOf(":");
  return message.substring(pos+1);
};

StubAjax.prototype.getTimeZone = function() {
  var dateVar = new Date();
  return dateVar.getTimezoneOffset()/60 * (-1);
};

StubAjax.prototype.request = function(action, operation, parameters, checkConnection) {
  //Desktop.showLoading();
  
  if (checkConnection == null) checkConnection = true;
  
  $.ajax({
    url: Context.Config.Url + writeServerRequest(this.mode, "op=" + operation + "&sender=ajax&timezone=" + this.getTimeZone() + ((parameters != null)?serializeParameters(parameters):"")),
    complete: StubAjax.prototype.atRequestResponse.bind(this, action, operation, parameters, checkConnection)
  });
};

StubAjax.prototype.atRequestResponse = function(action, operation, parameters, checkConnection, response, state) {
  if (state == "success") {
  
    if ((checkConnection) && (response.responseText == "")) {
      this.ping(action, operation, parameters);
      return;
    }

    action.data = readServerResponse(this.mode, response.responseText);
    if ((this.isSessionExpired(action.data)) || (this.isBusinessUnitStopped(action.data))) {
      if (this.isSessionExpired(action.data)) alert(Lang.SessionExpired);
      if (this.isBusinessUnitStopped(action.data)) alert(Lang.BusinessUnitStopped);
      window.location = window.location + (location.indexOf("?")!=-1?"&":"?") + "m=" + Math.random();
      parent.document.title = this.title;
      return;
    }
    //Desktop.hideLoading();
    try {
      if (action.onFailure && this.existServerError(response.responseText)) action.onFailure(this.getErrorMessage(response.responseText));
      else if (action.onSuccess) action.onSuccess();
    }
    catch(e) { 
      alert(e.message); 
    }
  }
  else {
    //Desktop.hideLoading();
    RequestException(operation, response);
    Desktop.hideReports();
    Desktop.hideProgress();
  }
};

StubAjax.prototype.ping = function(action, operation, parameters) {
  this.hideConnectionFailure();
  $.ajax({
    url: Context.Config.Url + writeServerRequest(this.mode, "op=ping&sender=ajax&timezone=" + this.getTimeZone()),
    complete: StubAjax.prototype.atPingResponse.bind(this, action, operation, parameters)
  }, this);
};

StubAjax.prototype.atPingResponse = function(action, operation, parameters, response, state) {
  if (state == "success") {
    if (response.responseText == "") {
      this.showConnectionFailure();
      window.setTimeout(StubAjax.prototype.ping.bind(this, action, operation, parameters), 5000);
    }
    else {
      this.hideConnectionFailure();
      this.request(action, operation, parameters, false);
    }
  }
  else {
    this.showConnectionFailure();
    window.setTimeout(StubAjax.prototype.ping.bind(this, action, operation, parameters), 5000);
  }
};

StubAjax.prototype.zoombieRequest = function(operation, parameters) {
  //Desktop.showLoading();
  $.ajax({
    url: Context.Config.Url + writeServerRequest(this.mode, "op=" + operation + "&sender=ajax&timezone=" + this.getTimeZone() + ((parameters != null)?serializeParameters(parameters):"")),
    complete: StubAjax.prototype.atZoombieRequestResponse.bind(this)
  });
};

StubAjax.prototype.atZoombieRequestResponse = function(response, state) {
  if (state == "success") {
    var serverResponse = readServerResponse(this.mode, response.responseText);
    if ((this.isSessionExpired(serverResponse)) || (this.isBusinessUnitStopped(serverResponse))) {
      if (this.isSessionExpired(serverResponse)) alert(Lang.SessionExpired);
      if (this.isBusinessUnitStopped(serverResponse)) alert(Lang.BusinessUnitStopped);
      window.location = window.location + (location.indexOf("?")!=-1?"&":"?") + "m=" + Math.random();
      return;
    }
    //Desktop.hideLoading();
  }
  //else Desktop.hideLoading();
};