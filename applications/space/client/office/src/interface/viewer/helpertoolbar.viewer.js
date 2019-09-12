function CGViewerToolbar() {
  this.extLayer = null;
  this.templates = new Object();
}

CGViewerToolbar.prototype.init = function () {
  this.templates.navigation = new Template(translate(AppTemplate.ViewerHelperToolbarNavigation, Lang.ViewerHelperToolbar));
  this.templates.add = new Template(translate(AppTemplate.ViewerHelperToolbarAdd, Lang.ViewerHelperToolbar));
  this.templates.copy = new Template(translate(AppTemplate.ViewerHelperToolbarCopy, Lang.ViewerHelperToolbar));
  this.templates.download = new Template(translate(AppTemplate.ViewerHelperToolbarDownload, Lang.ViewerHelperToolbar));
  this.templates.print = new Template(translate(AppTemplate.ViewerHelperToolbarPrint, Lang.ViewerHelperToolbar));
  this.templates.edit = new Template(translate(AppTemplate.ViewerHelperToolbarEdit, Lang.ViewerHelperToolbar));
  this.templates.tool = new Template(translate(AppTemplate.ViewerHelperToolbarTool, Lang.ViewerHelperToolbar));
  this.templates.custom = new Template(translate(AppTemplate.ViewerHelperToolbarCustom, Lang.ViewerHelperToolbar));
  this.templates.multiple = new Template(translate(AppTemplate.ViewerHelperToolbarMultiple, Lang.ViewerHelperToolbar));
  this.templates.multipleItem = new Template(translate(AppTemplate.ViewerHelperToolbarMultipleItem, Lang.ViewerHelperToolbar));
  this.templates.simple = new Template(translate(AppTemplate.ViewerHelperToolbarSimple, Lang.ViewerHelperToolbar));
};

CGViewerToolbar.prototype.setTarget = function (target) {
  this.target = target;
};

CGViewerToolbar.prototype.hide = function () {
  this.extLayer.hide();
};

CGViewerToolbar.prototype.show = function () {
  this.extLayer.show();
};

CGViewerToolbar.prototype.renderOperations = function (operations, label, collapsed) {
  var result = "";
  var className = "";

  if (operations.length == 0) return;

  if (operations.length == 1) {
    result = this.templates.simple.evaluate({operation: operations[0]});
    className = "";
  }
  else {
    if (collapsed) {
      var operationsValue = "";
      for (var i = 0; i < operations.length; i++)
        operationsValue += this.templates.multipleItem.evaluate({operation: operations[i]});
      result = this.templates.multiple.evaluate({label: label, operations: operationsValue});
      className = "selector";
    }
    else {
      for (var i = 0; i < operations.length; i++)
        result += this.templates.simple.evaluate({operation: operations[i]});
      className = "";
    }
  }

  var extRow = this.extLayer.select("table tr").first();
  var extPivotColumn = this.extLayer.select("table tr td.pivot").first();
  var DOMColumn = document.createElement("td");
  DOMColumn.innerHTML = result;
  DOMColumn.className = className;
  extRow.dom.insertBefore(DOMColumn, extPivotColumn.dom);
};

CGViewerToolbar.prototype.render = function (extLayer) {
  this.extLayer = extLayer;
  this.refresh();
};

CGViewerToolbar.prototype.getTemplate = function (operation) {

  if (operation.parameters.command != null)
    return this.templates.custom;

  if (operation.type == Operation.NAVIGATION) return this.templates.navigation;
  else if (operation.type == Operation.ADD) return this.templates.add;
  else if (operation.type == Operation.COPY) return this.templates.copy;
  else if (operation.type == Operation.DOWNLOAD) return this.templates.download;
  else if (operation.type == Operation.PRINT) return this.templates.print;
  else if (operation.type == Operation.EDIT) return this.templates.edit;
  else if (operation.type == Operation.TOOL) return this.templates.tool;
  else if (operation.type == Operation.CUSTOM) return this.templates.custom;
  else if (operation.type == Operation.CONTEXT) return this.templates.custom;

  return null;
};

CGViewerToolbar.prototype.refresh = function () {

  if (this.target == null) return;

  var navigationOperations = new Array();
  var addOperations = new Array();
  var copyOperations = new Array();
  var downloadOperations = new Array();
  var printOperations = new Array();
  var editOperations = new Array();
  var toolOperations = new Array();
  var customOperations = new Array();
  var contextOperations = new Array();

  this.extLayer.dom.innerHTML = translate(AppTemplate.ViewerHelperToolbar, Lang.ViewerHelperToolbar);

  var count = 0;
  for (var i = 0; i < this.target.operationSet.length; i++) {
    var operation = this.target.operationSet[i];
    if (operation.type == Operation.ADD) count++;
  }

  labelAdd = null;
  if (count <= 1) labelAdd = Lang.ViewerHelperToolbar.Add;

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

    if (operation.type == Operation.ADD && labelAdd != null) parameters.label = labelAdd;
    if (parameters.code == null) parameters.code = "";

    if (operation.type == Operation.CONTEXT) parameters.css = "context";

    if (operation.type == Operation.NAVIGATION)
      navigationOperations.push(template.evaluate(parameters));
    else if (operation.type == Operation.ADD)
      addOperations.push(template.evaluate(parameters));
    else if (operation.type == Operation.COPY)
      copyOperations.push(template.evaluate(parameters));
    else if (operation.type == Operation.DOWNLOAD)
      downloadOperations.push(template.evaluate(parameters));
    else if (operation.type == Operation.PRINT)
      printOperations.push(template.evaluate(parameters));
    else if (operation.type == Operation.EDIT)
      editOperations.push(template.evaluate(parameters));
    else if (operation.type == Operation.TOOL)
      toolOperations.push(template.evaluate(parameters));
    else if (operation.type == Operation.CUSTOM)
      customOperations.push(template.evaluate(parameters));
    else if (operation.type == Operation.CONTEXT)
      contextOperations.push(template.evaluate(parameters));
  }

  addOperations = addOperations.concat(copyOperations);
  downloadOperations = downloadOperations.concat(printOperations);
  toolOperations = toolOperations.concat(customOperations);

  this.renderOperations(navigationOperations, "", false);
  this.renderOperations(addOperations, Lang.ViewerHelperToolbar.Add, true);
  this.renderOperations(downloadOperations, Lang.ViewerHelperToolbar.Download, true);
  this.renderOperations(toolOperations, Lang.ViewerHelperToolbar.Tools, true);
  this.renderOperations(editOperations, "", false);
  this.renderOperations(contextOperations, "", false);

  var aBehaviours = this.extLayer.select(".behaviour");
  aBehaviours.each(function (extBehaviour) {
    DOMBehaviour = extBehaviour.dom;
    Event.observe(DOMBehaviour, 'click', CGViewerToolbar.prototype.atBehaviourClick.bind(this, DOMBehaviour));
  }, this);

  var extSelectorOptionsList = this.extLayer.select(CSS_SELECTOR + BLANK + CSS_OPTIONS + " a");
  extSelectorOptionsList.each(function (extSelectorOption) {
    var DOMSelectorOption = extSelectorOption.dom;
    Event.observe(DOMSelectorOption, 'click', CGViewerToolbar.prototype.atSelectorOptionClick.bind(this, DOMSelectorOption));
  }, this);

  this.refreshBackTaskCommand();
  this.refreshBackLinkCommand();

  CommandListener.capture(this.extLayer);
};

CGViewerToolbar.prototype.showBackTaskCommand = function (IdTask, TargetNode, TargetView) {
  var extCommand = this.extLayer.select(".command.backtask").first();
  if (extCommand) {
    extCommand.dom.style.display = "block";
    extCommand.dom.onclick = CGViewerToolbar.prototype.doShowBackTaskCommand.bind(this, IdTask, TargetNode, TargetView);//href = getMonetLinkAction(TargetNode != null ? "ml://node." + TargetNode + "." + TargetView : "ml://task." + IdTask);
  }
};

CGViewerToolbar.prototype.doShowBackTaskCommand = function(IdTask, TargetNode, TargetView) {
  var action = TargetNode != null ? new CGActionShowNode() : new CGActionShowTask();
  // getMonetLinkAction(TargetNode != null ? "ml://node." + TargetNode + "." + TargetView : "ml://task." + IdTask);
  action.Id = TargetNode != null ? TargetNode : IdTask;
  action.execute();
  if (TargetView == null) return;
  window.setTimeout(function() { Desktop.Main.Center.Body.getContainerView(VIEW_NODE, TargetNode).getDOM().activateTab(TargetView); }, 1000);
};

CGViewerToolbar.prototype.refreshBackTaskCommand = function () {
  var node = NodesCache.getCurrent();
  if (node == null) return;

  var viewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, node.getId());
  if (viewNode == null) return;

  var DOMNode = viewNode.getDOM();
  if (DOMNode == null) return;

  if (State.TaskNode != null) {
    if ((DOMNode.getId() != State.TaskNode.IdNode) && (!DOMNode.isChild(State.TaskNode.IdNode))) {
      this.hideBackTaskCommand();
      State.TaskNode = null;
    } else
      this.showBackTaskCommand(State.TaskNode.IdTask, State.TaskNode.TargetNode, State.TaskNode.TargetView);
  } else
    this.hideBackTaskCommand();
};

CGViewerToolbar.prototype.hideBackTaskCommand = function () {
  var extCommand = this.extLayer.select(".command.backtask").first();
  if (extCommand) extCommand.dom.style.display = "none";
};

CGViewerToolbar.prototype.showBackLinkCommand = function (IdNode) {
  var extCommand = this.extLayer.select(".command.backlink").first();
  if (extCommand) {
    extCommand.dom.style.display = "block";
    extCommand.dom.href = getMonetLinkAction("ml://node." + IdNode);
  }
};

CGViewerToolbar.prototype.showOperation = function (name) {
  var extCommand = this.extLayer.select(".command." + name).first();
  if (extCommand) extCommand.dom.style.display = "block";
};

CGViewerToolbar.prototype.hideOperation = function (name) {
  var extCommand = this.extLayer.select(".command." + name).first();
  if (extCommand) extCommand.dom.style.display = "none";
};

CGViewerToolbar.prototype.enableOperation = function (name) {
  var extCommand = this.extLayer.select(".command." + name).first();
  if (extCommand) extCommand.removeClass(CLASS_DISABLED);
};

CGViewerToolbar.prototype.disableOperation = function (name) {
  var extCommand = this.extLayer.select(".command." + name).first();
  if (extCommand) extCommand.addClass(CLASS_DISABLED);
};

CGViewerToolbar.prototype.refreshBackLinkCommand = function () {
  var node = NodesCache.getCurrent();
  if (node == null) return;

  var viewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, node.getId());
  if (viewNode == null) return;

  var DOMNode = viewNode.getDOM();
  if (DOMNode == null) return;

  if (State.LinkNode != null) {
    if ((DOMNode.getId() != State.LinkNode.IdLink) && (!DOMNode.isChild(State.LinkNode.IdLink))) {
      this.hideBackLinkCommand();
      State.LinkNode = null;
    } else
      this.showBackLinkCommand(State.LinkNode.IdNode);
  } else
    this.hideBackLinkCommand();
};

CGViewerToolbar.prototype.hideBackLinkCommand = function () {
  var extCommand = this.extLayer.select(".command.backlink").first();
  if (extCommand) extCommand.dom.style.display = "none";
};

CGViewerToolbar.prototype.showSelectorOptions = function (DOMSelector, DOMOptions) {
  DOMOptions.style.display = "block";

  Ext.get(document.body).on("click", this.atHideSelectorOptions, this);
  this.DOMOptions = DOMOptions;

  if (DOMOptions.hasClassName("top")) {
    DOMOptions.style.marginTop = "-" + (DOMSelector.getHeight() + DOMOptions.getHeight() - 1) + "px";
  }

  if (DOMSelector.getWidth() > DOMOptions.getWidth()) {
    var extOptions = Ext.get(DOMOptions);
    extOptions.setWidth(DOMSelector.getWidth());
  }
};

CGViewerToolbar.prototype.hideSelectorOptions = function (DOMOptions) {
  if (!DOMOptions) DOMOptions = this.DOMOptions;
  DOMOptions.style.display = "none";
  this.DOMOptions = null;
  Ext.get(document.body).un("click", this.atHideSelectorOptions);
};

CGViewerToolbar.prototype.toggleSelectorOptions = function (DOMSelector, DOMOptions) {
  var isVisible = DOMOptions.style.display == "block";
  if (isVisible) this.hideSelectorOptions(DOMOptions);
  else this.showSelectorOptions(DOMSelector, DOMOptions);
};

CGViewerToolbar.prototype.atHideSelectorOptions = function () {
  this.hideSelectorOptions();
};

CGViewerToolbar.prototype.atSelectorOptionClick = function (DOMSelectorOption, EventLaunched) {
  this.hideSelectorOptions();
};

CGViewerToolbar.prototype.atBehaviourClick = function (DOMBehaviour, EventLaunched) {
  var BehaviourInfo = new CGCommandInfo(DOMBehaviour.href);

  if (BehaviourInfo.getOperation() == "toggleselector") {
    if ((DOMOptions = DOMBehaviour.next("ul.options")) != null) {
      this.toggleSelectorOptions(DOMBehaviour, DOMOptions);
    }
  }

  Event.stop(EventLaunched);

  return false;
};