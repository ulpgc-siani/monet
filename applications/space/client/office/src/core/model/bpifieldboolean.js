function CGBPIFieldBoolean(DOMField) {
  this.base = CGBPIField;
  this.base(DOMField);
};

CGBPIFieldBoolean.prototype = new CGBPIField;

//---------------------------------------------------------------------
CGBPIFieldBoolean.prototype.isTrue = function () {
  return this.getValue();
};

//---------------------------------------------------------------------
CGBPIFieldBoolean.prototype.getValue = function () {
  var Attribute = new CGAttribute();
  var Indicator;

  Attribute.unserialize(this.DOMField.getData());
  Indicator = Attribute.getIndicator(CGIndicator.CODE);

  if (Indicator == null) return "false";

  return (Indicator.getValue() == "true") ? "true" : "false";
};

//---------------------------------------------------------------------
CGBPIFieldBoolean.prototype.setValue = function (bValue) {
  var Attribute = new CGAttribute();

  Attribute.code = this.getCode();
  Attribute.addIndicatorByValue(CGIndicator.CODE, 1, (bValue) ? "true" : "false");
  Attribute.addIndicatorByValue(CGIndicator.VALUE, 2, (bValue) ? "X" : "");

  this.DOMField.setData(Attribute.serialize());
};

//---------------------------------------------------------------------
CGBPIFieldBoolean.prototype.toggle = function () {
  this.setValue(!this.getValue());
};

//*********************************************************************
BPIFieldsFactory.register(FIELD_TYPE_BOOLEAN, "CGBPIFieldBoolean");