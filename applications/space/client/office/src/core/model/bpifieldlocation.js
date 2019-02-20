function CGBPIFieldLocation(DOMField) {
  this.base = CGBPIField;
  this.base(DOMField);
};

CGBPIFieldLocation.prototype = new CGBPIField;

//*********************************************************************
BPIFieldsFactory.register(FIELD_TYPE_LOCATION, "CGBPIFieldLocation");