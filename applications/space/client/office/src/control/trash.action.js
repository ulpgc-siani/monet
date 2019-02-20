//----------------------------------------------------------------------
// Show trash
//----------------------------------------------------------------------
function CGActionShowTrash() {
  this.base = CGAction;
  this.base(2);
  this.aNodesSelected = new Array();
};

CGActionShowTrash.prototype = new CGAction;
CGActionShowTrash.constructor = CGActionShowTrash;
CommandFactory.register(CGActionShowTrash, null, true);

CGActionShowTrash.prototype.step_1 = function () {

  Desktop.hideBanner();
  State.isShowingPrototype = false;

  Kernel.loadSystemTemplate(this, "trash");
};

CGActionShowTrash.prototype.step_2 = function () {
  ViewTrash.setContent(this.data);
  ViewTrash.refresh();
  ViewTrash.show();
  Desktop.Main.Center.Body.activateTrash();
  this.setActiveFurniture(Furniture.TRASH);
};

//----------------------------------------------------------------------
// Render Trash List
//----------------------------------------------------------------------
function CGActionRenderTrashList() {
  this.base = CGAction;
  this.base(2);
  this.AvailableProcessClass = CGProcessCleanDirty;
};

CGActionRenderTrashList.prototype = new CGAction;
CGActionRenderTrashList.constructor = CGActionRenderTrashList;
CommandFactory.register(CGActionRenderTrashList, { IdDOMViewerLayer: 0, IdDOMViewerLayerOptions: 1 }, false);

CGActionRenderTrashList.prototype.atSelectItem = function (bSelected, Sender, Id) {
  if (bSelected) State.addNodeReferenceToTrashSelection(Id, bSelected);
  else State.deleteSelectedNodeReferenceFromTrash(Id);
};

CGActionRenderTrashList.prototype.atSelectItems = function (bSelected, Sender, Items) {
  if (bSelected) {
    State.addNodesReferencesToTrashSelection(Items);
  }
  else {
    State.deleteSelectedNodesReferencesFromTrash();
  }
};

CGActionRenderTrashList.prototype.atBoundItem = function (Sender, Item) {
  var Dummy = Item;
  for (var index in Dummy) {
    if (isFunction(Dummy[index])) continue;
    Item[index + "_short"] = shortValue(Dummy[index]);
  }
};

CGActionRenderTrashList.prototype.destroyViewer = function () {
  if (State.TrashListViewer == null) return;
  State.registerListViewerState("trashlist", State.TrashListViewer.getState());
  State.TrashListViewer.dispose();
  $(Literals.ListViewerWizard).innerHTML = "";
};

CGActionRenderTrashList.prototype.createViewer = function () {
  var Options;

  this.destroyViewer();

  eval($(this.IdDOMViewerLayerOptions).innerHTML);
  State.TrashListViewer = new CGListViewer(Options, Context.Config.Language, Context.Config.ImagesPath);
  State.TrashListViewer.setDataSourceUrls(Kernel.getLoadNodesFromTrashLink(), null);
  State.TrashListViewer.setWizardLayer(Literals.ListViewerWizard);
  State.TrashListViewer.onSelectItem = CGActionRenderTrashList.prototype.atSelectItem.bind(this, true);
  State.TrashListViewer.onUnselectItem = CGActionRenderTrashList.prototype.atSelectItem.bind(this, false);
  State.TrashListViewer.onSelectAllItems = CGActionRenderTrashList.prototype.atSelectItems.bind(this, true);
  State.TrashListViewer.onUnselectAllItems = CGActionRenderTrashList.prototype.atSelectItems.bind(this, false);
  State.TrashListViewer.onBoundItem = CGActionRenderTrashList.prototype.atBoundItem.bind(this);
  State.TrashListViewer.setState(State.getListViewerState("trashlist"));
  State.TrashListViewer.render(this.IdDOMViewerLayer);
};

CGActionRenderTrashList.prototype.step_1 = function () {

  if ((this.IdDOMViewerLayer == null) || (this.IdDOMViewerLayerOptions == null)) {
    this.terminate();
    return;
  }

  var Process = new CGProcessLoadHelperListViewer();
  Process.ReturnProcess = this;
  Process.execute();
};

CGActionRenderTrashList.prototype.step_2 = function () {
  this.createViewer();
  this.terminate();
};

//----------------------------------------------------------------------
// Empty trash
//----------------------------------------------------------------------
function CGActionEmptyTrash() {
  this.base = CGAction;
  this.base(3);
};

CGActionEmptyTrash.prototype = new CGAction;
CGActionEmptyTrash.constructor = CGActionEmptyTrash;
CommandFactory.register(CGActionEmptyTrash, null, false);

CGActionEmptyTrash.prototype.checkOption = function (ButtonResult) {
  if (ButtonResult == BUTTON_RESULT_YES) {
    this.execute();
  }
};

CGActionEmptyTrash.prototype.enabled = function () {
  return true;
};

CGActionEmptyTrash.prototype.step_1 = function () {
  Ext.MessageBox.confirm(Lang.ViewTrash.DialogEmptyTrash.Title, Lang.ViewTrash.DialogEmptyTrash.Description, CGActionEmptyTrash.prototype.checkOption.bind(this));
};

CGActionEmptyTrash.prototype.step_2 = function (ButtonResult) {
  Kernel.emptyTrash(this);
};

CGActionEmptyTrash.prototype.step_3 = function () {
  var Action = new CGActionShowTrash();
  Action.execute();

  Desktop.reportSuccess(Lang.Action.EmptyTrash.Done);
  this.terminate();
};

//----------------------------------------------------------------------
// Recover node from trash
//----------------------------------------------------------------------
function CGActionRecoverNodeFromTrash() {
  this.base = CGAction;
  this.base(3);
  this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGActionRecoverNodeFromTrash.prototype = new CGAction;
CGActionRecoverNodeFromTrash.constructor = CGActionRecoverNodeFromTrash;
CommandFactory.register(CGActionRecoverNodeFromTrash, { IdNode: 0 }, false);

CGActionRecoverNodeFromTrash.prototype.step_1 = function () {
  Ext.MessageBox.confirm(Lang.ViewTrash.DialogRecoverNodeFromTrash.Title, Lang.ViewTrash.DialogRecoverNodeFromTrash.Description, CGActionRecoverNodeFromTrash.prototype.checkOption.bind(this));
};

CGActionRecoverNodeFromTrash.prototype.step_2 = function () {
  Kernel.recoverNodeFromTrash(this, this.IdNode);
};

CGActionRecoverNodeFromTrash.prototype.step_3 = function () {
  var Node = new CGNode();
  Node.setId(this.IdNode);

  State.aMarkedNodesReferences = [this.IdNode];
  State.NodeReferenceMarkType = MarkType.RecoveredFromTrash;

  this.addRefreshTask(RefreshTaskType.RecoveredFromTrash, [Node]);

  Desktop.reportSuccess(Lang.Action.RecoverNodeFromTrash.Done);
  this.terminate();
};

//----------------------------------------------------------------------
// Recover nodes from trash
//----------------------------------------------------------------------
function CGActionRecoverNodesFromTrash() {
  this.base = CGAction;
  this.base(3);
  this.RefreshProcessClass = CGProcessRefreshDOM;
};

CGActionRecoverNodesFromTrash.prototype = new CGAction;
CGActionRecoverNodesFromTrash.constructor = CGActionRecoverNodesFromTrash;
CommandFactory.register(CGActionRecoverNodesFromTrash, null, false);

CGActionRecoverNodesFromTrash.prototype.enabled = function () {
  return (ViewTrash.getSelected().length > 0);
};

CGActionRecoverNodesFromTrash.prototype.step_1 = function () {
  Ext.MessageBox.confirm(Lang.ViewTrash.DialogRecoverNodesFromTrash.Title, Lang.ViewTrash.DialogRecoverNodesFromTrash.Description, CGActionRecoverNodesFromTrash.prototype.checkOption.bind(this));
};

CGActionRecoverNodesFromTrash.prototype.step_2 = function () {
  var aSelectedReferences = State.getSelectedTrashNodesReferences();
  var sSelectedReferences = aSelectedReferences.toString();

  if (sSelectedReferences == "") {
    Desktop.reportWarning(Lang.Action.RecoverNodesFromTrash.NoSelectedReferences);
    this.terminate();
    return;
  }

  Kernel.recoverNodesFromTrash(this, sSelectedReferences);
};

CGActionRecoverNodesFromTrash.prototype.step_3 = function () {
  var aIdNodes = State.getSelectedTrashNodesReferences();
  var aNodes = new Array();

  for (var index in aIdNodes) {
    if (isFunction(aIdNodes[index])) continue;
    var Node = new CGNode();
    Node.setId(aIdNodes[index]);
    aNodes.push(Node);
  }

  State.aMarkedNodesReferences = aIdNodes;
  State.NodeReferenceMarkType = MarkType.RecoveredFromTrash;

  this.addRefreshTask(RefreshTaskType.RecoveredFromTrash, aNodes);

  Desktop.reportSuccess(Lang.Action.RecoverNodesFromTrash.Done);
  this.terminate();
};

//----------------------------------------------------------------------
// Select trash nodes
//----------------------------------------------------------------------
function CGActionSelectTrashNodes() {
  this.base = CGAction;
  this.base(1);
};

CGActionSelectTrashNodes.prototype = new CGAction;
CGActionSelectTrashNodes.constructor = CGActionSelectTrashNodes;
CommandFactory.register(CGActionSelectTrashNodes, { Type: 0 }, false);

CGActionSelectTrashNodes.prototype.step_1 = function () {
  if (this.Type == "all") ViewTrash.selectAll();
  else if (this.Type == "invert") ViewTrash.selectInvert();
  else ViewTrash.selectNone();
  this.terminate();
};