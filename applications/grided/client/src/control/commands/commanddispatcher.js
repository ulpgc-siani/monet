CommandDispatcher = new Object();
CommandDispatcher.History = new Object();

CommandDispatcher.dispatch = function(sCommand, DOMItem) {
  if (DOMItem && (DOMItem.id == null || DOMItem.id == "")) DOMItem.id = Ext.id();
  if (CommandFactory.useHistory(sCommand))
    this.History.addCommand(sCommand, (DOMItem)?DOMItem.id:null);
  else
    this.execute(sCommand, DOMItem);
};

CommandDispatcher.execute = function(sCommand, DOMItem) {
  try {
    var Action = CommandFactory.getAction(sCommand);
    if (Action == null) return;
    Action.DOMItem = DOMItem;
    Action.execute();
    return false;
  }
  catch(e){ 
    InternalException(e,Object.inspect(this));
    return false;
  }
};

CommandDispatcher.History.addCommand = function(sCommand, idItem){
  frames[0].window.location = Context.Pages.History + "?command=" + sCommand + "&item=" + idItem;
};

CommandDispatcher.History.executeCommand = function(){
  if (!Application.isRunning) return;
  var DOMItem, aLink, sLink = frames[0].window.location.href;
  var iPos = sLink.indexOf("?command=");
  if (iPos == -1) return;
  sLink = sLink.substring(iPos+String("?command=").length);
  aLink = sLink.split("&item=");
  DOMItem = Ext.get(aLink[1]);
  CommandDispatcher.execute(aLink[0], DOMItem);
};