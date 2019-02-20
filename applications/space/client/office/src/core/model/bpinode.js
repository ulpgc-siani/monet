function CGBPINode(Node, DOMNode) {
  this.CodeDefinition = Node.Code;
  this.IdParent = Node.getIdParent();
  this.BPIReference = new CGBPINodeReference(Node.getReference());
  this.DOMNode = DOMNode;
};

//---------------------------------------------------------------------
// PRIVATE
CGBPINode.prototype.getFieldCode = function (sName) {
  var Definition = Extension.getDefinition(this.CodeDefinition);
  return Definition.FieldsNames[sName];
};

//---------------------------------------------------------------------
// PUBLIC
CGBPINode.prototype.getId = function () {
  return this.DOMNode.getIdNode();
};

//---------------------------------------------------------------------
CGBPINode.prototype.getIdParent = function () {
  return this.IdParent;
};

//---------------------------------------------------------------------
CGBPINode.prototype.getParent = function () {
  var ParentNode = NodesCache.get(this.IdParent);
  var DOMParent = this.DOMNode.getParentNode();
  return new CGBPINode(ParentNode, DOMParent);
};

//---------------------------------------------------------------------
CGBPINode.prototype.getReference = function () {
  return this.BPIReference;
};

//---------------------------------------------------------------------
CGBPINode.prototype.getLabel = function () {
  return this.BPIReference.getLabel();
};

//---------------------------------------------------------------------
CGBPINode.prototype.getDescription = function () {
  return this.BPIReference.getDescription();
};

//---------------------------------------------------------------------
CGBPINode.prototype.getChildNodeId = function (code) {
  return this.DOMNode.getChildNodeId();
};

//---------------------------------------------------------------------
CGBPINode.prototype.getFields = function (sName) {
  var BPIFields = new Array(), Code, DOMFields;

  Code = this.getFieldCode(sName);
  if (Code == null) Code = sName;

  DOMFields = this.DOMNode.getFieldsByCode(Code);

  for (var i = 0; i < DOMFields.length; i++)
    BPIFields.push(BPIFieldsFactory.get(DOMFields[i].getType(), DOMFields[i], this.CodeDefinition));

  return BPIFields;
};

//---------------------------------------------------------------------
CGBPINode.prototype.getField = function (sName) {
  var Code, DOMField, BPIField;

  Code = this.getFieldCode(sName);
  if (Code == null) Code = sName;

  DOMField = this.DOMNode.getField(Code);
  if (!DOMField) return false;

  BPIField = BPIFieldsFactory.get(DOMField.getType(), DOMField, this.CodeDefinition);

  return (BPIField) ? BPIField : false;
};

//---------------------------------------------------------------------
CGBPINode.prototype.isFieldEqualsTo = function (Code, Value) {
  var DOMField = this.DOMNode.getField(Code);
  if (!DOMField) return false;
  return DOMField.isEqualsTo(Value);
};

//---------------------------------------------------------------------
CGBPINode.prototype.isFieldGT = function (Code, Value) {
  var BPIField = this.getField(Code);
  if (!BPIField) return false;
  return BPIField.getValue() > Value;
};

//---------------------------------------------------------------------
CGBPINode.prototype.isFieldGE = function (Code, Value) {
  var BPIField = this.getField(Code);
  if (!BPIField) return false;
  return BPIField.getValue() >= Value;
};

//---------------------------------------------------------------------
CGBPINode.prototype.isFieldLT = function (Code, Value) {
  var BPIField = this.getField(Code);
  if (!BPIField) return false;
  return BPIField.getValue() < Value;
};

//---------------------------------------------------------------------
CGBPINode.prototype.isFieldLE = function (Code, Value) {
  var BPIField = this.getField(Code);
  if (!BPIField) return false;
  return BPIField.getValue() <= Value;
};

//---------------------------------------------------------------------
CGBPINode.prototype.isEditable = function () {
  return this.DOMNode.isEditable();
};

//---------------------------------------------------------------------
CGBPINode.prototype.showView = function (Code) {
  var DOMContainer, ViewNode = Desktop.Main.Center.Body.getView(VIEW_NODE, this.DOMNode.IdView);
  if (ViewNode.ViewContainer != null) DOMContainer = ViewNode.ViewContainer.getDOM();
  else DOMContainer = this.DOMNode;
  DOMContainer.showTab(Code);
};

//---------------------------------------------------------------------
CGBPINode.prototype.hideView = function (Code) {
  var DOMContainer, ViewNode = Desktop.Main.Center.Body.getView(VIEW_NODE, this.DOMNode.IdView);
  if (ViewNode.ViewContainer != null) DOMContainer = ViewNode.ViewContainer.getDOM();
  else DOMContainer = this.DOMNode;
  DOMContainer.hideTab(Code);
};

//---------------------------------------------------------------------
CGBPINode.prototype.lockView = function (Code) {
  this.DOMNode.lockTab(Code);
};

//---------------------------------------------------------------------
CGBPINode.prototype.unLockView = function (Code) {
  this.DOMNode.unLockTab(Code);
};

//---------------------------------------------------------------------
CGBPINode.prototype.showOperation = function (sName) {
  this.DOMNode.showOperation(sName);
};

//---------------------------------------------------------------------
CGBPINode.prototype.hideOperation = function (sName) {
  this.DOMNode.hideOperation(sName);
};
