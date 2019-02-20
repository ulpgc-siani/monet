function CGBPIFieldSelect(DOMField) {
  this.base = CGBPIField;
  this.base(DOMField);
};

CGBPIFieldSelect.prototype = new CGBPIField;

//---------------------------------------------------------------------
CGBPIFieldSelect.prototype.allowOthers = function () {
  return this.DOMField.allowOthers();
};

//---------------------------------------------------------------------
CGBPIFieldSelect.prototype.getCodeOnOthers = function () {
  return this.DOMField.getCodeOnOthers();
};

//---------------------------------------------------------------------
CGBPIFieldSelect.prototype.getValue = function () {
  var Attribute = new CGAttribute();
  var IndicatorCode, Indicator;
  var sFieldData = this.DOMField.getData();

  if (sFieldData == "") return "";
  Attribute.unserialize(sFieldData);

  IndicatorCode = Attribute.getIndicator(CGIndicator.CODE);
  Indicator = (IndicatorCode.getValue() == this.getCodeOnOthers()) ? Attribute.getIndicator(CGIndicator.OTHER) : Attribute.getIndicator(CGIndicator.VALUE);

  return (Indicator) ? Indicator.getValue() : "";
};

//---------------------------------------------------------------------
CGBPIFieldSelect.prototype.getValueCode = function () {
  var Indicator = this.getIndicator(CGIndicator.CODE);
  return (Indicator) ? Indicator.getValue() : "";
};

//---------------------------------------------------------------------
CGBPIFieldSelect.prototype.getValueAndCode = function () {
  var Attribute = new CGAttribute();
  var IndicatorCode, Indicator;
  var Result = new Object();

  Attribute.unserialize(this.DOMField.getData());

  IndicatorCode = Attribute.getIndicator(CGIndicator.CODE);
  if (IndicatorCode != null) Result.Code = IndicatorCode.getValue();

  Indicator = (IndicatorCode.getValue() == this.getCodeOnOthers()) ? Attribute.getIndicator(CGIndicator.OTHER) : Attribute.getIndicator(CGIndicator.VALUE);
  if (Indicator) Result.Value = Indicator.getValue();
  else Result.Value = "";

  return Result;
};

//---------------------------------------------------------------------
CGBPIFieldSelect.prototype.setValue = function (Code, sValue) {
  var Attribute = new CGAttribute();

  Attribute.code = this.getCode();

  Attribute.addIndicatorByValue(CGIndicator.CODE, 1, Code);

  if (Code == this.getCodeOnOthers()) Attribute.addIndicatorByValue(CGIndicator.OTHER, 2, sValue);
  else Attribute.addIndicatorByValue(CGIndicator.VALUE, 2, sValue);

  this.DOMField.setData(Attribute.serialize());
};

//*********************************************************************
BPIFieldsFactory.register(FIELD_TYPE_SELECT, "CGBPIFieldSelect");