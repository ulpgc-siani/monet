var ExceptionHandler = new Object(); 

//-------------------------------------------------------------------------------------------
ExceptionHandler.handle = function(ex) {
  var message = Lang.Exceptions[ex.code];
  var elementId = Literals.Dialogs.Exception;
  this.showMessage(elementId, message);
}; 

//-------------------------------------------------------------------------------------------
ExceptionHandler.showMessage = function(elementId, message) {
  var extDialog = new Ext.BasicDialog(elementId, {width:300,height:200,resizable:false});
  
  extDialog.body.dom.innerHTML = message;      
  extDialog.setTitle(Lang.Exceptions.Internal.Title);
  extDialog.addButton(Lang.Buttons.close, function() { extDialog.hide();});
  extDialog.center();
  extDialog.show();         
};



//---------------------------------------------------------------------------------------------
//TODO refactorizar el handler

DefaultExceptionHandler = function() {
	var containerId = '';
	var serverUrl = '';
	
	return {

	  init: function(containerId, serverUrl) {
			this.containerId = containerId;
			this.serverUrl = serverUrl;
			window.onerror = !window.onerror ? DefaultExceptionHandler.handleError : window.onerror.createSequence(DefaultExceptionHandler.handleError);
		},
		
		handleError:  function() {
		  var args = [];
	  	try {
  		    for (var x = 0; x < arguments.length; x++) {
			      args[x] = arguments[x];
			    }
  		    var message = this.getFormattedMessage(args);
  		    message = message.replace(/</g, "&lt;").replace(/>/g, "&gt;");
			    message = message.replace(/\n/g, "<br />").replace(/\t/g, " &nbsp; &nbsp;"),
			
		      this.displayError(message);
			    this.logToServer(message);
		  } catch(e) {
		    console.log("errorHandler is broken");
			  return false;
		  }
		  return true;
		},
					
		displayError: function(message) {
			var extDialog = new Ext.BasicDialog(this.containerId, {width:500,height:400,resizable:false});			
			
			extDialog.body.dom.innerHTML = message;
			    
			extDialog.setTitle(Lang.Exceptions.Internal.Title);
			extDialog.addButton(Lang.Buttons.close, CloseException.bind(this, extDialog));
			extDialog.center();
			extDialog.show();			  
		},
		
		logToServer: function(message) {
			var request = new Request({
			  url : this.serverUrl,
			  params: {message: message},
			  callback : {success: function() {return false;}, failure: function() {return false;}}
			});
			request.send();			
		},
					
		getFormattedMessage: function(args) {
			var lines = ["The following error has occured:"];
			if (args[0] instanceof Error) { // Error object thrown in try...catch
				var err = args[0];
				lines[lines.length] = "Message: (" + err.name + ") " + err.message;
				lines[lines.length] = "Error number: " + (err.number & 0xFFFF); //Apply binary arithmetic for IE number, firefox returns message string in element array element 0
				lines[lines.length] = "Description: " + err.description;
			} else if ((args.length == 3) && (typeof(args[2]) == "number")) { // Check the signature for a match with an unhandled exception
				lines[lines.length] = "Message: " + args[0];
				lines[lines.length] = "URL: " + args[1];
				lines[lines.length] = "Line Number: " + args[2];
			} else {
				lines = ["An unknown error has occured."]; // purposely rebuild lines
				lines[lines.length] = "The following information may be useful:";
				for (var x = 0; x < args.length; x++) {
					lines[lines.length] = Ext.encode(args[x]);
				}
			}
			return lines.join("\n");
		},
	};
}();

// the following line ensures that the handleError method always executes in the scope of DefaultExceptionHandler
DefaultExceptionHandler.handleError = DefaultExceptionHandler.handleError.createDelegate(DefaultExceptionHandler);