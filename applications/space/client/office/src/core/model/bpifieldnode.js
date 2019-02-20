function CGBPIFieldNode(DOMField) {
  this.base = CGBPIField;
  this.base(DOMField);
};

CGBPIFieldNode.prototype = new CGBPIField;

//*********************************************************************
BPIFieldsFactory.register(FIELD_TYPE_NODE, "CGBPIFieldNode");