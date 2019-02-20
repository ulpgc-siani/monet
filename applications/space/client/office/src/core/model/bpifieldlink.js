function CGBPIFieldLink(DOMField) {
  this.base = CGBPIField;
  this.base(DOMField);
};

CGBPIFieldLink.prototype = new CGBPIField;

//---------------------------------------------------------------------
CGBPIFieldLink.prototype.getValueCode = function () {
  var Attribute = new CGAttribute();
  var IndicatorCode, IndicatorNodeLink;
  var Code = "";

  Attribute.unserialize(this.DOMField.getData());

  IndicatorCode = Attribute.getIndicator(CGIndicator.CODE);
  if ((IndicatorCode != null) && (IndicatorCode.getValue() != "")) Code = IndicatorCode.getValue();
  else {
    IndicatorNodeLink = Attribute.getIndicator(CGIndicator.NODE_LINK);
    if (IndicatorNodeLink) Code = IndicatorNodeLink.getValue();
  }

  return Code;
};

//---------------------------------------------------------------------
CGBPIFieldLink.prototype.getValueAndCode = function () {
  var Attribute = new CGAttribute();
  var IndicatorCode, IndicatorOther, IndicatorValue;
  var Result = new Object();

  Attribute.unserialize(this.DOMField.getData());

  IndicatorCode = Attribute.getIndicator(CGIndicator.CODE);
  if ((IndicatorCode != null) && (IndicatorCode.getValue() != "")) Result.Code = IndicatorCode.getValue();
  else {
    IndicatorNodeLink = Attribute.getIndicator(CGIndicator.NODE_LINK);
    if (IndicatorNodeLink) Result.Code = IndicatorNodeLink.getValue();
    else Result.Code = "";
  }

  IndicatorValue = Attribute.getIndicator(CGIndicator.VALUE);
  if (IndicatorValue != null) Result.Value = IndicatorValue.getValue();

  return Result;
};

//---------------------------------------------------------------------
CGBPIFieldLink.prototype.setValue = function (Code, sValue) {
  var Attribute = new CGAttribute();

  Attribute.code = this.getCode();
  Attribute.addIndicatorByValue(CGIndicator.CODE, 1, Code);
  Attribute.addIndicatorByValue(CGIndicator.VALUE, 2, sValue);
  Attribute.addIndicatorByValue(CGIndicator.NODE_LINK, 3, Code);

  this.DOMField.setData(Attribute.serialize());
};

//---------------------------------------------------------------------
CGBPIFieldLink.prototype.getLinkId = function () {
  return this.getValueCode();
};

//---------------------------------------------------------------------
CGBPIFieldLink.prototype.setParameter = function (Code, sValue) {
  this.DOMField.setParameter(Code, sValue);
};

//*********************************************************************
BPIFieldsFactory.register(FIELD_TYPE_LINK, "CGBPIFieldLink");