//----------------------------------------------------------------------
// RefreshDOM
//----------------------------------------------------------------------
function CGProcessRefreshDOM() {
};

CGProcessRefreshDOM.prototype = new CGProcess;
CGProcessRefreshDOM.constructor = CGProcessRefreshDOM;
CommandFactory.register(CGProcessRefreshDOM, { Event: 0, Target: 1 }, false);

CGProcessRefreshDOM.prototype.isCollectionMagnetized = function (DOMCollection, aNodes) {
  var bIsMagnetized = false;
  var iPos = 0;

  while ((iPos < aNodes.length) && (!bIsMagnetized)) {
    if (DOMCollection.isMagnetized(aNodes[iPos].Type)) bIsMagnetized = true;
    iPos++;
  }

  return bIsMagnetized;
};

CGProcessRefreshDOM.prototype.getDOMNodeCollections = function (aNodes) {
  var aResult = new Array();
  var aViews = Desktop.Main.Center.Body.getViews(VIEW_NODE, VIEW_NODE_TYPE_COLLECTION);
  var CurrentNode = NodesCache.getCurrent();
  var DOMNodeCollection;
  var ControlInfo;

  if (!aNodes) return aResult;

  for (var iPos = 0; iPos < aViews.length; iPos++) {
    DOMNodeCollection = aViews[iPos].getDOM();
    if ((!DOMNodeCollection.isMagnetized) || (!DOMNodeCollection.isEditionMode) || (!DOMNodeCollection.isRefreshable) || (!DOMNodeCollection.getControlInfo)) continue;

    if (!DOMNodeCollection.isEditionMode()) continue;
    if (!DOMNodeCollection.isRefreshable()) continue;
    if (!this.isCollectionMagnetized(DOMNodeCollection, aNodes)) continue;

    ControlInfo = DOMNodeCollection.getControlInfo();
    if (!CurrentNode.getId() == ControlInfo.IdNode) continue;

    aResult.push(DOMNodeCollection);
  }

  return aResult;
};

CGProcessRefreshDOM.prototype.step_1 = function () {
  var aRefreshTasks = this.RefreshTaskList.getAll();
  var aViews, aDummyViews, IdNode;

  for (var iPos = 0; iPos < aRefreshTasks.length; iPos++) {
    var RefreshTask = aRefreshTasks[iPos];

    switch (RefreshTask.Type) {
      case RefreshTaskType.TaskList:
        break;

      case RefreshTaskType.Forms:
        IdNode = RefreshTask.Target.getId();

        aViews = new Array();
        aDummyViews = new Array();

        aDummyViews = Desktop.Main.Center.Body.getViews(VIEW_NODE, VIEW_NODE_TYPE_NODE, IdNode);
        for (var jPos = 0; jPos < aDummyViews.length; jPos++) {
          if ((RefreshTask.Sender != null) && (RefreshTask.Sender.getId() == aDummyViews[jPos].getId())) continue;
          Desktop.Main.Center.Body.deleteViewsOfContainer(VIEW_NODE, aDummyViews[jPos].getId());
          aViews.push(aDummyViews[jPos]);
        }

        aDummyViews = Desktop.Main.Center.Body.getViews(VIEW_NODE, VIEW_NODE_TYPE_FORM, IdNode);
        for (var jPos = 0; jPos < aDummyViews.length; jPos++) {
          if ((RefreshTask.Sender != null) && (RefreshTask.Sender.getId() == aDummyViews[jPos].getId())) continue;
          if (aDummyViews[jPos].getContainer().getTarget().getId() == IdNode) continue;
          aViews.push(aDummyViews[jPos]);
        }

        var ProcessNode = new CGProcessRefreshDOMNode();
        ProcessNode.Id = IdNode;
        ProcessNode.Views = aViews;
        ProcessNode.execute();

        var ProcessReferences = new CGProcessRefreshDOMNodeReferences();
        ProcessReferences.IdNode = IdNode;
        ProcessReferences.execute();

        break;

      case RefreshTaskType.Descriptors:
        IdNode = RefreshTask.Target.getId();

        aViews = Desktop.Main.Center.Body.getViews(VIEW_NODE, VIEW_NODE_TYPE_NODE, IdNode);
        for (var jPos = 0; jPos < aViews.length; jPos++) {
          Desktop.Main.Center.Body.deleteViewsOfContainer(VIEW_NODE, aViews[jPos].getId());
        }

        var ProcessNode = new CGProcessRefreshDOMNode();
        ProcessNode.Id = IdNode;
        ProcessNode.Views = aViews;
        ProcessNode.execute();

        var ProcessReferences = new CGProcessRefreshDOMNodeReferences();
        ProcessReferences.IdNode = IdNode;
        ProcessReferences.execute();

        break;

      case RefreshTaskType.References:
        IdNode = RefreshTask.Target.getId();

        var ProcessDescriptor = new CGProcessRefreshDOMNodeDescriptor();
        ProcessDescriptor.execute();

        var ProcessReferences = new CGProcessRefreshDOMNodeReferences();
        ProcessReferences.IdNode = IdNode;
        ProcessReferences.execute();

        break;

      case RefreshTaskType.RecoveredFromTrash:
      case RefreshTaskType.Added:
      case RefreshTaskType.Copied:
        var ProcessCollections = new CGProcessRefreshDOMNodesCollections();
        ProcessCollections.Nodes = RefreshTask.Target;
        ProcessCollections.Collections = this.getDOMNodeCollections(RefreshTask.Target);
        ProcessCollections.execute();

        if (Desktop.Main.Center.Body.isTabActive(VIEW_NODE, ID_NODE_SEARCH)) {
          var ActionSearchNodes = new CGActionSearchNodes();
          ActionSearchNodes.execute();
        }

        break;

      case RefreshTaskType.Deleted:
      case RefreshTaskType.Discarted:
        var ProcessNodes = new CGProcessRefreshDOMNodesDeleted();
        ProcessNodes.Nodes = RefreshTask.Target;
        ProcessNodes.Views = Desktop.Main.Center.Body.getViews(VIEW_NODE, VIEW_NODE_TYPE_COLLECTION);
        ProcessNodes.execute();

        if (Desktop.Main.Center.Body.isTabActive(VIEW_NODE, ID_NODE_SEARCH)) {
          var ActionSearchNodes = new CGActionSearchNodes();
          ActionSearchNodes.execute();
        }

        break;

    }
  }

  this.terminate();
};

//----------------------------------------------------------------------
// RefreshDOM Node
//----------------------------------------------------------------------
function CGProcessRefreshDOMNode() {
  this.ActivateNode = false;
};

CGProcessRefreshDOMNode.prototype = new CGProcess;
CGProcessRefreshDOMNode.constructor = CGProcessRefreshDOMNode;
CommandFactory.register(CGProcessRefreshDOMNode, { Id: 0, Views: 1 }, false);

CGProcessRefreshDOMNode.prototype.execute = function () {
  var aViews, View;
  var DOMNode;

  var Process = new CGProcessRefreshDOMNodeDescriptor();
  Process.execute();

  aViews = this.Views;

  for (var iPos = 0; iPos < aViews.length; iPos++) {
    View = aViews[iPos];
    DOMNode = View.getDOM();

    if (!DOMNode) continue;
    if (!DOMNode.getControlInfo) continue;

    var Process = new CGProcessShowNode();
    Process.Id = this.Id;
    Process.Mode = DOMNode.getControlInfo().Templates.Refresh;
    Process.ViewNode = View;
    Process.ActivateNode = this.ActivateNode;
    Process.execute();
  }

};

//----------------------------------------------------------------------
//RefreshDOM Node
//----------------------------------------------------------------------
function CGProcessRefreshDOMNodeDescriptor() {
  this.base = CGProcess;
  this.base(2);
};

CGProcessRefreshDOMNodeDescriptor.prototype = new CGProcess;
CGProcessRefreshDOMNodeDescriptor.constructor = CGProcessRefreshDOMNode;
CommandFactory.register(CGProcessRefreshDOMNodeDescriptor, { Id: 0 }, false);

CGProcessRefreshDOMNodeDescriptor.prototype.step_1 = function () {
  var Node = NodesCache.getCurrent();

  if (Node == null) {
    this.terminate();
    return;
  }

  Kernel.loadNodeDescriptor(this, Node.getId());
};

CGProcessRefreshDOMNodeDescriptor.prototype.step_2 = function () {
  var Node = NodesCache.getCurrent();

  if (Node == null) {
    this.terminate();
    return;
  }

  var ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, Node.getId());
  if (ViewNode == null) {
    this.terminate();
    return;
  }

  var AttributeList = new CGAttributeList();
  var Attribute, Indicator;
  AttributeList.unserialize(this.data);

  Attribute = AttributeList.getAttribute(DESCRIPTOR_LABEL);
  if (Attribute == null) {
    this.terminateOnFailure();
    return;
  }

  Indicator = Attribute.getIndicator(CGIndicator.VALUE);
  if (Indicator == null) {
    this.terminateOnFailure();
    return;
  }

  ViewNode.getDOM().setTitle(Indicator.getValue());

  this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// RefreshDOM Node references
//----------------------------------------------------------------------
function CGProcessRefreshDOMNodeReferences() {
  this.base = CGProcess;
  this.base(1);
  this.IndexReference = 0;
  this.aModesContent = new Array();
};

CGProcessRefreshDOMNodeReferences.prototype = new CGProcess;
CGProcessRefreshDOMNodeReferences.constructor = CGProcessRefreshDOMNodeReferences;
CommandFactory.register(CGProcessRefreshDOMNodeReferences, { IdNode: 0, References: 1 }, false);

//PUBLIC
CGProcessRefreshDOMNodeReferences.prototype.step_1 = function () {
  if (State.NodeListViewer) State.NodeListViewer.refreshBackground();
  if (State.SetListViewer) State.SetListViewer.refreshBackground();
  this.terminateOnSuccess();
};

//----------------------------------------------------------------------
// RefreshDOM Node collections
//----------------------------------------------------------------------
function CGProcessRefreshDOMNodesCollections() {
};

CGProcessRefreshDOMNodesCollections.prototype = new CGProcess;
CGProcessRefreshDOMNodesCollections.constructor = CGProcessRefreshDOMNodesCollections;
CommandFactory.register(CGProcessRefreshDOMNodesCollections, { Nodes: 0, Collections: 1 }, false);

//PUBLIC
CGProcessRefreshDOMNodesCollections.prototype.execute = function () {
  var DOMNodeCollection;
  var ControlInfo;
  var ViewCollection;

  if (this.Collections == null) return;

  if (State.NodeListViewer) State.NodeListViewer.refreshBackground();
  if (State.TrashListViewer) State.TrashListViewer.refreshBackground();

  for (var iPos = 0; iPos < this.Collections.length; iPos++) {
    DOMNodeCollection = this.Collections[iPos];

    if (!DOMNodeCollection.getControlInfo) continue;
    if (!DOMNodeCollection.IdView) continue;

    ControlInfo = DOMNodeCollection.getControlInfo();
    if ((ViewCollection = Desktop.Main.Center.Body.getView(VIEW_NODE, DOMNodeCollection.IdView)) == null) continue;

    var Process = new CGProcessShowNode();
    Process.Id = ControlInfo.IdNode;
    Process.Mode = ControlInfo.Templates.Refresh;
    Process.ViewNode = ViewCollection;
    Process.ActivateNode = false;
    Process.execute();
  }
};

//----------------------------------------------------------------------
// RefreshDOM Node deleted
//----------------------------------------------------------------------
function CGProcessRefreshDOMNodesDeleted() {
  this.base = CGProcess;
  this.base(2);
};

CGProcessRefreshDOMNodesDeleted.prototype = new CGProcess;
CGProcessRefreshDOMNodesDeleted.constructor = CGProcessRefreshDOMNodesDeleted;
CommandFactory.register(CGProcessRefreshDOMNodesDeleted, { IdNode: 0, Views: 1 }, false);

CGProcessRefreshDOMNodesDeleted.prototype.existCollectionReferencesToNodes = function (DOMCollection) {
  for (var iPos = 0; iPos < this.Nodes.length; iPos++) {
    if (DOMCollection.getNodeReferencesCount(this.Nodes[iPos].getId()) > 0) return true;
  }
  return false;
};

CGProcessRefreshDOMNodesDeleted.prototype.step_1 = function () {
  var aDOMCollectionsToRefresh = new Array();

  if (State.NodeListViewer) State.NodeListViewer.refreshBackground();
  else if (State.SetListViewer) State.SetListViewer.refreshBackground();

  for (var iPos = 0; iPos < this.Views.length; iPos++) {

    if (this.Views[iPos].getTarget() != null) {
      if (this.Views[iPos].getTarget().getId() == ID_NODE_SEARCH) continue;
    }

    var DOMNodeCollection = this.Views[iPos].getDOM();
    if (!DOMNodeCollection) continue;
    if (!DOMNodeCollection.getNodeReferencesCount) continue;
    if (!DOMNodeCollection.isEditionMode) continue;
    if (!DOMNodeCollection.isRefreshable) continue;

    if ((DOMNodeCollection.isEditionMode()) && (DOMNodeCollection.isRefreshable()) && (this.existCollectionReferencesToNodes(DOMNodeCollection))) {
      aDOMCollectionsToRefresh.push(DOMNodeCollection);
    }
  }

  var Action = new CGProcessRefreshDOMNodesCollections();
  Action.Nodes = this.Nodes;
  Action.Collections = aDOMCollectionsToRefresh;
  Action.ReturnProcess = this;
  Action.execute();
};

CGProcessRefreshDOMNodesDeleted.prototype.step_2 = function () {
  var CurrentNode = NodesCache.getCurrent();
  var ViewNode = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, CurrentNode.getId());

  if (ViewNode == null) {
    this.terminate();
    return;
  }

  for (var iPos = 0; iPos < this.Nodes.length; iPos++) {
    var Node = this.Nodes[iPos];
    var ViewContainer = Desktop.Main.Center.Body.getContainerView(VIEW_NODE, Node.getId());

    if (ViewContainer != null) {
      Desktop.Main.Center.Body.deleteView(VIEW_NODE, ViewContainer.getId());
      Desktop.Main.Center.Body.deleteTab(VIEW_NODE, Node.getId());
    }

    if (NodesCache.exists(Node.getId())) {
      NodesCache.unregister(Node.getId());
    }

    if (CurrentNode.getId() == Node.getId()) {
      var Process = new CGProcessShowLastView();
      Process.execute();
    }
    else {
      DOMNode = ViewNode.getDOM();
      if (DOMNode.getNodesReferencesCount) {
        CurrentNode.NodeList.setCount(DOMNode.getNodesReferencesCount());
      }
    }
  }

  this.terminate();
};