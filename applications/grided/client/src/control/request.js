function Request(config) {
  this.config = config;  
}

Request.prototype.send = function() {  	
  if (this.config.method == 'get') this._sendGetRequest();
  else this._sendPostRequest();    	 
};

//----------------------------------------------------------------------------------------
Request.prototype._successHandler = function(res, req) {  
  var responseObject = (res.responseText != "")? Ext.util.JSON.decode(res.responseText) : null;
  if (! responseObject) this._failureHandler(res, req);
  if (responseObject.type == Literals.ERROR) this._failureHandler(res, req);
  else this.config.callback.success.call(this.config.callback.context, responseObject.data);  
};

//----------------------------------------------------------------------------------------
Request.prototype._failureHandler = function(res, req) {   	
  var responseObject = (res.responseText != "")? Ext.util.JSON.decode(res.responseText) : null;
  if (responseObject == null) throw {type : Literals.SERVER_ERROR};
  
  var exception = responseObject;    
  this.config.callback.failure.call(this.config.callback.context, exception);
};

//----------------------------------------------------------------------------------------
Request.prototype._sendPostRequest = function() {
  Ext.Ajax.request({
	url : this.config.url,  
    method: this.config.method,
    params : this.config.params,
	form  : this.config.form || null,
	isUpload : this.config.isUpload || false,
	scope : this,
	success : this._successHandler,
	failure : this._failureHandler	
  });	
};

//----------------------------------------------------------------------------------------
Request.prototype._sendGetRequest = function() {
  Ext.Ajax.request({
	url: this.config.url,
	method: this.config.method,
	params : this.config.params,
	scope : this,	
	success : this._successHandler,
	failure : this._failureHandler	
  });	
};