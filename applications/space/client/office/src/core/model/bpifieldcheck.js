function CGBPIFieldCheck(DOMField) {
  this.base = CGBPIField;
  this.base(DOMField);
};

CGBPIFieldCheck.prototype = new CGBPIField;

//---------------------------------------------------------------------
CGBPIFieldCheck.prototype.isChecked = function () {
  var Indicator = this.getIndicator(CGIndicator.CHECKED);
  return (Indicator) ? ((Indicator.getValue() == "true") ? true : false) : "";
};

//---------------------------------------------------------------------
CGBPIFieldCheck.prototype.getValue = function () {
  var Indicator = this.getIndicator(CGIndicator.VALUE);
  return (Indicator) ? Indicator.getValue() : "";
};

//---------------------------------------------------------------------
CGBPIFieldCheck.prototype.setValue = function (bChecked, sValue) {
  var Attribute = new CGAttribute();

  Attribute.code = this.getCode();
  Attribute.addIndicatorByValue(CGIndicator.CHECKED, 1, (bChecked) ? "true" : "false");
  Attribute.addIndicatorByValue(CGIndicator.VALUE, 2, sValue);

  this.DOMField.setData(Attribute.serialize());
};

//---------------------------------------------------------------------
CGBPIFieldCheck.prototype.check = function () {
  this.setValue(true, this.getValue());
};

//---------------------------------------------------------------------
CGBPIFieldCheck.prototype.uncheck = function () {
  this.setValue(false, this.getValue());
};

//---------------------------------------------------------------------
CGBPIFieldCheck.prototype.toggle = function () {
  this.setValue(!this.isChecked(), this.getValue());
};

//*********************************************************************
BPIFieldsFactory.register(FIELD_TYPE_CHECK, "CGBPIFieldCheck");