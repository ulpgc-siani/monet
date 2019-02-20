var Application = new Object;
var Context = new Object;
Context.Config = new Object;
Context.Operation = new Object();
Context.Debugging = true;

Application.init = function() {
  readData(Context, $("#dataInit"));

  Kernel.init();
  CommandListener.start(CommandDispatcher);
  $(document).click(Application.atMouseClick);
  
  Desktop.init();
  Desktop.refresh();
  
  var action = new ActionInit();
  action.execute();

  this.isRunning = true;
};

Application.atMouseClick = function(event) {
  Application.mousePosition = { left: event.pageX, top: event.pageY };
};

Application.onLinkClick = function(link) {
  alert(link);
};

Application.onOpenIndicator = function(view, code, period) {
  var process = new ProcessOpenIndicator();
  process.view = view;
  process.code = code;
  process.period = period;
  process.execute();
};

$(document).ready(
  function(){
    Application.init();
  }
);