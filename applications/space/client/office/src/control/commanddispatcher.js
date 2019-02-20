CommandDispatcher = new Object();
CommandDispatcher.History = new Object();

CommandDispatcher.mainTitle = " - " + parent.document.title;

CommandDispatcher.dispatch = function (sCommand, DOMItem) {
  var CommandInfo = new CGCommandInfo(sCommand);
  var sOperation = CommandInfo.getOperation();
  var aParameters = CommandInfo.getParameters();
  var bUseHistory = true;

  if (DOMItem && (DOMItem.id == null || DOMItem.id == "")) DOMItem.id = Ext.id();

  if ((sOperation == "shownode" && State.LastCommand.Name == "shownode" && aParameters[0] == State.LastCommand.Id) ||
      (sOperation == "showchildfield" && State.LastCommand.Name == "showchildfield" && aParameters[0] == State.LastCommand.Id) ||
      (sOperation == "showfield" && State.LastCommand.Name == "showfield" && aParameters[0] == State.LastCommand.Id)) {
    bUseHistory = false;
  }

  State.LastCommand.Name = sOperation;
  State.LastCommand.Id = aParameters[0];

  if (bUseHistory && CommandFactory.useHistory(sCommand))
    this.History.addCommand(sCommand, DOMItem);
  else
    this.execute(sCommand, DOMItem);
};

CommandDispatcher.execute = function (sCommand, DOMItem, args) {

  try {
    var Action = CommandFactory.getAction(sCommand);
    if (Action == null) return;
    Action.DOMItem = DOMItem;
    if (args) {
      for (var index in args)
        Action[index] = args[index];
    }
    Action.onTerminate = function () {
      parent.document.title = Desktop.Main.Center.Body.PageControl.getActivePage().title + CommandDispatcher.mainTitle;
    };
    Action.execute();
  }
  catch (e) {
    InternalException(e, Object.inspect(this));
    Desktop.hideProgress();
    Desktop.hideReports();
  }
};

CommandDispatcher.History.addCommand = function (sCommand, DOMItem) {
  var DOMFrame = $(Literals.Frames.History);
  var sCommand = Context.Pages.History + "?command=" + sCommand + "&item=" + ((DOMItem) ? DOMItem.id : null);
  DOMFrame.monetCommand = sCommand;
  frames[Literals.Frames.History].location = sCommand;
};

CommandDispatcher.History.executeCommand = function () {
  if (!Application.isRunning) return;
  var DOMFrame = $(Literals.Frames.History);
  var sLink = frames[Literals.Frames.History].location.href;
  var DOMItem, aLink;
  var iPos;

  if (sLink == null) sLink = DOMFrame.monetCommand;

  iPos = sLink.indexOf("?command=");
  if (iPos == -1) return;

  sLink = sLink.substring(iPos + String("?command=").length);
  aLink = sLink.split("&item=");
  DOMItem = $(aLink[1]);
  CommandDispatcher.execute(aLink[0], DOMItem);
};

function executeMonetCommand(sCommand) {
  CommandDispatcher.execute(sCommand, null);
};