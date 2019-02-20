ViewerSidebar = new Object();
ViewerSidebar.extLayer = null;
ViewerSidebar.activeHelper = null;
ViewerSidebar.templates = new Object();

ViewerSidebar.init = function (extLayer) {
  ViewerSidebar.extLayer = extLayer;

  ViewerSidebar.extLayer.dom.innerHTML = translate(AppTemplate.ViewerHelperSidebar, Lang.ViewerHelperSidebar);
  ViewerSidebar.templates.add = new Template(translate(AppTemplate.ViewerHelperSidebarAdd, Lang.ViewerHelperSidebar));
  ViewerSidebar.templates.copy = new Template(translate(AppTemplate.ViewerHelperSidebarCopy, Lang.ViewerHelperSidebar));
  ViewerSidebar.templates.download = new Template(translate(AppTemplate.ViewerHelperSidebarDownload, Lang.ViewerHelperSidebar));
  ViewerSidebar.templates.print = new Template(translate(AppTemplate.ViewerHelperSidebarPrint, Lang.ViewerHelperSidebar));
  ViewerSidebar.templates.tool = new Template(translate(AppTemplate.ViewerHelperSidebarTool, Lang.ViewerHelperSidebar));
  ViewerSidebar.templates.custom = new Template(translate(AppTemplate.ViewerHelperSidebarCustom, Lang.ViewerHelperSidebar));

  ViewerHelperPage.init(extLayer.down(".help"));
  ViewerHelperEditors.init(extLayer.down(".component").down(".editors"));
  ViewerHelperPreview.init(extLayer.down(".component").down(".preview"));
  ViewerHelperObservers.init(extLayer.down(".observers"));
  ViewerHelperRevisionList.init(extLayer.down(".component").down(".revisionlist"));
  ViewerHelperMap.init(extLayer.down(".component").down(".map"));
  ViewerHelperSource.init(extLayer.down(".component").down(".source"));
  ViewerHelperRole.init(extLayer.down(".component").down(".role"));
  ViewerHelperList.init(extLayer.down(".component").down(".list"));
  ViewerHelperChat.init(extLayer.down(".chat"));

  ViewerHelperObservers.onBeforeShow = ViewerSidebar.atObserversBeforeShow;
};

ViewerSidebar.setTarget = function (target) {
  ViewerSidebar.target = target;
};

ViewerSidebar.isHelperEditorsActive = function () {
  return ViewerSidebar.activeHelper == Helper.EDITORS;
};

ViewerSidebar.isHelperPreviewActive = function () {
  return ViewerSidebar.activeHelper == Helper.PREVIEW;
};

ViewerSidebar.isHelperListActive = function () {
  return ViewerSidebar.activeHelper == Helper.LIST;
};

ViewerSidebar.getHelper = function (name) {
  if (name == Helper.PAGE) return ViewerHelperPage;
  else if (name == Helper.EDITORS) return ViewerHelperEditors;
  else if (name == Helper.PREVIEW) return ViewerHelperPreview;
  else if (name == Helper.OBSERVERS) return ViewerHelperObservers;
  else if (name == Helper.REVISIONLIST) return ViewerHelperRevisionList;
  else if (name == Helper.MAP) return ViewerHelperMap;
  else if (name == Helper.SOURCE) return ViewerHelperSource;
  else if (name == Helper.ROLE) return ViewerHelperRole;
  else if (name == Helper.LIST) return ViewerHelperList;
  else if (name == Helper.CHAT) return ViewerHelperChat;
};

ViewerSidebar.activateHelper = function (name) {
  var helper = ViewerSidebar.getHelper(name);

  if (helper != null) {
    helper.show();
    helper.refresh();
  }

  ViewerSidebar.activeHelper = name;
};

ViewerSidebar.hide = function () {
  var extAddHelper = ViewerSidebar.extLayer.down(".add");
  var extToolsHelper = ViewerSidebar.extLayer.down(".tools");

  extAddHelper.dom.style.display = "none";
  extToolsHelper.dom.style.display = "none";

  ViewerHelperPage.hide();
  ViewerHelperEditors.hide();
  ViewerHelperPreview.hide();
  ViewerHelperObservers.hide();
  ViewerHelperRevisionList.hide();
  ViewerHelperMap.hide();
  ViewerHelperSource.hide();
  ViewerHelperRole.hide();
  ViewerHelperList.hide();
  ViewerHelperChat.hide();
  ViewerSidebar.activateHelper(null);
};

ViewerSidebar.show = function () {
};

ViewerSidebar.refreshHelper = function (itemsContent, style) {
  if (itemsContent != "") {
    var extHelper = ViewerSidebar.extLayer.down(style);
    var extList = extHelper.down("ul");
    extHelper.dom.style.display = "block";
    extList.dom.innerHTML = itemsContent;
    CommandListener.capture(extList);
  }
};

ViewerSidebar.getTemplate = function (operation) {

  if (operation.parameters.command != null)
    return ViewerSidebar.templates.custom;

  if (operation.type == Operation.ADD) return ViewerSidebar.templates.add;
  else if (operation.type == Operation.COPY) return ViewerSidebar.templates.copy;
  else if (operation.type == Operation.DOWNLOAD) return ViewerSidebar.templates.download;
  else if (operation.type == Operation.PRINT) return ViewerSidebar.templates.print;
  else if (operation.type == Operation.TOOL) return ViewerSidebar.templates.tool;
  else if (operation.type == Operation.CUSTOM) return ViewerSidebar.templates.custom;

  return null;
};

ViewerSidebar.refresh = function () {

  if (this.target == null) return;

  var addItems = "";
  var copyItems = "";
  var downloadItems = "";
  var printItems = "";
  var toolItems = "";
  var customItems = "";

  ViewerSidebar.showObserverHelper = false;

  this.target.operationSet = this.target.operationSet.sort(function(Operation1, Operation2) {
    if (Operation1.label == null || Operation2.label == null)
      return -1;
    return Operation1.label.localeCompare(Operation2.label);
  });

  for (var i = 0; i < this.target.operationSet.length; i++) {
    var operation = this.target.operationSet[i];
    var parameters = operation.parameters;
    var template = this.getTemplate(operation);

    parameters.label = operation.label;
    parameters.display = operation.visible == null || operation.visible ? "inline-block" : "none";

    if (parameters.code == null) parameters.code = "";

    if (operation.type == Operation.ADD)
      addItems += template.evaluate(parameters);
    else if (operation.type == Operation.COPY)
      copyItems += template.evaluate(parameters);
    else if (operation.type == Operation.DOWNLOAD)
      downloadItems += template.evaluate(parameters);
    else if (operation.type == Operation.PRINT)
      printItems += template.evaluate(parameters);
    else if (operation.type == Operation.TOOL)
      toolItems += template.evaluate(parameters);
    else if (operation.type == Operation.CUSTOM)
      customItems += template.evaluate(parameters);
    else if (operation.type == Operation.WIDGET) {
      var extComponent = ViewerSidebar.extLayer.down(".component");
      extComponent.show();
    }
    else if (operation.type == Operation.HELP && ViewerHelperPage.getTarget() != null)
      ViewerHelperPage.show();
    else if (operation.type == Operation.OBSERVER)
      ViewerSidebar.showObserverHelper = true;
  }

  addItems = addItems + copyItems;
  toolItems = downloadItems + printItems + toolItems + customItems;

  this.refreshHelper(addItems, ".add");
  this.refreshHelper(toolItems, ".tools");
};

ViewerSidebar.showOperation = function (name) {
  var extCommand = this.extLayer.select(".command." + name).first();
  if (extCommand) extCommand.dom.style.display = "block";
};

ViewerSidebar.hideOperation = function (name) {
  var extCommand = this.extLayer.select(".command." + name).first();
  if (extCommand) extCommand.dom.style.display = "none";
};

ViewerSidebar.enableOperation = function (name) {
  var extCommand = this.extLayer.select(".command." + name).first();
  if (extCommand) extCommand.removeClass(CLASS_DISABLED);
};

ViewerSidebar.disableOperation = function (name) {
  var extCommand = this.extLayer.select(".command." + name).first();
  if (extCommand) extCommand.addClass(CLASS_DISABLED);
};

ViewerSidebar.atObserversBeforeShow = function () {
  return ViewerSidebar.showObserverHelper;
};

// implements event manager methods

ViewerSidebar.nodeBeforeOpened = function (Sender) {
};

ViewerSidebar.nodeFocused = function (Sender) {
  if (!Sender.DOMNode) return;
  CommandListener.throwCommand("loadnodehelperpage(" + Sender.Node.Code + ")");
};

ViewerSidebar.nodeViewFocused = function (Sender) {
  if (!Sender.DOMNode) return;
  CommandListener.throwCommand("loadnodehelperpage(" + Sender.Node.Code + ")");
};

ViewerSidebar.nodeFieldFocused = function (Sender) {
  if (ViewerSidebar.isHelperEditorsActive()) return;
  CommandListener.throwCommand("loadhelpereditors()");
};

ViewerSidebar.nodeFieldBlur = function (Sender) {
};

ViewerSidebar.previewFocused = function (Sender) {
  if (ViewerSidebar.isHelperPreviewActive()) return;
  CommandListener.throwCommand("loadhelperpreview()");
  ViewerSidebar.activateHelper(Helper.PREVIEW);
};

ViewerSidebar.listViewerFocused = function (Sender) {
  if (ViewerSidebar.isHelperListActive()) return;
  CommandListener.throwCommand("loadhelperlistviewer()");
  ViewerSidebar.activateHelper(Helper.LIST);
};