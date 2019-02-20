CommandListener = new Object;
CommandListener.onUnload = null;

CommandListener.start = function(Dispatcher) {
  this.Dispatcher = Dispatcher;
  $(document.body).bind("keyup", CommandListener.atCommandKey);
  $(window).bind("beforeunload", CommandListener.atBeforeUnload);
  $(window).bind("unload", CommandListener.atUnload);
};

CommandListener.stop = function() {
  this.Dispatcher = null;
};

CommandListener.capture = function(DOMElement){
  if (!this.Dispatcher) return;

  var jElement = $(DOMElement);
  if (! jElement) return;

  if (jElement.hasClass("command")) jElement.bind('click', CommandListener.atCommandClick.bind(CommandListener, DOMElement));

  jCommandList = jElement.find(".command");
  jCommandList.each(function(index, DOMCommand) { 
    $(DOMCommand).click(CommandListener.atCommandClick.bind(CommandListener, DOMCommand));
  });
};

CommandListener.throwCommand = function(command) {
  this.Dispatcher.execute(command, null);
};

CommandListener.dispatchCommand = function(command) {
  this.Dispatcher.dispatch(command, null);
};

CommandListener.getCommand = function(command) {
  var Expression = new RegExp("\\([^\\)]*\\)");
  var resultArray = Expression.exec(command);
  
  if (resultArray != null && resultArray.length > 0)
    command = command.replace(resultArray[0], "#command#");

  command = command.substr(command.lastIndexOf("/")+1);
  if (resultArray != null && resultArray.length > 0) command = command.replace("#command#", resultArray[0]);
  
  return command;
};

CommandListener.atCommandKeyUp = function(event){
  if (event.keyCode != 13) return false;

  var jForm = $(event.target);
  if (!jForm) return false;

  jForm = jForm.parents("form:first").eq(0);
  if (!jForm) return false;

  DOMForm = jForm.get(0);

  var command = DOMForm.action;
  if (command != null) this.Dispatcher.dispatch(command, DOMForm);
  if (event) Event.stop(event);

  return false;
};

CommandListener.atBeforeUnload = function(event){
  if (CommandListener.onUnload) {
    var result = CommandListener.onUnload();
    if ((result != "") && (result != null)) {
      if (typeof event == 'undefined') {
        event = window.event;
      }
      if (event) {
        event.returnValue = result;
      }
      return result;
    }
  }
};

CommandListener.atUnload = function(event){
  if (CommandListener.onUnload)
    CommandListener.onUnload();
};

CommandListener.atCommandClick = function(DOMItem, event){
  var command = null;
  var jItem = $(DOMItem);
  
  if (DOMItem.href) command = DOMItem.href;
  else if (jItem.hasClass("button")) command = DOMItem.name;
  else command = DOMItem.value;

  command = CommandListener.getCommand(command);

  if (command != null) this.Dispatcher.dispatch(command, DOMItem);
  if (event) event.stopPropagation();
  
  return false;
};