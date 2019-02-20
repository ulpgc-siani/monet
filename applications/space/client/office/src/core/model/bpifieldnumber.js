function CGBPIFieldNumber(DOMField) {
  this.base = CGBPIField;
  this.base(DOMField);
};

CGBPIFieldNumber.prototype = new CGBPIField;

//---------------------------------------------------------------------
CGBPIFieldNumber.prototype.getRange = function () {
  return this.DOMField.getRange();
};

//---------------------------------------------------------------------
CGBPIFieldNumber.prototype.getIncrements = function () {
  return this.DOMField.getIncrements();
};

//---------------------------------------------------------------------
CGBPIFieldNumber.prototype.getDefaultMetric = function () {
  return this.DOMField.getDefaultMetric();
};

//---------------------------------------------------------------------
CGBPIFieldNumber.prototype.getFormat = function () {
  return this.DOMField.getFormat();
};

//---------------------------------------------------------------------
CGBPIFieldNumber.prototype.getValue = function () {
  var Attribute = new CGAttribute();
  var Indicator, sValue;

  Attribute.unserialize(this.DOMField.getData());
  Indicator = Attribute.getIndicator(CGIndicator.VALUE);

  if (Indicator) {
    sValue = Indicator.getValue().replace(/\./g, EMPTY);
    return parseFloat(sValue.replace(COMMA, DOT));
  }

  return 0;
};

//---------------------------------------------------------------------
CGBPIFieldNumber.prototype.setValue = function (iValue) {
  var Attribute = new CGAttribute();
  var sValue = this.DOMField.format(iValue + '');
  if (!sValue) return false;

  Attribute.code = this.getCode();
  Attribute.addIndicatorByValue(CGIndicator.VALUE, 1, iValue);
  Attribute.addIndicatorByValue(CGIndicator.METRIC, 2, this.getDefaultMetric().value);

  this.DOMField.setData(Attribute.serialize());
};

//---------------------------------------------------------------------
CGBPIFieldNumber.prototype.getIntegerPart = function () {
  var sNumber = this.getValue() + '';
  var iPos;

  iPos = sNumber.indexOf(COMMA);
  if (iPos == -1) iPos = sNumber.indexOf(DOT);
  if (iPos == -1) return sNumber;

  return sNumber.substr(0, iPos);
};

//---------------------------------------------------------------------
CGBPIFieldNumber.prototype.getDecimalPart = function () {
  var sNumber = this.getValue() + '';
  var iPos;

  iPos = sNumber.indexOf(COMMA);
  if (iPos == -1) iPos = sNumber.indexOf(DOT);
  if (iPos == -1) return "";

  return sNumber.substr(iPos + 1, sNumber.length - iPos + 1);
};

//---------------------------------------------------------------------
CGBPIFieldNumber.prototype.isEven = function () {
  var iIntegerPart;

  try {
    iIntegerPart = parseInt(this.getIntegerPart());
  }
  catch (e) {
    return false;
  }

  return ((iIntegerPart % 2) == 0);
};

//---------------------------------------------------------------------
CGBPIFieldNumber.prototype.isValidRange = function () {
  return this.DOMField.isValidRange(this.getValue());
};

//*********************************************************************
BPIFieldsFactory.register(FIELD_TYPE_NUMBER, "CGBPIFieldNumber");