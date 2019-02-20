CGNodeTypeList = function () {
  this.aItems = new Array();
  this.aIndexes = new Array();
  this.Default = "";
};

CGNodeTypeList.prototype.get = function () {
  return this.aItems;
};

CGNodeTypeList.prototype.getNodeType = function (CodeNodeType) {
  if (this.aIndexes[CodeNodeType] == null) return false;
  return this.aItems[this.aIndexes[CodeNodeType]];
};

CGNodeTypeList.prototype.add = function (NodeType) {
  this.aItems.push(NodeType);
  this.aIndexes[NodeType.Code] = this.aItems.length - 1;
};

CGNodeTypeList.prototype.setDefault = function (Code) {
  this.Default = Code;
};

CGNodeTypeList.prototype.getDefault = function () {
  if (this.aItems[this.aIndexes[this.Default]] == null) return false;
  return this.aItems[this.aIndexes[this.Default]];
};