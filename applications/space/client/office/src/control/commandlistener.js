CommandListener = new Object;
CommandListener.onUnload = null;
CommandListener.onUnload = null;

CommandListener.start = function (Dispatcher) {
  this.Dispatcher = Dispatcher;
  Event.observe(document.body, 'keyup', CommandListener.atCommandKeyUp.bind(CommandListener));
  Event.observe(window, 'beforeunload', CommandListener.atBeforeUnload.bind(CommandListener));
  Event.observe(window, 'unload', CommandListener.atUnload.bind(CommandListener));
};

CommandListener.stop = function () {
  this.Dispatcher = null;
};

CommandListener.capture = function (DOMElement) {
  if (!this.Dispatcher) return;

  var extElement = Ext.get(DOMElement);
  if (!extElement) return;

  if (extElement.hasClass("command")) Event.observe(extElement.dom, 'click', CommandListener.atCommandClick.bind(CommandListener, extElement.dom));

  var extCommandList = extElement.select(".command");
  extCommandList.each(function (extCommand) {
    DOMCommand = extCommand.dom;
    Event.observe(DOMCommand, 'click', CommandListener.atCommandClick.bind(CommandListener, DOMCommand));
  });

  extCommandList = extElement.select(".changecommand");
  extCommandList.each(function (extCommand) {
    DOMCommand = extCommand.dom;
    Event.observe(DOMCommand, 'change', CommandListener.atCommandChange.bind(CommandListener, DOMCommand));
  });

  extCommandList = extElement.select(".hover");
  extCommandList.each(function (extCommand) {
    DOMCommand = extCommand.dom;
    Event.observe(DOMCommand, 'mouseover', CommandListener.atCommandHover.bind(CommandListener, DOMCommand));
  });

};

CommandListener.throwCommand = function (sCommand) {
  this.Dispatcher.execute(sCommand, null);
};

CommandListener.dispatchCommand = function (sCommand) {
  this.Dispatcher.dispatch(sCommand, null);
};

CommandListener.getCommand = function (sCommand) {
  var Expression = new RegExp("\\([^\\)]*\\)");
  var aResult = Expression.exec(sCommand);

  if (aResult != null && aResult.length > 0) {
    sCommand = sCommand.replace(aResult[0], "#command#");
  }

  sCommand = sCommand.substr(sCommand.lastIndexOf("/") + 1);
  if (aResult != null && aResult.length > 0) sCommand = sCommand.replace("#command#", aResult[0]);

  return sCommand;
};

CommandListener.atCommandKeyUp = function (EventLaunched) {
  if (EventLaunched.keyCode != 13) return false;

  var extForm = Ext.get(EventLaunched.target);
  if (!extForm) return false;

  var extForm = extForm.up("form");
  if (!extForm) return false;

  DOMForm = extForm.dom;

  var sCommand = DOMForm.action;
  if (sCommand != null) this.Dispatcher.dispatch(sCommand, DOMForm);
  if (EventLaunched) Event.stop(EventLaunched);

  return false;
};

CommandListener.atBeforeUnload = function (EventLaunched) {
  if (CommandListener.onUnload) {
    var sResult = CommandListener.onUnload();
    if ((sResult != "") && (sResult != null)) {
      if (typeof EventLaunched == 'undefined') {
        EventLaunched = window.event;
      }
      if (EventLaunched) {
        EventLaunched.returnValue = sResult;
      }
      return sResult;
    }
  }
};

CommandListener.atUnload = function (EventLaunched) {
  if (CommandListener.onUnload) {
    CommandListener.onUnload();
  }
};

CommandListener.atCommandClick = function (DOMItem, EventLaunched) {
  CommandListener.dispatchAnchorCommand(DOMItem);
  if (EventLaunched) Event.stop(EventLaunched);
  return false;
};

CommandListener.atCommandChange = function (DOMItem, EventLaunched) {
  var sCommand = null;

  if (DOMItem.href) sCommand = DOMItem.href;
  else if (DOMItem.name) sCommand = DOMItem.name;
  else if (DOMItem.value) sCommand = DOMItem.value;

  if (sCommand != null) this.Dispatcher.dispatch(sCommand, DOMItem);
  if (EventLaunched) Event.stop(EventLaunched);

  return false;
};

CommandListener.atCommandHover = function (DOMItem) {
  CommandListener.dispatchAnchorCommand(DOMItem);
};

CommandListener.dispatchAnchorCommand = function(DOMItem) {
  var sCommand = null;

  if (DOMItem.href) sCommand = DOMItem.href;
  else if (DOMItem.hasClassName("button")) sCommand = DOMItem.name;
  else sCommand = DOMItem.value;

  sCommand = CommandListener.getCommand(sCommand);
  if (sCommand == null)
    return;

  this.Dispatcher.dispatch(sCommand, DOMItem);
};
