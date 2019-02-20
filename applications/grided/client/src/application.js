
var Context = {};
Context.Config = {};
Context.Config.Layer = {};
Context.Pages = {};

//needed by rsh.js library
dhtmlHistory.create({
  toJSON: function(o) {
    var result = Ext.util.JSON.encode(o);
    return result;
  }, 
  fromJSON: function(s) {
    var result = Ext.util.JSON.decode(s);
    return result;
  }
});



var Application = new Object;
Application.listeners = [];

Application.init = function() {
  if (!console)  console = { log : function(){} };
 
  readData(Context, Ext.get(Literals.Data));
  
  var loggingUrl = Context.Config.Api + "?op=savelog";
  var responder = function(caller, ex) { 
    DefaultExceptionHandler.handleError(ex);
    if (ex.type == Literals.SERVER_ERROR) Ajax.Responders.unregister(this); 
  };
  
  Ajax.Responders.register({ onException : responder });
  
  DefaultExceptionHandler.init(Literals.Dialogs.Exception, loggingUrl);
  
  Desktop.init();
  Controller.init();
};

Ext.onReady(
  function() {
    Ext.QuickTips.init();
    Ext.enableListenerCollection = true;
    
    try {    	           
      Application.init();
       
    } catch (ex) {
      console.log(ex);
      ErrorHandler.handleError(ex);            
    }
  }
);