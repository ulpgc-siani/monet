var Application = {};
var Context = {};
Context.Config = {};
Context.Config.DefaultLocation = {};
Context.Config.Layer = {};
Context.Config.Layer.iWidth = DEFAULT_WIDTH;
Context.Config.Layer.iHeight = DEFAULT_HEIGHT;
Context.Config.NodesBufferSize = 1;
Context.Config.SignatureApplication = {};
Context.Config.Map = {};
Context.Pages = {};
Context.Debugging = false;

Application.init = function () {

  readData(Context, Ext.get(Literals.Data));

  Ext.BLANK_IMAGE_URL = Context.Config.ImagesPath + "/s.gif";

  Kernel.init();
  CommandListener.start(CommandDispatcher);
  Extension = new CGExtension();

  NodesCache = new CGCache();
  TasksCache = new CGCache();
  TeamsCache = new CGCache();

  Desktop.init(Context.Config.Layer.Name);
  Desktop.setLayerSize(Context.Config.Layer.Width, Context.Config.Layer.Height);

  var DOMHistory = $(Literals.Frames.History);
  Event.observe(DOMHistory, "load", CommandDispatcher.History.executeCommand.bind(DOMHistory));

  var BPIListener = new CGListenerBPI();
  EventManager.addListener(BPIListener);
  EventManager.addListener(ViewerSidebar);
  if (Context.Config.PushEnabled == "true") EventManager.addListener(PushListener);

  var Action = new CGActionInit();
  Action.execute();

  WidgetFactory.init();
  Application.registerActionTimers();

  this.isRunning = true;
};

Application.registerActionTimers = function () {
  /*
   window.setInterval(function() {
   var Process = new CGProcessRefreshTasks();
   Process.execute();
   }, 120000);
   */
};

Ext.onReady(
    function () {
      Ext.QuickTips.init();
      Ext.enableListenerCollection = true;
      Application.init();
    }
);