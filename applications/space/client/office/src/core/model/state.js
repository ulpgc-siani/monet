State = {};
State.View = null;
State.discardNode = false;
State.LastView = null;
State.LastObject = {};
State.LastObject.Id = null;
State.LastObject.Mode = null;
State.aSelectedNodesReferences = [];
State.aSelectedTrashNodesReferences = [];
State.aMarkedNodesReferences = [];
State.NodeReferenceMarkType = null;
State.Searching = false;
State.LastSearch = {};
State.logout = false;
State.aRefreshingTasks = [];
State.LastCommand = {};
State.ListViewerStates = {};
State.CurrentView = null;
State.isShowingPrototype = false;
State.RoleDefinitionList = null;
State.PartnerList = null;
State.SetsContext = {};
State.NodesStates = {};
State.ActiveFurniture = null;
State.MainNodes = {};

State.getSelectedNodesReferences = function (IdNode) {
  if (!State.aSelectedNodesReferences[IdNode]) return [];
  return State.aSelectedNodesReferences[IdNode];
};

State.addNodeReferenceToSelection = function (IdNode, IdNodeReference) {
  if (!State.aSelectedNodesReferences[IdNode]) State.aSelectedNodesReferences[IdNode] = [];
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
  State.aSelectedNodesReferences[IdNode] = [];
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
  State.aSelectedTrashNodesReferences = [];
};

State.registerListViewerState = function (Id, NewState) {
  State.ListViewerStates[Id] = NewState;
};

State.getListViewerState = function (Id) {
  return State.ListViewerStates[Id];
};

State.getListViewerFilters = function (Id) {
  var state = State.ListViewerStates[Id];
  if (state == null) return null;

  var result = {};
  result["query"] = state.Filter;
  result["sortsby"] = "";
  result["groupsby"] = "";
  for (var i = 0; i < state.Sorts.length; i++)
    result["sortsby"] += state.Sorts[i].Code + MONET_FILTER_SEPARATOR + state.Sorts[i].Mode + MONET_FILTERS_SEPARATOR;
  for (var i = 0; i < state.Groups.length; i++)
    result["groupsby"] += state.Groups[i].Code + MONET_FILTER_SEPARATOR + state.Groups[i].Value + MONET_FILTERS_SEPARATOR;

  if (result["sortsby"].length > 0)
    result["sortsby"] = result["sortsby"].substring(0, result["sortsby"].length - MONET_FILTERS_SEPARATOR.length);
  if (result["groupsby"].length > 0)
    result["groupsby"] = result["groupsby"].substring(0, result["groupsby"].length - MONET_FILTERS_SEPARATOR.length);

  return result;
};
