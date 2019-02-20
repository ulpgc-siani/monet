function RequestException (conn,response,options){
  var extDialog, extTemplate;
  
  if (Ext.MessageBox.isVisible()) Ext.MessageBox.hide();
  
  extDialog = new Ext.BasicDialog(Literals.Dialogs.Exception,{width:500,height:400,resizable:false});
  extTemplate = new Ext.Template(Lang.Exceptions.Request.Description + 'url: {url}<br/>' + 'params: {params}<br/>' +'status: {status}<br/>' + 'response: {response}');
                            
  extDialog.body.dom.innerHTML = extTemplate.applyTemplate({url:options.url ? options.url : Ext.Ajax.url, params:options.params, status:response.status + " " + response.statusText, response:response.responseText});

  extDialog.setTitle(Lang.Exceptions.Request.Title);
  extDialog.addButton(Lang.Buttons.Close, CloseException.bind(this, extDialog));
  extDialog.center();
  extDialog.show();
}

function InternalException (e,dOp,dState){
  var extDialog, extTemplate;

  if (Ext.MessageBox.isVisible()) Ext.MessageBox.hide();
    
  extDialog = new Ext.BasicDialog(Literals.Dialogs.Exception,{width:500,height:400,resizable:false});
  extTemplate = new Ext.Template(Lang.Exceptions.Internal.Description + 'operation: {op}<br/>' +'state: {state}<br/>' + 'name: {name}<br/>' + 'message: {message}<br/>');
    
  extDialog.body.dom.innerHTML = extTemplate.applyTemplate({op: dOp, state: dState, name: e.name, message: e.message});
    
  extDialog.setTitle(Lang.Exceptions.Internal.Title);
  extDialog.addButton(Lang.Buttons.Close, CloseException.bind(this, extDialog));
  extDialog.center();
  extDialog.show();
}

function CloseException(extDialog) {
  if (! extDialog) return;
  extDialog.hide();
  extDialog.destroy();
}

function printStackTrace(e) {
	var callstack = [];
  var isCallstackPopulated = false;
  try {
    i.dont.exist+=0; //doesn't exist- that's the point
  } catch(e) {
    if (e.stack) { //Firefox
      var lines = e.stack.split('\n');
      for (var i=0, len=lines.length; i<len; i++) {
        if (lines[i].match(/^\s*[A-Za-z0-9\-_\$]+\(/)) {
          callstack.push(lines[i]);
        }
      }
      //Remove call to printStackTrace()
      callstack.shift();
      isCallstackPopulated = true;
    }
    else if (window.opera && e.message) { //Opera
      var lines = e.message.split('\n');
      for (var i=0, len=lines.length; i < len; i++) {
        if (lines[i].match(/^\s*[A-Za-z0-9\-_\$]+\(/)) {
          var entry = lines[i];
          //Append next line also since it has the file info
          if (lines[i+1]) {
            entry += ' at ' + lines[i+1];
            i++;
          }
          callstack.push(entry);
        }
      }
      //Remove call to printStackTrace()
      callstack.shift();
      isCallstackPopulated = true;
    }
  }
  if (!isCallstackPopulated) { //IE and Safari
    var currentFunction = arguments.callee.caller;
    while (currentFunction) {
      var fn = currentFunction.toString();
      var fname = fn.substring(fn.indexOf('function') + 8, fn.indexOf('')) || 'anonymous';
      callstack.push(fname);
      currentFunction = currentFunction.caller;
    }
  }
  output(callstack);
}
 
function output(arr) {
  console.log(arr.join('\n\n'));	
}