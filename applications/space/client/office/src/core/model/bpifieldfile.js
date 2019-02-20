function CGBPIFieldFile(DOMField) {
  this.base = CGBPIField;
  this.base(DOMField);
};

CGBPIFieldFile.prototype = new CGBPIField;

//---------------------------------------------------------------------
CGBPIFieldFile.prototype.setValue = function (sValue) {
  var Attribute = new CGAttribute();

  Attribute.code = this.getCode();
  Attribute.addIndicatorByValue(CGIndicator.VALUE, 1, sValue);
  Attribute.addIndicatorByValue(CGIndicator.FORMAT, 2, getFileExtension(sValue).toUpperCase());

  this.DOMField.setData(Attribute.serialize());
};

//*********************************************************************
BPIFieldsFactory.register(FIELD_TYPE_FILE, "CGBPIFieldFile");