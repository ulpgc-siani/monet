CGTaskConstructor = function () {
};

CGTaskConstructor.prototype.initNodes = function (extObject) {
  var Constructor = new CGNodeConstructor();
  var aExtNodes = extObject.select(CSS_NODE);
  aExtNodes.each(function (extNode) {
    Constructor.init(extNode.dom);
  }, this);
};

CGTaskConstructor.prototype.initTasks = function (extObject) {
  var Constructor = new CGTaskConstructor();
  var aExtTasks = extObject.select(CSS_TASK);
  aExtTasks.each(function (extTask) {
    Constructor.init(extTask.dom);
  }, this);
};

CGTaskConstructor.prototype.initRouteMap = function (extObject) {
  var DecoratorRouteMap = new CGDecoratorRouteMap();
  var aExtRouteMaps = extObject.select(CSS_ROUTE_MAP);
  aExtRouteMaps.each(function (extRouteMap) {
    DecoratorRouteMap.execute(extRouteMap.dom);
  }, this);
};

CGTaskConstructor.prototype.initWidgets = function (extObject) {
  var DecoratorWidget = new CGDecoratorWidget();

  if (extObject.hasClass(CLASS_WIDGET)) DecoratorWidget.execute(extObject.dom);
  else {
    var aExtWidgets = extObject.select(CSS_WIDGET);
    aExtWidgets.each(function (extWidget) {
      DecoratorWidget.execute(extWidget.dom);
    }, this);
  }
};

CGTaskConstructor.prototype.initBehaviours = function (extObject) {
  var aBehaviours = extObject.select(".behaviour");
  aBehaviours.each(function (Behaviour) {
    Behaviour = Behaviour.dom;
    Event.observe(Behaviour, 'click', CGTaskConstructor.prototype.atBehaviourClick.bind(this, Behaviour));
  }, this);
};

CGTaskConstructor.prototype.executeFunction = function (extObject) {
  var FunctionInfo = new CGCommandInfo(extObject.dom.innerHTML);
  var aParameters = FunctionInfo.getParameters();

  if (FunctionInfo.getOperation() == "printtaskcaption") {
    if (aParameters.length == 0) extObject.dom.innerHTML = EMPTY;
    var sCaption = Extension.getDefinitionCaption(aParameters[0]);
    extObject.dom.innerHTML = (aParameters[1]) ? sCaption.toShort(aParameters[1]) : sCaption;
  }
  else if (FunctionInfo.getOperation() == "formatdate") {
    if (aParameters.length == 0) extObject.dom.innerHTML = EMPTY;
    if ((aParameters[0] == null) || (aParameters[0] == "")) extObject.dom.innerHTML = "";
    extObject.dom.innerHTML = getFormattedDate(parseServerDate(aParameters[0]), Context.Config.Language);
  }
  else if (FunctionInfo.getOperation() == "formatdatetime") {
    if (aParameters.length == 0) extObject.dom.innerHTML = EMPTY;
    if ((aParameters[0] == null) || (aParameters[0] == "")) extObject.dom.innerHTML = "";
    extObject.dom.innerHTML = getFormattedDateTime(parseServerDate(aParameters[0]), Context.Config.Language);
  }
  else if (FunctionInfo.getOperation() == "formattimeleft") {
    if (aParameters.length == 0) extObject.dom.innerHTML = EMPTY;
    if ((aParameters[0] == null) || (aParameters[0] == "")) extObject.dom.innerHTML = "";
    var dtDate = new Date();
    var iMiliseconds = (new Date()).getTime();
    var iTimeLeft = parseInt(aParameters[0]);
    iMiliseconds += (iTimeLeft > 0) ? iTimeLeft : 0;
    dtDate.setTime(iMiliseconds);
    extObject.dom.innerHTML = getFormattedDateTime(dtDate);
  }
};

CGTaskConstructor.prototype.executeFunctions = function (extObject) {
  if (extObject.hasClass(CLASS_FUNCTION)) this.executeFunction(extObject);
  else {
    var aExtFunctions = extObject.select(CSS_FUNCTION);
    aExtFunctions.each(function (extFunction) {
      this.executeFunction(extFunction);
    }, this);
  }
};

CGTaskConstructor.prototype.init = function (DOMObject) {
  var extObject = Ext.get(DOMObject);
  var DecoratorTask = new CGDecoratorTask();

  if (extObject == null) return;
  if (extObject.hasClass(CLASS_TASK)) DecoratorTask.execute(DOMObject);

  this.initNodes(extObject);
  this.initTasks(extObject);
  this.initRouteMap(extObject);
  this.initWidgets(extObject);
  this.initBehaviours(extObject);
  this.executeFunctions(extObject);
};

CGTaskConstructor.prototype.atBehaviourClick = function (DOMBehaviour, EventLaunched) {
  var BehaviourInfo = new CGCommandInfo(DOMBehaviour.href);
  var aParameters = BehaviourInfo.getParameters();

  Event.stop(EventLaunched);

  return false;
};