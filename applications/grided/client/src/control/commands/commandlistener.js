CommandListener = new Object;

CommandListener.start = function(Dispatcher) {
  this.Dispatcher = Dispatcher;
  Event.observe(document.body, 'keyup', CommandListener.atCommandKeyUp.bind(CommandListener));
};

CommandListener.stop = function() {
  this.Dispatcher = null;
};

CommandListener.capture = function(DOMElement){
  if (!this.Dispatcher) return;

  var extElement = Ext.get(DOMElement);
  if (! extElement) return;

  extCommandList = extElement.select(".command");
  extCommandList.each(function(extCommand) { 
    DOMCommand = extCommand.dom;
    Event.observe(DOMCommand, 'click', CommandListener.atCommandClick.bind(CommandListener, DOMCommand));
  });

  extCommandList = extElement.select(".changecommand");
  extCommandList.each(function(extCommand) { 
    DOMCommand = extCommand.dom;
    Event.observe(DOMCommand, 'change', CommandListener.atCommandChange.bind(CommandListener, DOMCommand));
  });

};

CommandListener.throwCommand = function(sCommand) {
  this.Dispatcher.execute(sCommand, null);
};

CommandListener.atCommandKeyUp = function(EventLaunched){
  if (EventLaunched.keyCode != 13) return;

  var extForm = Ext.get(EventLaunched.target);
  if (!extForm) return;

  var extForm = extForm.up("form");
  if (!extForm) return;

  DOMForm = extForm.dom;

  var sCommand = DOMForm.action;
  if (sCommand != null) this.Dispatcher.dispatch(sCommand, DOMForm);
  if (EventLaunched) Event.stop(EventLaunched);

  return false;
};

CommandListener.atCommandClick = function(DOMItem, EventLaunched){
  var sCommand = null;

  if (DOMItem.href) sCommand = DOMItem.href;
  else if (DOMItem.hasClassName("button")) sCommand = DOMItem.name;
  else sCommand = DOMItem.value;

  sCommand = sCommand.substr(sCommand.lastIndexOf("/")+1);

  if (EventLaunched) Event.stop(EventLaunched);
  if (sCommand != null) this.Dispatcher.dispatch(sCommand, DOMItem);

  return false;
};

CommandListener.atCommandChange = function(DOMItem, EventLaunched){
  var sCommand = null;

  if (DOMItem.href) sCommand = DOMItem.href;
  else if (DOMItem.value) sCommand = DOMItem.value;

  if (sCommand != null) this.Dispatcher.dispatch(sCommand, DOMItem);
  if (EventLaunched) Event.stop(EventLaunched);
  return false;
  if ((EventLaunched) && (!DOMItem.value)) Event.stop(EventLaunched);
};
