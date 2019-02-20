CGNodeTypeViewList = function () {
  this.aItems = new Array();
  this.aIndexes = new Array();
  this.sDefault = "";
};

CGNodeTypeViewList.prototype.get = function () {
  return this.aItems;
};

CGNodeTypeViewList.prototype.getNodeView = function (CodeNodeView) {
  if (this.aIndexes[CodeNodeView] == null) return false;
  return this.aItems[this.aIndexes[CodeNodeView]];
};

CGNodeTypeViewList.prototype.add = function (NodeView) {
  this.aItems.push(NodeView);
  this.aIndexes[NodeView.sName] = this.aItems.length - 1;
};

CGNodeTypeViewList.prototype.setDefault = function (sNodeView) {
  this.sDefault = sNodeView;
};

CGNodeTypeViewList.prototype.getDefault = function () {
  if (this.aItems[this.aIndexes[this.sDefault]] == null) return false;
  return this.aItems[this.aIndexes[this.sDefault]];
};