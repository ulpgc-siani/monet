CommandFactory = new Object;
CommandFactory.list = new Array();

CommandFactory.register = function(classAction, parameters, useHistory) {
  if (!classAction) return;
  if (!classAction.prototype.constructor) return;
  var className = this.getClassName(classAction);
  var operation = String(className.substring(String("Action").length)).toLowerCase();
  if (parameters == null) parameters = new Object;
  this.list[operation] = {
    classAction: classAction,
    parameters: parameters,
    useHistory: useHistory
  };
};

CommandFactory.getClassName = function(classAction) {
  var data = classAction.constructor.toString().match(/function\s*(\w+)/);
  if (data && data.length == 2) return data[1];
};

CommandFactory.getAction = function(command){
  var Command = this.parseCommand(command);
  var Item = this.list[Command.operation];
  if (Item == null) return null;
  var Action = new Item.classAction;
  Action.ClassName = this.getClassName(Item.classAction);
  for (var index in Command.parameters) Action[index] = Command.parameters[index];
  Action.OptionalParameters = Command.optionalParameters;
  return Action;
};

CommandFactory.useHistory = function(command) {
  var Command = this.parseCommand(command);
  var Item = this.list[Command.operation];
  if (Item == null) return;
  return Item.useHistory;
};

CommandFactory.parseCommand = function(command) {
  var Command = new Object;

  command = command.replace(/%28/g, "(");
  command = command.replace(/%29/g, ")");

  while ((pos = command.indexOf("/")) != -1) {
    var bracketPos = command.indexOf("(");
    if (pos < bracketPos) command = command.substring(pos+1, command.length);
    else command = command.substring(0,pos, command.length) + "#@@@@#" + command.substring(pos+1, command.length);
  }
  command = command.replace(/#@@@@#/g, "/");

  if ((pos=command.indexOf("(")) == -1) {
    Command.operation = command;
    return Command;
  }

  if (command.substring(command.length-1,command.length) != ")") return false;

  Command.operation = command.substring(0,pos);

  command = command.substring(pos+1,command.length-1);
  
  var i = 0;
  var resultArray = null;
  var aJsonParameters = new Array();
  while ((resultArray = command.match(/\{.*\}/)) != null) {
    aJsonParameters[i] = resultArray[0];
    command = command.replace(resultArray[0], "#" + i + "#");
    i++;
  }
  
  var result = command.split(',');
  for (var i=0; i<aJsonParameters.length; i++) {
    for (var j=0; j<result.length; j++) {
      if (result[j] == "#" + i + "#")
        result[j] = aJsonParameters[i];
    }
  }
  
  var Item = this.list[Command.operation];
  var requiredParameters = new Array();
  Command.parameters = new Object;
  Command.optionalParameters = new Array();
  for (var key in Item.parameters) {
    var index = Item.parameters[key];
    if (result[index] == null) continue;
    Command.parameters[key] = utf8Decode(result[index]);
    requiredParameters[index] = index;
  }
  for (var index in result) {
    if (requiredParameters[index] != null) continue;
    Command.optionalParameters.push(utf8Decode(result[index]));
  }
  return Command;
};