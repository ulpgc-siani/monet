State = new Object();
State.View = null;
State.discardNode = false;
State.LastView = null;
State.LastObject = new Object();
State.LastObject.Id = null;
State.LastObject.Mode = null;
State.aSelectedNodesReferences = new Array();
State.aSelectedTrashNodesReferences = new Array();
State.aMarkedNodesReferences = new Array();
State.NodeReferenceMarkType = null;
State.Searching = false;
State.LastSearch = new Object();
State.logout = false;
State.aRefreshingTasks = new Array();
State.LastCommand = new Object();
State.ListViewerStates = new Object();
State.CurrentView = null;
State.isShowingPrototype = false;
State.RoleDefinitionList = null;
State.PartnerList = null;
State.NodesStates = new Object();
State.ActiveFurniture = null;
State.MainNodes = {};

State.getSelectedNodesReferences = function (IdNode) {
  if (!State.aSelectedNodesReferences[IdNode]) return new Array();
  return State.aSelectedNodesReferences[IdNode];
};

State.addNodeReferenceToSelection = function (IdNode, IdNodeReference) {
  if (!State.aSelectedNodesReferences[IdNode]) State.aSelectedNodesReferences[IdNode] = new Array();
  State.aSelectedNodesReferences[IdNode][IdNodeReference] = IdNodeReference;
};

State.deleteSelectedNodeReference = function (IdNode, IdNodeReference) {
  if (!State.aSelectedNodesReferences[IdNode]) return;
  delete State.aSelectedNodesReferences[IdNode][IdNodeReference];
};

State.addNodesReferencesToSelection = function (IdNode, aNodesReferences) {
  for (var IdNodeReference in aNodesReferences) {
    if (isFunction(aNodesReferences[IdNodeReference])) continue;
    State.addNodeReferenceToSelection(IdNode, IdNodeReference);
  }
};

State.deleteSelectedNodesReferences = function (IdNode) {
  State.aSelectedNodesReferences[IdNode] = new Array();
};

State.getSelectedTrashNodesReferences = function (IdNode) {
  return State.aSelectedTrashNodesReferences;
};

State.addNodeReferenceToTrashSelection = function (IdNodeReference) {
  State.aSelectedTrashNodesReferences[IdNodeReference] = IdNodeReference;
};

State.deleteSelectedNodeReferenceFromTrash = function (IdNodeReference) {
  delete State.aSelectedTrashNodesReferences[IdNodeReference];
};

State.addNodesReferencesToTrashSelection = function (aNodesReferences) {
  for (var IdNodeReference in aNodesReferences) {
    if (isFunction(aNodesReferences[IdNodeReference])) continue;
    State.addNodeReferenceToTrashSelection(IdNodeReference);
  }
};

State.deleteSelectedNodesReferencesFromTrash = function () {
  State.aSelectedTrashNodesReferences = new Array();
};

State.registerListViewerState = function (Id, NewState) {
  State.ListViewerStates[Id] = NewState;
};

State.getListViewerState = function (Id) {
  return State.ListViewerStates[Id];
};
