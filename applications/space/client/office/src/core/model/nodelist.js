function CGNodeList() {
  this.aNodes = new Array;
  this.aIterNodes = new Array;
  this.bDirty = false;
  this.iCount = 0;
};

CGNodeList.prototype.getNodes = function () {
  return this.aNodes;
};

CGNodeList.prototype.getNode = function (Id) {
  return this.aNodes[Id];
};

CGNodeList.prototype.existNode = function (Id) {
  return (this.aNodes[Id] != null);
};

CGNodeList.prototype.isNodeLoaded = function (Id) {
  eNode = Extension.getModelNodeDOM(Id);
  if (!eNode.isLoaded) return false;
  return eNode.isLoaded();
//  if (this.aNodes[Id] == null) return false;
//  return (this.aNodes[Id].isLoaded());
};

CGNodeList.prototype.addNode = function (Node) {
  if (Node.getId() == null) Node.setId(this.aNodes.length);

  this.aNodes[Node.getId()] = Node;
  this.aIterNodes.push(Node);
  this.iCount++;

  return Node;
};

CGNodeList.prototype.deleteNode = function (IdNode) {
  if (!this.existNode(IdNode)) return false;

  delete this.aNodes[IdNode];
  this.iCount--;
};

CGNodeList.prototype.clearNodes = function () {
  this.aNodes = new Array();
  this.aIterNodes = new Array();
  this.iCount = 0;
};

CGNodeList.prototype.isDirty = function () {
  return this.bDirty;
};

CGNodeList.prototype.getCount = function () {
  return this.iCount;
};

CGNodeList.prototype.setCount = function (iCount) {
  this.iCount = iCount;
};

CGNodeList.prototype.newNodes = function (aJsonItems) {

  for (var i = 0; i < aJsonItems.length; i++) {
    Item = aJsonItems[i];
    Node = new CGNode();
    Node.unserializeFromJSON(Item);
    this.addNode(Node);
  }

  return this.aNodes;
};

CGNodeList.prototype.clone = function () {
  var NewNodeList = new CGNodeList();
  var j = 0;

  for (var i in this.aNodes) {
    if (isFunction(this.aNodes[i])) continue;
    NewNodeList.aNodes[i] = this.aNodes[i].PartialClone();
    NewNodeList.aIterNodes[j] = NewNodeList.aNodes[i];
    j++;
  }

  return NewNodeList;
};

CGNodeList.prototype.serialize = function () {
  var sNodes = "";
  var iPos = 0;

  for (var index in this.aNodes) {
    if (isFunction(this.aNodes[index])) continue;
    sNodes += this.aNodes[index].serialize(iPos);
    iPos++;
  }

  if (sNodes == "") return "";

  return "<nodelist>" + sNodes + "</nodelist>";
};

CGNodeList.prototype.serializeWithData = function (data) {
  return "<nodelist>" + data + "</nodelist>";
};

CGNodeList.prototype.unserialize = function (serialized) {
  var jsonData = Ext.util.JSON.decode(serialized);
  this.iCount = jsonData.count;
};