function CGStubAjax(Mode, idMessageLayer) {
  this.base = CGStub;
  this.base(Mode, idMessageLayer);
  this.title = parent.document.title;
};

CGStubAjax.prototype = new CGStub;

CGStubAjax.prototype.isSessionExpired = function (sMessage) {
  var sInitialChars;

  sInitialChars = sMessage.substr(0, SERVER_USER_NOT_LOGGED.length);
  if (sInitialChars.toLowerCase() == SERVER_USER_NOT_LOGGED) return true;

  sInitialChars = sMessage.substr(0, SERVER_SESSION_EXPIRES.length);
  if (sInitialChars.toLowerCase() == SERVER_SESSION_EXPIRES) return true;

  return false;
};

CGStubAjax.prototype.isBusinessUnitStopped = function (sMessage) {
  var sInitialChars;

  sInitialChars = sMessage.substr(0, SERVER_BUSINESSUNIT_STOPPED.length);
  if (sInitialChars.toLowerCase() == SERVER_BUSINESSUNIT_STOPPED) return true;

  return false;
};

CGStubAjax.prototype.existServerError = function (sMessage) {
  var sInitialChars = sMessage.substr(0, SERVER_ERROR_PREFIX.length);
  return (sInitialChars.toLowerCase() == SERVER_ERROR_PREFIX);
};

CGStubAjax.prototype.getErrorMessage = function (sMessage) {
  var iPos = sMessage.indexOf(":");
  return sMessage.substring(iPos + 1);
};

CGStubAjax.prototype.getTimeZone = function () {
  var dateVar = new Date();
  return dateVar.getTimezoneOffset() / 60 * (-1);
};

CGStubAjax.prototype.request = function (Action, sOperation, aParameters, bCheckConnection) {
  this.showLoading();
  if (bCheckConnection == null) bCheckConnection = true;
  Ext.Ajax.method = "POST";
  Ext.Ajax.url = Context.Config.Api;
  Ext.Ajax.request({
    params: writeServerRequest(this.Mode, "op=" + sOperation + "&sender=ajax&timezone=" + this.getTimeZone() + ((aParameters != null) ? serializeParameters(aParameters) : "")),
    callback: CGStubAjax.prototype.atRequestResponse.bind(this, Action, sOperation, aParameters, bCheckConnection)
  }, this);
};

CGStubAjax.prototype.atRequestResponse = function (Action, sOperation, aParameters, bCheckConnection, sOptions, bSuccess, Response) {
  if (bSuccess) {

    if ((bCheckConnection) && (Response.responseText == "")) {
      this.ping(Action, sOperation, aParameters);
      return;
    }

    Action.data = readServerResponse(this.Mode, Response.responseText);
    if ((this.isSessionExpired(Action.data)) || (this.isBusinessUnitStopped(Action.data))) {
      if (this.isSessionExpired(Action.data)) alert(Lang.Exceptions.SessionExpired);
      if (this.isBusinessUnitStopped(Action.data)) alert(Lang.Exceptions.BusinessUnitStopped);
      window.location = window.location + (location.indexOf("?") != -1 ? "&" : "?") + "m=" + Math.random();
      parent.document.title = this.title;
      return;
    }
    this.hideLoading();
    try {
      if (Action.onFailure && this.existServerError(Response.responseText)) Action.onFailure(this.getErrorMessage(Response.responseText));
      else if (Action.onSuccess) Action.onSuccess();
    }
    catch (e) {
      Ext.MessageBox.alert(Lang.Exceptions.Title, e.message);
    }
  }
  else {
    this.hideLoading();
    try {
      if (Action.onFailure && this.existServerError(Response.responseText)) Action.onFailure(this.getErrorMessage(Response.responseText));
      else RequestException(sOperation, Response, sOptions);
    }
    catch (e) {
      RequestException(sOperation, Response, sOptions);
    }
    Desktop.hideReports();
    Desktop.hideProgress();
  }
};

CGStubAjax.prototype.ping = function (Action, sOperation, aParameters) {
  this.hideConnectionFailure();
  Ext.Ajax.method = "POST";
  Ext.Ajax.url = Context.Config.Api;
  Ext.Ajax.request({
    params: writeServerRequest(this.Mode, "op=ping&sender=ajax&timezone=" + this.getTimeZone()),
    callback: CGStubAjax.prototype.atPingResponse.bind(this, Action, sOperation, aParameters)
  }, this);
};

CGStubAjax.prototype.atPingResponse = function (Action, sOperation, aParameters, sOptions, bSuccess, Response) {
  if (bSuccess) {
    if (Response.responseText == "") {
      this.showConnectionFailure();
      window.setTimeout(CGStubAjax.prototype.ping.bind(this, Action, sOperation, aParameters), 5000);
    }
    else {
      this.hideConnectionFailure();
      this.request(Action, sOperation, aParameters, false);
    }
  }
  else {
    this.showConnectionFailure();
    window.setTimeout(CGStubAjax.prototype.ping.bind(this, Action, sOperation, aParameters), 5000);
  }
};

CGStubAjax.prototype.zoombieRequest = function (sOperation, aParameters) {
  this.showLoading();
  Ext.Ajax.method = "POST";
  Ext.Ajax.url = Context.Config.Api;
  Ext.Ajax.request({
    params: writeServerRequest(this.Mode, "op=" + sOperation + "&sender=ajax&timezone=" + this.getTimeZone() + ((aParameters != null) ? serializeParameters(aParameters) : "")),
    callback: CGStubAjax.prototype.atZoombieRequestResponse.bind(this)
  });
};

CGStubAjax.prototype.atZoombieRequestResponse = function (sOptions, bSuccess, Response) {
  if (bSuccess) {
    var sResponse = readServerResponse(this.Mode, Response.responseText);
    if ((this.isSessionExpired(sResponse)) || (this.isBusinessUnitStopped(sResponse))) {
      if (this.isSessionExpired(sResponse)) alert(Lang.Exceptions.SessionExpired);
      if (this.isBusinessUnitStopped(sResponse)) alert(Lang.Exceptions.BusinessUnitStopped);
      window.location = window.location + (location.indexOf("?") != -1 ? "&" : "?") + "m=" + Math.random();
      return;
    }
    this.hideLoading();
  }
  else this.hideLoading();
};

CGStubAjax.prototype.upload = function (Action, sOperation, DOMForm) {
  this.showLoading();
  var iPos = DOMForm.action.indexOf("?");

  DOMForm.action = Context.Config.Api + (iPos != -1 ? "&" : "?") + "op=" + sOperation + "&sender=ajax&timezone=" + this.getTimeZone();

  Ext.Ajax.method = "POST";
  Ext.Ajax.url = DOMForm.action;
  Ext.Ajax.on("requestexception", RequestException);
  Ext.Ajax.request({
    callback: CGStubAjax.prototype.atUploadResponse.bind(this, Action),
    form: DOMForm,
    isUpload: true
  });
};

CGStubAjax.prototype.atUploadResponse = function (Action, sOptions, bSuccess, Response) {
  if (bSuccess) {
    Action.data = readServerResponse(this.Mode, Response.responseText);
    if ((this.isSessionExpired(Action.data)) || (this.isBusinessUnitStopped(Action.data))) {
      if (this.isSessionExpired(Action.data)) alert(Lang.Exceptions.SessionExpired);
      if (this.isBusinessUnitStopped(Action.data)) alert(Lang.Exceptions.BusinessUnitStopped);
      window.location = window.location + (location.indexOf("?") != -1 ? "&" : "?") + "m=" + Math.random();
      return;
    }
    this.hideLoading();
    try {
      if (Action.onFailure && this.existServerError(Response.responseText)) Action.onFailure(Response.responseText);
      else if (Action.onSuccess) Action.onSuccess();
    }
    catch (e) {
      Ext.MessageBox.alert(Lang.Exceptions.Title, e.message);
    }
  }
  else {
    this.hideLoading();
    RequestException(sOperation, Response, sOptions);
    Desktop.hideReports();
    Desktop.hideProgress();
  }
};