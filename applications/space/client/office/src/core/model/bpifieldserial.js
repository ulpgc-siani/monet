function CGBPIFieldSerial(DOMField) {
  this.base = CGBPIField;
  this.base(DOMField);
};

CGBPIFieldSerial.prototype = new CGBPIField;

//*********************************************************************
BPIFieldsFactory.register(FIELD_TYPE_SERIAL, "CGBPIFieldSerial");