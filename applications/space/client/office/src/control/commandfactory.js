CommandFactory = new Object;
CommandFactory.aList = new Array();

CommandFactory.register = function (ClassAction, aParameters, bUseHistory) {
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

CommandFactory.getClassName = function (ClassAction) {
  var data = ClassAction.constructor.toString().match(/function\s*(\w+)/);
  if (data && data.length == 2) return data[1];
};

CommandFactory.getAction = function (sCommand) {
  var Command = this.parseCommand(sCommand);
  var Item = this.aList[Command.sOperation];
  if (Item == null) return null;
  var Action = new Item.ClassAction;
  Action.ClassName = this.getClassName(Item.ClassAction);
  for (var index in Command.aParameters) Action[index] = Command.aParameters[index];
  Action.OptionalParameters = Command.aOptionalParameters;
  return Action;
};

CommandFactory.useHistory = function (sCommand) {
  var Command = this.parseCommand(sCommand);
  var Item = this.aList[Command.sOperation];
  if (Item == null) return;
  return Item.bUseHistory;
};

CommandFactory.parseCommand = function (sCommand) {
  var Command = new Object;

  sCommand = sCommand.replace(/%28/g, LEFT_BRACKET);
  sCommand = sCommand.replace(/%29/g, RIGHT_BRACKET);

  while ((iPos = sCommand.indexOf(SLASH)) != -1) {
    var iBracketPos = sCommand.indexOf(LEFT_BRACKET);
    if (iPos < iBracketPos) sCommand = sCommand.substring(iPos + 1, sCommand.length);
    else sCommand = sCommand.substring(0, iPos, sCommand.length) + "#@@@@#" + sCommand.substring(iPos + 1, sCommand.length);
  }
  sCommand = sCommand.replace(/#@@@@#/g, "/");

  if ((iPos = sCommand.indexOf(LEFT_BRACKET)) == -1) {
    Command.sOperation = sCommand;
    return Command;
  }

  if (sCommand.substring(sCommand.length - 1, sCommand.length) != RIGHT_BRACKET) return false;

  Command.sOperation = sCommand.substring(0, iPos);

  sCommand = sCommand.substring(iPos + 1, sCommand.length - 1);
  aResult = sCommand.split(',');
  var Item = this.aList[Command.sOperation];
  var aRequiredParameters = new Array();
  Command.aParameters = new Object;
  Command.aOptionalParameters = new Array();
  for (var key in Item.aParameters) {
    var index = Item.aParameters[key];
    if (aResult[index] == null) continue;
    Command.aParameters[key] = unescape(aResult[index]);
    aRequiredParameters[index] = index;
  }
  for (var index in aResult) {
    if (aRequiredParameters[index] != null) continue;
    Command.aOptionalParameters.push(unescape(aResult[index]));
  }
  return Command;
};