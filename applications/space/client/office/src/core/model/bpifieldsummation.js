function CGBPIFieldSummation(DOMField) {
  this.base = CGBPIField;
  this.base(DOMField);
};

CGBPIFieldSummation.prototype = new CGBPIField;

//*********************************************************************
BPIFieldsFactory.register(FIELD_TYPE_SUMMATION, "CGBPIFieldSummation");