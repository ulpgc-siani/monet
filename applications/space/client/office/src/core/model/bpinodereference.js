function CGBPINodeReference(NodeReference) {
  this.NodeReference = NodeReference;
};

//---------------------------------------------------------------------
CGBPINodeReference.prototype.getLabel = function () {
  return this.NodeReference.getLabel();
};

//---------------------------------------------------------------------
CGBPINodeReference.prototype.setLabel = function (sValue) {
  return this.NodeReference.setLabel(sValue);
};

//---------------------------------------------------------------------
CGBPINodeReference.prototype.getDescription = function () {
  return this.NodeReference.getDescription();
};

//---------------------------------------------------------------------
CGBPINodeReference.prototype.setDescription = function (sValue) {
  return this.NodeReference.setDescription(sValue);
};

//---------------------------------------------------------------------
CGBPINodeReference.prototype.getAttributeValue = function (code) {
  return this.NodeReference.getAttributeValue(code);
};

//---------------------------------------------------------------------
CGBPINodeReference.prototype.setAttributeValue = function (code, sValue) {
  return this.NodeReference.setAttributeValue(code, sValue);
};