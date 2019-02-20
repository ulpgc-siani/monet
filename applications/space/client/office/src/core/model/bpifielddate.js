function CGBPIFieldDate(DOMField) {
  this.base = CGBPIField;
  this.base(DOMField);
};

CGBPIFieldDate.prototype = new CGBPIField;

//---------------------------------------------------------------------
CGBPIFieldDate.prototype.getInternalDate = function () {
  var sDate = this.getInternalValue();
  return Date.parseDate(sDate, DATE_FORMAT_INTERNAL);
};

//---------------------------------------------------------------------
CGBPIFieldDate.prototype.getInternalValue = function () {
  var Indicator = this.getIndicator(CGIndicator.INTERNAL);
  return (Indicator) ? Indicator.getValue() : "";
};

//---------------------------------------------------------------------
CGBPIFieldDate.prototype.setValue = function (sValue) {
  var Attribute = new CGAttribute();
  var Pattern = Date.getPattern(this.DOMField.getFormat());
  var sInternalDate, dtInternalDate;

  dtInternalDate = Date.parseDate(sValue, Pattern);
  sInternalDate = (dtInternalDate) ? dtInternalDate.format(DATE_FORMAT_INTERNAL) : "";

  Attribute.code = this.getCode();
  Attribute.addIndicatorByValue(CGIndicator.VALUE, -1, sValue);
  Attribute.addIndicatorByValue(CGIndicator.INTERNAL, -1, sInternalDate);

  this.DOMField.setData(Attribute.serialize());
};

//---------------------------------------------------------------------
CGBPIFieldDate.prototype.format = function (SelectType, sInternalDate) {
  var dtInternalDate = Date.parseDate(sInternalDate, DATE_FORMAT_INTERNAL);
  var Pattern = Date.getPattern(SelectType);
  return dtInternalDate.format(Pattern);
};

//*********************************************************************
BPIFieldsFactory.register(FIELD_TYPE_DATE, "CGBPIFieldDate");