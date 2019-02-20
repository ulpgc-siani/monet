function CGNode() {
  this.Id = null;
  this.IdParent = null;
  this.Code = "";
  this.bIsPrototype = false;
  this.sContent = null;
  this.Reference = null;
  this.NodeList = new CGNodeList();
  this.AttributeList = new CGAttributeList();
  this.bLoaded = false;
};

//---------------------------------------------------------------------
CGNode.prototype.getId = function () {
  return this.Id;
};

//---------------------------------------------------------------------
CGNode.prototype.setId = function (Id) {
  this.Id = Id;
};

//---------------------------------------------------------------------
CGNode.prototype.setCode = function (Code) {
  this.Code = Code;
};

//---------------------------------------------------------------------
CGNode.prototype.getIdParent = function () {
  return this.IdParent;
};

//---------------------------------------------------------------------
CGNode.prototype.isPrototype = function () {
  return this.bIsPrototype;
};

//---------------------------------------------------------------------
CGNode.prototype.getLabel = function () {
  return this.Reference.getLabel();
};

//---------------------------------------------------------------------
CGNode.prototype.setLabel = function (sLabel) {
  this.Reference.setLabel(sLabel);
};

//---------------------------------------------------------------------
CGNode.prototype.getContent = function () {
  return this.sContent;
};

//---------------------------------------------------------------------
CGNode.prototype.setContent = function (sContent) {
  this.sContent = sContent;
  this.bLoaded = true;
};

//---------------------------------------------------------------------
CGNode.prototype.getReference = function () {
  return this.Reference;
};

//---------------------------------------------------------------------
CGNode.prototype.setReference = function (Reference) {
  this.Reference = Reference;
};

//---------------------------------------------------------------------
CGNode.prototype.isLoaded = function () {
  return this.bLoaded;
};

//---------------------------------------------------------------------
CGNode.prototype.setLoaded = function (bValue) {
  this.bLoaded = bValue;
};

//---------------------------------------------------------------------
CGNode.prototype.toArray = function () {
  return {
    Id: this.Id,
    sLabel: this.Reference.getLabel(),
    sDescription: this.Reference.getDescription(),
    dtDeleted: getFormattedDate(parseServerDate(this.Reference.getDeleteDate()), Context.Config.Language)
  };
};

//---------------------------------------------------------------------
CGNode.prototype.unserializeFromJSON = function (ItemStructure) {
  this.Id = ItemStructure.id;
  this.IdParent = ItemStructure.idparent;
  this.Code = ItemStructure.code;
  this.bIsPrototype = ItemStructure.isPrototype;
  this.sContent = ItemStructure.content;
  if (ItemStructure.reference) this.Reference = new CGNodeReference(ItemStructure.reference);
  if (ItemStructure.nodelist) {
    this.NodeList = new CGNodeList();
    this.NodeList.setCount(ItemStructure.nodelist.count);
  }
  this.AttributeList = new CGAttributeList();
};

CGNode.prototype.copyFromFields = function (aFields) {
  this.AttributeList.copyFromFields(aFields);
};

//---------------------------------------------------------------------
CGNode.prototype.serialize = function (iOrder) {
  var sResult = "<node id=\"" + this.Id + "\" code=\"" + this.Code + "\">";
  sResult += this.AttributeList.serialize();
  sResult += this.NodeList.serialize();
  sResult += "</node>";
  return sResult;
};

//---------------------------------------------------------------------
CGNode.prototype.serializeWithData = function (sData) {
  return "<node id=\"" + this.Id + "\" code=\"" + this.Code + "\">" + sData + "</node>";
};

//---------------------------------------------------------------------
CGNode.prototype.unserialize = function (serialized) {
  var jsonData = Ext.util.JSON.decode(serialized);
  this.unserializeFromJSON(jsonData);
};