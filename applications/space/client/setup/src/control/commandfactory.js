CommandFactory = new Object;
CommandFactory.aList = new Array();

CommandFactory.register = function(ClassAction, aParameters, bUseHistory) {
  if (!ClassAction) return;
  if (!ClassAction.prototype.constructor) return;
  var sClassName = this.getClassName(ClassAction);
  var sOperation = String(sClassName.substring(String("GGAction").length)).toLowerCase();
  if (aParameters == null) aParameters = new Object;
  this.aList[sOperation] = {
    ClassAction: ClassAction,
    aParameters: aParameters,
    bUseHistory: bUseHistory
  };
};

CommandFactory.getClassName = function(ClassAction) {
  var data = ClassAction.constructor.toString().match(/function\s*(\w+)/);
  if (data && data.length == 2) return data[1];
};

CommandFactory.getAction = function(sCommand){
  var Command = this.parseCommand(sCommand);
  var Item = this.aList[Command.sOperation];
  if (Item == null) return;
  var Action = new Item.ClassAction;
  Action.ClassName = this.getClassName(Item.ClassAction);
  for (var index in Command.aParameters)
    Action[index] = Command.aParameters[index];
  return Action;
};

CommandFactory.useHistory = function(sCommand) {
  var Command = this.parseCommand(sCommand);
  var Item = this.aList[Command.sOperation];
  if (Item == null) return;
  return Item.bUseHistory;
};

CommandFactory.parseCommand = function(sCommand) {
  var Command = new Object;

  sCommand = sCommand.replace(/%28/g, LEFT_BRACKET);
  sCommand = sCommand.replace(/%29/g, RIGHT_BRACKET);

  while ((iPos = sCommand.indexOf(SLASH)) != -1) {
    sCommand = sCommand.substring(iPos+1, sCommand.length);
  }

  if (sCommand.indexOf(LEFT_BRACKET) == -1) {
    Command.sOperation = sCommand;
    return Command;
  }

  var reg = new RegExp(/(.*)\(([^\)]*)/g);
  var aResult = reg.exec(sCommand);

  Command.sOperation = aResult[1];
  if (aResult[2] != EMPTY) {
    aResult = aResult[2].split(',');
    var Item = this.aList[Command.sOperation];
    Command.aParameters = new Object;
    for (var key in Item.aParameters) {
      var index = Item.aParameters[key];
      Command.aParameters[key] = aResult[index];
    }
  }
  return Command;
};