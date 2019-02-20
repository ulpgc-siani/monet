CommandDispatcher = new Object();
CommandDispatcher.History = new Object();

CommandDispatcher.mainTitle = " - " + parent.document.title;

CommandDispatcher.dispatch = function(command, DOMItem) {
  var commandInfo = new CommandInfo(command);
  var operation = commandInfo.getOperation();
  var parameters = commandInfo.getParameters();
  var useHistory = true;
  
  if (DOMItem && (DOMItem.id == null || DOMItem.id == "")) DOMItem.id = generateId();

  State.lastCommand.Name = operation;
  State.lastCommand.Id = parameters[0];

  if (useHistory && CommandFactory.useHistory(command))
    this.History.addCommand(command, DOMItem);
  else
    this.execute(command, DOMItem);
};

CommandDispatcher.execute = function(command, DOMItem, args) {

  try {
    var Action = CommandFactory.getAction(command);
    if (Action == null) return;
    Action.DOMItem = DOMItem;
    if(args) {
      for(var index in args)
        Action[index] = args[index];
    }
    Action.onTerminate = function() {
      //parent.document.title = Desktop.Main.Center.Body.PageControl.getActivePage().title + CommandDispatcher.mainTitle; 
    };
    Action.execute();
  }
  catch(e){ 
    InternalException(e,Object.inspect(this));
    Desktop.hideProgress();
    Desktop.hideReports();
  }
};

CommandDispatcher.History.addCommand = function(command, DOMItem){
  var DOMFrame = $("#history");
  var monetCommand = Context.Pages.History + "?command=" + command + "&item=" + ((DOMItem)?DOMItem.id:null);
  DOMFrame.monetCommand = monetCommand;
  frames["history"].location = monetCommand;
};

CommandDispatcher.History.executeCommand = function(){
  if (!Application.isRunning) return;
  var DOMFrame = $("#history");
  var link = frames["history"].location.href;
  var DOMItem, linkArray;
  var pos;
  
  if (link == null) link = DOMFrame.monetCommand;

  pos = link.indexOf("?command=");
  if (pos == -1) return;

  link = link.substring(pos+String("?command=").length);
  linkArray = link.split("&item=");
  DOMItem = $(linkArray[1]);
  CommandDispatcher.execute(linkArray[0], DOMItem);
};

function executeMonetCommand(command) {
  CommandDispatcher.execute(command, null);
};